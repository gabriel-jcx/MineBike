package org.ngs.bigx.minecraft.client.gui.quest.chase;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistManager;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class GuiChasingQuestLevelSlot extends GuiScrollingList {
	GuiChasingQuest parent;
	private int slotHeight;
	private int slotWidth;
	public static int numberOfQuestLevels = 10;
	
	public GuiChasingQuestLevelSlot(int width, int height, int top, int left, int entryHeight) {
		super(Minecraft.getMinecraft(), width, height, top, top + height, left, entryHeight);
		this.slotHeight = height;
		this.slotWidth = width;
	}
	
	public void resetScroll() {
		ReflectionHelper.setPrivateValue(GuiScrollingList.class, this, 0F, "scrollDistance");
	}
	
	@Override
	protected void drawBackground() {
	}

	public GuiChasingQuestLevelSlot(GuiChasingQuest parent) {
		this(150, ((parent.height - parent.getTopMargin())>=171)?171:(parent.height - parent.getTopMargin()), parent.getTopMargin(), 0, 17);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return parent.getChasingQuestLevelListSize();
	}

	@Override
	protected void elementClicked(int i, boolean doubleclick) {
		parent.selectQuestlevel(i);
	}

	@Override
	protected boolean isSelected(int i) {
		return parent.getSelectedQuestLevelIndex() == i;
	}

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
	{
		String name = "";
		
		if(i < QuestTaskChasing.villainNames.length)
			name = QuestTaskChasing.villainNames[i];
		else
			name = "Random Thief";
		
		int fontColor = 0xFFFFFF;
		
		if(parent.getSelectedQuestLevelList(i).isLocked)
		{
			name += " (Locked)";
			fontColor = 0xFF4C3B;
		}
		
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		
		font.drawStringWithShadow(name, j + 20 - listWidth, k + 3, fontColor);
	}
	
	public int getHeight()
	{
		return this.slotHeight;
	}
	
	public int getWidth()
	{
		return this.slotWidth;
	}
}
