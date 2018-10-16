package org.ngs.bigx.minecraft.quests.chase;

import java.util.ArrayList;

import org.ngs.bigx.minecraft.quests.chase.IAudioFeedbackPlayback.AudioFeedBackEnum;

public class AudioFeedback {
	private IAudioFeedbackPlayback audioFeedbackPlayback;
	private AudioFeedBackEnum audioFeedBackEnum = AudioFeedBackEnum.REGULAR;
	//REGULAR(0), AHEAD(1), NOMOVE(2), NOHIT(3), NOTCLOSEENOUGH(4), GOODJOB(5), END(6);
	
	public static final String[][][] audioFeedbackLib = {
		{{"cheering_1", "C'mon you can make it!"}, {"cheering_2", "So close!"}, {"cheering_3", "Focus!"}},
		{{"behindyou_1", "He is behind you"}, {"behindyou_2", "He is behind you"}, {"behindyou_3", "He is behind you"}},
		{{ "goaroundit_1", "Oh. Go around it"}, { "goaroundit_2", "Go around it"}, { "goaroundit_3", "Jump over it"}, { "goaroundit_4", "Jump over it"}},
		{{ "aimathim_1", "Aim at him!"}, { "aimathim_2", "Aim at him!"}, { "hithim_1", "Hit him!"}},
		{{ "getcloser_1", "Get closer!"}, { "getcloser_2", "Get closer!"}, { "getcloser_3", "Move!"}, { "getcloser_4", "Move move move!"}, { "getcloser_5", "Move!"}, {"focus_1", "Focus!"}},
		{{ "amazing_1", "Amazing!"}, {"fantastic_1", "Fantastic!"}, {"fantastic_2", "Fantastic!"}, {"goodjob_1", "Good job!"}, {"goodjob_2", "Good job!"}, {"greatjob_1", "Great job!"}, {"yeh_1", "Yeh!"}}
	};
	
	private int[] audioFeedbackFlags = new int[AudioFeedBackEnum.getSize()];
	private long[] audioFeedbackFlagTimeStamp = new  long[AudioFeedBackEnum.getSize()];
	private static final int[] audioFeedbackScheduledTime = {10000,6000,4000,6000,6000,4000,0};
	
	private static ArrayList<AudioFeedBackEnum> audioFeedbackQueue = new ArrayList();
	
	public AudioFeedback(IAudioFeedbackPlayback iAudioFeedbackPlayback)
	{
		this.audioFeedbackPlayback = iAudioFeedbackPlayback;
		this.audioFeedbackQueue = new ArrayList<IAudioFeedbackPlayback.AudioFeedBackEnum>();
	}
	
	public void updateState(AudioFeedBackEnum audioFeedBackEnum)
	{
		System.out.println("[BiGX] this.audioFeedBackEnum[" + this.audioFeedBackEnum + "] audioFeedBackEnum[" + audioFeedBackEnum + "]");
		
		if(this.audioFeedBackEnum != audioFeedBackEnum)
		{
			initializeAllFlags();
			this.audioFeedBackEnum = audioFeedBackEnum;
			this.audioFeedbackFlagTimeStamp[audioFeedBackEnum.getValue()] = System.currentTimeMillis();
		}
		else
		{
			evalState(audioFeedBackEnum);
		}
	}

	private void evalState(AudioFeedBackEnum audioFeedBackEnum) {
		long getEnlapsedMiliSeconds = (System.currentTimeMillis() - audioFeedbackFlagTimeStamp[audioFeedBackEnum.getValue()]);
		int eventScheduledTime = audioFeedbackScheduledTime[audioFeedBackEnum.getValue()];
		
		if(audioFeedBackEnum == AudioFeedBackEnum.GOODJOB)
		{
			return;
		}
//		System.out.println("getEnlapsedMiliSeconds["+getEnlapsedMiliSeconds+"] eventScheduledTime["+eventScheduledTime+"]");
		if(getEnlapsedMiliSeconds > eventScheduledTime)
		{
			synchronized (audioFeedbackQueue)
			{
				if(enqueueAudioFeedback(audioFeedBackEnum))
				{
					audioFeedbackFlagTimeStamp[audioFeedBackEnum.getValue()] = System.currentTimeMillis();
				}
				else
				{
					this.audioFeedbackPlayback.playAudioFeedback(audioFeedbackQueue.remove(0));
				}
			}
		}
	}

	private boolean enqueueAudioFeedback(AudioFeedBackEnum audioFeedBackEnum) {
		boolean returnValue = false;
		
		if(this.audioFeedbackQueue.isEmpty())
		{
			this.audioFeedbackQueue.add(audioFeedBackEnum);
			returnValue = true;
		}
		
		return returnValue;
	}

	private void initializeAllFlags() {
		for(int i=0; i<audioFeedbackFlags.length; i++)
		{
			this.audioFeedbackFlags[i] = 0;
			this.audioFeedbackFlagTimeStamp[i] = 0;
		}
	}
}
