package org.ngs.bigx.minecraft.gamestate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameStateSerializer {
	/**
	 * Serialize the GameState class to the game state string 
	 * @param gameState
	 * @return
	 */
	public String serializeGameState(GameState gameState)
	{
		if(gameState == null)
		{
			return "";
		}
		
		Gson gson = new GsonBuilder().serializeNulls().create();
		
		return gson.toJson(gameState); 
	}
	
	/**
	 * Deserialize the game state string to the GameState class 
	 * @param gameStateString
	 * @return
	 */
	public GameState serializeGameState(String gameStateString)
	{
		if(gameStateString.equals(""))
		{
			return null;
		}

		Gson gson = new GsonBuilder().serializeNulls().create();
		
		return gson.fromJson(gameStateString, GameState.class); 
	}
}
