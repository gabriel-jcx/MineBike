package org.ngs.bigx.minecraft.quests;

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
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiLeaderBoard;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.levelUp.LevelSystem;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiome;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNpcCrystal;

public class QuestEventChasing implements IQuestEvent {

	static float playerQuestPitch, playerQuestYaw;
	
	private static long questTimeStamp = 0;

	private static boolean completed = false;
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
	CharacterProperty characterProperty = BiGX.instance().characterProperty;
	EntityCustomNpc npc;
	NpcCommand command;
	
	public static final float chaseRunBaseSpeed = 2.1f; // 157 blocks per 15 seconds!!
	public static float speedchange = 0f;
	private final float chaseRunSpeedInBlocks = 157f/15f;
	public static boolean chasingQuestOnGoing = false;
	public static boolean chasingQuestOnCountDown = false;
	public static int virtualCurrency = 0;
	public static long warningMsgBlinkingTime = System.currentTimeMillis();

	private static TerrainBiome terrainBiome = new TerrainBiome();
	
	private static ArrayList<Integer> questSettings = null;

	private static int chasingQuestInitialPosX = 0;
	private static int chasingQuestInitialPosY = 0;
	private static int chasingQuestInitialPosZ = 0;
	
	private static Timer t = null;
	private static Timer t2 = null;
	private static QuestTeleporter teleporter = null;

	private static int thiefHealthMax = 50;
	private static int thiefHealthCurrent = thiefHealthMax;
	private static int thiefLevel = 1;
	private static int thiefMaxLevel = 1;
	private static boolean thiefLevelUpFlag = false;
	
	private WorldServer ws;
	
	public static EntityPlayer player;
	
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
	
	public static int getThiefHealthMax() {
		return thiefHealthMax;
	}

	public static int getThiefHealthCurrent() {
		return thiefHealthCurrent;
	}

	public static int getThiefLevel() {
		return thiefLevel;
	}
	
