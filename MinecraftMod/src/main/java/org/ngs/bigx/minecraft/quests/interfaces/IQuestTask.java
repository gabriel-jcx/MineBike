package org.ngs.bigx.minecraft.quests.interfaces;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

public interface IQuestTask {
	
	/**
	 * Returns whether or not this QuestEvent is complete (the only thing the Quest needs to know) 
	 */
	public boolean IsComplete();
	public void CheckComplete();
	public boolean IsMainTask();
	public String getTaskDescription();
	public String getTaskName();
}
