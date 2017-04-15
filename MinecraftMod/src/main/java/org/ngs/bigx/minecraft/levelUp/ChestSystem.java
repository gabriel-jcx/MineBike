package org.ngs.bigx.minecraft.levelUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;


public class ChestSystem {
	private static Vec3 secretChest = Vec3.createVectorHelper(-174, 70, 336);
	
	private static Map<String, String> normalChests = populateNormalChests();
	private static Map<String, String> lockedChests = populateLockedChests();
	
	private static Map<String, String> populateNormalChests(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Vec3.createVectorHelper(-155, 71, 359).toString(), "level123Chest");
		return map;
	}
	
	private static Map<String, String> populateLockedChests(){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Vec3.createVectorHelper(-174, 70, 336).toString(), "MysteriousKey");
		map.put(Vec3.createVectorHelper(-155, 71, 359).toString(), "Burnt Key");
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
		
		if (keyName == "MysteriousKey"){
			putMessageInChest(c, 0, BiGXTextBoxDialogue.secretRoomMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.secretRoomMsgTitle);
			for (int i = 1; i <= levelSys.getThiefLevel(); ++i)
				putPotionInChest(c, ("Teleportation Potion "+i), i);
		}
		
		if (keyName == "Burnt Key")
			putPotionInChest(c, "Teleportation Potion 4", 0);
		
		if (keyName == "Wet Key")
			putPotionInChest(c, "Teleportation Potion 5", 0);
		
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
