package com.ramon.hellow;

import java.util.List;
import java.util.Random;

import com.ramon.hellow.worldgen.Structure;
import com.ramon.hellow.worldgen.StructureTower;
import com.ramon.hellow.worldgen.Theme;
import com.ramon.hellow.worldgen.ThemeDesert;
import com.ramon.hellow.worldgen.ThemeNorman;
import com.ramon.hellow.worldgen.WorldStructure;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
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
		//Check every block in the chunk
		for(int x=chunkX;x<chunkX+16;x++) {
			for (int z=chunkZ;z<chunkZ+16;z++) {
				//Low probability - so we don't have structures everywhere!
				if (random.nextInt(10000)<5) {
					int y = 200;
					//Blocks that cannot support a structure (make this into a data structure of some kind?)
					while(world.getBlock(x,y,z)==Blocks.air||world.getBlock(x, y, z)==Blocks.snow_layer) {
						y--;
					}
					Block b = world.getBlock(x, y, z);
					//Don't want to place a structure on grass - only on dirt
					if (b==Blocks.grass) {
						b = Blocks.dirt;
					}
					/*Code below is unusable because it introduces order dependence - the way chunks generate is dependent upon when they are generated
					 * for a procedural game like Minecraft this is VERY BAD
					List<WorldStructure> structs = BikeWorldData.get(world).getStructures();
					for (WorldStructure ws:structs) {
						int xc = ws.getX();
						int yc = ws.getY();
						int zc = ws.getZ();
						//Minimum distance from other structures
						if (Math.sqrt(Math.pow(xc-x,2)+Math.pow(yc-y, 2)+Math.pow(zc-z, 2))<200) {
							return;
						}
					}*/
					//If we can generate a structure on this block
					if (context.canGenerateOn.contains(b)) {
						//Get a theme for this biome
						Theme theme = context.getTheme(biome,random);
						//If no themes work in this biome
						if (theme==null) {
							return;
						}
						//Pick a structure
						Structure structure = context.getStructure(theme,random);
						int width = structure.getWidth();
						int length = structure.getLength();
						int depth = structure.getDepth();
						int height = structure.getHeight();
						//Carve out some space in the world for the structure
						for(int i=x-width/2;i<=x+width/2;i++) {
							for(int j=z-length/2;j<=z+length/2;j++) {
								boolean outlineX = (i==x-width/2||i==x+width/2);
								boolean outlineZ = (j==z-length/2||j==z+length/2);
								boolean outline = (outlineX||outlineZ);
								boolean corner = (outlineX&&outlineZ);
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
						//Build it!
						structure.generate(world, x, y, z, theme, random);
						WorldStructure struct = new WorldStructure(I18n.format("world.theme."+theme.getName(),new Object[0])+" "+I18n.format("world.structure."+structure.getName(),new Object[0]),x,y,z,world,structure,theme);
						//Keep track of it in our world data
						BikeWorldData.get(world).addStructure(struct);
						return;
					}
				}
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
