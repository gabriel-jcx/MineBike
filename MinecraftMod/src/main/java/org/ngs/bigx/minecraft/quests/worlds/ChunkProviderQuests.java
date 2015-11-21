package org.ngs.bigx.minecraft.quests.worlds;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;

public class ChunkProviderQuests implements IChunkProvider
{
	private Random rand;
	private World worldObj;
	private final boolean mapFeaturesEnabled;
	private BiomeGenBase[] biomesForGeneration;
	float[] parabolicField;
	int[][] field_73219_j = new int[32][32];
	
	public ChunkProviderQuests(World par1World, long par2, boolean par4) {
		this.worldObj = par1World;
		this.mapFeaturesEnabled = par4;
		this.rand = new Random(par2);
	}
	
	public void generateTerrain(int par1, int par2, byte[] par3ArrayOfByte) {
		//stub
	}
		
	public Chunk loadChunk(int par1, int par2) {
		return provideChunk(par1, par2);
	}
	
	public Chunk provideChunk(int par1, int par2)
	{
		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
		byte[] var3 = new byte[32768];
		generateTerrain(par1, par2, var3);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
		Chunk var4 = new Chunk(this.worldObj, par1, par2);
		byte[] var5 = var4.getBiomeArray();
		for (int var6 = 0; var6 < var5.length; var6++)
		{
			var5[var6] = ((byte)this.biomesForGeneration[var6].biomeID);
		}
		var4.generateSkylightMap();
		return var4;
	}
	
	public boolean chunkExists(int par1, int par2) {
		return true;
	}
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
		//stub
	}
	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		return true;
	}
	public boolean unload100OldestChunks() {
		return false;
	}
	public boolean canSave() {
		return true;
	}
	public String makeString() {
		return "QuestSource";
	}
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		//BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(par2, par4);
		//return var5 == null ? null : var5.getSpawnableList(par1EnumCreatureType);
		return null;
	}
	public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
		return null;
	}
	
	public int getLoadedChunkCount() {
		return 0;
	}
	
	public boolean unloadQueuedChunks() {
		return false;
	}
	
	public void recreateStructures(int i, int j) {
	}
	@Override
	public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_,
			int p_147416_5_) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void saveExtraData() {
		// TODO Auto-generated method stub
		
	}
}
