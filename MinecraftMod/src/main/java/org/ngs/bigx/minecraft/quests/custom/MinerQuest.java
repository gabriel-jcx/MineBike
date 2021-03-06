package org.ngs.bigx.minecraft.quests.custom;
	
import java.time.Clock;
import java.util.Random;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.math.BlockPos;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.npcs.custom.MinerNPC;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderMineRun;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderTRON;

// com.mojang.realmsclient.gui.EditOnlineWorldScreen;

//import cpw.mods.fml.common.gameevent.TickEvent;
//import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;

public class MinerQuest extends CustomQuestAbstract
{
 public static final int MINERUNDIMENSIONID = 220;
		private boolean pleaseStop;
		//private ChunkCoordinates playerLoc;
		//private TickEvent.WorldTickEvent worldEvent;
		//private TickEvent.PlayerTickEvent playerEvent;
		//private ChunkCoordinates[] playerLocation;
		private int lastPosNum;
		//these are used to keep track of all of the players locations
		private double posXofPlayer;
		private double posZofPlayer;
		private int posXofLava;
		private int currentTick;
		private boolean initialize;
		
		private HudRectangle timerRectangle;
		private HudString timerString;
		private int seconds;
		private static Clock clock;
		private String secondsString;
		private int startTime;
		private double posYofPlayer;
		private long TIME;
		private Clock fireClock;
		private long fireTIME;
		
		private int numMsForFireToSpawn;
		
		public MinerQuest()
		{
			super();
			//initializing all private variables
			progress = 0;
			name = "MinerQuest";
			completed = false;
			pleaseStop = false;
			//playerLoc = new ChunkCoordinates();
			//worldEvent = new TickEvent.WorldTickEvent(null, null, null);
			//playerEvent = new TickEvent.PlayerTickEvent(null, null);
			///playerLocation = new ChunkCoordinates[3];
			posXofPlayer = 0;
			posZofPlayer = 0;
			posYofPlayer = 0;
			lastPosNum = 0;
			posXofLava = -40;
			currentTick = 0;
			timerRectangle = new HudRectangle(-25, 5, 50, 30, 0xff0000ff, true, false);
			clock = Clock.systemDefaultZone();
			fireClock = Clock.systemDefaultZone();
			seconds = 60;
			secondsString += seconds;
			timerString = new HudString(0, 10, secondsString, 2.0f ,true,false);
			register();
			
			numMsForFireToSpawn = 180;
			
			instructionTextureLocations = new ResourceLocation[]  
				{	
				new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/instructions/MinerQuestInstruction1.png"),
				new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/instructions/MinerQuestInstruction2.png"),
				new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/instructions/MinerQuestInstruction3.png"),
				};
			instructionStringContents = new String[]
				{
				"Run from the fire",
				"Collect Gold",
				"Stay alive until time runs out",
				};
		}
		
