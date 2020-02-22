package org.ngs.bigx.minecraft.quests.worlds;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;

public class WorldProviderTRON extends WorldProviderFlats 
{
	public static final int TRONDIMENSIONID = 210;
	public static String TRONDimName = "TRONDimension";
	
	@Override
	public String getDimensionName() {
		return TRONDimName;
	}
	
//	public void registerWorldChunkManager() {
//		this.dimensionId = TRONDIMENSIONID;
//		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
//		this.hasNoSky = true;
//		this.terrainType = WorldType.FLAT;
//	}
 public void registerWorldChunkManager() {
	this.setDimension(TRONDIMENSIONID);
	Biome.BiomeProperties properties = new Biome.BiomeProperties("Flat");
	//this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlatCaves(properties), 0F);
	this.hasSkyLight = true;
	this.terrainType = WorldType.FLAT;

}
}
	