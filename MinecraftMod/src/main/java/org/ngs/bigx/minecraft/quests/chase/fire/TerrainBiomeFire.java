package org.ngs.bigx.minecraft.quests.chase.fire;

import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeDef;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TerrainBiomeFire {
	public TerrainBiomeDef gateBiome;
	public TerrainBiomeDef fieldBiome;
	public TerrainBiomeDef lavaFountainBiome;
	
	public TerrainBiomeFire()
	{
		TerrainBiomeArea biomeArea;
		this.gateBiome = new TerrainBiomeDef();
		this.fieldBiome = new TerrainBiomeDef();
		this.lavaFountainBiome = new TerrainBiomeDef();
		
		// SETBLOCK(x,y,x,direction,3)
		
		this.createGateBiome();
		this.createFieldBiome();
		this.createLavaFountainBiome();
	}
	
	public TerrainBiomeArea getRandomGateBiome()
	{
		return this.gateBiome.getRandomArea();
	}
	
	public TerrainBiomeArea getRandomFieldBiome()
	{
		return this.fieldBiome.getRandomArea();
	}
	
	public TerrainBiomeArea getRandomLavaFountainBiome()
	{
		return this.lavaFountainBiome.getRandomArea();
	}
	
	public void createGateBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// Room
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,1,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,4,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,3,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,4,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,3,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,1,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,1,3), Blocks.iron_bars);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,4,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.iron_bars);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,3,3), Blocks.iron_bars);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,3,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,2,3), Blocks.iron_bars);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,3,3), Blocks.iron_bars);
		
		this.gateBiome.areas.add(biomeArea);
		
		// Pilla with lava in it
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,3,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.lava);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,1,3), Blocks.glass);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,2,3), Blocks.glass);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,3,3), Blocks.glass);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,2,3), Blocks.glass);
		
		this.gateBiome.areas.add(biomeArea);
		
		// Transparent Glass on the Floor
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.lava);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.glass);
		
		this.gateBiome.areas.add(biomeArea);
		
		// Cobble Stone pile
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.glowstone);
		
		this.gateBiome.areas.add(biomeArea);
	}
	
	public void createFieldBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// Lights
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,1,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,6,1,3), Blocks.glowstone);


		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,4,3), Blocks.glowstone);
		
		this.fieldBiome.areas.add(biomeArea);
		
		// Lava underneath
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,1,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,4,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,1,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,3,3), Blocks.lava);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,3), Blocks.glass);
		
		this.fieldBiome.areas.add(biomeArea);
		
		// Pillars of lights
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,4,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,4,3), Blocks.glowstone);
		
		this.fieldBiome.areas.add(biomeArea);
		
		// Stair ways to go down and up
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,0,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,1,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,2,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,3,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,0,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,3,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,4,2), Blocks.stone_stairs);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,0,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,1,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,0,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,1,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,2), Blocks.stone_stairs);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,0,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,1,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,2), Blocks.stone_stairs);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,0,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,1,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,2,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,3), Blocks.air);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,0,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,1,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,2,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,3,3), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,4,2), Blocks.stone_stairs);
		
		this.fieldBiome.areas.add(biomeArea);
	}
	
	public void createLavaFountainBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();

		// Pilla with lava in it
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,3,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.cobblestone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.cobblestone);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.lava);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.glass);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,1,3), Blocks.glass);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,3,3), Blocks.glass);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.glass);
		
		this.lavaFountainBiome.areas.add(biomeArea);
		
		// Zig Zag Terrain
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,4,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.cobblestone);
		
		this.lavaFountainBiome.areas.add(biomeArea);
		
		// Lava underneath
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,1,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,4,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,1,3), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,3,3), Blocks.lava);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,3), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,3), Blocks.glass);
		
		this.lavaFountainBiome.areas.add(biomeArea);
		
		// Wall or lights
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,2,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.cobblestone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.glowstone);
		
		this.lavaFountainBiome.areas.add(biomeArea);
	}
}
