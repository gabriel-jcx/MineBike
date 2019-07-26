package org.ngs.bigx.minecraft.quests.custom;

import java.time.Clock;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.items.CustomEntityItem;
import org.ngs.bigx.minecraft.items.CustomFishHook;
import org.ngs.bigx.minecraft.npcs.custom.Chum;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract.Difficulty;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFishing;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldSettings;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

public class FishingQuest extends CustomQuestAbstract
{
	public static final int FISHINGDIMENSIONID = WorldProviderFishing.fishingDimID;
	//Checks to see if the player loaded the dimension yet
	private boolean worldLoaded = false;
	
	//stores the time
	private Clock clock;
	
	//Used to see if the minigame has started and to tell it to track time
	private boolean clockStart = false;
	
	//Stores the time when the minigame is started, used to track how much time has passed
	long lastTime = 0;
	
	//How Long the Minigame will be in seconds
	int time = 300;
	
	//Tracks how long in seconds it's been since lastTime
	int timeElapsed = 0;
	
	//Checks to see if player is in fishing dimension
	boolean checkDimension = false;
	
	//The amount of fish required to complete the minigame
	int requiredFish = 20;
	
	HudString timer = new HudString(0, -170, "Time Limit: " + (time/60) +  ":" + (time%60), 2, true, true);
	HudString fishCount = new HudString(0, -150, "Fish Left: " + requiredFish, 2, true, true);

	
	public FishingQuest()
	{
		super();
		progress = 0;
		name = "Fishing Quest";
		completed = false;
		
		register();
	}
	
