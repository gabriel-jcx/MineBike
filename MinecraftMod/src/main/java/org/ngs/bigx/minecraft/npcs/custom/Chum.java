package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.quests.custom.FishingQuest;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class Chum extends CustomNPCAbstract
{

	public static final String NAME = "Chum";
	public static final Vec3 LOCATION = Vec3.createVectorHelper(96, 86, -51);
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/Fisherman.png";
	
	private CustomQuestAbstract quest;
	
	public Chum()
	{
		name = NAME;
		location = LOCATION;
		texture = TEXTURE;
		
		quest = new FishingQuest();
	}
	
	@Override
	public void onInteraction(EntityPlayer player, EntityInteractEvent event) 
	{
		// TODO Auto-generated method stub
		quest.start();
		System.out.println("Interacting with Jah");
		GuiMessageWindow.showMessage("On Jah you bout to catch \n        these fish?");
		
	}

}
