package org.ngs.bigx.minecraft.quests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.dictionary.objects.game.properties.Stage;
import org.ngs.bigx.dictionary.objects.game.properties.StageSettings;
import org.ngs.bigx.dictionary.protocol.Specification.GameTagType;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiLeaderBoard;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.GuiStats;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiome;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.fire.TerrainBiomeFire;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventAttack;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemUse;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDark;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.utility.NpcCommand;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Start;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNpcCrystal;

public class QuestTaskChasing extends QuestTask implements IQuestEventAttack, IQuestEventItemUse {
	public enum QuestChaseTypeEnum { REGULAR, FIRE, ICE, AIR, LIFE };

	protected QuestChaseTypeEnum questChaseType = QuestChaseTypeEnum.REGULAR;
	
	private String id = "QUEST_TASK_CHASE";
	
	private float playerQuestPitch, playerQuestYaw;
	
	protected long questTimeStamp = 0;
	 
	protected long lastCountdownTickTimestamp = 0;
	protected int countdown = 11;
	
	protected int time = 0;
	protected double elapsedTime = 0;
	protected double pausedTime = 0;
	protected double lastTickTime = 0;
	protected int lastTickStage = 0;
	protected int timeFallBehind = 0;
	protected NpcCommand activecommand;
	
	protected float initialDist, dist = 0;
	protected int startingZ, endingZ;
	private boolean doMakeBlocks;
	float ratio;
	private Vec3 returnLocation;
	
	private CharacterProperty characterProperty = BiGX.instance().characterProperty;
	private EntityCustomNpc npc;
	private NpcCommand command;
	
	public static final float chaseRunBaseSpeed = 2.1f; // 157 blocks per 15 seconds!!
	protected float speedchange = 0f;
	private final float chaseRunSpeedInBlocks = 157f/15f;
	protected boolean chasingQuestOnGoing = false;
	protected boolean chasingQuestOnCountDown = false;
	protected int virtualCurrency = 0;
	protected long warningMsgBlinkingTime = System.currentTimeMillis();

	protected TerrainBiome terrainBiome = new TerrainBiome();
	protected TerrainBiomeFire terrainBiomeFire = new TerrainBiomeFire();
	
	protected ArrayList<Integer> questSettings = null;

	protected int chasingQuestInitialPosX = 0;
	protected int chasingQuestInitialPosY = 0;
	protected int chasingQuestInitialPosZ = 0;

	protected int thiefHealthMax = 15;
	protected int thiefHealthCurrent = thiefHealthMax;
	protected int thiefLevel = 1;
	protected int thiefMaxLevel = 1;
	protected boolean thiefLevelUpFlag = false;
	
	private WorldServer ws;
	private int questDestinationDimensionId = -1;
	private int questSourceDimensionId = -1;
	
	protected LevelSystem levelSys;
	
	public EntityPlayer player;
	private List<Vec3> blocks = new ArrayList<Vec3>();
	
	
	public QuestTaskChasing(LevelSystem levelSys, QuestManager questManager, EntityPlayer p, WorldServer worldServer, int level, int maxLevel, QuestChaseTypeEnum questChaseType) {
		super(questManager, true);
		
		this.levelSys = levelSys;
		player = p;
		ws = worldServer;
		thiefLevel = level;
		thiefMaxLevel = maxLevel;
		this.id = this.id + "_" + questChaseType;
		
		switch(questChaseType)
		{
		case REGULAR:
			this.questDestinationDimensionId = WorldProviderFlats.dimID;
			this.questSourceDimensionId = 0;
			break;
		case FIRE:
			this.questDestinationDimensionId = WorldProviderDark.dimID;
			this.questSourceDimensionId = 105;
			break;
		default:
			break;
		}
		
		this.questChaseType = questChaseType;
	}
	
	public QuestTaskChasing(LevelSystem levelSys, QuestManager questManager, EntityPlayer p, WorldServer worldServer, int level, int maxLevel) {
		this(levelSys, questManager, p, worldServer, level, maxLevel, QuestChaseTypeEnum.REGULAR);
	}
	public boolean checkPlayerInArea(EntityPlayer player, int x1, int y1, int z1, int x2, int y2, int z2) {
		return  player.posX >= x1 && player.posX <= x2 &&
				player.posY >= y1 && player.posY <= y2 &&
				player.posZ >= z1 && player.posZ <= z2;
	}
	
