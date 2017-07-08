package org.ngs.bigx.minecraft.quests.chase;

import net.minecraft.init.Blocks;

public class ObstacleBiome {
	public TerrainBiomeDef obstacleBiomeDef;
	
	public ObstacleBiome()
	{
		this.obstacleBiomeDef = new TerrainBiomeDef();
		
		this.createObstacleBiome();
	}
	
	public TerrainBiomeArea getObstacleBiomeByIdex(int index)
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
		
		// SAMPLE
		biomeArea = new TerrainBiomeArea();
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(1,0,4,3), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,2,2), Blocks.sandstone_stairs);
		biomeArea.map.put(new TerrainBiomeAreaIndex(3,0,4,3), Blocks.sandstone_stairs);

		this.obstacleBiomeDef.areas.add(biomeArea);
	}
}
