package org.ngs.bigx.minecraft.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.utility.NpcCommand;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import noppes.npcs.entity.EntityCustomNpc;

public class NpcDatabase {
	
	private static Map<String, Vec3> npcs = populateMap();
	private static Map<String, Vec3> npcsTutorial = populateMapTutorial();
	
	private static Map<String, Vec3> populateMap() {
		Map<String, Vec3> map = new HashMap<String, Vec3>();
		map.put("Dad", NpcLocations.dad);
		map.put("Weapons Merchant", NpcLocations.weaponsMerchant);
		map.put("Blacksmith", NpcLocations.blacksmith);
		map.put("Potions Seller", NpcLocations.potionSeller);
		return map;
	}
	
	private static Map<String, Vec3> populateMapTutorial() {
		Map<String, Vec3> map = new HashMap<String, Vec3>();
		map.put("Scientist", NpcLocations.scientists);
		map.put("Training Bot", NpcLocations.trainingBot);
		return map;
	}
	
	public static List<String> NpcNames(int dimID) {
		List<String> toReturn = new ArrayList<String>();
		if (dimID == 0)
			for (Map.Entry<String, Vec3> item : npcs.entrySet())
				toReturn.add(item.getKey());
		else if (dimID == 102)
			for (Map.Entry<String, Vec3> item : npcsTutorial.entrySet())
				toReturn.add(item.getKey());
//		System.out.println(toReturn);
		return toReturn;
	}
	
	public static void spawn(WorldServer world, String name, int dimID) {
//		System.out.println("System is remote in DIMID " + dimID + ": " + world.isRemote);
		EntityCustomNpc npc = null;
		if (dimID == 0){
			npc = NpcCommand.spawnNpc((int)npcs.get(name).xCoord, (int)npcs.get(name).yCoord, (int)npcs.get(name).zCoord, MinecraftServer.getServer().worldServerForDimension(0), name);
//			npc.setPosition(npcs.get(name).xCoord, (float)npcs.get(name).yCoord, npcs.get(name).zCoord);
		}
		else if (dimID == 102){
//			System.out.println("[BiGX] NPC SPAWN FUNCTION [" + name + "]");
			npc = NpcCommand.spawnNpc((int)npcsTutorial.get(name).xCoord, (int)npcsTutorial.get(name).yCoord, (int)npcsTutorial.get(name).zCoord, MinecraftServer.getServer().worldServerForDimension(102), name);
//			npc.setPosition(npcsTutorial.get(name).xCoord, (float)npcsTutorial.get(name).yCoord, npcsTutorial.get(name).zCoord);
		}
		if (npc != null){
//			System.out.println("NPC Placed!");
			npc.display.texture = getTexture(name);
			npc.setRoleDataWatcher(getRole(name));	
			if (name.contains("Training Bot")){
				System.out.println("Altering Training Bot");
				npc.ai.stopAndInteract = false;
//				npc.setHealth(10);
				npc.inventory.setInventorySlotContents(0, new ItemStack(Item.getItemById(50)));
			}
		}
	}
	
	public static Vec3 getSpawn(String name) {
		return npcs.get(name);
	}
	
	private static String getTexture(String name) {
		if (name.equals("Dad"))
			return "customnpcs:textures/entity/humanmale/VillagerSteve.png";
		if (name.contains("Merchant") || name.contains("Blacksmith") || name.contains("Seller"))
			return "customnpcs:textures/entity/humanmale/TraderSteve.png";
		if (name.contains("Scientist"))
			return "customnpcs:textures/entity/humanmale/DoctorSteve.png";
		if (name.contains("Training Bot"))
			return "customnpcs:textures/entity/humanmale/RobesBrownSteve.png";
		
		return "customnpcs:textures/entity/humanmale/Steve.png";
	}
	
	private static String getRole(String name){
		if (name.contains("Merchant") || name.contains("Blacksmith") || name.contains("Seller"))
			return "Trader";
		return "No Role";
	}
	
	public static void sortFurthestSpawn(List<EntityCustomNpc> list) {
		// TODO implement
	}
}
