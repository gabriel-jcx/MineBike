package org.ngs.bigx.minecraft.client;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
	public List<LeaderboardRow> leaderboardRows;
	
	public Leaderboard()
	{
		this.leaderboardRows = new ArrayList<LeaderboardRow>();
	}
}
