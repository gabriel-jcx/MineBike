package org.ngs.bigx.minecraft.client;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.bike.PedalingToBuildEventHandler;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.client.gui.GuiChapter;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiStats extends GuiScreen {
	

	public static int tick = 0;
	
	private Minecraft mc;

	private ResourceLocation SPEEDOMETER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/gauge_bg.png");
	private ResourceLocation QUEST_TIMER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/timer.png");
	private ResourceLocation OBJECTIVE_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/objective.png");
	private ResourceLocation THIEF_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/theif.png");
	private ResourceLocation DAMAGEUP_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/damageup.png");
	private ResourceLocation SPEEDUP_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/speedup.png");
	private ResourceLocation TRAFFIC_NONE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/traffic-none.png");
	private ResourceLocation TRAFFIC_RED = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/traffic-red.png");
	private ResourceLocation TRAFFIC_YELLOW = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/traffic-yellow.png");
	private ResourceLocation TRAFFIC_GREEN = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/traffic-green.png");
	private ResourceLocation QUESTLOCATION_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "texture/GUI/questlocationicon.png");
	private ResourceLocation PEDALINGMODE_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/pedalingmode-reverse.png");
	private ResourceLocation PEDALINGGAUGE_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/pedalinggaugebar.png");
	private ResourceLocation CHAPTER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/chapterinstruction.png");
	private ResourceLocation FIRSTINSTRUCTION_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/arrowsoffirstinstruction.png");
	
	private int HEART_OFFSET = 54;
	private int HEART_SIZE = 16;
	
	public static float gauge_01_percentile = 0;
	public static float gauge_02_percentile = 0;

	private BigxClientContext context;
	private static BigxServerContext serverContext = null;
	
	public GuiStats(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiStats(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	/**
	 * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
	 */
	public static void drawRect(int par0, int par1, int par2, int par3, int par4)
	{
	    int j1;

	    if (par0 < par2)
	    {
	        j1 = par0;
	        par0 = par2;
	        par2 = j1;
	    }

	    if (par1 < par3)
	    {
	        j1 = par1;
	        par1 = par3;
	        par3 = j1;
	    }

	    float f = (float)(par4 >> 24 & 255) / 255.0F;
	    float f1 = (float)(par4 >> 16 & 255) / 255.0F;
	    float f2 = (float)(par4 >> 8 & 255) / 255.0F;
	    float f3 = (float)(par4 & 255) / 255.0F;
	    
	    Tessellator tessellator = Tessellator.instance;
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glColor4f(f1, f2, f3, f);
	    tessellator.startDrawingQuads();
	    tessellator.addVertex((double)par0, (double)par3, 0.0D);
	    tessellator.addVertex((double)par2, (double)par3, 0.0D);
	    tessellator.addVertex((double)par2, (double)par1, 0.0D);
	    tessellator.addVertex((double)par0, (double)par1, 0.0D);
	    tessellator.draw();
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	    GL11.glDisable(GL11.GL_BLEND);
	}

	private float lerp(float a, float b, float f) 
	{
	    return (a * (1.0f - f)) + (b * f);
	}
	
	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if(event.isCancelable() || event.type != event.type.TEXT || !context.modEnabled)
	    {      
	      return;
	    }
    	int xPos = 2;
    	int yPos = 2;
    	FontRenderer fontRendererObj;
    	String text = "";

    	if (mc.player != null) {
	    	EntityPlayer p = mc.player;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int WIDTH = 200;
	    	int HEIGHT = HEART_SIZE + mc.fontRenderer.FONT_HEIGHT * 1 + 20 + 2;
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	xPos = mcWidth - WIDTH + 2;
	    	double percentBig = context.timeSpent;
	    	double percentSmall = context.timeSpentSmall;
	    	int yy = yPos+HEART_SIZE+mc.fontRenderer.FONT_HEIGHT;

    		float scaleX = 0.25f;
    		float scaleY = 0.25f*1.5f;
    		int rectX = 256;
	    	int rectY = (int)(256/3f);
	    	float targetAlpha = 0.0f;

	    	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    	
	    	if(mc.player.worldObj.provider.dimensionId == 0)
	    	{
	    		if(mc.currentScreen == null)
				{
	    			if(BigxClientContext.getIsGameSaveRead())
	    			{
	    				text = GuiChapter.getCurrentChapterSubtitleShort();
	    				fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 25, 0xFFFFFF);
			    		
			    		GL11.glPushMatrix();
			    			GL11.glScalef(.5f, .5f, .5f);
			    			// TODO
				    		mc.renderEngine.bindTexture(CHAPTER_TEXTURE);
				    		int chapterNumber = GuiChapter.getChapterNumber();
				    		
				    		if(!GuiChapter.isTodayWorkoutDone())
				    		{
				    			drawTexturedModalRect(mcWidth -64 - 10, 50 + 30, 64*(3-1), 0,  64 , 64);
				    			drawTexturedModalRect(mcWidth     + 10, 50 + 30, 64*(3-1), 64, 64 , 64);
				    		}
				    		else if(chapterNumber != 3)
				    		{
				    			drawTexturedModalRect(mcWidth     , 50 + 30, 64*(chapterNumber-1), 0,  64 , 64);
				    		}
				    		else
				    		{
				    			drawTexturedModalRect(mcWidth -64 - 10, 50 + 30, 64*(chapterNumber-1), 0,  64 , 64);
				    			drawTexturedModalRect(mcWidth     + 10, 50 + 30, 64*(chapterNumber-1), 64, 64 , 64);
				    		}
			    		GL11.glPopMatrix();
	    			}
				}
	    	}
	    	
	    	/**
	    	 * Pedaling Mode Related Drawing
	    	 */
	    	if(mc.player.getHeldItem() != null)
	    	{
	    		if(mc.player.getHeldItem().getItem() == Items.paper)
				{
	    			text = "Press LT to open Quest List Menu";

		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, mcHeight/2 - 50, 0xFFFFFF);
				}
	    		else if(mc.player.getHeldItem().getDisplayName().contains("Phone"))
	    		{
	    			text = "Press LT to Change Bike Mode (Move/Mining/Building)";

		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, mcHeight/2 - 50, 0xFFFFFF);
				}
	    	}
	    	
	    	/**
	    	 * Draw the fist instruction screen
	    	 * 88, 74, 241), new Vec3d(91, 80, 244
	    	 */
	    	if( (mc.player.dimension == 0) && (GuiChapter.getChapterNumber() == 1) )
	    	{
	    		if( (mc.player.posX >= 88) && (mc.player.posX <= 91) &&
	    				(mc.player.posY >= 74) && (mc.player.posY <=80) &&
	    				(mc.player.posZ >= 241) && (mc.player.posZ <= 244) )
	    		{
	    			GL11.glPushMatrix();
	    				GL11.glPushMatrix();
	    					drawRect(0, 0, mcWidth, mcHeight, 0x44000000); // The Box on top
	    				GL11.glPopMatrix();
	    			// TODO
					    GL11.glTranslatef(mcWidth/2, 12f, 0); 
					    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					    GL11.glEnable(GL11.GL_BLEND);
					    GL11.glScalef(.5f, .5f, .5f);
					    mc.renderEngine.bindTexture(FIRSTINSTRUCTION_TEXTURE);
				        drawTexturedModalRect(-160, 45, 0, 0, 83 , 65);

					    mc.renderEngine.bindTexture(FIRSTINSTRUCTION_TEXTURE);
				        drawTexturedModalRect(70, 100, 0, 65, 83 , 65);
	    			GL11.glPopMatrix();
	    			
	    			text = "Describes the current task";

		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-90-fontRendererObj.getStringWidth(text)/2, 70, 0xFFFFFF);
	    			
	    			text = "Describes where to go or whom to talk";

		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawStringWithShadow(text, mcWidth/2+80-fontRendererObj.getStringWidth(text)/2, 100, 0xFFFFFF);
	    		}
	    	}
	    	
	    	/**
	    	 *  Peadling Gauge Drawing
	    	 */
	    	int pdealgauge = ClientEventHandler.pedalingCombo.getGauge();
	    	int pedalgaugelevel = ClientEventHandler.pedalingCombo.getLevel();
	    	
	    	int pedalgaugedxscreen = 1;
	    	float pdealgaugedxlevel = 72f;
	    	
	    	switch(pedalgaugelevel)
	    	{
	    	case 0:
	    		pedalgaugedxscreen = 1;
	    		pdealgaugedxlevel = 72f;
    			break;
	    	case 1:
	    		pedalgaugedxscreen = 73;
	    		pdealgaugedxlevel = 48f;
	    		break;
	    	case 2:
	    		pedalgaugedxscreen = 121;
	    		pdealgaugedxlevel = 24f;
	    		break;
    		default:
	    		pedalgaugedxscreen = 1;
	    		pdealgaugedxlevel = 72f;
    			break;
	    	};
