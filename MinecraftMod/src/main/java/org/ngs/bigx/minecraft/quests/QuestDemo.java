package org.ngs.bigx.minecraft.quests;

import net.minecraft.entity.player.EntityPlayer;

public class QuestDemo {
	private Quest activeQuest;
	private EntityPlayer player;
	
	public QuestDemo(EntityPlayer p) {
		player = p;
	}
	
	public void setActiveQuest(Quest q) {
		activeQuest = q;
	}
	
	public Quest getQuest() {
		return activeQuest;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public boolean CheckQuestEventCompleted() {
		boolean found = false;
		for (IQuestEvent e : activeQuest.events) {
			if (!e.IsComplete()) {
				found = true;
				e.CheckComplete();
			}
		}
		return found;
	}
}
