package com.ramon.hellow.worldgen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeNorman implements Theme {
	
	public Block getWallBlock() {
		return Blocks.cobblestone;
	}
	
	public int getWallMeta() {
		return 0;
	}
	
	public Block getAlternateWallBlock() {
		return Blocks.mossy_cobblestone;
	}
	
	public int getAlternateWallMeta() {
		return 0;
	}
	
	public Block getFloorBlock() {
		return Blocks.planks;
	}
	
	public int getFloorMeta() {
		return 0;
	}
	
	public Block getAlternateFloorBlock() {
		return Blocks.planks;
	}
	
	public int getAlternateFloorMeta() {
		return 1;
	}
	
	public Block getRoofBlock() {
		return Blocks.planks;
	}
	
	public int getRoofMeta() {
		return 0;
	}
	
	public Block getAlternateRoofBlock() {
		return Blocks.hay_block;
	}
	
	public int getAlternateRoofMeta() {
		return 0;
	}
	
	public Block getStairBlock() {
		return Blocks.oak_stairs;
	}
	
	public Block getTorchBlock() {
		return Blocks.torch;
	}
	
	public Block getCenterBlock() {
		return Blocks.glowstone;
	}
	
	public Block getDoorBlock() {
		return Blocks.wooden_door;
	}
	
	public String getName() {
		return "Norman";
	}

	public List<BiomeGenBase> getBiomes() {
		List<BiomeGenBase> list = new ArrayList<BiomeGenBase>();
		list.add(BiomeGenBase.forest);
		list.add(BiomeGenBase.birchForest);
		list.add(BiomeGenBase.swampland);
		list.add(BiomeGenBase.savanna);
		list.add(BiomeGenBase.taiga);
		list.add(BiomeGenBase.plains);
		list.add(BiomeGenBase.extremeHills);
		list.add(BiomeGenBase.jungle);
		return list;
	}
}
