package com.ramon.hellow.worldgen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class ThemeDesert implements Theme {
	
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
	
	public BlockMeta getWallBlock() {
		return new BlockMeta(Blocks.sandstone,0);
	}
	
	public BlockMeta getAlternateWallBlock() {
		return new BlockMeta(Blocks.sandstone,1);
	}
	
	public BlockMeta getFloorBlock() {
		return new BlockMeta(Blocks.planks,2);
	}
		
	public BlockMeta getAlternateFloorBlock() {
		return new BlockMeta(Blocks.planks,0);
	}
	
	public BlockMeta getRoofBlock() {
		return new BlockMeta(Blocks.brick_block,0);
	}
	
	public BlockMeta getAlternateRoofBlock() {
		return new BlockMeta(Blocks.nether_brick,0);
	}
	
	public Block getStairBlock() {
		return Blocks.sandstone_stairs;
	}
	
	public Block getTorchBlock() {
		return Blocks.redstone_torch;
	}
	
	public BlockMeta getCenterBlock() {
		return new BlockMeta(Blocks.redstone_lamp,0);
	}
	
	public Block getDoorBlock() {
		return Blocks.iron_door;
	}
	
	public BlockMeta getCrop() {
		return new BlockMeta(Blocks.wheat,2);
	}

	public BlockMeta getFence() {
		return new BlockMeta(Blocks.nether_brick_fence,0);
	}

	public BlockMeta getSoil() {
		return new BlockMeta(Blocks.farmland,7);
	}
}
