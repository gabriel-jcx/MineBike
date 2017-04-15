package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.levelUp.LevelSystem;

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
	public void Run(final LevelSystem levelSys) {
		
	}
	
	@Override
	public void CheckComplete() {
		if (npc.interactingEntities.contains(player))
			completed = true;
	}
}
