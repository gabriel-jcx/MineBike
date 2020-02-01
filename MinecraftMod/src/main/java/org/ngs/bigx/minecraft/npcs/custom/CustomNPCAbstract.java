package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.npcs.NpcDatabase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public abstract class CustomNPCAbstract 
{
	//Three properties of an NPC
	protected String name;
	protected Vec3d location;
	protected String texture;
	
	//important that this method is called after the instantiation of the object
	public void register()
	{
		NpcDatabase.registerNPC(name, location);
	}
	
	public abstract void onInteraction(EntityPlayer player, EntityInteractEvent event);
	
	public Vec3d getLocation() {return location;}
	public String getName() {return name;}
	public String getTexture() {return texture;}
}
