package org.ngs.bigx.minecraft.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;

import com.ibm.icu.impl.ICUService.Key;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiBuildinglistManager extends GuiScreen {
	private static ArrayList<String> buildingReferenceList;
	private static int selectedBuildingReferenceIndex = -1;

	private GuiBuildinglistSlot guiBuildinglistSlot; 
	
	private Minecraft mc;
	private BigxClientContext context;
	
	public GuiBuildinglistManager(Minecraft mc) {
		super();
		this.mc = mc;
		
		if(this.buildingReferenceList == null)
			this.buildingReferenceList = new ArrayList<String>();
	}
	
	public GuiBuildinglistManager(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		if(this.guiBuildinglistSlot == null)
			this.guiBuildinglistSlot = new GuiBuildinglistSlot(this);
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		//guiBuildinglistSlot.drawBackground(mx,my, partialTicks);
		//guiBuildinglistSlot.
		guiBuildinglistSlot.drawScreen(mx, my, partialTicks);
		drawRect(0, 0, width, getTopMargin(), 0xCC663300); // The Box on top
		drawRect(197, getTopMargin(), 200, height, 0xFF000000); // The Seperator

		int x=0;
		int y=0;
		String text = "";
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		
		text = "Building List";
		x = 10;
		y =getTopMargin() - 15;
		font.drawStringWithShadow(text, x, y, 0xFFFFFF);
		
		text = "Detail";
		x = 210;
		font.drawStringWithShadow(text, x, y, 0xFFFFFF);
		
		super.drawScreen(mx, my, partialTicks);
	}

    @Override
    public void keyTyped(char c, int i) throws IOException {
    	super.keyTyped(c, i);
    	
    	if( (c == 'N') || (c=='n') )
    	{
            if(mc.currentScreen != null) {
            	Minecraft.getMinecraft().player.closeScreen();
            	//Minecraft.getMinecraft().player.closeScreen();
            }
    	}
    }
    
    public void addBuildingReference(String buildingId) throws Exception
	{
		int i=0;
		
		if(buildingId.equals(""))
			throw new Exception("Building ID Empty");
		
		for(i=0; i<this.buildingReferenceList.size(); i++)
		{
			if(this.buildingReferenceList.get(i).equals(buildingId))
			{
				throw new Exception("Building ID Already Exists!");
			}
		}
		
		this.buildingReferenceList.add(buildingId);
	}
	
	public void addBuildingReferences(List<String> buildingIdlist) throws Exception
	{
		if(buildingIdlist == null)
			throw new NullPointerException();
		
		for(String buildingId : buildingIdlist)
		{
			this.addBuildingReference(buildingId);
		}
	}
	
	public int getBuildingReferenceListSize()
	{
		return this.buildingReferenceList.size();
	}
	
	public void resetBuildingReferences()
	{
		this.buildingReferenceList.clear();
	}
	
	public void selectBuildinglist(int i)
	{
		selectedBuildingReferenceIndex = i;
//		this.guiBuildinglistDescriptionSlot.resetScroll();
	}
	
	public String getSelectedBuildinglist(int i)
	{
		return this.buildingReferenceList.get(i);
	}
	
	public String getSelectedBuilding()
	{
		String returnValue = null;
		
		if(this.selectedBuildingReferenceIndex != -1)
		{
			returnValue = getSelectedBuildinglist(this.selectedBuildingReferenceIndex);
		}
		
		return returnValue;
	}
	
	public int getTopMargin() {
		return 30;
	}

	public int getSelectedBuildinglistIndex() {
		return this.selectedBuildingReferenceIndex;
	}
}
