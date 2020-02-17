package org.ngs.bigx.minecraft.bike;

import java.util.List;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.util.math.BlockPos;
import org.ngs.bigx.minecraft.BlockPositionMapping;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
//import cpw.mods.fml.common.eventhandler.SubscribeEvent;
//import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
public class PedalingToBuildEventHandler {
	public static PedalingToBuild pedalingToBuild = null;

	private static int onPlayerTickEventCount = 0;
	
	public static String buildingId = "";
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		int determineToPlaceBlockReturnValue = -1;
		
		if(event.player != null)
		{
			if(pedalingToBuild == null)
			{
				return;
			}
			
			/**
			 * Only one side to increment tick count
			 */
			if(!event.player.world.isRemote)
			{
				onPlayerTickEventCount++;
				
				if(onPlayerTickEventCount >= 10)
				{
					onPlayerTickEventCount = 0;
					
					if(pedalingToBuild.isCleaningDoneFlag())
					{
						// Clean the area
						for(int x=0; x<5; x++)
						{
							for(int y=0; y<5; y++)
							{
								for(int z=0; z<5; z++)
								{
									BlockPos pos = new BlockPos(x + pedalingToBuild.getPosx(),y + pedalingToBuild.getPosy(),z + pedalingToBuild.getPosz());
									//event.player.world.getBlockState().getBlock().equals()
									if(event.player.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
									//if(!event.player.world.getBlock(x + pedalingToBuild.getPosx(), y + pedalingToBuild.getPosy(), z + pedalingToBuild.getPosz()).equals(Blocks.AIR))
									{

										event.player.world.setBlockToAir(pos);
										//event.player.world.setBlock(x + pedalingToBuild.getPosx(), y + pedalingToBuild.getPosy(), z + pedalingToBuild.getPosz(), Blocks.AIR);
									}
								}
							}
						}
						
						// Unset the flag
						pedalingToBuild.unsetCleaningDoneFlag();
						
						// return
						return;
					}
					determineToPlaceBlockReturnValue = pedalingToBuild.determineToPlaceBlock();
					List<BlockPositionMapping> tempBlockList = pedalingToBuild.getTheListOfBlockToBePlaced();
					synchronized (tempBlockList)
					{
						for(BlockPositionMapping BlockPositionMapping : tempBlockList)
						{
							Block block = BlockPositionMapping.block;
							TerrainBiomeAreaIndex areaIndex = BlockPositionMapping.terrainBiomeAreaIndex;
							IBlockState state = BlockPositionMapping.block.getDefaultState();
							BlockPos pos = new BlockPos(areaIndex.x + pedalingToBuild.getPosx(), areaIndex.y + pedalingToBuild.getPosy(), areaIndex.z + pedalingToBuild.getPosz());
							//event.player.world.setBlock(pos,block);
							event.player.world.setBlockState(pos, state);
						}
						pedalingToBuild.emptyBlockToBePlaced();
					}
					
					if(determineToPlaceBlockReturnValue == 2)
					{
						pedalingToBuild = null;
					}
				}
			}
		}
	}
}
