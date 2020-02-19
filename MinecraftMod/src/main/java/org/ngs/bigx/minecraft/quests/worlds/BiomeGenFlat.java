package org.ngs.bigx.minecraft.quests.worlds;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
//import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenFlat extends BiomeGenBase {

	public BiomeGenFlat(int par1) {
		super(par1);
		this.setBiomeName("Flat");
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.topBlock = Blocks.GRASS;
		this.fillerBlock = Blocks.DIRT;
		this.setHeight(height_LowPlains);
		this.theBiomeDecorator.treesPerChunk = -999;
		this.theBiomeDecorator.grassPerChunk = 1;
		this.theBiomeDecorator.flowersPerChunk = 1;
	}
}
