package com.ramon.hellow.worldgen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeIce implements Theme {
	
	public String getName() {
		return "ice";
	}
	
	public List<BiomeGenBase> getBiomes() {
		List<BiomeGenBase> list = new ArrayList<BiomeGenBase>();
		list.add(BiomeGenBase.coldTaiga);
		list.add(BiomeGenBase.coldTaigaHills);
		list.add(BiomeGenBase.iceMountains);
		list.add(BiomeGenBase.icePlains);
		return list;
	}
	
	public BlockMeta getWallBlock() {
		return new BlockMeta(Blocks.snow,0);
	}
	
	public BlockMeta getAlternateWallBlock() {
		return new BlockMeta(Blocks.snow,0);
	}
	
	public BlockMeta getFloorBlock() {
		return new BlockMeta(Blocks.ice,0);
	}
	
	public BlockMeta getAlternateFloorBlock() {
		return new BlockMeta(Blocks.packed_ice,0);
	}
	
	public BlockMeta getRoofBlock() {
		return new BlockMeta(Blocks.gold_block,0);
	}
	
	public BlockMeta getAlternateRoofBlock() {
		return new BlockMeta(Blocks.iron_block,0);
	}
	
	
	public Block getStairBlock() {
		return Blocks.stone_brick_stairs;
	}
	
	public Block getTorchBlock() {
		return Blocks.air;
	}
	
	public BlockMeta getCenterBlock() {
		return new BlockMeta(Blocks.diamond_block,0);
	}
	
	public Block getDoorBlock() {
		return Blocks.iron_bars;
	}
	
	public BlockMeta getCrop() {
		return new BlockMeta(Blocks.melon_stem,1);
	}

	public BlockMeta getFence() {
		return new BlockMeta(Blocks.fence,0);
	}

	public BlockMeta getSoil() {
		return new BlockMeta(Blocks.farmland,7);
	}
}
