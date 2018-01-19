package org.ngs.bigx.minecraft.gamestate;

public class GameSaveJson {
	// Save Player Level
	// Save Skill Level
	// Save Quest State (is on?)
	private boolean isChasingQuestOn = false;
	private int playerLevel = 1;
	private int skills = 0x0;
	private int boundaryProgress;
	
	public boolean isChasingQuestOn() {
		return isChasingQuestOn;
	}
	public void setChasingQuestOn(boolean isChasingQuestOn) {
		this.isChasingQuestOn = isChasingQuestOn;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public int getSkills() {
		return skills;
	}
	public void setSkills(int skills) {
		this.skills = skills;
	}
	public void setBoundaryProgress(int progress) {
		boundaryProgress = progress;
	}
	public int getBoundaryProgress() {
		return boundaryProgress;
	}
}
