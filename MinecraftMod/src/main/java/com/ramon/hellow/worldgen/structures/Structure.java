package com.ramon.hellow.worldgen.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ramon.hellow.worldgen.themes.Theme;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class Structure {
	
	protected String name = "";
	protected int width = 1;
	protected int length = 1;
	protected int depth = 1;
	protected int height = 1;
	protected List<Class> themes = new ArrayList<Class>();
	
	public abstract void generate(World world,int x,int y,int z,Theme theme,Random random);
	
	public String getName() {
		return name;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getHeight() {
		return height;
	}
	
	public List<Class> getBannedThemes() {
		return themes;
	}
	
	public String getFullName(Theme theme) {
		return StatCollector.translateToLocal("world.theme."+theme.getName())+" "+StatCollector.translateToLocal("world.structure."+name);
	}
}
