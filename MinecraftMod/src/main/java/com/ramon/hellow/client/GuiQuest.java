package com.ramon.hellow.client;

import org.lwjgl.opengl.GL11;

import com.ramon.hellow.BikeWorldData;
import com.ramon.hellow.Context;
import com.ramon.hellow.Main;
import com.ramon.hellow.worldgen.WorldStructure;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiQuest extends Gui {
	
	private Minecraft mc;
	
	private ResourceLocation QUESTBOX_TEXTURE = new ResourceLocation(Main.TEXTURE_PREFIX+":/textures/GUI/questbox.png");
	private int QUESTBOX_WIDTH = 100;
	private int QUESTBOX_HEIGHT = 50;
	
	private Context context;
	
	public GuiQuest(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiQuest(Context c,Minecraft mc) {
		this(mc);
		context = c;
	}

	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if(event.isCancelable() || event.type != event.type.TEXT)
	    {      
	      return;
	    }
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
    	int xPos = 0;
    	int yPos = sr.getScaledHeight()-QUESTBOX_HEIGHT;
    	if (mc.thePlayer != null) {
	    	EntityPlayer player = mc.thePlayer;
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	mc.renderEngine.bindTexture(QUESTBOX_TEXTURE);
	    	int WIDTH = 200;
	    	int HEIGHT = QUESTBOX_HEIGHT;
	        drawTexturedModalRect(0, yPos, 0, 0, QUESTBOX_WIDTH , QUESTBOX_HEIGHT);
	        mc.fontRenderer.drawString("Current Quest: ",xPos+2,yPos+2,0x000000);
	        mc.fontRenderer.drawString("Slay: "+100+"x §2Zombies",xPos+2,yPos+2+mc.fontRenderer.FONT_HEIGHT,0x000000);
	    }
    }
}