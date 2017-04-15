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
import org.ngs.bigx.minecraft.quests.QuestEventChasing;
import org.ngs.bigx.minecraft.quests.QuestEventChasingFire;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
	
	private static int thiefMaxLevel = 1;
	
	public static LevelSystem levelSys = new LevelSystem();
	public static QuestEventChasing chaseQuest = new QuestEventChasing();
	public static QuestEventChasingFire chaseQuestFire = new QuestEventChasingFire();
	boolean chaseQuestInProgress = false;
	
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (inBounds(event.player, Vec3.createVectorHelper(118, 152, -148), Vec3.createVectorHelper(125, 146, -151))) {
			QuestTeleporter teleporter = new QuestTeleporter(MinecraftServer.getServer().worldServerForDimension(WorldProviderDungeon.dimID));
			teleporter.teleport(event.player, MinecraftServer.getServer().worldServerForDimension(WorldProviderDungeon.dimID), 0, 64, 0);
		}
	}
	
	private boolean inBounds(EntityPlayer player, Vec3 edge1, Vec3 edge2) {
		return (player.posX >= edge1.xCoord && player.posX <= edge2.xCoord || player.posX <= edge1.xCoord && player.posX >= edge2.xCoord) &&
				(player.posY >= edge1.yCoord && player.posY <= edge2.yCoord || player.posY <= edge1.yCoord && player.posY >= edge2.yCoord) &&
				(player.posZ >= edge1.zCoord && player.posZ <= edge2.zCoord || player.posZ <= edge1.zCoord && player.posZ >= edge2.zCoord);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		event.world.provider.setWorldTime(0);
		NpcCommand.setNpcSpawnFlag();
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
	public void entityInteractEvent(EntityInteractEvent e) {
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
	
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
		System.out.println(event.item.getDisplayName());
		if (event.item.getDisplayName().contains("Teleportation Potion")) {
			EntityPlayer player = event.entityPlayer;
			QuestEventChasing.player = event.entityPlayer;
			QuestEventChasingFire.player = event.entityPlayer;
			if (player.getHeldItem().getDisplayName().contains("Teleportation Potion") && QuestEventChasing.checkPlayerInArea(player, 94, 53, -54, 99, 58, -48))
			{
				chaseQuest.Run(levelSys);
				chaseQuestInProgress = true;
			}
			else if (player.getHeldItem().getDisplayName().contains("Teleportation Potion") && QuestEventChasingFire.checkPlayerInArea(player, -50, 50, -50, 50, 100, 50))
			{
				chaseQuestFire.Run(levelSys);
				chaseQuestInProgress = true;
			}
			else if (chaseQuestInProgress)
				chaseQuest.goBackToOriginalWorld(); //probably want to make this a part of the template class or something
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
			
			//20 ticks = 1 second
			if (server_tick==20) {
				server_tick = 0;
				NpcCommand.spawnNpcInDB(MinecraftServer.getServer().worldServerForDimension(0), MinecraftServer.getServer().getEntityWorld());
				NpcCommand.spawnTheifOnRegularChaseQuest();
				NpcCommand.spawnTheifOnFireChaseQuest();
			}
			
			//Making sure it remains daytime all the time
			World current_world = MinecraftServer.getServer().getEntityWorld();

			current_world.setWorldTime(8000);
			
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
				}
			}
		}
	}
}