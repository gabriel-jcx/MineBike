package org.ngs.bigx.minecraft.client.gui.hud;

import net.minecraft.util.ResourceLocation;

/**
 * Small helper class to be used with HudManager
 * HudRectangle has 5 properties.
 * x1, y1 - top left corner of the rectangle (Measured from top left of the screen)
 * x2, y2 - bottom right corner of the rectangle
 * color - create using 0xffffffff notation
 */
public class HudRectangle 
{
	public int x;
	public int y;
	public int w;
	public int h;
	
	public boolean centerX;
	public boolean centerY;
	
	public int color;
	
	/**
	 * takes: <br>
	 * starting x, y, <br>
	 * ending x, y, <br>
	 * color using 0xffffffff hex notation (or other formats this is just easy)
	 */
	public HudRectangle(int x, int y, int w, int h, int color)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.color = color;
		
	}
	
	/**
	 * takes: <br>
	 * starting x, y, <br>
	 * ending x, y, <br>
	 * color using 0xffffffff hex notation (or other formats this is just easy), <br> <br>
	 * centerX and centerY - these turn the starting x and y into relative coordinates from the center
	 * of the screen vertically or horizontally
	 * <br> 
	 * i.e. centerX is true and screen resolution is 1920x1080, and x is -20, 
	 * the rectangle's top left corner's x coordinate will be (1920/2) - 20 = 940 
	 */
	public HudRectangle(int x, int y, int w, int h, int color, boolean centerX, boolean centerY)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		this.color = color;
		
	}
}
