package org.ngs.bigx.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.SetChangeListener;

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
import noppes.npcs.client.ClientEventHandler;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiChapter extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;
	
	private static int chapterNumber = 1;
	private static String chapterText = "";
	private static String title = "";
	private static String subtitleLine1 = "";
	private static String subtitleLine2 = "";
	private static String subtitleLineShort = "";
	private static String strUnlocked = "UNLOCKED NEW CHAPTER";

	private boolean flagUnlocked = false;
	private static boolean flagProceedToNextChapter = false;
	
	public static final String STR_CHAPTER_1_TITLE = "WAKE UP!";
	public static final String STR_CHAPTER_1_SUBTITLE_LINE_1 = "Go talk to father";
	public static final String STR_CHAPTER_1_SUBTITLE_LINE_2 = "";
	public static final String STR_CHAPTER_1_SUBTITLE_LINE_SHORT = "Go talk to father";
	
	public static final String STR_CHAPTER_2_TITLE = "NOT READY YET!";
	public static final String STR_CHAPTER_2_SUBTITLE_LINE_1 = "Go talk to a trainer home";
	public static final String STR_CHAPTER_2_SUBTITLE_LINE_2 = "";
	public static final String STR_CHAPTER_2_SUBTITLE_LINE_SHORT = "Go talk to a trainer";
	
	public static final String STR_CHAPTER_3_TITLE = "CATCH DIAMOND THIEF";
	public static final String STR_CHAPTER_3_SUBTITLE_LINE_1 = "Go talk to a police officer";
	public static final String STR_CHAPTER_3_SUBTITLE_LINE_2 = "at police station";
	public static final String STR_CHAPTER_3_SUBTITLE_LINE_SHORT = "Go talk to a police officer and defeat \"Gold Thief\"";
	
	public static final String STR_CHAPTER_4_TITLE = "THE JOURNEY STARTS";
	public static final String STR_CHAPTER_4_SUBTITLE_LINE_1 = "Defeat the Thief Boss from the cave";
	public static final String STR_CHAPTER_4_SUBTITLE_LINE_2 = "located on top of the mountain!";
	public static final String STR_CHAPTER_4_SUBTITLE_LINE_SHORT = "Defeat the Thief Boss";
	
	public static boolean didOneSecondPassed = false;

	public static void proceedToNextChapter()
	{
		System.out.println("[BiGX] Next Chapter Initiated.");
		chapterNumber ++;
		GuiChapter.setChapter(chapterNumber);
		flagProceedToNextChapter = true;
	}
	
	public static boolean isFlagProceedToNextChapter() {
		return flagProceedToNextChapter;
	}

	public static void unsetFlagProceedToNextChapter()
	{
		flagProceedToNextChapter = false;
	}
	
	public GuiChapter(Minecraft mc, int chapterNumber, boolean flagUnlocked) {
		super();
		didOneSecondPassed = false;
		this.mc = mc;
		setChapter(this.chapterNumber);
		this.flagUnlocked = flagUnlocked;

		if(GuiChapter.isFlagProceedToNextChapter())
		{
			GuiChapter.unsetFlagProceedToNextChapter();
			
			// Play Sound
			Minecraft.getMinecraft().thePlayer.playSound("minebike:victory", 1.5f, 1.0f);
		}
	}
	
	public GuiChapter(BigxClientContext c, Minecraft mc, int chapterNumber, boolean flagUnlocked) {
		this(mc, chapterNumber, flagUnlocked);
		context = c;
	}
	
	public static int getChapterNumber() {
		return chapterNumber;
	}

	public static String getCurrentChapterSubtitleShort()
	{
		if(!BigxClientContext.getIsGameSaveRead())
		{
			return null;
		}
		
		GuiChapter.setChapter(chapterNumber);
		
		return subtitleLineShort;
	}
	
	public static void setChapter(int chapterNumber)
	{
		GuiChapter.chapterNumber = chapterNumber;
		
		GuiChapter.setTitleAndSubTitles(chapterNumber);
	}
	
	private static void setTitleAndSubTitles(int chapterNumber)
	{	
		switch(chapterNumber)
		{
		case 1:
			chapterText = "~ Chapter 1 ~";
			title = STR_CHAPTER_1_TITLE;
			subtitleLine1 = STR_CHAPTER_1_SUBTITLE_LINE_1;
			subtitleLine2 = STR_CHAPTER_1_SUBTITLE_LINE_2;
			subtitleLineShort = STR_CHAPTER_1_SUBTITLE_LINE_SHORT;
			break;
		case 2:
			chapterText = "~~";
			title = STR_CHAPTER_2_TITLE;
			subtitleLine1 = STR_CHAPTER_2_SUBTITLE_LINE_1;
			subtitleLine2 = STR_CHAPTER_2_SUBTITLE_LINE_2;
			subtitleLineShort = STR_CHAPTER_2_SUBTITLE_LINE_SHORT;
			break;
		case 3:
			chapterText = "~~";
			title = STR_CHAPTER_3_TITLE;
			subtitleLine1 = STR_CHAPTER_3_SUBTITLE_LINE_1;
			subtitleLine2 = STR_CHAPTER_3_SUBTITLE_LINE_2;
			subtitleLineShort = STR_CHAPTER_3_SUBTITLE_LINE_SHORT;
			break;
		case 4:
			chapterText = "~ Chapter 2 ~";
			title = STR_CHAPTER_4_TITLE;
			subtitleLine1 = STR_CHAPTER_4_SUBTITLE_LINE_1;
			subtitleLine2 = STR_CHAPTER_4_SUBTITLE_LINE_2;
			subtitleLineShort = STR_CHAPTER_4_SUBTITLE_LINE_SHORT;
			break;
		default:
			break;
		}
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
				if(mc.currentScreen instanceof GuiChapter)
				{
					didOneSecondPassed = true;
					
					// Play Sound
					Minecraft.getMinecraft().thePlayer.playSound("minebike:chaptertada", 1.0f, 1.0f);
				}
			}
		}, 1*1000);
		
		// Start Timer for close screen
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mc.currentScreen instanceof GuiChapter)
				{
					didOneSecondPassed = false;
					Minecraft.getMinecraft().thePlayer.closeScreen();
				}
			}
		}, 4*1000);
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
		
		GL11.glPushMatrix();
		    GL11.glTranslatef(mcWidth/2, 0, 0);
	    	GL11.glScalef(2F, 2F, 2F);
		    
		    if(flagUnlocked)
		    {
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(strUnlocked, -1 * fontRendererObj.getStringWidth(strUnlocked)/2, mcHeight/4 - 40, 0xFF3333);
		    }
	
        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
    		fontRendererObj.drawString(chapterText, -1 * fontRendererObj.getStringWidth(chapterText)/2, mcHeight/4 - 30, 0xFFFFFF);

        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
    		fontRendererObj.drawString(title, -1 * fontRendererObj.getStringWidth(title)/2, mcHeight/4 - 20, 0xFFFFFF);
    		
    		if(didOneSecondPassed)
    		{
    			fontRendererObj = Minecraft.getMinecraft().fontRenderer;
        		fontRendererObj.drawString(subtitleLine1, -1 * fontRendererObj.getStringWidth(subtitleLine1)/2, mcHeight/4, 0xCCCCCC);

            	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
        		fontRendererObj.drawString(subtitleLine2, -1 * fontRendererObj.getStringWidth(subtitleLine2)/2, mcHeight/4 + 10, 0xCCCCCC);
    		}
		GL11.glPopMatrix();
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}
}
