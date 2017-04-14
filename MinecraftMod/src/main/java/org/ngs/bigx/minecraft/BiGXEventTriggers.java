package org.ngs.bigx.minecraft;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleInterface;
import noppes.npcs.roles.RoleTrader;

public class BiGXEventTriggers {	
	
	//private ContainerNPCTraderSetup weaponsShop;
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
	public static void InteractWithNPC(EntityPlayer player, EntityInteractEvent event){
		if (checkEntityInArea(event.target, -65, 73, 13, -64, 74, 14)) //checks to see if NPC is Dad
			InteractWithFather(player, event);
		if (checkEntityInArea(event.target, -55, 73, 91, -54, 74, 92))  //checks to see if NPC is Weapons Merch
			InteractWithWeaponsMerchant(player, event);
//		if (checkEntityInArea(event.target, -55, 73, 83, -54, 74, 84))  //checks to see if NPC is Potions Merch
//			InteractWithBlacksmith(player, event);
//		if (checkEntityInArea(event.target, -48, 73, 83, -47, 74, 84))  //checks to see if NPC is Food Merch
//			System.out.println("Interacting with Food NPC");
//		if (checkEntityInArea(event.target, -48, 73, 91, -47, 74, 92))  //checks to see if NPC is Books Merch
//			System.out.println("Interacting with Books NPC");
	}
	
	private static void InteractWithFather(EntityPlayer player, EntityInteractEvent event){
		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.fatherMsg);
		///Give player the mysterious key
		givePlayerMessage(player, BiGXTextBoxDialogue.firstQuestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.firstQuestMsgTitle);
		ItemStack key = new ItemStack(Item.getItemById(131));
		key.setStackDisplayName("MysteriousKey");
		if (!player.inventory.hasItemStack(key))
			player.inventory.addItemStackToInventory(key);
	}
	
	private static void InteractWithWeaponsMerchant(EntityPlayer player, EntityInteractEvent event){
		//display the trader gui with the right market
		System.out.println("Interacting with Weapons NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createMarketCurrency(traderInterface.inventoryCurrency);
		if (traderInterface.inventorySold.items.isEmpty())
			createMarketSold(traderInterface.inventorySold);
	}
	
	private static void InteractWithBlacksmith(EntityPlayer player, EntityInteractEvent event){
		//display the trader gui with the blacksmith market
		System.out.println("Interacting with Blacksmith NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createBlacksmithCurrency(traderInterface.inventoryCurrency);
		if (traderInterface.inventorySold.items.isEmpty())
			createBlacksmithSold(traderInterface.inventorySold);
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
	
	private static boolean checkEntityInArea(Entity entity, int x1, int y1, int z1, int x2, int y2, int z2){
		if (entity.posX >= x1 && entity.posX <= x2)
			if (entity.posY >= y1 && entity.posY <= y2)
				if (entity.posZ >= z1 && entity.posZ <= z2)
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
		return Math.floorDiv(numCoins, 100);
	}
	
	private static void createMarketCurrency(NpcMiscInventory inventoryCurrency){
		//////////////////////
		inventoryCurrency.addItemStack(new ItemStack(CustomItems.coinIron,5));
		inventoryCurrency.addItemStack(new ItemStack(CustomItems.coinBronze,5));
		inventoryCurrency.addItemStack(new ItemStack(CustomItems.coinGold,5));
		inventoryCurrency.addItemStack(new ItemStack(CustomItems.coinDiamond,5));
		inventoryCurrency.addItemStack(new ItemStack(CustomItems.coinEmerald,5));
	}
	
	private static void createMarketSold(NpcMiscInventory inventorySold){
		//////////////////////
		inventorySold.addItemStack(new ItemStack(Item.getItemById(267)));
		inventorySold.addItemStack(new ItemStack(CustomItems.swordBronze));
		inventorySold.addItemStack(new ItemStack(CustomItems.swordFrost));
		inventorySold.addItemStack(new ItemStack(CustomItems.swordMithril));
		inventorySold.addItemStack(new ItemStack(CustomItems.swordEmerald));
	}
	
	private static void createBlacksmithCurrency(NpcMiscInventory inventoryCurrency){
		/////////////////////
		inventoryCurrency.addItemStack(new ItemStack(Item.getItemById(131)));
	}
	
	private static void createBlacksmithSold(NpcMiscInventory inventorySold){
		/////////////////////
		inventorySold.addItemStack(new ItemStack(Item.getItemById(130)));
	}
	
}