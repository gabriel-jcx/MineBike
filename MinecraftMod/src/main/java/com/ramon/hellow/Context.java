package com.ramon.hellow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;

import com.ramon.hellow.worldgen.Structure;
import com.ramon.hellow.worldgen.StructureTower;
import com.ramon.hellow.worldgen.Theme;
import com.ramon.hellow.worldgen.ThemeDesert;
import com.ramon.hellow.worldgen.ThemeIce;
import com.ramon.hellow.worldgen.ThemeNorman;
import com.ramon.hellow.worldgen.WorldStructure;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class Context {
	public BiGXNetClient bigxclient;
	public int heartrate = 80;
	private float speed = 0;
	public float resistance = 0;
	public boolean bump = false;
	public Block block = Blocks.air;
	public int rotation = 0;
	
	public enum Resistance {
		NONE(0),LOW(4),MLOW(7),MID(10),MHIGH(13),HIGH(16);
		
		private float resistance;
		
		Resistance(float res) {
			resistance = res;
		}
		
		public float getResistance() {
			return resistance;
		}
	}
	
	public HashMap<Block,Resistance> resistances = new HashMap<Block,Resistance>();
	
	public List<Block> canGenerateOn = new ArrayList<Block>();
	
	public List<Theme> themes = new ArrayList<Theme>();
	
	public List<Structure> structures = new ArrayList<Structure>();
	
	public Context() {
		resistances.put(Blocks.air, Resistance.NONE);
		resistances.put(Blocks.ice, Resistance.LOW);
		resistances.put(Blocks.stone, Resistance.MLOW);
		resistances.put(Blocks.cobblestone, Resistance.MLOW);
		resistances.put(Blocks.grass, Resistance.MID);
		resistances.put(Blocks.dirt, Resistance.MID);
		resistances.put(Blocks.sand, Resistance.MHIGH);
		resistances.put(Blocks.gravel, Resistance.MHIGH);
		resistances.put(Blocks.water, Resistance.HIGH);
		
		canGenerateOn.add(Blocks.grass);
		canGenerateOn.add(Blocks.dirt);
		canGenerateOn.add(Blocks.sand);
		canGenerateOn.add(Blocks.stone);
		
		themes.add(new ThemeDesert());
		themes.add(new ThemeNorman());
		themes.add(new ThemeIce());
		
		structures.add(new StructureTower());
	}
	
	public Theme getTheme(BiomeGenBase biome,Random random) {
		int i = random.nextInt(themes.size()-1);
		int j = 0;
		while(j<themes.size()) {
			Theme th = themes.get(i);
			if ( th.getBiomes().contains(biome) ) {
				return th;
			}
			i++;
			if (i>=themes.size()) {
				i-=themes.size();
			}
			j++;
		}
		return null;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public Structure getStructure(String name) {
		for (Structure structure:structures) {
			if (structure.getName().equals(name)) {
				return structure;
			}
		}
		return null;
	}
	
	public Theme getTheme(String name) {
		for (Theme theme:themes) {
			if (theme.getName().equals(name)) {
				return theme;
			}
		}
		return null;
	}
}