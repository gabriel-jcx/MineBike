package org.ngs.bigx.minecraft.levelUp;

import java.util.HashMap;
import java.util.Map;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;


public class ChestSystem {
	
	private static Map<String, String> normalChests = populateNormalChests();
	private static Map<String, String> lockedChests = populateLockedChests();
	
	private static Map<String, String> populateNormalChests(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Vec3.createVectorHelper(-155, 71, 359).toString(), "level123Chest");
		return map;
	}
	
	private static Map<String, String> populateLockedChests(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Vec3.createVectorHelper(105, 70, 214).toString(), "Mysterious Key");
		map.put(Vec3.createVectorHelper(125, 160, -140).toString(), "Burnt Key");
		map.put(Vec3.createVectorHelper(154, 63, 245).toString(), "Damp Key");
		map.put(Vec3.createVectorHelper(95, 55, -55).toString(), "Dusty Key"); ////Change coordinates later
		map.put(Vec3.createVectorHelper(125, 160, -140).toString(), "Light Key"); ////Change coordinates later
		return map;
	}
	
	public static void interactWithChests(PlayerInteractEvent e, LevelSystem levelSys){
		System.out.println("Interacting with Chest");
		String chestCoords = Vec3.createVectorHelper(e.x, e.y, e.z).toString();
		if (normalChests.containsKey(chestCoords))
			interactWithNormalChest(e, normalChests.get(chestCoords));
		else if (lockedChests.containsKey(chestCoords))
			interactWithLockedChest(e, levelSys, lockedChests.get(chestCoords));
	}
	
	public static void interactWithNormalChest(PlayerInteractEvent e, String chestName){
		System.out.println("CHEST FOUND");
		TileEntityChest c = (TileEntityChest)e.world.getTileEntity(e.x, e.y, e.z);
		
		if (chestName == "level123Chest")
			putMessageInChest(c, 0, BiGXTextBoxDialogue.caveChestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.caveChestMsgTitle);
	}
	
	public static void interactWithLockedChest(PlayerInteractEvent e, LevelSystem levelSys, String keyName){
		System.out.println("LOCKED CHEST FOUND");
		ChestLocked(e, e.entityPlayer, keyName);
		TileEntityChest c = (TileEntityChest)e.world.getTileEntity(e.x, e.y, e.z);
		
		if (keyName == "Mysterious Key"){
			putMessageInChest(c, 0, BiGXTextBoxDialogue.level1Msg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.level1MsgTitle);
			for (int i = 1; i <= levelSys.getPlayerLevel() && i <= 3; ++i)
				putPotionInChest(c, ("Teleportation Potion "+i), i);
//			putPotionInChest(c, ("Teleportation Potion 1"), 0);
		}
		
		if (keyName == "Burnt Key"){
			putMessageInChest(c, 0, BiGXTextBoxDialogue.fireLevelMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.fireLevelMsgTitle);
			putPotionInChest(c, "Teleportation Potion 4", 0);	
		}
		
		if (keyName == "Damp Key"){
			putMessageInChest(c, 0, BiGXTextBoxDialogue.waterLevelMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.waterLevelMsgTitle);
			putPotionInChest(c, "Teleportation Potion 5", 0);	
		}
		
		if (keyName == "Dusty Key"){
			putMessageInChest(c, 0, BiGXTextBoxDialogue.earthLevelMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.earthLevelMsgTitle);
			putPotionInChest(c, "Teleportation Potion 6", 0);	
		}
		
		if (keyName == "Light Key"){
			putMessageInChest(c, 0, BiGXTextBoxDialogue.airLevelMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.airLevelMsgTitle);
			putPotionInChest(c, "Teleportation Potion 7", 0);	
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
