package org.ngs.bigx.minecraft.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.minecraft.npcs.custom.CustomNPCAbstract;
import org.ngs.bigx.minecraft.npcs.custom.CustomNPCStorage;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.entity.EntityCustomNpc;

public class NpcDatabase {
	
	private static ArrayList<CustomNPCAbstract> customNPCs = CustomNPCStorage.customNPCs;
	
	private static Map<String, Vec3> npcs = populateMap();
	private static Map<String, Vec3> npcsTutorial = populateMapTutorial();
	private static Map<String, Vec3> npcsDungeon = populateDungeon();
	
	private static boolean customNPCSInitialized = registerCustomNPCs();
	
	public static void registerNPC(String name, Vec3 location)
	{
		if (npcs == null)
		{
			System.out.println("I AM NULL THIS IS THE CAUSE OF THE ERROR!!!!!");
		}
		else
		{
			npcs.put(name, location);
		}
	}
	
	private static Map<String, Vec3> populateMap() {
		Map<String, Vec3> map = new HashMap<String, Vec3>();
		map.put("Dad", NpcLocations.dad);
		map.put("Weapons Merchant", NpcLocations.weaponsMerchant);
		map.put("Blacksmith", NpcLocations.blacksmith);
		map.put("Skills Seller", NpcLocations.skillSeller);
		map.put("Trader", NpcLocations.trader);
		map.put("Police Officer", NpcLocations.officer);
//		map.put("Thief", NpcLocations.jailedthief1);
		map.put("Trainer", NpcLocations.tutorialGuy);
		
		// NPCs in the village
		map.put("Gate Guard Ivan", NpcLocations.guard1);
		map.put("Gate Guard Roy", NpcLocations.guard2);
		map.put("sergeant Weirdo", NpcLocations.sergeantWeirdo);
		map.put("George", NpcLocations.wanderer);
		map.put("Dr. Jose", NpcLocations.doctor);
		map.put("Farmer Sam", NpcLocations.farmer);
		map.put("B.F.F James", NpcLocations.cook);
		map.put("Paul", NpcLocations.marketPlaceAd);
		map.put("Johnson", NpcLocations.thiefInCage);
		
		//testing down here
//		map.put("jeff", NpcLocations.jeff);
//		registerCustomNPCs();
		
		return map;
	}
	
	private static boolean registerCustomNPCs()
	{
		for(CustomNPCAbstract npc : customNPCs)
		{
			npc.register();
		}
		return true;
	}
	
	private static Map<String, Vec3> populateMapTutorial() {
		Map<String, Vec3> map = new HashMap<String, Vec3>();
//		map.put("Scientist", NpcLocations.scientists);
		map.put("Training Bot", NpcLocations.trainingBot);
		map.put("Telly Port", NpcLocations.tutorialExit);
		return map;
	}
	
	private static Map<String, Vec3> populateDungeon() {
		HashMap<String, Vec3> map = new HashMap<String, Vec3>();
		map.put("Ifrit", NpcLocations.baddestGuy);
		return map;
	}
	
	public static List<String> NpcNames(int dimID) {
		List<String> toReturn = new ArrayList<String>();
		//dimID is dimension ID - 0: the home world, 102: the tutorial dimension, else: ?
		if (dimID == 0)
			for (Map.Entry<String, Vec3> item : npcs.entrySet())
				toReturn.add(item.getKey());
		else if (dimID == 102)
			for (Map.Entry<String, Vec3> item : npcsTutorial.entrySet())
				toReturn.add(item.getKey());
		else if (dimID == WorldProviderDungeon.dimID)
			for (Map.Entry<String, Vec3> item : npcsDungeon.entrySet())
				toReturn.add(item.getKey());
		return toReturn;
	}
	
	public static void spawn(WorldServer world, String name, int dimID) {
		EntityCustomNpc npc = null;
		if (dimID == 0){
			npc = NpcCommand.spawnNpc((int)npcs.get(name).xCoord, (int)npcs.get(name).yCoord, (int)npcs.get(name).zCoord, 
					MinecraftServer.getServer().worldServerForDimension(0), name, getTexture(name));
			npc.ai.movingType = getMovement(name);
//			int dialogId = getDialog(name);
		}
		else if (dimID == 102){
			npc = NpcCommand.spawnNpc((int)npcsTutorial.get(name).xCoord, (int)npcsTutorial.get(name).yCoord, 
					(int)npcsTutorial.get(name).zCoord, MinecraftServer.getServer().worldServerForDimension(102), name, getTexture(name));
		} else if (dimID == WorldProviderDungeon.dimID) {
			npc = NpcCommand.spawnNpc((int)npcsDungeon.get(name).xCoord, (int)npcsDungeon.get(name).yCoord,
					(int)npcsDungeon.get(name).zCoord, MinecraftServer.getServer().worldServerForDimension(WorldProviderDungeon.dimID), name, getTexture(name));
		}
		if (npc != null){
//			npc.display.texture = getTexture(name);
			npc.setRoleDataWatcher(getRole(name));	
			if (name.contains("Training Bot")){
				System.out.println("Altering Training Bot");
				npc.ai.stopAndInteract = false;
				npc.inventory.setOffHand(new ItemStack(Item.getItemById(50)));
			}
		}
	}
	
