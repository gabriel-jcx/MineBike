package org.ngs.bigx.minecraft.quests.worlds;

import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;

import net.minecraft.world.WorldType;

public class WorldProviderOvercooked extends WorldProviderFlats 
{
	public static String overcookedDimName = "OvercookedDimension";
	public static final int overcookedDimID = 240;
	
	@Override
	public String getDimensionName()
	{
		return overcookedDimName;
	}
	
	public void registerWorldChunkManager() 
	{
		this.dimensionId = overcookedDimID;
		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
		this.hasNoSky = false;
		this.terrainType = WorldType.FLAT;
	}
}
