package org.ngs.bigx.minecraft.quests.custom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.npcs.custom.Flynn;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.custom.helpers.Utils;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderTRON;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.DataAI;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.entity.EntityCustomNpc;

public class TRONQuest extends CustomQuestAbstract
{

	private boolean worldLoaded = false; // flag to check if the TRONDimension has been loaded
	private boolean init = false; // flag to run game setup only once
	private boolean[][] glassPanes = new boolean[201][201]; // 2x2 matrix to keep track of placed glass panes by both
															// NPC and player

	private int timer = 0; // timer to check how long the player has been standing still
	private int[] warning = { 100, 400 }; // location from where the warningString and warningNumber are based off of
	private HudString warningString; // Displays when player stops moving
	private HudString warningNumber; // Counts down when player stops moving

	public static final String NPC_NAME = "Rinzler"; // Name of npc
	private ForgeDirection npcRunDirection; // which direction npc is currently going
	static EntityCustomNpc npc;
	static NpcCommand command;

	public static int[] npcPath = new int[3]; // staging matrix to add to npcPathList
	public static List<int[]> npcPathList = new ArrayList<int[]>(); // list of coordinates npc will seek out to

	/*
	 * "waterfall" method of laying down glass panes relies on adding new
	 * coordinates at top of array then getting old coordinates at end of array to
	 * lay down glass panes
	 */
	int numStagesPlayer = 8; // steps of delay when laying down glass panes for player
	int numStagesNpc = 8; // steps of delay when laying down glass panes for npc
	ArrayList<int[]> playerLocation = initialize(numStagesPlayer); // initialize the array used to lay down glass panes
																	// for player
	ArrayList<int[]> npcLocation = initialize(numStagesNpc); // initialize the array used to lay down glass panes for
																// npc

	boolean setPanePlayer = false; // flag to tell onWorldTick to place glass pane for player
	boolean setPaneNpc = false; // flag to tell onWorldTick to place glass pane for npc

	/*
	 * turning the npc relies on keeping track of how long the npc has NOT been
	 * moving; if npc stops moving for a little bit, then turn; if npc has not been
	 * moving for a long time, then the npc is stuck
	 */
	private int[] npcStart = { 15, 15 }; // where npc will be spawned
	private int npcTimerTurn = 0; // timer to check for when to turn npc
	private int npcTimerStuck = 0; // timer to check if npc is stuck

	private int[] npcMotionCheckerTurn = { 15, 15 }; // coordinates to compare with current coordinates
	private int[] NPCMotionChecker2 = { 15, 15 }; // coordinates to compare with currect coordinates

	private boolean gameEnded; // flag to check if you are supposed to be giving gold
	private boolean playerWasTeleported; // flag to make sure you only give gold once
	private boolean playerWon;

	private int npcSpeed;

	// 5 seconds ~= 100 ticks

