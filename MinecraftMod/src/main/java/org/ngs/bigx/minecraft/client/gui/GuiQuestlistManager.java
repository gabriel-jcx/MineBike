package org.ngs.bigx.minecraft.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.quests.Quest;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiQuestlistManager extends GuiScreen {
	private static Object questReferenceListLock;
	private static ArrayList<Quest> questReferenceList;
	private static int selectedQuestReferenceIndex = -1;

	private GuiQuestlistSlot guiQuestlistSlot; 
	private GuiQuestlistDescriptionSlot guiQuestlistDescriptionSlot; 
	
	private Minecraft mc;
	private Context context;
	
	public GuiQuestlistManager(Minecraft mc) {
		super();
		this.mc = mc;
		
		if(this.questReferenceList == null)
			this.questReferenceList = new ArrayList<Quest>();
		
		if(this.questReferenceListLock == null)
			this.questReferenceListLock = new Object();
	}
	
	public GuiQuestlistManager(Context c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		if(this.guiQuestlistSlot == null)
			this.guiQuestlistSlot = new GuiQuestlistSlot(this);
		
		if(this.guiQuestlistDescriptionSlot == null)
			this.guiQuestlistDescriptionSlot = new GuiQuestlistDescriptionSlot(this);
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		guiQuestlistSlot.drawScreen(mx, my, partialTicks);
		guiQuestlistDescriptionSlot.drawScreen(mx, my, partialTicks);
		drawRect(0, 0, width, getTopMargin(), 0xCC663300); // The Box on top
		drawRect(197, getTopMargin(), 200, height, 0xFF000000); // The Seperator

		int x=0;
		int y=0;
		String text = "";
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		
		text = "Quest List";
		x = 10;
		y =getTopMargin() - 15;
		font.drawStringWithShadow(text, x, y, 0xFFFFFF);
		
		text = "Detail";
		x = 210;
		font.drawStringWithShadow(text, x, y, 0xFFFFFF);
		
		super.drawScreen(mx, my, partialTicks);
	}

    @Override
    public void keyTyped(char c, int i)
    {
    	super.keyTyped(c, i);
    }
	
	public Quest getQuestReference(String id)
	{
		synchronized (questReferenceListLock) {
			int index = -1;
			int i=0;
			
			for(i=0; i<this.questReferenceList.size(); i++)
			{
				if(this.questReferenceList.get(i).getQuestId().equals(id))
				{
					index = i;
				}
			}
			
			if(index == -1)
				return null;
			
			return this.questReferenceList.get(index);
		}
	}
	
	public int getQuestReferenceListSize()
	{
		return this.questReferenceList.size();
	}
	
	public void addQuestReference(Quest quest) throws GuiQuestlistException, NullPointerException
	{
		int i=0;
		
		if(quest == null)
			throw new NullPointerException();
		
		for(i=0; i<this.questReferenceList.size(); i++)
		{
			if(this.questReferenceList.get(i).getQuestId().equals(quest.getQuestId()))
			{
				throw new GuiQuestlistException("Quest Already Exists!");
			}
		}
		
		this.questReferenceList.add(quest);
	}
	
	public void addQuestReferences(List<Quest> questlist) throws GuiQuestlistException, NullPointerException
	{
		if(questlist == null)
			throw new NullPointerException();
		
		for(Quest quest : questlist)
		{
			this.addQuestReference(quest);
		}
	}
	
	public void resetQuestReferences()
	{
		this.questReferenceList.clear();
	}
	
	public void selectQuestlist(int i)
	{
		selectedQuestReferenceIndex = i;
		this.guiQuestlistDescriptionSlot.resetScroll();
	}
	
	public Quest getSelectedQuestlist(int i)
	{
		return this.questReferenceList.get(i);
	}
	
	public Quest getSelectedQuest()
	{
		Quest returnValue = null;
		
		if(this.selectedQuestReferenceIndex != -1)
		{
			returnValue = getSelectedQuestlist(this.selectedQuestReferenceIndex);
		}
		
		return returnValue;
	}
	
	public int getTopMargin() {
		return 30;
	}

	public int getSelectedQuestlistIndex() {
		return this.selectedQuestReferenceIndex;
	}
}
