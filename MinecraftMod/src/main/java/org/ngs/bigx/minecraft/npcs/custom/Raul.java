package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class Raul extends CustomNPCAbstract 
{
	public static final String NAME = "Raul";
	public static final Vec3 LOCATION = Vec3.createVectorHelper(98, 71, 128);
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/SoccerSteve.png";
	
	private CustomQuestAbstract quest;
	
	//Constructor for Raul
	public Raul()
	{
		name = NAME;
		location = LOCATION;
		texture = TEXTURE;
		
		quest = new SoccerQuest();
		
		this.register();
	}
	
	@Override
	public void onInteraction(EntityPlayer player, EntityInteractEvent event) 
	{
		System.out.println("Interacting with raul!");
		quest.start();
	}

}
