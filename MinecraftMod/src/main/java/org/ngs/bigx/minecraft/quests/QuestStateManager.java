package org.ngs.bigx.minecraft.quests;

public class QuestStateManager 
{
	public QuestStateManager()
	{	
	}
	
	private enum State {
        Dead, Idle, QuestLoading, WaitToBeReady, QuestInProgress, QuestAccomplished, QuestFailed, RewardSelection, RetryOrEndTheQuest

    }
    private enum Trigger {
        Die, Revive, AcceptQuest, 

    }
}
