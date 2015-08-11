package com.ramon.hellow.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class StructureGarden implements Structure {
	public String getName() {
		return "garden";
	}
	
	public int getWidth() { return 8; }
	public int getLength() { return 6; }
	public int getDepth() { return 3; }
	public int getHeight() { return 3; }

	@Override
	public void generate(World world, int x, int y, int z, Theme theme, Random random) {
		for(int i=x-getWidth()/2;i<=x+getWidth()/2;i++) {
			for(int j=z-getLength()/2;j<=z+getLength()/2;j++) {
				boolean outlineX = (i==x-getWidth()/2||i==x+getWidth()/2);
				boolean outlineZ = (j==z-getLength()/2||j==z+getLength()/2);
				boolean outline = (outlineX||outlineZ);
				boolean corner = (outlineX&&outlineZ);
				//walls
				if (outline) {
					world.setBlock(i, y, j, theme.getWallBlock().getBlock(),theme.getWallBlock().getMeta(),1+2);
					world.setBlock(i, y+1, j, theme.getFence().getBlock(),theme.getFence().getMeta(), 1+2);
				}
				//Crops
				else if (j!=z) {
					world.setBlock(i, y, j,theme.getSoil().getBlock(),theme.getSoil().getMeta(),1+2);
					world.setBlock(i, y+1, j, theme.getCrop().getBlock(),theme.getCrop().getMeta(),1+2);
				}
				//Irrigation
				else {
					world.setBlock(i, y, j, Blocks.water);
				}
				//lighting
				if (corner) {
					world.setBlock(i, y+2, j, theme.getTorchBlock(),0,1+2);
				}
			}
		}
	}
}
