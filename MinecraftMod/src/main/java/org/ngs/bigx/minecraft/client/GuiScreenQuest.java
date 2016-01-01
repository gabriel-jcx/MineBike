package org.ngs.bigx.minecraft.client;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.networking.ReceiveQuestMessage;
import org.ngs.bigx.minecraft.networking.UpdateQuestMessage;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiScreenQuest extends GuiScreen {
	
	private Quest quest;
	private final int ButtonAccept = 0;
	private ResourceLocation QUEST_TEXTURE = new ResourceLocation(Main.TEXTURE_PREFIX,"textures/GUI/quest.png");
	private Context context;
	
	public GuiScreenQuest(EntityPlayer p_i1080_1_,Quest quest,Context context) {
		this.quest = quest;
		this.context = context;
	}
	
	@Override
	public void initGui()
    {
		buttonList.add(new GuiButton(ButtonAccept, width/2-50, height/2+height/4, 100, 20, "Accept"));
    }
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		if (button.id==ButtonAccept) {
			context.questManager.setQuest(context.questManager.getSuggestedQuest());
			context.questManager.setSuggestedQuest(null);
			this.mc.displayGuiScreen((GuiScreen)null);
			quest.triggerStateChange(Trigger.AcceptQuestAndTeleport);
			UpdateQuestMessage packet = new UpdateQuestMessage(quest,Trigger.AcceptQuestAndTeleport);
			Main.network.sendToServer(packet);
		}
    }
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
		int xPos = width/2;
		int yPos = height/2;
		for (Object button:buttonList) {
			((GuiButton) button).yPosition = yPos+50;
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	mc.renderEngine.bindTexture(QUEST_TEXTURE);
        drawTexturedModalRect(xPos-175/2, yPos-165/2, 0, 0, 175 , 165);
        String text = quest.getName();
		fontRendererObj.drawString(text, xPos-fontRendererObj.getStringWidth(text)/2, yPos-50, 0);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
