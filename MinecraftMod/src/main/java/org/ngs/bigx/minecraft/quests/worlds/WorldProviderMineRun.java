package org.ngs.bigx.minecraft.quests.worlds;


import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public class WorldProviderMineRun extends WorldProviderFlats 
{
	public static String mineRunDimName = "MineRunDimension";
	public static final int MINERUNDIMENSIONID = 220;
	@Override
	public String getDimensionName() {
		return mineRunDimName;
	}
	
//	public void registerWorldChunkManager() {
//		this.dimensionId = MINERUNDIMENSIONID;
//		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
//		this.hasNoSky = false;
//		this.terrainType = WorldType.FLAT;
//	}
	public void registerWorldChunkManager() {
	this.setDimension(MINERUNDIMENSIONID);
	Biome.BiomeProperties properties = new Biome.BiomeProperties("Flat");
	//this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlatCaves(properties), 0F);
	this.hasSkyLight = false;
	this.terrainType = WorldType.FLAT;

}

	@Override
	public DimensionType getDimensionType() {
		return null;
	}
}
