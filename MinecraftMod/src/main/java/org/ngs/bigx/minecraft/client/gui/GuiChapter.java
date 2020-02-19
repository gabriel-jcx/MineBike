package org.ngs.bigx.minecraft.client.gui;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.dictionary.objects.game.BiGXGameTag;
import org.ngs.bigx.dictionary.protocol.Specification;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxContext.LOGTYPE;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

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
	
	private static boolean isTodayWorkoutDone = true;
	private static int targetedLevel = 0;

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
	
	public static final String STR_CHAPTER_3_TITLE = "CATCH THE DIAMOND THIEF";
	public static final String STR_CHAPTER_3_SUBTITLE_LINE_1 = "Go talk to a police officer at the";
	public static final String STR_CHAPTER_3_SUBTITLE_LINE_2 = "police station and defeat the \"Diamond Thief\"";
	public static final String STR_CHAPTER_3_SUBTITLE_LINE_SHORT = "Go talk to a police officer and defeat \"Diamond Thief\"";
	
	public static final String STR_CHAPTER_4_TITLE = "THE JOURNEY STARTS";
	public static final String STR_CHAPTER_4_SUBTITLE_LINE_1 = "Defeat the Monster \"Ifrit\" from the cave";
	public static final String STR_CHAPTER_4_SUBTITLE_LINE_2 = "located on top of the northern mountain!";
	public static final String STR_CHAPTER_4_SUBTITLE_LINE_SHORT = "Go to the cave on the northern mountain. And defeat the Ifrit";
	
	public static final String STR_CHAPTER_N1_TITLE = "HELP POLICE OFFICER";
	public static final String STR_CHAPTER_N1_SUBTITLE_LINE_1 = "Go talk to a police officer at the";
	public static final String STR_CHAPTER_N1_SUBTITLE_LINE_2 = "police station and help to defeat thieves";
	public static final String STR_CHAPTER_N1_SUBTITLE_LINE_SHORT = "Go talk to a police officer and defeat thieves";
	
	public static boolean didOneSecondPassed = false;
	
	private ResourceLocation CHAPTER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/chapterinstruction.png");
	
	public static void sendChapterGameTag(int chapterNumber)
	{
		// SEND GAME TAG - Quest 0x(GAME TAG[0xFF])(questActivityTagEnum [0xF])
		try {			
			int chapterNumberTypeEnum = (0xfff & chapterNumber);
			BiGXGameTag biGXGameTag = new BiGXGameTag();
			biGXGameTag.setTagName("" + (Specification.GameTagType.GAMETAG_ID_CHAPTER_BEGINNING | chapterNumberTypeEnum));
			
			BigxClientContext.sendGameTag(biGXGameTag);
			
			BigxContext.logWriter(LOGTYPE.TAG, "" + Specification.GameTagType.GAMETAG_ID_CHAPTER_BEGINNING + "\t" + chapterNumberTypeEnum + "\t");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (BiGXNetException e) {
			e.printStackTrace();
		} catch (BiGXInternalGamePluginExcpetion e) {
			e.printStackTrace();
		}
	}

	public static void proceedToNextChapter()
	{
		System.out.println("[BiGX] Next Chapter Initiated.");
		chapterNumber ++;
		GuiChapter.setChapter(chapterNumber);
		flagProceedToNextChapter = true;
		
		sendChapterGameTag(chapterNumber);
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
			Minecraft.getMinecraft().player.playSound("minebike:victory", 1.5f, 1.0f);

			// Start Timer for close screen
	        Timer timer = new Timer(true);
	        timer.schedule(new TimerTask() {
				@Override
				public void run() {
		    		// Play Sound
					Minecraft.getMinecraft().player.playSound("minebike:narration_chapter_" + GuiChapter.chapterNumber, 1.0f, 1.0f);
				}
			}, 750);
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
		if( (isTodayWorkoutDone) || (chapterNumber < 4) )
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
		else {
			chapterText = "~~";
			title = STR_CHAPTER_N1_TITLE;
			subtitleLine1 = STR_CHAPTER_N1_SUBTITLE_LINE_1;
			subtitleLine2 = STR_CHAPTER_N1_SUBTITLE_LINE_2;
			subtitleLineShort = STR_CHAPTER_N1_SUBTITLE_LINE_SHORT;
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
				if(mc.currentScreen == null) {
					return;
				}
				else if(mc.currentScreen instanceof GuiChapter)
				{
					didOneSecondPassed = true;
					
					// Play Sound
					Minecraft.getMinecraft().player.playSound("minebike:chaptertada", 1.0f, 1.0f);
					
					if(GuiChapter.chapterNumber == 1)
					{
						// Start Timer for close screen
				        Timer timer = new Timer(true);
				        timer.schedule(new TimerTask() {
							@Override
							public void run() {
					    		// Play Sound
								Minecraft.getMinecraft().player.playSound("minebike:narration_chapter_" + GuiChapter.chapterNumber, 1.0f, 1.0f);
							}
						}, 1000);
					}
				}
			}
		}, 1*1000);
		
		// Start Timer for close screen
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mc.currentScreen == null) {
					return;
				}
				else if(mc.currentScreen instanceof GuiChapter)
				{
					didOneSecondPassed = false;
			        if(mc.currentScreen != null) {
			        	Minecraft.getMinecraft().player.closeScreen();
			        }
				}
			}
		}, 7*1000); // Originally 4 seconds but suggested to have longer exposure
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
		    GL11.glPushMatrix();
		    	GL11.glScalef(2F, 2F, 2F);
			    
			    if(flagUnlocked)
			    {
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(strUnlocked, -1 * fontRendererObj.getStringWidth(strUnlocked)/2, mcHeight/4 - 50, 0xFF3333);
			    }
		
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(chapterText, -1 * fontRendererObj.getStringWidth(chapterText)/2, mcHeight/4 - 40, 0xFFFFFF);
	
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(title, -1 * fontRendererObj.getStringWidth(title)/2, mcHeight/4 - 30, 0xFFFFFF);
	    		
	    		if(didOneSecondPassed)
	    		{
	    			fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	        		fontRendererObj.drawString(subtitleLine1, -1 * fontRendererObj.getStringWidth(subtitleLine1)/2, mcHeight/4 - 10, 0xCCCCCC);
	
	            	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	        		fontRendererObj.drawString(subtitleLine2, -1 * fontRendererObj.getStringWidth(subtitleLine2)/2, mcHeight/4, 0xCCCCCC);
	    		}
    		GL11.glPopMatrix();

    		if(didOneSecondPassed)
    		{
	    		// TODO
	    		mc.renderEngine.bindTexture(CHAPTER_TEXTURE);
	    		if(!GuiChapter.isTodayWorkoutDone())
	    		{
	    			drawTexturedModalRect(-64 - 20, mcHeight/2 + 10 + 32, 64*(3-1), 0,  64 , 64);
	    			drawTexturedModalRect(      20, mcHeight/2 + 10 + 32, 64*(3-1), 64, 64 , 64);
	    		}
	    		else if(chapterNumber != 3)
	    		{
	    			drawTexturedModalRect(-32     , mcHeight/2 + 10 + 32, 64*(chapterNumber-1), 0,  64 , 64);
	    		}
	    		else
	    		{
	    			drawTexturedModalRect(-64 - 20, mcHeight/2 + 10 + 32, 64*(chapterNumber-1), 0,  64 , 64);
	    			drawTexturedModalRect(      20, mcHeight/2 + 10 + 32, 64*(chapterNumber-1), 64, 64 , 64);
	    		}
    		}
		GL11.glPopMatrix();
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}

	public static boolean isTodayWorkoutDone() {
		return isTodayWorkoutDone;
	}

	public static void setTodayWorkoutDone(boolean isTodayWorkoutDone) {
		GuiChapter.isTodayWorkoutDone = isTodayWorkoutDone;
	}

	public static int getTargetedLevel() {
		return targetedLevel;
	}

	public static void setTargetedLevel(int targetplayerlevel) {
		GuiChapter.targetedLevel = targetplayerlevel;
	}
}
