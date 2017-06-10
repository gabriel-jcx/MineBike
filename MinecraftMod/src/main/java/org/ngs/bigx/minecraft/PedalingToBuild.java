package org.ngs.bigx.minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class PedalingToBuild {
	private int posx = 0;
	private int posy = 0;
	private int posz = 0;
	
	private int direction = 0; // 0:East, 1:West, 2:South and 3:North

	private TerrainBiomeArea buildingLayout = null;
	private List<BlockPositionMapping> blocksToBePlaced = new ArrayList<BlockPositionMapping>();
	
	private int totalBlocks = 1;
	private int builtBlocks = 0;
	
	private int totalBlocksCleaning = 350;
	private int cleanedBlocks = 0;
	
	private int currentProgress = 0;
	
	private String buildingId = "";
	
	private boolean isLoaded = false;
	private boolean isCleaningDone = false;
	private boolean isCleaningDoneFlag = false;

	public static final float scalefactor = 50; // 1 rotation
	
	private BlockPositionMapping currentBlock = null;
	
	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

	public int getPosz() {
		return posz;
	}

	public boolean isCleaningDoneFlag() {
		return isCleaningDoneFlag;
	}

	public void unsetCleaningDoneFlag() {
		this.isCleaningDoneFlag = false;
	}

	public PedalingToBuild(int posx, int posy, int posz, int direction, String buildingId)
	{
		this.posx = posx;
		this.posy = posy;
		this.posz = posz;
		
		this.direction = direction;
		this.buildingId = buildingId;
		
		this.loadbuidlingLayoutByBuildingId(buildingId);
	}
	
	public void initStats()
	{
		this.totalBlocks = 1;
		this.builtBlocks = 0;
		this.buildingLayout = null;
		this.totalBlocksCleaning = 350;
		this.cleanedBlocks = 0;
		this.isLoaded = false;
		this.isCleaningDone = false;
		this.isCleaningDoneFlag = false;
		this.blocksToBePlaced = new ArrayList<BlockPositionMapping>();
	}
	
	public void loadbuidlingLayoutByBuildingId(String buildingId)
	{
		this.initStats();
		
		this.buildingLayout = this.loadbuildingLayoutFromFileByBuildingId(buildingId);
		this.totalBlocks = this.buildingLayout.map.size();
		
		this.isLoaded = true;
	}
	
	public String getCurrentProgressInTotalBlocks()
	{
		if(!this.isCleaningDone)
			return this.currentProgress + " / " + this.totalBlocksCleaning;
		else
			return this.builtBlocks + " / " + this.totalBlocks;
	}
	
	public String getCurrentProgressInCurrentBlock()
	{
		if(!this.isCleaningDone)
			return this.currentProgress + " / " + this.totalBlocksCleaning;
		else
			return this.currentProgress + " / " + this.getBlockHp(this.currentBlock.block);
	}
	
	public String getCurrentProgress()
	{
		return this.getCurrentProgressInCurrentBlock() + "  (" + this.getCurrentProgressInTotalBlocks() + ")";
	}
	
	public void proceed(int progress)
	{
		if(this.isLoaded)
		{
			currentProgress += progress;
			
			if(!this.isCleaningDone)
			{
				if(this.totalBlocksCleaning <= currentProgress)
				{
					currentProgress = 0;
					this.isCleaningDone = true;
					this.isCleaningDoneFlag = true;
				}
			}
			else if(this.currentBlock == null)
			{
				this.setNextBlockToWorkOn();
			}
		}
	}
	
	public List<BlockPositionMapping> getTheListOfBlockToBePlaced()
	{
		return this.blocksToBePlaced;
	}
	
	public void emptyBlockToBePlaced()
	{
		this.blocksToBePlaced.clear();
	}
	
	public void determineToPlaceBlock()
	{
		if(!this.isLoaded)
			return;
		
		float health = this.getBlockHp(this.currentBlock.block);
		
		if(this.currentProgress >= health)
		{
			this.currentProgress -= health;
			this.builtBlocks++;
			synchronized(this.blocksToBePlaced)
			{
				this.blocksToBePlaced.add(this.currentBlock);
			}
			this.currentBlock = null;
			this.setNextBlockToWorkOn();
		}
	}
	
	private float getBlockHp(Block block)
	{
		float returnValue = scalefactor;
		
		if(block == Blocks.log)
			returnValue *= 3f;
		if(block == Blocks.water)
			returnValue *= 6;
		else
			returnValue *= 2f;
		
		return returnValue;
	}
	
	private int setNextBlockToWorkOn()
	{	
		synchronized(this.buildingLayout.map)
		{
			if(!this.isLoaded)
			{
				return -1;
			}
			
			if(this.buildingLayout == null)
			{
				return -2;
			}
			
			if(this.buildingLayout.map == null)
			{
				return -3;
			}
			
			if(this.buildingLayout.map.size() == 0)
			{
				return 0;
			}
			
			TerrainBiomeAreaIndex[] idx = (TerrainBiomeAreaIndex[])this.buildingLayout.map.keySet().toArray();
			
			this.currentBlock = new BlockPositionMapping(this.buildingLayout.map.remove(idx[0]), idx[0]);
			
			if(idx.length > 1)
				return 2;
			else 
				return 1;
		}
	}
	
	public TerrainBiomeArea loadbuildingLayoutFromFileByBuildingId(String buildingId)
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		if(buildingId.equals("default.tree"))
		{
			biomeArea = new TerrainBiomeArea();
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,3,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,3,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.log);

			biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,4,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,4,3), Blocks.leaves);

			biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,4,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.leaves);

			biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,4,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,4,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,2,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,3,3), Blocks.leaves);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,4,3), Blocks.leaves);
		}
		else if(buildingId.equals("default.waterbasin"))
		{
			biomeArea = new TerrainBiomeArea();

			biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,1,3), Blocks.water);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,1,3), Blocks.water);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,2), Blocks.water);
			
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.water);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,2), Blocks.water);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,2,3), Blocks.water);

			biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,3,3), Blocks.water);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.water);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,2), Blocks.water);
			
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,4,2), Blocks.sandstone_stairs);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,3), Blocks.sandstone_stairs);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,4,2), Blocks.sandstone_stairs);
		}
		else if(buildingId.equals("default.bush"))
		{
			biomeArea = new TerrainBiomeArea();

			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,2,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,4,3), Blocks.leaves2);

			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.leaves2);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.leaves2);

			biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.leaves2);
		}
		else if(buildingId.equals("default.store"))
		{
			biomeArea = new TerrainBiomeArea();
			
			// City 1
			biomeArea = new TerrainBiomeArea();
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,2,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,1,3), Blocks.log);
			
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,3), Blocks.log);

			biomeArea.map.put(new TerrainBiomeAreaIndex(0,3,0,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,3,1,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,0,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,2), Blocks.glowstone);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,0,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,1,2), Blocks.wool);

			biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(0,2,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,2,2), Blocks.glass);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,2,2), Blocks.glass);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,2,2), Blocks.wool);
			

			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.log);
			
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,0,0,3), Blocks.log);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,0,1,3), Blocks.log);

			biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,0,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,1,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,0,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,2), Blocks.glowstone);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,3,0,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,3,1,2), Blocks.wool);

			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,2,2), Blocks.glass);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,2,2), Blocks.glass);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,0,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,1,2,2), Blocks.wool);
			biomeArea.map.put(new TerrainBiomeAreaIndex(5,2,2,2), Blocks.wool);
		}
		else if(buildingId.equals("default.cactus"))
		{
			biomeArea = new TerrainBiomeArea();

			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.cactus);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.cactus);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.cactus);

			biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.cactus);
			biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.cactus);

			biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.cactus);
			biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,3), Blocks.cactus);

			biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,3), Blocks.cactus);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,3), Blocks.cactus);
			biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,3,3), Blocks.cactus);
		}
		
		return biomeArea;
	}
}
