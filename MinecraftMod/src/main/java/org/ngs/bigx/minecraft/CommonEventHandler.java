package org.ngs.bigx.minecraft;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.dictionary.objects.game.properties.Stage;
import org.ngs.bigx.dictionary.objects.game.properties.StageSettings;
import org.ngs.bigx.dictionary.protocol.Specification.GameTagType;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiLeaderBoard;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.levelUp.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.npcs.NpcEvents;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiome;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.fire.TerrainBiomeFire;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.utility.NpcCommand;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.entity.EntityCustomNpc;


public class CommonEventHandler {

	static float playerQuestPitch, playerQuestYaw;

	int server_tick = 0;
	boolean serverQuestTest = true;
	int serverQuestTestTickCount = 10;
	private static int countdown = 10;
	private static int time = 0;
	private static double elapsedTime = 0;
	private static int timeFallBehind = 0;
	EntityCustomNpc activenpc;
	NpcCommand activecommand;
	public static float initialDist, dist = 0;
	public static int startingZ, endingZ;
	boolean doMakeBlocks;
	float ratio;
	Vec3 returnLocation;
	
	private static Context context;
	CharacterProperty characterProperty = BiGX.instance().characterProperty;;
	EntityCustomNpc npc;
	NpcCommand command;
	
	public static final float chaseRunBaseSpeed = 2.1f; // 157 blocks per 15 seconds!!
	public static float speedchange = 0f;
	private final float chaseRunSpeedInBlocks = 157f/15f;
	public static boolean chasingQuestOnGoing = false;
	public static boolean chasingQuestOnCountDown = false;
	public static int virtualCurrency = 0;
	public static long warningMsgBlinkingTime = System.currentTimeMillis();
	public static LevelSystem levelSys = new LevelSystem();
	private static TerrainBiome terrainBiome = new TerrainBiome();
	private static TerrainBiomeFire terrainBiomeFire = new TerrainBiomeFire();
	
	private static ArrayList<Integer> questSettings = null;

	private static int chasingQuestInitialPosX = 0;
	private static int chasingQuestInitialPosY = 0;
	private static int chasingQuestInitialPosZ = 0;
	
	private static Timer t = null;
	private static Timer t2 = null;
	private static Timer t3 = null;
	private static QuestTeleporter teleporter = null;

	
	private WorldServer ws;
	public static int getTime()
	{
		return time;
	}
	
	public static int getCountdown()
	{
		return countdown;
	}
	
