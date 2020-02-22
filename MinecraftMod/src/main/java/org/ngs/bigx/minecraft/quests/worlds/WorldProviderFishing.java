package org.ngs.bigx.minecraft.quests.worlds;


import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.ngs.bigx.minecraft.quests.custom.FishingQuest;

public class WorldProviderFishing extends WorldProviderFlats
{
public static String fishingDimName = "FishingDimension";
public static final int fishingDimID = 230;
	
	@Override
	public String getDimensionName() {
		return fishingDimName;
	}
	public void registerWorldChunkManager() {
		this.setDimension(fishingDimID);
		Biome.BiomeProperties properties = new Biome.BiomeProperties("Flat");
		//this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlatCaves(properties), 0F);
		this.hasSkyLight = false;
		this.terrainType = WorldType.FLAT;

	}

//	public void registerWorldChunkManager() {
//		this.dimensionId = fishingDimID;
//		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
//		this.hasNoSky = false;
//		this.terrainType = WorldType.FLAT;
//	}

}
