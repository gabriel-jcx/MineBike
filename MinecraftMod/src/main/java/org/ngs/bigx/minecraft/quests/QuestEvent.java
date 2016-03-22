package org.ngs.bigx.minecraft.quests;

public class QuestEvent {
	public enum eventType
	{
		CreateQuest, NotifyQuestPlayers
	}

	public Quest quest;
	public eventType type;
	public int retryCount;
	
	public QuestEvent(Quest quest, eventType type)
	{
		this.quest = quest;
		this.type = type;
		this.retryCount = 3;
	}
}
