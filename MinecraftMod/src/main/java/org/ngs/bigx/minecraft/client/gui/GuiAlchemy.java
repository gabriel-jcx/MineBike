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
import com.sun.xml.internal.bind.util.Which;

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
	
	public static enum AlchemyProcessStageEnum {
		SPINNING(0), SPARKLING(1), SHRINKING(2), EXPANDING(3), FINALE(4), DONE(5);
		
		private int alchemyProcessStageEnum = 0;
		
		AlchemyProcessStageEnum(int res) {
			alchemyProcessStageEnum = res;
		}
		
		public int getAlchemyProcessStageEnum() {
			return alchemyProcessStageEnum;
		}
		
		public static AlchemyProcessStageEnum fromId(int id) {
            for (AlchemyProcessStageEnum type : values()) {
                if (type.getAlchemyProcessStageEnum() == id) {
                    return type;
                }
            }
            return null;
        }
	}
	
	public static int[] AlchemyProcessStageTransitionTime = {
		0, 1500, 6000, 8500, 10000, 12000, 20000
	};
	
	
	private ResourceLocation ALCHEMIST_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemist.png");
	private ResourceLocation ALCHEMY_ON_PROGRESS_BG_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemy-icons-bg.png");
	private ResourceLocation ALCHEMY_ON_PROGRESS_DONE_ICON_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemy-icons.png");
	private static ResourceLocation ALCHEMY_ON_PROGRESS_EFFECT_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/alchemy_gui_effects.png");
	private ResourceLocation tempItemTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/originalIcons/items/chest.png");
	
	private AlchemyGuiMode alchemyGuiMode = AlchemyGuiMode.INITIAL;
	private CustomGuiButton cbtn;
	private ItemStack[] inventoryItemStack = new ItemStack[36];
	private ArrayList<ItemToBeAlchemied> listOfItemToBeAlchemied = new ArrayList<ItemToBeAlchemied>();

	private static AlchemyProcessStageEnum alchemyProcessStage = AlchemyProcessStageEnum.SPINNING;
	private static Object lockListOfItemToBeAlchemied = new Object();
	private static float twinkleTwinkleLittleStarTiming = 0f;
	private static long  twinkleTwinkleLittleStarStarTime = 0;
	private static float pedalingSpeed = 0;
	private static long pedalingSpeedTickTimestamp = 0;
	private static Object pedalingLock = new Object();
	private static float pedalingRotationAngle = 0;
	private static Timer timer;
	private static long enlapsedTimeOfPedalingOverThreshold=0;
	private static boolean pedalingStateChangeFlag = false;
	private static boolean pedalingStateChangeInTransitionFlag = false;
	private static boolean pedalingStateChangeDirection = true; // true to next, false to move back
	private static long pedalingStateChangeInTransitionTick = 0;
	private static ArrayList<FlatExplosion> flatExplosionsArray = new ArrayList<FlatExplosion>();
	
	public static float pedalingSpeedNatualDecelerationRate = 2f;
	public static int pedalingSpeedTickInterval = 100;
	public static float pedalingSpeedThreshold = 1000f;
	public static Object drawingLock = new Object();
	public static Object enlapsedTimeOfPedalingOverThresholdLock = new Object();
	public static Object pedalingStateChangeTransitionLock = new Object();
	
