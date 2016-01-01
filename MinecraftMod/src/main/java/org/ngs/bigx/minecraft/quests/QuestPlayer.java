package org.ngs.bigx.minecraft.quests;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class QuestPlayer {
	public double posX,posY,posZ;
	private World world;
	private String name;
	private EntityPlayer entity;

	public QuestPlayer(String playerName, EntityPlayerMP playerEntity) {
		this.name = playerName;
		this.entity = playerEntity;
		getInfo();
	}
	
	public void getInfo() {
		posX = entity.posX;
		posY = entity.posY;
		posZ = entity.posZ;
		world = entity.worldObj;
	}

	public EntityPlayer getEntity() {
		return entity;
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}
}
