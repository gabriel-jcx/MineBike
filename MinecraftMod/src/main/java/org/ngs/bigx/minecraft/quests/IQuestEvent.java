package org.ngs.bigx.minecraft.quests;

public interface IQuestEvent {
	
	/**
	 * Returns whether or not this QuestEvent is complete (the only thing the Quest needs to know) 
	 */
	public boolean IsComplete();
	public void CheckComplete();
	
	/**
	 * Allows the quest event to execute any timer-based mechanics, or anything that needs waiting
	 */
	public void Run();
}
