package org.ngs.bigx.minecraft.gamestate;

import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;

public class CustomQuestJson 
{
	private int progress;
	private String name;
	private boolean completed;
	private boolean started;
	
	public CustomQuestJson(CustomQuestAbstract quest)
	{
		name = quest.getName();
		completed = quest.isComplete();
		progress = quest.getProgress();
	}
	
	public int getProgress() {return progress;}
	public void setProgress(int progressIn) {progress = progressIn;}
	
	public String getName() {return name;}
	public void setName(String nameIn) {name = nameIn;}
	
	public boolean getCompleted() {return completed;}
	public void setCompleted(boolean completedIn) {completed = completedIn;}
	
	public boolean getStarted() {return started;}
	public void setStarted(boolean startedIn) {started = startedIn;}
}
