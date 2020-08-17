package org.ngs.bigx.minecraft;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
//import org.bukkit.block.Block;
import noppes.npcs.client.ClientEventHandler;
//import org.ngs.bigx.minecraft.client.ClientEventHandler;

import org.ngs.bigx.minecraft.client.skills.Skill;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.gamestate.GameSave;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager.CUSTOMCOMMAND;


import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.NPCEntityHelper;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;


public class CommonEventHandler {

	private static boolean EVENT_PLAYERSTATE_FLAG = false;
	private static boolean EVENT_PLAYERSTATE_RESET = false;
	private static boolean EVENT_PLAYERSTATE_LOAD = false;
	private static boolean EVENT_PLAYERSTATE_SAVE = false;
	
	public static boolean flagOpenMonsterEncounter = false;
	
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
	
	int fightAndChaseQuestTick = 0;
//	int fightAndChaseQuestTickCount = 90*20;
	int fightAndChaseQuestTickCount = 2000; // Every 20 seconds (10 seconds is too short)
	
	private static int thiefMaxLevel = 1;

	boolean chaseQuestInProgress = false;
	
	private static int onPlayerTickEventCount = 0;
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {

//		BiGX.instance().clientContext.resistance=5;
		if (event.player.world.isRemote)
		{
			try {
				onPlayerTickEventCount++;
				if(onPlayerTickEventCount >= 50)
					onPlayerTickEventCount = 0;

				if(onPlayerTickEventCount == 0 && EVENT_PLAYERSTATE_FLAG)
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
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private boolean inBounds(EntityPlayer player, Vec3d edge1, Vec3d edge2) {
		return (player.posX >= edge1.x && player.posX <= edge2.x || player.posX <= edge1.x && player.posX >= edge2.x) &&
				(player.posY >= edge1.y && player.posY <= edge2.y || player.posY <= edge1.y && player.posY >= edge2.y) &&
				(player.posZ >= edge1.z && player.posZ <= edge2.z || player.posZ <= edge1.z && player.posZ >= edge2.z);
	}

//	@SubscribeEvent
//	public void onWorldLoad(WorldEvent.Load event) {
//		System.out.println("[BiGX] onWorldLoad(WorldEvent.Load event)");
//
////		event.world.provider.setWorldTime(0);
//		event.getWorld().provider.resetRainAndThunder();
//
//		if(event.getWorld().isRemote)
//		{
//			NpcCommand.setNpcSpawnFlag();
//			NpcCommand.addNpcSpawnDimensionId(event.getWorld().provider.getDimension());
//		}
//	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
	//	BigxClientContext context = BiGX.instance().clientContext; // NOTE: what's the significance of this line?
	}

	
	@SubscribeEvent
	public void entityAttacked(LivingAttackEvent event)
	{
//		System.out.println("Attack the player!");

		// TODO: implement this no fall damaage on server? do we need it?
//		EntityLivingBase attackedEnt = event.getEntityLiving();
//		DamageSource attackSource = event.getSource();
//		if(attackedEnt instanceof EntityPlayer)
//		{
//			if(attackSource == DamageSource.FALL)
//			{
//				System.out.println("NO FALL!!!");
//				event.setCanceled(true);
//			}
//		}
	}
	
	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event) {
//		System.out.println("Falling...");
//		if (event.getEntityLiving() != null && event.getEntityLiving() instanceof EntityPlayer) {
//			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
//			player.fallDistance = 0.1F;
//		}
	}

}