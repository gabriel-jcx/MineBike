package org.ngs.bigx.minecraft.worldgen.themes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeNorman extends Theme {
	
	public ThemeNorman() {
		name = "norman";
	}
	
	public void allocateMemory()
	{
		wall = new BlockMeta(Blocks.cobblestone,0);
		alternateWall = new BlockMeta(Blocks.mossy_cobblestone,0);
		floor = new BlockMeta(Blocks.planks,0);
		alternateFloor = new BlockMeta(Blocks.planks,1);
		roof = new BlockMeta(Blocks.planks,0);
		alternateRoof = new BlockMeta(Blocks.hay_block,0);
		stair = new BlockMeta(Blocks.oak_stairs,0);
		torch = new BlockMeta(Blocks.torch,0);
		center = new BlockMeta(Blocks.glowstone,0);
		door = new BlockMeta(Blocks.wooden_door,0);
		crop = new BlockMeta(Blocks.carrots,1);
		fence = new BlockMeta(Blocks.fence,0);
		soil = new BlockMeta(Blocks.farmland,7);
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
