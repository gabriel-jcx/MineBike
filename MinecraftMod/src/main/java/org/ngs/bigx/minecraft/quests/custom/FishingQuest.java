package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.npcs.custom.Chum;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFishing;

import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class FishingQuest extends CustomQuestAbstract
{
	public static final int FISHINGDIMENSIONID = WorldProviderFishing.fishingDimID;
	
	public FishingQuest()
	{
		super();
		progress = 0;
		name = "Fishing Quest";
		completed = false;
		
		register();
	}
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) Chum.LOCATION.xCoord, (int) Chum.LOCATION.yCoord, (int) Chum.LOCATION.zCoord);
		super.complete();
	}
	
	@Override
	public void start() 	
	{
		progress = 0;
		
		//teleport them to the soccer arena
//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
		QuestTeleporter.teleport(player, FISHINGDIMENSIONID, 1, 70, 0);
		
		super.start();
	}
}
