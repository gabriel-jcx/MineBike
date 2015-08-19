package org.ngs.bigx.minecraft.client;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.quests.Quest;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class GuiScreenQuest extends GuiScreen {
	
	private Quest quest;
	private final int ButtonAccept = 0;
	
	public GuiScreenQuest(EntityPlayer p_i1080_1_,Quest q) {
		quest = q;
	}
	
	@Override
	public void initGui()
    {
		buttonList.add(new GuiButton(ButtonAccept, width/2-100, height-100, "Accept"));
    }
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		if (button.id==ButtonAccept) {
			this.mc.displayGuiScreen((GuiScreen)null);
			Main.instance().context.setQuest(quest);
		}
    }
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
		fontRendererObj.drawString(quest.getName(), this.width/2-50, 4, 0xFFFFFF);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
