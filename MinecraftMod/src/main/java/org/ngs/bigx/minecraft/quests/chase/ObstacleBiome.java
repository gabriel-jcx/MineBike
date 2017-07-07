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
		
		if(this.obstacleBiomeDef.areas.size() >= index)
			return null;
		
		return this.obstacleBiomeDef.areas.get(index);
	}
	
	public void createObstacleBiome()
	{
		TerrainBiomeArea biomeArea = new TerrainBiomeArea();
		
		// SAMPLE
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.sandstone_stairs);

		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//1 - Dirt Wall
		biomeArea.map.clear();
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.dirt);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.dirt);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,2), Blocks.dirt);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//2 - Bush
		biomeArea.map.clear();
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.deadbush);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.deadbush);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.deadbush);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//3 - Water
		biomeArea.map.clear();
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.water);
		biomeArea.map.put(new TerrainBiomeAreaIndex(0,0,3,2), Blocks.water);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//4 - Trap
		biomeArea.map.clear();
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,3,2), Blocks.cactus);
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,1,3,2), Blocks.cactus);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//5 - Spring
		biomeArea.map.clear();
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,3,2), Blocks.lava);
		biomeArea.map.put(new TerrainBiomeAreaIndex(2,0,3,2), Blocks.lava);
		this.obstacleBiomeDef.areas.add(biomeArea);
		
		//6 - Fire
		biomeArea.map.clear();
		biomeArea.map.put(new TerrainBiomeAreaIndex(-1,0,2,2), Blocks.fire);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.fire);
		this.obstacleBiomeDef.areas.add(biomeArea);
	}
}