//System.out.println("pdealgauge["+pdealgauge+"]");
    		pedalgaugedxscreen += (int) (((double)(pdealgauge-(1920*pedalgaugelevel))) * pdealgaugedxlevel / 1920f);
//    		System.out.println("pedalgaugedxscreen["+pedalgaugedxscreen+"]");
//    		System.out.println("pedalgaugelevel["+pedalgaugelevel+"]");
	    	
	    	GL11.glPushMatrix();
	    	
		    	GL11.glTranslatef(mcWidth/2 - 73, mcHeight/2 + 50, 0);
			    GL11.glEnable(GL11.GL_BLEND);
			    GL11.glColor4f(1.0F, 1.0F, 1.0F, .5F);
			    mc.renderEngine.bindTexture(PEDALINGGAUGE_TEXTURE);

		    	drawTexturedModalRect(0, 0, 0, 0, 146, 19);
		    	
			    GL11.glColor4f(1.0F, 1.0F, 1.0F, .8F);
		    	drawTexturedModalRect(0, 0, 0, 24, pedalgaugedxscreen, 19);
				
	    	GL11.glPopMatrix();
	    	
	    	if(ClientEventHandler.pedalingModeState == 2)
	    	{
	    		text = "  ";
	    		
	    		if(PedalingToBuildEventHandler.pedalingToBuild !=null)
	    		{
	    			text = "Or press DPAD DOWN to reselect a building";

		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 67, 0xFFFFFF);
		    		
		    		text = "" + PedalingToBuildEventHandler.pedalingToBuild.getCurrentProgress();
	    		}
	    		else if(PedalingToBuildEventHandler.buildingId.equals(""))
	    		{
		    		text = "Press DPAD DOWN to select a building";
	    		}
	    		else if(!PedalingToBuildEventHandler.buildingId.equals(""))
	    		{
	    			text = "Or press DPAD DOWN to reselect a building";

		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 67, 0xFFFFFF);
		    		
		    		text = "Hit a block to start a building";
	    		}

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawStringWithShadow(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 52, 0xFFFFFF);
	    	}
	    	/**
	    	 * END OF Pedaling Mode Related Drawing
	    	 */

	    	/**
	    	 * Quest Progress Indicator Drawing
	    	 */
	    	if(serverContext == null)
	    	{
	    		return;
	    	}
	    	
	    	BigxServerContext bigxServerContext = serverContext;
	    	
	    	if(bigxServerContext.getQuestManager() == null)
	    	{
	    		System.out.println("bigxServerContext.getQuestManager() is null");
	    		return;
	    	}
	    	
	    	if(bigxServerContext.getQuestManager().getActiveQuest() == null)
	    	{
	    		System.out.println("bigxServerContext.getQuestManager().getActiveQuest() is null");
	    		return;
	    	}
	    	
	    	synchronized (bigxServerContext.getQuestManager()) {
	    		boolean isQuestTaskChasing = false;
	    		boolean isQuestTaskFightAndChasing = false;
	    		
		    	if(bigxServerContext.getQuestManager() != null && bigxServerContext.getQuestManager().getActiveQuest() != null && 
		    			bigxServerContext.getQuestManager().getActiveQuest().getCurrentQuestTask() instanceof QuestTaskChasing )
		    	{
		    		isQuestTaskChasing = true;
		    	}
		    	else if(bigxServerContext.getQuestManager() != null && bigxServerContext.getQuestManager().getActiveQuest() != null && 
		    			bigxServerContext.getQuestManager().getActiveQuest().getCurrentQuestTask() instanceof QuestTaskFightAndChasing )
		    	{
		    		isQuestTaskFightAndChasing = true;
		    	}
		    	
		    	if(isQuestTaskChasing)
		    	{
		    		QuestTaskChasing questTaskChasing = (QuestTaskChasing) bigxServerContext.getQuestManager().getActiveQuest().getCurrentQuestTask();
		    		
		    		if(!questTaskChasing.isChasingQuestOnGoing())
		    			return;
		    		
			    	GL11.glPushMatrix();
			    	
					    GL11.glTranslatef(mcWidth/2, 12f, 0); 
					    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					    GL11.glEnable(GL11.GL_BLEND);
					    mc.renderEngine.bindTexture(QUEST_TIMER_TEXTURE);
				        drawTexturedModalRect(-10, -10, 0, 0, 20 , 20);
				        
					    mc.renderEngine.bindTexture(OBJECTIVE_TEXTURE);
				        drawTexturedModalRect(-40, -10, 0, 0, 20 , 20);
				        
					    mc.renderEngine.bindTexture(THIEF_TEXTURE);
				        drawTexturedModalRect(20, -10, 0, 0, 20 , 20);
				        
				        if(QuestTaskChasing.getSpeedBoostTickCountLeft() > 0)
				        {
						    mc.renderEngine.bindTexture(SPEEDUP_TEXTURE);
					        drawTexturedModalRect(50, -10, 0, 0, 20 , 20);
				        }
				        
				        if(QuestTaskChasing.getDamageBoostTickCountLeft() > 0)
				        {
						    mc.renderEngine.bindTexture(DAMAGEUP_TEXTURE);
					        drawTexturedModalRect(80, -10, 0, 0, 20 , 20);
				        }
		        	
		        	GL11.glPopMatrix();
		        	
		        	GL11.glPushMatrix();
		        		
		        		if (questTaskChasing.isChasingQuestOnCountDown() && questTaskChasing.getCountdown() < 6) {
		        			targetAlpha = 1.0f;
		        		} else {
		        			targetAlpha = 0.0f;
						    if (ClientEventHandler.animTickChasingFade < (ClientEventHandler.animTickChasingFadeLength + ClientEventHandler.animTickFadeTime))
						    	if (ClientEventHandler.animTickChasingFade >= ClientEventHandler.animTickChasingFadeLength)
						    		targetAlpha = lerp(1.0f, 0.0f, (float)(ClientEventHandler.animTickChasingFade - ClientEventHandler.animTickChasingFadeLength) / ClientEventHandler.animTickFadeTime);
						    	else
						    		targetAlpha = 1.0f;
		        		}
			        	
				        scaleX = 0.3f;
			    		scaleY = 0.3f;
			    		rectX = 256;
				    	rectY = 256;
				        GL11.glTranslatef(mcWidth/2 - (int)(rectX*scaleX/2f), 32, 0); 
					    GL11.glScalef(scaleX, scaleY, 1.0f);
					    GL11.glColor4f(1.0F, 1.0F, 1.0F, targetAlpha);
					    GL11.glEnable(GL11.GL_BLEND);
					    ResourceLocation chosenImage = questTaskChasing.getCountdown() > 2 && questTaskChasing.getCountdown() < 6  ? TRAFFIC_NONE :
					    	questTaskChasing.getCountdown() == 2 ? TRAFFIC_RED :
					    		questTaskChasing.getCountdown() == 1 ? TRAFFIC_YELLOW : 
					    			TRAFFIC_GREEN;
				        mc.renderEngine.bindTexture(chosenImage);
				        				        
				        drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			        
			        GL11.glPopMatrix();
		        
		        	int time = 0;
		        	
		        	QuestTaskChasing quest = (QuestTaskChasing) bigxServerContext.getQuestManager().getActiveQuest().getCurrentQuestTask();
		        	if(quest != null){
		        		if(quest.isChasingQuestOnCountDown())
						{
							time = quest.getCountdown();
						}
						else
						{
							time = quest.getTime();
						}
		        	}
		        	else
		        	{
		        		return;
		        	}
		        	
		        	int minuteLeft = time/60;
					int secondLeft = time%60;
					
					String chainsgQuestTimeLeft = "";
					
					if(minuteLeft<10)
					{
						chainsgQuestTimeLeft = "0";
					}
					
					chainsgQuestTimeLeft += minuteLeft + ":";
					
					if(secondLeft<10)
					{
						chainsgQuestTimeLeft += "0";
					}
					
					chainsgQuestTimeLeft += secondLeft;
		        	
		        	text = chainsgQuestTimeLeft;
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 22, 0);
		    		
		    		text = "" + new DecimalFormat("###.#").format(quest.getDist());
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 - 30, 22, 0);
		        	
		    		text = "HP";
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 22, 0);
			        
			        if(QuestTaskChasing.getSpeedBoostTickCountLeft() > 0)
			        {
			        	time = QuestTaskChasing.getSpeedBoostTickCountLeft() * 50;
			        	minuteLeft = time/1000;
						secondLeft = (time%1000)/10;
						
						chainsgQuestTimeLeft = "";
						
						if(minuteLeft<10)
						{
							chainsgQuestTimeLeft = "0";
						}
						
						chainsgQuestTimeLeft += minuteLeft + ":";
						
						if(secondLeft<10)
						{
							chainsgQuestTimeLeft += "0";
						}
						
						chainsgQuestTimeLeft += secondLeft;
			        	
			        	text = chainsgQuestTimeLeft;
		
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 60, 22, 0);
			        }
			        
			        if(QuestTaskChasing.getDamageBoostTickCountLeft() > 0)
			        {
			        	time = QuestTaskChasing.getDamageBoostTickCountLeft() * 50;
			        	minuteLeft = time/1000;
						secondLeft = (time%1000)/10;
						
						chainsgQuestTimeLeft = "";
						
						if(minuteLeft<10)
						{
							chainsgQuestTimeLeft = "0";
						}
						
						chainsgQuestTimeLeft += minuteLeft + ":";
						
						if(secondLeft<10)
						{
							chainsgQuestTimeLeft += "0";
						}
						
						chainsgQuestTimeLeft += secondLeft;
			        	
			        	text = chainsgQuestTimeLeft;
		
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 90, 22, 0);
			        }
		        	
		    		text = "" + quest.getThiefHealthCurrent();
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 32, 0);
		    		
		    		if(NpcCommand.hasFallen)
		    		{
	    				GL11.glPushMatrix();
					    GL11.glTranslatef(mcWidth/2, 0, 0);
				    	GL11.glScalef(2F, 2F, 2F);
				    		text = "THE THIEF HAS FALLEN!";
			
				        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
				    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, 30, 0xFF00FF);
			    		GL11.glPopMatrix();
		    		}
		    		
		    		if(quest.getTimeFallBehind() > 0)
		    		{
		    			if( (System.currentTimeMillis() - quest.getWarningMsgBlinkingTime()) < 700)
		    			{
		    				GL11.glPushMatrix();
							    GL11.glTranslatef(mcWidth/2, 0, 0);
						    	GL11.glScalef(2F, 2F, 2F);
						    	
					    		text = "WARNING";
			
					        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
					    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, 30, 0xFF0000);
						    	
					    		text = "THIEF IS GETTING AWAY";
			
					        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
					    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, 40, 0xFF0000);
				    		GL11.glPopMatrix();
		    			}
		    		}
		    		
		    		if(quest.getComboCount() != 0)
		    		{
		    			int combotextColor = 0xFFFFFF;
		    			combotextColor -= ((4 * quest.getComboCount()) << 8);
		    			combotextColor -= (4 * quest.getComboCount());
		    			combotextColor |= 0xFF0000;
		    			
	    				GL11.glPushMatrix();
					    GL11.glTranslatef(0, 0, 0);
				    	GL11.glScalef(3F, 3F, 3F);
				    		text = quest.getComboCount() + " COMBO";
			
				        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
				    		fontRendererObj.drawString(text, mcWidth/6-fontRendererObj.getStringWidth(text)/6 + 20, mcHeight/6 - 20, combotextColor);
			    		GL11.glPopMatrix();
		    		}
		    		else
		    		{
		    			quest.checkComboCount();
		    		}
		    		
		    		if(System.currentTimeMillis() - cheeringMessageTimeStamp < 1000)
		    		{
		    			int combotextColor = 0xFFFFFF;
		    			
	    				GL11.glPushMatrix();
					    GL11.glTranslatef(0, 0, 0);
				    	GL11.glScalef(2F, 2F, 2F);
				    		text = cheeringMessageText;
			
				        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
				    		fontRendererObj.drawStringWithShadow(text, 5, mcHeight/4 - 20, combotextColor);
			    		GL11.glPopMatrix();
		    		}
		    	}
		    	else if(isQuestTaskFightAndChasing)
		    	{
		    		QuestTaskFightAndChasing questTaskFightAndChasing = (QuestTaskFightAndChasing) bigxServerContext.getQuestManager().getActiveQuest().getCurrentQuestTask();
		    		
		    		if(!questTaskFightAndChasing.isChasingQuestOnGoing())
		    			return;
		    		
			    	GL11.glPushMatrix();
			    	
					    GL11.glTranslatef(mcWidth/2, 12f, 0); 
					    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					    GL11.glEnable(GL11.GL_BLEND);
					    mc.renderEngine.bindTexture(QUEST_TIMER_TEXTURE);
				        drawTexturedModalRect(-10, -10, 0, 0, 20 , 20);
				        
					    mc.renderEngine.bindTexture(OBJECTIVE_TEXTURE);
				        drawTexturedModalRect(-40, -10, 0, 0, 20 , 20);
				        
					    mc.renderEngine.bindTexture(THIEF_TEXTURE);
				        drawTexturedModalRect(20, -10, 0, 0, 20 , 20);
				        
				        if(QuestTaskFightAndChasing.getSpeedBoostTickCountLeft() > 0)
				        {
						    mc.renderEngine.bindTexture(SPEEDUP_TEXTURE);
					        drawTexturedModalRect(50, -10, 0, 0, 20 , 20);
				        }
				        
				        if(QuestTaskFightAndChasing.getDamageBoostTickCountLeft() > 0)
				        {
						    mc.renderEngine.bindTexture(DAMAGEUP_TEXTURE);
					        drawTexturedModalRect(80, -10, 0, 0, 20 , 20);
				        }
		        	
		        	GL11.glPopMatrix();
		        	
//		        	GL11.glPushMatrix();
//		        		
//		        		if (questTaskFightAndChasing.isChasingQuestOnCountDown() && questTaskFightAndChasing.getCountdown() < 6) {
//		        			targetAlpha = 1.0f;
//		        		} else {
//		        			targetAlpha = 0.0f;
//						    if (ClientEventHandler.animTickChasingFade < (ClientEventHandler.animTickChasingFadeLength + ClientEventHandler.animTickFadeTime))
//						    	if (ClientEventHandler.animTickChasingFade >= ClientEventHandler.animTickChasingFadeLength)
//						    		targetAlpha = lerp(1.0f, 0.0f, (float)(ClientEventHandler.animTickChasingFade - ClientEventHandler.animTickChasingFadeLength) / ClientEventHandler.animTickFadeTime);
//						    	else
//						    		targetAlpha = 1.0f;
//		        		}
//			        	
//				        scaleX = 0.3f;
//			    		scaleY = 0.3f;
//			    		rectX = 256;
//				    	rectY = 256;
//				        GL11.glTranslatef(mcWidth/2 - (int)(rectX*scaleX/2f), 32, 0); 
//					    GL11.glScalef(scaleX, scaleY, 1.0f);
//					    GL11.glColor4f(1.0F, 1.0F, 1.0F, targetAlpha);
//					    GL11.glEnable(GL11.GL_BLEND);
//					    ResourceLocation chosenImage = questTaskFightAndChasing.getCountdown() > 2 && questTaskFightAndChasing.getCountdown() < 6  ? TRAFFIC_NONE :
//					    	questTaskFightAndChasing.getCountdown() == 2 ? TRAFFIC_RED :
//					    		questTaskFightAndChasing.getCountdown() == 1 ? TRAFFIC_YELLOW : 
//					    			TRAFFIC_GREEN;
//				        mc.renderEngine.bindTexture(chosenImage);
//				        drawTexturedModalRect(0, 0, 0, 0, 256, 256);
//			        
//			        GL11.glPopMatrix();
		        
		        	int time = 0;
		        	
		        	QuestTaskFightAndChasing quest = (QuestTaskFightAndChasing) bigxServerContext.getQuestManager().getActiveQuest().getCurrentQuestTask();
		        	if(quest != null){
		        		if(quest.isChasingQuestOnCountDown())
						{
							time = quest.getCountdown();
						}
						else
						{
							time = quest.getTime();
						}
		        	}
		        	else
		        	{
		        		return;
		        	}
		        	
		        	int minuteLeft = time/60;
					int secondLeft = time%60;
					
					String chainsgQuestTimeLeft = "";
					
					if(minuteLeft<10)
					{
						chainsgQuestTimeLeft = "0";
					}
					
					chainsgQuestTimeLeft += minuteLeft + ":";
					
					if(secondLeft<10)
					{
						chainsgQuestTimeLeft += "0";
					}
					
					chainsgQuestTimeLeft += secondLeft;
		        	
		        	text = chainsgQuestTimeLeft;
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 22, 0);
		    		
		    		text = "" + new DecimalFormat("###.#").format(quest.getDist());
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 - 30, 22, 0);
		        	
		    		text = "HP";
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 22, 0);
			        
			        if(QuestTaskFightAndChasing.getSpeedBoostTickCountLeft() > 0)
			        {
			        	time = QuestTaskFightAndChasing.getSpeedBoostTickCountLeft() * 50;
			        	minuteLeft = time/1000;
						secondLeft = (time%1000)/10;
						
						chainsgQuestTimeLeft = "";
						
						if(minuteLeft<10)
						{
							chainsgQuestTimeLeft = "0";
						}
						
						chainsgQuestTimeLeft += minuteLeft + ":";
						
						if(secondLeft<10)
						{
							chainsgQuestTimeLeft += "0";
						}
						
						chainsgQuestTimeLeft += secondLeft;
			        	
			        	text = chainsgQuestTimeLeft;
		
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 60, 22, 0);
			        }
			        
			        if(System.currentTimeMillis() - cheeringMessageTimeStamp < 1000)
		    		{
		    			int combotextColor = 0xFFFFFF;
		    			
	    				GL11.glPushMatrix();
					    GL11.glTranslatef(0, 0, 0);
				    	GL11.glScalef(2F, 2F, 2F);
				    		text = cheeringMessageText;
			
				        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
				    		fontRendererObj.drawStringWithShadow(text, 5, mcHeight/4 - 20, combotextColor);
			    		GL11.glPopMatrix();
		    		}
			        
			        if(QuestTaskFightAndChasing.getDamageBoostTickCountLeft() > 0)
			        {
			        	time = QuestTaskFightAndChasing.getDamageBoostTickCountLeft() * 50;
			        	minuteLeft = time/1000;
						secondLeft = (time%1000)/10;
						
						chainsgQuestTimeLeft = "";
						
						if(minuteLeft<10)
						{
							chainsgQuestTimeLeft = "0";
						}
						
						chainsgQuestTimeLeft += minuteLeft + ":";
						
						if(secondLeft<10)
						{
							chainsgQuestTimeLeft += "0";
						}
						
						chainsgQuestTimeLeft += secondLeft;
			        	
			        	text = chainsgQuestTimeLeft;
		
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 90, 22, 0);
			        }
		        	
		    		text = "" + quest.getThiefHealthCurrent();
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 32, 0);
		    		
		    		if(quest.getTimeFallBehind() > 0)
		    		{
		    			if( (System.currentTimeMillis() - quest.getWarningMsgBlinkingTime()) < 700)
		    			{
		    				GL11.glPushMatrix();
							    GL11.glTranslatef(mcWidth/2, 0, 0);
						    	GL11.glScalef(2F, 2F, 2F);
						    	
					    		text = "WARNING";
			
					        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
					    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, 30, 0xFF0000);
						    	
					    		text = "THIEF IS GETTING AWAY";
			
					        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
					    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, 40, 0xFF0000);
				    		GL11.glPopMatrix();
		    			}
		    		}
		    		
		    		if(quest.getComboCount() != 0)
		    		{
		    			int combotextColor = 0xFFFFFF;
		    			combotextColor -= ((4 * quest.getComboCount()) << 8);
		    			combotextColor -= (4 * quest.getComboCount());
		    			combotextColor |= 0xFF0000;
		    			
	    				GL11.glPushMatrix();
					    GL11.glTranslatef(0, 0, 0);
				    	GL11.glScalef(3F, 3F, 3F);
				    		text = quest.getComboCount() + " COMBO";
			
				        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
				    		fontRendererObj.drawString(text, mcWidth/6-fontRendererObj.getStringWidth(text)/6 + 20, mcHeight/6 - 20, combotextColor);
			    		GL11.glPopMatrix();
		    		}
		    		else
		    		{
		    			quest.checkComboCount();
		    		}
		    		
		    		
		    	}
	    	}
	    	/**
	    	 * END OF Quest Progress Indicator Drawing
	    	 */
    	}
    }
	
	private float calculateLocationIconPositions(Vec3d vLook, ChunkCoordinates playerLocation, ChunkCoordinates questLocation)
	{
		float scaleFactorLookVector;
		float scaleFactorQuestVector;
		
		Vec3d perpVector = new Vec3d(vLook.yCoord, vLook.xCoord * -1f, vLook.zCoord);
		
		
		
		return 0f;
	}

	public static void setServerContext(BigxServerContext serverContext) {
		GuiStats.serverContext = serverContext;
	}
	
	public static long cheeringMessageTimeStamp = System.currentTimeMillis();
	public static String cheeringMessageText = "";

	public static void showCheeringMessage(String text) {
		cheeringMessageTimeStamp = System.currentTimeMillis();
		cheeringMessageText = text;
		System.out.println("[][][] show cheering ts["+cheeringMessageTimeStamp+"]");
	}
}