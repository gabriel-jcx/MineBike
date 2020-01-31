package org.ngs.bigx.minecraft.quests.worlds;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenFlatCaves extends BiomeGenBase {
	
	public BiomeGenFlatCaves(int par1) {
		super(par1);
		this.setBiomeName("FlatCaves");
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.topBlock = Blocks.STONE;
		this.fillerBlock = Blocks.STONE;
		this.setHeight(height_LowPlains);
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.grassPerChunk = -999;
		this.theBiomeDecorator.flowersPerChunk = -999;
	}
}
