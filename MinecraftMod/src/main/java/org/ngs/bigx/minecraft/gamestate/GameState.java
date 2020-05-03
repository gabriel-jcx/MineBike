package org.ngs.bigx.minecraft.gamestate;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;


public class GameState {
	private String gamename = "minecraft";
	
	// PLAYER STATE
	private ArrayList<String> itemOnItemStack;		// Items by item id
	
	// QUEST STATE
//	private GameStateQuestProgress gameStateQuestProgress = null;
	
	// LOCATION (SAVED LOCATION IN GAME)
	private int posX;
	private int posY;
	private int posZ;
	private int posDim;
	
	// UNLOCKED QUEST LIST
	private ArrayList<String> accomplishedQuestId;
	
	public GameState()
	{
		this.gamename = "minecraft";
		this.itemOnItemStack = new ArrayList<String>();
//		this.gameStateQuestProgress = new GameStateQuestProgress();
		this.posDim = 0;
		this.posX = 0;
		this.posY = 0;
		this.posZ = 0;
		this.accomplishedQuestId = new ArrayList<String>();
	}
}
