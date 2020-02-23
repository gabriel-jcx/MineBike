package org.ngs.bigx.minecraft.quests.worlds;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
public class BiomeGenDungeon extends Biome {

	public BiomeGenDungeon(Biome.BiomeProperties properties) {

		super(properties);
		// Biome Name is now dealt with on the upper level before declaring the class obj.
		//this.setBiomeName("Dungeon");
//		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.topBlock = Blocks.STONE.getDefaultState();
		this.fillerBlock = Blocks.STONE.getDefaultState();
		//this.setHeight(height_LowPlains);
		this.decorator.treesPerChunk = -999;
		this.decorator.grassPerChunk = -999;
		this.decorator.flowersPerChunk = -999;
	}
}
