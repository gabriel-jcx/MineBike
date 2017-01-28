package org.ngs.bigx.minecraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnClient;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestChasing;
import org.ngs.bigx.minecraft.quests.QuestEvent;
import org.ngs.bigx.minecraft.quests.QuestEvent.eventType;
import org.ngs.bigx.minecraft.quests.QuestPlayer;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.utility.NpcCommand;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
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
	int countdown = 10;
	int time = 30;
	EntityCustomNpc activenpc;
	NpcCommand activecommand;
	float initialDist, dist;
	boolean doMakeBlocks;
	
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
	
	// TODO BUG: Player transports to Quest World when items are used (leave this in for testing purposes)
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
		final WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderFlats.dimID);
		if (ws != null && event.entity instanceof EntityPlayerMP) {
			try {
				QuestChasing questChasing = new QuestChasing(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			QuestTeleporter teleporter = new QuestTeleporter(ws);
			teleporter.teleport(event.entity, ws);
			
			final EntityCustomNpc npc = NpcCommand.spawnNpc(0, 10, 20, ws, "Thief");
			//teleporter.teleport(npc, ws);
			final NpcCommand command = new NpcCommand(npc);
			command.setSpeed(10);
			command.enableMoving(false);
			command.runInDirection(ForgeDirection.SOUTH);
			final Timer t = new Timer();
			final Timer t2 = new Timer();
			final Timer t3 = new Timer();
			final List<Vec3> blocks = new ArrayList<Vec3>();
			final TimerTask t3Task = new TimerTask() {
				@Override
				public void run() {
					for (int x = (int)event.entity.posX-16; x < (int)event.entity.posX+16; ++x) {
						for (int z = (int)event.entity.posZ+48; z < (int)event.entity.posZ+64; ++z) {
							ws.setBlock(x, (int)event.entity.posY-1, z, Blocks.gravel);
							blocks.add(Vec3.createVectorHelper(x, (int)event.entity.posY-1, z));
							ws.setBlock(x, (int)event.entity.posY-1, z-64, Blocks.grass);
						}
					}
				}
			};
			final TimerTask t2Task = new TimerTask() {
				@Override
				public void run() {
					System.out.println(time);
					dist = event.entity.getDistanceToEntity(npc);
					float ratio = (initialDist-dist)/initialDist;
					if (BiGX.instance().context.getSpeed() < 2.2f) {
						BiGX.instance().context.setSpeed(2.2f);
						System.out.println("PLAYER: " + event.entity.motionX + " " + event.entity.motionZ);
					}
					if (ratio > 0.5) {
						if (!doMakeBlocks) {
							doMakeBlocks = true;
							t3.scheduleAtFixedRate(t3Task, 0, 1000);
						}
					} else {
						if (doMakeBlocks) {
							doMakeBlocks = false;
							t3.cancel();
						}
					}
					if (time-- <= 0) {
						t2.cancel();
						time = 30;
						BiGX.instance().context.setSpeed(0);
						for (Vec3 v : blocks) {
							ws.setBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord, Blocks.grass);
						}
					}
				}
			};
			
			final TimerTask tTask = new TimerTask() {
				@Override
				public void run() {
					if (countdown > 0) {
						System.out.println(countdown-- + "...");
					} else {
						System.out.println("GO!");
						command.enableMoving(true);
						countdown = 10;
						t.cancel();
						initialDist = event.entity.getDistanceToEntity(npc);
						t2.scheduleAtFixedRate(t2Task, 0, 1000);
					}
				}
			};
			t.scheduleAtFixedRate(tTask, 0, 1000);
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