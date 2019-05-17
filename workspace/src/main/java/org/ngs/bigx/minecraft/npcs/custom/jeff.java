package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.npcs.NpcLocations;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

public class jeff extends CustomNPCAbstract
{
	public static final String NAME = "jeff";
	public static final Vec3 LOCATION = Vec3.createVectorHelper(116, 70, 238);
	
	public jeff()
	{
		//important that these are set in the constructor
		name = NAME;
		location = LOCATION;
	}

	@Override
	public void onInteraction() 
	{
		GuiMessageWindow.showMessage("My name is jeff!");
			
		//this is what happens when the player interacts with this NPC
		
//		QuestTeleporter.teleport(Minecraft.getMinecraft().thePlayer, -1, 1, 11, 0);
	}
}
