package org.ngs.bigx.minecraft.client.gui.quest.chase;

public class GuiChasingQuestLevelSlotItem {
	public int level = 0;
	public boolean isLocked = true;
	
	public GuiChasingQuestLevelSlotItem(int level, boolean islocked)
	{
		this.level = level;
		this.isLocked = islocked;
	}
}
