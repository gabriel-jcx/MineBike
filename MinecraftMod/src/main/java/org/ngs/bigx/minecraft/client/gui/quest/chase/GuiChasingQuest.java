package org.ngs.bigx.minecraft.client.gui.quest.chase;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGXPacketHandler;
import org.ngs.bigx.minecraft.client.Leaderboard;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.client.gui.CustomGuiButton;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistDescriptionSlot;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistException;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistSlot;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiChasingQuest extends GuiScreen {
	public static enum ChasingQuestDifficultyEnum
	{
		EASY, MEDIUM, HARD
	};
	
	private static Object questLevelLock;
	private static ArrayList<GuiChasingQuestLevelSlotItem> chasingQuestLevelList;
	private static int selectedQuestLevelIndex = -1;
	private static ChasingQuestDifficultyEnum chasingQuestDifficulty = ChasingQuestDifficultyEnum.MEDIUM;
	
	private static ArrayList<Leaderboard> leaderboards = new ArrayList<Leaderboard>(); 

	private GuiChasingQuestLevelSlot guiChasingQuestLevelSlot;
	
	private Minecraft mc;
	private BigxClientContext context;
	
	public GuiChasingQuest(Minecraft mc) {
		super();
		this.mc = mc;
		
		if(this.chasingQuestLevelList == null)
			this.chasingQuestLevelList = new ArrayList<GuiChasingQuestLevelSlotItem>();
		
		if(this.questLevelLock == null)
			this.questLevelLock = new Object();
	}
	
	public GuiChasingQuest(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		if(this.guiChasingQuestLevelSlot == null)
			this.guiChasingQuestLevelSlot = new GuiChasingQuestLevelSlot(this);

		int chasingquestlevelslotWidth = guiChasingQuestLevelSlot.getWidth();
		int chasingquestlevelslotHeight = guiChasingQuestLevelSlot.getHeight();
		
		/**
		 * Difficulty Buttons
		 */
		GuiButton btn;
		
		btn = new GuiButton(1, 0 , getTopMargin() + chasingquestlevelslotHeight + 22 , chasingquestlevelslotWidth , 20 , 
				I18n.format("gui.chasingquest.easy", new Object[0]));
		
		if(this.chasingQuestDifficulty == ChasingQuestDifficultyEnum.EASY)
			btn.enabled = false;
		else
			btn.enabled = true;
		
		this.buttonList.add(btn);
	
		btn = new GuiButton(2, 0 , getTopMargin() + chasingquestlevelslotHeight + 42 , chasingquestlevelslotWidth , 20 , 
				I18n.format("gui.chasingquest.medium", new Object[0]));
		
		if(this.chasingQuestDifficulty == ChasingQuestDifficultyEnum.MEDIUM)
			btn.enabled = false;
		else
			btn.enabled = true;
		
		this.buttonList.add(btn);
	
		btn = new GuiButton(3, 0 , getTopMargin() + chasingquestlevelslotHeight + 62 , chasingquestlevelslotWidth , 20 , 
				I18n.format("gui.chasingquest.hard", new Object[0]));
		
		if(this.chasingQuestDifficulty == ChasingQuestDifficultyEnum.HARD)
			btn.enabled = false;
		else
			btn.enabled = true;
		
		this.buttonList.add(btn);
	
		CustomGuiButton cbtn = new CustomGuiButton(4, 0 , height - 40 , chasingquestlevelslotWidth , 40 , 
				"", "/textures/GUI/startbutton.png");
		this.buttonList.add(cbtn);
		/**
		 * END OF Difficulty Buttons
		 */
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		guiChasingQuestLevelSlot.drawScreen(mx, my, partialTicks);

		int chasingquestlevelslotWidth = guiChasingQuestLevelSlot.getWidth();
		int chasingquestlevelslotHeight = guiChasingQuestLevelSlot.getHeight();
		
		drawRect(0, 0, width, getTopMargin(), 0xCC0072BB); // The Box on top
		drawRect(0, getTopMargin() + chasingquestlevelslotHeight, chasingquestlevelslotWidth, getTopMargin() + chasingquestlevelslotHeight + 22, 0xFF000000); // The Seperator (level and difficulty) Background
		drawRect(1, getTopMargin() + chasingquestlevelslotHeight + 1, chasingquestlevelslotWidth - 1, getTopMargin() + chasingquestlevelslotHeight + 21, 0xCC0072BB); // The Seperator (level and difficulty)
		drawRect(0, getTopMargin() + chasingquestlevelslotHeight + 82, chasingquestlevelslotWidth, height, 0xFF000000); // The START!
		drawRect(chasingquestlevelslotWidth+3, getTopMargin()+3, width, getTopMargin() + 25, 0xCC0072BB); // Leader Board Title Bar
		drawRect(chasingquestlevelslotWidth+3, getTopMargin()+25, width, height, 0x88000000); // Leader Board Background
		
		drawRect(chasingquestlevelslotWidth, getTopMargin(), chasingquestlevelslotWidth+3, height, 0xFF000000); // The Seperator (left and right)
		drawRect(0, getTopMargin(), width, getTopMargin()+3, 0xFF000000); // The Seperator (top and contents)
		

		int x=0;
		int y=0;
		String text = "";
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		
		text = "Chasing Quest Menu";
		x = 5;
		y = 4;
		
		GL11.glPushMatrix();
    		GL11.glScalef(2F, 2F, 2F);
			font.drawStringWithShadow(text, x, y, 0xFFFFFF);
    	GL11.glPopMatrix();
		
		text = "Difficulty";
		x = 10;
		y = getTopMargin() + chasingquestlevelslotHeight + 6;

		font.drawStringWithShadow(text, x, y, 0xFFFFFF);
		
		/**
		 * Draw Leader Board
		 */
		int level = 1;
		
		if(this.getSelectedQuestlevel() != null)
			level = this.getSelectedQuestlevel().level;
		
		text = "Level " + level + "  Leaderboard";
		x = chasingquestlevelslotWidth + 13;
		y = getTopMargin() + 9;

		font.drawStringWithShadow(text, x, y, 0xFFFFFF);
		
		GL11.glPushMatrix();
		    GL11.glTranslatef(chasingquestlevelslotWidth + 13, getTopMargin() + 31, 0); 
		
	        ArrayList<LeaderboardRow> leaderboardRows = new ArrayList<LeaderboardRow>();
	        
	        if( (this.leaderboards != null) && (this.leaderboards.size() >= level) )
	        	leaderboardRows = this.leaderboards.get(level-1).leaderboardRows;
	        else{
	        	LeaderboardRow leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "Level";
	        	leaderboardRow.name = "Name";
	        	leaderboardRow.rank = "Rank";
	        	leaderboardRow.time_elapsed = "Time";
	        	leaderboardRows.add(leaderboardRow);
	        	
	        	leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "" + level;
	        	leaderboardRow.name = "John Lennon";
	        	leaderboardRow.rank = "1";
	        	leaderboardRow.time_elapsed = "1 min 2 secs";
	        	leaderboardRows.add(leaderboardRow);
	        	
	        	leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "" + level;
	        	leaderboardRow.name = "Mc Donald";
	        	leaderboardRow.rank = "2";
	        	leaderboardRow.time_elapsed = "1 min 3 secs";
	        	leaderboardRows.add(leaderboardRow);
	        	
	        	leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "" + level;
	        	leaderboardRow.name = "Mc Donald";
	        	leaderboardRow.rank = "2";
	        	leaderboardRow.time_elapsed = "1 min 3 secs";
	        	leaderboardRows.add(leaderboardRow);
	        	
	        	leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "" + level;
	        	leaderboardRow.name = "Mc Donald";
	        	leaderboardRow.rank = "2";
	        	leaderboardRow.time_elapsed = "1 min 3 secs";
	        	leaderboardRows.add(leaderboardRow);
	        	
	        	leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "" + level;
	        	leaderboardRow.name = "Mc Donald";
	        	leaderboardRow.rank = "2";
	        	leaderboardRow.time_elapsed = "1 min 3 secs";
	        	leaderboardRows.add(leaderboardRow);
	        	
	        	leaderboardRow =new LeaderboardRow();
	        	leaderboardRow.level = "" + level;
	        	leaderboardRow.name = "Mc Donald";
	        	leaderboardRow.rank = "2";
	        	leaderboardRow.time_elapsed = "1 min 3 secs";
	        	leaderboardRows.add(leaderboardRow);
	        }
	        
	    	for(int i=0; i<leaderboardRows.size(); i++)
	    	{
	    		String rank = leaderboardRows.get(i).rank;
	    		String name = leaderboardRows.get(i).name;
	    		String levelstr = leaderboardRows.get(i).level;
	    		String time_elapsed = leaderboardRows.get(i).time_elapsed;
	
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(rank, 0, i*14, 0xFFFFFF);
	    		fontRendererObj.drawString(name, 30, i*14, 0xFFFFFF);
	    		fontRendererObj.drawString(levelstr, 140, i*14, 0xFFFFFF);
	    		fontRendererObj.drawString(time_elapsed, 180, i*14, 0xFFFFFF);
	    	}
		
		GL11.glPopMatrix();
		/**
		 * END OF Draw Leader Board
		 */
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		try{
			switch(button.id)
			{
			case 1:
				chasingQuestDifficulty = ChasingQuestDifficultyEnum.EASY;
				System.out.println("Chasing Quest Easy Selected");
				break;
			case 2:
				chasingQuestDifficulty = ChasingQuestDifficultyEnum.MEDIUM;
				System.out.println("Chasing Quest Medium Selected");
				break;
			case 3:
				chasingQuestDifficulty = ChasingQuestDifficultyEnum.HARD;
				System.out.println("Chasing Quest Hard Selected");
				break;
			case 4:
	    		Minecraft.getMinecraft().thePlayer.closeScreen();
				System.out.println("Chasing Quest START Selected");
				break;
			default:
				System.out.println("Clicked Unimplemented Button");
				break;
			};
			
			for(int i=0; i<this.buttonList.size(); i++)
			{
				if(button.id == 4)
					break;
				
				if( ((GuiButton)this.buttonList.get(i)).id == button.id )
				{
					((GuiButton)this.buttonList.get(i)).enabled = false;
				}
				else
				{
					((GuiButton)this.buttonList.get(i)).enabled = true;
				}
					
			}
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
		}
	}

    @Override
    public void keyTyped(char c, int i)
    {
    	super.keyTyped(c, i);
    }
	
	public int getChasingQuestLevelListSize()
	{
		return chasingQuestLevelList.size();
	}
	
	public void addChasingQuestLevel(GuiChasingQuestLevelSlotItem guiChasingQuestLevelSlotItem) throws GuiQuestlistException, NullPointerException
	{
		int i=0;
		
		if(guiChasingQuestLevelSlotItem == null)
			throw new NullPointerException();
		
		for(i=0; i<chasingQuestLevelList.size(); i++)
		{
			if(chasingQuestLevelList.get(i).level == guiChasingQuestLevelSlotItem.level)
			{
				throw new GuiQuestlistException("Level Already Exists!");
			}
		}
		
		chasingQuestLevelList.add(guiChasingQuestLevelSlotItem);
	}
	
	public void addChasingQuestLevels(List<GuiChasingQuestLevelSlotItem> guiChasingQuestLevelSlotItems) throws GuiQuestlistException, NullPointerException
	{
		if(guiChasingQuestLevelSlotItems == null)
			throw new NullPointerException();
		
		for(GuiChasingQuestLevelSlotItem guiChasingQuestLevelSlotItem : guiChasingQuestLevelSlotItems)
		{
			this.addChasingQuestLevel(guiChasingQuestLevelSlotItem);
		}
	}
	
	public void resetChasingQuestLevels()
	{
		chasingQuestLevelList.clear();
	}
	
	public void selectQuestlevel(int i)
	{
		selectedQuestLevelIndex = i;
//		this.guiQuestlistDescriptionSlot.resetScroll();
	}
	
	public GuiChasingQuestLevelSlotItem getSelectedQuestLevelList(int i)
	{
		return this.chasingQuestLevelList.get(i);
	}
	
	public GuiChasingQuestLevelSlotItem getSelectedQuestlevel()
	{
		GuiChasingQuestLevelSlotItem returnValue = null;
		
		if(this.selectedQuestLevelIndex != -1)
		{
			returnValue = getSelectedQuestLevelList(this.selectedQuestLevelIndex);
		}
		
		return returnValue;
	}
	
	public int getTopMargin() {
		return 30;
	}

	public int getSelectedQuestLevelIndex() {
		return this.selectedQuestLevelIndex;
	}
	
	public ChasingQuestDifficultyEnum getDifficulty()
	{
		return this.chasingQuestDifficulty;
	}
	
	public void resetLeaderboards()
	{
		this.leaderboards = new ArrayList<Leaderboard>();
	}
	
	public void addLeaderboard(Leaderboard leaderboard)
	{
		if(leaderboard == null)
			throw new NullPointerException();
		
		this.leaderboards.add(leaderboard);
	}
	
	public void addLeaderboards(List<Leaderboard> leaderboards)
	{
		if(leaderboards == null)
			throw new NullPointerException();
		
		this.resetLeaderboards();
		
		for(Leaderboard leaderboard : leaderboards)
		{
			this.leaderboards.add(leaderboard);
		}
	}
}
