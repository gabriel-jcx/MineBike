package org.ngs.bigx.utility;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;

public class NpcCommand {
	public static void spawnNpc(float x, float y, float z, World w, String name) {
		EntityCustomNpc npc = new EntityCustomNpc(w);
		npc.display.name = name;
		npc.setPosition(x, y, z);
		
		npc.ai.startPos = new int[]{
	    		MathHelper.floor_double(x),
	    		MathHelper.floor_double(y),
	    		MathHelper.floor_double(z)};
		w.spawnEntityInWorld(npc);
	    
	    npc.setHealth(npc.getMaxHealth());
	}
}
