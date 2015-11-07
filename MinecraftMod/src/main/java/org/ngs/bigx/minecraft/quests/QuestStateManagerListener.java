package org.ngs.bigx.minecraft.quests;

import java.util.EventListener;

public interface QuestStateManagerListener extends EventListener {
	public void onQuestInactive();
	public void onQuestLoading();
	public void onQuestWaitToStart();
	public void onQuestInProgress();
	public void onQuestPaused();
	public void onQuestAccomplished();
	public void onQuestFailed();
	public void onRewardSelection();
	public void onRetryOrEndTheQuest();
}
