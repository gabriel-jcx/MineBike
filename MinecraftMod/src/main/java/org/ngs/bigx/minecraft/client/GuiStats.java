package org.ngs.bigx.minecraft.client;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Main;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiStats extends Gui {
	
	private Minecraft mc;
	
	private ResourceLocation HEART_TEXTURE = new ResourceLocation(Main.TEXTURE_PREFIX,"textures/GUI/Heart.png");
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
	    	drawRect(mcWidth - WIDTH, 0, mcWidth , HEIGHT , 0xFF000000);
	    	drawRect(mcWidth - WIDTH + 1, 1, mcWidth - 1 , HEIGHT - 1, 0xFFAAAAAA);
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
    	}
    }
}