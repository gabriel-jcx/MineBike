package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class SoccerQuest extends CustomQuestAbstract
{
	private int playerScore;
	private int enemyScore;
	
	public static final int SOCCERDIMENSIONID = 106;
	
	public SoccerQuest()
	{
		super();
		progress = 0;
		name = "SoccerQuest";
		completed = false;
		
		playerScore = 0;
		enemyScore = 0;
		
		register();
	}
	
	
	@Override
	public void start()
	{
		progress = 0;
		
		//teleport them to the soccer arena
//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
		QuestTeleporter.teleport(player, SOCCERDIMENSIONID, 1, 5, 0);
		
		super.start();
	}
}
