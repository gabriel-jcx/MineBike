package org.ngs.bigx.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;

import com.ibm.icu.impl.ICUService.Key;

//import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiMonsterReadyFight extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;
	private static String text = "";
	
	public GuiMonsterReadyFight(Minecraft mc) {
		super();
		this.mc = mc;
		text = "READY";
	}
	
	public GuiMonsterReadyFight(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
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
				mc = Minecraft.getMinecraft();
				if(mc.currentScreen == null) {
					return;
				}
				else if(mc.currentScreen instanceof GuiMonsterReadyFight)
				{
			        if(mc.currentScreen != null) {
			        	Minecraft.getMinecraft().player.closeScreen();
			        }
				}
			}
		}, 2*1000);
        
		// Start Timer
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				text = "GO and FIGHT!";
			}
		}, 1*1000);
	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();
	};
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		mc = Minecraft.getMinecraft();
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
		int mcWidth = sr.getScaledWidth();
    	int mcHeight = sr.getScaledHeight();
    	
		drawRect(0, 0, mcWidth, mcHeight, 0xCC000000); // The Box on top

		GL11.glPushMatrix();
		    GL11.glTranslatef(mcWidth/2, 0, 0);
	    	GL11.glScalef(2F, 2F, 2F);

        	fontRendererObj = mc.fontRenderer;
    		fontRendererObj.drawStringWithShadow(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4, 0xFFFFFF);
		GL11.glPopMatrix();
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}
}
