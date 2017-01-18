package org.ngs.bigx.minecraft;

import java.util.Collection;
import java.util.List;

import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnClient;
import org.ngs.bigx.minecraft.quests.Quest;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.entity.EntityCustomNpc;


public class CommonEventHandler {
	
	int server_tick = 0;
	boolean serverQuestTest = true;
	int serverQuestTestTickCount = 10;
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		
		BikeWorldData data = BikeWorldData.get(event.world);
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
	public void onItemUse(PlayerUseItemEvent.Start event) {
		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderFlats.dimID);
		if (ws != null && event.entity instanceof EntityPlayerMP) {
			QuestTeleporter teleporter = new QuestTeleporter(ws);
			teleporter.teleport(event.entity, ws);
			EntityPlayerMP player = (EntityPlayerMP)event.entity;
			EntityCustomNpc npc = NpcCommand.spawnNpc(0f, 10f, 10f, ws, "Thief");
			//NpcCommand.addPathPoint(npc, 0, 10, 20);
			NpcCommand command = new NpcCommand(npc);
			command.enableMoving(true);
			command.setSpeed(10);
			command.runInDirection(ForgeDirection.EAST);
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
		

	
}