//	
//	private static Object guiVictoryLock = new Object();
	
	public GuiAlchemy(Minecraft mc) {
		super();
		this.mc = mc;
		
		if(timer ==  null)
		{
			// Start Timer for close screen
	        timer = new Timer(true);
	        timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					decreasePedalingSpeedByTick();
					checkIfSpeedIsOverThreshold();
					checkCurrentPedalingState();
					addExplosionAnimation();
				}
			}, 0, pedalingSpeedTickInterval);
		}
	}
	
	public GuiAlchemy(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
	}
	
	public static void addExplosionAnimation()
	{
		float newrandomFloat = (new Random()).nextFloat();
		
		if(newrandomFloat < 0.2f)
		{
			FlatExplosion explosion = new FlatExplosion();
			explosion.timeEffectStarts = System.currentTimeMillis();
			explosion.xPos = (int) (((new Random()).nextFloat() - .5f) * 250);
			explosion.yPos = (int) (((new Random()).nextFloat() - .5f) * 250);
			flatExplosionsArray.add(explosion);
		}
	}
	
	public static void checkIfSpeedIsOverThreshold()
	{
		synchronized (enlapsedTimeOfPedalingOverThresholdLock) {
			if(pedalingSpeed > pedalingSpeedThreshold)
			{
				enlapsedTimeOfPedalingOverThreshold += pedalingSpeedTickInterval;
			}
			else
			{
				enlapsedTimeOfPedalingOverThreshold -= pedalingSpeedTickInterval;
			}
		}
	}
	
	public static void checkCurrentPedalingState()
	{
		int currentStateIdx = 0;
		int IterationCount = AlchemyProcessStageTransitionTime.length-1;
		
		for(currentStateIdx=0; currentStateIdx < IterationCount; currentStateIdx ++)
		{
			if( (enlapsedTimeOfPedalingOverThreshold >= AlchemyProcessStageTransitionTime[currentStateIdx]) && (enlapsedTimeOfPedalingOverThreshold < AlchemyProcessStageTransitionTime[currentStateIdx+1]) )
			{
				break;
			}
		}
		
		AlchemyProcessStageEnum nextEnum = AlchemyProcessStageEnum.fromId(currentStateIdx);
		
		if(nextEnum != alchemyProcessStage)
		{
			synchronized (pedalingStateChangeTransitionLock) {
				pedalingStateChangeFlag = true;
				pedalingStateChangeInTransitionFlag = true;
				pedalingStateChangeDirection = false;
				pedalingStateChangeInTransitionTick = System.currentTimeMillis();
				
				if(nextEnum.getAlchemyProcessStageEnum() > alchemyProcessStage.getAlchemyProcessStageEnum())
				{
					pedalingStateChangeDirection = true;
				}
				
				alchemyProcessStage = nextEnum;	
			}
		}
	}
	
	public static void increasePedalingSpeed(int speed)
	{
		if(speed == 0)
		{
			decreasePedalingSpeedByValue(4f);
			return;
		}
		System.out.println("ts["+System.currentTimeMillis()+"] speed["+speed+"]");
		synchronized (pedalingLock) {
			pedalingSpeed += speed;
		}
	}
	
	public static void decreasePedalingSpeedByValue(float value)
	{
		synchronized (pedalingLock) {
			pedalingSpeed -= value;
			
			if(pedalingSpeed <= 0)
			{
				pedalingSpeed = 0;
			}
		}
		
//		pedalingRotationAngle += pedalingSpeed/60f*5f;
	}
	
	public static void decreasePedalingSpeedByTick()
	{
		synchronized (pedalingLock) {
			pedalingSpeed -= pedalingSpeedNatualDecelerationRate;
			
			if(pedalingSpeed <= 0)
			{
				pedalingSpeed = 0;
			}
		}
		
//		pedalingRotationAngle += pedalingSpeed/60f*5f;
	}
	
	@Override
	public void initGui() {
		super.initGui();

		this.alchemyGuiMode = AlchemyGuiMode.INITIAL;
		this.alchemyProcessStage = AlchemyProcessStageEnum.SPINNING;
		
		flatExplosionsArray = new ArrayList<FlatExplosion>();
		pedalingSpeedTickTimestamp = System.currentTimeMillis();
		pedalingRotationAngle = 0;
		enlapsedTimeOfPedalingOverThreshold= 0 ;
		
//		this.inventoryItemStack.clear();
		this.listOfItemToBeAlchemied.clear();
		this.inventoryItemStack = new ItemStack[36];
		this.pedalingSpeed = 0;
		
		twinkleTwinkleLittleStarStarTime = System.currentTimeMillis();
		
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
	    						drawRect(j*boxSize, i*boxSize, (j+1)*boxSize - 1, (i+1)*boxSize - 1, 			0xFF4B380C); // The Boundary for the items
	    						drawRect(j*boxSize + 1, i*boxSize + 1, (j+1)*boxSize - 2, (i+1)*boxSize - 2, 	0x99FFFFFF); // The Background for the items
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
	    						drawRect(j*2*boxSize + 1, i*boxSize*3/2 + 1, (j*2+1)*boxSize - 2, i*boxSize*3/2 + boxSize - 2, 0x99FFFFFF); // The Background for the items
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

	    			int itemBoxSize = 31;
	    			int starSize = 185;
	    			int leftX = 0 - (int)(starSize/4*1.7f) - 8 - itemBoxSize/2;
	    			int rightX = 0 + (int)(starSize/4*1.7f) + 8 - itemBoxSize/2;
	    			int topY = starSize*-1/4-itemBoxSize/2;
	    			int bottomY = starSize*1/4-itemBoxSize/2 -3;
	    			int[][] itemLocation = { 
	    					{(itemBoxSize)/-2, starSize/-2 - itemBoxSize/2}, 
	    					{leftX, topY},  
	    					{rightX, topY}, 
	    					{leftX, bottomY}, 
	    					{rightX, bottomY}, 
	    					{(itemBoxSize)/-2, starSize/2 - itemBoxSize/2} };
	    			
	    			int twinkleRate = 2000;
	    			int halfTwinkleRate = twinkleRate/2;
	    			int quarterTwinkleRate = twinkleRate/4;
	    			int octoTwinkleRate = twinkleRate/8;
	    			
	    			long currentTimeStampForTTLS = (System.currentTimeMillis() - twinkleTwinkleLittleStarStarTime) % twinkleRate;
	    			
	    			if(currentTimeStampForTTLS < halfTwinkleRate)
	    			{
    					if(currentTimeStampForTTLS < quarterTwinkleRate)
    						twinkleTwinkleLittleStarTiming = (float)(Math.pow((currentTimeStampForTTLS%quarterTwinkleRate)/(float)quarterTwinkleRate,2)/2f);
    					else
    						twinkleTwinkleLittleStarTiming = (float)(Math.pow((quarterTwinkleRate-currentTimeStampForTTLS%quarterTwinkleRate)/(float)quarterTwinkleRate,2)/2f);
	    			}
	    			else
	    			{
	    				currentTimeStampForTTLS = currentTimeStampForTTLS%quarterTwinkleRate;
    					if(currentTimeStampForTTLS < octoTwinkleRate)
    						twinkleTwinkleLittleStarTiming = (float)(Math.pow((currentTimeStampForTTLS%octoTwinkleRate)/(float)octoTwinkleRate,2)/2f);
    					else
    						twinkleTwinkleLittleStarTiming = (float)(Math.pow((octoTwinkleRate-currentTimeStampForTTLS%octoTwinkleRate)/(float)octoTwinkleRate,2)/2f);
	    			}

	    			synchronized (drawingLock) {
	    				if(pedalingRotationAngle > 360)
	    					pedalingRotationAngle -= 360;
		    			pedalingRotationAngle += (System.currentTimeMillis() - pedalingSpeedTickTimestamp)/(float)pedalingSpeedTickInterval*pedalingSpeed;
//		    			System.out.println("pedalingRotationAngle[" + pedalingRotationAngle + "]");
		    			pedalingSpeedTickTimestamp = System.currentTimeMillis();
					}
	    			
	    			if(pedalingStateChangeInTransitionFlag)
			    	{
			    		if( (System.currentTimeMillis() - pedalingStateChangeInTransitionTick) > 500 )
			    		{
			    			synchronized (pedalingStateChangeTransitionLock) {
				    			pedalingStateChangeInTransitionFlag = false;
				    			pedalingStateChangeFlag = false;
							}
			    		}
			    	}
	    			
	    			
	    			if(alchemyProcessStage.getAlchemyProcessStageEnum() > AlchemyProcessStageEnum.SPINNING.getAlchemyProcessStageEnum())
	    			{
		    			GL11.glPushMatrix();
		    				// Draw the Wing below
						    int wingWidth = 250;
						    int wingHeight = 120;
						    float wingWidthScale = 1f;
						    
						    if(alchemyProcessStage == AlchemyProcessStageEnum.SPARKLING)
						    {
						    	if(pedalingStateChangeInTransitionFlag)
						    	{
						    		wingWidthScale = (System.currentTimeMillis() - pedalingStateChangeInTransitionTick) / 500f;
						    		
						    		if(wingWidthScale > 1f)
						    		{
						    			wingWidthScale = 1f;
						    		}
						    		
						    		wingWidthScale = (float) Math.pow(wingWidthScale, 2);
						    		
						    		GL11.glScalef(wingWidthScale, 0, 0);
						    	}
						    }
						    GL11.glTranslatef(mcWidth/2 - 30 + starSize/2, 25 + starSize/2 + 30, 0);
	    		    		GL11.glColor4f(1F - twinkleTwinkleLittleStarTiming, 1F - twinkleTwinkleLittleStarTiming, 1F - twinkleTwinkleLittleStarTiming, 1.0F);
						    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_EFFECT_TEXTURE);
					        drawTexturedModalRect(wingWidth/-2, wingHeight/-2, 0, 0, wingWidth , wingHeight);
					        
					        // Draw the bubble effects
					        drawFlatExplosions(flatExplosionsArray, mcWidth, mcHeight);
		    			GL11.glPopMatrix();
	    			}
	    			
	    			// Draws the Item Rotation
	    			GL11.glPushMatrix();
	    				GL11.glTranslatef(mcWidth/2 - 30 + starSize/2, 25 + starSize/2, 0);
			    		GL11.glRotatef(pedalingRotationAngle, 0, 0, 1);

			    		/**
			    		 * Draws alchemy progress screen
			    		 */
			    		GL11.glPushMatrix();
	    		    		GL11.glColor4f(.5F + twinkleTwinkleLittleStarTiming, .5F + twinkleTwinkleLittleStarTiming, .5F + twinkleTwinkleLittleStarTiming, 1.0F);
						    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_BG_TEXTURE);
					        drawTexturedModalRect(starSize/-2, starSize/-2, 0, 0, starSize , starSize);
				        GL11.glPopMatrix();
				        
				        for(i=0; i<6; i++)
				        {
				    		GL11.glPushMatrix();
		    		    		GL11.glColor4f(.5F + twinkleTwinkleLittleStarTiming, .5F + twinkleTwinkleLittleStarTiming, .5F + twinkleTwinkleLittleStarTiming, 1.0F);
					        	drawRect(itemLocation[i][0],     itemLocation[i][1],     itemLocation[i][0] + itemBoxSize - 1, itemLocation[i][1] + itemBoxSize - 1, 0xFF4B380C + (int)(0xFF*twinkleTwinkleLittleStarTiming)<<32); // The Boundary for the items
	    						drawRect(itemLocation[i][0] + 1, itemLocation[i][1] + 1, itemLocation[i][0] + itemBoxSize - 2, itemLocation[i][1] + itemBoxSize - 2, 0xEE8B7844 + (int)(0xFF*twinkleTwinkleLittleStarTiming)<<32); // The Background for the items
    				        GL11.glPopMatrix();
    						
    						if((i) < this.listOfItemToBeAlchemied.size())
    	    		    	{
	    						if(this.listOfItemToBeAlchemied.get(i) != null) {
		    		            	ItemStack item = this.listOfItemToBeAlchemied.get(i).item;
		    	    		    	UniqueIdentifier UID = GameRegistry.findUniqueIdentifierFor(item.getItem());
		    	    		    	String itemID;
		    	    		    	
				    		    	itemID = "textures/originalIcons/items/" + UID.name + ".png";
				    		    	ResourceLocation tempItemTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX, itemID);
				    		    	mc.renderEngine.bindTexture(tempItemTexture);
				    		    	GL11.glPushMatrix();
		    	    		    		GL11.glTranslatef(itemLocation[i][0] + 3, itemLocation[i][1] + 3, 0);
			    	    		    	GL11.glPushMatrix();
					    		    		GL11.glScalef(0.094f, 0.094f, 0.094f);
					    		    			drawTexturedModalRect(0, 0, 0, 0, 256, 256);
								        GL11.glPopMatrix();
					    			GL11.glPopMatrix();
	    						}
    	    		    	}
				        }
	    			GL11.glPopMatrix();
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
	
	public void drawFlatExplosions(ArrayList<FlatExplosion> flatExplosionsArray, int mcWidth, int mcHeight)
	{
		synchronized (flatExplosionsArray) {
			ArrayList<FlatExplosion> explosionToBeRemoved = new ArrayList<FlatExplosion>();
			long currentTime = System.currentTimeMillis();
			float whiteCircleSizeRatio = 1f;
			float darkCircleSizeRatio = 1f;
			float yellowCircleSizeRatio = 1f;
			
			float whiteCircleAlphaRatio = 1f;
			float darkCircleAlphaRatio = 1f;
			float yellowCircleAlphaRatio = .6f;
			
			float animationTickTime = 250f;
			long totalAnimationTime = (long) (animationTickTime * 6);
			Minecraft mc = Minecraft.getMinecraft();
			
			// Iterates the array
			for(FlatExplosion explosion : flatExplosionsArray)
			{
				// Chek if the timestamp is outdated
				if( (System.currentTimeMillis() - explosion.timeEffectStarts) > totalAnimationTime )
				{
					explosionToBeRemoved.add(explosion);
				}
				else
				{
					GL11.glPushMatrix();
					GL11.glTranslatef(explosion.xPos, explosion.yPos, 0);
						currentTime = System.currentTimeMillis();
						whiteCircleSizeRatio = 1f;
						darkCircleSizeRatio = 1f;
						yellowCircleSizeRatio = 1f;
						
						whiteCircleAlphaRatio = 1f;
						darkCircleAlphaRatio = 1f;
						yellowCircleAlphaRatio = .6f;
						
						animationTickTime = 250f;
						
						// Draw the white circle
						if(currentTime < animationTickTime * 4)
						{
							GL11.glPushMatrix();
								// Calculate size ratio
								if(currentTime < animationTickTime) {
									whiteCircleSizeRatio = (float)currentTime/animationTickTime;
									
									if(whiteCircleSizeRatio < 0)
										whiteCircleSizeRatio = 0;
								}
								else if(currentTime < animationTickTime*2) {
									float sizeExpansionRatio = (float)Math.log10((float)(currentTime-animationTickTime)/animationTickTime*10f + 1);
									whiteCircleSizeRatio = 1f + sizeExpansionRatio * .25f;
								}
								else if(currentTime < animationTickTime*3) {
									whiteCircleAlphaRatio = (1f - (currentTime-animationTickTime*2)/animationTickTime);
								}
								// Calculate alpha ratio
								GL11.glScalef(whiteCircleSizeRatio,	whiteCircleSizeRatio, whiteCircleSizeRatio);
								GL11.glColor4d(1f, 1f, 1f, whiteCircleAlphaRatio);
							    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_EFFECT_TEXTURE);
						        drawTexturedModalRect(mcWidth - 114 , mcHeight/2 - 24, 0, 150, 50, 50);
					        GL11.glPopMatrix();
						}
						
						// Draw the Dark circle
						if( (currentTime > animationTickTime) && (currentTime < animationTickTime * 5) )
						{
							GL11.glPushMatrix();
								// Calculate size ratio
								if(currentTime < animationTickTime*2) {
									darkCircleSizeRatio = (float)(currentTime-animationTickTime)/animationTickTime;
									
									if(darkCircleSizeRatio < 0)
										darkCircleSizeRatio = 0;
								}
								else if(currentTime < animationTickTime*3) {
									float sizeExpansionRatio = (float)Math.log10((float)(currentTime-animationTickTime*2)/animationTickTime*10f + 1);
									darkCircleSizeRatio = 1f + sizeExpansionRatio * .25f;
								}
								else if(currentTime < animationTickTime*4) {
									darkCircleAlphaRatio = (1f - (currentTime-animationTickTime*3)/animationTickTime);
								}
								// Calculate alpha ratio
								GL11.glScalef(darkCircleSizeRatio,	darkCircleSizeRatio, darkCircleSizeRatio);
								GL11.glColor4d(.133f, .133f, .133f, darkCircleAlphaRatio);
							    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_EFFECT_TEXTURE);
						        drawTexturedModalRect(mcWidth - 114 , mcHeight/2 - 24, 0, 150, 50, 50);
					        GL11.glPopMatrix();
						}
						
						// Draw the Yellow circle
						if( (currentTime > animationTickTime*2) && (currentTime < animationTickTime * 6) )
						{
							GL11.glPushMatrix();
								// Calculate size ratio
								if(currentTime < animationTickTime*3) {
									yellowCircleSizeRatio = (float)(currentTime-animationTickTime*2)/animationTickTime;
									
									if(yellowCircleSizeRatio < 0)
										yellowCircleSizeRatio = 0;
								}
								else if(currentTime < animationTickTime*4) {
									float sizeExpansionRatio = (float)Math.log10((float)(currentTime-animationTickTime*3)/animationTickTime*10f + 1);
									yellowCircleSizeRatio = 1f + sizeExpansionRatio * .25f;
								}
								else if(currentTime < animationTickTime*5) {
									yellowCircleAlphaRatio = .6f * (1f - (currentTime-animationTickTime*4)/animationTickTime);
								}
								// Calculate alpha ratio
								GL11.glScalef(yellowCircleSizeRatio, yellowCircleSizeRatio, yellowCircleSizeRatio);
								GL11.glColor4d(.39f, .39f, .086f, yellowCircleAlphaRatio);
							    mc.renderEngine.bindTexture(ALCHEMY_ON_PROGRESS_EFFECT_TEXTURE);
						        drawTexturedModalRect(mcWidth - 114 , mcHeight/2 - 24, 0, 150, 50, 50);
					        GL11.glPopMatrix();
						}
					GL11.glPopMatrix();
				}
			}
			
			for(FlatExplosion explosion : explosionToBeRemoved)
			{
				flatExplosionsArray.remove(explosion);
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		try{
			switch(button.id)
			{
			case 1:
				this.alchemyGuiMode = AlchemyGuiMode.INPROGRESS;
				buttonList.clear();
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
