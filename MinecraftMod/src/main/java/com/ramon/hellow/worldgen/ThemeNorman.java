package com.ramon.hellow.worldgen;

import java.util.ArrayList;
import java.util.List;

import com.ramon.hellow.worldgen.Theme.BlockMeta;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeNorman implements Theme {
	
	public String getName() {
		return "norman";
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
	
	public BlockMeta getWallBlock() {
		return new BlockMeta(Blocks.cobblestone,0);
	}
	
	public BlockMeta getAlternateWallBlock() {
		return new BlockMeta(Blocks.mossy_cobblestone,0);
	}
	
	public BlockMeta getFloorBlock() {
		return new BlockMeta(Blocks.planks,0);
	}
	
	public BlockMeta getAlternateFloorBlock() {
		return new BlockMeta(Blocks.planks,1);
	}
	
	public BlockMeta getRoofBlock() {
		return new BlockMeta(Blocks.planks,0);
	}
	
	public BlockMeta getAlternateRoofBlock() {
		return new BlockMeta(Blocks.hay_block,0);
	}
	
	public Block getStairBlock() {
		return Blocks.oak_stairs;
	}
	
	public Block getTorchBlock() {
		return Blocks.torch;
	}
	
	public BlockMeta getCenterBlock() {
		return new BlockMeta(Blocks.glowstone,0);
	}
	
	public Block getDoorBlock() {
		return Blocks.wooden_door;
	}
	
	public BlockMeta getCrop() {
		return new BlockMeta(Blocks.carrots,1);
	}
	
	public BlockMeta getFence() {
		return new BlockMeta(Blocks.fence,0);
	}

	public BlockMeta getSoil() {
		return new BlockMeta(Blocks.farmland,7);
	}
}
