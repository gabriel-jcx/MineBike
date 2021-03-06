package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.quests.custom.CustomQuest;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class jeff extends CustomNPCAbstract
{
	public static final String NAME = "jeff";
	public static final Vec3d LOCATION = new Vec3d(116, 70, 238);//new Vec3d
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/TuxedoSteve.png";
	
	private CustomQuestAbstract quest;
	
	public jeff()
	{
		//important that these are set in the constructor
		name = NAME;
		location = LOCATION;
		texture = TEXTURE;
		
		quest = new CustomQuest();
	}

	@Override
	public void onInteraction(EntityPlayer player, PlayerInteractEvent.EntityInteract event) 
	{
		//this happens when the player interacts with jeff
		if (!quest.isStarted())
		{
			GuiMessageWindow.showMessage("My name is jeff!");
			quest.start();
		}
			
		//this is what happens when the player interacts with this NPC
		
//		QuestTeleporter.teleport(Minecraft.getMinecraft().player, -1, 1, 11, 0);
	}
}
