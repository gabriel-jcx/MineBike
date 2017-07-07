package org.ngs.bigx.minecraft.quests.chase;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TerrainBiome {
	public TerrainBiomeDef grassBiome;
	public TerrainBiomeDef cityBiome;
	public TerrainBiomeDef desertBiome;
	
	public TerrainBiome()
	{
		this.grassBiome = new TerrainBiomeDef();
		this.cityBiome = new TerrainBiomeDef();
		this.desertBiome = new TerrainBiomeDef();
		
		// SETBLOCK(x,y,x,direction,3)
		
		this.createGrassBiome();
		this.createCityBiome();
		this.createDesertBiome();
	}
	
	public TerrainBiomeArea getRandomGrassBiome()
	{
		return this.grassBiome.getRandomArea();
	}
	
	public TerrainBiomeArea getRandomCityBiome()
	{
		return this.cityBiome.getRandomArea();
	}
	
	public TerrainBiomeArea getRandomDesertBiome()
	{
		return this.desertBiome.getRandomArea();
	}
	
	public void createGrassBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// Grass 1
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.sandstone_stairs);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.sandstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,2), Blocks.sandstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.sandstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,3), Blocks.leaves);
		
		this.grassBiome.areas.add(biomeArea);
		
		// Grass 2
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.log);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,4,3), Blocks.leaves);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.leaves);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,4,3), Blocks.leaves);
		
		this.grassBiome.areas.add(biomeArea);
		
		// Grass 3
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,1,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,1,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,2), Blocks.water);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,2), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,2,3), Blocks.water);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,3,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,2), Blocks.water);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,4,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,4,3), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,4,2), Blocks.sandstone_stairs);
		
		this.grassBiome.areas.add(biomeArea);
		
		// Grass 4
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,2,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,4,3), Blocks.leaves2);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.leaves2);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.leaves2);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.leaves2);
		
		this.grassBiome.areas.add(biomeArea);
	}
	
	public void createCityBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// City 1
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,2,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,1,3), Blocks.log);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,3), Blocks.log);

		biomeArea.map.put(new TerrainBiomeAreaIndex(0,3,0,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,3,1,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,0,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,2), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,0,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,1,2), Blocks.wool);

		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,2,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,2,2), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,2,2), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,2,2), Blocks.wool);
		

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,3), Blocks.log);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,0,0,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,0,1,3), Blocks.log);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,0,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,1,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,0,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,2), Blocks.glowstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,3,0,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,3,1,2), Blocks.wool);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,2,2), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,2,2), Blocks.glass);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,0,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,1,2,2), Blocks.wool);
		biomeArea.map.put(new TerrainBiomeAreaIndex(5,2,2,2), Blocks.wool);
		
		this.cityBiome.areas.add(biomeArea);
		
		// City 2
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,2,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,3,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.log);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.log);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,5,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,5,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,5,4,3), Blocks.leaves);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,4,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,4,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,4,4,3), Blocks.leaves);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,6,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,6,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,2,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,3,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,6,4,3), Blocks.leaves);
		
		this.cityBiome.areas.add(biomeArea);
		
		// City 3
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,3,4,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,1,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,1,3), Blocks.glowstone);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,4,3), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,3,4,3), Blocks.glowstone);
		
		this.cityBiome.areas.add(biomeArea);
		
		// City 4
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,4,3), Blocks.planks);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,1,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,5,3), Blocks.planks);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,2,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,4,3), Blocks.planks);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,4,3), Blocks.planks);
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,4,3), Blocks.planks);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,5,3), Blocks.planks);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,4,3), Blocks.planks);

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,4,3), Blocks.anvil);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,4,3), Blocks.anvil);
		
		this.cityBiome.areas.add(biomeArea);
	}
	
	public void createDesertBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// Desert 1
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,1,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,1,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,1,3), Blocks.cactus);

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,4,3), Blocks.cactus);

		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,2,3), Blocks.cactus);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,2,3,3), Blocks.cactus);
		
		this.desertBiome.areas.add(biomeArea);
		
		// Desert 2
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,3,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,4,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,2,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,4,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,1,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,2,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,2), Blocks.oak_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,1,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,2,3), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,-1,3,2), Blocks.oak_stairs);
		
		this.desertBiome.areas.add(biomeArea);
		
		// Desert 3
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,2,3,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,3,3,3), Blocks.cactus);

		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,1,3), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,1,3), Blocks.cactus);
		
		this.desertBiome.areas.add(biomeArea);
		
		// Desert 4
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,0), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,1,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,2,3), Blocks.sandstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,0), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,3), Blocks.sandstone);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,1), Blocks.sandstone_stairs);
		
		this.desertBiome.areas.add(biomeArea);
	}
}
