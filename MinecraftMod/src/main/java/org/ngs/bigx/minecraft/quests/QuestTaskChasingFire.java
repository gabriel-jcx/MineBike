package org.ngs.bigx.minecraft.quests;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.fire.TerrainBiomeFire;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDark;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.utility.NpcCommand;

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
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskChasingFire implements IQuestTask {
	public static final String id = "QUEST_CHASE";
	
	private boolean isRequired;
	
	static float playerQuestPitch, playerQuestYaw;
	
	private static long questTimeStamp = 0;

	private static boolean completed = false;

	private static int countdown = 10;
	private static int time = 0;
	private static double elapsedTime = 0;
	private static int timeFallBehind = 0;
	NpcCommand activecommand;
	public static float initialDist, dist = 0;
	public static int startingZ, endingZ;
	boolean doMakeBlocks;
	float ratio;
	Vec3 returnLocation;
	
	private static BigxClientContext context;
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

	private static TerrainBiomeFire terrainBiomeFire = new TerrainBiomeFire();
	
	private static ArrayList<Integer> questSettings = null;

	private static int chasingQuestInitialPosX = 0;
	private static int chasingQuestInitialPosY = 0;
	private static int chasingQuestInitialPosZ = 0;
	
	private static Timer t = null;
	private static Timer t2 = null;

	private static int thiefHealthMax = 50;
	private static int thiefHealthCurrent = thiefHealthMax;
	private static int thiefLevel = 1;
	private static int thiefMaxLevel = 1;
	private static boolean thiefLevelUpFlag = false;
	
	private WorldServer ws;
	
	public static EntityPlayer player;
	
	public QuestTaskChasingFire(EntityPlayer p, WorldServer worldServer, int level, int maxLevel, boolean required) {
		player = p;
		ws = worldServer;
		thiefLevel = level;
		thiefMaxLevel = maxLevel;
		isRequired = required;
	}
	
	public QuestTaskChasingFire(EntityPlayer p, WorldServer worldServer, int level, int maxLevel) {
		player = p;
		ws = worldServer;
		thiefLevel = level;
		thiefMaxLevel = maxLevel;
		isRequired = false;
	}
	
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
	
	public void goBackToTheOriginalWorld(World world, Entity entity)
	{		
		chasingQuestOnGoing = false;
		chasingQuestOnCountDown = false;
		timeFallBehind = 0;
		time = 0;
		BiGX.instance().context.setSpeed(0);
		
		if(npc != null)
			command.removeNpc(npc.display.name, WorldProviderFlats.fireQuestDimID);

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
		QuestTeleporter.teleport(entity, 0, (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
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
		ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderDark.dimID);
		
		context = BiGX.instance().context;
		if (player.getHeldItem().getDisplayName().contains("Teleportation Potion") && player.dimension != WorldProviderDark.dimID){
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
				
				Quest chaseQuest = new Quest(this.id, "Chagse", "Let's get started!");
				chaseQuest.events.add(this);
				ClientEventHandler.getHandler().questDemo.setActiveQuest(chaseQuest);
				
				setThiefLevel(Integer.parseInt(player.getHeldItem().getDisplayName().split(" ")[2]));
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
				
				returnLocation = Vec3.createVectorHelper(player.posX-1, player.posY-1, player.posZ);
				QuestTeleporter.teleport(player, WorldProviderDark.dimID, 1, 11, 0);

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
										areas.add(terrainBiomeFire.getRandomGateBiome());
									}
								}
								else if(blockByDifficulty == Blocks.grass)
								{
									for(int idx = 0; idx<4; idx++)
									{
										areas.add(terrainBiomeFire.getRandomFieldBiome());
									}
								}
								else if(blockByDifficulty == Blocks.sand)
								{
									for(int idx = 0; idx<4; idx++)
									{
										areas.add(terrainBiomeFire.getRandomLavaFountainBiome());
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
							
							completed = true;
							goBackToTheOriginalWorld(ws, player);
							
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
							completed = true;
							goBackToTheOriginalWorld(ws, player);
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
								for (Object o : NpcCommand.getCustomNpcsInDimension(WorldProviderDark.dimID)) {
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
								NpcCommand.triggerSpawnTheifOnFireChaseQuest();								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseShowup);
								GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questChaseHintWeapon);
							}
							else if (countdown == 1)
							{
//								try {
//									context.bigxclient.sendGameEvent(GameTagType.GAMETAG_NUMBER_QUESTSTART, System.currentTimeMillis());
//								} catch (SocketException e) {
//									e.printStackTrace();
//								} catch (UnknownHostException e) {
//									e.printStackTrace();
//								} catch (BiGXNetException e) {
//									e.printStackTrace();
//								} catch (BiGXInternalGamePluginExcpetion e) {
//									e.printStackTrace();
//								}
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
				&& player.dimension == WorldProviderDark.dimID){
			// CHASE QUEST LOSE CONDITION
			if (ws != null && player instanceof EntityPlayerMP) {
				BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency); ///Give player reward
				virtualCurrency = 0;
				initThiefStat();
				cleanArea(ws, chasingQuestInitialPosX, chasingQuestInitialPosY, (int)player.posZ - 128, (int)player.posZ);
				completed = true;
				goBackToTheOriginalWorld(ws, player);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean IsMainTask() {
		return isRequired;
	}

	@Override
	public String getTaskDescription() {
		return null;
	}

	@Override
	public String getTaskName() {
		return null;
	}
	
}
