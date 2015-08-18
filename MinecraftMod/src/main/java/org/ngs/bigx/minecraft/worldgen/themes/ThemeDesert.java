package org.ngs.bigx.minecraft.worldgen.themes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeDesert extends Theme {
	
	public ThemeDesert() {
		name = "desert";
	}
	
	public void allocateMemory()
	{
		wall = new BlockMeta(Blocks.sandstone,0);
		alternateWall = new BlockMeta(Blocks.sandstone,1);
		floor = new BlockMeta(Blocks.planks,2);
		alternateFloor = new BlockMeta(Blocks.planks,4);
		roof = new BlockMeta(Blocks.brick_block,0);
		alternateRoof = new BlockMeta(Blocks.nether_brick,0);
		stair = new BlockMeta(Blocks.birch_stairs,0);
		torch = new BlockMeta(Blocks.redstone_torch,0);
		center = new BlockMeta(Blocks.redstone_lamp,0);
		door = new BlockMeta(Blocks.iron_door,0);
		crop = new BlockMeta(Blocks.wheat,1);
		fence = new BlockMeta(Blocks.nether_brick_fence,0);
		soil = new BlockMeta(Blocks.farmland,7);
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
