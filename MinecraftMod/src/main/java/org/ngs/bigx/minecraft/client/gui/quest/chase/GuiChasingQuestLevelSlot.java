package org.ngs.bigx.minecraft.client.gui.quest.chase;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistManager;
import org.ngs.bigx.minecraft.quests.QuestException;

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
		
		switch(i)
		{
		case 0:
			name = "Gold Thief";
			break;
		case 1:
			name = "Element Thief";
			break;
		case 2:
			name = "Key Thief";
			break;
		case 3:
			name = "Thief Master";
			break;
		case 4:
			name = "Thief King";
			break;
		case 5:
			name = "Creeper";
			break;
		case 6:
			name = "Spider";
			break;
		case 7:
			name = "Zombie";
			break;
		case 8:
			name = "Zombie Pigman";
			break;
		case 9:
			name = "Ender Man";
			break;
		default:
			name = "Random Thief";
			break;
		}
		
		
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