		@Override
		public void onItemPickUp(EntityItemPickupEvent event)
		{
			//QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.xCoord, (int) MinerNPC.LOCATION.yCoord, (int) MinerNPC.LOCATION.zCoord);
			//super.complete();
			
		}
		enum direction
		{
			NORTH,
			WEST;
		}
		@Override
		public void start() 	
		{
//			if (isStarted())
//				return;
			progress = 0;
			TIME = clock.millis();
			//teleport them to the soccer arena
	//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
			QuestTeleporter.teleport(player, MINERUNDIMENSIONID, 7, 25, 5);
			
			super.start();
		}
		@Override
		public void onQuit(ClientDisconnectionFromServerEvent event)
		{
			
			QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.x, (int) MinerNPC.LOCATION.y, (int) MinerNPC.LOCATION.z);
		}
		
		static boolean worldLoaded = false;
		@Override
		public void onWorldLoadEvent(WorldEvent.Load event)
		{
			if (event.getWorld().provider.getDimension() == MINERUNDIMENSIONID)
				worldLoaded = true;
			initialize = false;
			posXofLava = -40;
			currentTick = 0;
			HudManager.registerRectangle(timerRectangle);
			HudManager.registerString(timerString);
		}

		private void clearStartPotHoles(int startx, int startz, World world) 
		{
				for(int x = startx; x < startx+200; x++)
				{
					for(int z = 0; z < 11; z++)
					{
						Random rand = new Random();
						int randomInt = rand.nextInt(10);
						BlockPos pos = new BlockPos((int)(startx)+x,20, (int)(startz)+z);
						switch(randomInt)
						{
						case 0:
							world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
							//world.setBlock((int) (startx)+x, 20, (int) (startz+z),Blocks.iron_ore);
						break;
						case 1:
							world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
							//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.gold_ore);
						break;
						case 2:
							world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
							//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.diamond_ore);
						break;
						case 3:
							world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState());
							//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.redstone_ore);
						break;
						case 4:
							world.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState());
							//world.setBlock((int) (startx)+x, 20,(int) (startz+z), Blocks.glowstone);
						break;
						default:
							world.setBlockState(pos, Blocks.STONE.getDefaultState());
							//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.STONE);
						break;
						}
						//set it to air above
						pos = pos.add(0,1,0);
						world.setBlockState(pos,Blocks.AIR.getDefaultState());
						//world.setBlock((int) startx + x, 21, (int) (startz + z), Blocks.AIR);
					}
				}
		}
		private void clearPotHoles(int startx, int startz, World world) 
		{
			for(int z = 0; z < 11; z++)
			{
				
				Random rand = new Random();
				int randomInt = rand.nextInt(10);
				BlockPos pos = new BlockPos((int)(startx),20, (int)(startz)+z);
				switch(randomInt)
				{
					case 0:
						world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
						//world.setBlock((int) (startx)+x, 20, (int) (startz+z),Blocks.iron_ore);
						break;
					case 1:
						world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
						//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.gold_ore);
						break;
					case 2:
						world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
						//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.diamond_ore);
						break;
					case 3:
						world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState());
						//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.redstone_ore);
						break;
					case 4:
						world.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState());
						//world.setBlock((int) (startx)+x, 20,(int) (startz+z), Blocks.glowstone);
						break;
					default:
						world.setBlockState(pos, Blocks.STONE.getDefaultState());
						//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.STONE);
						break;
				}
			}
		}
		@Override
		public void onWorldTickEvent(TickEvent.WorldTickEvent event)
		{
			World world= event.world;
			if (event.world.isRemote)
				return;
			
			if(worldLoaded && event.world.provider.getDimension() == WorldProviderMineRun.MINERUNDIMENSIONID && !pleaseStop)
			{
				
				
				if (super.showingInstructions())
				{
					resetToAirStart(posXofPlayer - 1,0,world);
					resetToAirStart(posXofPlayer - 1,0,world);
					BiGX.instance().clientContext.lock(true);
					return;
				}
				else
				{
					TIME = clock.millis();
					BiGX.instance().clientContext.lock(false);
				}
				
						
					//if(posXofPlayer-35>lastPosNum)
						//{
						int secondsPassed = (int)((clock.millis()-TIME)/1000);
						if(secondsPassed == seconds||posYofPlayer<19)
						{
							super.complete();
							HudManager.unregisterRectangle(timerRectangle);
							HudManager.unregisterString(timerString);
							currentTick = 0;
							QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.x, (int) MinerNPC.LOCATION.y, (int) MinerNPC.LOCATION.z);
							initialize = false;
						}
						timerString.text = ""+(seconds - secondsPassed);
						generateWestWall(5,0,world);
						if(!initialize)
						{
							resetToAirStart(-40,0,world);
							clearStartPotHoles(0,0,world);
							initialize = true;
						}
						resetToAirCont(posXofPlayer+50,0,world);
						resetToAirCont(posXofPlayer+50,0,world);
						clearPotHoles((int) (posXofPlayer+50),0,world);
						clearPotHoles((int) (posXofPlayer+50),0,world);
						if(posXofPlayer - posXofLava >=40)
						{
							if (clock.millis() - fireTIME >= numMsForFireToSpawn)
							{
								generateLavaWall(posXofLava, 0, world);
								fireTIME = clock.millis();
								posXofLava++;
							}
						}
						else
						{
							if (clock.millis() - fireTIME >= numMsForFireToSpawn)
							{
								generateLavaWall(posXofLava, 0, world);
								fireTIME = clock.millis();
								posXofLava++;
							}
						}
						
						generateFloorAndCeiling(posXofPlayer-1,0,world);
						generateWalls((int) (posXofPlayer)-1,posZofPlayer,world);
						Random rand = new Random();
						Random rand2 = new Random();
						int randomInt2 = rand2.nextInt(8);
						int randomInt = rand.nextInt(150);
						int randomInt3 = rand.nextInt(300);
						int randomInt4 = rand2.nextInt(7)+2;
						int randomInt5 = rand2.nextInt(1000);
						switch(randomInt)
						{							
							case 1:
							generateEmeraldWall(posXofPlayer+20,0,world);
							break;
							case 2:
							createPotHole(posXofPlayer+20,randomInt2+2,world);
							case 3:
							case 4:
							case 5:
							//EntityItem entityitem1 = new EntityItem(event.world.provider.worldObj, posXofPlayer+20, 21, randomInt4, new ItemStack(Items.gold_ingot,1));
							//event.world.provider.worldObj.spawnEntityInWorld(entityitem1);
							break;
						}
						switch(randomInt5)
						{							
							case 1:
							EntityItem entityitem1 = new EntityItem(event.world, posXofPlayer+20, 21, randomInt4, new ItemStack(Items.GOLD_INGOT,1));
							event.world.spawnEntity(entityitem1);
							break;
							
						}
						if (posXofPlayer % 2 == 0)
						{
							switch(randomInt3)
							{
								
								case 1:
								case 3:
								if(!isWall(posXofPlayer+20, 0, world)&&!isWall(posXofPlayer+19, 0, world))
								{
									generateObstacleWall(posXofPlayer+20, 0, world);
								}
								break;
								case 2:
								case 4:
								if(!isWall(posXofPlayer+20, 5, world)&&!isWall(posXofPlayer+19, 5, world))
									{
										generateObstacleWall(posXofPlayer+20, 5, world);
									}
								break;
								
							}
						}
			} // end init
				
		}
		
		private boolean isWall(double startx, double startz, World world)
		{
			for(int z = (int) startz; z < startz+6; z++)
			{
				for(int y = 21; y < 24; y++)
				{
					 if(world.getBlockState(new BlockPos((int) startx, y, z)).equals(Blocks.COBBLESTONE))
					 {
						 return true;
					 }
				}
			}
			return false;
			
		}
	// TODO Auto-generated method stub
		private void resetToAirCont(double startx, double startz, World world) 
		{
		
				for(int z = (int) startz+1; z < startz+10; z++)
				{
					for(int y = 21; y < 24; y++)
					{
						world.setBlockState(new BlockPos((int) startx, y, z),Blocks.AIR.getDefaultState());
						//world.setBlock((int) startx, y, z, Blocks.AIR);
					}
				}
		}
		private void resetToAirStart(double startx, double startz, World world) 
		{
			for(int x = (int) startx; x < 140+startx; x++)
			{
				for(int z = (int) startz+1; z < startz+10; z++)
				{
					for(int y = 21; y < 24; y++)
					{
						world.setBlockState(new BlockPos((int) x, y, z),Blocks.AIR.getDefaultState());
						//world.setBlock(x, y, z, Blocks.AIR);
					}
				}
			}
		}
		private void generateLavaWall(double startx, double startz, World world)
		{
			for(int z = (int) startz+1; z < startz+10; z++)
			{
				for(int y = 21; y < 24; y++)
				{
					world.setBlockState(new BlockPos((int) startx, y, z),Blocks.FIRE.getDefaultState());
					//world.setBlock((int) startx, y, z, Blocks.fire);
				}
			}
		}
		@Override
	    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	    {
	        posXofPlayer = event.player.getPosition().getX();
	        posZofPlayer = event.player.getPosition().getZ();
	        posYofPlayer = event.player.getPosition().getY();
			if(event.player.isBurning())
			{
				super.complete();
				HudManager.unregisterRectangle(timerRectangle);
				HudManager.unregisterString(timerString);
				currentTick = 0;
				QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.x, (int) MinerNPC.LOCATION.y, (int) MinerNPC.LOCATION.z);
				seconds = 60;
				initialize = false;
			}
	//		playerLoc = event.player.getPlayerCoordinates();
	//        playerLocation[1] = playerLoc;
			//System.out.println("ticking");        
	        if(event.player.capabilities.allowEdit)
	            event.player.setGameType(WorldSettings.getGameTypeById(2));
	        //playerEvent = event;
	       
	       
	    }
		
		private void generateEmeraldWall(double startx, double startz, World world) 
		{
			for(int z = 0; z < 11; z++)
			{
				if(world.isAirBlock(new BlockPos((int) (startx), 21, (int) (startz+z))))
				{
					world.setBlockState(new BlockPos((int) startx, 21, (int)startz+ z),Blocks.EMERALD_ORE.getDefaultState());

					//world.setBlock((int) (startx), 21, (int) (startz+z), Blocks.emerald_ore);
				}
			}
		}
		private void generateObstacleWall(double startx, double startz, World world)
		{
			
			for(int z = (int) startz; z < startz+6; z++)
			{
				for(int y = 21; y < 24; y++)
				{
					world.setBlockState(new BlockPos((int) startx, y, (int)startz+ z),Blocks.COBBLESTONE.getDefaultState());

					//world.setBlock((int) startx, y, z, Blocks.COBBLESTONE);
				}
			}
		}
		private void createPotHole(double startx, double startz, World world)
		{
			for(int x = (int) startx; x < startx+3; x++)
			{
				for(int z = (int) startz; z < startz+3; z++)
				{
					world.setBlockState(new BlockPos( x, 20,  z),Blocks.LAVA.getDefaultState());

					//world.setBlock(x, 20, z, Blocks.lava);
				}
			}
		}
	//Generation of normal floor and ceiling north
		private void generateFloorAndCeiling(double startx, double startz, World world)
		{
			for(int x = 0; x < 30; x++)
			{
				for(int z = 0; z < 11; z++)
				{
					Random rand = new Random();
					int randomInt = rand.nextInt(10);
					BlockPos pos = new BlockPos((int) (startx+x), 20, (int) (startz+z));;
					if(world.isAirBlock(pos))
					//|| world.getBlock((int) (startx+x), 20, (int) (startz+z)).equals(Blocks.lava))
					{
						switch (randomInt) {
							case 0:
								world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z),Blocks.iron_ore);
								break;
							case 1:
								world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.gold_ore);
								break;
							case 2:
								world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.diamond_ore);
								break;
							case 3:
								world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.redstone_ore);
								break;
							case 4:
								world.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20,(int) (startz+z), Blocks.glowstone);
								break;
							default:
								world.setBlockState(pos, Blocks.STONE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.STONE);
								break;
						}
					}
					BlockPos nextPos = new BlockPos((int) (startx+x), 25, (int) (startz+z));
					if(world.isAirBlock(nextPos))
					{
						randomInt = rand.nextInt(10);
						switch(randomInt)
						{
							case 0:
								world.setBlockState(nextPos, Blocks.IRON_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z),Blocks.iron_ore);
								break;
							case 1:
								world.setBlockState(nextPos, Blocks.GOLD_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.gold_ore);
								break;
							case 2:
								world.setBlockState(nextPos, Blocks.DIAMOND_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.diamond_ore);
								break;
							case 3:
								world.setBlockState(nextPos, Blocks.REDSTONE_ORE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.redstone_ore);
								break;
							case 4:
								world.setBlockState(nextPos, Blocks.GLOWSTONE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20,(int) (startz+z), Blocks.glowstone);
								break;
							default:
								world.setBlockState(nextPos, Blocks.STONE.getDefaultState());
								//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.STONE);
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
			
			for(int x = 0; x < 30 ; x++)
			{
				for(int y = 20; y < 25; y++)
				{
					BlockPos pos = new BlockPos((int) (startx+x), y, 0);
					if(world.isAirBlock(pos))
					//		|| world.getBlock((int) (startx+x), y, 0).equals(Blocks.lava))
					{
						Random rand = new Random();
						int randomInt = rand.nextInt(10);
						genBlock(randomInt,pos,world);
						BlockPos nextPos = new BlockPos((int)startx+x,y,10);
						genBlock(randomInt,nextPos,world);					}
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
			
			for(int x = 0; x < 10 ; x++)
			{
				for(int y = 20; y < 25; y++)
				{
					BlockPos pos = new BlockPos((int) (startx+x), 20, (int) (startz+x));
					if(world.isAirBlock(pos))
					{
						Random rand = new Random();
						int randomInt = rand.nextInt(10);
						genBlock(randomInt,pos, world);
						BlockPos nextPos = new BlockPos(10, y, (int) (startz+x));
						genBlock(randomInt,nextPos,world);

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
					BlockPos pos = new BlockPos((int) (startx+x), 20, (int) (startz+z));
					if(world.isAirBlock(pos))
					{
						Random rand = new Random();
						int randomInt = rand.nextInt(10);
						genBlock(randomInt, pos,world);
						randomInt = rand.nextInt(10);
						BlockPos nextPos = new BlockPos((int) (startx+x), 25, (int) (startz+z));
						genBlock(randomInt,nextPos,world);
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
					world.setBlockState(new BlockPos((int) (startx+x), y, 10),Blocks.STONE.getDefaultState());
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
          world.setBlockState(new BlockPos((int) startx, y, (int) (startz+x)), Blocks.OBSIDIAN.getDefaultState());
        }
      }
    }

    @Override
    public void setDifficulty(Difficulty difficultyIn) {
      // TODO Auto-generated method stub

    }
    public void genBlock(int randomInt, BlockPos pos, World world){
		switch(randomInt)
		{
			case 0:
				world.setBlockState(pos, Blocks.IRON_ORE.getDefaultState());
				//world.setBlock((int) (startx)+x, 20, (int) (startz+z),Blocks.iron_ore);
				break;
			case 1:
				world.setBlockState(pos, Blocks.GOLD_ORE.getDefaultState());
				//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.gold_ore);
				break;
			case 2:
				world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState());
				//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.diamond_ore);
				break;
			case 3:
				world.setBlockState(pos, Blocks.REDSTONE_ORE.getDefaultState());
				//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.redstone_ore);
				break;
			case 4:
				world.setBlockState(pos, Blocks.GLOWSTONE.getDefaultState());
				//world.setBlock((int) (startx)+x, 20,(int) (startz+z), Blocks.glowstone);
				break;
			default:
				world.setBlockState(pos, Blocks.STONE.getDefaultState());
				//world.setBlock((int) (startx)+x, 20, (int) (startz+z), Blocks.STONE);
				break;
		}

	}
}//end class
				
		
	
