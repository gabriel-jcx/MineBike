package org.ngs.bigx.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class GuiQuestlistDescriptionSlot extends GuiScrollingList {
	GuiQuestlistManager parent;
	private static int selectedItem = -1;
	
	public GuiQuestlistDescriptionSlot(int width, int height, int top, int left, int entryHeight) {
		super(Minecraft.getMinecraft(), width, height, top, top + height, left, entryHeight);
	}
	
	public void resetScroll() {
		ReflectionHelper.setPrivateValue(GuiScrollingList.class, this, 0F, "scrollDistance");
	}
	
	@Override
	protected void drawBackground() {
	}

	public GuiQuestlistDescriptionSlot(GuiQuestlistManager parent) {
		this(parent.width - 170, parent.height - parent.getTopMargin(), parent.getTopMargin(), 170, 40);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		if(parent.getSelectedQuestlistIndex() == -1)
		{
			return 0;
		}
		else
		{
			return 3 + parent.getSelectedQuest().getRequirements().length;
		}
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		String text = "";
		switch(i)
		{
		case 0:
			text = "[Quest] " + parent.getSelectedQuest().getName();
			break;
		case 1:
			text = "[Story] " + parent.getSelectedQuest().getDescription();
			break;
		default:
			if( (i-2) < parent.getSelectedQuest().getRequirements().length)
				text = "[Requirement " + (i-1) + "] " + parent.getSelectedQuest().getRequirements()[i-2];
			break;
		};
		
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		font.drawStringWithShadow(text, j + 20 - listWidth, k + 3, 0xFFFFFF);
	}

	protected void elementClicked(int index, boolean doubleClick) {
		selectedItem = index;
	}

	@Override
	protected boolean isSelected(int index) {
		return selectedItem == index;
	}
	
}
