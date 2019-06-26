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
	public static final Vec3 LOCATION = jeff.LOCATION.addVector(5.0, 0.0, 1.0);
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/SoccerSteve.png";
	
	private CustomQuestAbstract quest;
	
	//Constructor for Raul
	public Raul()
	{
		name = NAME;
		location = LOCATION;
		texture = TEXTURE;
		
		quest = new SoccerQuest();
	}
	
	@Override
	public void onInteraction(EntityPlayer player, EntityInteractEvent event) 
	{
		quest.start();
	}

}
