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
	
	//interpolates between pos1 and pos 2
	//the higher value of t is the closer it is to pos2 
	//0.0 = pos1, 1.0 = pos2
	public static double[] lerp(double[] pos1, double[] pos2, double t)
	{
		if (pos1.length == 3 && pos2.length == 3)
			return new double[] { 
					pos1[0] * (1-t) + pos2[0] * t,
					pos1[1] * (1-t) + pos2[1] * t,
					pos1[2] * (1-t) + pos2[2] * t
					};
		else //if we didn't get valid arguments
			return null;
	}
}