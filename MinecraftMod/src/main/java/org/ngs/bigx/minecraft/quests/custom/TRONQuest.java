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

    private boolean worldLoaded = false;
    private boolean init = false;
    private boolean[][] glassPanes = new boolean[201][201];
    private int timer = 0;
   
    private HudString warnin;
    private int[] warningString = { 100, 400 };
    private HudString warningNumber;

    public static final String NPC_NAME = "Rinzler";
    private ForgeDirection npcRunDirection;
    static EntityCustomNpc npc;
    static NpcCommand command;

    public static int[] npcPath = new int[3];
    public static List<int[]> npcPathList = new ArrayList<int[]>();

    int numStagesPlayer = 8; // how many steps ArrayList - how many steps of delay in laying down the glass
                       // panes
    int numStagesNpc = 8;
    ArrayList<int[]> playerLocation = initialize(numStagesPlayer); // 4 slots currently in ArrayList

    
    ArrayList<int[]> npcLocation = initialize(numStagesNpc); // 4 slots currently in ArrayList
    boolean setBlock = false; // used to tell OnWorldTick whether they can drop a block at
                              // previousPlayerLocationLast
    boolean setBlock2 = false;
    private int[] NPCStart = { 15, 15 };
    private int npcTimer = 0;
    private int npcTimer2 = 0;
    private int[] NPCMotionChecker = { 15, 15 };
    private int[] NPCMotionChecker2 = { 15, 15 };
    
    private boolean giveGold;

    // 5 seconds = 100 ticks

    public TRONQuest()
    {
        super();
        initializeGlassPanes();
        progress = 0;
        name = "TRONQuest";
        setBlock = false;
        setBlock2 = false;

        completed = false;
   
        warnin = new HudString(warningString[0], warningString[1] - 300, "Keep moving, elimination in:");
        warnin.scale = 3.0f;
        warningNumber = new HudString(warningString[0] + 400, warningString[1] - 300, "");
        warningNumber.scale = 5.0f;
        
        npcPath = new int[3];
        npcPathList = new ArrayList<int[]>();
        npcRunDirection = ForgeDirection.EAST;
        // 0x tells it's a hex number, ff at the end is for transparency
        
        giveGold = false;
        
        register();
    }

    @Override
    public void onItemPickUp(EntityItemPickupEvent event)
    {
        returnToMainMenu(); // brings back player to previous dimension
    }

    public void initializeGlassPanes()
    {
        for (int i = 0; i < 201; i++)
        {
            for (int j = 0; j < 201; j++)
            {
                glassPanes[i][j] = false;
            }
        }
    }

    public void returnToMainMenu() // call to return player to previous dimension
    {
        QuestTeleporter.teleport(player, 0, (int) Flynn.LOCATION.xCoord, (int) Flynn.LOCATION.yCoord,
                (int) Raul.LOCATION.zCoord);
        HudManager.unregisterString(warnin);
        HudManager.unregisterString(warningNumber);
        setBlock = false;
        init = false;
        super.complete();
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
        // teleport them to the TRON arena
        QuestTeleporter.teleport(player, WorldProviderTRON.TRONDIMENSIONID, 0, 50, 0);
        super.start();
    }

    public boolean isNew(int[] coordinate)
    {
        for (int x = 0; x < numStagesPlayer; x++)
        {
            if (coordinate[0] == playerLocation.get(x)[0] && coordinate[1] == playerLocation.get(x)[1])
                return false;
        }
        return true;
    }
    
    public boolean isNew2(int[] coordinate)
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
        if (event.player.worldObj.provider.dimensionId != WorldProviderTRON.TRONDIMENSIONID)
        {
            return;
        }

        // checking if player has moved
        double velocity = Math.pow((Math.pow((double) event.player.motionX, 2) + (Math.pow(
                (double) event.player.motionZ, 2))), 0.5);
        if (velocity == 0) // values get close to 0 when running
        {
            timer++;
        } 
        else
        {
            timer = 0;
        }

        if (timer >= 100) // begin warning with hud, prevents from hud showing up all the time
        {
            HudManager.registerString(warnin);
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
            HudManager.unregisterString(warnin);
            HudManager.unregisterString(warningNumber);
        }

        if (timer >= 500) // if player has not been moving for n seconds, then they get sent back
        {
            returnToMainMenu();
        }
        // update speed bar
        

        if (!event.player.capabilities.isCreativeMode)
        {
            event.player.setGameType(WorldSettings.getGameTypeById(1)); // switches player back to creative in case the
                                                                        // world tries set back to survival
        }

        int currentCoX = event.player.getPlayerCoordinates().posX; // current player X location, helps cut down lag
                                                                   // significantly
        int currentCoZ = event.player.getPlayerCoordinates().posZ; // current player Z location
        int[] tempCoordinate = { currentCoX, currentCoZ };

        if (!setBlock && isNew(tempCoordinate))
        {
            setBlock = true;
            // shift the ArrayList

            int[] newLoc = { currentCoX, currentCoZ };
            playerLocation.add(0, newLoc); // adds the player coordinates to the beginning of the list
            playerLocation.remove(numStagesPlayer); // removes at end of list
        }

        if (event.player.getPlayerCoordinates().posY < 14) // if player falls, then they will return to the main menu
                                                           // before hitting the lava
        {
            returnToMainMenu();
        }

        if (currentCoX <= 100 && currentCoX >= -100 && currentCoZ <= 100 && currentCoZ >= -100)
        {
            // to prevent null errors
            if (glassPanes[currentCoX + 100][currentCoZ + 100] && currentCoX != 0 && currentCoZ != 0)
            // if player returned to a location that had a glass pane on
            {
                returnToMainMenu();
            }
        }
        
        if (giveGold)
        {
        	for(int i = 0; i < event.player.inventory.getSizeInventory(); i++)
        	{
        		if (event.player.inventory.getStackInSlot(i) == null)
        		{
        			event.player.inventory.setInventorySlotContents(i, new ItemStack(Item.getItemById(266), 6));
        			break;
        		}

        		else if (event.player.inventory.getStackInSlot(i).getItem().getUnlocalizedName().equals("Gold Ingot"))
        		{
        			while (event.player.inventory.getStackInSlot(i).stackSize < 64)
        			{
        				event.player.inventory.getStackInSlot(i).stackSize ++;
        			}
        			break;
        		}
        		
        	}
        	returnToMainMenu();
        	giveGold = false;
        }

        // sets previousPlayerLocation between actual location and

        // previousPlayerLocation, this ensures that the
        // previousPlayerLocationLast variable will be far away enough
    }
    
    @Override
    public void setDifficulty(Difficulty difficultyIn)
    {
    	
    }

    @Override
    public void onWorldTickEvent(TickEvent.WorldTickEvent event)
    {    	
        if (!worldLoaded || event.world.provider.dimensionId != WorldProviderTRON.TRONDIMENSIONID || event.world.isRemote)
        {
            return; // prevent errors
        }

        if (!init)
        {
            for (int i = -200; i <= 200; i++)
            {
                for (int j = -200; j <= 200; j++)
                {
                    if (i >= -100 && i <= 100 && j >= -100 && j <= 100)
                    {
                        glassPanes[i + 100][j + 100] = false;
                    }
                    event.world.setBlock(i, 45, j, Blocks.air);
                    event.world.setBlock(i, 46, j, Blocks.air);
                }
            }
            NPCStart[0] = 15;
            NPCStart[1] = 15;
            npcTimer = 0;
            npcTimer2 = 0;
            NPCMotionChecker[0] = 15;
            NPCMotionChecker[1] = 15;
            NPCMotionChecker2[0] = 15;
            NPCMotionChecker2[1] = 15;
            npcPath = new int[3];
            npcPathList = new ArrayList<int[]>();
            npcRunDirection = ForgeDirection.EAST;
            
        


//            NpcCommand.removeNpc(NPC_NAME + " ", WorldProviderTRON.TRONDIMENSIONID);

            WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderTRON.TRONDIMENSIONID);
            synchronized (ws.loadedEntityList)
            {
                Iterator iter = ws.loadedEntityList.iterator();
                while (iter.hasNext())
                {
                    Entity entity = (Entity) iter.next();
                    if (! (entity instanceof EntityPlayer))
                	{
                		entity.isDead = true;
                	}
                }
            }
            
                // spawns the npc
            npc = NpcCommand.spawnNpc(NPCStart[0], 45, NPCStart[1], (WorldServer) event.world.provider.worldObj,
                    NPC_NAME + " ", Flynn.TEXTURE);
            npc.velocityChanged = true;
        
            
            
            command = new NpcCommand(BigxClientContext.getInstance(), npc);
            command.setSpeed(9);

            npcPathList.add(npcPath);
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
            int NPCPaneX = -5;
            int NPCPaneZ = -5;
            
            int[] tempCoordinate2 = { (int) npc.posX, (int) npc.posZ };
            // 
            if (!setBlock && isNew2(tempCoordinate2))
            {
                setBlock2 = true;
                // shift the ArrayList
                int[] newLoc = { (int) npc.posX, (int) npc.posZ };
                npcLocation.add(0, newLoc); // adds the player coordinates to the beginning of the list
                npcLocation.remove(numStagesPlayer); // removes at end of list
            }
            if (setBlock2) // if true, then you can now set the glass panes
            {
                int paneX = npcLocation.get(numStagesNpc - 1)[0];
                int paneZ = npcLocation.get(numStagesNpc - 1)[1];

                event.world.setBlock(paneX, 45, paneZ, Blocks.stained_glass_pane, 14, 2);
                event.world.setBlock(paneX, 46, paneZ, Blocks.stained_glass_pane, 14, 2);
                if (paneX <= 100 && paneX >= -100 && paneZ <= 100 && paneZ >= -100)
                // prevent outOfBounds
                {
                    glassPanes[paneX + 100][paneZ + 100] = true;
                    // set the last stage of waterfall as the glass pane location
                    // sets a location where a glass pane was set so that it can be used later
                    // to determine if a player hit a glass pane
                    // resets the value so that it tells the program that it doesn't have to keep
                    // setting down blocks
                }
                setBlock2 = false;
            }

           
            if (NPCPaneX <= 100 && NPCPaneX >= -100 && NPCPaneZ <= 100 && NPCPaneZ >= -100)
            // prevent outOfBounds
            {
                glassPanes[NPCPaneX + 100][NPCPaneZ + 100] = true;
            }
            updateNpcPath();
        }

        if (setBlock) // if true, then you can now set the glass panes
        {
            int paneX = playerLocation.get(numStagesPlayer - 1)[0];
            int paneZ = playerLocation.get(numStagesPlayer - 1)[1];

            event.world.setBlock(paneX, 45, paneZ, Blocks.stained_glass_pane, 9, 2);
            event.world.setBlock(paneX, 46, paneZ, Blocks.stained_glass_pane, 9, 2);
            if (paneX <= 100 && paneX >= -100 && paneZ <= 100 && paneZ >= -100)
            // prevent outOfBounds
            {
                glassPanes[paneX + 100][paneZ + 100] = true;
                // set the last stage of waterfall as the glass pane location
                // sets a location where a glass pane was set so that it can be used later
                // to determine if a player hit a glass pane
                // resets the value so that it tells the program that it doesn't have to keep
                // setting down blocks
            }
            setBlock = false;
        }
    }

    private void updateNpcPath()
    {


        // this is where you update where the NPC has to go

        if (Math.abs(NPCStart[0] - npc.posX) + Math.abs(NPCStart[1] - npc.posZ) > 35) // if NPC went further than 10
        {
            npcRunDirection = turn(npcRunDirection); // new direction
            NPCStart[0] = (int) npc.posX;
            NPCStart[1] = (int) npc.posZ;
        }

        int tempX = (int) npc.posX;
        int tempY = 45; //this is the y level of the floor
        int tempZ = (int) npc.posZ;        

        switch (npcRunDirection) // checking the current direction of NPC, add in destination coordinates according to direction
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
//      command.addPathPoint(intPoint);
        npc.ai.getMovingPath().add(npcPath);
        command.enableMoving(true);
        npc.velocityChanged = true;
        
        npcTimer++;
        npcTimer2++;        

        if (npcTimer > 50) // 2000 ticks have passed
        {
            
            if (Math.abs(NPCMotionChecker[0] - (int) npc.posX) <= 2 && Math.abs(NPCMotionChecker[1] - (int) npc.posZ) <= 2)
            {
                npcRunDirection = turn(npcRunDirection); // new direction
                NPCStart[0] = (int) npc.posX;
                NPCStart[1] = (int) npc.posZ;
            }
            NPCMotionChecker[0] = (int) npc.posX;
            NPCMotionChecker[1] = (int) npc.posZ;
            npcTimer = 0;
        }  
        
        if (npcTimer2 > 500)
        {
        	
            if (Math.abs(NPCMotionChecker2[0] - (int) npc.posX) <= 2 && Math.abs(NPCMotionChecker2[1] - (int) npc.posZ) <= 2)
            {
            	giveGold = true;
                //returnToMainMenu();
            }   
            else
            {
            NPCMotionChecker2[0] = (int) npc.posX;
            NPCMotionChecker2[1] = (int) npc.posZ;
            npcTimer2 = 0;
            }
        }

    }

    private ForgeDirection turn(ForgeDirection direction)
    {
        if (direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH) // return 1 or 2
        {
            ForgeDirection[] temp = { ForgeDirection.EAST, ForgeDirection.WEST };
            int index = (int) (Math.random() * 2);
            return temp[index];

        } else // return 3 or 0
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

    public ArrayList<int[]> initialize(int howMany)
    {
        ArrayList<int[]> outerArr = new ArrayList<int[]>();
        for (int i = 0; i < howMany; i++)
        {
            int[] myInt = { 2, 2 };
            outerArr.add(myInt); // makes all the int[]
        }
        return outerArr;
    }
}
