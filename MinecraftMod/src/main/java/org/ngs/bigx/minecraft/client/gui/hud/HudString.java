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
public class HudString 
{
	public int x;
	public int y;
	public boolean shadow;
	public boolean centerX;
	public boolean centerY;
	public float scale;
	public String text;
	public int color;
	
	/**
	 * takes: <br>
	 * top left x, y, <br>
	 * location - should be located in assets.minebike.textures.gui
	 * , and put in constructor like this: textures/GUI/*.png
	 */
	public HudString(int x, int y, String text)
	{
		this.x = x;
		this.y = y;
		
		this.scale = 1.0f;
		
		this.centerX = false;
		this.centerY = false;
		
		this.color = 0xffffffff;
		
		this.text = text;
		this.shadow = true;
	}
	
	/**
	 * takes: <br>
	 * top left 
	 * x, y, <br>
	 * text - the text to be drawn<br> <br>
	 * centerX and centerY - these turn the starting x and y into relative coordinates from the center
	 * of the screen vertically or horizontally
	 * <br> 
	 * i.e. centerX is true and screen resolution is 1920x1080, and x is -20, 
	 * the rectangle's top left corner's x coordinate will be (1920/2) - 20 = 940 
	 */
	public HudString(int x, int y, String text, boolean centerX, boolean centerY)
	{
		this.x = x;
		this.y = y;
		
		this.scale = 1.0f;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.text = text;
		
		this.color = 0xffffffff;
		this.shadow = true;
	}
	
	/**
	 * takes: <br>
	 * top left 
	 * x, y, <br>
	 * text - the text to be drawn<br> <br>
	 * centerX and centerY - these turn the starting x and y into relative coordinates from the center
	 * of the screen vertically or horizontally <br>
	 * scale - the scale of the font to be drawn at, larger than 1.0f makes it bigger
	 * <br> 
	 * i.e. centerX is true and screen resolution is 1920x1080, and x is -20, 
	 * the rectangle's top left corner's x coordinate will be (1920/2) - 20 = 940 
	 */
	public HudString(int x, int y, String text, float scale, boolean centerX, boolean centerY)
	{
		this.x = x;
		this.y = y;
		
		this.scale = scale;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.text = text;
		
		this.color = 0xffffffff;
		this.shadow = true;
	}
	
	/**
	 * takes: <br>
	 * top left 
	 * x, y, <br>
	 * text - the text to be drawn<br> <br>
	 * centerX and centerY - these turn the starting x and y into relative coordinates from the center
	 * of the screen vertically or horizontally <br>
	 * scale - the scale of the font to be drawn at, larger than 1.0f makes it bigger
	 * color - in the format 0xRRGGBB__ the last two hex digits don't matter
	 * <br> 
	 * i.e. centerX is true and screen resolution is 1920x1080, and x is -20, 
	 * the rectangle's top left corner's x coordinate will be (1920/2) - 20 = 940 
	 */
	public HudString(int x, int y, String text, float scale, int color, boolean centerX, boolean centerY)
	{
		this.x = x;
		this.y = y;
		
		this.scale = scale;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.text = text;
		
		this.color = color;
		this.shadow = true;
	}
}
