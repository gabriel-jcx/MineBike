package org.ngs.bigx.minecraft.client.gui.quest.chase;

import org.ngs.bigx.minecraft.quests.QuestEventHandler;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventRewardSession.eQuestEventRewardSessionType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
		
//		drawRect(0, 0, width, 30, 0xCC0072BB); // The Box on top
		
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

		Minecraft.getMinecraft().thePlayer.closeScreen();
	}

    @Override
    public void keyTyped(char c, int i) {
    	
    	super.keyTyped(c, i);
    }
}
