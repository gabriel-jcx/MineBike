package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;

import org.ngs.bigx.minecraft.quests.interfaces.IQuestTask;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Quest implements Runnable {
	public static final String QUEST_ID_STRING_CHASE_REG = "ID_QUEST_CHASE_REG";
	public static final String QUEST_ID_STRING_CHASE_FIRE = "ID_QUEST_CHASE_FIRE";
	
	protected String id; 		// Quest ID by type
	
	private QuestManager questManager;
	private String name, description;
	private String[] requirements;
	private List<EntityPlayer> players;
	private EntityPlayer thePlayer;
	private List<QuestTask> tasks;
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
		this.tasks = new ArrayList<QuestTask>();
	}
	
	public List<QuestTask> getTasks()
	{
		return this.tasks;
	}
	
	public void addTasks(QuestTask questTask) throws QuestException
	{
		if(questTask == null)
			throw new QuestException("Task is null");
		
		if(questTask.getQuestManager() != this.questManager)
		{
			questTask.setQuestManager(this.questManager);
		}
		
		this.tasks.add(questTask);
	}
	
	/**
	 * Returns the progress of a Quest in percentage
	 * @return 0-100 in percentage
	 * @throws QuestException
	 */
	public int getQuestProgress() throws QuestException // returns 0-100% 
	{
		if(this.tasks.size() == 0)
		{
			throw new QuestException("Quest with no task detected");
		}
		
		int returnValue = 0;
		int countTaskDone = 0;
		
		for(QuestTask task : this.tasks)
		{
			if(task.IsComplete())
				countTaskDone++;
		}
		
		returnValue = countTaskDone / this.tasks.size();
		
		return returnValue;
	}
	
	public Quest(String id, String n, String d, String[] requirements) {
		this.id = id;
		tasks = new ArrayList<QuestTask>();
		rewardItems = new ArrayList<ItemStack>();
		name = n;
		description = d;
		this.requirements = requirements;
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

	public String[] getRequirements() {
		return requirements;
	}

	public void AddPlayer(EntityPlayer player) {
		players.add(player);
	}
	
	public IQuestTask getCurrentQuestTask() {
		for (IQuestTask e : this.tasks) {
			if (!e.IsComplete())
				return e;
		}
		return null;
	}
	
	public boolean IsComplete() {
		for (IQuestTask questTask : this.tasks) {
			if (!questTask.IsComplete() && (questTask.IsMainTask()) )
			{
				return false;
			}
		}
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
		for(QuestTask questTask : this.tasks)
		{	
			questTask.deactivateTask();
		}
	}

	@Override
	public void run() {
		synchronized(questManager)
		{
			if(this.tasks.size() == 0)
			{
				try {
					throw new QuestException("The current Quest has not tasks");
				} catch (QuestException e) {
					e.printStackTrace();
				}
				return;
			}
			
			for(QuestTask questTask : this.tasks)
			{
				questTask.setQuestManager(questManager);
				questTask.activateTask();
			}
		}
	}
}
