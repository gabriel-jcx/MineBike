package org.ngs.bigx.minecraft.quests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.dictionary.objects.game.properties.Stage;
import org.ngs.bigx.dictionary.objects.game.properties.StageSettings;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiLeaderBoard;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.GuiStats;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.client.gui.GuiMonsterReadyFight;
import org.ngs.bigx.minecraft.client.gui.GuiMonsterStunned;
import org.ngs.bigx.minecraft.client.gui.GuiVictory;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiFinishChasingQuest;
import org.ngs.bigx.minecraft.client.skills.Skill.enumSkillState;
import org.ngs.bigx.minecraft.client.skills.SkillBoostDamage;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.quests.chase.AudioFeedback;
import org.ngs.bigx.minecraft.quests.chase.IAudioFeedbackPlayback;
import org.ngs.bigx.minecraft.quests.chase.ObstacleBiome;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiome;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.IAudioFeedbackPlayback.AudioFeedBackEnum;
import org.ngs.bigx.minecraft.quests.chase.fire.TerrainBiomeFire;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventAttack;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemPickUp;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemUse;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventRewardSession;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
//import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.util.EnumFacing;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Start;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskFightAndChasing extends QuestTask implements IAudioFeedbackPlayback, IQuestEventRewardSession, IQuestEventAttack, IQuestEventItemUse, IQuestEventItemPickUp, IQuestEventNpcInteraction {
public enum QuestChaseTypeEnum { REGULAR, FIRE, ICE, AIR, LIFE };

	public static final int NPCRUNNINGSPEED = QuestTaskChasing.NPCRUNNINGSPEED;
	public static final double NPCRUNNINGSPEEDBOOSTRATE = QuestTaskChasing.NPCRUNNINGSPEEDBOOSTRATE;
	
	public static final String[] villainNames = {"Zombie","Zombie","Ogre","Maniac","Ifrit"};

	public static ArrayList<ArrayList<String>> monsterTypes = GetMonsterTypes();
	
	public static boolean isBoss = false;
	
	protected QuestChaseTypeEnum questChaseType = QuestChaseTypeEnum.REGULAR;
	
	private String id = "QUEST_TASK_CHASE";
	
	private float playerQuestPitch, playerQuestYaw;
	
	protected long questTimeStamp = 0;
	 
	protected long lastCountdownTickTimestamp = 0;
	protected int countdown = 5;
	
	protected int time = 0;
	protected double elapsedTime = 0;
	protected double pausedTime = 0;
	protected double lastTickTime = 0;
	protected int lastTickStage = 0;
	protected int timeFallBehind = 0;
	public int endOfChaseItemCounter = 0;
	protected NpcCommand activecommand;

	protected int obstacleRefreshed = 4;
	protected int obstacleTime = obstacleRefreshed/2; // 0 to init selection -3 to spawn
	protected int obstacleId = -1;
	
	protected long comboTime = 0;
	protected int comboCount = 0;
	protected int bestCombo = 0;
	protected final int comboTimeLimit = 400;
	
	protected float initialDist, dist = 0;
	protected int startingZ, endingZ;
	float ratio;
	private Vec3d returnLocation;
	
	private CharacterProperty characterProperty = BiGX.instance().characterProperty;
	private EntityCustomNpc npc;
	private NpcCommand command;
	
	protected float speedchange = 0f;
	private final float chaseRunSpeedInBlocks = 157f/15f;
	protected boolean chasingQuestOnGoing = false;
	protected boolean chasingQuestOnCountDown = false;
	protected int virtualCurrency = 0;
	protected long warningMsgBlinkingTime = System.currentTimeMillis();

	protected TerrainBiome terrainBiome = new TerrainBiome();
	protected ObstacleBiome obstacleBiome = new ObstacleBiome();
	protected TerrainBiomeFire terrainBiomeFire = new TerrainBiomeFire();
	
	protected ArrayList<Integer> questSettings = null;

	protected int chasingQuestInitialPosX = 0;
	protected int chasingQuestInitialPosY = 0;
	protected int chasingQuestInitialPosZ = 0;

	protected int thiefHealthMaxDefault = 50;
	protected int thiefHealthMax = thiefHealthMaxDefault;
	protected int thiefHealthCurrent = thiefHealthMax;
	protected int thiefLevel = 1;
	protected int thiefMaxLevel = 1;
	protected boolean thiefLevelUpFlag = false;
	protected boolean thiefLevelSet = false;
	
	private WorldServer ws;
	private int questDestinationDimensionId = WorldProviderFlats.dimID;
	private int questSourceDimensionId = 0;
	
	public EntityPlayer player;
	private List<Vec3d> blocks = new ArrayList<Vec3d>();
	
	private boolean menuOpen = false;
	
	private int questTypeId = 1;
	
	private int initialHunger;
	
	public String chosenSong = "";
	
	public boolean isStunnedGuiHappend = false;
	
	private static LevelSystem levelSys;

	public static final int speedUpEffectTickCountMax = 60;
	public static final int damageUpEffectTickCountMax = 60;
	public static final int thiefSpeedUpEffectTickCountMax = 10;
	public static final float chaseRunBaseSpeed = 2.1f; // 157 blocks per 15 seconds!!
	
	private static int speedUpEffectTickCount = 0;
	private static int damageUpEffectTickCount = 0;
	private static int thiefSpeedUpEffectTickCount = 0;
	
	public static int sprintTickCount = 0;
	public static int sprintTickCountMax = 120;
	public static int sprintTickCountMinMax = 10; // 7 seconds
	
	public static int positionSelectionTickCount = 0;
	public static int positionSelectionTickCountMax = 100;

	public static boolean isContinueSelected = false;
	public static boolean isRetrySelected = false;
	public static boolean isExitSelected = false;

	private static boolean flagAccomplished = false;
	private static boolean flagFallBehind = false;
	private static boolean flagRetry = false;
	private static boolean flagGiveup = false;
	private static boolean flagContinue = false;
	private static boolean flagOpenQuestMenuGui = false;
	private static boolean flagLeave = false;
	
	private static boolean isRewardState = false;
	
	private static boolean isNpcFellDown = false;
	private static boolean npcFellDownFlag = false;
	private static int npcFellDownTimestamp = 0;
	private static int npcFellDownLength = 10;
	private static int npcFellDownPeriod = 12;
	
	private static AudioFeedback audioFeedback;
	private static float playerPosZLastFewSecondsAgo;
	private static float distToNpcLastFewSeconds;
	
	private static float distMovedLastFewSeconds;
	private static float damageLastFewSeconds;
	private static long progressStatTimeStamp;
	
	public void setPreviousLocationBeforeTheQuest(int dim, int x, int y, int z)
	{
		this.questSourceDimensionId = dim;

		returnLocation = new Vec3d(x, y, z);
	}

	public static LevelSystem getLevelSystem()
	{
		return levelSys;
	}
	
	public QuestTaskFightAndChasing(LevelSystem levelSys, QuestManager questManager, EntityPlayer p, WorldServer worldServer, int level, int maxLevel, QuestChaseTypeEnum questChaseType) 
	{
		super(questManager, true);

		this.levelSys = levelSys;
		player = p;
		ws = worldServer;
		thiefLevel = level;
		thiefMaxLevel = maxLevel;
		this.id = this.id + "_" + questChaseType;
		
		this.questDestinationDimensionId = WorldProviderFlats.dimID;
		this.questSourceDimensionId = 0;
		
		this.questChaseType = questChaseType;
		audioFeedback = new AudioFeedback(this);
	}
	
	public QuestTaskFightAndChasing(LevelSystem levelSys, QuestManager questManager, EntityPlayer p, WorldServer worldServer, int level, int maxLevel) {
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
			return Blocks.BRICK_BLOCK;
		case 1:
			return Blocks.STONE;
		case 2:
			return Blocks.GRASS;
		case 3:
			return Blocks.GRAVEL;
		case 4:
			return Blocks.SAND;
		default:
			return null;
		}
	}
	
	public void goBackToTheOriginalWorld(World world, Entity entity)
	{
		Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;

		Minecraft.getMinecraft().player.sendChatMessage("/playsoundb " + chosenSong + " stop");
		chosenSong = "";
		
		System.out.println("goBackToTheOriginalWorld");
//		deactivateTask();
		
		chasingQuestOnGoing = false;
		chasingQuestOnCountDown = false;
		timeFallBehind = 0;
		time = 0;
		initThiefStat();
		countdown = 11;
		lastCountdownTickTimestamp = 0;
		pausedTime = 0;
		thiefLevelSet = false;
		
		Minecraft.getMinecraft().player.getFoodStats().setFoodLevel(initialHunger);
		
		if(world.isRemote)
			((BigxClientContext)clientContext).setSpeed(0);
		
		if(npc != null)
			command.removeNpc(npc.display.name, WorldProviderFlats.dimID);

//		switch(this.questChaseType)
//		{
//		case REGULAR:
//			returnLocation = new Vec3d(96, 58, -52);
//			break;
//		case FIRE:
//			returnLocation = new Vec3d(96, 73, -8);
//			break;
//		default:
//			returnLocation = new Vec3d(96, 73, -8);
//			break;	
//		};

		initThiefStat();
		cleanArea(world, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)entity.posZ - 100, (int)entity.posZ + 16);
		System.out.println("[BiGX] Cleaning Done.");
		System.out.println("Teleport x["+(int)returnLocation.xCoord+"] y["+(int)returnLocation.yCoord+"] z["+(int)returnLocation.zCoord+"]");
		QuestTeleporter.teleport(entity, this.questSourceDimensionId, (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
		System.out.println("[BiGX] Teleport Called");
	}
	
	public void cleanArea(World world, int initX, int initY, int initZ, int endZ)
	{
		for(int dz=initZ; dz<endZ+16; dz++)
		{
			for(int dx=chasingQuestInitialPosX-32; dx<chasingQuestInitialPosX+32; dx++)
			{
				cleanBlock(world, dx, initY-1, dz, Blocks.GRASS);
				for(int dy= initY; dy<initY+16; dy++)
				{
					if(!(world.getBlock(dx, dy, dz) == Blocks.AIR))
						setBlock(world, dx, dy, dz, Blocks.AIR);
				}
			}
			cleanBlock(world, chasingQuestInitialPosX-16, initY, dz, Blocks.AIR);
			cleanBlock(world, chasingQuestInitialPosX+16, initY, dz, Blocks.AIR);
		}
	}
	
	public void initThiefStat()
	{
		this.setThiefLevel(1);
	}
	
	public void setThiefLevel(int level)
	{
		thiefLevel = level;
		if (thiefLevel > thiefMaxLevel)
			thiefMaxLevel = thiefLevel;

		if(thiefLevel >= monsterTypes.size())
			thiefLevel = monsterTypes.size()-1;
		
		if (isBoss) {
			thiefHealthMax = 1000;
		} else {
			thiefHealthMax = Integer.parseInt(monsterTypes.get(thiefLevel).get(1));
		}
		thiefHealthCurrent = thiefHealthMax;
		
		System.out.println("Thief's level has been set!");
	}
	
	public void deductThiefHealth(Item itemOnHands)
	{
		long currentTime = System.currentTimeMillis();
		
		if( (currentTime - comboTime) < comboTimeLimit )
			comboCount ++;
		else {
			if (comboCount > bestCombo)
				bestCombo = comboCount;
			comboCount = 0;
			System.out.println("Best Combo: " + bestCombo);
		}
		
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
			else if (itemOnHands.getUnlocalizedName().equals("item.npcFrostSword")){
					if (this.questChaseType == QuestChaseTypeEnum.FIRE)
						deduction = 10;
					else
						deduction = 8;
					//TODO: Change ^
			}
		}
		
		if(((BigxClientContext)BigxClientContext.getInstance()).getCurrentGameState().getSkillManager().getSkills().get(1).getSkillState() == enumSkillState.EFFECTIVE)
		{
			deduction += SkillBoostDamage.boostRate;
		}
		
		// Combo Calculation
		deduction = (int) (deduction + comboCount*(deduction*.5));
		
		if(damageUpEffectTickCount > 0)
		{
			System.out.println("DAMAGE BOOST");
			deduction *= 2f;
		}
		
		thiefHealthCurrent -= deduction;
		
		virtualCurrency += deduction;
		
		if(thiefHealthCurrent <= 0)
		{
			thiefHealthCurrent = 0;
			thiefLevelUpFlag = true;
		}
		
		GuiDamage.addDamageText(deduction, 255, 10, 10);
		
		comboTime = currentTime;
	}

	public void handleQuestStart(){
		boolean isReboot = !isActive;
		Minecraft mc = Minecraft.getMinecraft();
		
		initialHunger = mc.player.getFoodStats().getFoodLevel();
		time = 0;
		initThiefStat();
		countdown = 5;
		lastCountdownTickTimestamp = 0;
		dist = 0;
		pausedTime = 0;
		completed = false;
		
		distMovedLastFewSeconds = 0;
		damageLastFewSeconds = 0;
		progressStatTimeStamp = 0;
		
		// INIT questSettings ArrayList if there is any
		if(serverContext.isSuggestedGamePropertiesReady())
		{
			time = 0; 
			questSettings = new ArrayList<Integer>();
			StageSettings stagesettings = serverContext.suggestedGameProperties.getQuestProperties().getStageSettingsArray().get(0);
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

		ws = MinecraftServer.getServer().worldServerForDimension(this.questDestinationDimensionId);
		QuestTeleporter.teleport(player, 100, 1, 11, 0);

		chasingQuestInitialPosX = 1;
		chasingQuestInitialPosY = 10;
		chasingQuestInitialPosZ = 0;
		
		blocks = new ArrayList<Vec3d>();
		
		for (int z = -16; z < (int)player.posZ+64; ++z) {
			setBlock(ws, chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
			blocks.add(new Vec3d((int)player.posX-16, chasingQuestInitialPosY, z));
			setBlock(ws, chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
			blocks.add(new Vec3d((int)player.posX+16, chasingQuestInitialPosY, z));
		}
		for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
			setBlock(ws, x, chasingQuestInitialPosY, -16, Blocks.fence);
			blocks.add(new Vec3d(x, chasingQuestInitialPosY, -16));
		}
		
		for (int z = (int)player.posZ; z < (int)player.posZ+64; ++z) {
			setBlock(ws, chasingQuestInitialPosX-16, chasingQuestInitialPosY-2, z, Blocks.fence);
			blocks.add(new Vec3d((int)player.posX-16, chasingQuestInitialPosY-2, z));
			setBlock(ws, chasingQuestInitialPosX+16, chasingQuestInitialPosY-2, z, Blocks.fence);
			blocks.add(new Vec3d((int)player.posX+16, chasingQuestInitialPosY-2, z));
		}
		
		chasingQuestOnGoing = true;
		chasingQuestOnCountDown = true; 
		questTimeStamp = System.currentTimeMillis();
		
		if(isReboot)
			reactivateTask();
	}
	
	private void generateFakeHouse(World w, List<Vec3d> blocks, int origX, int origY, int origZ) {
		for (int x = origX; x < origX + 7; ++x) {
			if (x == origX || x == origX + 6) {
				for (int y = origY; y < origY + 5; ++y) {
					for (int z = origZ; z < origZ + 11; ++z) {
						if (z == origZ || z == origZ + 10)
							setBlock(w, x, y, z, Blocks.log);
						else
							setBlock(w, x, y, z, Blocks.planks);
						blocks.add(new Vec3d(x, y, z));
					}
				}
				setBlock(w, x, origY+1, origZ+5, Blocks.glass);
				setBlock(w, x, origY+2, origZ+5, Blocks.glass);
			} else {
				for (int y = origY; y < origY + 7; ++y) {
					for (int z = origZ; z < origZ + 11; ++z) {
						if ((z == origZ || z == origZ + 10) && y < origY + 5) {
							setBlock(w, x, y, z, Blocks.planks);
							blocks.add(new Vec3d(x, y, z));
						}
						if (z > origZ && z < origZ + 10 && y == origY + 5 && !(x == origX + 3 && (z == origZ + 3 || z == origZ + 7))) {
							setBlock(w, x, y, z, Blocks.planks);
							blocks.add(new Vec3d(x, y, z));
						}
						if (z > origZ + 1 && z < origZ + 9 && x > origX + 1 && x < origX + 5 && y == origY + 6 && !(x == origX + 3 && (z == origZ + 3 || z == origZ + 7))) {
							setBlock(w, x, y, z, Blocks.planks);
							blocks.add(new Vec3d(x, y, z));
						}
					}
				}
				if (x == origX + 2 || x == origX + 4) {
					setBlock(w, x, origY+1, origZ, Blocks.glass);
					setBlock(w, x, origY+2, origZ, Blocks.glass);
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
			setBlock(ws, chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
			blocks.add(new Vec3d((int)player.posX-16, chasingQuestInitialPosY, z));
			setBlock(ws, chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
			blocks.add(new Vec3d((int)player.posX+16, chasingQuestInitialPosY, z));
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
		
		if(serverContext.isSuggestedGamePropertiesReady())
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

			if( (blockByDifficulty == Blocks.BRICK_BLOCK) || 
					(blockByDifficulty == Blocks.STONE) || 
					(blockByDifficulty == Blocks.GRAVEL) )
			{
				for(int idx = 0; idx<4; idx++)
				{
					switch(this.questChaseType)
					{
					case REGULAR:
						if(levelSys.getPlayerLevel() < 6)
							areas.add(terrainBiome.getRandomCityBiome());
						else
							areas.add(terrainBiomeFire.getRandomFieldBiome());
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
			else if(blockByDifficulty == Blocks.GRASS)
			{
				for(int idx = 0; idx<4; idx++)
				{
					switch(this.questChaseType)
					{
					case REGULAR:
						if(levelSys.getPlayerLevel() < 6)
							areas.add(terrainBiome.getRandomGrassBiome());
						else
							areas.add(terrainBiomeFire.getRandomGateBiome());
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
			else if(blockByDifficulty == Blocks.SAND)
			{
				for(int idx = 0; idx<4; idx++)
				{
					switch(this.questChaseType)
					{
					case REGULAR:
						if(levelSys.getPlayerLevel() < 6)
							areas.add(terrainBiome.getRandomDesertBiome());
						else
							areas.add(terrainBiomeFire.getRandomLavaFountainBiome());
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
				blockByDifficulty = Blocks.STONE;
			}
			
			for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
				for (int z = (int)player.posZ+48; z < (int)player.posZ+64; ++z) {
					setBlock(ws, x, chasingQuestInitialPosY-1, z, blockByDifficulty);
					blocks.add(new Vec3d(x, chasingQuestInitialPosY-1, z));
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
						if(terrainBiomeArea.map.get(terrainBiomeAreaIndex) == Blocks.WATER)
							setBlock(ws, terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex));
						else
							setBlock(ws, terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex), terrainBiomeAreaIndex.direction, 3);
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
						setBlock(ws, x, chasingQuestInitialPosY-1, z, Blocks.GRAVEL);
						blocks.add(new Vec3d(x, chasingQuestInitialPosY-1, z));
						setBlock(ws, x, chasingQuestInitialPosY-1, z-64, Blocks.GRASS);
					}
				}
			}
		}
	}

	private void generateObstacles()
	{
		// Check if the game is running
		if(!chasingQuestOnGoing)
			return;
		
		if(chasingQuestOnCountDown)
			return;
		
		// Obstacle Timer Check
		if(obstacleTime == 0)  // Start spawn an Obstacle
		{
			// Select Obstacle
			Random rand = new Random();
			
			int obstacleBiomeLevel = this.obstacleBiome.obstacleBiomeDef.areas.size();
			
			if(levelSys.getPlayerLevel() == 1)
				obstacleBiomeLevel = 2;
			else if(levelSys.getPlayerLevel() == 2)
				obstacleBiomeLevel = 4;
			else if(levelSys.getPlayerLevel() == 3)
				obstacleBiomeLevel = 5;
			else if(levelSys.getPlayerLevel() == 4)
				obstacleBiomeLevel = 6;
			else if(levelSys.getPlayerLevel() == 5)
				obstacleBiomeLevel = 6;
			else if(levelSys.getPlayerLevel() == 6)
				obstacleBiomeLevel = 7;
			else if(levelSys.getPlayerLevel() == 7)
				obstacleBiomeLevel = 8;
			else if(levelSys.getPlayerLevel() == 8)
				obstacleBiomeLevel = 8;
			else if(levelSys.getPlayerLevel() == 9)
				obstacleBiomeLevel = 9;

			this.obstacleId = rand.nextInt(obstacleBiomeLevel);
		}
		else if(obstacleTime > -3)
			return;
		else if(obstacleTime == -3)  // Start spawn an Obstacle
		{
			// Refresh Obstacle Spawn Time
			this.obstacleTime = obstacleRefreshed;
			
			System.out.println("this.obstacleId["+this.obstacleId+"]");;
			
			// Get Obstacle
			TerrainBiomeArea terrainBiomeArea = this.obstacleBiome.getObstacleBiomeByIndex(this.obstacleId);
			
			// Spawn Obstacle
			int x = (int)npc.posX;
			int y = chasingQuestInitialPosY;
			int z = (int)npc.posZ-4;
			
			for(TerrainBiomeAreaIndex terrainBiomeAreaIndex : terrainBiomeArea.map.keySet())
			{
				if(terrainBiomeArea.map.get(terrainBiomeAreaIndex) == Blocks.WATER)
					setBlock(ws, terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex));
				else
					setBlock(ws, terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex), terrainBiomeAreaIndex.direction, 3);
			}
		}
	}
	
	private void generateDroppedItems() {
		ws = MinecraftServer.getServer().worldServerForDimension(this.questDestinationDimensionId);
		
		Random randomNumber = new Random();
		
		int x = (int)player.posX;
		int y = (int)player.posY+10;
		int z = (int)player.posZ+56;
		
		double randDouble = 0;
		
		Item randomItem;
		
		// Decision to place or not (66% chance spawn)
		if(randomNumber.nextDouble() > 0.66)
		{
			return;
		}
		
		// Select Region (left, right (1 out of 3 regions)
		randDouble = randomNumber.nextDouble();
		if(randDouble < 0.33)
		{
			x -= 8;
		}
		else if(randDouble > 0.66)
		{
			x += 8;
		}
		
		// Select What Item (Gold Ingot(10%), Blaze Powder(35%) or feather(55%))
		randDouble = randomNumber.nextDouble();
		if(randDouble < 0.1)
		{
			randomItem = Items.gold_ingot;
		}
		else if(randDouble < 0.45)
		{
			randomItem = Items.blaze_powder;
		}
		else
		{
			randomItem = Items.feather;
		}
		
		EntityItem item = new EntityItem(ws, x, y, z, new ItemStack(randomItem,1));
		ws.spawnEntityInWorld(item);
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
		System.out.println("Checking if Chasing Quest is Complete");
		
		if(chasingQuestOnGoing)
		{
			if (thiefHealthCurrent <= 0) 
			{
				flagAccomplished = true;
				chasingQuestOnGoing = false;
			}
			
			if(isExitSelected)
			{
				flagGiveup = true;
				chasingQuestOnGoing = false;
			}
			
			// CHASE QUEST LOSE CONDITION
			if(timeFallBehind >= 30)
			{
				flagFallBehind = true;
				chasingQuestOnGoing = false;
			}
		}
		else if(isRewardState) {
			npc.setDead();
			
			if (endOfChaseItemCounter > 0) {
				endOfChaseItemCounter--;
				
				List<EntityItem> nearbyItems = npc.worldObj.getEntitiesWithinAABB(EntityItem.class,
						AxisAlignedBB.getBoundingBox(npc.posX-5, npc.posY-2, npc.posZ-5, npc.posX+5, npc.posY+2, npc.posZ+5));
				List<EntityXPOrb> nearbyOrbs = npc.worldObj.getEntitiesWithinAABB(EntityXPOrb.class,
						AxisAlignedBB.getBoundingBox(npc.posX-5, npc.posY-2, npc.posZ-5, npc.posX+5, npc.posY+2, npc.posZ+5));
				
				if (nearbyItems.size() == 0 && nearbyOrbs.size() == 0) {
					endOfChaseItemCounter = 0;
				}
			}
			else if(endOfChaseItemCounter == 0)
			{
				flagLeave = true;
			}
			
			if(isExitSelected)
			{
				flagLeave = true;
				chasingQuestOnGoing = false;
			}
			
			if(isRetrySelected)
			{
				flagRetry = true;
				chasingQuestOnGoing = false;
			}
			
			if(isContinueSelected)
			{
				flagContinue = true;
				chasingQuestOnGoing = false;
			}
		}
		
		if(isRetrySelected)
		{
			flagRetry = true;
			chasingQuestOnGoing = false;
		}
	}
	
	public void countdownTick()
	{
		System.out.println("isClient[" + player.world.isRemote + "] countdown[" + countdown + "]");
		
		long timeNow = System.currentTimeMillis();
		
		if(countdown > 0){	
			// REMOVE existing NPC
			if (countdown == 4) {
				for (Object o : player.world.loadedEntityList) {
					if (((Entity)o) instanceof EntityCustomNpc) {
						((EntityCustomNpc)o).delete();
					}
				}
				
				player.rotationPitch = 0f;
				player.rotationYaw = 0f;
				
				/**
				 * PLAYING MUSIC FOR CHASING QUEST
				 */
				
				if (thiefLevel < 2 || thiefLevel > 3)
					chosenSong = "minebike:mus_metal";
				else
					chosenSong = "minebike:mus_breaks";

//				Minecraft.getMinecraft().player.sendChatMessage("/playsoundb " + chosenSong + " loop");
				Minecraft.getMinecraft().player.sendChatMessage("/playsoundb " + "minebike:mus_metal" + " loop @p 0.5f");
				
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
				setThiefLevel(levelSys.getPlayerLevel());
				System.out.println("Thief's level is: " + getThiefLevel());
				thiefLevelSet = true;
			}
			// SPAWN surrounding
			else if(countdown == 3)
			{	
				for(int row=0; row<3; row++)
				{
					ArrayList<TerrainBiomeArea> areas = new ArrayList<TerrainBiomeArea>();
					areas.add(terrainBiome.getRandomGrassBiome());
					areas.add(terrainBiome.getRandomGrassBiome());
					areas.add(terrainBiome.getRandomGrassBiome());
					areas.add(terrainBiome.getRandomGrassBiome());
					
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
						int z = (int)player.posZ+5 + row*9;
						
						TerrainBiomeArea terrainBiomeArea = areas.get(idx);
						
						for(TerrainBiomeAreaIndex terrainBiomeAreaIndex : terrainBiomeArea.map.keySet())
						{
							if(terrainBiomeArea.map.get(terrainBiomeAreaIndex) == Blocks.WATER)
								setBlock(ws, terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex));
							else
								setBlock(ws, terrainBiomeAreaIndex.x + x, terrainBiomeAreaIndex.y + y, terrainBiomeAreaIndex.z + z, terrainBiomeArea.map.get(terrainBiomeAreaIndex), terrainBiomeAreaIndex.direction, 3);
						}
					}
				}
			}
			// SPAWN a monster
			else if (countdown == 1) 
			{
				if(player.world.isRemote) {
				}
				else
				{
					System.out.println("if (countdown == 1)");
					
					GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseShowup);
					GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseHintWeapon);
					
					if (isBoss) {
						npc = NpcCommand.spawnNpc(0, 11, 5, ws, "Ifrit", "customnpcs:textures/entity/humanmale/Evil_Gold_Knight.png");
					} else {
						if(thiefLevel >= monsterTypes.size())
							thiefLevel = monsterTypes.size()-1;

						ArrayList<String> monsterType = monsterTypes.get(thiefLevel-1);
						npc = NpcCommand.spawnNpc(0, 11, 5, ws, monsterType.get(0), monsterType.get(2));
					}
					
					npc.ai.stopAndInteract = false;
					setNpc(npc);
					
					npc.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
					
					command = new NpcCommand(serverContext, npc);

				    npc.setHealth(10000f);
					npc.faction.neutralPoints = 2000;
					npc.faction.friendlyPoints = 3000;
					npc.faction.defaultPoints = 2500;
					npc.faction.attackFactions.add(player);
					npc.ai.canLeap = true;
					npc.attackEntityAsMob(player);
					
					System.out.print("[BiGX] Faction id[" + npc.getFaction().id + "] attacked[" + npc.getFaction().getsAttacked + "] AF[" + npc.getFaction().attackFactions + "]");
					
					npc.setFaction(2);
				    npc.setHealth(10000f);
					
					System.out.print("[BiGX] Faction id[" + npc.getFaction().id + "] attacked[" + npc.getFaction().getsAttacked + "] AF[" + npc.getFaction().attackFactions + "]");
					
					command.enableMoving(false);
					
					setNpcCommand(command);
				}
				
				isNpcFellDown = false;
				npcFellDownTimestamp = 0;
			}
			
			if (countdown == 1)
			{	
			    npc.setHealth(10000f);
			}
		} else {
			chasingQuestOnCountDown = false;
//			System.out.println("GO!");
			command.enableMoving(true);

			System.out.println("Thief Level on GO" + thiefLevel);
			countdown = 5;
			lastCountdownTickTimestamp = 0;
			initialDist = 20; // HARD CODED
			pausedTime = 0;
			ClientEventHandler.animTickChasingFade = 0;

			Minecraft mc = Minecraft.getMinecraft();
			
			if(mc.currentScreen == null)
				mc.displayGuiScreen(new GuiMonsterReadyFight(BiGX.instance().clientContext, mc));
		}
		
		// COUNT DOWN TIME
		{
			System.out.println("[BiGX] Count down from fq by one");
			countdown --;
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
		if(countdown == 5)
		{
			pausedTime = 0;
			questTimeStamp = timeNow;
			countdown = 4;
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
			if(speedUpEffectTickCount > 0)
				speedUpEffectTickCount--;
			
			if(damageUpEffectTickCount > 0)
				damageUpEffectTickCount--;
			
			if(thiefSpeedUpEffectTickCount > 0)
				thiefSpeedUpEffectTickCount --;
			
			if((time > 60*7) && (time > (npcFellDownTimestamp + npcFellDownLength + npcFellDownPeriod)))
			{
//				npcFellDownFlag = true;
				isNpcFellDown = true;
				NpcCommand.hasFallen = true;
				NpcCommand.isSiting = false;
				playFellDownSound();
				npcFellDownTimestamp = time;
			}
			else
			{
				if(isNpcFellDown)
				{
					if(time > (npcFellDownTimestamp + npcFellDownLength))
					{
						NpcCommand.hasFallen = false;
						NpcCommand.isSiting = false;
						isNpcFellDown = false;
					}
					else if(time > (npcFellDownTimestamp + npcFellDownLength - 1))
					{
						NpcCommand.isSiting = true;
					}
				}
				else
				{
					NpcCommand.hasFallen = false;
					NpcCommand.isSiting = false;
				}
			}
			
			if(sprintTickCount > 0)
			{
				sprintTickCount --;
			}
			else
			{
				sprintTickCountMax = ((new Random()).nextInt(5) + sprintTickCountMinMax) * 12; 
				sprintTickCount = sprintTickCountMax;
				thiefSpeedUpEffectTickCount = thiefSpeedUpEffectTickCountMax;
				player.world.playSoundAtEntity(player, "minebike:getawayfromme", 1.0f, 1.0f);
			}
			
			if(positionSelectionTickCount > 0)
			{
				positionSelectionTickCount --;
			}
			else
			{
				int nextPosition = (new Random()).nextInt(2) - 1;
				positionSelectionTickCount = positionSelectionTickCountMax;
				command.setRunStartX(nextPosition);
				player.world.playSoundAtEntity(player, "minebike:boop", 1.0f, 1.0f);
			}
			
			long timeNow = System.currentTimeMillis();
			if( (timeNow - lastTickTime - pausedTime) < 125 )
			{
			}
			else if(lastTickStage == 0)
			{
				if(thiefHealthCurrent > (thiefHealthMax*.8f))
					obstacleRefreshed = 4;
				else if(thiefHealthCurrent > (thiefHealthMax*.6f))
					obstacleRefreshed = 3;
				else if(thiefHealthCurrent > (thiefHealthMax*.4f))
					obstacleRefreshed = 3;
				else if(thiefHealthCurrent > (thiefHealthMax*.2f))
					obstacleRefreshed = 2;
				else
					obstacleRefreshed = 2;
					
//				System.out.println("GENERATING");
				
				lastTickStage ++;
				
				// Make Obstacles and structures on the side
				this.generateStructuresOnSides();
				this.generateTerrainByPatientProfile();
				this.generateDroppedItems();
				this.generateObstacles();

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				if(isNpcFellDown)
				{
					command.setSpeed(0);
				}
				else if((player.posZ-2) > npc.posZ) {
					timeFallBehind = 0;
					int tempThiefSpeed = NPCRUNNINGSPEED + 3;
					command.setSpeed(tempThiefSpeed);
				}
				else if ( (dist > 40) || (time > 60 * 6) ) {
					warningMsgBlinkingTime = System.currentTimeMillis();
					timeFallBehind++;
					int tempThiefSpeed = (int)(NPCRUNNINGSPEED * .3);
					if( (thiefSpeedUpEffectTickCount > 0) && (thiefLevel>1) )
						tempThiefSpeed *= NPCRUNNINGSPEEDBOOSTRATE;
					command.setSpeed(tempThiefSpeed);
				}
				else{
					timeFallBehind = 0;
					int tempThiefSpeed = NPCRUNNINGSPEED;
					
					if (time > 60*5) {
						tempThiefSpeed = (int)(NPCRUNNINGSPEED * .7);
					}
					else if (time > 6*45) {
						tempThiefSpeed = (int)(NPCRUNNINGSPEED * .8);
					}
					else if (time > 60*4) {
						tempThiefSpeed = (int)(NPCRUNNINGSPEED * .9);
					}
					
//					if (dist > 40) {
//						tempThiefSpeed = (int)(NPCRUNNINGSPEED * .4);
//					}
					if (dist > 20) {
						tempThiefSpeed = (int)(NPCRUNNINGSPEED * .7);
					}
					
					if( (thiefSpeedUpEffectTickCount > 0) && (thiefLevel>1) )
						tempThiefSpeed *= NPCRUNNINGSPEEDBOOSTRATE;
					command.setSpeed(tempThiefSpeed);
				}
				
				this.time++;
				this.obstacleTime--;
			}
			else if(lastTickStage == 1)// && !npc.isDead)
			{
				lastTickStage ++;
			}
			else if(lastTickStage == 2)// && !npc.isDead)
			{
				lastTickStage ++;
			}
			else if(lastTickStage == 3)
			{
//				System.out.println("CLEANING 1");
				
				lastTickStage++;

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				// CLEAN the terrain behind
				/**
				 * Terrain Cleaning
				 */
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ-128, (int)player.posZ-92);
				/**
				 * END OF Terrain Cleaning
				 */
			}
			else if(lastTickStage == 4)
			{
//				System.out.println("CLEANING 2");

				lastTickStage++;

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				// CLEAN the terrain behind
				/**
				 * Terrain Cleaning
				 */
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ-92, (int)player.posZ-56);
				/**
				 * END OF Terrain Cleaning
				 */
			}
			else if(lastTickStage == 5)
			{
//				System.out.println("CLEANING 3");
				
				lastTickStage++;

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				// CLEAN the terrain behind
				/**
				 * Terrain Cleaning
				 */
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ-56	, (int)player.posZ-24);
				/**
				 * END OF Terrain Cleaning
				 */
			}
			else if(lastTickStage == 6)
			{
//				System.out.println("CLEANING 4");
				
				lastTickStage = 0;

				this.pausedTime = 0;
				this.lastTickTime = System.currentTimeMillis();
				
				// CLEAN the terrain behind
				/**
				 * Terrain Cleaning
				 */
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ-24, (int)player.posZ-20);
				/**
				 * END OF Terrain Cleaning
				 */
			}
		}
	}
	
	private void playFellDownSound() {
		int randomNumber = (new Random()).nextInt() % 4 + 1;
		player.world.playSoundAtEntity(player, "minebike:hit" + randomNumber, 1.0f, 1.0f);
	}

	public void handlePlayTimeOnClient()
	{
		// SPEED CHANGE LOGIC BASED ON THE HEART RATE AND THE RPM OF THE PEDALLING
		BigxClientContext context = (BigxClientContext) this.clientContext;
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
		
		if(speedUpEffectTickCount > 0)
		{
			System.out.println("SPEED BOOST");
			speedchange *= 1.5f;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		
//		if(mc.getMinecraft().objectMouseOver != null) {
//			if(mc.getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
//				mc.getMinecraft().playerController.attackEntity(mc.player, mc.getMinecraft().objectMouseOver.entityHit);
//			}
//		}
//		player.swingItem();
	}

	private static void setBlock(World world, int x, int y, int z, Block block)
	{
		if(Minecraft.getMinecraft().player.worldObj.provider.dimensionId == WorldProviderFlats.dimID)
			world.setBlock(x, y, z, block);
	}
	
	private void setBlock(World world, int x, int y, int z, Block block, int direction, int l) 
	{
		if(Minecraft.getMinecraft().player.worldObj.provider.dimensionId == WorldProviderFlats.dimID)
			world.setBlock(x, y, z, block, direction, 3);
	}
	
	private static void cleanBlock(World world, int x, int y, int z, Block block)
	{
		if(Minecraft.getMinecraft().player.worldObj.provider.dimensionId == WorldProviderFlats.dimID)
			world.setBlock(x, y, z, block);
	}

	@Override
	public void run()
	{
		System.out.println("[BiGX] Quest Fight And Chasing Task Started");
 		synchronized (questManager) {
			init();

			while(isActive)
			{	
				if(checkChasingQuestTaskActivityFlags() == 2)
					continue;

				checkComboCount();
				
				if(chasingQuestOnGoing)
				{	
					if(chasingQuestOnCountDown)
					{
						if(npc != null)
							npc.setHealth(10000f);
						
						handleCountdown();
					}
					else if(getThiefHealthCurrent() < (getThiefHealthMax() * 0.75))
					{
						if(npc != null)
							npc.setHealth(10000f);
					    
						if(!isStunnedGuiHappend)
						{
							isStunnedGuiHappend = true;

							npc.ai.canLeap = false;
						    npc.setHealth(10000f);
						    npc.faction.attackFactions.remove(player);
						    npc.ai.avoidsWater = true;
						    npc.ai.onAttack = 3;
						    npc.setResponse();

							int tempThiefSpeed = NPCRUNNINGSPEED;
							if(thiefSpeedUpEffectTickCount > 0)
								tempThiefSpeed *= NPCRUNNINGSPEEDBOOSTRATE;
							command.setSpeed(tempThiefSpeed);
							command.runInDirection(EnumFacing.SOUTH);

							// OPEN Monster Stunned GUI
							Minecraft mc = Minecraft.getMinecraft();
							if(mc.currentScreen == null)
								mc.displayGuiScreen(new GuiMonsterStunned(BiGX.instance().clientContext, mc));
						}
						
//						System.out.println("[BiGX] FQ AF[!chasingQuestOnCountDown]");

						player.setHealth(player.getMaxHealth());
						if(!player.world.isRemote)
						{
							handlePlayTimeOnServer();
							
							if(updateProgressStats((float)player.posZ, (float)player.getDistanceToEntity(npc)))
								audioFeedback.updateState(getAudioFeedbackEnum((float)player.posZ, (float)npc.posZ, distMovedLastFewSeconds, damageLastFewSeconds, (damageLastFewSeconds>0)));
						}
						else
						{
							System.out.println("handlePlayTimeOnClient");
							handlePlayTimeOnClient();
						}
						
						if( (npc.motionZ == 0) && (!npc.isDead) )
						{
							command.correctRunningDirection(EnumFacing.SOUTH);
						}
					}
					else if(getThiefHealthCurrent() > 0)
					{
						if(npc != null)
							npc.setHealth(10000f);
					}
					
					Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
				}
				
				if(!player.world.isRemote)
				{
					((BigxServerContext)serverContext).updateQuestInformationToClient((BigxServerContext)serverContext);
				}
				
				try {
//					System.out.println("[BiGX] questManager wait");
					questManager.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("[BiGX] Logic out");

			time = 0;
			initThiefStat();
			countdown = 5;
			pausedTime = 0;
			((BigxServerContext)serverContext).updateQuestInformationToClient(null);
		}
	}
	
	private static boolean updateProgressStats(float playerPosZ, float distToNpc) {
		boolean returnValue = false;
		
		if((System.currentTimeMillis() - progressStatTimeStamp) > 2000)
		{
			progressStatTimeStamp = System.currentTimeMillis();
			distMovedLastFewSeconds = playerPosZ - playerPosZLastFewSecondsAgo;
			playerPosZLastFewSecondsAgo = playerPosZ;
			distToNpcLastFewSeconds += distToNpc;
			
			returnValue = true;
		}
		
		return returnValue;
	}

	private AudioFeedBackEnum getAudioFeedbackEnum(float playerPosZ, float npcPosZ, float distMovedLastFewSeconds, float damageLastFewSeconds, boolean hitFlag) // Every Two Seconds
	{
		AudioFeedBackEnum returnValue = AudioFeedBackEnum.REGULAR;
		
		// 6) Good Job
		if(hitFlag)
		{
			returnValue = AudioFeedBackEnum.GOODJOB;
			QuestTaskFightAndChasing.damageLastFewSeconds = 0;
		}
		else {
			// 2) Being ahead
			if(npcPosZ < playerPosZ)
			{
				returnValue = AudioFeedBackEnum.AHEAD;
			}
			
			// 3) Stuck an obstacles
			else if(distMovedLastFewSeconds < 4)
			{
				System.out.println("BIGX Audio Effect NOMOVE["+distMovedLastFewSeconds+"]");
				returnValue = AudioFeedBackEnum.NOMOVE;
			}
			
			// 4) No able to hit him
			else if( (damageLastFewSeconds < 2) && (distToNpcLastFewSeconds < 20) )
			{
				System.out.println("BIGX Audio Effect NOHIT["+distToNpcLastFewSeconds+"]");
				returnValue = AudioFeedBackEnum.NOHIT;
			}
			
			// 5) Not too far but no progress
			else if(distToNpcLastFewSeconds < 100)
			{
				System.out.println("BIGX Audio Effect NOTCLOSEENOUGH["+distToNpcLastFewSeconds+"]");
				returnValue = AudioFeedBackEnum.NOTCLOSEENOUGH;
			}
		}
		
		this.distMovedLastFewSeconds = 0;
		this.distToNpcLastFewSeconds = 0;
		
		return returnValue;
	}
	
	private int checkChasingQuestTaskActivityFlags() {
		int returnValue = 1;
		
		if(flagAccomplished)
		{
			// Play Monster Scream Sound
			player.world.playSoundAtEntity(player, "minebike:monsterdeath", 1.0f, 1.0f);
			
			System.out.println("flagAccomplished");
			
			flagAccomplished = false;
			isRewardState = true;
			returnValue = 2;		// ACCOMPLISHED

			levelSys.levelUp();
			
			this.sendQuestGameTag(QuestActivityTagEnum.ACCOMPLESHED);
			endingZ = (int)player.posZ;
			LeaderboardRow row = new LeaderboardRow();
			row.name = player.getDisplayName();
			row.level = Integer.toString(thiefLevel);
			row.time_elapsed = "" + time;
			row.combo = "" + bestCombo;
			
			command.setSpeed(0);
			npc.setDead();
			
			// REWARD ITEMS (Add items with npc.entityDropItem(new ItemStack(), 0.0F) )
			endOfChaseItemCounter = 10;
			
			Random random = new Random();
			
			int totalXP = 15;
			int xpGiven; // ignore pls
			
			String specialItemName = "";
			
			while (totalXP > 0) {
		         xpGiven = random.nextBoolean() || totalXP == 1 ? 1 : 2;
		         totalXP -= xpGiven;
		         npc.worldObj.spawnEntityInWorld(new EntityXPOrb(npc.worldObj, npc.posX, npc.posY, npc.posZ, xpGiven));
		    }
			
			npc.entityDropItem(new ItemStack(Items.apple), random.nextInt(16)/8F);
			npc.entityDropItem(new ItemStack(Items.apple), random.nextInt(16)/8F);
			for (int i = 0; i < BiGXEventTriggers.convertCoinsToGold(virtualCurrency); ++i) {
				npc.entityDropItem(new ItemStack(Items.gold_ingot), 1);//, random.nextInt(16)/8F);
			}
			//Combo Bonus
			for (int i = 0; i < (bestCombo/2); ++i){
				npc.entityDropItem(new ItemStack(Items.gold_ingot), 1);
			}
			
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldBarInfo);
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldSpendWisely);

			Minecraft mc = Minecraft.getMinecraft();
			if(mc.currentScreen == null)
				mc.displayGuiScreen(new GuiVictory(BiGX.instance().clientContext, mc, totalXP, BiGXEventTriggers.convertCoinsToGold(virtualCurrency), (bestCombo/2), specialItemName));
		}
//		else if(flagOpenQuestMenuGui)
//		{
//			flagOpenQuestMenuGui = false;
//			
//			Minecraft mc = Minecraft.getMinecraft();
//			System.out.println(serverContext instanceof BigxServerContext);
//			GuiFinishChasingQuest gui = new GuiFinishChasingQuest(true); 
//
////			if(mc.currentScreen == null) {
////				mc.displayGuiScreen(gui);
////			}
//			
//			// Set another 15 second timer to open gui
//			endOfChaseItemCounter = 10;
//		}
		else if(flagGiveup)
		{
			flagGiveup = false;
			
			System.out.println("[BiGX] Player gives up the quest");
			
			this.sendQuestGameTag(QuestActivityTagEnum.FAILED);

			flagLeave = true;
		}
		else if(flagFallBehind)
		{
			flagFallBehind = false;
			
			System.out.println("[BiGX] Player fall behind too far");
			
			this.sendQuestGameTag(QuestActivityTagEnum.FAILED);

			flagLeave = true;
		}
		else if(flagRetry)
		{
			flagRetry = false;
			
			System.out.println("isRetrySelected");
			isContinueSelected = false;
			isRetrySelected = false;
			isExitSelected = false;
			
			init();
			
			QuestTeleporter.teleport(player, 100, 1, 11, 0);

			chasingQuestInitialPosX = 1;
			chasingQuestInitialPosY = 10;
			chasingQuestInitialPosZ = 0;
			
			blocks = new ArrayList<Vec3d>();
			
			for (int z = -16; z < (int)player.posZ+64; ++z) {
				setBlock(ws, chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
				blocks.add(new Vec3d((int)player.posX-16, chasingQuestInitialPosY, z));
				setBlock(ws, chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
				blocks.add(new Vec3d((int)player.posX+16, chasingQuestInitialPosY, z));
			}
			for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
				setBlock(ws, x, chasingQuestInitialPosY, -16, Blocks.fence);
				blocks.add(new Vec3d(x, chasingQuestInitialPosY, -16));
			}
			
			for (int z = (int)player.posZ; z < (int)player.posZ+64; ++z) {
				setBlock(ws, chasingQuestInitialPosX-16, chasingQuestInitialPosY-2, z, Blocks.fence);
				blocks.add(new Vec3d((int)player.posX-16, chasingQuestInitialPosY-2, z));
				setBlock(ws, chasingQuestInitialPosX+16, chasingQuestInitialPosY-2, z, Blocks.fence);
				blocks.add(new Vec3d((int)player.posX+16, chasingQuestInitialPosY-2, z));
			}
			
			chasingQuestOnGoing = true;
			chasingQuestOnCountDown = true; 
			questTimeStamp = System.currentTimeMillis();

			// Need to remove the previous thief
			if(npc != null)
				command.removeNpc(npc.display.name, WorldProviderFlats.dimID);
		}
		else if(flagLeave)
		{
			System.out.println("[bigx] Leave the quest");
			
			isContinueSelected = false;
			isRetrySelected = false;
			isExitSelected = false;
			
			isActive = false;
			completed = false;

			goBackToTheOriginalWorld(ws, player);
		}
		else if(flagContinue)
		{
			System.out.println("[Bigx] This button is not implemented yet.");
			
			int tCurrentLevel = levelSys.getPlayerLevel();
			
			flagRetry = true;
			returnValue = 0;
		}
		else
		{
			returnValue = 0;
		}
		
//		resetChasingQuestTaskActivityFlags();
		
		return returnValue;
	}
	
	private void resetChasingQuestTaskActivityFlags() {
		flagAccomplished = false;
		flagContinue = false;
		flagFallBehind = false;
		flagGiveup = false;
		flagRetry = false;
		flagOpenQuestMenuGui = false;
		flagLeave = false;
	}
	
	@Override
	public void init()
	{
		World world = player.world;
		isStunnedGuiHappend = false;
		time = 0;
		virtualCurrency = 0;
		initThiefStat();
		completed = false;
		countdown = 5;
		pausedTime = 0;
		comboCount = 0;
		bestCombo = 0;
		isRewardState = false;
		thiefLevelSet = false;
		
		resetChasingQuestTaskActivityFlags();
		
		questSettings = new ArrayList<Integer>();
		StageSettings stagesettings;
		List<Stage> stageList;
		stageList = new ArrayList<Stage>();
		questSettings.add(0);
		
		clientContext = BiGX.instance().clientContext;
		serverContext = BiGX.instance().serverContext;
		
		if(serverContext.suggestedGameProperties != null)
		{
			if(serverContext.suggestedGameProperties.getQuestProperties().getStageSettingsArray().size() != 0)
			{
				stagesettings = serverContext.suggestedGameProperties.getQuestProperties().getStageSettingsArray().get(0);
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
	}
	
	@Override
	public void onNpcInteraction(PlayerInteractEvent.EntityInteract event) {
		System.out.println("Interacting with NPC During Quest FQ");
	}
	
	@Override
	public void onItemUse(Start event) {
		synchronized (questManager) {
			player = event.entityPlayer;
			
			if(!player.world.isRemote)
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
				
				if (player.getHeldItem().getDisplayName().contains("Teleportation Potion")
						&& player.dimension == this.questDestinationDimensionId)
				{
					// CHASE QUEST LOSE CONDITION
					if (ws != null && player instanceof EntityPlayerMP) {
						isActive = false;
						virtualCurrency = 0;
						time = 0;
						chasingQuestOnCountDown = false;
						chasingQuestOnGoing = false;
						countdown = 5;
						pausedTime = 0;
						initThiefStat();
//						cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ - 128, (int)player.posZ);
						completed = false;
//						player.setGameType(GameType.SURVIVAL);
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
			if(questManager.getActiveQuestId() != Quest.QUEST_ID_STRING_FIGHT_CHASE)
				return;
			
			if( (chasingQuestOnGoing) && (!chasingQuestOnCountDown) )
			{
				if (event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] == null)
					deductThiefHealth(null);
				else
					deductThiefHealth(event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem].getItem());
				
				npc.setHealth(10000f);
				
				damageLastFewSeconds ++;
				
				if((damageLastFewSeconds % 5) == 0)
				{
					playAudioFeedback(AudioFeedBackEnum.GOODJOB);
				}
				
				if( (getThiefHealthCurrent() < (getThiefHealthMax() * 0.75)) && (getThiefHealthCurrent() > 0))
				{
					if( (npc.motionZ == 0) && (!npc.isDead) )
					{
						command.correctRunningDirection(EnumFacing.SOUTH);
					}
				}
			}

			// Play Monster Hit Sound
			int randomNumber = (new Random()).nextInt() % 4 + 1;
			player.world.playSoundAtEntity(player, "minebike:hit" + randomNumber, 1.0f, 1.0f);
		}
	}

	@Override
	public void unregisterEvents() {
		QuestEventHandler.unregisterQuestEventAttack(this);
		
		if(!player.world.isRemote){
			QuestEventHandler.unregisterQuestEventItemUse(this);
			QuestEventHandler.unregisterQuestEventCheckComplete(this);
			QuestEventHandler.unregisterQuestEventItemPickUp(this);
			QuestEventHandler.unregisterQuestEventNpcInteraction(this);
		}
	}
	
	@Override
	public void registerEvents() {
		QuestEventHandler.registerQuestEventAttack(this);
		if(!player.world.isRemote)
		{
			QuestEventHandler.registerQuestEventItemUse(this);
			QuestEventHandler.registerQuestEventCheckComplete(this);
			QuestEventHandler.registerQuestEventItemPickUp(this);
			QuestEventHandler.registerQuestEventNpcInteraction(this);
		}
	}

	@Override
	public String getTaskDescription() {
		return "Defeat the Thief and retrive the gold.";
	}

	@Override
	public String getTaskName() {
		return QuestTaskFightAndChasing.class.toString();
	}

	public int getComboCount()
	{
		return comboCount;
	}
	
	public void checkComboCount()
	{
		long currentTime = System.currentTimeMillis();
		
		if( (currentTime - comboTime) >= comboTimeLimit )
			comboCount = 0;
	}
	
	private static ArrayList<ArrayList<String>> GetMonsterTypes() {
		ArrayList<ArrayList<String>> types = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> type1 = new ArrayList<String>();
		type1.add(villainNames[0]);
		type1.add("50");
		type1.add("customnpcs:textures/entity/monstermale/ZombieSteve.png");
		ArrayList<String> type2 = new ArrayList<String>();
		type2.add(villainNames[1]);
		type2.add("80");
		type2.add("customnpcs:textures/entity/monstermale/ZombieSteve.png");
		ArrayList<String> type3 = new ArrayList<String>();
		type3.add(villainNames[2]);
		type3.add("150");
		type3.add("customnpcs:textures/entity/orcmale/GenericOrc1.png");
		ArrayList<String> type4 = new ArrayList<String>();
		type4.add(villainNames[3]);
		type4.add("400");
		type4.add("customnpcs:textures/entity/orcmale/MercenaryOrc1.png");
		
		types.add(type1);
		types.add(type2);
		types.add(type3);
		types.add(type4);
		
		return types;
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

	public static int getSpeedBoostTickCountLeft(){
		return speedUpEffectTickCount;
	}
	
	public static int getDamageBoostTickCountLeft() // Each ticks are 50 ms
	{
		return damageUpEffectTickCount;
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
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event) {
		if(event.item.getEntityItem().getItem() == Items.feather)
		{
			player.world.playSoundAtEntity(player, "minebike:powerup", 1.0f, 1.0f);
			System.out.println("speedUpEffectTickCount refresh");
			// Speed Up Effect On
			speedUpEffectTickCount = speedUpEffectTickCountMax;
		}
		else if(event.item.getEntityItem().getItem() == Items.blaze_powder)
		{
			player.world.playSoundAtEntity(player, "minebike:powerup", 1.0f, 1.0f);
			System.out.println("damageUpEffectTickCount refresh");
			// Power Up Effect On
			damageUpEffectTickCount = damageUpEffectTickCountMax;
		}
	}

	@Override
	public void onRewardSessionContinueClicked() {
		System.out.println("Continue Clicked");
		isContinueSelected = true;
	}

	@Override
	public void onRewardSessionRetryClicked() {
		System.out.println("Retry Clicked");
		isRetrySelected = true;
	}

	@Override
	public void onRewardSessionExitClicked() {
		System.out.println("Exit Clicked");
		isExitSelected = true;
	}

	@Override
	public void playAudioFeedback(AudioFeedBackEnum audioFeedBackEnum) {
		System.out.println("[BiGX] playAudioFeedback");
		String text = "";
		String audioEffectName = "focus";
		
		int randomNumber = new Random().nextInt(AudioFeedback.audioFeedbackLib[audioFeedBackEnum.getValue()].length);
		
		audioEffectName = AudioFeedback.audioFeedbackLib[audioFeedBackEnum.getValue()][randomNumber][0];
		text = AudioFeedback.audioFeedbackLib[audioFeedBackEnum.getValue()][randomNumber][1];
		
		GuiStats.showCheeringMessage(text);
		playAudioFeedBack(audioEffectName);
	}

	private void playAudioFeedBack(String audioEffectName) {
		Minecraft.getMinecraft().player.playSound("minebike:" + audioEffectName, 1.5f, 1.0f);
	}
}
