package org.ngs.bigx.minecraft.gamestate;

import java.util.ArrayList;

public class GameSaveList {
	private String gamename = "minecraft";
	private ArrayList<GameSave> gameSaves = null;
	
	public GameSaveList()
	{
		this.gamename = "minecraft";
		this.gameSaves = new ArrayList<GameSave>();
	}

	public String getGamename() {
		return gamename;
	}

	public ArrayList<GameSave> getGameSaves() {
		return gameSaves;
	}
	
	public boolean addGameSave(GameSave gamesave)
	{
		if(gamesave == null)
			return false;
		
		if(this.gameSaves == null)
			this.gameSaves = new ArrayList<GameSave>();
		
		this.gameSaves.add(gamesave);
		
		return true;
	}
}
