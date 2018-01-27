package org.ngs.bigx.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;

import com.ibm.icu.impl.ICUService.Key;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiVictory extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;

	private static boolean isKOTimeout = false;

	private String victoryMessageLine1 = "Nice Work!";
	private String victoryMessageLine2 = "You earned ";
	private String victoryMessageLine3 = "Collect them from the floor!";
	
	public GuiVictory(Minecraft mc) {
		super();
		this.mc = mc;
		isKOTimeout = true;
		
		// Play Vicotry Sound
		Minecraft.getMinecraft().thePlayer.playSound("minebike:victory", 1.5f, 1.0f);
	}
	
	public GuiVictory(BigxClientContext c, Minecraft mc) {
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
				isKOTimeout = false;
			}
		}, 2*1000);
		
		// Start Timer for close screen
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mc.currentScreen instanceof GuiVictory)
				{
					Minecraft.getMinecraft().thePlayer.closeScreen();
					isKOTimeout = false;
				}
			}
		}, 3*1000);
	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();
	};
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
		int mcWidth = sr.getScaledWidth();
    	int mcHeight = sr.getScaledHeight();

		drawRect(0, 0, mcWidth, mcHeight, 0xCC000000); // The Box on top
		
		String text = "";
		
		if(isKOTimeout)
		{
			
			// SHOW K.O
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
		    	GL11.glScalef(3F, 3F, 3F);
		    	
		    	text = "K.O";
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 30, 0xFF8888);
    		
    		GL11.glPopMatrix();
		}
		else
		{
			// SHOW K.O
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
		    	GL11.glScalef(2F, 2F, 2F);
		    	
		    	text = victoryMessageLine1;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 30, 0xFFFFFF);
		    	
		    	text = victoryMessageLine2;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 20, 0xFFFFFF);
		    	
		    	text = victoryMessageLine2;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 10, 0xFFFFFF);
    		
    		GL11.glPopMatrix();
		}
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}
}
