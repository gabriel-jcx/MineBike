package org.ngs.bigx.minecraft.client;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestRunFromMummy;

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
	private ResourceLocation HEART_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX,"textures/GUI/heart.png");
	private ResourceLocation QUESTLOCATION_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "texture/GUI/questlocationicon.png");
	private int HEART_OFFSET = 54;
	private int HEART_SIZE = 16;
	
	public static float gauge_01_percentile = 0;
	public static float gauge_02_percentile = 0;
	
	private Context context;
	
	public GuiStats(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiStats(Context c,Minecraft mc) {
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

    	if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int WIDTH = 200;
	    	int HEIGHT = HEART_SIZE + mc.fontRenderer.FONT_HEIGHT * 1 + 20 + 2;
	    	int mcWidth = sr.getScaledWidth();
	    	xPos = mcWidth - WIDTH + 2;
	    	double percentBig = context.timeSpent;
	    	double percentSmall = context.timeSpentSmall;
	    	int yy = yPos+HEART_SIZE+mc.fontRenderer.FONT_HEIGHT;
	    	
	    	if(CommonEventHandler.chasingQuestOnGoing)
	    	{
		    	GL11.glPushMatrix();
		    	
				    GL11.glTranslatef(mcWidth/2, 12f, 0); 
				    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				    GL11.glEnable(GL11.GL_BLEND);
				    mc.renderEngine.bindTexture(QUEST_TIMER_TEXTURE);
			        drawTexturedModalRect(-10, -10, 0, 0, 20 , 20);
	        	
	        	GL11.glPopMatrix();
	        	
	        	int time = 0;
	        	
	        	if(CommonEventHandler.chasingQuestOnCountDown)
	        	{
	        		time = CommonEventHandler.getCountdown();
	        	}
	        	else
	        	{
	        		time = CommonEventHandler.getTime();
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
	        	
	        	String text = chainsgQuestTimeLeft;

	        	FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, mcWidth/2-fontRendererObj.getStringWidth(text)/2, 22, 0);
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