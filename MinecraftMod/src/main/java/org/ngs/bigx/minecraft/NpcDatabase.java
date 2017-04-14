package org.ngs.bigx.minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.utility.NpcCommand;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;

public class NpcDatabase {
	
	private static Map<String, Vec3> npcs = populateMap();
	
	private static Map<String, Vec3> populateMap() {
		Map<String, Vec3> map = new HashMap<String, Vec3>();
		map.put("Dad", Vec3.createVectorHelper(-65, 74, 13));
		map.put("Weapons Merchant", Vec3.createVectorHelper(-55, 73, 91));
		map.put("Blacksmith", Vec3.createVectorHelper(-55, 73, 83));
		return map;
	}
	
	public static List<String> NpcNames() {
		List<String> toReturn = new ArrayList<String>();
		for (Map.Entry<String, Vec3> item : npcs.entrySet())
			toReturn.add(item.getKey());
		return toReturn;
	}
	
	public static void spawn(World world, String name) {
		EntityCustomNpc npc = NpcCommand.spawnNpc((float)npcs.get(name).xCoord, (float)npcs.get(name).yCoord, (float)npcs.get(name).zCoord, world, name);
		npc.display.texture = getTexture(name);
		npc.setRoleDataWatcher(getRole(name));
	}
	
	public static Vec3 getSpawn(String name) {
		return npcs.get(name);
	}
	
	private static String getTexture(String name) {
		if (name.equals("Dad"))
			return "customnpcs:textures/entity/humanmale/VillagerSteve.png";
		if (name.contains("Merchant") || name.contains("Blacksmith"))
			return "customnpcs:textures/entity/humanmale/TraderSteve.png";
		
		return "customnpcs:textures/entity/humanmale/Steve.png";
	}
	
	private static String getRole(String name){
		if (name.contains("Merchant") || name.contains("Blacksmith"))
			return "Trader";
		return "No Role";
	}
	
	public static void sortFurthestSpawn(List<EntityCustomNpc> list) {
		// TODO implement
	}
}