	public Block getBlockByDifficulty(int difficultyLevel)
	{
		int category = difficultyLevel;
		
		switch(category)
		{
		case 0:
			return Blocks.brick_block;
		case 1:
			return Blocks.stone;
		case 2:
			return Blocks.grass;
		case 3:
			return Blocks.gravel;
		case 4:
			return Blocks.sand;
		default:
			return null;
		}
	}
	
	public void goBackToTheOriginalWorld(World world, Entity entity)
	{
//		System.out.println("goBackToTheOriginalWorld");
//		deactivateTask();
		
		chasingQuestOnGoing = false;
		chasingQuestOnCountDown = false;
		timeFallBehind = 0;
		time = 0;
		initThiefStat();
		countdown = 11;
		pausedTime = 0;
		
		if(world.isRemote)
			((BigxClientContext)context).setSpeed(0);
		
		if(npc != null)
			command.removeNpc(npc.display.name, WorldProviderFlats.dimID);

		switch(this.questChaseType)
		{
		case REGULAR:
			returnLocation = Vec3.createVectorHelper(96, 73, -8);
			break;
		case FIRE:
			returnLocation = Vec3.createVectorHelper(96, 73, -8);
			break;
		default:
			returnLocation = Vec3.createVectorHelper(96, 73, -8);
			break;	
		};

		initThiefStat();
		cleanArea(world, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)entity.posZ - 128, (int)entity.posZ);
		QuestTeleporter.teleport(entity, this.questSourceDimensionId, (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
	}
	
	public void cleanArea(World world, int initX, int initY, int initZ, int endZ)
	{
		for(int dz=initZ; dz<endZ+64; dz++)
		{
			for(int dx=chasingQuestInitialPosX-32; dx<chasingQuestInitialPosX+32; dx++)
			{
				world.setBlock(dx, initY-1, dz, Blocks.grass);
				for(int dy= initY; dy<initY+16; dy++)
				{
					if(!(world.getBlock(dx, dy, dz) == Blocks.air))
						world.setBlock(dx, dy, dz, Blocks.air);
				}
			}
			world.setBlock(chasingQuestInitialPosX-16, initY, dz, Blocks.air);
			world.setBlock(chasingQuestInitialPosX+16, initY, dz, Blocks.air);
		}
	}
	
	public void initThiefStat()
	{
		thiefHealthMax = 15;
		thiefHealthCurrent = thiefHealthMax;
		thiefLevel = 1;
	}
	
	public void thiefLevelUp()
	{
		thiefLevel ++;
		thiefMaxLevel ++;
		
		thiefHealthMax = 15 + (int) Math.pow(3, thiefLevel);
		thiefHealthCurrent = thiefHealthMax;
	}
	
	public void setThiefLevel(int level)
	{
		thiefLevel = level;
		
		thiefHealthMax = 15 + (int) Math.pow(3, thiefLevel);
		thiefHealthCurrent = thiefHealthMax;
	}
	
	public void deductThiefHealth(Item itemOnHands)
	{
		int deduction = 1;
		if (itemOnHands != null) {
			if(itemOnHands.getUnlocalizedName().equals("item.swordWood"))
			{
				deduction = 2;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.swordIron"))
			{
				deduction = 3;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.npcBronzeSword"))
			{
				deduction = 4;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.npcMithrilSword"))
			{
				deduction = 5;
			} 
			else if(itemOnHands.getUnlocalizedName().equals("item.npcEmeraldSword"))
			{
				deduction = 6;
			}
		}
		
		thiefHealthCurrent -= deduction;
		
		virtualCurrency += deduction;
		
		if(thiefHealthCurrent <= 0)
		{
			thiefHealthCurrent = 0;
			thiefLevelUpFlag = true;
		}
		
		GuiDamage.addDamageText(deduction, 255, 10, 10);
	}

	private void generateFakeHouse(World w, List<Vec3> blocks, int origX, int origY, int origZ) {
		for (int x = origX; x < origX + 7; ++x) {
			if (x == origX || x == origX + 6) {
				for (int y = origY; y < origY + 5; ++y) {
					for (int z = origZ; z < origZ + 11; ++z) {
						if (z == origZ || z == origZ + 10)
							w.setBlock(x, y, z, Blocks.log);
						else
							w.setBlock(x, y, z, Blocks.planks);
						blocks.add(Vec3.createVectorHelper(x, y, z));
					}
				}
				w.setBlock(x, origY+1, origZ+5, Blocks.glass);
				w.setBlock(x, origY+2, origZ+5, Blocks.glass);
			} else {
				for (int y = origY; y < origY + 7; ++y) {
					for (int z = origZ; z < origZ + 11; ++z) {
						if ((z == origZ || z == origZ + 10) && y < origY + 5) {
							w.setBlock(x, y, z, Blocks.planks);
							blocks.add(Vec3.createVectorHelper(x, y, z));
						}
						if (z > origZ && z < origZ + 10 && y == origY + 5 && !(x == origX + 3 && (z == origZ + 3 || z == origZ + 7))) {
							w.setBlock(x, y, z, Blocks.planks);
							blocks.add(Vec3.createVectorHelper(x, y, z));
						}
						if (z > origZ + 1 && z < origZ + 9 && x > origX + 1 && x < origX + 5 && y == origY + 6 && !(x == origX + 3 && (z == origZ + 3 || z == origZ + 7))) {
							w.setBlock(x, y, z, Blocks.planks);
							blocks.add(Vec3.createVectorHelper(x, y, z));
						}
					}
				}
				if (x == origX + 2 || x == origX + 4) {
					w.setBlock(x, origY+1, origZ, Blocks.glass);
					w.setBlock(x, origY+2, origZ, Blocks.glass);
				}
			}
		}
	}
	
	private void generateStructuresOnSides()
	{
		/**
		 * Generates structures on sides (fence and Fake house on sides)
		 */
		for (int z = (int)player.posZ+32; z < (int)player.posZ+64; ++z) {
			ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
			blocks.add(Vec3.createVectorHelper((int)player.posX-16, chasingQuestInitialPosY, z));
			ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
			blocks.add(Vec3.createVectorHelper((int)player.posX+16, chasingQuestInitialPosY, z));
		}
		
		Random rand = new Random();
		if (rand.nextInt(10) < 2) {
			generateFakeHouse(ws, blocks, chasingQuestInitialPosX-25, chasingQuestInitialPosY, (int)player.posZ+64);
			
		}
		rand = new Random();
		if (rand.nextInt(10) < 2) {
			generateFakeHouse(ws, blocks, chasingQuestInitialPosX+18, chasingQuestInitialPosY, (int)player.posZ+64);
			
		}
	}
	
	private void generateTerrainByPatientProfile() {
		ws = MinecraftServer.getServer().worldServerForDimension(this.questDestinationDimensionId);
		
		if(context.isSuggestedGamePropertiesReady())
		{
			/**
			 * Generates structures inside fence
			 */
			ArrayList<TerrainBiomeArea> areas = new ArrayList<TerrainBiomeArea>();
			int currentRelativePosition = (int)player.posZ - chasingQuestInitialPosZ;
			int currentRelativeTime = (int) (currentRelativePosition/chaseRunSpeedInBlocks);
			
			if(currentRelativeTime >= questSettings.size())
			{
				currentRelativeTime = questSettings.size() -1;
			}
			
			int currentQuestDifficulty = questSettings.get(currentRelativeTime);
			Block blockByDifficulty = getBlockByDifficulty(currentQuestDifficulty);

			if( (blockByDifficulty == Blocks.brick_block) || 
					(blockByDifficulty == Blocks.stone) || 
					(blockByDifficulty == Blocks.gravel) )
			{
				for(int idx = 0; idx<4; idx++)
				{
					switch(this.questChaseType)
					{
					case REGULAR:
						areas.add(terrainBiome.getRandomCityBiome());
						break;
					case FIRE:
						areas.add(terrainBiomeFire.getRandomFieldBiome());
						break;
					default:
						areas.add(new TerrainBiomeArea());
						break;
					}
				}
			}
			else if(blockByDifficulty == Blocks.grass)
			{
				for(int idx = 0; idx<4; idx++)
				{
					switch(this.questChaseType)
					{
					case REGULAR:
						areas.add(terrainBiome.getRandomGrassBiome());
						break;
					case FIRE:
						areas.add(terrainBiomeFire.getRandomGateBiome());
						break;
					default:
						areas.add(new TerrainBiomeArea());
						break;
					}
				}
			}
			else if(blockByDifficulty == Blocks.sand)
			{
				for(int idx = 0; idx<4; idx++)
				{
					switch(this.questChaseType)
					{
					case REGULAR:
						areas.add(terrainBiome.getRandomDesertBiome());
						break;
					case FIRE:
						areas.add(terrainBiomeFire.getRandomLavaFountainBiome());
						break;
					default:
						areas.add(new TerrainBiomeArea());
						break;
					}
				}
			}
			else {
				System.out.println("DIFFICULTY IS OUT OF OUR HAND...");
				blockByDifficulty = Blocks.stone;
			}
			
			for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
				for (int z = (int)player.posZ+48; z < (int)player.posZ+64; ++z) {
					ws.setBlock(x, chasingQuestInitialPosY-1, z, blockByDifficulty);
					blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY-1, z));
				}
			}
			
			for(int row=0; row<1; row++)
			{
				for(int idx=0; idx<areas.size(); idx++)
				{
					int x=0;
					switch(idx)
					{
					case 0:
						x = chasingQuestInitialPosX-14;
						break;
					case 1:
						x = chasingQuestInitialPosX-8;
						break;
					case 2:
						x = chasingQuestInitialPosX+2;
						break;
					case 3:
						x = chasingQuestInitialPosX+9;
						break;
					}
					int y = chasingQuestInitialPosY;
					int z = (int)player.posZ+49 + row*9;
					
					TerrainBiomeArea terrainBiomeArea = areas.get(idx);
					
					for(TerrainBiomeAreaIndex terrainBiomeAreaIndex : terrainBiomeArea.map.keySet())
					{
						if(terrainBiomeArea.map.get(terrainBiomeAreaIndex) == Blocks.water)
							ws.setBlock(terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex));
						else
							ws.setBlock(terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex), terrainBiomeAreaIndex.direction, 3);
					}
				}
			}
			
			if(areas.size() != 0)
				areas.clear();
			/**
			 * END OF Generates structures inside fence
			 */
		}
		else{
			if (ratio > 0.4) {
				for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
					for (int z = (int)player.posZ+48; z < (int)player.posZ+64; ++z) {
						ws.setBlock(x, chasingQuestInitialPosY-1, z, Blocks.gravel);
						blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY-1, z));
						ws.setBlock(x, chasingQuestInitialPosY-1, z-64, Blocks.grass);
					}
				}
			}
		}
	}
	
	public float getPlayerPitch() {
		return playerQuestPitch;
	}

	public float getPlayerYaw() {
		return playerQuestYaw;
	}
	
	public void setNpc(EntityCustomNpc npc)
	{
		this.npc = npc;
	}
	
	public void setNpcCommand(NpcCommand npcCommand)
	{
		this.command = npcCommand;
	}

	@Override
	public void CheckComplete() {
		// CHASE QUEST WINNING CONDITION == WHEN the HP of the bad guy reached 0 or below
		if (thiefHealthCurrent <= 0) {
//			try {
////				context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTOPSUCCESS, System.currentTimeMillis());
//			} catch (SocketException e) {
//				e.printStackTrace();
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			} catch (BiGXNetException e) {
//				e.printStackTrace();
//			} catch (BiGXInternalGamePluginExcpetion e) {
//				e.printStackTrace();
//			}
			
			player.worldObj.playSoundAtEntity(player, "win", 1.0f, 1.0f);
			endingZ = (int)player.posZ;
			LeaderboardRow row = new LeaderboardRow();
			row.name = player.getDisplayName();
			row.level = Integer.toString(thiefLevel);
			row.time_elapsed = "" + time;
			
			try {
				GuiLeaderBoard.writeToLeaderboard(row);
			} catch (IOException e) {
				e.printStackTrace();
			}

			BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldBarInfo);
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldSpendWisely);
			
			if (thiefLevel == 1){
				ItemStack key = new ItemStack(Item.getItemById(4424));
				key.setStackDisplayName("Burnt Key"); 
				player.inventory.addItemStackToInventory(key);
				player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4420))); //water element 
			}
			
			System.out.println("[BiGX] increased exp: " + levelSys.incExp(50));
