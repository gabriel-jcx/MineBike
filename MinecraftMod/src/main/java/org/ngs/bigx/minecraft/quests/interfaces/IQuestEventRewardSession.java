package org.ngs.bigx.minecraft.quests.interfaces;

public interface IQuestEventRewardSession {
	public static enum eQuestEventRewardSessionType
	{
		CONTINUE,
		RETRY,
		EXIT
	};
	
	public void onRewardSessionContinueClicked();
	public void onRewardSessionRetryClicked();
	public void onRewardSessionExitClicked();
}
