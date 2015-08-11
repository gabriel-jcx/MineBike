package com.ramon.hellow.worldgen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeDesert implements Theme {
	
	public Block getWallBlock() {
		return Blocks.sandstone;
	}
	
	public int getWallMeta() {
		return 0;
	}
	
	public Block getAlternateWallBlock() {
		return Blocks.sandstone;
	}
	
	public int getAlternateWallMeta() {
		return 1;
	}
	
	public Block getFloorBlock() {
		return Blocks.planks;
	}
	
	public int getFloorMeta() {
		return 2;
	}
	
	public Block getAlternateFloorBlock() {
		return Blocks.sandstone;
	}
	
	public int getAlternateFloorMeta() {
		return 2;
	}
	
	public Block getRoofBlock() {
		return Blocks.brick_block;
	}
	
	public int getRoofMeta() {
		return 0;
	}
	
	public Block getAlternateRoofBlock() {
		return Blocks.nether_brick;
	}
	
	public int getAlternateRoofMeta() {
		return 0;
	}
	
	public Block getStairBlock() {
		return Blocks.sandstone_stairs;
	}
	
	public Block getTorchBlock() {
		return Blocks.redstone_torch;
	}
	
	public Block getCenterBlock() {
		return Blocks.redstone_lamp;
	}
	
	public Block getDoorBlock() {
		return Blocks.iron_door;
	}
	
	public String getName() {
		return "desert";
	}
	
	public List<BiomeGenBase> getBiomes() {
		List<BiomeGenBase> list = new ArrayList<BiomeGenBase>();
		list.add(BiomeGenBase.desert);
		list.add(BiomeGenBase.mesa);
		list.add(BiomeGenBase.desertHills);
		list.add(BiomeGenBase.mesaPlateau);
		list.add(BiomeGenBase.mesaPlateau_F);
		return list;
	}
}
