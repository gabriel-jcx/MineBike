package org.ngs.bigx.minecraft.gamestate;

public class GameStateQuestProgress {
	public static enum QuestProgressEnum {
		NOTSTARTED, PROGRESSING, ACHIEVED
	}
	
	public String questId;
	public QuestProgressEnum questProgress;
	public int questProgressPercentage;
}
