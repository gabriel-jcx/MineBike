package org.ngs.bigx.minecraft.client;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Main;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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

public class GuiStats extends Gui {
	
	private Minecraft mc;
	
	private ResourceLocation HEART_TEXTURE = new ResourceLocation(Main.TEXTURE_PREFIX,"textures/GUI/heart.png");
	private ResourceLocation QUESTLOCATION_TEXTURE = new ResourceLocation(Main.TEXTURE_PREFIX, "texture/GUI/questlocationicon.png");
	private int HEART_OFFSET = 54;
	private int HEART_SIZE = 16;
	
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
	    	
	    	drawRect(mcWidth - WIDTH, 0, mcWidth , HEIGHT , 0x8F000000);
	    	drawRect(mcWidth - WIDTH + 1, 1, mcWidth - 1 , HEIGHT - 1, 0x00AAAAAA);
	    	
	    	xPos = mcWidth - WIDTH + 2;
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	mc.fontRenderer.drawString(I18n.format("gui.stats.heartrate", new Object[0])+": ",xPos,yPos+3,0xFFFFFF);
	    	mc.renderEngine.bindTexture(HEART_TEXTURE);
	        if (context.bump == false) {
	        	drawTexturedModalRect(xPos + HEART_OFFSET, yPos, 0, 0, HEART_SIZE , HEART_SIZE);
	        }
	        else {
	        	drawTexturedModalRect(xPos + HEART_OFFSET, yPos, HEART_SIZE, 0, HEART_SIZE, HEART_SIZE);
	        }
	        if(mc.currentScreen != null)
	        mc.fontRenderer.drawString(mc.currentScreen.width+" "+I18n.format("gui.stats.bpm", new Object[0]),xPos + HEART_OFFSET + HEART_SIZE + 2,yPos+3,0xFFFFFF);
//	        mc.fontRenderer.drawString(context.heartrate+" "+I18n.format("gui.stats.bpm", new Object[0]),xPos + HEART_OFFSET + HEART_SIZE + 2,yPos+3,0xFFFFFF);
	    	mc.fontRenderer.drawString(I18n.format("gui.stats.speed", new Object[0])+": "+(context.getSpeed()*20)+" meters per second",xPos,yPos+HEART_SIZE,0xFFFFFF);
	    	double percentBig = context.timeSpent;
	    	double percentSmall = context.timeSpentSmall;
	    	int yy = yPos+HEART_SIZE+mc.fontRenderer.FONT_HEIGHT;
	    	drawRect(xPos,yy,xPos+180,yy+20, 0xFF000000);
	    	drawGradientRect(xPos+1,yy+1,(int) (xPos+180*(percentBig/100)-1),yy+20-1, 0xFFFF0000, 0xFFAA0000);
	    	String text = "Progress: "+((int) percentBig)+"."+((int) percentSmall)+"%";
	    	mc.fontRenderer.drawString(text,xPos+180/2-mc.fontRenderer.getStringWidth(text)/2,yy+20/2-mc.fontRenderer.FONT_HEIGHT/2,0xFFFF00);
	    	//mc.fontRenderer.drawString(I18n.format("gui.stats.resistance", new Object[0])+": "+(context.resistance),xPos,yPos+HEART_SIZE+mc.fontRenderer.FONT_HEIGHT,0xFFFFFF);
	    	
	    	Vec3 playerlook = mc.thePlayer.getLookVec();
	    	ChunkCoordinates playerLocation = mc.thePlayer.getPlayerCoordinates();
    	}
    }
}