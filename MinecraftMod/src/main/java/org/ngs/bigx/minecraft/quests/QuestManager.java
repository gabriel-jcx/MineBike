package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * QuestDemo - Active Quest   - Quest Detail
 *            \ Player (Local) \ List of Players
 *                              \ Quest Events List - RUN/REWARD/DONE
 *                                                   \ Types Quest Requirements
 * @author localadmin
 *
 */

public class QuestManager {
	private HashMap<String, Quest> availableQuestList;		// String is the ID of the quest
	private String activeQuestId;
	private EntityPlayer player;
	
	public QuestManager(EntityPlayer p) {
		player = p;
		this.availableQuestList = new HashMap<String, Quest>();
		activeQuestId = "NONE";
	}
	
	public void setActiveQuest(String questid) throws QuestException {
		if(activeQuestId == "NONE" || this.availableQuestList.get(questid) == null)
		{
			throw new QuestException("The requeseted Quest is not available");
		}
		
		activeQuestId = questid;
	}
	
	public String getActiveQuestId() {
		return activeQuestId;
	}
	
	// WARNING: volatile - use this to read information, not set new information
	public Quest getActiveQuest() {
		return availableQuestList.get(activeQuestId);
	}
	
	// WARNING: volatile - use this to read information, not set new information
	public IQuestTask getActiveQuestTask() {
		return availableQuestList.get(activeQuestId).getCurrentQuestEvent();
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public List<ItemStack> getActiveQuestRewards() throws QuestException {
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		return activeQuest.GetRewardItems();
	}
	
	public int getActiveQuestRewardXP() throws QuestException {
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		return activeQuest.GetRewardXP();
	}
	
	public boolean CheckActiveQuestCompleted() throws QuestException {
		if (activeQuestId == "NONE") {
			return false;
		}
		
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		return activeQuest.IsComplete();
	}
	
	public boolean CheckQuestEventCompleted() throws QuestException {
		if (activeQuestId == "NONE") {
			return false;
		}
		
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		for (IQuestTask e : activeQuest.tasks) {
			if (!e.IsComplete()) {
				e.CheckComplete();
				return e.IsComplete();
			}
		}
		return false;
	}
}
