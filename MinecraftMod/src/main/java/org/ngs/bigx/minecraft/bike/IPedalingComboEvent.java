package org.ngs.bigx.minecraft.bike;

public interface IPedalingComboEvent {
	public void onLevelChange(int oldLevel, int newLevel);
	public void onOneGaugeFilled();
}
