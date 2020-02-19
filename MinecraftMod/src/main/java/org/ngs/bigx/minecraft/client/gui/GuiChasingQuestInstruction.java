package org.ngs.bigx.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;

import com.ibm.icu.impl.ICUService.Key;

//import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiChasingQuestInstruction extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;

	private static boolean isFindThiefTimeout = false;
	private static boolean isStayCloseTimeout = false;
	private static boolean isAimTimeout = false;
	private static boolean isAndTimeout = false;
	private static boolean isHitTimeout = false;

	private ResourceLocation CHASING_QUEST_INSTRUCTION_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/chasinginstruction.png");
	
	private static Object guiChasingQuestInstructionLock = new Object();
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	public GuiChasingQuestInstruction(Minecraft mc) {
		super();
		this.mc = mc;
//		isKOTimeout = true;
//		isVictoryMsgTimeout = true;
		isFindThiefTimeout = true;
		isAimTimeout = true;
		isAndTimeout = true;
		isHitTimeout = true;
		isStayCloseTimeout = true;
		
		// TODO Play bgm_chasingquest_instruction
//		Minecraft.getMinecraft().player.playSound("minebike:bgm_chasingquest_instruction", 1.0f, 1.0f);
//		System.out.println("Play sound");
		
	}
	
	public GuiChasingQuestInstruction(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
		
		// Start Timer for close screen
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isFindThiefTimeout = false;
			}
		}, 1500);
        
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isStayCloseTimeout = false;
			}
		}, 3000);
        
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isAimTimeout = false;
			}
		}, 3500);
        
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isAndTimeout = false;
			}
		}, 4000);
		
		// Start Timer for close screen
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (guiChasingQuestInstructionLock) {
					if(mc.currentScreen == null) {
						return;
					}
					else if(mc.currentScreen instanceof GuiChasingQuestInstruction)
					{
						Minecraft.getMinecraft().player.closeScreen();
//						isKOTimeout = false;
//						isVictoryMsgTimeout = false;
						isFindThiefTimeout = false;
						isAimTimeout = false;
						isAndTimeout = false;
						isHitTimeout = false;
						isStayCloseTimeout = false;
					}
				}
			}
		}, 5000);
	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();
	};
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
	    ScaledResolution sr = new ScaledResolution(mc);
		int mcWidth = sr.getScaledWidth();
    	int mcHeight = sr.getScaledHeight();

		drawRect(0, 0, mcWidth, mcHeight, 0xCC000000); // The Box on top
		
		int combotextColor = 0xFFFFFF;
		String text = "";
		
		if(isFindThiefTimeout)
		{
			// SHOW Stay Close
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
			    GL11.glPushMatrix();
			    	GL11.glScalef(2F, 2F, 2F);
			    	
			    	text = "Look around. And find the thief";
			
		        	fontRenderer = Minecraft.getMinecraft().fontRenderer;
//		        	(text, 5, mcHeight/4 - 20, combotextColor);
		    		fontRenderer.drawString(text, -1 * fontRenderer.getStringWidth(text)/2, mcHeight/4 - 45, combotextColor);

		    		mc.renderEngine.bindTexture(CHASING_QUEST_INSTRUCTION_TEXTURE);
			        drawTexturedModalRect(-27, mcHeight/4 - 27, 0, 100, 54 , 54);
	    		GL11.glPopMatrix();
	    		
    		GL11.glPopMatrix();
		}
		else if(isStayCloseTimeout)
		{
			// SHOW Stay Close
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
			    GL11.glPushMatrix();
			    	GL11.glScalef(2F, 2F, 2F);
			    	
			    	text = "Stay Close";
			
		        	fontRenderer = Minecraft.getMinecraft().fontRenderer;
//		        	(text, 5, mcHeight/4 - 20, combotextColor);
		    		fontRenderer.drawString(text, -1 * fontRenderer.getStringWidth(text)/2, mcHeight/4 - 45, combotextColor);

		    		mc.renderEngine.bindTexture(CHASING_QUEST_INSTRUCTION_TEXTURE);
			        drawTexturedModalRect(-50, mcHeight/4 - 25, 0, 0, 100 , 50);
	    		GL11.glPopMatrix();
	    		
    		GL11.glPopMatrix();
		}
		else
		{
			// SHOW Aim
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
			    GL11.glPushMatrix();
			    	GL11.glScalef(2F, 2F, 2F);
				    
			    	text = "Aim & Hit";
			    	
			    	if(isAimTimeout)
			    	{
			    		text = "Aim";
			    	}
			    	else if(isAndTimeout)
			    	{
				    	text = "Aim &";
			    	}
			
		        	fontRenderer = Minecraft.getMinecraft().fontRenderer;
//		    		fontRenderer.drawString(text, -1 * fontRenderer.getStringWidth(text)/2, mcHeight/4 - 40, 0xFFFFFF);
		    		fontRenderer.drawString(text, -1 * fontRenderer.getStringWidth(text)/2, mcHeight/4 - 45, combotextColor);
	    		
		    		mc.renderEngine.bindTexture(CHASING_QUEST_INSTRUCTION_TEXTURE);
			        drawTexturedModalRect(-50, mcHeight/4 - 25, 0, 50, 100 , 50);
	    		GL11.glPopMatrix();
    		GL11.glPopMatrix();
		}
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}
}
