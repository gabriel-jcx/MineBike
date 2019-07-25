package org.ngs.bigx.minecraft.quests.worlds;


import net.minecraft.world.WorldType;

public class WorldProviderFishing extends WorldProviderFlats
{
public static String fishingDimName = "FishingDimension";
public static int fishingDimID = 230;
	
	@Override
	public String getDimensionName() {
		return fishingDimName;
	}
	
	public void registerWorldChunkManager() {
		this.dimensionId = fishingDimID;
		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
		this.hasNoSky = false;
		this.terrainType = WorldType.FLAT;
	}
}
