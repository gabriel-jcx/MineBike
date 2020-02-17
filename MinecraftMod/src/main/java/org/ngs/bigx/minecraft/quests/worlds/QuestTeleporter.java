package org.ngs.bigx.minecraft.quests.worlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

//import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.LongHashMap;
import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class QuestTeleporter {
	public static boolean teleportFlag = false;
	public static int dimId, posX, posY, posZ;
	
    // Move the Entity to the portal
    public static void teleport(Entity entity, int targetDimensionId,int x,int y,int z) {
    	BlockPos pos = new BlockPos(x,y,z);
		Minecraft.getMinecraft().player.sendChatMessage("/tpx" + " " + targetDimensionId + " " + x + " " + y + " " + z);

		teleportFlag = true;
		dimId = targetDimensionId;
		posX = x;
		posY = y;
		posZ = z;
		
		if(targetDimensionId == Minecraft.getMinecraft().player.world.provider.getDimension())
		{
			System.out.println("[BiGX] Teleport to the same dimension?? isRemote[" + Minecraft.getMinecraft().player.world.isRemote + "]");
			(new Exception()).printStackTrace();
		}
    }
}
