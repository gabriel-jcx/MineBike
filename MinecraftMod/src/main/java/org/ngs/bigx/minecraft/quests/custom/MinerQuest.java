package org.ngs.bigx.minecraft.quests.custom;

import java.util.Random;

import org.ngs.bigx.minecraft.npcs.custom.MinerNPC;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderMineRun;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;

public class MinerQuest extends CustomQuestAbstract
{
	//TODO: comment these members
	public static final int MINERUNDIMENSIONID = 220;
	private boolean pleaseStop;
	private ChunkCoordinates playerLoc;
	private TickEvent.WorldTickEvent worldEvent;
	private TickEvent.PlayerTickEvent playerEvent;
	private ChunkCoordinates[] playerLocation;
	private double posXofPlayer;
	private double posZofPlayer;
	public MinerQuest()
	{
		super();
		progress = 0;
		name = "MinerQuest";
		completed = false;
		pleaseStop = false;
		playerLoc = new ChunkCoordinates();
		worldEvent = new TickEvent.WorldTickEvent(null, null, null);
		playerEvent = new TickEvent.PlayerTickEvent(null, null);
		playerLocation = new ChunkCoordinates[3];
		posXofPlayer = 0;
		posZofPlayer = 0;
		register();
	}
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.xCoord, (int) MinerNPC.LOCATION.yCoord, (int) MinerNPC.LOCATION.zCoord);
		super.complete();
	}
	enum direction
	{
		NORTH,
		WEST;
	}
	@Override
	public void start() 	
	{
//		if (isStarted())
//			return;
		progress = 0;
		
		//teleport them to the soccer arena
//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
		QuestTeleporter.teleport(player, MINERUNDIMENSIONID, 6, 25, 5);
		
		super.start();
	}
	@Override
	public void onQuit(ClientDisconnectionFromServerEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.xCoord, (int) MinerNPC.LOCATION.yCoord, (int) MinerNPC.LOCATION.zCoord);	
	}
	
	static boolean worldLoaded = false;
	@Override
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		if (event.world.provider.dimensionId == MINERUNDIMENSIONID)
			worldLoaded = true;
	}
	
	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{
		worldEvent = event;
		World world= event.world.provider.worldObj;
		if (event.world.provider.worldObj.isRemote)
			return;
		
		if(worldLoaded && event.world.provider.dimensionId == WorldProviderMineRun.MINERUNDIMENSIONID && !pleaseStop)
		{
			System.out.println(posXofPlayer+"  "+posZofPlayer);
			
					//generateFloorAndCeiling(posXofPlayer,0,world);
					generateFloorAndCeilingWest(0,posZofPlayer,world);
					//generateWalls((int) (posXofPlayer)+1,posZofPlayer,world);
					generateWallsWest((int) (posXofPlayer),posZofPlayer,world);
					//pleaseStop=true;
					generateWall(20,0,world,direction.NORTH);
					generateWall(0,20,world,direction.WEST);

			//setting blocks in the world

		//	for(int c = 0; c < 5; c++)
		//	{
		//		generateFloorAndCeiling(0+(c*5),0,event);
		//		generateWalls(0+(c*5),0,event);	
		//	}
//				pleaseStop = true;
		}
			
	}
	
	@Override
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
    {
        System.out.println("Player Ticking");
        posXofPlayer = event.player.getPlayerCoordinates().posX;
        posZofPlayer = event.player.getPlayerCoordinates().posZ;

//		playerLoc = event.player.getPlayerCoordinates();
//        playerLocation[1] = playerLoc;
		//System.out.println("ticking");        
        if(!event.player.capabilities.isCreativeMode)
            event.player.setGameType(WorldSettings.getGameTypeById(1));
        playerEvent = event;
       
       
    }
	
	
//Generation of normal floor and ceiling north
	private void generateFloorAndCeiling(double startx, double startz, World world)
	{
		for(int x = 0; x < 10; x++)
		{
			for(int z = 0; z < 11; z++)
			{
				if(!world.isAirBlock((int) (startx+x), 20, (int) (startz+z)))
				{
				Random rand = new Random();
				int randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:
				 world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.iron_ore);
				break;
				case 1:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.gold_ore);
				break;
				case 2:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock((int) (startx+x), 20,(int) (startz+z), Blocks.glowstone);
				break;
				default:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.stone);
				break;
				}
				randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:	
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.iron_ore);
				break;
				case 1:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.gold_ore);
				break;
				case 2:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.glowstone);
				break;
//				case 5:
//					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.air);
//				break;
				default:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.stone);
				break;
				}
				}
			}
		}//end outer for loop
	}//end function
