package org.ngs.bigx.minecraft.client.gui.quest.chase;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.quests.QuestEventHandler;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventRewardSession.eQuestEventRewardSessionType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class GuiFinishChasingQuest extends GuiScreen {
	private boolean isContinueVisible = false; 
	
	// Need context in order for the GUI to function properly - keep empty constructor protected
	public GuiFinishChasingQuest(boolean isContinueVisible) {
		super();
		this.isContinueVisible = isContinueVisible;
	}
	
	@Override
	public void initGui() {
		GuiButton continueButton, retryButton, exitButton;
		
		int buttonWidth = 150;
		int buttonHeight = 20;
		
		continueButton = new GuiButton(1,
				(width-buttonWidth)/2, (height-buttonHeight)/2 - 40,
				buttonWidth, buttonHeight,
				I18n.format("gui.finishchasingquest.continue"));
		continueButton.visible = isContinueVisible;
		
		retryButton = new GuiButton(2,
				(width-buttonWidth)/2, (height-buttonHeight)/2,
				buttonWidth, buttonHeight,
				I18n.format("gui.finishchasingquest.retry"));
		
		exitButton = new GuiButton(3,
				(width-buttonWidth)/2, (height-buttonHeight)/2 + 40,
				buttonWidth, buttonHeight,
				I18n.format("gui.finishchasingquest.exit"));
		
		this.buttonList.add(continueButton);
		this.buttonList.add(retryButton);
		this.buttonList.add(exitButton);
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();

	    ScaledResolution sr = new ScaledResolution(mc);
		int mcWidth = sr.getScaledWidth();
    	int mcHeight = sr.getScaledHeight();

		drawRect(0, 0, mcWidth, mcHeight, 0xCC000000); // Background
		
		super.drawScreen(mx, my, partialTicks);
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id) {
		case 1:
			QuestEventHandler.onQuestEventRewardSessionButtonClicked(eQuestEventRewardSessionType.CONTINUE);
			break;
		case 2:
			QuestEventHandler.onQuestEventRewardSessionButtonClicked(eQuestEventRewardSessionType.RETRY);
			break;
		case 3:
			QuestEventHandler.onQuestEventRewardSessionButtonClicked(eQuestEventRewardSessionType.EXIT);
			break;
		default:
			System.out.println("Clicked Unimplemented Button");
			break;
		}

        if(mc.currentScreen != null) {
        	Minecraft.getMinecraft().player.closeScreen();
        }
	}

    @Override
    public void keyTyped(char c, int i) {
    	
    	super.keyTyped(c, i);
    }
}
