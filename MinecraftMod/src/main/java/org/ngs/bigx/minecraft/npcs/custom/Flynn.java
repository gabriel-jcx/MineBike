package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.quests.custom.TRONQuest;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class Flynn extends CustomNPCAbstract 
{
	public static final String NAME = "Flynn";
	public static final Vec3 LOCATION = Vec3.createVectorHelper(109, 71, 231);
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/MercenarySteve.png";
	//MincraftTRONSkin
	
	private CustomQuestAbstract quest;
	
	public Flynn()
	{
		name = NAME;
		location = LOCATION;
		texture = TEXTURE;		
		quest = new TRONQuest();
		
		register();
	}
	
	@Override
	public void onInteraction(EntityPlayer player, EntityInteractEvent event) 
	{
		quest.start();
	}

}
