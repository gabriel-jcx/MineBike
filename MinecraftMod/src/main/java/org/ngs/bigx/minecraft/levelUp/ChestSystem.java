package org.ngs.bigx.minecraft.levelUp;

import java.util.HashMap;
import java.util.Map;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;


public class ChestSystem {
	
	private static Map<String, String> normalChests = populateNormalChests();
	private static Map<String, String> lockedChests = populateLockedChests();
	
	private static Map<String, String> populateNormalChests(){
		Map<String, String> map = new HashMap<String, String>();
//		map.put(Vec3.createVectorHelper(0, 72, 117).toString(), "TutorialChestWater");
//		map.put(Vec3.createVectorHelper(1, 72, 117).toString(), "TutorialChestWater");
//		map.put(Vec3.createVectorHelper(95, 79, 0).toString(), "TutorialChestAir"); 
//		map.put(Vec3.createVectorHelper(96, 79, 0).toString(), "TutorialChestAir");
//		map.put(Vec3.createVectorHelper(10, 63, -111).toString(), "TutorialChestEarth");
//		map.put(Vec3.createVectorHelper(-155, 71, 359).toString(), "level123Chest");
		map.put(Vec3.createVectorHelper(568, 65, -4).toString(), "TutorialChest");
		return map;
	}
	
	private static Map<String, String> populateLockedChests(){
		Map<String, String> map = new HashMap<String, String>();
//		map.put(Vec3.createVectorHelper(604, 66, -1).toString(), "Shiny Key"); //TODO: change coords
//		map.put(Vec3.createVectorHelper(604, 66, 0).toString(), "Shiny Key"); //TODO: change coords
		map.put(Vec3.createVectorHelper(96, 55, -54).toString(), "Mysterious Key");
		map.put(Vec3.createVectorHelper(20, 77, -76).toString(), "Burnt Key");
		map.put(Vec3.createVectorHelper(154, 63, 245).toString(), "Damp Key");
		map.put(Vec3.createVectorHelper(95, 55, -55).toString(), "Dusty Key"); //TODO: change coords
		map.put(Vec3.createVectorHelper(125, 160, -140).toString(), "Light Key"); //TODO: change coords
		return map;
	}
	
	public static void interactWithChests(PlayerInteractEvent e, LevelSystem levelSys){
		System.out.println("Interacting with Chest");
		String chestCoords = Vec3.createVectorHelper(e.x, e.y, e.z).toString();
		System.out.println(chestCoords);
		if (e.action == Action.LEFT_CLICK_BLOCK)
			e.setCanceled(true);
		if (normalChests.containsKey(chestCoords))
			interactWithNormalChest(e, normalChests.get(chestCoords));
		else if (lockedChests.containsKey(chestCoords))
			interactWithLockedChest(e, levelSys, lockedChests.get(chestCoords));
	}
	
	public static void interactWithNormalChest(PlayerInteractEvent e, String chestName){
		System.out.println("CHEST FOUND");
		TileEntityChest c = (TileEntityChest)e.world.getTileEntity(e.x, e.y, e.z);
		if (e.action == Action.LEFT_CLICK_BLOCK)
			e.setCanceled(true);
		
		if (chestName == "TutorialChest"){
			c.setInventorySlotContents(0, new ItemStack(Item.getItemById(268))); //wooden sword
			c.setInventorySlotContents(1, new ItemStack(Item.getItemById(266)));
		}
		
//		if (chestName == "TutorialChestWater")
//			c.setInventorySlotContents(0, new ItemStack(Item.getItemById(373), 1, 1)); //Health potion? TODO: Double check ids
//		
//		else if (chestName == "TutorialChestAir")
//			c.setInventorySlotContents(0, new ItemStack(Item.getItemById(266)));//new ItemStack(Item.getItemById(373), 1, 8194)); //Swiftness potion? TODO: Double check ids
//		
//		else if (chestName == "TutorialChestEarth"){
//			c.setInventorySlotContents(0, new ItemStack(Item.getItemById(268))); //wooden sword
//			c.setInventorySlotContents(1, new ItemStack(Item.getItemById(269))); //wood shovel
//			c.setInventorySlotContents(2, new ItemStack(Item.getItemById(270))); //wood pickaxe
//			c.setInventorySlotContents(3, new ItemStack(Item.getItemById(271))); //wood axe
//		}
//		
//		else if (chestName == "level123Chest")
//			putMessageInChest(c, 0, BiGXTextBoxDialogue.caveChestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.caveChestMsgTitle);
	}
	
	public static void interactWithLockedChest(PlayerInteractEvent e, LevelSystem levelSys, String keyName){
		System.out.println("LOCKED CHEST FOUND");
		ChestLocked(e, e.entityPlayer, keyName);
		TileEntityChest c = (TileEntityChest)e.world.getTileEntity(e.x, e.y, e.z);
		
		if (keyName == "Shiny Key"){
			putPotionInChest(c, "Teleportation Potion - Past", 0);	
		}
		
		if (keyName == "Mysterious Key"){
//			for (int i = 1; i <= levelSys.getPlayerLevel() && i <= 3; ++i)
//				putPotionInChest(c, ("Teleportation Potion "+i), i-1);
			putPotionInChest(c, "Teleportation Potion 1", 0);	
		}
		
		if (keyName == "Burnt Key"){
			putPotionInChest(c, "Teleportation Potion 2", 0);	
		}
		
		if (keyName == "Damp Key"){
			putPotionInChest(c, "Teleportation Potion 3", 0);	
		}
		
		if (keyName == "Dusty Key"){
			putPotionInChest(c, "Teleportation Potion 4", 0);	
		}
		
		if (keyName == "Light Key"){
			putPotionInChest(c, "Teleportation Potion 5", 0);	
		}
		
	}
	
	//private helper methods
	private static void ChestLocked(PlayerInteractEvent event, EntityPlayer player, String keyName){
		if(player.inventory.getCurrentItem() == null || !player.inventory.getCurrentItem().getDisplayName().contains(keyName)){
				event.setCanceled(true);
				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.chestLocked);
		}
	}
	
	private static void putPotionInChest(TileEntityChest c, String displayName, int slot){
		ItemStack p = new ItemStack(Items.potionitem);
		p.setStackDisplayName(displayName);
		c.setInventorySlotContents(slot, p);
	}
	
	private static void putMessageInChest(TileEntityChest c, int slot, String message, String author, String title){
		ItemStack b = BiGXEventTriggers.createMessage(message, author, title);
		c.setInventorySlotContents(slot, b);
	}
	
}
