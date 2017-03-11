package org.ngs.bigx.minecraft;

import java.awt.Event;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BiGXEventTriggers {	
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
	
	public static void MusicPlaying(EntityPlayer player){
		if (checkPlayerInArea(player, -167, 70, 343, -166, 74, 346))
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.soundComment);
	}
	
	public static void ChestLocked(PlayerInteractEvent event, EntityPlayer player){
		if (checkPlayerInArea(player, -177, 70, 333, -171, 74, 339))//checking if player is in Secret Room
			//System.out.println(player.inventory.getCurrentItem() == null || !player.inventory.getCurrentItem().getDisplayName().contains("MysteriousKey"));
			if(player.inventory.getCurrentItem() == null || !player.inventory.getCurrentItem().getDisplayName().contains("MysteriousKey"))
			{
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
	
	private static int convertCoinsToGold(int numCoins){
		return Math.floorDiv(numCoins, 10);
	}
	
}
