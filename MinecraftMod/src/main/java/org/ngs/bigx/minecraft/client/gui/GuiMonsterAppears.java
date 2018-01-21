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

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiMonsterAppears extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;

	public static boolean isGuiMonsterAppearsOpened = false;
	public static boolean isGuiMonsterAppearsClosed = false;
	
	public GuiMonsterAppears(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiMonsterAppears(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
		
		System.out.println("[BiGX] GuiMonsterAppears Opened");
		
		// Start Timer
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mc.currentScreen instanceof GuiMonsterAppears)
				{
					System.out.println("[Bigx] GuiMonsterAppears timer ticks");
					Minecraft.getMinecraft().thePlayer.closeScreen();
				}
			}
		}, 3*1000);
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
	    	GL11.glScalef(2F, 2F, 2F);

			String text  = "MONSTER APPEARS!";

        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
    		fontRendererObj.drawStringWithShadow(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4, 0xFFFFFF);
		GL11.glPopMatrix();
		
		super.drawScreen(my, mx, partialTicks);
	}
	
	public int getTopMargin() {
		return 30;
	}
}
