package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.gui.quest.custom.jeffGui;
import org.ngs.bigx.minecraft.quests.custom.CustomQuest;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class jeff extends CustomNPCAbstract
{
	public static final String NAME = "jeff";
	public static final Vec3 LOCATION = Vec3.createVectorHelper(116, 70, 238);
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
	public void onInteraction(EntityPlayer player, EntityInteractEvent event) 
	{
		//this happens when the player interacts with jeff
		if (!quest.isStarted())
			GuiMessageWindow.showMessage("My name is jeff!");
		
		Minecraft.getMinecraft().displayGuiScreen(new jeffGui((CustomQuest)quest));
		
		quest.start();
			
		//this is what happens when the player interacts with this NPC
		
//		QuestTeleporter.teleport(Minecraft.getMinecraft().thePlayer, -1, 1, 11, 0);
	}
}
