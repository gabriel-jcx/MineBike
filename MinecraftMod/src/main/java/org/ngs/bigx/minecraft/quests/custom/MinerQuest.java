package org.ngs.bigx.minecraft.quests.custom;

import java.util.Random;

import org.ngs.bigx.minecraft.npcs.custom.MinerNPC;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderMineRun;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;

public class MinerQuest extends CustomQuestAbstract
{
	
	
	
	public static final int MINERUNDIMENSIONID = 220;
	private boolean pleaseStop;
	
	public MinerQuest()
	{
		super();
		progress = 0;
		name = "MinerQuest";
		completed = false;
		pleaseStop = false;
		
		
		register();
	}
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.xCoord, (int) MinerNPC.LOCATION.yCoord, (int) MinerNPC.LOCATION.zCoord);
		super.complete();
	}
	
	@Override
	public void start() 	
	{
//		if (isStarted())
//			return;
		progress = 0;
		
		//teleport them to the soccer arena
//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
		QuestTeleporter.teleport(player, MINERUNDIMENSIONID, 1, 70, 0);
		
		super.start();
	}
	@Override
	public void onQuit(ClientDisconnectionFromServerEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) MinerNPC.LOCATION.xCoord, (int) MinerNPC.LOCATION.yCoord, (int) MinerNPC.LOCATION.zCoord);	
	}
	
	 boolean worldLoaded = false;
	@Override
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		if (event.world.provider.dimensionId == MINERUNDIMENSIONID)
			worldLoaded = true;
	}
	
	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{
		if (event.world.provider.worldObj.isRemote)
			return;
		
		if(worldLoaded && event.world.provider.dimensionId == WorldProviderMineRun.MINERUNDIMENSIONID && !pleaseStop)
		{
			
			
			//setting blocks in the world
			World world = event.world.provider.worldObj;
			Random rand = new Random();
			for(int c = 0; c < 5; c++)
			{
				
			
			for(int x = 0; x < 5; x++)
			{
				for(int y = 20; y < 25; y++)
				{
					int randomInt = rand.nextInt(10);
					switch(randomInt)
					{
					case 0:
						world.setBlock(x+(c*10), y, 0, Blocks.iron_ore);
					break;
					case 1:
						world.setBlock(x+(c*10), y, 0, Blocks.gold_ore);
					break;
					case 2:
						world.setBlock(x+(c*10), y, 0, Blocks.diamond_ore);
					break;
					case 3:
						world.setBlock(x+(c*10), y, 0, Blocks.lit_redstone_ore);
					break;
					case 4:
						world.setBlock(x+(c*10), y, 0, Blocks.glowstone);
					break;
					default:
						world.setBlock(x+(c*10), y, 0, Blocks.stone);
					break;
					}
					switch(randomInt)
					{case 0:
						world.setBlock(x+(c*10), y, 10, Blocks.iron_ore);
					break;
					case 1:
						world.setBlock(x+(c*10), y, 10, Blocks.gold_ore);
					break;
					case 2:
						world.setBlock(x+(c*10), y, 10, Blocks.diamond_ore);
					break;
					case 3:
						world.setBlock(x+(c*10), y, 10, Blocks.lit_redstone_ore);
					break;
					case 4:
						world.setBlock(x+(c*10), y, 10, Blocks.glowstone);
					break;
					default:
						world.setBlock(x+(c*10), y, 10, Blocks.stone);
					break;
					}
				}
			}
			for(int spec = 0; spec < 2; spec++)
			{
			for(int x = 0; x < 5; x++)
			{
				for(int z = 0; z < 11; z++)
				{
					int randomInt = rand.nextInt(10);
					switch(randomInt)
					{
					case 0:
						world.setBlock(x+(c*5), 20, z, Blocks.iron_ore);
					break;
					case 1:
						world.setBlock(x+(c*5), 20, z, Blocks.gold_ore);
					break;
					case 2:
						world.setBlock(x+(c*5), 20, z, Blocks.diamond_ore);
					break;
					case 3:
						world.setBlock(x+(c*5), 20, z, Blocks.lit_redstone_ore);
					break;
					case 4:
						world.setBlock(x+(c*5), 20, z, Blocks.glowstone);
					break;
					default:
						world.setBlock(x+(c*5), 20, z, Blocks.stone);
					break;
					}
				
					randomInt = rand.nextInt(10);
					switch(randomInt)
					{
					case 0:	
						world.setBlock(x+(c*5), 25, z, Blocks.iron_ore);
					break;
					case 1:
						world.setBlock(x+(c*5), 25, z, Blocks.gold_ore);
					break;
					case 2:
						world.setBlock(x+(c*5), 25, z, Blocks.diamond_ore);
					break;
					case 3:
						world.setBlock(x+(c*5), 25, z, Blocks.lit_redstone_ore);
					break;
					case 4:
						world.setBlock(x+(c*5), 25, z, Blocks.glowstone);
					break;
					default:
						world.setBlock(x+(c*5), 25, z, Blocks.stone);
					break;
					}
				}
			}
			}
			
			pleaseStop = true;
		}
			
	}
	}
	@Override
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
    {
        //System.out.println("ticking");        
        if(!event.player.capabilities.isCreativeMode)
            event.player.setGameType(WorldSettings.getGameTypeById(1));
       
    }

	@SuppressWarnings("unused")
	private void generateFloorAndCeiling(int x, int y, int z, TickEvent.WorldTickEvent event)
	{
		for(x = 0; x < 5; x++)
		{
			for(z = 0; z < 11; z++)
			{
				World world = event.world.provider.worldObj;
				Random rand = new Random();
				int randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:
					world.setBlock(x, 20, z, Blocks.iron_ore);
				break;
				case 1:
					world.setBlock(x, 20, z, Blocks.gold_ore);
				break;
				case 2:
					world.setBlock(x, 20, z, Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock(x, 20, z, Blocks.lit_redstone_ore);
				break;
				case 4:
					world.setBlock(x, 20, z, Blocks.glowstone);
				break;
				default:
					world.setBlock(x, 20, z, Blocks.stone);
				break;
				}
				randomInt = rand.nextInt(10);
				switch(randomInt)
				{
				case 0:	
					world.setBlock(x, 25, z, Blocks.iron_ore);
				break;
				case 1:
					world.setBlock(x, 25, z, Blocks.gold_ore);
				break;
				case 2:
					world.setBlock(x, 25, z, Blocks.diamond_ore);
				break;
				case 3:
					world.setBlock(x, 25, z, Blocks.lit_redstone_ore);
				break;
				case 4:
					world.setBlock(x, 25, z, Blocks.glowstone);
				break;
				default:
					world.setBlock(x, 25, z, Blocks.stone);
				break;
				}
			}
		}//end outer for loop
	}//end function
	@SuppressWarnings("unused")
	private void duplicateFloorAndCeiling(int amnt, int x, int y, int z, TickEvent.WorldTickEvent event)
		{
		World world = event.world.provider.worldObj;
		for(int c = 0; c < amnt; c++)
			{
				generateFloorAndCeiling(x+(c*5), 20, z, event);
			}
		}	
}//end class
			
	

