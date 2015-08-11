package com.ramon.hellow.worldgen;

import java.util.Random;

import net.minecraft.world.World;

public interface Structure {
	
	public String getName();
	
	public int getWidth();
	public int getLength();
	public int getDepth();
	public int getHeight();
	
	public void generate(World world,int x,int y,int z,Theme theme,Random random);
}
