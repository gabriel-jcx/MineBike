package org.ngs.bigx.minecraft.client;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.utility.Pair;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiMessageWindow extends GuiScreen {	
	private Minecraft mc;

	//TODO: CHANGE TEXTURE LOCATIONS
	public static ResourceLocation MESSAGE_WINDOW_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/dialog.png");
	public static ResourceLocation GOLDBAR_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/goldbar.png");
	public static ResourceLocation BOOK_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/book.png");
	public static ResourceLocation PEDAL_FORWARD_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/pedal-forward.png");
	public static ResourceLocation PEDAL_BACKWARD_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/pedal-backward.png");
	public static ResourceLocation JUMP_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/jump.png");
	public static ResourceLocation MINE_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/icon-break.png");
	public static ResourceLocation BUILD_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/icon-build.png");
	public static ResourceLocation HIT_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/aimandhit.png");
	public static ResourceLocation TALK_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/aimandtalk.png");
	public static ResourceLocation DASH_JUMP_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/run-jump.png");
	public static ResourceLocation POTION_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/potion.png");
	public static ResourceLocation CHEST_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/chest.png");
	
	private BigxClientContext context;
	private static long timestampLastShowWindowCall = 0;
	private static String[] textLineArray = null;
	private static ResourceLocation currentImage = null;
	
	// Fixed animation times for various stages of showing messages/images
//	private final long durationShowWindow = 5000;
	private final long durationShowWindow = 8000;
	private final long durationFadeIn = 250;
	private final long durationFadeOut = 1000;
	private final long delayReplaceText = 2500;
	
	// Enable for image that use the 256x256 limitation, otherwise disable
	private static boolean isImageFixed;
	
	private static ArrayList<Pair<String, ResourceLocation>> messages = new ArrayList<Pair<String, ResourceLocation>>();
	
	public GuiMessageWindow(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiMessageWindow(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	/**
	 * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
	 */
	public static void drawRect(int par0, int par1, int par2, int par3, int par4) {
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
	
	public static void updateMessageFromQueue() {
		if (messages == null)
			return;
		
		if (messages.size() == 0)
			return;
		
		Pair message = messages.remove(0);
		
		if(message == null)
			return;
		
		String messageText = (String) message.getKey();
		ResourceLocation messageImage = (ResourceLocation) message.getValue();
		
		// Line break every 30 chars
		textLineArray = messageText.split("\n");
		timestampLastShowWindowCall = System.currentTimeMillis();
		
		currentImage = messageImage;
	}
	
	public static void showMessage(String message) {
		messages.add(new Pair<String, ResourceLocation>(message, null));
		
		if (timestampLastShowWindowCall == 0)
			timestampLastShowWindowCall = System.currentTimeMillis();
	}
	
	public static void showMessageAndImage(String message, ResourceLocation image) {
		showMessageAndImage(message, image, true);
	}
	
	public static void showMessageAndImage(String message, ResourceLocation image, boolean fixed) {
		messages.add(new Pair<String, ResourceLocation>(message, image));
		
		isImageFixed = fixed;
		
		if (timestampLastShowWindowCall == 0)
			timestampLastShowWindowCall = System.currentTimeMillis();
	}

	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if (event.isCancelable() || event.type != event.type.TEXT || !context.modEnabled)
	    {      
	      return;
	    }
	    
    	FontRenderer fontRendererObj;

    	if (mc.thePlayer != null)
    	{
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	long timeDifference = (System.currentTimeMillis() - timestampLastShowWindowCall);
	    	float windowAlphaValue = 0f;
	    	
	    	if (timestampLastShowWindowCall == 0)
	    		return;
	    	
	    	if (textLineArray == null)
	    	{
	    		updateMessageFromQueue();
	    	}
	    	else if (messages.size() != 0)
	    	{
	    		if (timeDifference > delayReplaceText)
	    		{
	    			updateMessageFromQueue();
	    	    	timeDifference = (System.currentTimeMillis() - timestampLastShowWindowCall);
	    		}
	    	}
	    	
	    	if (textLineArray == null)
	    	{
	    		return;
	    	}
	    	
	    	if (timeDifference < durationShowWindow)
	    	{
	    		if (timeDifference <= durationFadeIn)
	    		{
	    			windowAlphaValue = ((float)timeDifference/(float)durationFadeIn);
	    		}
	    		else if (timeDifference > (durationShowWindow-durationFadeOut))
	    		{
	    			windowAlphaValue = (float)(durationShowWindow-timeDifference)/(float)durationFadeOut;
	    		}
	    		else
	    		{
	    			windowAlphaValue = 1f;
	    		}
	    		
		    	GL11.glPushMatrix();
		    	
				    GL11.glTranslatef(mcWidth/2, mcHeight-88f, 0); 
				    GL11.glColor4f(1.0F, 1.0F, 1.0F, windowAlphaValue);
				    GL11.glEnable(GL11.GL_BLEND);
				    mc.renderEngine.bindTexture(MESSAGE_WINDOW_TEXTURE);
			        drawTexturedModalRect(-80, -20, 0, 0, 160 , 40);
	        	
		        	for (int i = 0; i < textLineArray.length; i++)
		        	{
		        		String text = textLineArray[i];
		
			        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			        	// If alpha for text drops below 4, for some reason the text gets shown as full opacity (visible) for the small time it's still visible.
			        	int alpha = windowAlphaValue < (4/255f) ? 0x04FFFFFF : (int)Long.parseLong(String.format("%02X", (int)(windowAlphaValue*255)) + "FFFFFF", 16);
			    		fontRendererObj.drawString(text, -65, -15 + i*10, alpha);
		        	}
	        	
	        	GL11.glPopMatrix();
	        	
	        	if (currentImage != null)
	        	{
	        		if(isImageFixed) {
	        			// OLD WAY - Fixing images to a 256x256 transparent image and placing them somewhere on there
	        			GL11.glPushMatrix();
				    	
						    GL11.glTranslatef(mcWidth/2, mcHeight-165f, 0); 
						    GL11.glColor4f(1.0F, 1.0F, 1.0F, windowAlphaValue);
						    GL11.glEnable(GL11.GL_BLEND);
						    mc.renderEngine.bindTexture(currentImage);
					        drawTexturedModalRect(-75, -53, 0, 0, 150 , 105);
		        	
				        GL11.glPopMatrix();
	        		} else {
	        			// NEW WAY - Scaling and placing images center of the screen regardless of resolution
	        			try {
	        				GL11.glPushMatrix();
	        				
	        					// Obtain image height & width to determine which is larger, for scaling purposes
		        				InputStream stream = mc.getResourceManager().getResource(currentImage).getInputStream();
		        				Image image = ImageIO.read(stream);
		        				int height = image.getHeight(null);
		        				int width = image.getWidth(null);
		        				
		        				float scaleX = width > height ? 0.45f : 0.45f * ((float)width/height);
		        				float scaleY = height > width ? 0.45f : 0.45f * ((float)height/width);
		        				
			        			GL11.glTranslatef(mcWidth/2, mcHeight/2, 0); 
							    GL11.glColor4f(1.0F, 1.0F, 1.0F, windowAlphaValue);
							    GL11.glScalef(scaleX, scaleY, 1f);
							    GL11.glEnable(GL11.GL_BLEND);
							    mc.renderEngine.bindTexture(currentImage);
						        drawTexturedModalRect(-128, -256+24, 0, 0, 256, 256);
	        			
					        GL11.glPopMatrix();
	        			} catch (IOException e) {
	        				e.printStackTrace();
	        			}
	        			
	        		}
	        		
		        }
	    	}
	    	else
	    	{
	    		timestampLastShowWindowCall = 0;
	    		textLineArray = null;
	    	}
    	}
    	else
    	{
    		timestampLastShowWindowCall = 0;
    		textLineArray = null;
    	}
    }
}