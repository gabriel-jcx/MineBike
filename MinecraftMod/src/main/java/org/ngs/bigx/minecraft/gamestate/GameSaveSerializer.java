package org.ngs.bigx.minecraft.gamestate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameSaveSerializer {
	/**
	 * Serialize the GameSaveList class to the game save string 
	 * @param gameSavelist
	 * @return
	 */
	public String serializeGameState(GameSaveList gameSavelist)
	{
		if(gameSavelist == null)
		{
			return "";
		}
		
		Gson gson = new GsonBuilder().serializeNulls().create();
		
		return gson.toJson(gameSavelist); 
	}
	
	/**
	 * Deserialize the game save list string to the GameSaveList class 
	 * @param gameSaveListString
	 * @return
	 */
	public GameSaveList serializeGameState(String gameSaveListString)
	{
		if(gameSaveListString.equals(""))
		{
			return null;
		}

		Gson gson = new GsonBuilder().serializeNulls().create();
		
		return gson.fromJson(gameSaveListString, GameSaveList.class); 
	}
}
