package org.ngs.bigx.minecraft.client.gui.hud;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

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
	    int temp;
	    
	    //swaps if it's in the wrong orientation
	    if (x1 < x2)
	    {
	        temp = x1;
	        x1 = x2;
	        x2 = temp;
	    }

	    if (y1 < y2)
	    {
	        temp = y1;
	        y1 = y2;
	        y2 = temp;
	    }

	    //rgba masks
	    float r = (float)(color >> 24 & 255) / 255.0F;
	    float g = (float)(color >> 16 & 255) / 255.0F;
	    float b = (float)(color >> 8  & 255) / 255.0F;
	    float a = (float)(color >> 0  & 255) / 255.0F;
	    
	    //gl stuff
	    Tessellator tessellator = Tessellator.instance;
	    GL11.glPushAttrib(GL11.GL_COLOR);
	    GL11.glPushMatrix();
		    GL11.glEnable(GL11.GL_BLEND);
		    GL11.glDisable(GL11.GL_TEXTURE_2D);
			    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			    //store the color so we can restore it back
//			    FloatBuffer currentColor = BufferUtils.createFloatBuffer(16);
//			    GL11.glGetFloat(GL11.GL_CURRENT_COLOR, currentColor);
			    GL11.glColor4f(r, g, b, a);
			    //this is where it becomes drawn
				    tessellator.startDrawingQuads();
				    tessellator.addVertex((double)x1, (double)y2, 0.0D);
				    tessellator.addVertex((double)x2, (double)y2, 0.0D);
				    tessellator.addVertex((double)x2, (double)y1, 0.0D);
				    tessellator.addVertex((double)x1, (double)y1, 0.0D);
				    tessellator.draw();
//			    GL11.glColor4f(currentColor.get(0), currentColor.get(1), currentColor.get(2), currentColor.get(3));
		    GL11.glEnable(GL11.GL_TEXTURE_2D);
		    GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	
	public void drawString(HudString hudString)
	{	
		
		//get current texture and then set it back at the end of this function
		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		//translate to where it is going to be displayed, then scale it
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
		GL11.glPushAttrib(GL11.GL_COLOR);
					GL11.glTranslatef(
						 (int) (hudString.x + (hudString.centerX ? 
							mcWidth/2-fontRendererObj.getStringWidth(hudString.text)/2 * hudString.scale
							: 0)),
							hudString.y + (hudString.centerY ? mcHeight/2 : 0), 
							0.0f);
				
					GL11.glScalef(hudString.scale, hudString.scale, hudString.scale);
//					this breaks the other textures
					if (hudString.shadow)
					{
						fontRendererObj.drawStringWithShadow(	
								hudString.text, 
								0, 
								0, 
								hudString.color >> 8);
					}
					else
					{
						fontRendererObj.drawString(	
								hudString.text, 
								0, 
								0, 
								hudString.color >> 8);
					}
					GL11.glScalef(1.0f, 1.0f, 1.0f);
		GL11.glPopAttrib();
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	
	public void drawTexture(HudTexture hudTexture)
	{
		//draw the hudTexture
		GL11.glPushMatrix();	
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
		GL11.glPushAttrib(GL11.GL_COLOR);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, (hudTexture.alpha * 1.0f / 255.0f));
				Tessellator tessellator = Tessellator.instance;
			    GL11.glEnable(GL11.GL_TEXTURE_2D);
					this.mc.renderEngine.bindTexture(hudTexture.resourceLocation);	
				    //this is where it becomes drawn
				    tessellator.startDrawingQuads();
				    tessellator.addVertexWithUV((double)hudTexture.x, (double)hudTexture.y, 0.0D, 0.0d, 0.0d);
				    tessellator.addVertexWithUV((double)hudTexture.x, (double)hudTexture.y + hudTexture.h, 0.0D, 0.0d, 1.0d);
				    tessellator.addVertexWithUV((double)hudTexture.x + hudTexture.w, (double)hudTexture.y + hudTexture.h, 0.0D, 1.0d, 1.0d);
				    tessellator.addVertexWithUV((double)hudTexture.x + hudTexture.w, (double)hudTexture.y, 0.0D, 1.0d, 0.0d);
				    tessellator.draw();
			    GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	
	//RECTANGLES
	public static void unregisterRectangle(HudRectangle rectangle)
	{
		rectangles.remove(rectangle);
	}
	
	public static void registerRectangle(HudRectangle rectangle)
	{
		if (!rectangles.contains(rectangle))
			rectangles.add(rectangle);
		else
			System.out.println("Rectangle already registered!");
	}
	
	//STRINGS
	public static void unregisterString(HudString string)
	{
		strings.remove(string);
	}
	
	public static void registerString(HudString string)
	{
		if (!strings.contains(string))
			strings.add(string);
		else
			System.out.println("String already registered!");
	}
	
	//TEXTURES
	public static void unregisterTexture(HudTexture texture)
	{
		textures.remove(texture);
	}
	
	public static void registerTexture(HudTexture texture)
	{
		if (!textures.contains(texture))
			textures.add(texture);
		else
			System.out.println("Texture already registered!");
	}
	
	private void updateResolution()
	{
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
    	mcWidth = sr.getScaledWidth();
    	mcHeight = sr.getScaledHeight();
	}
	
	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) 
	{
	    if(event.isCancelable() || event.type != event.type.TEXT)
	    {
	      return;
	    }
	    
	    updateResolution();

    	for(int i = 0; i < rectangles.size(); i++)
    	{
    		drawRect(rectangles.get(i));
    	}
    	
    	for(int i = 0; i < textures.size(); i++)
    	{
    		drawTexture(textures.get(i));
    	}
    	
    	for(int i = 0; i < strings.size(); i++)
    	{

    		drawString(strings.get(i));
    	}
	}
}