	public static int getTimeFallBehind()
	{
		return timeFallBehind;
	}	

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		event.world.provider.setWorldTime(0);
		//System.out.println(event.world.provider.dimensionId);
		if (event.world.provider.dimensionId == 0){
			System.out.println("DIMENSION ID == 0");
			
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
			
			// NPC CHECKING
			for (String name : NpcDatabase.NpcNames()) {
				int found = 0;
				for (Object obj : NpcCommand.getCustomNpcsInDimension(0))
					if (((EntityCustomNpc)obj).display.name.equals(name))
						++found;
				if (found == 0) {
					NpcDatabase.spawn(ws, name);
				} else if (found > 1) {
					List<EntityCustomNpc> list = new ArrayList<EntityCustomNpc>();
					for (Object obj : NpcCommand.getCustomNpcsInDimension(0))
						if (((EntityCustomNpc)obj).display.name.equals(name))
							list.add((EntityCustomNpc)obj);
					NpcDatabase.sortFurthestSpawn(list);
					for (int i = 0; i < list.size()-1; ++i)
						list.get(i).delete();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		Context context = BiGX.instance().context;
		
		//TODO: Implement proper cleanup when the game is exited
		//The event which is called by the server shutting down needs to be located and used
		//instead of this event which is called whenever the game is paused.
		
		//context.unloadWorld();
	}
	
	@SubscribeEvent
	public void entityInteractEvent(EntityInteractEvent e){
		EntityPlayer player = e.entityPlayer;
		System.out.println("Entity Interact Event");
		NpcEvents.InteractWithNPC(player, e);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e) {
		EntityPlayer player = e.entityPlayer;
		World w = e.world;

		if (!w.isRemote) {
			if (w.getBlock(e.x, e.y, e.z) == Blocks.chest)
				BiGXEventTriggers.chestInteract(e, w, levelSys);
		}
	}
	
	public boolean checkPlayerInArea(EntityPlayer player, int x1, int y1, int z1, int x2, int y2, int z2){
		if (player.posX >= x1 && player.posX <= x2)
			if (player.posY >= y1 && player.posY <= y2)
				if (player.posZ >= z1 && player.posZ <= z2)
					return true;
		return false;
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
	
	public void goBackToTheOriginalWorld(World world, MinecraftServer worldServer, QuestTeleporter teleporter, Entity entity)
	{		
		chasingQuestOnGoing = false;
		chasingQuestOnCountDown = false;
		timeFallBehind = 0;
		time = 0;
		BiGX.instance().context.setSpeed(0);
		
		if(npc != null)
			command.removeNpc(npc.display.name, WorldProviderFlats.dimID);

		if(t != null)
		{
			t.cancel();
			t = null;
		}
		if(t2 != null)
		{
			t2.cancel();
			t2 = null;
		}
		if(t3 != null)
		{
			t3.cancel();
			t3 = null;
		}

		returnLocation = Vec3.createVectorHelper(-174, 71, 338);

		levelSys.initThiefStat();
		cleanArea(world, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)entity.posZ - 128, (int)entity.posZ);
		teleporter.teleport(entity, worldServer.worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
//		entity.setPosition(returnLocation.xCoord, returnLocation.yCoord, returnLocation.zCoord);
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
					world.setBlock(dx, dy, dz, Blocks.air);
				}
			}
			world.setBlock(chasingQuestInitialPosX-16, initY, dz, Blocks.air);
			world.setBlock(chasingQuestInitialPosX+16, initY, dz, Blocks.air);
		}
	}
		
