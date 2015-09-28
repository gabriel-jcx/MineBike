package org.ngs.bigx.minecraft.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BikeWorldData;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.quests.Quest;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiQuest extends Gui {
	
	private Minecraft mc;
	
	private ResourceLocation QUESTBOX_TEXTURE = new ResourceLocation(Main.TEXTURE_PREFIX,"textures/GUI/questbox.png");
	private int QUESTBOX_WIDTH = 101;
	private int QUESTBOX_HEIGHT = 50;
	
	private Context context;
	
	private Textbox text;
	
	public GuiQuest(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiQuest(Context c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	public void updateText() {
		Quest quest = context.getQuest();
		if (quest!=null) {
        	text = quest.getFullDescription(99,mc.fontRenderer);
		}
		else {
			text = new Textbox(99);
			text.addLine(EnumChatFormatting.DARK_RED+"No Quest",mc.fontRenderer);
		}
		
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
    	if (mc.thePlayer != null&&context.checkQuestsEnabled()) {
	    	EntityPlayer player = mc.thePlayer;
	    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	mc.renderEngine.bindTexture(QUESTBOX_TEXTURE);
	    	int WIDTH = 200;
	    	int HEIGHT = QUESTBOX_HEIGHT;
	        drawTexturedModalRect(0, yPos, 0, 0, QUESTBOX_WIDTH , QUESTBOX_HEIGHT);
	        updateText();
	        List<String> desc = text.getLines();
	        int i = 0;
	        for (String s:desc) {
	        	mc.fontRenderer.drawString(s, xPos+2, yPos+2+mc.fontRenderer.FONT_HEIGHT*i, 0x000000);
	        	i++;
	        }
	    }
    }
}