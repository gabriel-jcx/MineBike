package org.ngs.bigx.minecraft.quests.chase.fire;

import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeDef;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TerrainBiomeFire {
	public TerrainBiomeDef gateBiome;
	public TerrainBiomeDef fieldBiome;
	public TerrainBiomeDef LAVAFountainBiome;
	
	public TerrainBiomeFire()
	{
		TerrainBiomeArea biomeArea;
		this.gateBiome = new TerrainBiomeDef();
		this.fieldBiome = new TerrainBiomeDef();
		this.LAVAFountainBiome = new TerrainBiomeDef();
		
		// SETBLOCK(x,y,x,direction,3)
		
		this.createGateBiome();
		this.createFieldBiome();
		this.createLAVAFountainBiome();
	}
	
	public TerrainBiomeArea getRandomGateBiome()
	{
		return this.gateBiome.getRandomArea();
	}
	
	public TerrainBiomeArea getRandomFieldBiome()
	{
		return this.fieldBiome.getRandomArea();
	}
	
	public TerrainBiomeArea getRandomLAVAFountainBiome()
	{
		return this.LAVAFountainBiome.getRandomArea();
	}
	
	public void createGateBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// Room
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,1,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,4,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,3,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,4,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,3,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,1,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,1,3), Blocks.IRON_BARS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,4,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.IRON_BARS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,3,3), Blocks.IRON_BARS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,3,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,2,3), Blocks.IRON_BARS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,3,3), Blocks.IRON_BARS);
		
		this.gateBiome.areas.add(biomeArea);
		
		// Pilla with LAVA in it
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,3,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.LAVA);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,1,3), Blocks.GLASS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,2,3), Blocks.GLASS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,3,3), Blocks.GLASS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,2,3), Blocks.GLASS);
		
		this.gateBiome.areas.add(biomeArea);
		
		// Transparent GLASS on the Floor
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.LAVA);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.GLASS);
		
		this.gateBiome.areas.add(biomeArea);
		
		// Cobble Stone pile
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.GLOWSTONE);
		
		this.gateBiome.areas.add(biomeArea);
	}
	
	public void createFieldBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// Lights
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,1,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,6,1,3), Blocks.GLOWSTONE);


		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,4,3), Blocks.GLOWSTONE);
		
		this.fieldBiome.areas.add(biomeArea);
		
		// LAVA underneath
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,1,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,4,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,1,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,3,3), Blocks.LAVA);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,3), Blocks.GLASS);
		
		this.fieldBiome.areas.add(biomeArea);
		
		// Pillars of lights
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,4,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,1,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,4,3), Blocks.GLOWSTONE);
		
		this.fieldBiome.areas.add(biomeArea);
		
		// Stair ways to go down and up
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,0,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,1,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,2,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,3,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,0,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,3,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,4,2), Blocks.STONE_STAIRS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,0,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,1,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,0,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,1,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,2), Blocks.STONE_STAIRS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,0,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,1,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,2), Blocks.STONE_STAIRS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,0,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,1,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,2,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,3), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,0,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,1,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,2,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,3,3), Blocks.GLOWSTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,4,2), Blocks.STONE_STAIRS);
		
		this.fieldBiome.areas.add(biomeArea);
	}
	
	public void createLAVAFountainBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();

		// Pilla with LAVA in it
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,5,3,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.COBBLESTONE);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.LAVA);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,1,3), Blocks.GLASS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,1,3), Blocks.GLASS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,4,3,3), Blocks.GLASS);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.GLASS);
		
		this.LAVAFountainBiome.areas.add(biomeArea);
		
		// Zig Zag Terrain
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,4,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.COBBLESTONE);
		
		this.LAVAFountainBiome.areas.add(biomeArea);
		
		// LAVA underneath
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-2,3,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,1,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,2,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,3,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-2,4,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,1,3), Blocks.LAVA);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-2,3,3), Blocks.LAVA);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,3), Blocks.GLASS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,3), Blocks.GLASS);
		
		this.LAVAFountainBiome.areas.add(biomeArea);
		
		// Wall or lights
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,2,1,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,1,3), Blocks.GLOWSTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.GLOWSTONE);
		
		this.LAVAFountainBiome.areas.add(biomeArea);
	}
}
