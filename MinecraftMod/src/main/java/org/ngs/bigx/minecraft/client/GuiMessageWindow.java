package org.ngs.bigx.minecraft.client;

import java.text.DecimalFormat;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestRunFromMummy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiMessageWindow extends GuiScreen {	
	private Minecraft mc;

	private ResourceLocation MESSAGE_WINDOW_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/dialog.png");
	private ResourceLocation GOLDBAR_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/goldbar.png");
	
	private Context context;
	private static long timestampLastShowWindowCall = 0;
	private static long timestampLastShowGoldbarCall = 0;
	private static String[] textLinesToBeShown = {};
	
	private final long durationShowWindow = 5000;
	private final long durationFadeIn = 250;
	private final long durationFadeOut = 1000;
	
	public GuiMessageWindow(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiMessageWindow(Context c,Minecraft mc) {
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
	
	public static void showMessage(String message)
	{
		// Line Break Every 30 Chars
		textLinesToBeShown = message.split("\n");
		timestampLastShowWindowCall = System.currentTimeMillis();
	}
	
	public static void showGoldBar(String message)
	{
		// Line Break Every 30 Chars
		textLinesToBeShown = message.split("\n");
		timestampLastShowGoldbarCall = System.currentTimeMillis();
		timestampLastShowWindowCall = timestampLastShowGoldbarCall;
	}

	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if(event.isCancelable() || event.type != event.type.TEXT || !context.modEnabled)
	    {      
	      return;
	    }
	    
    	FontRenderer fontRendererObj;

    	if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	long timeDifference = (System.currentTimeMillis() - timestampLastShowWindowCall);
	    	float windowAlphaValue = 0f;
	    	int windowAlphaValueInBytes = 0;
	    	
	    	if(timestampLastShowWindowCall == 0)
	    	{
	    		return;
	    	}
	    	else if(timeDifference < durationShowWindow)
	    	{
	    		if(timeDifference <= durationFadeIn)
	    		{
	    			windowAlphaValue = ((float)timeDifference/(float)durationFadeIn);
	    		}
	    		else if(timeDifference > (durationShowWindow-durationFadeOut))
	    		{
	    			windowAlphaValue = (float)(durationShowWindow-timeDifference)/(float)durationFadeOut;
	    		}
	    		else
	    		{
	    			windowAlphaValue = 1f;
	    		}
	    		
	    		// Convert windowAlphaValue to two bytes value
	    		windowAlphaValueInBytes = (int) (255 - (255f / windowAlphaValue));
	    		
		    	GL11.glPushMatrix();
		    	
				    GL11.glTranslatef(mcWidth/2, mcHeight-88f, 0); 
				    GL11.glColor4f(1.0F, 1.0F, 1.0F, windowAlphaValue);
				    GL11.glEnable(GL11.GL_BLEND);
				    mc.renderEngine.bindTexture(MESSAGE_WINDOW_TEXTURE);
			        drawTexturedModalRect(-80, -20, 0, 0, 160 , 40);
	        	
		        	for(int i=0; i<textLinesToBeShown.length; i++)
		        	{
		        		String text = textLinesToBeShown[i];
		
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawString(text, -65, -15 + i*10, 0xFFFFFF + ((windowAlphaValueInBytes&0xFF)<<24));
		        	}
	        	
	        	GL11.glPopMatrix();
	        	
	        	if(timestampLastShowGoldbarCall != 0)
	        	{
	        		GL11.glPushMatrix();
			    	
					    GL11.glTranslatef(mcWidth/2, mcHeight-165f, 0); 
					    GL11.glColor4f(1.0F, 1.0F, 1.0F, windowAlphaValue);
					    GL11.glEnable(GL11.GL_BLEND);
					    mc.renderEngine.bindTexture(GOLDBAR_TEXTURE);
				        drawTexturedModalRect(-75, -53, 0, 0, 150 , 105);
		        	
		        	GL11.glPopMatrix();
	        	}
	    	}
    	}
    	else{
    		timestampLastShowWindowCall = 0;
    		timestampLastShowGoldbarCall = 0;
    	}
    }
}