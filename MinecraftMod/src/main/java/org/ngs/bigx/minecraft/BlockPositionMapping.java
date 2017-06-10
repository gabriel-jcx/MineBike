package org.ngs.bigx.minecraft;

import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;

import net.minecraft.block.Block;

public class BlockPositionMapping {
	public Block block;
	public TerrainBiomeAreaIndex terrainBiomeAreaIndex;
	
	public BlockPositionMapping(Block block, TerrainBiomeAreaIndex terrainBiomeAreaIndex)
	{
		this.block = block;
		this.terrainBiomeAreaIndex = terrainBiomeAreaIndex;
	}
}
