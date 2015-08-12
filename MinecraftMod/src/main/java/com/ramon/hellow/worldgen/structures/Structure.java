package com.ramon.hellow.worldgen.structures;

import java.util.Random;

import com.ramon.hellow.worldgen.themes.Theme;

import net.minecraft.world.World;

public abstract class Structure {
	
	public static final String name = "";
	public static final int width = 1;
	public static final int length = 1;
	public static final int depth = 1;
	public static final int height = 1;
	
	public void generate(World world,int x,int y,int z,Theme theme,Random random) {
		
	}
}
