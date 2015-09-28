package org.ngs.bigx.minecraft.worldgen.structures;

import org.ngs.bigx.minecraft.worldgen.themes.Theme;

import net.minecraft.world.World;

public class WorldStructure {
	private String name;
	private int x;
	private int y;
	private int z;
	private World world;
	private Structure structure;
	private Theme theme;
	private int id;
	
	public WorldStructure(String name,int x,int y,int z,World world,Structure structure,Theme theme,int id) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.structure = structure;
		this.theme = theme;
		this.id = id;
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
	
	public int getID() {
		return id;
	}
}
