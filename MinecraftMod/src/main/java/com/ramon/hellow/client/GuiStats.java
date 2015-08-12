package com.ramon.hellow.client;

import org.lwjgl.opengl.GL11;

import com.ramon.hellow.Context;
import com.ramon.hellow.Main;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
	    if(event.isCancelable() || event.type != event.type.TEXT)
	    {      
	      return;
	    }
    	int xPos = 2;
    	int yPos = 2;
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //GL11.glDisable(GL11.GL_LIGHTING);
    	if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
	    	int WIDTH = 200;
	    	int HEIGHT = HEART_SIZE + mc.fontRenderer.FONT_HEIGHT * 2 + 2;
	    	drawRect(0, 0, xPos + WIDTH , yPos + HEIGHT , 0xFF000000);
	    	drawRect(1, 1, xPos + WIDTH - 1 , yPos + HEIGHT - 1, 0xFFAAAAAA);
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	mc.fontRenderer.drawString(I18n.format("gui.stats.heartrate", new Object[0])+": ",xPos,yPos+3,0xFFFFFF);
	    	mc.renderEngine.bindTexture(HEART_TEXTURE);
	        if (context.bump == false) {
	        	drawTexturedModalRect(xPos + HEART_OFFSET, yPos, 0, 0, HEART_SIZE , HEART_SIZE);
	        }
	        else {
	        	drawTexturedModalRect(xPos + HEART_OFFSET, yPos, HEART_SIZE, 0, HEART_SIZE, HEART_SIZE);
	        }
	        mc.fontRenderer.drawString(context.heartrate+" "+I18n.format("gui.stats.bpm", new Object[0]),xPos + HEART_OFFSET + HEART_SIZE + 2,yPos+3,0xFFFFFF);
	    	mc.fontRenderer.drawString(I18n.format("gui.stats.speed", new Object[0])+": "+(context.getSpeed()*20)+" meters per second"+" Rot: "+context.rotation,xPos,yPos+HEART_SIZE,0xFFFFFF);
	    	mc.fontRenderer.drawString(I18n.format("gui.stats.resistance", new Object[0])+": "+(context.resistance),xPos,yPos+HEART_SIZE+mc.fontRenderer.FONT_HEIGHT,0xFFFFFF);
    	}
    }
}