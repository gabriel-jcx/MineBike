package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskTalk implements IQuestTask {

	private boolean completed;
	private EntityPlayer player;
	private EntityCustomNpc npc;
	private boolean isRequired;
	
	public QuestTaskTalk(EntityPlayer p, EntityCustomNpc n) {
		player = p;
		npc = n;
		isRequired = false;
	}
	
	
	public QuestTaskTalk(EntityPlayer p, EntityCustomNpc n, boolean required) {
		player = p;
		npc = n;
		isRequired = required;
	}
	
	@Override
	public boolean IsComplete() {
		return completed;
	}

	@Override
	public void Run(LevelSystem levelSys) {
		
	}
	
	@Override
	public void CheckComplete() {
		if (npc.interactingEntities.contains(player))
			completed = true;
	}

	@Override
	public boolean IsMainTask() {
		return isRequired;
	}

	@Override
	public String getTaskDescription() {
		return null;
	}

	@Override
	public String getTaskName() {
		return null;
	}
}