	// TODO BUG: Player transports to Quest World when items are used (leave this in for testing purposes)
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
		ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderFlats.dimID);
		context = BiGX.instance().context;
		if (event.item.getDisplayName().contains("Teleportation Potion") && checkPlayerInArea(event.entityPlayer, -177, 70, 333, -171, 74, 339)
				&& event.entity.dimension != WorldProviderFlats.dimID){
			if (ws != null && event.entity instanceof EntityPlayerMP) {		
				System.out.println("[BiGX] Current dimension ["+event.entity.dimension+"]");		
				// INIT questSettings ArrayList if there is any
				if(context.suggestedGamePropertiesReady)
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
				
				t = new Timer();
				t2 = new Timer();
				t3 = new Timer();
				
				teleporter = new QuestTeleporter(ws);
				
				returnLocation = Vec3.createVectorHelper(event.entity.posX-1, event.entity.posY-1, event.entity.posZ);
				teleporter.teleport(event.entity, ws, 1, 11, 0);

				chasingQuestInitialPosX = 1;
				chasingQuestInitialPosY = 10;
				chasingQuestInitialPosZ = 0;
				
				// Clean up placed blocks when the quest ends
				final List<Vec3> blocks = new ArrayList<Vec3>();
				
				for (int z = -16; z < (int)event.entity.posZ+64; ++z) {
					ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
					blocks.add(Vec3.createVectorHelper((int)event.entity.posX-16, chasingQuestInitialPosY, z));
					ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
					blocks.add(Vec3.createVectorHelper((int)event.entity.posX+16, chasingQuestInitialPosY, z));
				}
				for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
					ws.setBlock(x, chasingQuestInitialPosY, -16, Blocks.fence);
					blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY, -16));
				}
				
				final TimerTask t2Task = new TimerTask() {
					@Override
					public void run() {
						dist = event.entity.getDistanceToEntity(npc);
						ratio = (initialDist-dist)/initialDist;
						if (!Minecraft.getMinecraft().isGamePaused()) {
							time++;
							
							/**
							 * Generates structures on sides (fence and Fake house on sides)
							 */
							for (int z = (int)event.entity.posZ+32; z < (int)event.entity.posZ+64; ++z) {
								ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
								blocks.add(Vec3.createVectorHelper((int)event.entity.posX-16, chasingQuestInitialPosY, z));
								ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
								blocks.add(Vec3.createVectorHelper((int)event.entity.posX+16, chasingQuestInitialPosY, z));
							}
							
							Random rand = new Random();
							if (rand.nextInt(10) < 2) {
//								generateFakeHouse(ws, blocks, chasingQuestInitialPosX-25, chasingQuestInitialPosY, (int)event.entity.posZ+64);
								
							}
							rand = new Random();
							if (rand.nextInt(10) < 2) {
//								generateFakeHouse(ws, blocks, chasingQuestInitialPosX+18, chasingQuestInitialPosY, (int)event.entity.posZ+64);
								
							}
							
							
							generateFakeCave(ws, blocks, chasingQuestInitialPosX+18, chasingQuestInitialPosY, (int)event.entity.posZ+64, 1);
							generateFakeCave(ws, blocks, chasingQuestInitialPosX-25, chasingQuestInitialPosY, (int)event.entity.posZ+64, 0);
							generateFakeCave(ws, blocks, chasingQuestInitialPosX-18, chasingQuestInitialPosY, (int)event.entity.posZ+64, 2);
							generateFakeCave(ws, blocks, chasingQuestInitialPosX-11, chasingQuestInitialPosY, (int)event.entity.posZ+64, 2);
							generateFakeCave(ws, blocks, chasingQuestInitialPosX-4, chasingQuestInitialPosY, (int)event.entity.posZ+64, 2);
							generateFakeCave(ws, blocks, chasingQuestInitialPosX+3, chasingQuestInitialPosY, (int)event.entity.posZ+64, 2);
							generateFakeCave(ws, blocks, chasingQuestInitialPosX+11, chasingQuestInitialPosY, (int)event.entity.posZ+64, 2);
							
							/**
							 * END OF Generates structures on sides
							 */
							
							if(context.suggestedGamePropertiesReady)
							{
								/**
								 * Generates structures inside fence
								 */
								ArrayList<TerrainBiomeArea> areas = new ArrayList<TerrainBiomeArea>();
								int currentRelativePosition = (int)event.entity.posZ - chasingQuestInitialPosZ;
								int currentRelativeTime = (int) (currentRelativePosition/chaseRunSpeedInBlocks);
								
								if(currentRelativeTime > questSettings.size())
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
										if(false)
											areas.add(terrainBiome.getRandomCityBiome());
										else
											areas.add(terrainBiomeFire.getRandomGateBiome());
									}
								}
								else if(blockByDifficulty == Blocks.grass)
								{
									for(int idx = 0; idx<4; idx++)
									{
										if(false)
											areas.add(terrainBiome.getRandomGrassBiome());
										else
											areas.add(terrainBiomeFire.getRandomFieldBiome());
									}
								}
								else if(blockByDifficulty == Blocks.sand)
								{
									for(int idx = 0; idx<4; idx++)
									{
										if(false)
											areas.add(terrainBiome.getRandomDesertBiome());
										else
											areas.add(terrainBiomeFire.getRandomLavaFountainBiome());
									}
								}
								else {
									System.out.println("DIFFICULTY IS OUT OF HANDLE...");
								}
								
								for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
									for (int z = (int)event.entity.posZ+48; z < (int)event.entity.posZ+64; ++z) {
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
											x = chasingQuestInitialPosX-7;
											break;
										case 2:
											x = chasingQuestInitialPosX+1;
											break;
										case 3:
											x = chasingQuestInitialPosX+9;
											break;
										}
										int y = chasingQuestInitialPosY;
										int z = (int)event.entity.posZ+49 + row*9;
										
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
								/**
								 * END OF Generates structures inside fence
								 */
								
								/**
								 * Terrain Cleaning
								 */
								if(areas.size() != 0)
									areas.clear();
								
								cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)event.entity.posZ-128, (int)event.entity.posZ-112);
								/**
								 * END OF Terrain Cleaning
								 */
							}
							else{
								if (ratio > 0.4) {
									for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
										for (int z = (int)event.entity.posZ+48; z < (int)event.entity.posZ+64; ++z) {
											ws.setBlock(x, chasingQuestInitialPosY-1, z, Blocks.gravel);
											blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY-1, z));
											ws.setBlock(x, chasingQuestInitialPosY-1, z-64, Blocks.grass);
										}
									}
								}
							}
						}
						
						// SPEED CHANGE LOGIC BASED ON THE HEART RATE AND THE RPM OF THE PEDALLING