	public static Vec3 getSpawn(String name) {
		return npcs.get(name);
	}
	
	private static int getDialog(String name)
	{
		if (name.contains("Gate Guard Ivan"))
			return 4;
		else if (name.contains("Gate Guard Roy"))
			return 5;
		else if (name.contains("sergeant Weirdo"))
			return 6;
		else if (name.contains("George"))
			return 2;
		else if (name.contains("Dr. Jose"))
			return 7;
		else if (name.contains("Farmer Sam"))
			return 8;
		else if (name.contains("B.F.F James"))
			return 9;
		else if (name.contains("Paul"))
			return 13;
		else if (name.contains("Johnson"))
			return 10;
		
		return -1;
	}
	
	private static EnumMovingType getMovement(String name)
	{
		if (name.contains("Gate Guard Ivan"))
			return EnumMovingType.Standing;
		else if (name.contains("Gate Guard Roy"))
			return EnumMovingType.Standing;
		else if (name.contains("sergeant Weirdo"))
			return EnumMovingType.Standing;
		else if (name.contains("George"))
			return EnumMovingType.Standing;
		else if (name.contains("Dr. Jose"))
			return EnumMovingType.Standing;
		else if (name.contains("Farmer Sam"))
			return EnumMovingType.Standing;
		else if (name.contains("B.F.F James"))
			return EnumMovingType.Standing;
		else if (name.contains("Paul"))
			return EnumMovingType.Standing;
		else if (name.contains("Johnson"))
			return EnumMovingType.Standing;
		else
			return EnumMovingType.Standing;
	}
	
	private static String getTexture(String name) {
		if (name.equals("Dad"))
			return "customnpcs:textures/entity/humanmale/VillagerSteve.png";
		else if (name.contains("Merchant") || name.contains("Blacksmith") || name.contains("Seller") || name.contains("Trader"))
			return "customnpcs:textures/entity/humanmale/TraderSteve.png";
		else if (name.contains("Scientist"))
			return "customnpcs:textures/entity/humanmale/DoctorSteve.png";
		else if (name.contains("Training Bot"))
			return "customnpcs:textures/entity/humanmale/RobesBrownSteve.png";
		else if (name.contains("Police"))
			return "customnpcs:textures/entity/humanmale/BodyguardSteve.png";
		else if (name.contains("Thief"))
			return "customnpcs:textures/entity/humanmale/BodyguardSteve.png";
		else if (name.contains("Telly Port"))
			return "customnpcs:textures/entity/humanmale/TuxedoSteve.png";
		else if (name.contains("Trainer"))
			return "customnpcs:textures/entity/humanmale/CasualSteve.png";
		else if (name.contains("Ifrit"))
			return "customnpcs:textures/entity/humanmale/Evil_Gold_Knight.png";
		

		else if (name.contains("Gate Guard Ivan"))
			return "customnpcs:textures/entity/humanmale/GuardSteve.png";
		else if (name.contains("Gate Guard Roy"))
			return "customnpcs:textures/entity/humanmale/GuardSteve.png";
		else if (name.contains("sergeant Weirdo"))
			return "customnpcs:textures/entity/humanmale/CamoSpecOpsSteve.png";
		else if (name.contains("George"))
			return "customnpcs:textures/entity/humanmale/TownsmanSteve.png";
		else if (name.contains("Dr. Jose"))
			return "customnpcs:textures/entity/humanmale/WizardSteve.png";
		else if (name.contains("Farmer Sam"))
			return "customnpcs:textures/entity/humanmale/VillagerOldSteve.png";
		else if (name.contains("B.F.F James"))
			return "customnpcs:textures/entity/humanmale/ChefSteve.png";
		else if (name.contains("Paul"))
			return "customnpcs:textures/entity/humanmale/TuxedoSteve.png";
		else if (name.contains("Johnson"))
			return "customnpcs:textures/entity/humanmale/GangsterSteve.png";
		
		for(CustomNPCAbstract npc : customNPCs)
		{
			if (name.contains(npc.getName()))
				return npc.getTexture();
		}
		
		return "customnpcs:textures/entity/humanmale/GangsterSteve.png";
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
