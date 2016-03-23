package org.ngs.bigx.minecraft;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ngs.bigx.minecraft.item.ModItems;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnClient;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnServer;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestEvent;
import org.ngs.bigx.minecraft.quests.QuestPlayer;
import org.ngs.bigx.minecraft.quests.QuestEvent.eventType;
import org.ngs.bigx.minecraft.quests.QuestRun;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderQuests;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

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
		Context context = Main.instance().context;
		
		//TODO: Implement proper cleanup when the game is exited
		//The event which is called by the server shutting down needs to be located and used
		//instead of this event which is called whenever the game is paused.
		
		//context.unloadWorld();
	}
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerUseItemEvent.Start event) {
		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderQuests.dimID);
		if (ws!=null&&event.entity instanceof EntityPlayerMP) {
			new QuestTeleporter(ws).teleport(event.entity, ws);
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
					
					Quest q = Main.instance().context.questManager.makeQuest("run");
					
					for (WorldServer world:MinecraftServer.getServer().worldServers) {
						List<EntityPlayerMP> playerList = world.playerEntities;
						
						for (EntityPlayerMP player:playerList) {
							q.addPlayer(player.getDisplayName(),Main.instance().context);
						}
					}
					
					System.out.println("[BIGX] CREATE QUEST QUEUEING");
					Main.instance().context.questEventQueue.add(new QuestEvent(q, eventType.CreateQuest));
				}
			}
			
			if(Main.instance().context.questEventQueue.size() == 0)
			{
				return;
			}
			
			QuestEvent questevent = Main.instance().context.questEventQueue.remove();
			Quest quest = questevent.quest;
			eventType type = questevent.type; 
			Collection<QuestPlayer> players = quest.players.values();
			
			switch(type)
			{
			case CreateQuest:
				for (QuestPlayer player : players)
				{
					HandleQuestMessageOnClient packet = new HandleQuestMessageOnClient(quest, Trigger.MakeQuest);
					Main.network.sendTo(packet, (EntityPlayerMP) player.getEntity());
				}
				break;
			case NotifyQuestPlayers:
				for (QuestPlayer player : players)
				{
					HandleQuestMessageOnClient packet = new HandleQuestMessageOnClient(quest, Trigger.NotifyQuest);
					Main.network.sendTo(packet, (EntityPlayerMP) player.getEntity());
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
					
//					if (server_tick==0 && !Main.instance().context.questManager.playerQuestsMapping.containsKey(player.getDisplayName())) {
//						// TODO: Need to revise the code to make a quest (DETECT A FREE GAMER!!!!)
//						if(this.serverQuestTest && (this.serverQuestTestTickCount <0))
//						{
//							this.serverQuestTest = false;
//							
//							Quest q = Main.instance().context.questManager.makeQuest("run");
//							q.addPlayer(player.getDisplayName(),Main.instance().context);
//							
//							HandleQuestMessageOnClient packet = new HandleQuestMessageOnClient(q, Trigger.MakeQuest);
//							Main.network.sendTo(packet,player);
//						}
//					}
		}
	}
		

	
}