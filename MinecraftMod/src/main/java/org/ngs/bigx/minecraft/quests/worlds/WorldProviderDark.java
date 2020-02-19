package org.ngs.bigx.minecraft.quests.worlds;

import net.minecraft.server.v1_15_R1.ChunkProviderFlat;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
/* import net.minecraft.world.gen.ChunkProviderFlat; */

public class WorldProviderDark extends WorldProvider{
	public static int dimID = 101;
	public static int fireQuestDimID = 101;
	
	public static int flatBiomeID = 51;
	public static String dimName = "Chasing - Caves";
	private static String flatGenPreset = "2;7,9x1,7x0,2x1;" + Integer.toString(flatBiomeID) + ";";
	public static int groundHeight = 11;
	
	
	public String getDimensionName() {
		return dimName;
	}
	
	public void registerWorldChunkManager() {
		this.setDimension(dimID);
		//Biome.BiomeProperties properties = new Biome.BiomeProperties("FlatCaves");
		//this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlatCaves(properties), 0F);
		this.hasSkyLight = false;
		this.
		this.terrainType = WorldType.FLAT;
		
	}
	
	public IChunkGenerator createChunkGenerator() {
		WorldType terrian = world.getWorldType();
		return terrian.getChunkGenerator(this.world, flatGenPreset);
		//return new ChunkProviderFlat(this.world, this.world.getSeed(), false, flatGenPreset);
	}
	
	public int getAverageGroundLevel() {
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int par1, int par2) {
		return false;
	}
	
	public boolean renderStars() {
		return true;
	}
	
	public float getStarBrightness(World world, float f) {
		return 10.0F;
	}
	
	public boolean renderClouds() {
		return true;
	}
	
	public boolean renderVoidFog() {
		return false;
	}
	
	public boolean renderEndSky() {
		return false;
	}
	
	public float setSunSize() {
		return 10.0F;
	}
	
	public float setMoonSize() {
		return 8.0F;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored() {
		return true;
	}
	
	public boolean canRespawnHere() {
		return false;
	}
	
	public boolean isSurfaceWorld() {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 128.0F;
	}
	
	@Override
	public float calculateCelestialAngle(long var1, float var2) {
		// Day is perpetual
		return 0.9F;
	}
	
	@Override
	public double getHorizon() {
		return 0.0D;
	}

	@Override
	public DimensionType getDimensionType() {
		// TODO: yet to be implemented
		return null;
	}
}
