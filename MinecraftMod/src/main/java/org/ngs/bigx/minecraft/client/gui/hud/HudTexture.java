package org.ngs.bigx.minecraft.client.gui.hud;

import org.ngs.bigx.minecraft.BiGX;

import net.minecraft.util.ResourceLocation;

/**
 * Small helper class to be used with HudManager
 * HudRectangle has 5 properties.
 * x1, y1 - top left corner of the rectangle (Measured from top left of the screen)
 * x2, y2 - bottom right corner of the rectangle
 * color - create using 0xffffffff notation
 */
public class HudTexture 
{
	public int x;
	public int y;
	public int w;
	public int h;
	public boolean centerX;
	public boolean centerY;
	public ResourceLocation resourceLocation;
	
	/**
	 * takes: <br>
	 * top left x, y, <br>
	 * width, height, <br>
	 * location - should be located in assets.minebike.textures.gui
	 * , and put in constructor like this: textures/GUI/*.png
	 */
	public HudTexture(int x, int y, int w, int h, String location)
	{
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
		
		this.centerX = false;
		this.centerY = false;
		
		this.resourceLocation = new ResourceLocation(BiGX.TEXTURE_PREFIX, location);
		
	}
	
	/**
	 * takes: <br>
	 * top left x, y, <br>
	 * width, height, <br>
	 * location - should be located in assets.minebike.textures.gui
	 * , and put in constructor like this: "textures/GUI/*.png"<br> <br>
	 * centerX and centerY - these turn the starting x and y into relative coordinates from the center
	 * of the screen vertically or horizontally
	 * <br> 
	 * i.e. centerX is true and screen resolution is 1920x1080, and x is -20, 
	 * the rectangle's top left corner's x coordinate will be (1920/2) - 20 = 940 
	 */
	public HudTexture(int x, int y, int w, int h, String location, boolean centerX, boolean centerY)
	{
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.resourceLocation = new ResourceLocation(BiGX.TEXTURE_PREFIX, location);
		
	}
}
