package com.ramon.hellow.worldgen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeIce implements Theme {
	
	public Block getWallBlock() {
		return Blocks.snow;
	}
	
	public int getWallMeta() {
		return 0;
	}
	
	public Block getAlternateWallBlock() {
		return Blocks.snow;
	}
	
	public int getAlternateWallMeta() {
		return 0;
	}
	
	public Block getFloorBlock() {
		return Blocks.ice;
	}
	
	public int getFloorMeta() {
		return 0;
	}
	
	public Block getAlternateFloorBlock() {
		return Blocks.packed_ice;
	}
	
	public int getAlternateFloorMeta() {
		return 0;
	}
	
	public Block getRoofBlock() {
		return Blocks.gold_block;
	}
	
	public int getRoofMeta() {
		return 0;
	}
	
	public Block getAlternateRoofBlock() {
		return Blocks.iron_block;
	}
	
	public int getAlternateRoofMeta() {
		return 0;
	}
	
	public Block getStairBlock() {
		return Blocks.stone_brick_stairs;
	}
	
	public Block getTorchBlock() {
		return Blocks.air;
	}
	
	public Block getCenterBlock() {
		return Blocks.diamond_block;
	}
	
	public Block getDoorBlock() {
		return Blocks.iron_bars;
	}
	
	public String getName() {
		return "Ice";
	}
	
	public List<BiomeGenBase> getBiomes() {
		List<BiomeGenBase> list = new ArrayList<BiomeGenBase>();
		list.add(BiomeGenBase.coldTaiga);
		list.add(BiomeGenBase.coldTaigaHills);
		list.add(BiomeGenBase.iceMountains);
		list.add(BiomeGenBase.icePlains);
		return list;
	}
}
