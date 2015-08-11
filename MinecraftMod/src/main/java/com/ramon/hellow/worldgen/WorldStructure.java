package com.ramon.hellow.worldgen;

import net.minecraft.world.World;

public class WorldStructure {
	private String name;
	private int x;
	private int y;
	private int z;
	private World world;
	private Structure structure;
	private Theme theme;
	
	public WorldStructure(String name,int x,int y,int z,World world,Structure structure,Theme theme) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.structure = structure;
		this.theme = theme;
	}
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Structure getStructure() {
		return structure;
	}
	
	public Theme getTheme() {
		return theme;
	}
}
