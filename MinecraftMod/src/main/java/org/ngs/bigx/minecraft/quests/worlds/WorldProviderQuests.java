package org.ngs.bigx.minecraft.quests.worlds;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderQuests extends WorldProvider {

	@Override
	public String getDimensionName() {
		return "Quest World";
	}
	
	public void registerWorldChunkManager() {
		this.dimensionId = 2;
		this.worldChunkMgr = new net.minecraft.world.biome.WorldChunkManagerHell(BiomeGenBase.hell, this.dimensionId);
		this.hasNoSky = false;
	}
	
	public IChunkProvider createChunkGenerator() {
		return null;
		//return new ChunkProviderTutorial(this.worldObj, this.worldObj.getSeed(), false);
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

}