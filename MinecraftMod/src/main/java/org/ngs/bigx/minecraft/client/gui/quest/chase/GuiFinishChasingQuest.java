package org.ngs.bigx.minecraft.client.gui.quest.chase;

import org.ngs.bigx.minecraft.quests.QuestTaskChasing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFinishChasingQuest extends GuiScreen {
	private QuestTaskChasing questTask;
	
	// Need context in order for the GUI to function properly - keep empty constructor protected
	protected GuiFinishChasingQuest() {
		super();
	}
	
	public GuiFinishChasingQuest(QuestTaskChasing task) {
		this();
		questTask = task;
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
		// TODO replace with enums?
		switch(button.id) {
		case 1:
			questTask.endGuiChoice = 1;
			// questTask.resumeTask();
			break;
		case 2:
			questTask.endGuiChoice = 2;
			// questTask.restartTask();
			break;
		case 3:
			questTask.endGuiChoice = 3;
//			 questTask.waitForRewardPickupAndContinue(); -- but public version for this GUI
			break;
		default:
			System.out.println("Clicked Unimplemented Button");
			break;
		}
	}

    @Override
    public void keyTyped(char c, int i) {
    	
    	super.keyTyped(c, i);
    }
}
