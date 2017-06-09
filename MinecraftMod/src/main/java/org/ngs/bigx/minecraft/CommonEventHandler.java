package org.ngs.bigx.minecraft;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.jws.Oneway;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.dictionary.objects.game.properties.Stage;
import org.ngs.bigx.dictionary.objects.game.properties.StageSettings;
import org.ngs.bigx.dictionary.protocol.Specification.GameTagType;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiLeaderBoard;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager.CUSTOMCOMMAND;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.npcs.NpcEvents;
import org.ngs.bigx.minecraft.npcs.NpcLocations;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.minecraft.quests.QuestTask;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskTutorial;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiome;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.fire.TerrainBiomeFire;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.utility.NpcCommand;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.entity.EntityCustomNpc;


public class CommonEventHandler {
	private static boolean EVENT_PLAYERSTATE_FLAG = false;
	private static boolean EVENT_PLAYERSTATE_RESET = false;
	private static boolean EVENT_PLAYERSTATE_LOAD = false;
	private static boolean EVENT_PLAYERSTATE_SAVE = false;
	
	public static void setEVENT_PLAYERSTATE_FLAG() {
		EVENT_PLAYERSTATE_FLAG = true;
	}

	public static void setEVENT_PLAYERSTATE_RESET() {
		setEVENT_PLAYERSTATE_FLAG();
		EVENT_PLAYERSTATE_RESET = true;
	}

	public static void setEVENT_PLAYERSTATE_LOAD() {
		setEVENT_PLAYERSTATE_FLAG();
		EVENT_PLAYERSTATE_LOAD = true;
	}

	public static void setEVENT_PLAYERSTATE_SAVE() {
		setEVENT_PLAYERSTATE_FLAG();
		EVENT_PLAYERSTATE_SAVE = true;
	}

	static float playerQuestPitch, playerQuestYaw;
	
	int server_tick = 0;
	int serverQuestTestTickCount = 10;
	
	private static int thiefMaxLevel = 1;
	
	public static LevelSystem levelSys = new LevelSystem();
//	public static QuestTaskChasing chaseQuest = new QuestTaskChasing();
//	public static QuestTaskChasingFire chaseQuestFire = new QuestTaskChasingFire();
	boolean chaseQuestInProgress = false;
	
	private static int onPlayerTickEventCount = 0;
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (inBounds(event.player, Vec3.createVectorHelper(118, 152, -148), Vec3.createVectorHelper(125, 146, -151)) &&
				event.player.dimension == 0) {
			QuestTeleporter.teleport(event.player, WorldProviderDungeon.dimID, 0, 64, 0);
		}
		else if (inBounds(event.player, Vec3.createVectorHelper(3, 63, 10), Vec3.createVectorHelper(-3, 68, 20)) &&
				event.player.dimension == WorldProviderDungeon.dimID) {
			QuestTeleporter.teleport(event.player, 0, 121, 163, -145);
		}
		
