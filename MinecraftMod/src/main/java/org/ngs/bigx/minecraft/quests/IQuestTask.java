package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

public interface IQuestTask {
	
	/**
	 * Returns whether or not this QuestEvent is complete (the only thing the Quest needs to know) 
	 */
	public boolean IsComplete();
	public void CheckComplete();
	public boolean IsMainTask();
	public String getTaskDescrption();
	public String getTaskName();
	
	/**
	 * Allows the quest event to execute any timer-based mechanics, or anything that needs waiting
	 */
	public void Run(LevelSystem levelsys);
}
