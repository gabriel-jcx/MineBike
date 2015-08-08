package com.ramon.hellow.worldgen;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public interface Theme {
	
	public String getName();
	
	public List<BiomeGenBase> getBiomes();
	
	public Block getWallBlock();
	public int getWallMeta();
	public Block getAlternateWallBlock();
	public int getAlternateWallMeta();
	
	public Block getFloorBlock();
	public int getFloorMeta();
	public Block getAlternateFloorBlock();
	public int getAlternateFloorMeta();
	
	public Block getRoofBlock();
	public int getRoofMeta();
	public Block getAlternateRoofBlock();
	public int getAlternateRoofMeta();
	
	public Block getStairBlock();
	
	public Block getTorchBlock();
	
	public Block getCenterBlock();

	public Block getDoorBlock();
	
}