		if (!event.player.worldObj.isRemote)
		{
			try {
				onPlayerTickEventCount++;
				
				if(onPlayerTickEventCount >= 50)
					onPlayerTickEventCount = 0;
				
				if(onPlayerTickEventCount == 0)
				{
					if(EVENT_PLAYERSTATE_FLAG)
					{
						if(EVENT_PLAYERSTATE_LOAD)
						{
							GameSaveManager.sendCustomCommand((BigxClientContext)BigxClientContext.getInstance(), BiGX.BIGXSERVERIP, CUSTOMCOMMAND.GETGAMESAVES);
							EVENT_PLAYERSTATE_LOAD = false;
						}
						if(EVENT_PLAYERSTATE_RESET)
						{
							/**
							 * 
							 * 
							 * 
							 * FILL THIS IN TO IMPLEMENT THE GAME STATE RESET FEATURE
							 * 
							 * 
							 */
							EVENT_PLAYERSTATE_RESET = false;
						}
						if(EVENT_PLAYERSTATE_SAVE)
						{
							GameSaveManager.sendCustomCommand((BigxClientContext)BigxClientContext.getInstance(), BiGX.BIGXSERVERIP, CUSTOMCOMMAND.SETGAMESAVES);
							EVENT_PLAYERSTATE_SAVE = false;
						}
						
						if(!(EVENT_PLAYERSTATE_LOAD || EVENT_PLAYERSTATE_RESET || EVENT_PLAYERSTATE_SAVE))
							EVENT_PLAYERSTATE_FLAG = false;
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean inBounds(EntityPlayer player, Vec3 edge1, Vec3 edge2) {
		return (player.posX >= edge1.xCoord && player.posX <= edge2.xCoord || player.posX <= edge1.xCoord && player.posX >= edge2.xCoord) &&
				(player.posY >= edge1.yCoord && player.posY <= edge2.yCoord || player.posY <= edge1.yCoord && player.posY >= edge2.yCoord) &&
				(player.posZ >= edge1.zCoord && player.posZ <= edge2.zCoord || player.posZ <= edge1.zCoord && player.posZ >= edge2.zCoord);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		System.out.println("[BiGX] onWorldLoad(WorldEvent.Load event)");
		
		event.world.provider.setWorldTime(0);
		event.world.provider.resetRainAndThunder();
		
		if(event.world.isRemote)
		{	
			NpcCommand.setNpcSpawnFlag();
			NpcCommand.addNpcSpawnDimensionId(event.world.provider.dimensionId);
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
//		BigxContext context = BiGX.instance().context;
		
		//TODO: Implement proper cleanup when the game is exited
		//The event which is called by the server shutting down needs to be located and used
		//instead of this event which is called whenever the game is paused.
		
		//context.unloadWorld();
	}
	
	@SubscribeEvent
	public void entityInteractEvent(EntityInteractEvent e) {
		EntityPlayer player = e.entityPlayer;
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
	
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
		if (event.item.getDisplayName().contains("Teleportation Potion - Village"))
			QuestTeleporter.teleport(event.entityPlayer, 0, 94, 71, 227);
		if (event.item.getDisplayName().contains("Teleportation Potion - Past"))
			QuestTeleporter.teleport(event.entityPlayer, 0, 88, 78, 243);
//		if (event.item.getDisplayName().contains("Sword"))
//			QuestTeleporter.teleport(event.entityPlayer, 102, 1, 64, 1);
	}
	
	@SubscribeEvent
	public void onAttackEntityEvent(AttackEntityEvent event) {
		System.out.println("COMMON EVENT ATTAAAACK");
		if (event.target.worldObj.isRemote){
			if (event.target.toString().contains("Scientist"))
				GuiMessageWindow.showMessage("Scientist: Don't hit me...");
			else if (BiGX.instance().clientContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_TUTORIAL){
				Quest activeQuest = BiGX.instance().clientContext.getQuestManager().getActiveQuest();
				QuestTaskTutorial tutorialTask = (QuestTaskTutorial) activeQuest.getCurrentQuestTask();
				tutorialTask.hitEntity(event.entityPlayer, (EntityLivingBase) event.target);
			}
		}
		else if (BiGX.instance().serverContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_TUTORIAL){
			if (event.target.toString().contains("Scientist"))
				return;
			Quest activeQuest = BiGX.instance().serverContext.getQuestManager().getActiveQuest();
			QuestTaskTutorial tutorialTask = (QuestTaskTutorial) activeQuest.getCurrentQuestTask();
			tutorialTask.hitEntity(event.entityPlayer, (EntityLivingBase) event.target);
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
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) throws Exception {
		if (MinecraftServer.getServer() != null && event.phase == TickEvent.Phase.END) {
			boolean isServer = MinecraftServer.getServer().isDedicatedServer();
			server_tick++;
			
			//200 ticks = 10 second
			if (server_tick==200) {
				server_tick = 0;
				
				//Making sure it remains daytime all the time
				World current_world = MinecraftServer.getServer().getEntityWorld();

				current_world.setWorldTime(8000);
			}
			
			if( (server_tick%60) == 0 )
			{
				NpcCommand.spawnNpcInDB();
			}
		}
	}
}