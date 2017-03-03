package org.ngs.bigx.minecraft.quests.worlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/***
 * @author localadmin
 * 
 * Reference: http://www.minecraftforge.net/forum/topic/18295-172-dimensional-teleporting-without-the-portals/
 *
 */
public class QuestTeleporter extends Teleporter {
   
    // Setup Specific Variables
    private WorldServer worldserver;

    public QuestTeleporter(WorldServer worldserver) {
        super(worldserver);
        
        // Setup Variables
        this.worldserver = worldserver;
        
    }
    
    public void teleport(Entity entity,World world) {
    	teleport(entity,world,0,0,0);
    }

    // Move the Entity to the portal
    public void teleport(Entity entity, World world,int x,int y,int z) {
       
        // Setup Variables
        EntityPlayerMP playerMP = (EntityPlayerMP) entity;
        
        // Set default location
        double dx = x;
        double dy = y;
        double dz = z;
        
        System.out.println("RUNNING FROM TELEPORT METHOD");
        // check for zeros
        if (dx == 0 && dy == 0 && dz == 0) {
           
            // Set height to something big
            dy = 250;

            // Drop down until find solid
            while (world.getBlock((int) dx, (int) dy - 1, (int) dz).equals(Blocks.air) && dy > 0) {
               
                dy--;
                
            }

            // Last check if dy == 0
            if (dy == 0) {
               
                dy = 128;
                
            }
            
        }

        // Offset locations for accuracy
        dx = dx + 0.5d;
        dy = dy + 1.0d;
        dz = dz + 0.5d;
        entity.setPosition(dx, dy, dz);
        
        // Freeze motion
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
        entity.setPosition(dx, dy, dz); 
        
//        
//        world.getChunkFromBlockCoords(x, z);
//        world.getChunkProvider().loadChunk(world.getChunkFromBlockCoords(x, z).xPosition, world.getChunkFromBlockCoords(x, z).zPosition);

        // Set Dimension
        if (entity.worldObj.provider.dimensionId != world.provider.dimensionId) {
            playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, world.provider.dimensionId, this);
        }

        entity.setPosition(dx, dy, dz); // silly to do this multiple time,s but it kept offsetting entity until this was done
    
    }

    @Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        return false;
    }

    @Override
    public void removeStalePortalLocations(long par1) {
    }

    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
    }
    
}
