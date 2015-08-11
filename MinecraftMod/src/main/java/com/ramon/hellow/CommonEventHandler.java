package com.ramon.hellow;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ramon.hellow.item.ModItems;
import com.ramon.hellow.worldgen.WorldStructure;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;

public class CommonEventHandler {
	
	int server_tick = 0;
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) event.entity;
			p.inventory.clearInventory(ModItems.Bike,-1);
			p.inventory.addItemStackToInventory(new ItemStack(ModItems.Bike));
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		BikeWorldData data = BikeWorldData.get(event.world);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
	}
	 
	 //Called when the server ticks. Usually 20 ticks a second. 
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (MinecraftServer.getServer()!=null&&event.phase==TickEvent.Phase.END) {
			boolean isServer = MinecraftServer.getServer().isDedicatedServer();
			server_tick++;
			//20 ticks = 1 second
			if (server_tick==20) {
				server_tick = 0;
			}
			WorldServer[] ws = MinecraftServer.getServer().worldServers;
			for (WorldServer w:ws) {
					List<EntityPlayerMP> lp = w.playerEntities;
					for (EntityPlayerMP p:lp) {
						if (p.isRiding()) {
							p.ridingEntity.setVelocity(p.motionX, p.ridingEntity.motionY, p.motionY);
						}
						//If 1 or 0.5 seconds has elapsed
						if (server_tick==0||server_tick==10) {
						}
					}
			}
		}
	}
		
	//Currently unused method, merely a reference for me to setup further reflection
	public static void setFoodLevelReflection(EntityPlayer p,int food) {
		try {
			Class foodStats = net.minecraft.util.FoodStats.class;
			Field foodLevel = foodStats.getDeclaredField("field_75127_a");
			foodLevel.setAccessible(true);
			foodLevel.set(p.getFoodStats(),food);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setFoodLevel(EntityPlayer p,int food) {
		p.getFoodStats().addStats(Math.min(food-p.getFoodStats().getFoodLevel(),20), 0);
	}
	
}