package org.ngs.bigx.minecraft.client.gui;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.PedalingToBuildEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class GuiBuildinglistSlot extends GuiScrollingList {
	GuiBuildinglistManager parent;
	
	public GuiBuildinglistSlot(int width, int height, int top, int left, int entryHeight) {
		super(Minecraft.getMinecraft(), width, height, top, top + height, left, entryHeight);
	}
	
	public void resetScroll() {
		ReflectionHelper.setPrivateValue(GuiScrollingList.class, this, 0F, "scrollDistance");
	}
	
	@Override
	protected void drawBackground() {
	}

	public GuiBuildinglistSlot(GuiBuildinglistManager parent) {
		this(197, parent.height - parent.getTopMargin(), parent.getTopMargin(), 0, 30);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return parent.getBuildingReferenceListSize();
	}

	@Override
	protected void elementClicked(int i, boolean doubleclick) {
		parent.selectBuildinglist(i);
		
		PedalingToBuildEventHandler.buildingId = parent.getSelectedBuildinglist(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return parent.getSelectedBuildinglistIndex() == i;
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		String name = parent.getSelectedBuildinglist(i);
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		font.drawStringWithShadow(name, j + 20 - listWidth, k + 3, 0xFFFFFF);
	}
	
}
