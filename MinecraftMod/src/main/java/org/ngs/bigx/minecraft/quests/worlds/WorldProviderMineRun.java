package org.ngs.bigx.minecraft.quests.worlds;


import net.minecraft.world.WorldType;

public class WorldProviderMineRun extends WorldProviderFlats 
{
	public static String mineRunDimName = "MineRunDimension";
	public static final int MINERUNDIMENSIONID = 220;
	@Override
	public String getDimensionName() {
		return mineRunDimName;
	}
	
	public void registerWorldChunkManager() {
		this.dimensionId = MINERUNDIMENSIONID;
		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
		this.hasNoSky = false;
		this.terrainType = WorldType.FLAT;
	}
}
