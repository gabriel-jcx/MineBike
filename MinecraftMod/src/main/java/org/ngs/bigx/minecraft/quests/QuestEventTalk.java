package org.ngs.bigx.minecraft.quests;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestEventTalk implements IQuestEvent {

	private boolean completed;
	private EntityPlayer player;
	private EntityCustomNpc npc;
	
	public QuestEventTalk(EntityPlayer p, EntityCustomNpc n) {
		player = p;
		npc = n;
	}
	
	@Override
	public boolean IsComplete() {
		return completed;
	}

	@Override
	public void Run() {
		
	}
	
	@Override
	public void CheckComplete() {
		if (npc.interactingEntities.contains(player))
			completed = true;
	}
}
