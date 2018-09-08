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
import net.minecraft.entity.player.InventoryPlayer;
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
	private ItemStack[] inventoryItemStack = new ItemStack[36];
	private ArrayList<ItemToBeAlchemied> listOfItemToBeAlchemied = new ArrayList<ItemToBeAlchemied>();
	
	private static Object lockListOfItemToBeAlchemied = new Object();
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
		
//		this.inventoryItemStack.clear();
		this.listOfItemToBeAlchemied.clear();
		inventoryItemStack = new ItemStack[36];
		
		for(int i=0; i<36; i++)
		{
			if(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i) != null)
				this.inventoryItemStack[i] = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i).copy();
			else
				this.inventoryItemStack[i] = null;
		}
		
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
				CustomGuiButton customButton = new CustomGuiButton(i*10 + j + 10, mcWidth/2 + 35 + j * this.boxSize + 1 , 20 + i * this.boxSize + 1 + this.boxSize/2 , this.boxSize-3 , this.boxSize-3 , 
					"", "textures/GUI/transparent.png");
				this.buttonList.add(customButton);
			}
		}
		
		// Buttons for the item selection
		for(int i=0; i<3; i++)
		{
			for(int j=0; j<2; j++)
			{
				CustomGuiButton customButton = new CustomGuiButton(i*2 + j + 70, mcWidth/2 - 50 - boxSize/2 + j*2*boxSize + 1 , 30 + i*boxSize*3/2 + 1 , this.boxSize-3 , this.boxSize-3 , 
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
    		    	GL11.glTranslatef(0, 30, 0); 
	    			GL11.glPushMatrix();
	    		    	GL11.glTranslatef(mcWidth/2 + 35, 0, 0); 

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

//	    	    		    	EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	    	    		    	ItemStack item = this.inventoryItemStack[(i*6 + j)];
    	    		            if(item != null) {
    	    	    		    	UniqueIdentifier UID = GameRegistry.findUniqueIdentifierFor(item.getItem());
    	    	    		    	String itemID;
    	    	    		    	
				    		    	itemID = "textures/originalIcons/items/" + UID.name + ".png";
				    		    	ResourceLocation tempItemTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX, itemID);
				    		    	mc.renderEngine.bindTexture(tempItemTexture);
				    		    	GL11.glPushMatrix();
		    	    		    		GL11.glTranslatef(j*boxSize + 3, i*boxSize + 3, 0);
			    	    		    	GL11.glPushMatrix();
					    		    		GL11.glScalef(0.08f, 0.08f, 0.08f);
					    		    			drawTexturedModalRect(0, 0, 0, 0, 256, 256);
    							        GL11.glPopMatrix();
    				    			GL11.glPopMatrix();
    				    			
//    				    			text = "2";
    				    			if(item.stackSize > 1)
    				    			{
    				    				text = "" + item.stackSize;
	    				    			fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    					    		fontRendererObj.drawStringWithShadow(text, (j+1)*boxSize - 8 - fontRendererObj.getStringWidth(text)/2, (i+1)*boxSize - 10, 0xFFFFFF);    				    			}
    					    		//fontRendererObj.drawStringWithShadow(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4, 0xFFFFFF);
    	    		            }
	    					}
	    				}
	    				
					GL11.glPopMatrix();
	    			GL11.glPushMatrix();
    		    		GL11.glTranslatef(mcWidth/2 - 50 - boxSize/2, 0, 0);
    		    		
    		    		/**
	    		    	 * Display Items in the inventory
	    		    	 */
	    				for(i=0; i<3; i++)
	    				{
	    					drawRect(0,     i*boxSize*3/2 + boxSize/2,     3*boxSize - 2, i*boxSize*3/2 + 1 + boxSize/2, 0xFFAF883F); // The Boundary for the items
	    					
	    					for(j=0; j<2; j++)
	    					{
	    						drawRect(j*2*boxSize,     i*boxSize*3/2,     (j*2+1)*boxSize - 1, i*boxSize*3/2 + boxSize - 1, 0xFF6D380C); // The Boundary for the items
	    						drawRect(j*2*boxSize + 1, i*boxSize*3/2 + 1, (j*2+1)*boxSize - 2, i*boxSize*3/2 + boxSize - 2, 0xAA999999); // The Background for the items
	    	    		    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    	    		    	
	    	    		    	if((i*2 + j) < this.listOfItemToBeAlchemied.size())
	    	    		    	{
	    	    		            if(this.listOfItemToBeAlchemied.get(i*2 + j) != null) {
	    	    		            	ItemStack item = this.listOfItemToBeAlchemied.get(i*2 + j).item;
	    	    	    		    	UniqueIdentifier UID = GameRegistry.findUniqueIdentifierFor(item.getItem());
	    	    	    		    	String itemID;
	    	    	    		    	
					    		    	itemID = "textures/originalIcons/items/" + UID.name + ".png";
					    		    	ResourceLocation tempItemTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX, itemID);
					    		    	mc.renderEngine.bindTexture(tempItemTexture);
					    		    	GL11.glPushMatrix();
			    	    		    		GL11.glTranslatef(j*2*boxSize + 3, i*boxSize*3/2 + 3, 0);
				    	    		    	GL11.glPushMatrix();
						    		    		GL11.glScalef(0.08f, 0.08f, 0.08f);
						    		    			drawTexturedModalRect(0, 0, 0, 0, 256, 256);
	    							        GL11.glPopMatrix();
	    				    			GL11.glPopMatrix();
	    				    			
//	//    				    			text = "2";
//	    				    			if(item.stackSize > 1)
//	    				    			{
//	    				    				text = "" + item.stackSize;
//		    				    			fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//		    					    		fontRendererObj.drawStringWithShadow(text, (j+1)*boxSize - 8 - fontRendererObj.getStringWidth(text)/2, (i+1)*boxSize - 10, 0xFFFFFF);
//	    					    		}
//	    					    		//fontRendererObj.drawStringWithShadow(text, -1 * fontRendererObj.getStringWidth(text)/2, mcHeight/4, 0xFFFFFF);
	    	    		            }
	    	    		    	}
	    					}
	    				}
	    				
	    				drawRect(boxSize*3/2,     boxSize/2,     boxSize*3/2 + 1, i*boxSize*3/2 + 1 + boxSize/2, 0xFFAF883F); // The Boundary for the items
	    				
	    				drawRect(boxSize,     i*boxSize*3/2 + 10, (2)*boxSize - 1, i*boxSize*3/2 + boxSize + 9, 0xFFBF984F); // The Boundary for the items
						drawRect(boxSize + 1, i*boxSize*3/2 + 11, (2)*boxSize - 2, i*boxSize*3/2 + boxSize + 8, 0x99000000); // The Background for the items
						
						text = "?";
		    			fontRendererObj = Minecraft.getMinecraft().fontRenderer;
			    		fontRendererObj.drawStringWithShadow(text, boxSize*3/2 - fontRendererObj.getStringWidth(text)/2, i*boxSize*2 - 22, 0xFFFFFF);
			    		
	    		    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
				if( (button.id >= 10) && (button.id < 66) )
				{
					if(this.listOfItemToBeAlchemied.size() >= 6)
						return;
					
					int tempButtonId = button.id - 10;
					int inveotyrItemStackIdx = (tempButtonId/10)*6 + tempButtonId%10;
					
					System.out.println("Inventory Items Clicked [" + tempButtonId + "][" + inveotyrItemStackIdx + "]");
					
					// Decrease the item from the itemstack (if that is the last item, then
					ItemStack item = this.inventoryItemStack[inveotyrItemStackIdx];
					
					if(item == null)
						return;
					
					int itemSz = item.stackSize;
					if(itemSz > 1)
					{
						item.stackSize --;
					}
					else
					{
						this.inventoryItemStack[inveotyrItemStackIdx] = null;
					}
					
					// Create an Alchemy item with the item stack id
					ItemToBeAlchemied itemToBeAlchemied = new ItemToBeAlchemied();
					itemToBeAlchemied.item = item.copy();
					itemToBeAlchemied.item.stackSize = 1;
					itemToBeAlchemied.itemStackId = inveotyrItemStackIdx;
					
					// Add the Alchemy item to the list
					this.listOfItemToBeAlchemied.add(itemToBeAlchemied);
				}
				else if ( (button.id >= 70) && (button.id < 76) )
				{	
					synchronized(lockListOfItemToBeAlchemied)
					{
						if((button.id - 70) >= this.listOfItemToBeAlchemied.size())
							return;
						
						if(this.listOfItemToBeAlchemied.get(button.id - 70) != null)
						{
							ItemToBeAlchemied itemToBeAlchemied = this.listOfItemToBeAlchemied.get(button.id - 70);
							
							if(this.inventoryItemStack[itemToBeAlchemied.itemStackId] == null)
							{
								this.inventoryItemStack[itemToBeAlchemied.itemStackId] = itemToBeAlchemied.item.copy();
							}
							else
							{
								this.inventoryItemStack[itemToBeAlchemied.itemStackId].stackSize++;
							}
							
							this.listOfItemToBeAlchemied.remove(button.id - 70);
						}
					}
					
					System.out.println("Alchemy Items Clicked [" + button.id + "]");
				}
				else
				{
					System.out.println("Unimplemented Button Clicked [" + button.id + "]");
				}
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
	
	class ItemToBeAlchemied
	{
		public int itemStackId = -1;
		public ItemStack item;
	}
}