	public TRONQuest() {
		super();
		initializeGlassPanes();
		progress = 0;
		name = "TRONQuest";
		setPanePlayer = false;
		setPaneNpc = false;
		completed = false;

		warningString = new HudString(warning[0], warning[1] - 300, "Keep moving, elimination in:");
		warningString.scale = 3.0f;
		warningNumber = new HudString(warning[0] + 400, warning[1] - 300, "");
		warningNumber.scale = 5.0f;

		npcPath = new int[3];
		npcPathList = new ArrayList<int[]>();
		npcRunDirection = ForgeDirection.EAST;
		npcSpeed = 9;
		// 0x tells it's a hex number, ff at the end is for transparency

		gameEnded = false;
		playerWasTeleported = false;
		playerWon = false;

		register();
	}

	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		returnToMainMenu(); // ends quest when player picks up gold
	}

	public void initializeGlassPanes() // resets the the records of all glass panes from the grid
	{
		for (int i = 0; i < 201; i++)
		{
			for (int j = 0; j < 201; j++)
			{
				glassPanes[i][j] = false;
			}
		}
	}

	/* POSSIBLE BUGS IN NEXT METHOD */
	public void returnToMainMenu() // returns player back to bike dimension
	{
		QuestTeleporter.teleport(player, 0, (int) Flynn.LOCATION.xCoord + 1, (int) Flynn.LOCATION.yCoord,
				(int) Flynn.LOCATION.zCoord + 1);
		super.complete();
		HudManager.unregisterString(warningString);
		HudManager.unregisterString(warningNumber);
		setPanePlayer = false;
		setPaneNpc = false;
		init = false;
		timer = 0;
		npcPath = new int[3];
		npcPathList = new ArrayList<int[]>();
		if (npc != null)
			npc.isDead = true;
		npc = null;
	}

	@Override
	public void start()
	{
		progress = 0;
		QuestTeleporter.teleport(player, WorldProviderTRON.TRONDIMENSIONID, 0, 50, 0); // teleport them to the TRON
																						// arena
		super.start();
	}

	/* minecraft often oscillates coordinates (they are doubles) */
	public boolean isNewPlayer(int[] coordinate) // checks if the added coordinates are new for the player
	{
		for (int x = 0; x < numStagesPlayer; x++)
		{
			if (coordinate[0] == playerLocation.get(x)[0] && coordinate[1] == playerLocation.get(x)[1])
				return false;
		}
		return true;
	}

	public boolean isNewNpc(int[] coordinate) // checks if the added coordinates are new for the npc
	{
		for (int x = 0; x < numStagesNpc; x++)
		{
			if (coordinate[0] == npcLocation.get(x)[0] && coordinate[1] == npcLocation.get(x)[1])
				return false;
		}
		return true;
	}

	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.player.worldObj.provider.dimensionId != WorldProviderTRON.TRONDIMENSIONID) // makes sure the player
																								// has finished spawning
		{
			return;
		}

		// checking if player has moved
		double velocity = Math
				.pow((Math.pow((double) event.player.motionX, 2) + (Math.pow((double) event.player.motionZ, 2))), 0.5);
		if (velocity == 0) // values are 0 when standing still
		{
			timer++;
		} else // resets when the player has begun moving again
		{
			timer = 0;
		}

		if (timer >= 100) // countdown sequence; not supposed to be super accurate
		{
			HudManager.registerString(warningString);
			HudManager.registerString(warningNumber);
			if (timer >= 200)
			{
				if (timer >= 300)
				{
					if (timer >= 400)
					{
						warningNumber.text = "0";
					} else
					{
						warningNumber.text = "1";
					}
				} else
				{
					warningNumber.text = "2";
				}
			} else
			{
				warningNumber.text = "3";
			}
		} else
		{
			HudManager.unregisterString(warningString);
			HudManager.unregisterString(warningNumber);
		}

		if (timer >= 500) // if player has not been moving for n seconds, then they get sent back
		{
			timer = 0;
			if (!gameEnded) // if gold has not been given yet
			{
				gameEnded = true; // player lost
			}
		}

		if (!event.player.capabilities.isCreativeMode)
		{
			event.player.setGameType(WorldSettings.getGameTypeById(1)); // switches player back to creative in case the
																		// world tries set back to survival
		}

		int currentCoX = event.player.getPlayerCoordinates().posX; // current player X location, helps cut down lag
																	// significantly
		int currentCoZ = event.player.getPlayerCoordinates().posZ; // current player Z location
		int[] tempCoordinate = { currentCoX, currentCoZ };

		if (!setPanePlayer && isNewPlayer(tempCoordinate))
		{
			setPanePlayer = true; // player may now set down a glass pane

			int[] newLoc = { currentCoX, currentCoZ };
			playerLocation.add(0, newLoc); // adds the player coordinates to the beginning of the list
			playerLocation.remove(numStagesPlayer); // removes at end of list
		}

		if (event.player.getPlayerCoordinates().posY < 14) // if player falls from arena
		{
			// returnToMainMenu();
			gameEnded = true; // player lost
		}

		if (currentCoX <= 100 && currentCoX >= -100 && currentCoZ <= 100 && currentCoZ >= -100) // to prevent null
																								// errors

		{
			if (glassPanes[currentCoX + 100][currentCoZ + 100] && currentCoX != 0 && currentCoZ != 0)
			// if player returned to a location that had a glass pane on
			{
				gameEnded = true; // player lost
				// returnToMainMenu();
			}
		}

	}

	@Override
	public void setDifficulty(Difficulty difficultyIn)
	{
		// npcSpeed = difficultyIn; //add in dificulty variable
	}

	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{
		if (!worldLoaded || event.world.provider.dimensionId != WorldProviderTRON.TRONDIMENSIONID
				|| event.world.isRemote)
		{
			return; // makes sure world has been loaded before starting game
		}

		if (!init) // runs once on startup
		{
			for (int i = -200; i <= 200; i++)
			{
				for (int j = -200; j <= 200; j++)
				{
					if (i >= -100 && i <= 100 && j >= -100 && j <= 100) // clears the arena of panes
					{
						glassPanes[i + 100][j + 100] = false;
					}
					event.world.setBlock(i, 45, j, Blocks.air);
					event.world.setBlock(i, 46, j, Blocks.air);
				}
			}
			// reset variables
			npcStart[0] = 15;
			npcStart[1] = 15;
			npcTimerTurn = 0;
			npcTimerStuck = 0;
			npcMotionCheckerTurn[0] = 15;
			npcMotionCheckerTurn[1] = 15;
			NPCMotionChecker2[0] = 15;
			NPCMotionChecker2[1] = 15;
			npcPath = new int[3];
			npcPathList = new ArrayList<int[]>();
			npcRunDirection = ForgeDirection.EAST;

			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderTRON.TRONDIMENSIONID);
			synchronized (ws.loadedEntityList)
			{
				Iterator iter = ws.loadedEntityList.iterator();
				while (iter.hasNext())
				{
					Entity entity = (Entity) iter.next();
					if (!(entity instanceof EntityPlayer))
					{
						entity.isDead = true;
					}
				}
			}

			// spawns the npc
			npc = NpcCommand.spawnNpc(npcStart[0], 45, npcStart[1], (WorldServer) event.world.provider.worldObj,
					NPC_NAME + " ", Flynn.TEXTURE); // using same texture as flynn
			npc.velocityChanged = true;

			command = new NpcCommand(BigxClientContext.getInstance(), npc);
			command.setSpeed(npcSpeed); // sets speed of npc accordingly to difficulty level

			npcPathList.add(npcPath); // must add npc path twice for unknown reasons
			npcPathList.add(npcPath);

			npc.ai = new DataAI(npc)
			{
				public int[] path = npcPath;

				@Override
				public int[] getCurrentMovingPath()
				{
					return path;
				}

				@Override
				public List getMovingPath()
				{
					return npcPathList;
				}
			};
			npc.ai.movingPause = false;
			npc.ai.movingPos = 0;
			npc.ai.startPos = npcPath;
			npc.ai.walkingRange = 100;
			npc.ai.movingPattern = 0;
			npc.ai.movingType = EnumMovingType.MovingPath;
			init = true;
		}

		if (npc != null)
		{
			int NPCPaneX = -5; // sets values to prevent possible null erros on startup
			int NPCPaneZ = -5;

			int[] tempCoordinate2 = { (int) npc.posX, (int) npc.posZ };
			//
			if (!setPaneNpc && isNewNpc(tempCoordinate2))
			{
				setPaneNpc = true;
				// shift the ArrayList
				int[] newLoc = { (int) npc.posX, (int) npc.posZ };
				npcLocation.add(0, newLoc); // adds the npc coordinates to the beginning of the list
				npcLocation.remove(numStagesPlayer); // removes at end of list
			}
			if (setPaneNpc) // if true, then you can now set the glass panes
			{
				int paneX = npcLocation.get(numStagesNpc - 1)[0];
				int paneZ = npcLocation.get(numStagesNpc - 1)[1];

				event.world.setBlock(paneX, 45, paneZ, Blocks.stained_glass_pane, 14, 2);
				event.world.setBlock(paneX, 46, paneZ, Blocks.stained_glass_pane, 14, 2);
				if (paneX <= 100 && paneX >= -100 && paneZ <= 100 && paneZ >= -100)
				// prevent outOfBounds
				{
					glassPanes[paneX + 100][paneZ + 100] = true;
				}
				setPaneNpc = false;
			}

			if (NPCPaneX <= 100 && NPCPaneX >= -100 && NPCPaneZ <= 100 && NPCPaneZ >= -100)
			// prevent outOfBounds
			{
				glassPanes[NPCPaneX + 100][NPCPaneZ + 100] = true;
			}
			updateNpcPath();
		}

		if (setPanePlayer) // if true, then you can now set the glass panes
		{
			int paneX = playerLocation.get(numStagesPlayer - 1)[0];
			int paneZ = playerLocation.get(numStagesPlayer - 1)[1];

			event.world.setBlock(paneX, 45, paneZ, Blocks.stained_glass_pane, 9, 2);
			event.world.setBlock(paneX, 46, paneZ, Blocks.stained_glass_pane, 9, 2);
			if (paneX <= 100 && paneX >= -100 && paneZ <= 100 && paneZ >= -100)
			// prevent outOfBounds
			{
				glassPanes[paneX + 100][paneZ + 100] = true;
			}
			setPanePlayer = false;
		}

		if (gameEnded && !playerWasTeleported) // give gold to player
		{
			playerWasTeleported = true;
			// tp to platform
			QuestTeleporter.teleport(player, WorldProviderTRON.TRONDIMENSIONID, 134, 98, 99);
			// spawn gold stack on platform
			EntityItem entityitem1 = new EntityItem(event.world.provider.worldObj, 137, 98, 102,
					new ItemStack(Item.getItemById(266), 1));
			EntityItem entityitem2 = new EntityItem(event.world.provider.worldObj, 137, 98, 102,
					new ItemStack(Item.getItemById(266), 3));
			if (playerWon) //if player won...
			{
				event.world.provider.worldObj.spawnEntityInWorld(entityitem2); // give 6 units of gold
			}
			else //if player lost...
			{
				event.world.provider.worldObj.spawnEntityInWorld(entityitem1); // give 2 units of gold
			}
			// on item pickup, return to main menu
		}

	}

	private void updateNpcPath()
	{
		// this is where you update where the NPC has to go

		if (Math.abs(npcStart[0] - npc.posX) + Math.abs(npcStart[1] - npc.posZ) > 35) // if NPC went further than 10
		{
			npcRunDirection = turn(npcRunDirection); // new direction
			npcStart[0] = (int) npc.posX;
			npcStart[1] = (int) npc.posZ;
		}

		int tempX = (int) npc.posX;
		int tempY = 45; // this is the y level of the floor
		int tempZ = (int) npc.posZ;

		switch (npcRunDirection) // checking the current direction of NPC, add in destination coordinates
		{
		case NORTH:
			npcPath[0] = tempX;
			npcPath[1] = tempY;
			npcPath[2] = tempZ + 3; // going north; destination 2 blocks north from present location
			break;
		case WEST:
			npcPath[0] = tempX - 3;// going west
			npcPath[1] = tempY;
			npcPath[2] = tempZ;
			break;
		case EAST:
			npcPath[0] = tempX + 3;// going east
			npcPath[1] = tempY;
			npcPath[2] = tempZ;
			break;
		case SOUTH:
			npcPath[0] = tempX;
			npcPath[1] = tempY;
			npcPath[2] = tempZ - 3;// going south
			break;
		}

		npcPathList.set(0, npcPath);
		npcPathList.set(1, npcPath);
		npc.ai.setMovingPath(npcPathList);
		npc.ai.getMovingPath().add(npcPath);
		command.enableMoving(true);
		npc.velocityChanged = true;

		npcTimerTurn++;
		npcTimerStuck++;

		if (npcTimerTurn > 50) // npc has remained still for 50 ticks
		{

			if (Math.abs(npcMotionCheckerTurn[0] - (int) npc.posX) <= 2
					&& Math.abs(npcMotionCheckerTurn[1] - (int) npc.posZ) <= 2)
			{
				npcRunDirection = turn(npcRunDirection); // new direction
				npcStart[0] = (int) npc.posX;
				npcStart[1] = (int) npc.posZ;
			}
			npcMotionCheckerTurn[0] = (int) npc.posX;
			npcMotionCheckerTurn[1] = (int) npc.posZ;
			npcTimerTurn = 0;
		}

		if (npcTimerStuck > 500) // npc has remained still for 500 ticks
		{

			if (Math.abs(NPCMotionChecker2[0] - (int) npc.posX) <= 2
					&& Math.abs(NPCMotionChecker2[1] - (int) npc.posZ) <= 2)
			{
				gameEnded = true; // game ended, but player won
				playerWon = true; // player won
			} else
			{
				NPCMotionChecker2[0] = (int) npc.posX;
				NPCMotionChecker2[1] = (int) npc.posZ;
				npcTimerStuck = 0;
			}
		}

	}

	private ForgeDirection turn(ForgeDirection direction) // randomly changes the direction of npc
	{
		if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH) // return 1 or 2
		{
			ForgeDirection[] temp = { ForgeDirection.EAST, ForgeDirection.WEST };
			int index = (int) (Math.random() * 2);
			return temp[index];

		} else
		{
			ForgeDirection[] temp = { ForgeDirection.NORTH, ForgeDirection.SOUTH };
			int index = (int) (Math.random() * 2);
			return temp[index];
		}
	}

	@Override
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		if (event.world.provider.dimensionId == WorldProviderTRON.TRONDIMENSIONID)
		{
			worldLoaded = true;
		}
	}

	public ArrayList<int[]> initialize(int howMany) // initialize the numStages array for npc and player
	{
		ArrayList<int[]> outerArr = new ArrayList<int[]>();
		for (int i = 0; i < howMany; i++)
		{
			int[] myInt = { 2, 2 };
			outerArr.add(myInt);
		}
		return outerArr;
	}
}
