package org.ngs.bigx.minecraft.client;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskChasingFire;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiStats extends GuiScreen {
	
	public static int tick = 0;
	
	private Minecraft mc;

	private ResourceLocation SPEEDOMETER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/gauge_bg.png");
	private ResourceLocation QUEST_TIMER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/timer.png");
	private ResourceLocation OBJECTIVE_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/objective.png");
	private ResourceLocation THIEF_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX,"textures/GUI/theif.png");
	private ResourceLocation QUESTLOCATION_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "texture/GUI/questlocationicon.png");
	private int HEART_OFFSET = 54;
	private int HEART_SIZE = 16;
	
	public static float gauge_01_percentile = 0;
	public static float gauge_02_percentile = 0;
	
	private BigxClientContext context;
	
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

	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if(event.isCancelable() || event.type != event.type.TEXT || !context.modEnabled)
	    {      
	      return;
	    }
    	int xPos = 2;
    	int yPos = 2;
    	FontRenderer fontRendererObj;
    	String text;

    	if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int WIDTH = 200;
	    	int HEIGHT = HEART_SIZE + mc.fontRenderer.FONT_HEIGHT * 1 + 20 + 2;
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	xPos = mcWidth - WIDTH + 2;
	    	double percentBig = context.timeSpent;
	    	double percentSmall = context.timeSpentSmall;
	    	int yy = yPos+HEART_SIZE+mc.fontRenderer.FONT_HEIGHT;
	    	
	    	if(context.getQuestManager() != null && context.getQuestManager().getActiveQuestId() != "NONE" && 
	    			context.getQuestManager().getActiveQuestTask() instanceof QuestTaskChasing )
	    	{
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
	        	
	        	GL11.glPopMatrix();
	        	
	        	int time = 0;
	        	
	        	QuestTaskChasing quest = (QuestTaskChasing) context.getQuestManager().getActiveQuestTask();
	        	if(quest.chasingQuestOnCountDown)
	        	{
	        		time = quest.getCountdown();
	        	}
	        	else
	        	{
	        		time = quest.getTime();
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
	        	
	    		text = "" + new DecimalFormat("###.#").format(quest.dist);

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 - 30, 22, 0);
	        	
	    		text = "Lv: " + quest.getThiefLevel();

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 22, 0);
	        	
	    		text = "HP: " + quest.getThiefHealthCurrent();

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 32, 0);
	    		
	    		if(quest.getTimeFallBehind() > 0)
	    		{
	    			if( (System.currentTimeMillis() - quest.warningMsgBlinkingTime) < 700)
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
	    	}
	    	else if(context.getQuestManager() != null && context.getQuestManager().getActiveQuestId() != "NONE" && 
	    			context.getQuestManager().getActiveQuestTask() instanceof QuestTaskChasingFire )
	    	{
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
	        	
	        	GL11.glPopMatrix();
	        	
	        	int time = 0;
	        	
	        	QuestTaskChasingFire quest = (QuestTaskChasingFire) context.getQuestManager().getActiveQuestTask();
	        	if(quest.chasingQuestOnCountDown)
	        	{
	        		time = quest.getCountdown();
	        	}
	        	else
	        	{
	        		time = quest.getTime();
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
	        	
	    		text = "" + new DecimalFormat("###.#").format(quest.dist);

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 - 30, 22, 0);
	        	
	    		text = "Lv: " + quest.getThiefLevel();

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 22, 0);
	        	
	    		text = "HP: " + quest.getThiefHealthCurrent();

	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2 + 30, 32, 0);
	    		
	    		if(quest.getTimeFallBehind() > 0)
	    		{
	    			if( (System.currentTimeMillis() - quest.warningMsgBlinkingTime) < 700)
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
	    	}
	    	
	    	Vec3 playerlook = mc.thePlayer.getLookVec();
	    	ChunkCoordinates playerLocation = mc.thePlayer.getPlayerCoordinates();
    	}
    }
	
	private float calculateLocationIconPositions(Vec3 vLook, ChunkCoordinates playerLocation, ChunkCoordinates questLocation)
	{
		float scaleFactorLookVector;
		float scaleFactorQuestVector;
		
		Vec3 perpVector = Vec3.createVectorHelper(vLook.yCoord, vLook.xCoord * -1f, vLook.zCoord);
		
		
		
		return 0f;
	}
}