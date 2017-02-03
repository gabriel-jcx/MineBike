package org.ngs.bigx.minecraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.dictionary.objects.game.properties.Stage;
import org.ngs.bigx.dictionary.objects.game.properties.StageSettings;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Context.Resistance;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnClient;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestChasing;
import org.ngs.bigx.minecraft.quests.QuestEvent;
import org.ngs.bigx.minecraft.quests.QuestEvent.eventType;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.minecraft.quests.QuestPlayer;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.utility.NpcCommand;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.entity.EntityCustomNpc;
import scala.collection.concurrent.Debug;


public class CommonEventHandler {

	static float playerQuestPitch, playerQuestYaw;
	
	int server_tick = 0;
	boolean serverQuestTest = true;
	int serverQuestTestTickCount = 10;
	private static int countdown = 10;
	private static int time = 30;
	private static int timeFallBehind = 0;
	EntityCustomNpc activenpc;
	NpcCommand activecommand;
	float initialDist, dist;
	boolean doMakeBlocks;
	float ratio;
	Vec3 returnLocation;
	
	private static Context context;
	EntityCustomNpc npc;
	NpcCommand command;
	
	private final float chaseRunBaseSpeed = 2.1f; // 157 blocks per 15 seconds!!
	private final float chaseRunSpeedInBlocks = 157f/15f;
	public static boolean chasingQuestOnGoing = false;
	public static boolean chasingQuestOnCountDown = false;
	
	private static ArrayList<Integer> questSettings = null;

	private static int chasingQuestInitialPosX = 0;
	private static int chasingQuestInitialPosY = 0;
	private static int chasingQuestInitialPosZ = 0;
	
	public static int getTime()
	{
		return time;
	}
	
	public static int getCountdown()
	{
		return countdown;
	}
	
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		BikeWorldData data = BikeWorldData.get(event.world);
		System.out.println(event.world.provider.dimensionId);
		if (event.world.provider.dimensionId == 0){
			System.out.println("DIMENSION ID == 0");
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
			//WorldServer ws = MinecraftServer.getServer().worldServerForDimension(event.world.provider.dimensionId);
			EntityCustomNpc teleporternpc = NpcCommand.spawnNpc(-60f, 73f, 70f, ws, "Quest Giver");
			NpcCommand teleportercommand = new NpcCommand(teleporternpc);
			teleportercommand.enableMoving(false);
			teleportercommand.makeTransporter(true);
			
			activenpc = teleporternpc;
			activecommand = teleportercommand;
			
			boolean foundFather = false;
			for (Object o : NpcCommand.getCustomNpcsInDimension(0)) {
				if (((EntityCustomNpc)o).display.name == "Father")
					foundFather = true;
			}
			
			if (!foundFather) {
				//EntityCustomNpc npc = NpcCommand.spawnNpc(-56, 73, 7, ws, "Father");
				//npc.dialogs.
			}
				
			//allNPCS.SetQuestNPCS();
		}
//		if (event.world.provider.dimensionId == 100){
//			System.out.println("DIMENSION ID == 100");
//			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(100);
//			//WorldServer ws = MinecraftServer.getServer().worldServerForDimension(event.world.provider.dimensionId);
//			EntityCustomNpc thiefnpc = NpcCommand.spawnNpc(0f, 10f, 10f, event.world, "Thief");
//			NpcCommand thiefcommand = new NpcCommand(thiefnpc);
//			thiefcommand.enableMoving(true);
//			thiefcommand.setSpeed(10);
//			thiefcommand.runInDirection(ForgeDirection.EAST);
//			//allNPCS.SetChaseNPC();
//			Timer questTimer = new Timer();
//		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		Context context = BiGX.instance().context;
		
		//TODO: Implement proper cleanup when the game is exited
		//The event which is called by the server shutting down needs to be located and used
		//instead of this event which is called whenever the game is paused.
		
