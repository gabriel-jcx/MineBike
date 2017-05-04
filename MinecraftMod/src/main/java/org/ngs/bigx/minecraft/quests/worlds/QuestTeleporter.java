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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

public class QuestTeleporter {
    public static void teleport(Entity entity,int targetDimensionId) {
    	teleport(entity,targetDimensionId,0,0,0);
    }

    // Move the Entity to the portal
    public static void teleport(Entity entity, int targetDimensionId,int x,int y,int z) {
    	if (entity.worldObj.provider.dimensionId != targetDimensionId) {
    		Minecraft.getMinecraft().thePlayer.sendChatMessage("/tpx" + " " + targetDimensionId);
    	}
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/tp" + " " + x + " " + y + " " + z);
    }
}
