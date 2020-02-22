package org.ngs.bigx.minecraft.quests.worlds;

import net.minecraft.world.biome.Biome;
import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;

import net.minecraft.world.WorldType;

public class WorldProviderSoccer extends WorldProviderFlats 
{
	public static String soccerDimName = "SoccerDimension";
	
	@Override
	public String getDimensionName() {
		return soccerDimName;
	}
	
//	public void registerWorldChunkManager() {
//		this.dimensionId = SoccerQuest.SOCCERDIMENSIONID;
//		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(flatBiomeID), 0F);
//		this.hasNoSky = false;
//		this.terrainType = WorldType.FLAT;
//	}
	public void registerWorldChunkManager() {
	this.setDimension(SoccerQuest.SOCCERDIMENSIONID);
	Biome.BiomeProperties properties = new Biome.BiomeProperties("Flat");
	//this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlatCaves(properties), 0F);
	this.hasSkyLight = false;
	this.terrainType = WorldType.FLAT;

}
}