	public static boolean checkPlayerInArea(EntityPlayer player, int x1, int y1, int z1, int x2, int y2, int z2) {
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

		returnLocation = Vec3.createVectorHelper(96, 72, -8);

		initThiefStat();
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
	
	public static void initThiefStat()
	{
		thiefHealthMax = 50;
		thiefHealthCurrent = thiefHealthMax;
		thiefLevel = 1;
	}
	
	public static void thiefLevelUp()
	{
		thiefLevel ++;
		thiefMaxLevel ++;
		
		thiefHealthMax = 50 + (int) Math.pow(3, thiefLevel);
		thiefHealthCurrent = thiefHealthMax;
	}
	
	public static void setThiefLevel(int level)
	{
		thiefLevel = level;
		
		thiefHealthMax = 50 + (int) Math.pow(3, thiefLevel);
		thiefHealthCurrent = thiefHealthMax;
	}
	
	public static void deductThiefHealth(Item itemOnHands)
	{
		int deduction = 1;
		if (itemOnHands != null) {
			if(itemOnHands.getUnlocalizedName().equals("item.hoeStone"))
			{
				deduction = 3;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.hoeIron"))
			{
				deduction = 9;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.hoeGold"))
			{
				deduction = 27;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.hoeDiamond"))
			{
				deduction = 81;
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
	
	@Override
	public boolean IsComplete() {
		return completed;
	}

	@Override
	public void Run(final LevelSystem levelSys) {
		System.out.println("Running...");
		ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderFlats.dimID);
		
		context = BiGX.instance().context;

		System.out.println(player.dimension);
		System.out.println(WorldProviderFlats.dimID);
		if (player.getHeldItem().getDisplayName().contains("Teleportation Potion") && checkPlayerInArea(player, 94, 53, -54, 99, 58, -48)
				&& player.dimension != WorldProviderFlats.dimID){
			if (ws != null && player instanceof EntityPlayerMP) {
				// SET CURRENT ACTIVE QUEST DEMO
				if(ClientEventHandler.getHandler().questDemo == null)
					ClientEventHandler.getHandler().questDemo = new QuestDemo(player);
				else {
					if(ClientEventHandler.getHandler().questDemo.getQuest().events.contains(this)) {
						if(System.currentTimeMillis() - questTimeStamp < 1000)
							return;
					}
				}
				
				completed = false;
				questTimeStamp = System.currentTimeMillis();
				
				Quest chaseQuest = new Quest("Chagse", "Let's get started!");
				chaseQuest.events.add(this);
				ClientEventHandler.getHandler().questDemo.setActiveQuest(chaseQuest);
						
				setThiefLevel(Integer.parseInt(player.getHeldItem().getDisplayName().split(" ")[2]));
				System.out.println("[BiGX] thiefLevel: " + thiefLevel + " vs playerlevel: " + levelSys.getPlayerLevel());
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
				
				teleporter = new QuestTeleporter(ws);
				
				returnLocation = Vec3.createVectorHelper(player.posX-1, player.posY-1, player.posZ);
				teleporter.teleport(player, ws, 1, 11, 0);

				chasingQuestInitialPosX = 1;
				chasingQuestInitialPosY = 10;
				chasingQuestInitialPosZ = 0;
				
				// Clean up placed blocks when the quest ends
				final List<Vec3> blocks = new ArrayList<Vec3>();
				
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
				
				final TimerTask t2Task = new TimerTask() {
					@Override
					public void run() {
						dist = player.getDistanceToEntity(npc);
						ratio = (initialDist-dist)/initialDist;
						if (!Minecraft.getMinecraft().isGamePaused()) {
							time++;
							
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
							
							if(context.suggestedGamePropertiesReady)
							{
								/**
								 * Generates structures inside fence
								 */
								ArrayList<TerrainBiomeArea> areas = new ArrayList<TerrainBiomeArea>();
								int currentRelativePosition = (int)player.posZ - chasingQuestInitialPosZ;
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
										areas.add(terrainBiome.getRandomCityBiome());
									}
								}
								else if(blockByDifficulty == Blocks.grass)
								{
									for(int idx = 0; idx<4; idx++)
									{
										areas.add(terrainBiome.getRandomGrassBiome());
									}
								}
								else if(blockByDifficulty == Blocks.sand)
								{
									for(int idx = 0; idx<4; idx++)
									{
										areas.add(terrainBiome.getRandomDesertBiome());
									}
								}
								else {
									System.out.println("DIFFICULTY IS OUT OF HANDLE...");
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
								/**
								 * END OF Generates structures inside fence
								 */
								
								/**
								 * Terrain Cleaning
								 */
								if(areas.size() != 0)
									areas.clear();
								
								cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ-128, (int)player.posZ-112);
								/**
								 * END OF Terrain Cleaning
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
						
						// SPEED CHANGE LOGIC BASED ON THE HEART RATE AND THE RPM OF THE PEDALLING
//						if (BiGX.instance().context.getSpeed() < chaseRunBaseSpeed) {
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
						if (thiefHealthCurrent <= 0) {
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
							

							player.worldObj.playSoundAtEntity(player, "win", 1.0f, 1.0f);
							endingZ = (int)player.posZ;
							LeaderboardRow row = new LeaderboardRow();
							row.name = context.BiGXUserName;
							row.level = Integer.toString(thiefLevel);
							row.time_elapsed = Double.toString((System.currentTimeMillis() - elapsedTime)/1000);
							GuiLeaderBoard.writeToLeaderboard(row);

							BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldBarInfo);
							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.goldSpendWisely);
							
							System.out.println("[BiGX] increased exp: " + levelSys.incExp(50));
							if(levelSys.getPlayerLevel() == thiefLevel && levelSys.incExp(50/levelSys.getPlayerLevel())){ //Can be changed later so it's more variable
								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.levelUpMsg);
								levelSys.giveLevelUpRewards(player);
							}
							
							
							teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
							completed = true;
							goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, player);
							
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
							
							BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
							if (thiefLevel == thiefMaxLevel && virtualCurrency > 50)
								thiefLevelUp();
							teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
							completed = true;
							goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, player);
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
								for (Object o : player.worldObj.loadedEntityList) {
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
								startingZ = (int)player.posZ;
								endingZ = (int)player.posZ;
							}
							if (countdown == 5) {
//								npc = NpcCommand.spawnNpc(0, 11, 20, ws, "Thief");
//								npc.ai.stopAndInteract = false;
//								command = new NpcCommand(npc);
//								command.setSpeed(10);
//								command.enableMoving(false);
//								command.runInDirection(ForgeDirection.SOUTH);
								NpcCommand.triggerSpawnTheifOnChaseQuest();
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
								player.rotationPitch = 0f;
								player.rotationYaw = 0f;
							}
							else if(countdown == 8)
							{
								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseBeginning);
							}
							
							
						} else {
							initThiefStat();
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
				
				for (int z = (int)player.posZ; z < (int)player.posZ+64; ++z) {
					ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY-2, z, Blocks.fence);
					blocks.add(Vec3.createVectorHelper((int)player.posX-16, chasingQuestInitialPosY-2, z));
					ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY-2, z, Blocks.fence);
					blocks.add(Vec3.createVectorHelper((int)player.posX+16, chasingQuestInitialPosY-2, z));
				}
				
				t.scheduleAtFixedRate(tTask, 0, 1000);
			}
		}
		else if (player.getHeldItem().getDisplayName().contains("Teleportation Potion")
				&& player.dimension == WorldProviderFlats.dimID){
			// CHASE QUEST LOSE CONDITION
			if (ws != null && player instanceof EntityPlayerMP) {
				BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
				virtualCurrency = 0;
				teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
				initThiefStat();
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ - 128, (int)player.posZ);
//				teleporter.teleport(player, MinecraftServer.getServer().worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
				completed = true;
				goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, player);
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
	
	public static float getPlayerPitch() {
		return playerQuestPitch;
	}
	public static float getPlayerYaw() {
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
	}
	
	public void goBackToOriginalWorld(){
		if (ws != null && player instanceof EntityPlayerMP) {
			BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
			virtualCurrency = 0;
			teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(0));
			initThiefStat();
			cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ - 128, (int)player.posZ);
//			teleporter.teleport(player, MinecraftServer.getServer().worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
			completed = true;
			goBackToTheOriginalWorld(ws, MinecraftServer.getServer(), teleporter, player);
		}
	}
	
}
