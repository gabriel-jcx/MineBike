package org.ngs.bigx.minecraft.gamestate;

import java.util.ArrayList;

import net.minecraft.item.Item;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

public class GameState {
	private String gamename = "minecraft";
	
	// PLAYER STATE
	private LevelSystem levelSystem = null;
	private ArrayList<Item> itemOnItemStack;
	
	// QUEST STATE
	// LOCATION (LOGGED)
	// UNLOCKED AREA LIST
	
	public GameState()
	{
		this.gamename = "minecraft";
		this.levelSystem = new LevelSystem();
		this.itemOnItemStack = new ArrayList();
	}
}
