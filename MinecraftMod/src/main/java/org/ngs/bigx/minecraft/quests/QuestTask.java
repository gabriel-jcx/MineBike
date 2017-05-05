package org.ngs.bigx.minecraft.quests;

import java.util.TimerTask;

import org.ngs.bigx.minecraft.quests.interfaces.IQuestTask;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public abstract class QuestTask implements IQuestTask, Runnable {
	protected boolean completed;
	public EntityPlayer player;
	protected boolean isRequired;
	protected QuestManager questManager;
	protected boolean isActive = false;
	
	public QuestTask(QuestManager questManager, boolean isMainTask)
	{
		this.questManager = questManager;
		this.isRequired = isMainTask;
	}
	
	public QuestManager getQuestManager() {
		return questManager;
	}

	public void setQuestManager(QuestManager questManager) {
		this.questManager = questManager;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * Determines this task is optional or not
	 * @return false if the task is optional. true, otherwise.
	 */
	public boolean IsMainTask()
	{
		return this.isRequired;
	}
	
	public boolean IsComplete()
	{
		return this.completed;
	}
	
	public void activateTask()
	{
		this.isActive = true;
		
		new Thread(this).start();
	}
	
	public void deactivateTask()
	{
		this.isActive = false;
		this.unregisterEvents();
	}
	
	public abstract void init();
	public abstract void unregisterEvents();
	public abstract void registerEvents();
}
