package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class TRONQuest extends CustomQuestAbstract
{
	private int playerScore;
	private int enemyScore;
	
	public static final int TRONDIMENSIONID = 107;
	
	public TRONQuest()
	{
		super();
		progress = 0;
		name = "TRONQuest";
		completed = false;
		
		playerScore = 0;
		enemyScore = 0;
		
		register();
	}
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) Raul.LOCATION.xCoord, (int) Raul.LOCATION.yCoord, (int) Raul.LOCATION.zCoord);
		super.complete();
	}
	
	
	@Override
	public void start() 	
	{		
		progress = 0;
		
		//teleport them to the soccer arena
//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
		QuestTeleporter.teleport(player, TRONDIMENSIONID, 1, 30, 0);
		
		super.start();
	}
}
