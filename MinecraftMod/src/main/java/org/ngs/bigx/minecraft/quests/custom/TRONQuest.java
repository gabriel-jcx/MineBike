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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
    private int playerScore;
    private int enemyScore;
    private boolean worldLoaded = false;
    private boolean init = false;
    private boolean[][] glassPanes = new boolean[201][201];
    private int timer = 0;

    private int[] powerBar = { 60, 0 };
    private HudRectangle power;
    private HudString stri;
    private HudRectangle rect;

    private HudString warnin;
    private int[] warningString = { 100, 400 };
    private HudString warningNumber;

    public static final String NPC_NAME = "Rinzler";
    private ForgeDirection npcRunDirection;
    static EntityCustomNpc npc;
    static NpcCommand command;

    public static int[] npcPath = new int[3];
    public static List<int[]> npcPathList = new ArrayList<int[]>();

    int numStages = 8; // how many steps ArrayList - how many steps of delay in laying down the glass
                       // panes
    ArrayList<int[]> playerLocation = initialize(numStages); // 4 slots currently in ArrayList
    boolean setBlock = false; // used to tell OnWorldTick whether they can drop a block at
                              // previousPlayerLocationLast

    private int[] NPCStart = { 15, 15 };
    private int currentNPCDirection = 2; // NPC begins moving posX
    private int npcTimer = 0;
    private int npcTimer2 = 0;
    private int[] NPCMotionChecker = { 15, 15 };
    // 5 seconds = 100 ticks

    public TRONQuest()
    {
        super();
        initializeGlassPanes();
        progress = 0;
        name = "TRONQuest";
        setBlock = false;
        completed = false;
        playerScore = 0;
        enemyScore = 0;

        warnin = new HudString(warningString[0], warningString[1] - 300, "Keep moving, elimination in:");
        warnin.scale = 3.0f;
        warningNumber = new HudString(warningString[0] + 400, warningString[1] - 300, "");
        warningNumber.scale = 5.0f;

        stri = new HudString(powerBar[0], powerBar[1] + 5, "SPEED/POWER");
        stri.centerX = true;
        stri.centerY = true;
        power = new HudRectangle(powerBar[0], powerBar[1], 68, 250, 0x0000ffff);
        power.centerX = true;
        power.centerY = true;
        npcPath = new int[3];
        npcPathList = new ArrayList<int[]>();
        npcRunDirection = ForgeDirection.EAST;
        // 0x tells it's a hex number, ff at the end is for transparency
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
        HudManager.unregisterRectangle(power);
        HudManager.unregisterString(stri);
        HudManager.unregisterString(warnin);
        HudManager.unregisterString(warningNumber);
        setBlock = false;
        init = false;
        super.complete();
        timer = 0;
    }

    @Override
    public void start()
    {
        progress = 0;
        // teleport them to the TRON arena
        QuestTeleporter.teleport(player, WorldProviderTRON.TRONDIMENSIONID, 0, 50, 0);
        // HudManager.registerRectangle(power);
        // HudManager.registerString(stri);
        super.start();
    }

    public boolean isNew(int[] coordinate)
    {
        for (int x = 0; x < numStages; x++)
        {
            if (coordinate[0] == playerLocation.get(x)[0] && coordinate[1] == playerLocation.get(x)[1])
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

//        for(int i = 0; i < playerLocation.size(); i++)
//        {
//            System.out.print(i + ": [ " + playerLocation.get(i)[0] + ", " + playerLocation.get(i)[1] + "]");
//        }
//        System.out.println();

        // checking if player has moved
        double velocity = Math.pow((Math.pow((double) event.player.motionX, 2) + (Math.pow(
                (double) event.player.motionZ, 2))), 0.5);
        if (velocity == 0) // values get close to 0 when running
        {
            timer++;
        } else
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
        if (velocity > 0)
        {
            int speedValue = (int) (velocity * 500);
            power.color = 0x0000ff00 + (int) (speedValue);
//            power.y = 300 - speedValue;
            // System.out.println(speedValue);
            power.h = -1 * speedValue;
        }
        // power = new HudRectangle(powerBar[0], powerBar[1] - 250, 68, 250,
        // 0xff0000ff);

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
            // System.out.println("Time to set the block.");
            setBlock = true;
            // shift the ArrayList
            // System.out.println(playerLocation.get(0)[0] + " " +
            // playerLocation.get(0)[1]);
            int[] newLoc = { currentCoX, currentCoZ };
            playerLocation.add(0, newLoc); // adds the player coordinates to the beginning of the list
            playerLocation.remove(numStages); // removes at end of list
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

        // sets previousPlayerLocation between actual location and

        // previousPlayerLocation, this ensures that the
        // previousPlayerLocationLast variable will be far away enough
    }

    @Override
    public void onWorldTickEvent(TickEvent.WorldTickEvent event)
    {
        // player.capabilities.setPlayerWalkSpeed(3);

        if (!worldLoaded || event.world.provider.dimensionId != WorldProviderTRON.TRONDIMENSIONID)
        {
            return; // prevent errors
        }

        if (!init)
        {
            for (int i = -105; i <= 105; i++)
            {
                for (int j = -105; j <= 105; j++)
                {
                    if (i >= -100 && i <= 100 && j >= -100 && j <= 100)
                    {
                        glassPanes[i + 100][j + 100] = false;
                    }
                    event.world.setBlock(i, 45, j, Blocks.air);
                    event.world.setBlock(i, 46, j, Blocks.air);
                }
            }
            NPCStart[0] = 20;
            NPCStart[1] = 20;
            currentNPCDirection = (int) (Math.random() * 3);
            npcTimer = 0;
            npcTimer2 = 0;
            NPCMotionChecker[0] = 20;
            NPCMotionChecker[1] = 20;
//            private int[] NPCStart = { 15, 15 };
//            private int currentNPCDirection = 2; // NPC begins moving posX
//            private int npcTimer = 0;
//            private int npcTimer2 = 0;
//            private int[] NPCMotionChecker = { 15, 15 };
            // npcPath = new int[3];
            // npcPathList = new ArrayList<int[]>();
            // npcRunDirection = ForgeDirection.EAST;

            NpcCommand.removeNpc(NPC_NAME + " ", WorldProviderTRON.TRONDIMENSIONID);

            WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderTRON.TRONDIMENSIONID);
            synchronized (ws.loadedEntityList)
            {
                Iterator iter = ws.loadedEntityList.iterator();
                while (iter.hasNext())
                {
                    Entity entity = (Entity) iter.next();
                    if (!(entity instanceof EntityPlayer))
                        entity.isDead = true;
                }
            }

            // spawns the npc
            npc = NpcCommand.spawnNpc(NPCStart[0], 46, NPCStart[1], (WorldServer) event.world.provider.worldObj,
                    NPC_NAME + " ", Flynn.TEXTURE);
            command = new NpcCommand(BigxClientContext.getInstance(), npc);
            command.setSpeed(10);

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
        // if init
//        else
//        {
//            npcPathList.clear();
//            npcPath[0] = ((int) npc.serverPosX) + 4 + (int) ((new Random()).nextDouble() * 10);
//            npcPath[1] = ((int) npc.serverPosY);
//            npcPath[2] = ((int) npc.serverPosZ);
//
//            npcPathList.add(0, npcPath);
//            npcPathList.add(1, npcPath);
//            npc.ai.setMovingPath(npcPathList);
//            npc.ai.getMovingPath().add(npcPath);
//            command.enableMoving(true);
//        }

        if (npc != null)
        {
            int NPCPaneX = -5;
            int NPCPaneZ = -5;
            boolean turned = (Math.abs(NPCStart[0] - npc.posX) + Math.abs(NPCStart[1] - npc.posZ) > 25);

            switch (npcRunDirection)
            {
            case NORTH:
                NPCPaneX = (int) npc.posX;
                NPCPaneZ = (int) npc.posZ - 2;
                break;
            case WEST:
                NPCPaneX = (int) npc.posX + 2;
                NPCPaneZ = (int) npc.posZ;
                break;
            case EAST:
                NPCPaneX = (int) npc.posX - 2;
                NPCPaneZ = (int) npc.posZ;
                break;
            case SOUTH:
                NPCPaneX = (int) npc.posX;
                NPCPaneZ = (int) npc.posZ + 2;
                break;
            }
            event.world.setBlock(NPCPaneX, 45, NPCPaneZ, Blocks.stained_glass_pane);
            event.world.setBlock(NPCPaneX, 46, NPCPaneZ, Blocks.stained_glass_pane);
            if (NPCPaneX <= 100 && NPCPaneX >= -100 && NPCPaneZ <= 100 && NPCPaneZ >= -100)
            // prevent outOfBounds
            {
                glassPanes[NPCPaneX + 100][NPCPaneZ + 100] = true;
            }
            updateNpcPath();
        }

        // update the npc's path
//        npcPath[0] = 15;
//        npcPath[1] = 46;
//        npcPath[2] = 15;

        // end updating

        if (setBlock) // if true, then you can now set the glass panes
        {
            // System.out.println("Setting block");
            int paneX = playerLocation.get(numStages - 1)[0];
            int paneZ = playerLocation.get(numStages - 1)[1];

            event.world.setBlock(paneX, 45, paneZ, Blocks.stained_glass_pane);
            event.world.setBlock(paneX, 46, paneZ, Blocks.stained_glass_pane);
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
//        double[] newPoint = Utils.lerp(
//                new double[] {npc.posX, npc.posY, npc.posZ}, 
//                new double[] {ball.posX, ball.posY, ball.posZ}, 
//                t);

//        double[] newPoint = new double[] { npc.serverPosX + 4.0, 46, 20.0 };
//        
//        int[] intPoint = new int[] {(int) newPoint[0], (int) newPoint[1], (int) newPoint[2]};
//        npcPath[0] = (int) newPoint[0];
//        npcPath[1] = (int) newPoint[1];
//        npcPath[2] = (int) newPoint[2];

        // this is where you update where the NPC has to go
        if (Math.abs(NPCStart[0] - npc.posX) + Math.abs(NPCStart[1] - npc.posZ) > 35) // if NPC went further than 10
        {
            npcRunDirection = turn(npcRunDirection); // new direction
            NPCStart[0] = (int) npc.posX;
            NPCStart[1] = (int) npc.posZ;
        }

        int tempX = (int) npc.posX;
        int tempY = (int) npc.posY;
        int tempZ = (int) npc.posZ;

        switch (npcRunDirection) // checking the current direction of NPC
        {
        case NORTH:
            npcPath[0] = tempX;
            npcPath[1] = tempY;
            npcPath[2] = tempZ + 2; // going north
            break;
        case WEST:
            npcPath[0] = tempX - 2;// going west
            npcPath[1] = tempY;
            npcPath[2] = tempZ;
            break;
        case EAST:
            npcPath[0] = tempX + 2;// going east
            npcPath[1] = tempY;
            npcPath[2] = tempZ;
            break;
        case SOUTH:
            npcPath[0] = tempX;
            npcPath[1] = tempY;
            npcPath[2] = tempZ - 2;// going south
            break;
        }

//        npcPath[0] = (int) npc.posX;
//        npcPath[1] = (int) npc.posY;
//        npcPath[2] = (int) npc.posZ + 3; //going north

        npcPathList.set(0, npcPath);
        npcPathList.set(1, npcPath);
        npc.ai.setMovingPath(npcPathList);
//      command.addPathPoint(intPoint);
        npc.ai.getMovingPath().add(npcPath);
        command.enableMoving(true);

        System.out.println(tempX + "   " + (int) npc.posX);
        System.out.println(tempY + "   " + (int) npc.posY);

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
        
        if (npcTimer2 > 750)
        {
            if (Math.abs(NPCMotionChecker[0] - (int) npc.posX) <= 2 && Math.abs(NPCMotionChecker[1] - (int) npc.posZ) <= 2)
            {
                returnToMainMenu();
            }            
            npcTimer2 = 0;
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
