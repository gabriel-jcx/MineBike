package org.ngs.bigx.minecraft.quests.custom.helpers;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.npcs.NpcLocations;

import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class Utils 
{
	public static boolean checkInArea(EntityInteractEvent event, Vec3 location)
	{
		return BiGXEventTriggers.checkEntityInArea(event.target, location.addVector(0, -1, 0), location.addVector(1, 0, 1));
	}
}