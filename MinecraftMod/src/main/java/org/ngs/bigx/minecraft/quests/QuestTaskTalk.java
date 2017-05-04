package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskTalk implements IQuestTask {

	private boolean completed;
	private EntityPlayer player;
	private EntityCustomNpc npc;
	
	public QuestTaskTalk(EntityPlayer p, EntityCustomNpc n) {
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

	@Override
	public boolean IsMainTask() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTaskDescrption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskName() {
		// TODO Auto-generated method stub
		return null;
	}
}
