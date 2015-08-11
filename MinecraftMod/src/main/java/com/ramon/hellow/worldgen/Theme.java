package com.ramon.hellow.worldgen;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public interface Theme {
	
	public String getName();
	
	public List<BiomeGenBase> getBiomes();
	
	public BlockMeta getWallBlock();
	public BlockMeta getAlternateWallBlock();
	
	public BlockMeta getFloorBlock();
	public BlockMeta getAlternateFloorBlock();
	
	public BlockMeta getRoofBlock();
	public BlockMeta getAlternateRoofBlock();
	
	public Block getStairBlock();
	
	public Block getTorchBlock();
	
	public BlockMeta getCenterBlock();

	public Block getDoorBlock();
	
	public BlockMeta getCrop();
	
	public BlockMeta getFence();
	
	public BlockMeta getSoil();
	
	public class BlockMeta {
		private Block block;
		private int meta;
		
		protected BlockMeta(Block block,int meta) {
			this.block = block;
			this.meta = meta;
		}
		
		public Block getBlock() {
			return block;
		}
		
		public int getMeta() {
			return meta;
		}
	}
	
}
