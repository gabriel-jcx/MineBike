package com.ramon.hellow.worldgen.themes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeIce extends Theme {
	
	public static final String name = "ice";
	
	@Override
	public void allocateMemory() {
		wall = new BlockMeta(Blocks.snow,0);
		alternateWall = new BlockMeta(Blocks.snow,0);
		floor = new BlockMeta(Blocks.ice,0);
		alternateFloor = new BlockMeta(Blocks.packed_ice,0);
		roof = new BlockMeta(Blocks.gold_block,0);
		alternateRoof = new BlockMeta(Blocks.iron_block,0);
		stair = new BlockMeta(Blocks.stone_brick_stairs,0);
		torch = new BlockMeta(Blocks.air,0);
		center = new BlockMeta(Blocks.diamond_block,0);
		door = new BlockMeta(Blocks.iron_bars,0);
		crop = new BlockMeta(Blocks.melon_stem,0);
		fence = new BlockMeta(Blocks.fence,0);
		soil = new BlockMeta(Blocks.farmland,7);
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
