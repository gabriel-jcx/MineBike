package com.ramon.hellow;

import java.util.Random;

import com.ramon.hellow.worldgen.Structure;
import com.ramon.hellow.worldgen.StructureTower;
import com.ramon.hellow.worldgen.Theme;
import com.ramon.hellow.worldgen.ThemeDesert;
import com.ramon.hellow.worldgen.ThemeNorman;
import com.ramon.hellow.worldgen.WorldStructure;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
		for(int x=chunkX;x<chunkX+16;x++) {
			for (int z=chunkZ;z<chunkZ+16;z++) {
				if (random.nextInt(10000)<5) {
					int y = 200;
					while(world.getBlock(x,y,z)==Blocks.air||world.getBlock(x, y, z)==Blocks.snow_layer) {
						y--;
					}
					Block b = world.getBlock(x, y, z);
					if (b==Blocks.grass) {
						b = Blocks.dirt;
					}
					if (context.canGenerateOn.contains(b)) {
						Theme theme = context.getTheme(biome,random);
						if (theme==null) {
							return;
						}
						Structure structure = new StructureTower();
						int width = structure.getWidth();
						int length = structure.getLength();
						int depth = structure.getDepth();
						int height = structure.getHeight();
						for(int i=x-width/2;i<=x+width/2;i++) {
							for(int j=z-length/2;j<=z+length/2;j++) {
								boolean outlineX = (i==x-width/2||i==x+width/2);
								boolean outlineY = (j==z-length/2||j==z+length/2);
								boolean outline = (outlineX||outlineY);
								boolean corner = (outlineX&&outlineY);
								for (int k=y-depth;k<=y;k++) {
									world.setBlock(i,k,j,b);
								}
								for (int k=y;k<y+height;k++) {
									if (!outline) {
										world.setBlock(i, k, j, Blocks.air);
									}
								}
							}
						}
						structure.generate(world, x, y, z, theme, random);
						WorldStructure struct = new WorldStructure(theme.getName()+" "+structure.getName(),x,y,z,world,structure,theme);
						BikeWorldData.get(world).addStructure(struct);
						return;
					}
				}
			}
		}
		//this.addOreSpawn(Tutorial.tutorialBlock, world, random, x, z, 16, 16, 4 + random.nextInt(3), 5, 15, 50);
	}
	 
	private void generateNether(World world, Random random, int x, int z)
	{
		int Xcoord = x + random.nextInt(16);
	    int Ycoord = 10 + random.nextInt(128);
	    int Zcoord = z + random.nextInt(16);
	    //(new WorldGenMinable(Tutorial.tutorialBlock, 1, 15, Blocks.netherrack)).generate(world, random, Xcoord, Ycoord, Zcoord);
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