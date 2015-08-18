package org.ngs.bigx.minecraft;

import java.util.List;
import java.util.Random;

import org.ngs.bigx.minecraft.networking.ReceiveQuestMessage;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.structures.Structure;
import org.ngs.bigx.minecraft.structures.StructureTower;
import org.ngs.bigx.minecraft.structures.WorldStructure;
import org.ngs.bigx.minecraft.themes.Theme;
import org.ngs.bigx.minecraft.themes.ThemeDesert;
import org.ngs.bigx.minecraft.themes.ThemeNorman;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGen implements IWorldGenerator {
	
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.dimensionId)
		{
		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);
			break;
		case 0:
			generateSurface(world, random, chunkX * 16, chunkZ * 16, Main.instance().context);
			break;
		case 1:
			generateEnd(world, random, chunkX * 16, chunkZ * 16);
			break;
		}
	}
	 
	private void generateEnd(World world, Random random, int x, int z)
	{
	 
	}
	 
	private void generateSurface(World world, Random random, int chunkX, int chunkZ, Context context)
	{
		BiomeGenBase biome = world.getBiomeGenForCoords(chunkX, chunkZ);
		int structure_separation = 5;
		//We only want to generate in chunks that are multiples of a certain number, to prevent structures from being near each other
		if (!(chunkX%structure_separation==0&&chunkZ%structure_separation==0)) {
			return;
		}
		//Low probability so we don't have a very obvious square arrangment
		if (random.nextInt(5)>1) {
			return;
		}
		//Check every block in the chunk
		for(int x=chunkX;x<chunkX+16;x++) {
			for (int z=chunkZ;z<chunkZ+16;z++) {
				//Allow it to be spawned anywhere in the chunk's 16x16 grid
				if (random.nextInt(16*16)>1) {
					continue;
				}
				int y = 200;
				//Blocks that cannot support a structure (make this into a data structure of some kind?)
				while(world.getBlock(x,y,z)==Blocks.air||world.getBlock(x, y, z)==Blocks.snow_layer) {
					y--;
				}
				Block block = world.getBlock(x, y, z);
				int meta = world.getBlockMetadata(x, y, z);
				//Don't want to place a structure on grass - only on dirt
				if (block==Blocks.grass) {
					block = Blocks.dirt;
				}
				//If we can generate a structure on this block
				if (!context.canGenerateOn.contains(block)) {
					continue;
				}
				//Get a theme for this biome
				Theme theme = context.getTheme(biome,random);
				//If no themes work in this biome
				if (theme==null) {
					return;
				}
				//Allocate memory
				theme.allocateMemory();
				//Pick a structure
				Structure structure = context.getStructure(theme,random);
				int width = structure.getWidth();
				int length = structure.getLength();
				int depth = structure.getDepth();
				int height = structure.getHeight();
				//Carve out some space in the world for the structure
				for(int i=x-width/2;i<=x+width/2;i++) {
					for(int j=z-length/2;j<=z+length/2;j++) {
						for (int k=y-depth;k<=y;k++) {
							world.setBlock(i,k,j,block,meta,3);
						}
						for (int k=y;k<y+height;k++) {
							world.setBlock(i, k, j, Blocks.air);
						}
					}
				}
				//Build it!
				structure.generate(world, x, y, z, theme, random);
				WorldStructure struct = new WorldStructure(structure.getFullName(theme),x,y,z,world,structure,theme);
				//Keep track of it in our world data
				BikeWorldData.get(world).addStructure(struct);
				//Create a new quest and send it to the client - just a test for now
				Quest quest = new Quest(StatCollector.translateToLocal("quest.type.explore")+" "+structure.getFullName(theme),false);
				ReceiveQuestMessage message = new ReceiveQuestMessage(quest);
				context.main.network.sendToAll(message);
				//Deallocate memory
				theme.deallocateMemory();
				return;
			}
		}
	}
	 
	private void generateNether(World world, Random random, int x, int z)
	{
		int Xcoord = x + random.nextInt(16);
	    int Ycoord = 10 + random.nextInt(128);
	    int Zcoord = z + random.nextInt(16);
	}
	 
	/**
	* Adds an Ore Spawn to Minecraft. Simply register all Ores to spawn with this method in your Generation method in your IWorldGeneration extending Class
	*
	* @param The Block to spawn
	* @param The World to spawn in
	* @param A Random object for retrieving random positions within the world to spawn the Block
	* @param An int for passing the X-Coordinate for the Generation method
	* @param An int for passing the Z-Coordinate for the Generation method
	* @param An int for setting the maximum X-Coordinate values for spawning on the X-Axis on a Per-Chunk basis
	* @param An int for setting the maximum Z-Coordinate values for spawning on the Z-Axis on a Per-Chunk basis
	* @param An int for setting the maximum size of a vein
	* @param An int for the Number of chances available for the Block to spawn per-chunk
	* @param An int for the minimum Y-Coordinate height at which this block may spawn
	* @param An int for the maximum Y-Coordinate height at which this block may spawn
	**/
	public void addOreSpawn(Block block, World world, Random random, int blockXPos, int blockZPos, int maxX, int maxZ, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
	{
		assert maxY > minY : "The maximum Y must be greater than the Minimum Y";
		assert maxX > 0 && maxX <= 16 : "addOreSpawn: The Maximum X must be greater than 0 and less than 16";
		assert minY > 0 : "addOreSpawn: The Minimum Y must be greater than 0";
		assert maxY < 256 && maxY > 0 : "addOreSpawn: The Maximum Y must be less than 256 but greater than 0";
		assert maxZ > 0 && maxZ <= 16 : "addOreSpawn: The Maximum Z must be greater than 0 and less than 16";
		 
		int diffBtwnMinMaxY = maxY - minY;
		for (int x = 0; x < chancesToSpawn; x++)
		{
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			//(new WorldGenMinable(block, maxVeinSize)).generate(world, random, posX, posY, posZ);
		}
	}
}
