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
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
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
	
	private static int thiefMaxLevel = 1;
	
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
	void onPlayerInteractwithNPC(EntityInteractEvent e) {
		System.out.println("Player Interact w/ NPC Event");
}
	
	@SubscribeEvent
	public void entityInteractEvent(EntityInteractEvent e){
		EntityPlayer player = e.entityPlayer;
		System.out.println("Entity Interact Event");
		BiGXEventTriggers.InteractWithNPC(player, e);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e) {
		EntityPlayer player = e.entityPlayer;
		World w = e.world;
		
		if (!w.isRemote) {
			if (e.x == -155 && e.y == 71 && e.z == 359 && w.getBlock(e.x, e.y, e.z) == Blocks.chest) {
				System.out.println("CHEST FOUND");
				TileEntityChest c = (TileEntityChest)w.getTileEntity(e.x, e.y, e.z);
				ItemStack b = new ItemStack(Items.written_book);
				NBTTagList pages = new NBTTagList();
				pages.appendTag(new NBTTagString("In this cave lies a secret room. In order to save your father, you must follow the sounds of music nearby and unlock what's beyond the art."));
				b.stackTagCompound = new NBTTagCompound();
				b.stackTagCompound.setTag("author", new NBTTagString("A friend"));
				b.stackTagCompound.setTag("title", new NBTTagString("A hint that may help"));
				b.stackTagCompound.setTag("pages", pages);
				if (!e.entityPlayer.inventory.hasItemStack(b))
					c.setInventorySlotContents(0, b);
			}
			
			if (e.x == -174 && e.y == 70 && e.z == 336 && w.getBlock(e.x, e.y, e.z) == Blocks.chest){
				System.out.println("SECRET CHEST FOUND");
				BiGXEventTriggers.onRightClick(e, player);
				TileEntityChest c = (TileEntityChest)w.getTileEntity(e.x, e.y, e.z);
				ItemStack b = new ItemStack(Items.written_book);
				NBTTagList pages = new NBTTagList();
				pages.appendTag(new NBTTagString("Use this potion to persue he who wants to bring harm to your father."));
				b.stackTagCompound = new NBTTagCompound();
				b.stackTagCompound.setTag("author", new NBTTagString("A friend"));
				b.stackTagCompound.setTag("title", new NBTTagString("Potion Instructions"));
				b.stackTagCompound.setTag("pages", pages);
				//if (!e.entityPlayer.inventory.hasItemStack(b))
				c.setInventorySlotContents(0, b);
				
				for (int i = 1; i <= thiefMaxLevel; ++i){
					ItemStack p = new ItemStack(Items.potionitem);
					p.setStackDisplayName("Teleportation Potion " + i);
					//if (!e.entityPlayer.inventory.hasItemStack(p))
					c.setInventorySlotContents(i, p);
				}
			}
		}
	}
	
	// TODO BUG: Player transports to Quest World when items are used (leave this in for testing purposes)
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
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
			}
			
			//Making sure it remains daytime all the time
			World current_world = MinecraftServer.getServer().getEntityWorld();
//			if (current_world.provider.getWorldTime() >= 12000)
//				current_world.setWorldTime(0);
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
					
					//makeQuestOnServer();
				}
			}
		}
	}
}