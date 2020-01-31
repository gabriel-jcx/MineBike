package org.ngs.bigx.minecraft.quests.chase;

import net.minecraft.init.Blocks;

public class ObstacleBiome {
	public TerrainBiomeDef obstacleBiomeDef;
	
	public ObstacleBiome()
	{
		this.obstacleBiomeDef = new TerrainBiomeDef();
		
		this.createObstacleBiome();
	}
	
	public TerrainBiomeArea getObstacleBiomeByIndex(int index)
	{
		if(this.obstacleBiomeDef == null)
			return null;
		
		if(this.obstacleBiomeDef.areas == null)
			return null;
		
		if(this.obstacleBiomeDef.areas.size() <= index)
			return null;
		
		return this.obstacleBiomeDef.areas.get(index);
	}
	
	public void createObstacleBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		//0 - Dirt Wall
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,1,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,0,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,0,2,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,2), Blocks.DIRT);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.DIRT);
		this.obstacleBiomeDef.areas.add(biomeArea);

		//1 - Trap
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(-3,-1,3,2), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,-1,3,2), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,-1,3,2), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,-1,3,2), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,-1,3,2), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,-1,3,2), Blocks.AIR);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,-1,3,2), Blocks.AIR);

		biomeArea.map.put(new TerrainBiomeAreaIndex(-3,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,2), Blocks.WATER);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//2 - Bush
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,3,2), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.leaves);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.leaves);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//3 - Water
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.WATER);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//4 - Trap
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.CACTUS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,3,2), Blocks.CACTUS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.CACTUS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,2), Blocks.CACTUS);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,2), Blocks.CACTUS);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//5 - Spring
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,2,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,2,3,2), Blocks.WATER);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//6 - Sudden Walls
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(-4,1,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-3,1,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,2), Blocks.COBBLESTONE);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//7 - Fire
		biomeArea = new TerrainBiomeArea();
		
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,2,2), Blocks.fire);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.fire);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//8 - Sudden Water Basin
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,0,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,1,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,6,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,6,2), Blocks.WATER);

		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,0,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,1,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,1,5,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,5,2), Blocks.WATER);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//9 - Sudden Walls 2
		biomeArea = new TerrainBiomeArea();

		biomeArea.map.put(new TerrainBiomeAreaIndex(-4,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-3,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,3,2), Blocks.COBBLESTONE);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,0,3,2), Blocks.COBBLESTONE);

		biomeArea.map.put(new TerrainBiomeAreaIndex(-4,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-2,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,1,3,2), Blocks.WATER);
		biomeArea.map.put(new TerrainBiomeAreaIndex(4,1,3,2), Blocks.WATER);
		this.obstacleBiomeDef.areas.add(biomeArea);
	}
}
