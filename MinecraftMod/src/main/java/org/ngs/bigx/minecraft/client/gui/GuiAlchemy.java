package org.ngs.bigx.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiChasingQuest.ChasingQuestDifficultyEnum;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;

import com.ibm.icu.impl.ICUService.Key;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiAlchemy extends GuiScreen {
	private Minecraft mc;
	private BigxClientContext context;

	private int boxSize = 27;
//	private static boolean isKOTimeout = false;
//	private static boolean isVictoryMsgTimeout = false;
//
//	private String victoryMessageLine1 = "VICTORY!";
//	private String victoryMessageLineExp = "";
//	private String victoryMessageLineGold = "";
//	private String victoryMessageLineExtraGold = "";
//	private String victoryMessageLineSpecialItem = "";
//	private String victoryMessageLine2 = "Pick up the items!";
//	private String hintMessageLineTitle = "HINT!";
//	private String hintMessageLine1 = "Exchanged gold bars for useful items at Market Place";
//	private String hintMessageLine2 = "Spend your gold wisely!";
//
	public enum AlchemyGuiMode {
	    INITIAL, INPROGRESS, FINAL 
	}
	
	
	private ResourceLocation ALCHEMIST_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemist.png");
	private ResourceLocation ALCHEMY_ON_PROGRESS_BG_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemy-icons-bg.png");
	private ResourceLocation ALCHEMY_ON_PROGRESS_DONE_ICON_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemy-icons.png");
	private ResourceLocation tempItemTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/originalIcons/items/chest.png");
	
	private AlchemyGuiMode alchemyGuiMode = AlchemyGuiMode.INITIAL;
	private CustomGuiButton cbtn;
//	
//	private static Object guiVictoryLock = new Object();
	
	public GuiAlchemy(Minecraft mc) {
		super();
		this.mc = mc;
		this.alchemyGuiMode = AlchemyGuiMode.INITIAL;
	}
	
	public GuiAlchemy(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.clear();
		ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
    	int mcWidth = sr.getScaledWidth();
    	int mcHeight = sr.getScaledHeight();
    	
		this.cbtn = new CustomGuiButton(1, width - 170 , height - 45 , 150 , 40 , 
				"", "textures/GUI/startbutton.png");
		this.buttonList.add(cbtn);
		
		// Buttons for the item selection
		for(int i=0; i<6; i++)
		{
			for(int j=0; j<6; j++)
			{
				//mcWidth/2 + 35, 20, 0
				CustomGuiButton customButton = new CustomGuiButton(i*10 + j + 10, mcWidth/2 + 35 + j * this.boxSize + 1 , 20 + i * this.boxSize + 1 , this.boxSize-3 , this.boxSize-3 , 
					"", "textures/GUI/transparent.png");
				this.buttonList.add(customButton);
			}
		}
	}
	
	@Override
	public void updateScreen() 
	{
		super.updateScreen();
	};
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	int i=0, j=0;
	    	
	    	GL11.glPushMatrix();
				drawRect(0, 0, mcWidth, mcHeight, 0xCC000000); // The Background
			GL11.glPopMatrix();
			
			String text = "";
	    	
	    	GL11.glPushMatrix();
				// Draw controller picture
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	
	    		/**
	    		 * Draws an alchemist
	    		 */
			    mc.renderEngine.bindTexture(ALCHEMIST_TEXTURE);
		        drawTexturedModalRect(20, 40, 0, 0, 125, 182);
	        
	    		switch(this.alchemyGuiMode)
	    		{
	    		case INITIAL:
	    			cbtn.visible = true;
	    			GL11.glPushMatrix();
	    		    	GL11.glTranslatef(mcWidth/2 + 35, 20, 0); 

	    		    	/**
	    		    	 * Display Items in the inventory
	    		    	 */
	    				for(i=0; i<6; i++)
	    				{
	    					for(j=0; j<6; j++)
	    					{
	    						drawRect(j*boxSize, i*boxSize, (j+1)*boxSize - 1, (i+1)*boxSize - 1, 0xFF4B380C); // The Boundary for the items
	    						drawRect(j*boxSize + 1, i*boxSize + 1, (j+1)*boxSize - 2, (i+1)*boxSize - 2, 0xAA999999); // The Background for the items
	    	    		    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

	    	    		    	EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	    	    		    	ItemStack item = player.inventory.getStackInSlot(i*6 + j);
    	    		            if(item != null) {
    	    	    		    	UniqueIdentifier UID = GameRegistry.findUniqueIdentifierFor(item.getItem());
    	    	    		    	String itemID;
    	    	    		    	
			    		    		CustomGuiButton currentButton = null;
			    		    		for(int k=0; k<this.buttonList.size(); k++)
			    		    		{
			    		    			if( ((CustomGuiButton)(this.buttonList.get(k))).id == i*10 + j + 10 )
			    		    			{
			    		    				currentButton = (CustomGuiButton) this.buttonList.get(k);
			    		    				break;
			    		    			}
			    		    		}
			    		    		
				    		    	itemID = "textures/originalIcons/items/" + UID.name + ".png";
				    		    	ResourceLocation tempItemTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX, itemID);
				    		    	mc.renderEngine.bindTexture(tempItemTexture);
				    		    	GL11.glPushMatrix();
		    	    		    		GL11.glTranslatef(j*boxSize + 3, i*boxSize + 3, 0);
			    	    		    	GL11.glPushMatrix();
					    		    		GL11.glScalef(0.08f, 0.08f, 0.08f);
					    		    			currentButton.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
    							        GL11.glPopMatrix();
    				    			GL11.glPopMatrix();
    				    			
//    				    			text = "2";
    				    			if(item.stackSize > 1)
    				    			{
    				    				text = "" + item.stackSize;
	    				    			fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    					    		fontRendererObj.drawStringWithShadow(text, (j+1)*boxSize - 8 - fontRendererObj.getStringWidth(text)/2, (i+1)*boxSize - 10, 0xFFFFFF);
    				    			}
    					    		//fontRendererObj.drawStringWithShadow(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4, 0xFFFFFF);
    	    		            }
	    					}
	    				}
						
					GL11.glPopMatrix();
			        break;
	    		case INPROGRESS:
	    			cbtn.visible = false;
		    		/**
		    		 * Draws alchemy progress screen
		    		 */
				    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_BG_TEXTURE);
			        drawTexturedModalRect(mcWidth/2, 25, 0, 0, 185 , 185);
			        break;
	    		case FINAL:
	    			cbtn.visible = false;
		    		/**
		    		 * Draws alchemy progress screen
		    		 */
				    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_DONE_ICON_TEXTURE);
			        drawTexturedModalRect(mcWidth - 114 , mcHeight/2 - 24, 0, 0, 48, 48);
			        break;
	    		}
	        
	        GL11.glPopMatrix();
		}
		
		
