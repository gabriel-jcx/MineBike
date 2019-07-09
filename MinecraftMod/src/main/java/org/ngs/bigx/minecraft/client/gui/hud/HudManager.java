package org.ngs.bigx.minecraft.client.gui.hud;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.client.gui.GuiChapter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HudManager extends GuiScreen 
{
	//holds the data for the rectangles
	public static ArrayList<HudRectangle> rectangles = new ArrayList<HudRectangle>();
	public static ArrayList<HudString> strings = new ArrayList<HudString>();
	public static ArrayList<HudTexture> textures = new ArrayList<HudTexture>();
	
	private static Minecraft staticMC;
	private static int mcWidth;
	private static int mcHeight;
	
	public HudManager(Minecraft mc) {
		super();
		this.mc = mc;
		staticMC = mc;
	}
	
	public static void drawRect(HudRectangle rect)
	{
		int offSetX = (rect.centerX ? mcWidth/2 : 0);
		int offSetY = (rect.centerY ? mcHeight/2 : 0);
		drawRect(
				rect.x + offSetX,
				rect.y + offSetY,
				rect.x + rect.w + offSetX,
				rect.y + rect.h + offSetY,
				rect.color);
	}
	
	/*
	 * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
	 */
	public static void drawRect(int x1, int y1, int x2, int y2, int color)
	{
	    int j1;

	    if (x1 < x2)
	    {
	        j1 = x1;
	        x1 = x2;
	        x2 = j1;
	    }

	    if (y1 < y2)
	    {
	        j1 = y1;
	        y1 = y2;
	        y2 = j1;
	    }

	    //rgba masks
	    float r = (float)(color >> 24 & 255) / 255.0F;
	    float g = (float)(color >> 16 & 255) / 255.0F;
	    float b = (float)(color >> 8  & 255) / 255.0F;
	    float a = (float)(color >> 0  & 255) / 255.0F;
	    
	    Tessellator tessellator = Tessellator.instance;
	    GL11.glPushMatrix();
		    GL11.glEnable(GL11.GL_BLEND);
		    GL11.glDisable(GL11.GL_TEXTURE_2D);
			    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			    
			    FloatBuffer currentColor = BufferUtils.createFloatBuffer(16);
			    GL11.glGetFloat(GL11.GL_CURRENT_COLOR, currentColor);
			    GL11.glColor4f(r, g, b, a);
				    tessellator.startDrawingQuads();
				    tessellator.addVertex((double)x1, (double)y2, 0.0D);
				    tessellator.addVertex((double)x2, (double)y2, 0.0D);
				    tessellator.addVertex((double)x2, (double)y1, 0.0D);
				    tessellator.addVertex((double)x1, (double)y1, 0.0D);
				    tessellator.draw();
			    GL11.glColor4f(currentColor.get(0), currentColor.get(1), currentColor.get(2), currentColor.get(3));
		    GL11.glEnable(GL11.GL_TEXTURE_2D);
		    GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public void drawString(HudString hudString)
	{
		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		fontRendererObj.drawStringWithShadow(
				hudString.text, 
				hudString.x + (hudString.centerX ? 
						mcWidth/2-fontRendererObj.getStringWidth(hudString.text)/2
						: 0), 
				hudString.y + (hudString.centerY ? mcHeight/2 : 0), 
				0xFFFFFF);
	}
	
	public void drawTexture(HudTexture hudTexture)
	{
		GL11.glPushMatrix();
		GL11.glScalef(.5f, .5f, .5f);
		
			this.mc.renderEngine.bindTexture(hudTexture.resourceLocation);
//			drawTexturedModalRect(mcWidth -64 - 10, 50 + 30, 64*(3-1), 0,  64 , 64);
			drawTexturedModalRect(
					hudTexture.x + (hudTexture.centerX ? mcWidth/2 : 0),
					hudTexture.y + (hudTexture.centerY ? mcHeight/2 : 0),
					0, 0, 
					hudTexture.w, 
					hudTexture.h);

		GL11.glPopMatrix();
	}
	
	//RECTANGLES
	public static void unregisterRectangle(HudRectangle rectangle)
	{
		rectangles.remove(rectangle);
	}
	
	public static void registerRectangle(HudRectangle rectangle)
	{
		rectangles.add(rectangle);
	}
	
	//STRINGS
	public static void unregisterString(HudString string)
	{
		strings.remove(string);
	}
	
	public static void registerString(HudString string)
	{
		strings.add(string);
	}
	
	//TEXTURES
	public static void unregisterTexture(HudTexture texture)
	{
		textures.remove(texture);
	}
	
	public static void registerTexture(HudTexture texture)
	{
		textures.add(texture);
	}
	
	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) 
	{
	    if(event.isCancelable() || event.type != event.type.TEXT)
	    {      
	      return;
	    }
	    
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
    	mcWidth = sr.getScaledWidth();
    	mcHeight = sr.getScaledHeight();
    	
		
		for(HudRectangle rect : rectangles)
		{
			drawRect(rect);
		}
		
		for(HudString string : strings)
		{
			drawString(string);
		}
		
		for(HudTexture texture : textures)
		{
			drawTexture(texture);
		}
//		drawRect(0,0, 20, 20, 0xffffffff);
	}
}