//			if(levelSys.getPlayerLevel() == thiefLevel && levelSys.incExp(50/levelSys.getPlayerLevel())){ //Can be changed later so it's more variable
//				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.levelUpMsg);
//				levelSys.giveLevelUpRewards(player);
//			}
			
			isActive = false;
			completed = true;
			goBackToTheOriginalWorld(ws, player);
			
			return;
		}
		
		// CHASE QUEST LOSE CONDITION
		if(timeFallBehind >= 30)
		{
//			try {
//				context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTOPFAILURE, System.currentTimeMillis());
//			} catch (SocketException e) {
//				e.printStackTrace();
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			} catch (BiGXNetException e) {
//				e.printStackTrace();
//			} catch (BiGXInternalGamePluginExcpetion e) {
//				e.printStackTrace();
//			}
			
			BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
			if (thiefLevel == thiefMaxLevel && virtualCurrency > 50)
				thiefLevelUp();
			
			isActive = false;
			completed = false;
			goBackToTheOriginalWorld(ws, player);
		}
	}
	
	public void countdownTick()
	{
		System.out.println("isClient[" + player.worldObj.isRemote + "] countdown[" + countdown + "]");
		
		long timeNow = System.currentTimeMillis();
		
		// COUNT DOWN TIME
		countdown --;
		
		if(countdown > 0){	
			if (countdown == 7) {
				for (Object o : player.worldObj.loadedEntityList) {
					if (((Entity)o) instanceof EntityCustomNpc) {
						((EntityCustomNpc)o).delete();
					}
				}
				for (Object o : NpcCommand.getCustomNpcsInDimension(this.questDestinationDimensionId)) {
//					System.out.println(((EntityCustomNpc)o).display.name);
				}
			}
			if (countdown == 0) {
				elapsedTime = timeNow;
				dist = 0;
				startingZ = (int)player.posZ;
				endingZ = (int)player.posZ;
			}
			if (countdown == 5) {
				if(player.worldObj.isRemote) {
				}
				else
				{
					System.out.println("if (countdown == 5)");
					
					GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseShowup);
					GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseHintWeapon);
					
					switch(this.questChaseType)
					{
					case REGULAR:
						npc = NpcCommand.spawnNpc(0, 11, 20, ws, "Thief");
						npc.ai.stopAndInteract = false;
						break;
					case FIRE:
						npc = NpcCommand.spawnNpc(0, 11, 20, ws, "Ifrit");
						npc.ai.stopAndInteract = false;
						npc.display.texture = "customnpcs:textures/entity/humanmale/Evil_Gold_Knight.png";
						break;
					default:
						npc = NpcCommand.spawnNpc(0, 11, 20, ws, "Thief");
						npc.ai.stopAndInteract = false;
						break;
					};
					
					setNpc(npc);
					
					command = new NpcCommand(context, npc);
					command.setSpeed(10);
					command.enableMoving(false);
					command.runInDirection(ForgeDirection.SOUTH);
					
					setNpcCommand(command);
				}
			}
			else if (countdown == 1)
			{
//				try {
//					context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTART, System.currentTimeMillis());
//				} catch (SocketException e) {
//					e.printStackTrace();
//				} catch (UnknownHostException e) {
//					e.printStackTrace();
//				} catch (BiGXNetException e) {
//					e.printStackTrace();
//				} catch (BiGXInternalGamePluginExcpetion e) {
//					e.printStackTrace();
//				}
			}
			if(countdown == 9)
			{
				player.rotationPitch = 0f;
				player.rotationYaw = 0f;
			}
			if(countdown == 8)
			{
				if(player.worldObj.isRemote) {
					GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseBeginning);
				}
			}
		} else {
			initThiefStat();
			chasingQuestOnCountDown = false;
			System.out.println("GO!");
			command.enableMoving(true);
			countdown = 11;
			initialDist = 20; // HARD CODED
			pausedTime = 0;
//			Minecraft.getMinecraft().gameSettings.mouseSensitivity = 0;
		}
	}
	
	public void handleCountdown()
	{
		if (Minecraft.getMinecraft().isGamePaused())
		{
			questTimeStamp += System.currentTimeMillis() - lastCountdownTickTimestamp;
			return;
		}
		
		int tickCount = 0;
		long timeNow = System.currentTimeMillis();
		
		// Calulate Tick Numbers
		if(countdown == 11)
		{
			pausedTime = 0;
			questTimeStamp = timeNow;
			countdown = 10;
			tickCount ++;
		}
		else
		{
			tickCount = (int) ((timeNow - lastCountdownTickTimestamp)/1000);
		}

		if(tickCount != 0)
			lastCountdownTickTimestamp = timeNow;
		
		// Tick Countdown
		for(int i=0; i<tickCount; i++)
			countdownTick();
	}
	
	public void handlePlayTimeOnServer()
	{
		dist = player.getDistanceToEntity(npc);
		ratio = (initialDist-dist)/initialDist;
		
		if (Minecraft.getMinecraft().isGamePaused())
		{
			pausedTime += (QuestEventHandler.tickCountUpperLimit*20);
		}
		else
		{
			long timeNow = System.currentTimeMillis();
			if( (timeNow - lastTickTime - pausedTime) < 500 )
			{
			}
			else if(lastTickStage == 0)
			{
				System.out.println("CLEANING");
				
				lastTickStage++;

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				// CLEAN the terrain behind
				/**
				 * Terrain Cleaning
				 */
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ-128, (int)player.posZ-112);
				/**
				 * END OF Terrain Cleaning
				 */
			}
			else if(lastTickStage == 1)
			{
				System.out.println("GENERATING");
				
				lastTickStage = 0;
				
				// Make Obstacles and structures on the side
				this.generateStructuresOnSides();
				this.generateTerrainByPatientProfile();

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				if (ratio < 0) {
					warningMsgBlinkingTime = System.currentTimeMillis();
					timeFallBehind++;
				}
				else{
					timeFallBehind = 0;
				}
				
				this.time++;
			}
		}
	}
	
	public void handlePlayTimeOnClient()
	{
		// SPEED CHANGE LOGIC BASED ON THE HEART RATE AND THE RPM OF THE PEDALLING
		BigxClientContext context = (BigxClientContext) this.context;
		speedchange = 0f;
		float speedchangerate = 0.025f;
		
		// Handling Player heart rate and rpm as mechanics for Chase Quest
		BiGXPatientPrescription playerperscription;
		try{
			playerperscription = context.suggestedGameProperties.getPlayerProperties().getPatientPrescriptions().get(0);
		}
		catch (Exception e)
		{
			playerperscription = new BiGXPatientPrescription();
			playerperscription.setTargetMin(100);
			playerperscription.setTargetMax(100);
			System.out.println("[BiGX] player prescription is not avilable.");
		}
		
		if (playerperscription.getTargetMin() > context.heartrate || context.rpm < 40)
			speedchange += speedchangerate;
		else if (playerperscription.getTargetMax() >= context.heartrate || context.rpm > 60 && context.rpm <= 90)
			speedchange += speedchangerate;
		else if (playerperscription.getTargetMax() < context.heartrate)
			speedchange -= speedchangerate/2;
		
		if (context.rpm <= 60 && context.rpm > 40)
			speedchange += speedchangerate;
		else if (context.rpm <= 90 && context.rpm > 60)
			speedchange += speedchangerate;
		else if (context.rpm > 90)
			speedchange += speedchangerate/2;
		else if (context.rpm <= 40)
			speedchange -= speedchangerate;
		
		Minecraft mc = Minecraft.getMinecraft();
		
//		if(mc.getMinecraft().objectMouseOver != null) {
//			if(mc.getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
//				mc.getMinecraft().playerController.attackEntity(mc.thePlayer, mc.getMinecraft().objectMouseOver.entityHit);
//			}
//		}
		player.swingItem();
	}


	@Override
	public void run()
	{
		System.out.println("[BiGX] Quest Chasing Task Started");
		synchronized (questManager) {
			init();
			
			while(isActive)
			{	
				if(chasingQuestOnGoing)
				{
					if(chasingQuestOnCountDown)
					{
						handleCountdown();
					}
					else
					{
						// BUILD CENTER
						// BUILD SIDE
						if(!player.worldObj.isRemote)
							handlePlayTimeOnServer();
						else
							handlePlayTimeOnClient();
					}
				}
				
				if(!player.worldObj.isRemote)
				{
					((BigxServerContext)context).updateQuestInformationToClient((BigxServerContext)context);
				}
				
				try {
					questManager.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			time = 0;
			initThiefStat();
			countdown = 11;
			pausedTime = 0;
			((BigxServerContext)context).updateQuestInformationToClient(null);
		}
	}
	
	@Override
	public void init()
	{
		World world = player.worldObj;
		time = 0;
		virtualCurrency = 0;
		initThiefStat();
		completed = false;
		countdown = 11;
		pausedTime = 0;
		
		questSettings = new ArrayList<Integer>();
		StageSettings stagesettings;
		List<Stage> stageList;
		stageList = new ArrayList<Stage>();
		questSettings.add(0);
		
		if(context.suggestedGameProperties != null)
		{
			if(context.suggestedGameProperties.getQuestProperties().getStageSettingsArray().size() != 0)
			{
				stagesettings = context.suggestedGameProperties.getQuestProperties().getStageSettingsArray().get(0);
				stageList = stagesettings.stages;
				
				for(int i=0; i<stageList.size();i++)
				{
					for(int j=0; j<stageList.get(i).duration; j++)
					{
						questSettings.add(stageList.get(i).exerciseSettings);
					}
				}
			}
		}
		
		if(world.isRemote)
		{
			context = BiGX.instance().clientContext;
		}
		else
		{
			context = BiGX.instance().serverContext;
		}
	}

	
	@Override
	public void onItemUse(Start event) {
		synchronized (questManager) {
			player = event.entityPlayer;
			
			if(!player.worldObj.isRemote)
			{
				ws = MinecraftServer.getServer().worldServerForDimension(this.questDestinationDimensionId);
				
				int x1,y1,z1;
				int x2,y2,z2;
				
				switch(questChaseType)
				{
				case REGULAR:
					x1=94; y1=53; z1=-54; x2=99; y2=58; z2=-48;
					break;
				case FIRE:
					x1=18; y1=77; z1=-76; x2=23; y2=79; z2=-73;
					break;
				default:
					x1=94; y1=53; z1=-54; x2=99; y2=58; z2=-48;
					break;
				};
				
				if (player.getHeldItem().getDisplayName().contains("Teleportation Potion") && checkPlayerInArea(player, x1, y1, z1, x2, y2, z2)
						&& player.dimension != this.questDestinationDimensionId
						&& player.dimension == this.questSourceDimensionId)
				{
					boolean isReboot = !isActive;
					
					time = 0;
					initThiefStat();
					countdown = 11;
					pausedTime = 0;
					completed = false;
					setThiefLevel(Integer.parseInt(player.getHeldItem().getDisplayName().split(" ")[2]));
					
					// INIT questSettings ArrayList if there is any
					if(context.isSuggestedGamePropertiesReady())
					{
						time = 0; 
						questSettings = new ArrayList<Integer>();
						StageSettings stagesettings = context.suggestedGameProperties.getQuestProperties().getStageSettingsArray().get(0);
						List<Stage> stageList = stagesettings.stages;
						
						for(int i=0; i<stageList.size();i++)
						{
							for(int j=0; j<stageList.get(i).duration; j++)
							{
								questSettings.add(stageList.get(i).exerciseSettings);
							}
						}
					}
					else{
						time = 0;
					}

					returnLocation = Vec3.createVectorHelper(player.posX-1, player.posY-1, player.posZ);
					QuestTeleporter.teleport(player, this.questDestinationDimensionId, 1, 11, 0);

					chasingQuestInitialPosX = 1;
					chasingQuestInitialPosY = 10;
					chasingQuestInitialPosZ = 0;
					
					blocks = new ArrayList<Vec3>();
					
					for (int z = -16; z < (int)player.posZ+64; ++z) {
						ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
						blocks.add(Vec3.createVectorHelper((int)player.posX-16, chasingQuestInitialPosY, z));
						ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
						blocks.add(Vec3.createVectorHelper((int)player.posX+16, chasingQuestInitialPosY, z));
					}
					for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
						ws.setBlock(x, chasingQuestInitialPosY, -16, Blocks.fence);
						blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY, -16));
					}
					
					for (int z = (int)player.posZ; z < (int)player.posZ+64; ++z) {
						ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY-2, z, Blocks.fence);
						blocks.add(Vec3.createVectorHelper((int)player.posX-16, chasingQuestInitialPosY-2, z));
						ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY-2, z, Blocks.fence);
						blocks.add(Vec3.createVectorHelper((int)player.posX+16, chasingQuestInitialPosY-2, z));
					}
					
					chasingQuestOnGoing = true;
					chasingQuestOnCountDown = true; 
					questTimeStamp = System.currentTimeMillis();
					
					if(isReboot)
						reactivateTask();
				}
				else if (player.getHeldItem().getDisplayName().contains("Teleportation Potion")
						&& player.dimension == this.questDestinationDimensionId)
				{
					// CHASE QUEST LOSE CONDITION
					if (ws != null && player instanceof EntityPlayerMP) {
						isActive = false;
						BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
						virtualCurrency = 0;
						time = 0;
						countdown = 11;
						pausedTime = 0;
						initThiefStat();
						cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ - 128, (int)player.posZ);
						completed = false;
						goBackToTheOriginalWorld(ws, player);
					}

					chasingQuestOnGoing = false;
					chasingQuestOnCountDown = false; 
				}
			}
		}
	}

	@Override
	public void onAttackEntityEvent(AttackEntityEvent event) {
		synchronized (questManager) {
			if( (chasingQuestOnGoing) && (!chasingQuestOnCountDown) )
			{
				if (event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] == null)
					deductThiefHealth(null);
				else
					deductThiefHealth(event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem].getItem());
			}
		}
	}

	@Override
	public void unregisterEvents() {
		QuestEventHandler.unregisterQuestEventAttack(this);
		
		if(!player.worldObj.isRemote){
			QuestEventHandler.unregisterQuestEventItemUse(this);
			QuestEventHandler.unregisterQuestEventCheckComplete(this);
		}
	}
	
	@Override
	public void registerEvents() {
		QuestEventHandler.registerQuestEventAttack(this);
		if(!player.worldObj.isRemote)
		{
			QuestEventHandler.registerQuestEventItemUse(this);
			QuestEventHandler.registerQuestEventCheckComplete(this);
		}
	}

	@Override
	public String getTaskDescription() {
		return "Defeat the Thief and retrive the gold.";
	}

	@Override
	public String getTaskName() {
		return QuestTaskChasing.class.toString();
	}

	
	public int getTime()
	{
		return time;
	}
	
	public int getCountdown()
	{
		return countdown;
	}
	
	public int getTimeFallBehind()
	{
		return timeFallBehind;
	}	
	
	public int getThiefHealthMax() {
		return thiefHealthMax;
	}

	public int getThiefHealthCurrent() {
		return thiefHealthCurrent;
	}

	public int getThiefLevel() {
		return thiefLevel;
	}


	public boolean isChasingQuestOnGoing() {
		return chasingQuestOnGoing;
	}

	public boolean isChasingQuestOnCountDown() {
		return chasingQuestOnCountDown;
	}

	public long getWarningMsgBlinkingTime() {
		return warningMsgBlinkingTime;
	}

	public float getDist() {
		return dist;
	}

	public float getSpeedchange() {
		return speedchange;
	}

	@Override
	public void onCheckCompleteEvent() {
		CheckComplete();
	}
	
	
}