//		if(isKOTimeout)
//		{
//			// SHOW K.O
//			GL11.glPushMatrix();
//			    GL11.glTranslatef(mcWidth/2, 0, 0);
//		    	GL11.glScalef(4F, 4F, 3F);
//		    	
//		    	text = "K.O";
//		    	
//		    	float fdx = (new Random()).nextFloat();
//		    	float fdy = (new Random()).nextFloat();
//		    	
//		    	int dx = (fdx>0.80f)?1:(fdx>0.20)?0:-1;
//		    	int dy = (fdy>0.80f)?1:(fdy>0.20)?0:-1;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2 + dx, mcHeight/4 - 30 + dy, 0xFF8888);
//    		
//    		GL11.glPopMatrix();
//		}
//		else if(isVictoryMsgTimeout)
//		{
//			// SHOW VICOTRY
//			GL11.glPushMatrix();
//			    GL11.glTranslatef(mcWidth/2, 0, 0);
//				GL11.glPushMatrix();
//			    	GL11.glScalef(2F, 2F, 2F);
//			    	
//			    	text = victoryMessageLine1;
//			
//		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//		    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 40, 0xFFFFFF);
//	    		GL11.glPopMatrix();
//		    	
//		    	text = victoryMessageLineExp;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 - 20, 0xFFFFFF);
//		    	
//		    	text = victoryMessageLineGold;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 - 10, 0xFFFFFF);
//		    	
//		    	text = victoryMessageLineExtraGold;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2, 0xFFFFFF);
//		    	
//	    		if(!victoryMessageLineSpecialItem.equals(""))
//		    	{
//	    			text = victoryMessageLineSpecialItem;
//		
//		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//		    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 10, 0xFFFFFF);
//		    	}
//		    	
//		    	text = victoryMessageLine2;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 20, 0xFFFFFF);
//    		
//    		GL11.glPopMatrix();
//		}
//		else
//		{
//			// SHOW HINT
//			GL11.glPushMatrix();
//			    GL11.glTranslatef(mcWidth/2, 0, 0);
//				GL11.glPushMatrix();
//			    	GL11.glScalef(2F, 2F, 2F);
//			    	
//			    	text = hintMessageLineTitle;
//			
//		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//		    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4 - 50, 0xFFFFFF);
//	    		GL11.glPopMatrix();
//	    		
//	    		mc.renderEngine.bindTexture(HINT_TEXTURE);
//		        drawTexturedModalRect(-200, mcHeight/2 - 70, 0, 0, 120 , 120);
//		        drawTexturedModalRect(-60, mcHeight/2 - 70, 120, 0, 120 , 120);
//		        drawTexturedModalRect(80, mcHeight/2 - 70, 0, 120, 120 , 120);
//		    	
//		    	text = hintMessageLine1;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 60, 0xFFFFFF);
//		    	
//		    	text = hintMessageLine2;
//		
//	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//	    		fontRendererObj.drawString(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/2 + 75, 0xFFFFFF);
//    		
//    		GL11.glPopMatrix();
//		}
		
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		try{
			switch(button.id)
			{
			case 1:
				this.alchemyGuiMode = AlchemyGuiMode.INPROGRESS;
				System.out.println("Start Button Clicked.");
				break;
			default:
				System.out.println("Clicked Unimplemented Button ID[" + button.id + "]");
				break;
			};
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
		}
	}
	
	public int getTopMargin() {
		return 30;
	}
}
