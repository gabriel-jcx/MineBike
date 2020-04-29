package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestTask;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Quest implements Runnable {
	public static final String QUEST_ID_STRING_NONE = "ID_QUEST_NONE";
	public static final String QUEST_ID_STRING_TUTORIAL = "ID_QUEST_TUTORIAL";
	public static final String QUEST_ID_STRING_CHASE_REG = "ID_QUEST_CHASE_REG";
	public static final String QUEST_ID_STRING_FIGHT_CHASE = "ID_QUEST_FIGHT_CHASE";
	
	protected String id; 		// Quest ID by type
	
	private QuestManager questManager;
	private String name, description;
	private List<EntityPlayer> players;
	private EntityPlayer player;
//	private List<QuestTask> tasks;
	private List<ItemStack> rewardItems;
	private int rewardXP, rewardCoins;
	
	public Quest(String id, QuestManager questManager) {
		this(id, "Quest", "I'm a quest!", questManager);
	}
	
	public Quest(String id, String n, String d, QuestManager questManager) {
		this.id = id;
		this.name = n;
		this.description = d;
		this.questManager = questManager;
//		this.tasks = new ArrayList<QuestTask>();
	}
	
//	public List<QuestTask> getTasks()
//	{
//		return this.tasks;
//	}
//
//	public void addTasks(QuestTask questTask) throws QuestException
//	{
//		if(questTask == null)
//			throw new QuestException("Task is null");
//
//		if(questTask.getQuestManager() != this.questManager)
//		{
//			questTask.setQuestManager(this.questManager);
//		}
//
//		for(QuestTask task : this.tasks)
//		{
//			if(task.getClass().equals(questTask.getClass()))
//			{
//				System.out.println("[BiGX] Duplicate Quest Task Detected");
//				return;
//			}
//		}
//
//		this.tasks.add(questTask);
//	}
	
	/**
	 * Returns the progress of a Quest in percentage
	 * @return 0-100 in percentage
	 * @throws QuestException
	 */
	public int getQuestProgress() throws QuestException // returns 0-100% 
	{
//		if(this.tasks.size() == 0)
//		{
//			throw new QuestException("Quest with no task detected");
//		}
		
		int returnValue = 0;
		int countTaskDone = 0;
		
//		for(QuestTask task : this.tasks)
//		{
//			if(task.isCompleted())
//				countTaskDone++;
//		}
		
//		returnValue = countTaskDone / this.tasks.size();
		
		return returnValue;
	}
	
	public String getQuestId()
	{
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}



	public void AddPlayer(EntityPlayer player) {
		players.add(player);
	}
	
	public IQuestTask getCurrentQuestTask() {
//		for (IQuestTask e : this.tasks) {
//			if (!e.isCompleted())
//				return e;
//		}
		return null;
	}
	
	public boolean IsComplete() {
//		for (IQuestTask questTask : this.tasks) {
//			if (!questTask.isCompleted() && (questTask.IsMainTask()) )
//			{
//				return false;
//			}
//		}
		return true;
	}
	
	public List<ItemStack> GetRewardItems() {
		return rewardItems;
	}
	
	public int GetRewardXP() {
		return rewardXP;
	}
	
	public int GetRewardCoins() {
		return rewardCoins;
	}
	
	public void stop()
	{
//		for(QuestTask questTask : this.tasks)
//		{
//			questTask.deactivateTask();
//		}
	}

	@Override
	public void run() {
		synchronized(questManager)
		{
//			if(this.tasks.size() == 0)
//			{
//				try {
//					throw new QuestException("The current Quest has not tasks");
//				} catch (QuestException e) {
//					e.printStackTrace();
//				}
//				return;
//			}
//
//			for(QuestTask questTask : this.tasks)
//			{
//				questTask.setQuestManager(questManager);
//				questTask.activateTask();
//			}
		}
	}
}
