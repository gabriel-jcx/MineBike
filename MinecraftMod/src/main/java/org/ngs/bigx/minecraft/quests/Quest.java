package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Quest {
	protected String id; 		// Quest ID by type
	
	private String name, description;
	private String[] requirements;
	private List<EntityPlayer> players;
	public List<IQuestTask> tasks;
	private List<ItemStack> rewardItems;
	private int rewardXP, rewardCoins;
	
	public Quest(String id) {
		this(id, "Quest", "I'm a quest!");
	}
	
	public Quest(String id, String n, String d) {
		this(id, n, d, new String[1]);
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
		
		for(IQuestTask task : this.tasks)
		{
			if(task.IsComplete())
				countTaskDone++;
		}
		
		returnValue = countTaskDone / this.tasks.size();
		
		return returnValue;
	}
	
	public Quest(String id, String n, String d, String[] requirements) {
		this.id = id;
		events = new ArrayList<IQuestTask>();
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
	
	public IQuestTask getCurrentQuestEvent() {
		for (IQuestTask e : events) {
			if (!e.IsComplete())
				return e;
		}
		return null;
	}
	
	public boolean IsComplete() {
		for (IQuestTask questEvent : events) {
			if (!questEvent.IsComplete())
				return false;
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
}
