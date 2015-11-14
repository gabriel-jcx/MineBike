package org.ngs.bigx.minecraft.quests;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

public class QuestStateManager 
{
	private StateMachine<State, Trigger> QuestStateMachine;
	private QuestStateManagerListener stateChangeListner;
	
	public QuestStateManager(QuestStateManagerListener argStateChangeListner) throws Exception
	{
		if(argStateChangeListner == null)
			throw new Exception("StateMachine Listener is null!");
		
		this.QuestStateMachine = new StateMachine<State, Trigger>(State.Inactive);
		this.stateChangeListner = argStateChangeListner;
		
		this.QuestStateMachine.configure(State.Inactive)
			.onEntry(new Action() {
                @Override
                public void doIt() {
                	stateChangeListner.onQuestInactive();
                }
            })
            .permit(Trigger.AcceptQuestAndTeleport, State.QuestLoading);
		
		this.QuestStateMachine.configure(State.QuestLoading)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onQuestLoading();
	            }
	        })
			.permit(Trigger.TeleportDone, State.WaitToStart);
		
		this.QuestStateMachine.configure(State.WaitToStart)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onQuestWaitToStart();
	            }
	        })
			.permit(Trigger.StartQuest, State.QuestInProgress);
		
		this.QuestStateMachine.configure(State.QuestInProgress)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onQuestInProgress();
	            }
	        })
			.permit(Trigger.PauseQuest, State.QuestPaused)
			.permit(Trigger.SuccessQuest, State.QuestAccomplished)
			.permit(Trigger.FailureQuest, State.QuestFailed);
		
		this.QuestStateMachine.configure(State.QuestPaused)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onQuestPaused();
	            }
	        })
			.permit(Trigger.ResumeQuest, State.QuestInProgress);
		
		this.QuestStateMachine.configure(State.QuestAccomplished)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onQuestAccomplished();
	            }
	        })
			.permit(Trigger.ScoreDisplayClickOkay, State.RewardSelection);
		
		this.QuestStateMachine.configure(State.RewardSelection)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onRewardSelection();
	            }
	        })
			.permit(Trigger.RewardSelect, State.RetryOrEndTheQuest);
		
		this.QuestStateMachine.configure(State.QuestFailed)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onQuestFailed();
	            }
	        })
			.permit(Trigger.ScoreDisplayClickOkay, State.RetryOrEndTheQuest);
		
		this.QuestStateMachine.configure(State.RetryOrEndTheQuest)
			.onEntry(new Action() {
	            @Override
	            public void doIt() {
	            	stateChangeListner.onRetryOrEndTheQuest();
	            }
	        })
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
