package org.ngs.bigx.minecraft;

import java.awt.Event;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BiGXEventTriggers {	
	
	private boolean soundTriggerEntered = false;
	
	public static void onRightClick(PlayerInteractEvent event, EntityPlayer player){
		DoorLocked(event, player);
		ChestLocked(event, player);
	}
	
	public static void DoorLocked(PlayerInteractEvent event, EntityPlayer player){
		if(event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)){
			if(checkClickedInArea(event, -156,71,344, -156,71,345) && checkPlayerInArea(player, -158,71,344, -156,72,345))
				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.doorLocked);;
		}
	}
	
	public void MusicPlaying(EntityPlayer player){
		if (checkPlayerInArea(player, -167, 70, 343, -166, 74, 346) && player.dimension == 100 && !soundTriggerEntered){
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.soundComment);
			soundTriggerEntered = true;
		}
	}
	
	public static void ChestLocked(PlayerInteractEvent event, EntityPlayer player){
		if (checkPlayerInArea(player, -177, 70, 333, -171, 74, 339))//checking if player is in Secret Room
			if(player.inventory.getCurrentItem() == null || !player.inventory.getCurrentItem().getDisplayName().contains("MysteriousKey")){
				event.setCanceled(true);
				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.chestLocked);
			}
	}
	
	public static void GivePlayerGoldfromCoins(EntityPlayer player, int numOfCoins){
		int numOfGold = convertCoinsToGold(numOfCoins);
		String rewardStr = BiGXTextBoxDialogue.gotReward + numOfGold + " Gold Ingots!";
		GuiMessageWindow.showGoldBar(rewardStr);
		
		if (numOfGold > 0)
			for (int i = 0; i < numOfGold; i++)
				player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(266))); ///Add reward to inventory
	}
	
	//NPC Interactions
	public static void InteractWithNPC(EntityPlayer player){
		if (checkPlayerInArea(player, -67, 73, 12, -60, 75, 14)){
			InteractWithFather(player);
		}
	}
	
	private static void InteractWithFather(EntityPlayer player){
		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.fatherMsg);
		///Give player message from the friend
//		ItemStack b = new ItemStack(Items.written_book);
//		NBTTagList pages = new NBTTagList();
//		pages.appendTag(new NBTTagString("Your father is in danger. You need to find the one after him and stop him. Go to the cave just outside of town and follow the music. This key will unveil answers."));
//		b.stackTagCompound = new NBTTagCompound();
//		b.stackTagCompound.setTag("author", new NBTTagString("A friend"));
//		b.stackTagCompound.setTag("title", new NBTTagString("A Message"));
//		b.stackTagCompound.setTag("pages", pages);
//		if (!player.inventory.hasItemStack(b))
//			player.inventory.addItemStackToInventory(b);
		///Give player the mysterious key
		givePlayerMessage(player, BiGXTextBoxDialogue.firstQuestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.firstQuestMsgTitle);
		ItemStack key = new ItemStack(Item.getItemById(131));
		key.setStackDisplayName("MysteriousKey");
		if (!player.inventory.hasItemStack(key))
			player.inventory.addItemStackToInventory(key);
	}
	
	
	//Private helper methods
	private static boolean checkPlayerInArea(EntityPlayer player, int x1, int y1, int z1, int x2, int y2, int z2){
		if (player.posX >= x1 && player.posX <= x2)
			if (player.posY >= y1 && player.posY <= y2)
				if (player.posZ >= z1 && player.posZ <= z2)
					return true;
		return false;
	}
	
	private static boolean checkClickedInArea(PlayerInteractEvent event, int x1, int y1, int z1, int x2, int y2, int z2){
		if (event.x >= x1 && event.x <= x2)
			if (event.y >= y1 && event.y <= y2)
				if (event.z >= z1 && event.z <= z2)
					return true;
		return false;
	}
	
	private static void givePlayerMessage(EntityPlayer player, String message, String author, String title){
		ItemStack b = new ItemStack(Items.written_book);
		NBTTagList pages = new NBTTagList();
		pages.appendTag(new NBTTagString(message));
		b.stackTagCompound = new NBTTagCompound();
		b.stackTagCompound.setTag("author", new NBTTagString(author));
		b.stackTagCompound.setTag("title", new NBTTagString(title));
		b.stackTagCompound.setTag("pages", pages);
		if (!player.inventory.hasItemStack(b))
			player.inventory.addItemStackToInventory(b);
	}
	
	private static int convertCoinsToGold(int numCoins){
		return Math.floorDiv(numCoins, 10);
	}
	
}