//	
//generate walls going north
	private void generateWalls(int startx, double startz, World world)
	{
		
		for(int x = 0; x < 5 ; x++)
		{
			for(int y = 20; y < 25; y++)
			{
				if(!world.isAirBlock((int) (startx+x), 20, (int) (startz+x)))
				{
				Random rand = new Random();
				int randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:
					world.setBlock(startx+x, y, 0, Blocks.iron_ore);
				break;
				case 1:
					world.setBlock(startx+x, y, 0, Blocks.gold_ore);
				break;
				case 2:
					world.setBlock(startx+x, y, 0, Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock(startx+x, y, 0, Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock(startx+x, y, 0, Blocks.glowstone);
				break;
				default:
					world.setBlock(startx+x, y, 0, Blocks.stone);
				break;
				}
				switch(randomInt)
				{case 0:
					world.setBlock(startx+x, y, 10, Blocks.iron_ore);
				break;
				case 1:
					world.setBlock(startx+x, y, 10, Blocks.gold_ore);
				break;
				case 2:
					world.setBlock(startx+x, y, 10, Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock(startx+x, y, 10, Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock(startx+x, y, 10, Blocks.glowstone);
				break;
				default:
					world.setBlock(startx+x, y, 10, Blocks.stone);
				break;
				}
				}
			}
		}
	}//end function
//	@SuppressWarnings("unused")
//	private void duplicateWalls(int amnt, int x, int y, int z, TickEvent.WorldTickEvent event)
//		{
//		World world = event.world.provider.worldObj;
//		for(int c = 0; c < amnt; c++)
//			{
//				generateWalls(x+(c*5), z, event);
//			}
//		}
	// generate walls but going to the right
	private void generateWallsWest(int startx, double startz, World world)
	{
		
		for(int x = 0; x < 5 ; x++)
		{
			for(int y = 20; y < 25; y++)
			{
				if(!world.isAirBlock((int) (startx+x), 20, (int) (startz+x)))
				{
				Random rand = new Random();
				int randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:
					world.setBlock(0, y, (int) (startz+x), Blocks.iron_ore);
				break;
				case 1:
					world.setBlock(0, y, (int) (startz+x), Blocks.gold_ore);
				break;
				case 2:
					world.setBlock(0, y, (int) (startz+x), Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock(0, y, (int) (startz+x), Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock(0, y, (int) (startz+x), Blocks.glowstone);
				break;
				default:
					world.setBlock(0, y, (int) (startz+x), Blocks.stone);
				break;
				}
				switch(randomInt)
				{case 0:
					world.setBlock(10, y, (int) (startz+x), Blocks.iron_ore);
				break;
				case 1:
					world.setBlock(10, y, (int) (startz+x), Blocks.gold_ore);
				break;
				case 2:
					world.setBlock(10, y, (int) (startz+x), Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock(10, y, (int) (startz+x), Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock(10, y, (int) (startz+x), Blocks.glowstone);
				break;
				default:
					world.setBlock(10, y, (int) (startz+x), Blocks.stone);
				break;
				}
				}
			}
		}
	}//end function
	//generateFloor and ceiling with the direction going right
	private void generateFloorAndCeilingWest(double startx, double startz, World world)
	{
		for(int x = 0; x < 11; x++)
		{
			for(int z = 0; z < 10; z++)
			{
				
				if(!world.isAirBlock((int) (startx+x), 20, (int) (startz+z)))
				{
				Random rand = new Random();
				int randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:
				 world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.iron_ore);
				break;
				case 1:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.gold_ore);
				break;
				case 2:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock((int) (startx+x), 20,(int) (startz+z), Blocks.glowstone);
				break;
				default:
					world.setBlock((int) (startx+x), 20, (int) (startz+z), Blocks.stone);
				break;
				}
				randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:	
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.iron_ore);
				break;
				case 1:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.gold_ore);
				break;
				case 2:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.redstone_ore);
				break;
				case 4:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.glowstone);
				break;
//				case 5:
//					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.air);
//				break;
				default:
					world.setBlock((int) (startx+x), 25, (int) (startz+z), Blocks.stone);
				break;
				}
				}
			}
		}//end outer for loop
	}//end function
	//master function for generation of single walls
	private void generateWall(double startx, double startz, World world, direction dir) 
	
	{
		switch(dir)
		{
		case NORTH:
			generateNorthWall(startx, startz, world);
		break;
		case WEST:
			generateWestWall(startx, startz, world);
		break;
		
		}
	}
	//sub function for generation of north walls
	private void generateNorthWall(double startx, double startz, World world) 
	
	{
		for(int x = 0; x < 10 ; x++)
		{
			for(int y = 20; y < 25; y++)
			{
				world.setBlock((int) (startx+x), y, 10, Blocks.stone);
			}
		}
	}
	//generation of a single right traveling wall
private void generateWestWall(double startx, double startz, World world) 
	
	{
		for(int x = 0; x < 10 ; x++)
		{
			for(int y = 20; y < 25; y++)
			{
				world.setBlock(10, y, (int) (startz+x), Blocks.stone);
			}
		}
	}
	
}//end class

			
	

