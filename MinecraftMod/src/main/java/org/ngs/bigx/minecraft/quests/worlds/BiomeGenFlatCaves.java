package org.ngs.bigx.minecraft.quests.worlds;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeGenFlatCaves extends Biome {
	
	public BiomeGenFlatCaves(Biome.BiomeProperties properties) {
		super(properties);
		//this.setBiomeName("FlatCaves");
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.topBlock = Blocks.STONE.getDefaultState();
		this.fillerBlock = Blocks.STONE.getDefaultState();
		//this.setHeight(height_LowPlains);
		this.decorator.treesPerChunk = -999;
		this.decorator.grassPerChunk = -999;
		this.decorator.flowersPerChunk = -999;
	}
}
