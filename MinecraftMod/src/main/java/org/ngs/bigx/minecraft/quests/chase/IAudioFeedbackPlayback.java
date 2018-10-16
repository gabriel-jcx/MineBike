package org.ngs.bigx.minecraft.quests.chase;

public interface IAudioFeedbackPlayback {
	public enum AudioFeedBackEnum {
		REGULAR(0), AHEAD(1), NOMOVE(2), NOHIT(3), NOTCLOSEENOUGH(4), GOODJOB(5), END(6);
		
		private final int value;
		private AudioFeedBackEnum(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static AudioFeedBackEnum fromInt(int i) {
			for (AudioFeedBackEnum b : AudioFeedBackEnum.values()) {
	            if (b.getValue() == i) { return b; }
	        }
	        return null;
		}
		
		public static int getSize()
		{
			return END.getValue();
		}
	};
	public void playAudioFeedback(AudioFeedBackEnum audioFeedBackEnum);
}
