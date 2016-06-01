package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class QuestManager {
	private int questID;
	// Server side
	public Map<String,Quest> playerQuestsMapping;
	
	// Client side
	private Quest currentQuest;
	private Quest suggestedQuest;
	private boolean questPopupShown=false;
	
	public QuestManager() {
		questID = 0;
		currentQuest = null;
		suggestedQuest = null;
		questPopupShown = true;
		playerQuestsMapping = new HashMap<String,Quest>();
	}
	
	public Quest makeQuest(String type) {
		Quest quest = Quest.makeQuest(type,questID);
		questID++;
		return quest;
	}
	
	public Quest makeQuest(String type, int ID) {
		Quest quest = Quest.makeQuest(type,ID);
		return quest;
	}
	
	public void setQuest(Quest quest) {
		this.currentQuest = quest;
		System.out.println("set Quest Called.");
	}
	
	public void setSuggestedQuest(Quest quest) {
		this.suggestedQuest = quest;
//		questPopupShown = false;
	}
	
	public void unsetQuestPopupShown()
	{
		questPopupShown = false;
	}
	
	public Quest getQuest() {
		return currentQuest;
	}
	
	public Quest getSuggestedQuest() {
		return suggestedQuest;
	}
	
	public Boolean hasQuestPopupShown() {
		return questPopupShown;
	}
	
	public void showQuestPopup() {
		questPopupShown = true;
	}
	
	public void unloadWorld() {
		playerQuestsMapping.clear();
		currentQuest = null;
		suggestedQuest = null;
	}
	
	public Quest getQuestFromPlayerQuestMap(int ID) {
		for (Quest q:playerQuestsMapping.values()) {
			if (q.getID()==ID) {
				return q;
			}
		}
		return null;
	}
}
