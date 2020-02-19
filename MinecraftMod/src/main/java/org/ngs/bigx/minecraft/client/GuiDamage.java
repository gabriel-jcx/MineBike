package org.ngs.bigx.minecraft.client;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.context.BigxClientContext;

//import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
//import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.StatCollector;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiDamage extends GuiScreen {

	public static final long duration = 800;
	public static final long durationQuarter = duration/4;
	public static final long durationHalf = duration/2;
	
	private static Minecraft mc;

//	private ResourceLocation QUEST_TIMER_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/gauge_bg.png");
	
	private BigxClientContext context;
	
	private static ArrayList<DamageTextItem> damageTextList;
	
	public GuiDamage(Minecraft mc) {
		super();
		this.mc = mc;
		this.damageTextList = new ArrayList<DamageTextItem>();
	}
	
	public GuiDamage(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	public static void addDamageText(int damage, int colorR, int colorG, int colorB)
	{
		Random random = new Random();
		DamageTextItem damageTextItem = new DamageTextItem();
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
		
		damageTextItem.timestamp = System.currentTimeMillis();
		damageTextItem.damage = damage;
		damageTextItem.posX = sr.getScaledWidth()/2;
		damageTextItem.posY = 80 + random.nextInt(30) - 15;
		damageTextItem.dx = random.nextInt(30) - 15;
		damageTextItem.dy = random.nextInt(40) - 20;
		damageTextItem.colorR = colorR;
		damageTextItem.colorG = colorG;
		damageTextItem.colorB = colorB;
		
		if(damageTextItem.dx > 0)
		{
			damageTextItem.dx += 20;
		}
		else if(damageTextItem.dx < 0)
		{
			damageTextItem.dx += -20;
		}
		
		synchronized (damageTextList) {
			damageTextList.add(damageTextItem);
		}
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
    	ArrayList<DamageTextItem> removeList = new ArrayList<DamageTextItem>();

    	if (mc.player != null) {
	    	EntityPlayer p = mc.player;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int WIDTH = 200;
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	xPos = mcWidth - WIDTH + 2;
	    	double percentBig = context.timeSpent;
	    	double percentSmall = context.timeSpentSmall;

			synchronized (damageTextList) {
		    	for(DamageTextItem damageTextItem : this.damageTextList)
		    	{
		    		int color = 0xFFFFFF;
		    		float scalefactor = (damageTextItem.damage * 0.15f) + 4f;
		    		
		    		long currenttime = System.currentTimeMillis();
		    		
		    		if( (System.currentTimeMillis() - damageTextItem.timestamp) > GuiDamage.duration )
		    		{
		    			removeList.add(damageTextItem);
		    			continue;
		    		}
		    		
		    		float relativeTimeContant = (float)(currenttime - damageTextItem.timestamp) / (duration);
		    		
		    		if(((currenttime - damageTextItem.timestamp)%durationHalf) >= durationQuarter)
		    		{
		    			color = ((damageTextItem.colorR & 0xFF) << 16) | ((damageTextItem.colorG & 0xFF) << 8) | (damageTextItem.colorB & 0xFF);
		    		}
		    		
			    	GL11.glPushMatrix();
					    GL11.glTranslatef(damageTextItem.posX, damageTextItem.posY, 0);
					    
					    // Bouncing Animation
					    int currentYPosition = (int)((relativeTimeContant*relativeTimeContant - relativeTimeContant)*(damageTextItem.damage*3 + 50));
	
					    text = "" + damageTextItem.damage;
	
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	
			    		GL11.glScalef(scalefactor, scalefactor, scalefactor);
			    		fontRendererObj.drawString(text, (int)(damageTextItem.dx*relativeTimeContant)-fontRendererObj.getStringWidth(text)/2, currentYPosition + (int)(damageTextItem.dy*relativeTimeContant), 0);
			    		fontRendererObj.drawString(text, (int)(damageTextItem.dx*relativeTimeContant)-fontRendererObj.getStringWidth(text)/2 - 1, currentYPosition + (int)(damageTextItem.dy*relativeTimeContant) - 1, color);
	
			        	GL11.glPopMatrix();
		        	GL11.glPopMatrix();
		    	}
			}
    	}
    }
}