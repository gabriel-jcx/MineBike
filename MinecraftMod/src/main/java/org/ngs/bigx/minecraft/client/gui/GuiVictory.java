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

public class GuiVictory extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;

	private static boolean isKOTimeout = false;
	private static boolean isVictoryMsgTimeout = false;

	private String victoryMessageLine1 = "VICTORY!";
	private String victoryMessageLineExp = "";
	private String victoryMessageLineGold = "";
	private String victoryMessageLineExtraGold = "";
	private String victoryMessageLineSpecialItem = "";
	private String victoryMessageLine2 = "Pick up the items!";

	private String hintMessageLineTitle = "HINT!";
	private String hintMessageLine1 = "Exchanged gold bars for useful items at Market Place";
	private String hintMessageLine2 = "Spend your gold wisely!";

	private ResourceLocation HINT_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/marketplacehint.png");
	
	private static Object guiVictoryLock = new Object();
	
	public GuiVictory(Minecraft mc) {
		super();
		this.mc = mc;
		isKOTimeout = true;
		isVictoryMsgTimeout = true;
		
		// Play Vicotry Sound
		Minecraft.getMinecraft().player.playSound("minebike:victorywinfcquest", 1.5f, 1.0f);
	}
	
	public GuiVictory(BigxClientContext c, Minecraft mc, int exp, int gold, int extragold, String specialItem) {
		this(mc);
		context = c;
		this.victoryMessageLineGold = "Gold: " + gold;
		this.victoryMessageLineExtraGold = "Extra Gold (by Combo): " + extragold;
		this.victoryMessageLineExp = "EXP: " + exp;
		this.victoryMessageLineSpecialItem = "Item: " + specialItem;
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
        
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				isVictoryMsgTimeout = false;
				// TODO
	    		// Play Sound
				Minecraft.getMinecraft().player.playSound("minebike:buyweaponandgetstronger", 1.0f, 1.0f);
			}
		}, 5*1000);
		
		// Start Timer for close screen
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (guiVictoryLock) {
					if(mc.currentScreen == null) {
						return;
					}
					else if(mc.currentScreen instanceof GuiVictory)
					{
						Minecraft.getMinecraft().player.closeScreen();
						isKOTimeout = false;
						isVictoryMsgTimeout = false;
					}
				}
			}
		}, 9*1000);
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
		    	GL11.glScalef(4F, 4F, 3F);
		    	
		    	text = "K.O";
		    	
		    	float fdx = (new Random()).nextFloat();
		    	float fdy = (new Random()).nextFloat();
		    	
		    	int dx = (fdx>0.80f)?1:(fdx>0.20)?0:-1;
		    	int dy = (fdy>0.80f)?1:(fdy>0.20)?0:-1;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2 + dx, mcHeight/4 - 30 + dy, 0xFF8888);
    		
    		GL11.glPopMatrix();
		}
		else if(isVictoryMsgTimeout)
		{
			// SHOW VICOTRY
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
				GL11.glPushMatrix();
			    	GL11.glScalef(2F, 2F, 2F);
			    	
			    	text = victoryMessageLine1;
			
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 40, 0xFFFFFF);
	    		GL11.glPopMatrix();
		    	
		    	text = victoryMessageLineExp;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 - 20, 0xFFFFFF);
		    	
		    	text = victoryMessageLineGold;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 - 10, 0xFFFFFF);
		    	
		    	text = victoryMessageLineExtraGold;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2, 0xFFFFFF);
		    	
	    		if(!victoryMessageLineSpecialItem.equals(""))
		    	{
	    			text = victoryMessageLineSpecialItem;
		
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 10, 0xFFFFFF);
		    	}
		    	
		    	text = victoryMessageLine2;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 20, 0xFFFFFF);
    		
    		GL11.glPopMatrix();
		}
		else
		{
			// SHOW HINT
			GL11.glPushMatrix();
			    GL11.glTranslatef(mcWidth/2, 0, 0);
				GL11.glPushMatrix();
			    	GL11.glScalef(2F, 2F, 2F);
			    	
			    	text = hintMessageLineTitle;
			
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 50, 0xFFFFFF);
	    		GL11.glPopMatrix();
	    		
	    		mc.renderEngine.bindTexture(HINT_TEXTURE);
		        drawTexturedModalRect(-200, mcHeight/2 - 70, 0, 0, 120 , 120);
		        drawTexturedModalRect(-60, mcHeight/2 - 70, 120, 0, 120 , 120);
		        drawTexturedModalRect(80, mcHeight/2 - 70, 0, 120, 120 , 120);
		    	
		    	text = hintMessageLine1;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 60, 0xFFFFFF);
		    	
		    	text = hintMessageLine2;
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 75, 0xFFFFFF);
    		
    		GL11.glPopMatrix();
		}
		
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}
}
