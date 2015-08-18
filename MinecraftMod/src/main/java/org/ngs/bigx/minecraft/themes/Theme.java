package org.ngs.bigx.minecraft.themes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class Theme {
	protected String name = "";
	
	protected BlockMeta wall = null;
	protected BlockMeta alternateWall = null;
	protected BlockMeta floor = null;
	protected BlockMeta alternateFloor = null;
	protected BlockMeta roof = null;
	protected BlockMeta alternateRoof = null;
	protected BlockMeta stair = null;
	protected BlockMeta torch = null;
	protected BlockMeta center = null;
	protected BlockMeta door = null;
	protected BlockMeta crop = null;
	protected BlockMeta fence = null;
	protected BlockMeta soil = null;
	
	public abstract void allocateMemory();
	public abstract List<BiomeGenBase> getBiomes();
	
	public void deallocateMemory() {
		wall = null;
		alternateWall = null;
		floor = null;
		alternateFloor = null;
		roof = null;
		alternateRoof = null;
		stair = null;
		torch = null;
		center = null;
		door = null;
		crop = null;
		fence = null;
		soil = null;
	}
	
	public BlockMeta getWall() {
		return wall;
	}
	
	public BlockMeta getAlternateWall() {
		return alternateWall;
	}
	
	public BlockMeta getFloor() {
		return floor;
	}
	
	public BlockMeta getAlternateFloor() {
		return alternateFloor;
	}
	
	public BlockMeta getRoof() {
		return roof;
	}
	
	public BlockMeta getAlternateRoof() {
		return alternateRoof;
	}
	
	public BlockMeta getStair() {
		return stair;
	}
	
	public BlockMeta getTorch() {
		return torch;
	}
	
	public BlockMeta getCenter() {
		return center;
	}
	
	public BlockMeta getDoor() {
		return door;
	}
	
	public BlockMeta getCrop() {
		return crop;
	}
	
	public BlockMeta getFence() {
		return fence;
	}
	
	public BlockMeta getSoil() {
		return soil;
	}
	
	public String getName() {
		return name;
	}
	
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
