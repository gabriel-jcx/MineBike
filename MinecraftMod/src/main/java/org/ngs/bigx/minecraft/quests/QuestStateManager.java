package org.ngs.bigx.minecraft.quests;

import com.github.oxo42.stateless4j.StateMachine;

public class QuestStateManager 
{
	private StateMachine<State, Trigger> QuestStateMachine;
	
	public QuestStateManager() throws Exception
	{
		this.QuestStateMachine = new StateMachine<State, Trigger>(State.Inactive);
		
		this.QuestStateMachine.configure(State.Inactive)
			.permit(Trigger.AcceptQuestAndTeleport, State.QuestLoading);
		
		this.QuestStateMachine.configure(State.QuestLoading)
			.permit(Trigger.TeleportDone, State.WaitToStart);
		
		this.QuestStateMachine.configure(State.WaitToStart)
			.permit(Trigger.StartQuest, State.QuestInProgress);
		
		this.QuestStateMachine.configure(State.QuestInProgress)
			.permit(Trigger.PauseQuest, State.QuestPaused)
			.permit(Trigger.SuccessQuest, State.QuestAccomplished)
			.permit(Trigger.FailureQuest, State.QuestFailed);
		
		this.QuestStateMachine.configure(State.QuestPaused)
			.permit(Trigger.ResumeQuest, State.QuestInProgress);
		
		this.QuestStateMachine.configure(State.QuestAccomplished)
			.permit(Trigger.ScoreDisplayClickOkay, State.RewardSelection);
		
		this.QuestStateMachine.configure(State.RewardSelection)
			.permit(Trigger.RewardSelect, State.RetryOrEndTheQuest);
		
		this.QuestStateMachine.configure(State.QuestFailed)
			.permit(Trigger.ScoreDisplayClickOkay, State.RetryOrEndTheQuest);
		
		this.QuestStateMachine.configure(State.RetryOrEndTheQuest)
			.permit(Trigger.NextgameRetryClick, State.QuestLoading)
			.permit(Trigger.NextgameExitQuestClick, State.Inactive);
	}
	
	private enum State {
        //Dead, Idle,
		Inactive, 
        QuestLoading, WaitToStart, QuestInProgress, 
        QuestPaused, QuestAccomplished, QuestFailed, RewardSelection, 
        RetryOrEndTheQuest

    }
    private enum Trigger {
        //Die, Revive,
    	AcceptQuestAndTeleport, TeleportDone, StartQuest, 
        PauseQuest, StopQuest, ResumeQuest, SuccessQuest, FailureQuest, RewardSelect,
        ScoreDisplayClickOkay, NextgameRetryClick, NextgameExitQuestClick
    }
}
