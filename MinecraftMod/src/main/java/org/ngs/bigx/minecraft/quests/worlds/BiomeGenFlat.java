package org.ngs.bigx.minecraft.quests.worlds;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
//import net.minecraft.world.biome.BiomeGenBase;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeVoid;

// TODO: figure out if we need to set the height of the Biome, and how much the height need to be
public class BiomeGenFlat extends Biome {

	public BiomeGenFlat(Biome.BiomeProperties properties) {
		//
		super(properties);
		//properties.setBaseBiome("Flat");
		//Biome.BiomeProperties properties = new BiomeProperties("Flat");

		//this.setBiomeName("Flat");
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.topBlock = Blocks.GRASS.getDefaultState();
		this.fillerBlock = Blocks.DIRT.getDefaultState();
		//this.setHeight(height_LowPlains);
		this.decorator.treesPerChunk = -999;
		this.decorator.grassPerChunk = 1;
		this.decorator.flowersPerChunk = 1;
	}
}
