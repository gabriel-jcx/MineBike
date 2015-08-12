package com.ramon.hellow.worldgen.structures;

import java.util.Random;

import com.ramon.hellow.worldgen.themes.Theme;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class StructureGarden extends Structure {
	
	public static final String name = "garden";
	
	public static final int width = 8;
	public static final int length = 6;
	public static final int depth = 3;
	public static final int height = 3;
	
	@Override
	public void generate(World world, int x, int y, int z, Theme theme, Random random) {
		int x1 = x-width/2;
		int x2 = x+width/2;
		int z1 = z-length/2;
		int z2 = z+length/2;
		for(int i=x1;i<=x2;i++) {
			for(int j=z1;j<=z2;j++) {
				boolean outlineX = (i==x1||i==x2);
				boolean outlineZ = (j==z1||j==z2);
				boolean outline = (outlineX||outlineZ);
				boolean corner = (outlineX&&outlineZ);
				//walls
				if (outline) {
					world.setBlock(i, y, j, theme.getWall().getBlock(),theme.getWall().getMeta(),1+2);
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
					world.setBlock(i, y+2, j, theme.getTorch().getBlock(),0,1+2);
				}
			}
		}
	}
}