	@Override
	//Used To get out of the Dimension and complete quest
	public void onAttackEntityEvent(AttackEntityEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) Chum.LOCATION.xCoord, (int) Chum.LOCATION.yCoord, (int) Chum.LOCATION.zCoord);
		CustomFishHook.fishingLocation = 0;
		super.complete();
	}
	
	@Override
	//Starts the Dimension
	public void start() 	
	{
		progress = 0;
		
		//teleport them to the Fishing Hub
		QuestTeleporter.teleport(player, FISHINGDIMENSIONID, 0, 11, 0);
		
		super.start();
	}
  
	//Ends the Minigame and unregisters all timers and counters, Teleports player back to the hub of the dimensiom
	//Param: event is used to get the player
	public void endGame(TickEvent.PlayerTickEvent event)
	{
		if(CustomFishHook.numFish >= requiredFish)
		{
			Minecraft mc = Minecraft.getMinecraft();
		}
		unRegister();
		time = 300;
		timeElapsed = 0;
		CustomFishHook.numFish = 0;
		EntityItem entityitem = new EntityItem(event.player.worldObj, 0, 11, 0, new ItemStack(Items.gold_ingot, 5));
		(event.player.worldObj).spawnEntityInWorld(entityitem);
		QuestTeleporter.teleport(player, FISHINGDIMENSIONID, 0, 11, 0);
	}
	
	//Starts the timer and subsequently the minigame
	public void startTimer()
	{
		HudManager.registerString(timer);
		HudManager.registerString(fishCount);
		CustomFishHook.numFish = 0;
		clock = Clock.systemDefaultZone();
	    lastTime = clock.millis();
	    clockStart = true;
	}
	
	@Override
	/*
	 * Used in order to keep track of the Minigame
	 */
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{
		
		//If CclockStart = true it means the minigame has started and timers and counters should activate
		if(clockStart == true)
		{
			timeElapsed = (int)(clock.millis() - lastTime) / 1000;
			fishCount.text = "Fish Left: " + (requiredFish - CustomFishHook.numFish);
			//Displays the timer in minutes and seconds
			if(((time - timeElapsed) % 60) < 10)
		           timer.text = "Time Limit: " + ((time - timeElapsed)/60) + ":0" + ((time - timeElapsed)% 60);
		       else
		           timer.text = "Time Limit: " + ((time - timeElapsed)/60) + ":" + ((time - timeElapsed) % 60);
			
			//If the Clock has gone past the specified time the game will end and stop the Minigame
			if(clock.millis() >= (time * 1000) + lastTime)
			{
				endGame(event);
				clockStart = false;
			}
			
		}
		
		//Used To check if the player has entered one of the portals and started the Minigame
		//If any portal besides the return portal is walked into it will teleport player to specified
		//location and start the Minigame
		if(worldLoaded == true) 
		{
	
			if(event.player.dimension == FISHINGDIMENSIONID)
				checkDimension = true;
			
			if(!event.player.capabilities.isCreativeMode && checkDimension == true)
				event.player.setGameType(WorldSettings.getGameTypeById(1));
			
			ChunkCoordinates coord = event.player.getPlayerCoordinates();
			
			//Used to Return the Player to the Overworld and end the quest
			if(checkArea(-36, -35, 9, 25, -7, 7, coord, checkDimension))
			{
				System.out.println("Return");
				QuestTeleporter.teleport(player, 0, (int) Chum.LOCATION.xCoord, (int) Chum.LOCATION.yCoord, (int) Chum.LOCATION.zCoord);
				CustomFishHook.fishingLocation = 0;
				super.complete();
			}
			if(checkArea(-23, 9, 9, 25, 29, 30, coord, checkDimension))
			{		
				System.out.println("Lake");
				startTimer();
				CustomFishHook.fishingLocation = 1;
				QuestTeleporter.teleport(event.player, FISHINGDIMENSIONID, -87, 10, 177);
			}
			if(checkArea(7, 21, 9, 25, 29, 30, coord, checkDimension))
			{
				System.out.println("Spooky");
				startTimer();
				CustomFishHook.fishingLocation = 2;
				QuestTeleporter.teleport(event.player, FISHINGDIMENSIONID, 207, 10, 172);
			}
			if(checkArea(39, 40, 9, 25, 5, 19, coord, checkDimension))
			{
				System.out.println("Glacial");
				startTimer();
				CustomFishHook.fishingLocation = 3;
				QuestTeleporter.teleport(event.player, FISHINGDIMENSIONID, 256, 10, 1);
			}
			if(checkArea(39, 40, 9, 25, -23, -9, coord, checkDimension))
			{
				System.out.println("Koi Pond");
				startTimer();
				CustomFishHook.fishingLocation = 4;
				QuestTeleporter.teleport(event.player, FISHINGDIMENSIONID, 304, 10, -186);
			}
			if(checkArea(12, 26, 9, 25, -33, -32, coord, checkDimension))
			{
				System.out.println("Deep Sea");
				startTimer();
				CustomFishHook.fishingLocation = 5;
				QuestTeleporter.teleport(event.player, FISHINGDIMENSIONID, 197, 12, -344);
			}
			if(checkArea(-24, -10, 9, 25, -33, -32, coord, checkDimension))
			{
				System.out.println("Nether");
				startTimer();
				CustomFishHook.fishingLocation = 6;
				QuestTeleporter.teleport(event.player, FISHINGDIMENSIONID, -217, 13, -439);
			}
			
		}
	}
	
	/* Parameters ARE NOT based on just two coordinates
	 * xLow is the smallest x Value out of the two coordinates NOT THE FIRST X COORD
	 * xHigh is the larger x value out of the two coordinates
	 * Same rules apply for yLow, yHight and zLow, zHigh
	 * Ex: the coordinates are (-42, 15, 72) and (10, -20, 32)
	 * The Parameters for these coords are checkArea(-42, 10, -20, 15, 32, 72)
	 */
	public boolean checkArea(int xLow, int xHigh, int yLow, int yHigh, int zLow, int zHigh, ChunkCoordinates coord, boolean dimension)
	{
		if((coord.posX >= xLow && coord.posX <= xHigh) && (coord.posY >= yLow && coord.posY <= yHigh) && (coord.posZ >= zLow && coord.posZ <= zHigh)
				&& dimension)
		{
			return true;
		}
		return false;
	}
	
	//Unregisters all timers and counters for the quest
	public void unRegister()
	{
		CustomFishHook.unRegister();
		HudManager.unregisterString(timer);
		HudManager.unregisterString(fishCount);
	}

	//Checks to see if the player has loaded up the dimension
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		worldLoaded = true;
	}
	
	@Override
	public void setDifficulty(Difficulty difficultyIn) {
		// TODO Auto-generated method stub
		
	}
}
