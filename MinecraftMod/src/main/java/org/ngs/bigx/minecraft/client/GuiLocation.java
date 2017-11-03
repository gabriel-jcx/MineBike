package org.ngs.bigx.minecraft.client;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.client.area.Area.AreaTypeEnum;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.context.BigxClientContext;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiLocation extends GuiScreen {
	
	private Minecraft mc;

	private BigxClientContext context;
	
	public GuiLocation(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiLocation(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
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

	private float lerp(float a, float b, float f) 
	{
	    return (a * (1.0f - f)) + (b * f);
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
    	String text = "";
    	
    	if (mc.thePlayer == null)
    		return;
    	
    	EntityPlayer p = mc.thePlayer;
	    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
    	int WIDTH = 200;
//    	int HEIGHT = HEART_SIZE + mc.fontRenderer.FONT_HEIGHT * 1 + 20 + 2;
    	int mcWidth = sr.getScaledWidth();
    	int mcHeight = sr.getScaledHeight();
    	xPos = mcWidth - WIDTH + 2;
    	
    	int yy = yPos+mc.fontRenderer.FONT_HEIGHT;

		float scaleX = 0.25f;
		float scaleY = 0.25f*1.5f;
		int rectX = 256;
    	int rectY = (int)(256/3f);
    	float targetAlpha = 0.0f;

    	fontRendererObj = Minecraft.getMinecraft().fontRenderer;

    	/**
    	 * Pedaling Mode Indicator Drawing
    	 */
    	//                         bike (blue),mine (red),build (green)
    	int[] colors = new int[] { 0xFF00A0FF, 0xFFFF2000, 0xFF00FF00 };
    	int chosenColor = colors[ClientEventHandler.pedalingModeState > 2 ? 2 : ClientEventHandler.pedalingModeState];
    	
		float targetY = -(rectY) * (ClientEventHandler.pedalingModeState);
	    if (ClientEventHandler.animTickSwitch < ClientEventHandler.animTickSwitchLength) {
	    	targetY = lerp(-(rectY-2) * (ClientEventHandler.pedalingModeState - 1),
	    			-(rectY) * (ClientEventHandler.pedalingModeState),
	    			(float)ClientEventHandler.animTickSwitch / ClientEventHandler.animTickSwitchLength);
	    }
	    
    	// TEXT (top of the items, fades out alongside top portion)
	    GL11.glPushMatrix();
	    	
	    	if (ClientAreaEvent.previousArea.type != AreaTypeEnum.EVENT)
	    		text = ClientAreaEvent.previousArea.name;
	    	if (text.indexOf("< ") != -1 && text.indexOf(" >") != -1)
	    		text = text.substring(text.indexOf("< ")+2, text.indexOf(" >"));
	    	int w = mcWidth/2 - 100;
		    GL11.glTranslatef((fontRendererObj.getStringWidth(text)/2 + (mcWidth/2 - (90 + fontRendererObj.getStringWidth(text)/2)))/2, mcHeight-16, 0);
	    	
		    float xScale = 1F;
		    if (fontRendererObj.getStringWidth(text) > w && fontRendererObj.getStringWidth(text) != 0)
		    	xScale = (float) w / fontRendererObj.getStringWidth(text);
		    GL11.glScalef(xScale, 1F, 1F);
	    	GL11.glEnable(GL11.GL_BLEND);
	    	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    	int a = targetAlpha < (4/255f) ? 0x04FFFFFF : (int)Long.parseLong(String.format("%02X", (int)(targetAlpha*255)) + "FFFFFF", 16);
	    	
	    	fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, 0, 0xFFFFFFFF, true);
	    	
	    GL11.glPopMatrix();
	    
	    /*
    	// BOTTOM PORTION (selected item, always on screen)
    	GL11.glPushMatrix();
    	
	    	GL11.glTranslatef(12, mcHeight - 135, 0); 
		    GL11.glScalef(scaleX, scaleY, 1.0f);
		    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		    GL11.glEnable(GL11.GL_BLEND);
		    mc.renderEngine.bindTexture(PEDALINGMODE_TEXTURE);
		    
		    
	    	drawTexturedModalRect(0, 256-86, 0, (int) targetY - 86, 256, 87);
	        
			drawRect(0, 		rectY*2,		rectX,	rectY*2 + 4, chosenColor); // top line
			drawRect(0, 		rectY*2 + 2,	4,		rectY*3 + 2, chosenColor); // left line
			drawRect(rectX-4,	rectY*2 + 2,	rectX,	rectY*3 + 2, chosenColor); // right  line
			drawRect(0, 		rectY*3 + 1,	rectX,	rectY*3 + 5, chosenColor); // bottom line
			
    	GL11.glPopMatrix();
    	*/
    	/**
    	 * END OF Pedaling Mode Indicator Drawinge
    	 */
    }
}