package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskTalk extends QuestTask {
	private EntityCustomNpc npc;
	
	private int questTypeId = 3;
	
	public QuestTaskTalk(QuestManager questManager, EntityPlayer p, EntityCustomNpc n) {
		this(questManager, p, n, false);
	}
	
	
	public QuestTaskTalk(QuestManager questManager, EntityPlayer p, EntityCustomNpc n, boolean required) {
		super(questManager, required);
		player = p;
		npc = n;
		isRequired = required;
	}
	
	@Override
	public void CheckComplete() {
		if (npc.interactingEntities.contains(player))
			completed = true;
	}

	@Override
	public String getTaskDescription() {
		return "QuestTaskTalk Description";
	}

	@Override
	public String getTaskName() {
		return QuestTaskTalk.class.toString();
	}

	@Override
	public void run() { }

	@Override
	public void init() { }


	@Override
	public void unregisterEvents() { 
		synchronized (questManager) {
			QuestEventHandler.unregisterQuestEventCheckComplete(this);
		}
	}

	@Override
	public void registerEvents() {
		synchronized (questManager) {
			QuestEventHandler.registerQuestEventCheckComplete(this);
		}
	}

	@Override
	public void onCheckCompleteEvent() {
		CheckComplete();
	}
}