		//context.unloadWorld();
	}
	
	public boolean checkPlayerInArea(final PlayerUseItemEvent.Start event, int x1, int y1, int z1, int x2, int y2, int z2){
		if (event.entityPlayer.posX >= x1 && event.entityPlayer.posX <= x2)
			if (event.entityPlayer.posY >= y1 && event.entityPlayer.posY <= y2)
				if (event.entityPlayer.posZ >= z1 && event.entityPlayer.posZ <= z2)
					return true;
		return false;
	}
	
	public Block getBlockByDifficulty(int difficultyLevel)
	{
		int category = difficultyLevel/205;
		
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
			return Blocks.water;
		default:
			return null;
		}
	}
	
	// TODO BUG: Player transports to Quest World when items are used (leave this in for testing purposes)
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
		final WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderFlats.dimID);
		context = BiGX.instance().context;
		if (event.item.getDisplayName().contains("Diamond Sword") && checkPlayerInArea(event, -177, 70, 333, -171, 74, 339)
				|| event.entity.dimension == WorldProviderFlats.dimID){
			if (ws != null && event.entity instanceof EntityPlayerMP) {
				try {
					QuestChasing questChasing = new QuestChasing(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// INIT questSettings ArrayList if there is any
				if(context.suggestedGamePropertiesReady)
				{
					time = 300; 
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
					time = 30;
				}
				
				final QuestTeleporter teleporter = new QuestTeleporter(ws);
				returnLocation = Vec3.createVectorHelper(event.entity.posX-1, event.entity.posY-1, event.entity.posZ);
				teleporter.teleport(event.entity, ws, 1, 11, 0);

				chasingQuestInitialPosX = (int)event.entity.posX;
				chasingQuestInitialPosY = 10;
				chasingQuestInitialPosZ = (int)event.entity.posZ;

				final Timer t = new Timer();
				final Timer t2 = new Timer();
				final Timer t3 = new Timer();
				
				// Clean up placed blocks when the quest ends
				final List<Vec3> blocks = new ArrayList<Vec3>();
				final TimerTask t3Task = new TimerTask() {
					@Override
					public void run() {
						// Timer for the case where the main char is close enough to catch the bad guy
						if (!Minecraft.getMinecraft().isGamePaused()) {
							for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
								for (int z = (int)event.entity.posZ+48; z < (int)event.entity.posZ+64; ++z) {
									ws.setBlock(x, chasingQuestInitialPosY-1, z, Blocks.gravel);
									blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY-1, z));
									//ws.setBlock(x, (int)event.entity.posY-1, z-64, Blocks.grass);
								}
							}
						}
					}
				};
				final TimerTask t2Task = new TimerTask() {
					@Override
					public void run() {
						dist = event.entity.getDistanceToEntity(npc);
						ratio = (initialDist-dist)/initialDist;
						if (!Minecraft.getMinecraft().isGamePaused()) {
							time--;
							
							for (int z = (int)event.entity.posZ+32; z < (int)event.entity.posZ+64; ++z) {
								ws.setBlock(chasingQuestInitialPosX-16, chasingQuestInitialPosY, z, Blocks.fence);
								blocks.add(Vec3.createVectorHelper((int)event.entity.posX-16, chasingQuestInitialPosY, z));
								ws.setBlock(chasingQuestInitialPosX+16, chasingQuestInitialPosY, z, Blocks.fence);
								blocks.add(Vec3.createVectorHelper((int)event.entity.posX+16, chasingQuestInitialPosY, z));
							}
							
							if(context.suggestedGamePropertiesReady)
							{
								int currentRelativePosition = (int)event.entity.posZ - chasingQuestInitialPosZ;
								int currentRelativeTime = (int) (currentRelativePosition/chaseRunSpeedInBlocks);
								
								if(currentRelativeTime > questSettings.size())
								{
									currentRelativeTime = questSettings.size() -1;
								}
								
								int currentQuestDifficulty = questSettings.get(currentRelativeTime);
								Block blockByDifficulty = getBlockByDifficulty(currentQuestDifficulty);
								
								for (int x = chasingQuestInitialPosX-16; x < chasingQuestInitialPosX+16; ++x) {
									for (int z = (int)event.entity.posZ+48; z < (int)event.entity.posZ+64; ++z) {
										ws.setBlock(x, chasingQuestInitialPosY-1, z, blockByDifficulty);
										blocks.add(Vec3.createVectorHelper(x, chasingQuestInitialPosY-1, z));
									}
								}
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
						if (BiGX.instance().context.getSpeed() < chaseRunBaseSpeed) {
							float speedchange = 0f;
							// Handling Player heart rate and rpm as mechanics for Chase Quest
							BiGXPatientPrescription playerperscription = context.suggestedGameProperties.getPlayerProperties().getPatientPrescriptions().get(0);
							if (playerperscription.getTargetMin() > context.heartrate || context.rotation < 40)
								speedchange += .2f;
							else if (playerperscription.getTargetMax() >= context.heartrate || context.rotation > 60 && context.rotation <= 90)
								speedchange += .2f;
							else if (playerperscription.getTargetMax() < context.heartrate)
								speedchange += -.1f;
							
//							if(BiGX.)

							BiGX.instance().context.setSpeed(chaseRunBaseSpeed + speedchange);
						}
						
						// Quest Success!
						if (ratio > 0.8f) {
							chasingQuestOnGoing = false;
							chasingQuestOnCountDown = false;
							System.out.println("You got me!");
							time = 30;

							BiGX.instance().context.setSpeed(0);
							for (Vec3 v : blocks) {
								// Cleanup - change all blocks back to grass/air
//								if (ws.getBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord) == Blocks.fence) {
//									ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.air);
//								} else {
//									ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.grass);
//								}
							}
							command.removeNpc(npc.display.name, WorldProviderFlats.dimID);
							t2.cancel();
							teleporter.teleport(event.entity, MinecraftServer.getServer().worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
						}

						// Quest Failure: Fall Behind!!!
						if (ratio < 0) {
							timeFallBehind++;
							System.out.println("PUSH! You are too far away!");
						}
						
						if(timeFallBehind >= 10)
						{
							chasingQuestOnGoing = false;
							chasingQuestOnCountDown = false;
							timeFallBehind = 0;
							t2.cancel();
							System.out.println("Too far away! -- FAIL");
							time = 30;
							BiGX.instance().context.setSpeed(0);
//							for (Vec3 v : blocks) {
//								// Cleanup - change all blocks back to grass/air
//								if (ws.getBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord) == Blocks.fence) {
//									ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.air);
//								} else {
//									ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.grass);
//								}
//							}
							command.removeNpc(npc.display.name, WorldProviderFlats.dimID);
							t2.cancel();
							teleporter.teleport(event.entity, MinecraftServer.getServer().worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
						}

						// Quest Failure: Times up!
						if (time <= 0) {
							chasingQuestOnGoing = false;
							chasingQuestOnCountDown = false;
							t2.cancel();
							System.out.println("TIME UP -- FAIL");
							time = 30;
							BiGX.instance().context.setSpeed(0);
//							for (Vec3 v : blocks) {
//								// Cleanup - change all blocks back to grass/air
//								if (ws.getBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord) == Blocks.fence) {
//									ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.air);
//								} else {
//									ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.grass);
//								}
//							}
							command.removeNpc(npc.display.name, WorldProviderFlats.dimID);
							t2.cancel();
							teleporter.teleport(event.entity, MinecraftServer.getServer().worldServerForDimension(0), (int)returnLocation.xCoord, (int)returnLocation.yCoord, (int)returnLocation.zCoord);
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
							if (countdown == 5) {
								npc = NpcCommand.spawnNpc(0.5f, 11, 20, ws, "Thief");
								command = new NpcCommand(npc);
								command.setSpeed(10);
								command.enableMoving(false);
								command.runInDirection(ForgeDirection.SOUTH);
							}
						} else {
							chasingQuestOnCountDown = false;
							System.out.println("GO!");
							command.enableMoving(true);
							countdown = 10;
							t.cancel();
							initialDist = event.entity.getDistanceToEntity(npc);
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
	
	public static void makeQuestOnServer()
	{
		//Quest q = BiGX.instance().context.questManager.makeQuest("runFromMummy");
		Quest q = BiGX.instance().context.questManager.makeQuest("timedTrack");
		makeQuestOnServer();
		
		for (WorldServer world:MinecraftServer.getServer().worldServers) {
			List<EntityPlayerMP> playerList = world.playerEntities;
			
			for (EntityPlayerMP player:playerList) {
				q.addPlayer(player.getDisplayName(),BiGX.instance().context);
				World worldd = player.getEntityWorld();
				q.setOriginalWorld(worldd);
			}
			
			q.addQuestInitiator(1524, 65, 411);
		}
		
		System.out.println("[BIGX] CREATE QUEST QUEUEING");
		BiGX.instance().context.questEventQueue.add(new QuestEvent(q, eventType.CreateQuest));
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
			
			if(BiGX.instance().context.questEventQueue.size() == 0)
			{
				return;
			}
			
			QuestEvent questevent = BiGX.instance().context.questEventQueue.remove();
			Quest quest = questevent.quest;
			eventType type = questevent.type; 
			Collection<QuestPlayer> players = quest.players.values();
			
			switch(type)
			{
			case CreateQuest:
				for (QuestPlayer player : players)
				{
					HandleQuestMessageOnClient packet = new HandleQuestMessageOnClient(quest, Trigger.MakeQuest);
					BiGX.network.sendTo(packet, (EntityPlayerMP) player.getEntity());
				}
				break;
			case NotifyQuestPlayers:
				for (QuestPlayer player : players)
				{
					HandleQuestMessageOnClient packet = new HandleQuestMessageOnClient(quest, Trigger.NotifyQuest);
//					quest.removeQuestInitiator(1524, 65, 411);
					BiGX.network.sendTo(packet, (EntityPlayerMP) player.getEntity());
				}
				break;
			default:
				WorldServer[] worldServers = MinecraftServer.getServer().worldServers;
				
				for (WorldServer world:worldServers) {
					List<EntityPlayerMP> playerList = world.playerEntities;
					for (EntityPlayerMP player:playerList) {
					}
				}
				break;
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