//						if (BiGX.instance().context.getSpeed() < chaseRunBaseSpeed) {
						speedchange = 0f;
						float speedchangerate = 0.025f;
						
						// Handling Player heart rate and rpm as mechanics for Chase Quest
						BiGXPatientPrescription playerperscription; // = new BiGXPatientPrescription();
//						playerperscription.setTargetMin(100);
//						playerperscription.setTargetMax(100);
						
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
						
						if (playerperscription.getTargetMin() > context.heartrate || context.rotation < 40)
							speedchange += speedchangerate;
						else if (playerperscription.getTargetMax() >= context.heartrate || context.rotation > 60 && context.rotation <= 90)
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
						
						// CHASE QUEST WINNING CONDITION == WHEN the HP of the bad guy reached 0 or below
						if (levelSys.getThiefHealthCurrent() <= 0) {
							try {
								context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTOPSUCCESS, System.currentTimeMillis());
							} catch (SocketException e) {
								e.printStackTrace();
							} catch (UnknownHostException e) {
								e.printStackTrace();
							} catch (BiGXNetException e) {
								e.printStackTrace();
							} catch (BiGXInternalGamePluginExcpetion e) {
								e.printStackTrace();
							}
							

							event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "win", 1.0f, 1.0f);
							endingZ = (int)event.entity.posZ;
							LeaderboardRow row = new LeaderboardRow();
							row.name = context.BiGXUserName;
							row.level = Integer.toString(levelSys.getThiefLevel());
							row.time_elapsed = Double.toString((System.currentTimeMillis() - elapsedTime)/1000);
							GuiLeaderBoard.writeToLeaderboard(row);

//							BiGXEventTriggers.GivePlayerGoldfromCoins(event.entityPlayer, virtualCurrency); ///Give player reward
							
							levelSys.getLevelRewards(event.entityPlayer, virtualCurrency);
							if (levelSys.getThiefLevel() == levelSys.getPlayerLevel() && virtualCurrency > 50)
								levelSys.levelUp();
							
							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldBarInfo);
							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldSpendWisely);
							
							teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
							goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, event.entity);
							
							return;
						}

						if (ratio < 0) {
							warningMsgBlinkingTime = System.currentTimeMillis();
							timeFallBehind++;
						}
						else{
							timeFallBehind = 0;
						}

						// CHASE QUEST LOSE CONDITION
						if(timeFallBehind >= 30)
						{
							try {
								context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTOPFAILURE, System.currentTimeMillis());
							} catch (SocketException e) {
								e.printStackTrace();
							} catch (UnknownHostException e) {
								e.printStackTrace();
							} catch (BiGXNetException e) {
								e.printStackTrace();
							} catch (BiGXInternalGamePluginExcpetion e) {
								e.printStackTrace();
							}
							
