package org.ngs.bigx.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class GuiQuestlistSlot extends GuiScrollingList {
	GuiQuestlistManager parent;
	
	public GuiQuestlistSlot(int width, int height, int top, int left, int entryHeight) {
		super(Minecraft.getMinecraft(), width, height, top, top + height, left, entryHeight);
	}
	
	public void resetScroll() {
		ReflectionHelper.setPrivateValue(GuiScrollingList.class, this, 0F, "scrollDistance");
	}
	
	@Override
	protected void drawBackground() {
	}

	public GuiQuestlistSlot(GuiQuestlistManager parent) {
		this(197, parent.height - parent.getTopMargin(), parent.getTopMargin(), 0, 30);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return parent.getQuestReferenceListSize();
	}

	@Override
	protected void elementClicked(int i, boolean doubleclick) {
		parent.selectQuestlist(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return parent.getSelectedQuestlistIndex() == i;
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		String name = parent.getSelectedQuestlist(i).getName();
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		font.drawStringWithShadow(name, j + 20 - listWidth, k + 3, 0xFFFFFF);
	}
	
}