package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.gamestate.CustomQuestJson;
import org.ngs.bigx.minecraft.quests.custom.OverCookedQuest;
import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ChefGusteau extends CustomNPCAbstract
{
	public static final String NAME = "ChefGusteau";
	public static final Vec3d LOCATION = new Vec3d(107, 91, -36);
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/Chef_Gusteau.png";
	
	private OverCookedQuest quest;
	
	public ChefGusteau()
	{
		this.name = NAME;
		this.location = LOCATION;
		this.texture = TEXTURE;
		
		quest = new OverCookedQuest();
	}
	
	@Override
	public void onInteraction(EntityPlayer player, PlayerInteractEvent.EntityInteract event)
	{
		GuiMessageWindow.showMessage("     Anyone can cook� \n       So can you.");
		CustomQuestJson json = new CustomQuestJson(quest);
		quest = new OverCookedQuest();
		quest.loadFromJson(json);
		quest.start();
	}
	
}
