package org.ngs.bigx.minecraft.quests.worlds;

import org.ngs.bigx.minecraft.quests.custom.TRONQuest;

import net.minecraft.world.WorldType;

public class WorldProviderTRON extends WorldProviderFlats 
{
	public static String TRONDimName = "TRONDimension";
	
	@Override
	public String getDimensionName() {
		return TRONDimName;
	}
	
	public void registerWorldChunkManager() {
		this.dimensionId = TRONQuest.TRONDIMENSIONID;
		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
		this.hasNoSky = true;
		this.terrainType = WorldType.FLAT;
	}
}
