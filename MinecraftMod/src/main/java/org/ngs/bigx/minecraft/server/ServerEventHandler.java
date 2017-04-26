package org.ngs.bigx.minecraft.server;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ServerEventHandler {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerRespawn(PlayerInteractEvent event) 
	{
		System.out.println("[Server Log: YH] INTERACT");
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void playerRespawn(PlayerRespawnEvent event) 
	{
		System.out.println("[Server Log: YH] RESPAWN");			
	}
}