//							BiGXEventTriggers.GivePlayerGoldfromCoins(event.entityPlayer, virtualCurrency); ///Give player reward
							teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
							goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, event.entity);
						}
					}
				};

				final TimerTask tTask = new TimerTask() {
					@Override
					public void run() {
						if (countdown > 0) {
							chasingQuestOnGoing = true;
							chasingQuestOnCountDown = true;
							System.out.println(countdown-- + "...");
							
							if (countdown == 7) {
								for (Object o : event.entityPlayer.worldObj.loadedEntityList) {
									if (((Entity)o) instanceof EntityCustomNpc) {
//										System.out.println(((EntityCustomNpc)o).display.name);
										((EntityCustomNpc)o).delete();
									}
								}
								for (Object o : NpcCommand.getCustomNpcsInDimension(WorldProviderFlats.dimID)) {
									System.out.println(((EntityCustomNpc)o).display.name);
//									((EntityCustomNpc)o).delete();
								}
							}
							if (countdown == 0) {
								elapsedTime = System.currentTimeMillis();
								dist = 0;
								startingZ = (int)event.entity.posZ;
								endingZ = (int)event.entity.posZ;
							}
							if (countdown == 5) {
								npc = NpcCommand.spawnNpc(0f, 11, 20, ws, "Thief");
								npc.ai.stopAndInteract = false;
								command = new NpcCommand(npc);
								command.setSpeed(10);
								command.enableMoving(false);
								command.runInDirection(ForgeDirection.SOUTH);
								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseShowup);
								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseHintWeapon);
							}
							else if (countdown == 1)
							{
								try {
									context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTART, System.currentTimeMillis());
								} catch (SocketException e) {
									e.printStackTrace();
								} catch (UnknownHostException e) {
									e.printStackTrace();
								} catch (BiGXNetException e) {
									e.printStackTrace();
								} catch (BiGXInternalGamePluginExcpetion e) {
									e.printStackTrace();
								}
							}
							else if(countdown == 9)
							{
								event.entity.rotationPitch = 0f;
								event.entity.rotationYaw = 0f;
							}
							else if(countdown == 8)
							{
								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseBeginning);
							}
							
							
						} else {
							levelSys.initThiefStat();
							chasingQuestOnCountDown = false;
							System.out.println("GO!");
							command.enableMoving(true);
							countdown = 10;
							t.cancel();
							initialDist = 20; // HARD CODED
							t2.scheduleAtFixedRate(t2Task, 0, 1000);
						}
					}
				};
				
				for (int z = (int)event.entity.posZ; z < (int)event.entity.posZ+64; ++z) {
					ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY-2, z, Blocks.fence);
					blocks.add(Vec3.createVectorHelper((int)event.entity.posX-16, chasingQuestInitialPosY-2, z));
					ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY-2, z, Blocks.fence);
					blocks.add(Vec3.createVectorHelper((int)event.entity.posX+16, chasingQuestInitialPosY-2, z));
				}
				
				t.scheduleAtFixedRate(tTask, 0, 1000);
			}
		}
		else if (event.item.getDisplayName().contains("Teleportation Potion")
				&& event.entity.dimension == WorldProviderFlats.dimID){
			// CHASE QUEST LOSE CONDITION
			if (ws != null && event.entity instanceof EntityPlayerMP) {
				BiGXEventTriggers.GivePlayerGoldfromCoins(event.entityPlayer, virtualCurrency); ///Give player reward
				virtualCurrency = 0;
				teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
				levelSys.initThiefStat();
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)event.entity.posZ - 128, (int)event.entity.posZ);
//				teleporter.teleport(event.entity, MinecraftServer.getServer().worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
				goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, event.entity);
			}
		}
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
	
	private void generateFakeCave(World w, List<Vec3> blocks, int origX, int origY, int origZ, int side) {
		// Side==0: Left,   Side==1: Right,    Side==2: Ceiling
		if(side==0) {
			for (int x = origX; x < origX + 7; ++x) {
				if(x==origX) {
					for (int y = origY; y < origY + 4; ++y) {
						for (int z = origZ; z < origZ + 11; ++z) {
							if( ((z%2)==1) && (y==(origY+2)) )
								w.setBlock(x, y, z, Blocks.glowstone);
							else
								w.setBlock(x, y, z, Blocks.dirt);
						}
					}
				}
				else if(x==(origX+1)) {
					int y=origY + 4;
					
					for (int z = origZ; z < origZ + 11; ++z) {
						w.setBlock(x, y, z, Blocks.dirt);
					}
				}
				else if(x==(origX+2)) {
					int y=origY + 5;
					
					for (int z = origZ; z < origZ + 11; ++z) {
						if((z%2)==1)
							w.setBlock(x, y, z, Blocks.glowstone);
						else
							w.setBlock(x, y, z, Blocks.dirt);
					}
				}
				else if(x>=(origX+3)) {
					int y=origY + 6;
					
					for (int z = origZ; z < origZ + 11; ++z) {
						w.setBlock(x, y, z, Blocks.dirt);
					}
				}
			}
		}
		else if(side==1) {
			for (int x = origX+6; x >= origX; --x) {
				if(x==(origX+6)) {
					for (int y = origY; y < origY + 4; ++y) {
						for (int z = origZ; z < origZ + 11; ++z) {
							if( ((z%2)==1) && (y==(origY+2)) )
								w.setBlock(x, y, z, Blocks.glowstone);
							else
								w.setBlock(x, y, z, Blocks.dirt);
						}
					}
				}
				else if(x==(origX+5)) {
					int y=origY + 4;
					
					for (int z = origZ; z < origZ + 11; ++z) {
						w.setBlock(x, y, z, Blocks.dirt);
					}
				}
				else if(x==(origX+4)) {
					int y=origY + 5;
					
					for (int z = origZ; z < origZ + 11; ++z) {
						if((z%2)==1)
							w.setBlock(x, y, z, Blocks.glowstone);
						else
							w.setBlock(x, y, z, Blocks.dirt);
					}
				}
				else if(x<=(origX+3)) {
					int y=origY + 6;
					
					for (int z = origZ; z < origZ + 11; ++z) {
						w.setBlock(x, y, z, Blocks.dirt);
					}
				}
			}
		}
		else if(side==2) {
			for (int x = origX; x < origX + 7; ++x) {
				int y=origY + 6;
				
				for (int z = origZ; z < origZ + 11; ++z) {
					w.setBlock(x, y, z, Blocks.dirt);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDecoratorCreate(DecorateBiomeEvent.Decorate event) {
		if (event.world.provider.getDimensionName() == WorldProviderFlats.dimName) {
			if (event.type == DecorateBiomeEvent.Decorate.EventType.PUMPKIN) {
				// Stops the specified EventType from decorating during chunk generation
				event.setResult(Result.DENY);
			}
		}
	}
	
	 //Called when the server ticks. Usually 20 ticks a second. 
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) throws Exception {
		if (MinecraftServer.getServer()!=null&&event.phase==TickEvent.Phase.END) {
			boolean isServer = MinecraftServer.getServer().isDedicatedServer();
			server_tick++;
			//20 ticks = 1 second
			if (server_tick==20) {
				server_tick = 0;
			}
			
			//Making sure it remains daytime all the time
			World current_world = MinecraftServer.getServer().getEntityWorld();
			if (current_world.provider.getWorldTime() >= 12000)
				current_world.setWorldTime(0);

			
			// Test Purpose Code
			if(this.serverQuestTest)
			{
				if(this.serverQuestTestTickCount > 0)
				{
					if(server_tick == 0)
					{
						this.serverQuestTestTickCount--;
					}
				}
				else{
					this.serverQuestTest = false;
					
					//makeQuestOnServer();
				}
			}
		}
	}
		
	public static float getPlayerPitch() {
		return playerQuestPitch;
	}
	public static float getPlayerYaw() {
		return playerQuestYaw;
	}
	
}