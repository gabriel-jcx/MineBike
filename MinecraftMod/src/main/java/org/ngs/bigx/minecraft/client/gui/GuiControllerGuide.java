package org.ngs.bigx.minecraft.client.gui;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.SetChangeListener;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.dictionary.objects.game.BiGXGameTag;
import org.ngs.bigx.dictionary.protocol.Specification;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import com.ibm.icu.impl.ICUService.Key;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noppes.npcs.client.ClientEventHandler;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiControllerGuide extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;
	private ResourceLocation CONTROLLERMENU_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/joypadscreen.png");
	
	public GuiControllerGuide(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiControllerGuide(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
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
			// Draw controller picture
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(CONTROLLERMENU_TEXTURE);
	        drawTexturedModalRect(mcWidth/2 - 128, mcHeight/2 - 128, 0, 0, 256 , 256);
		GL11.glPopMatrix();
		
		super.drawScreen(mx, my, partialTicks);
	}
}
