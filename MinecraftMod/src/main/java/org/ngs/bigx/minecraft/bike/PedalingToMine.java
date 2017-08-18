package org.ngs.bigx.minecraft.bike;

public class PedalingToMine implements IPedalingComboEvent {

	@Override
	public void onLevelChange(int oldLevel, int newLevel) {
		if(oldLevel > newLevel)
		{
			// Level Decrease
		}
		else
		{
			// Level Increase
		}
	}

	@Override
	public void onOneGaugeFilled() {
		
	}

}
