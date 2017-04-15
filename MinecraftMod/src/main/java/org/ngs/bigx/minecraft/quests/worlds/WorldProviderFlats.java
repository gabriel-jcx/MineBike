package org.ngs.bigx.minecraft.quests.worlds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;

public class WorldProviderFlats extends WorldProvider {

	public static int dimID = 100;
	public static int fireQuestDimID = 110;
	
	public static int flatBiomeID = 50;
	public static String dimName = "Quest World";
	private static String flatGenPreset = "2;7,5x1,3x3,2;" + Integer.toString(flatBiomeID) + ";decoration";
	public static int groundHeight = 11;
	
	@Override
	public String getDimensionName() {
		return dimName;
	}
	
	public void registerWorldChunkManager() {
		this.dimensionId = dimID;
		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(new BiomeGenFlat(50), 0F);
		this.hasNoSky = false;
		this.terrainType = WorldType.FLAT;
		
	}
	
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), false, flatGenPreset);
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
}