package org.ngs.bigx.minecraft.client.gui;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.quests.QuestException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
		this(167, parent.height - parent.getTopMargin(), parent.getTopMargin(), 0, 30);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return parent.getQuestReferenceListSize();
	}

	@Override
	protected void elementClicked(int i, boolean doubleclick) {
		parent.selectQuestlist(i);
		
		if(doubleclick)
		{
			try {
				BiGX.instance().serverContext.getQuestManager().setActiveQuest(parent.getSelectedQuest().getQuestId());
				BiGX.instance().clientContext.getQuestManager().setActiveQuest(parent.getSelectedQuest().getQuestId());
			} catch (QuestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
