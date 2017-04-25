package org.ngs.bigx.minecraft.gamestate;

public class GameSaveQuestProgress {
	public static enum QuestProgressEnum {
		NOTSTARTED, PROGRESSING, ACHIEVED
	}
	
	public String questId;
	public QuestProgressEnum questProgress;
	public int questProgressPercentage;
}
