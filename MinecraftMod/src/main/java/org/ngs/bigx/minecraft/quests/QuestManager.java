package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

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
	}
	
	public void setActiveQuest(String questid) throws QuestException {
		if(this.availableQuestList.get(questid) == null)
		{
			throw new QuestException("The requeseted Quest is not available");
		}
		
		activeQuestId = questid;
	}
	
	public String getActiveQuestId() {
		return activeQuestId;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public boolean CheckQuestEventCompleted() throws QuestException {
		boolean found = false;
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		for (IQuestTask e : activeQuest.events) {
			if (!e.IsComplete()) {
				found = true;
				e.CheckComplete();
			}
		}
		return found;
	}
}
