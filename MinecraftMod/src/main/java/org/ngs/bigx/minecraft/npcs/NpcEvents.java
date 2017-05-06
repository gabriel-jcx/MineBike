package org.ngs.bigx.minecraft.npcs;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.roles.RoleTrader;


public class NpcEvents {
	
	public NpcEvents() {
	}
	
	//NPC Interactions
	public 	static void InteractWithNPC(EntityPlayer player, EntityInteractEvent event){
		if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.dad, NpcLocations.dad.addVector(1, 1, 1))) //checks to see if NPC is Dad
			InteractWithFather(player, event);
		if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.weaponsMerchant, NpcLocations.weaponsMerchant.addVector(1, 1, 1)))  //checks to see if NPC is Weapons Merch
			InteractWithWeaponsMerchant(player, event);
		if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.blacksmith, NpcLocations.blacksmith.addVector(1, 1, 1)))  //checks to see if NPC is Blacksmith
			InteractWithBlacksmith(player, event);
	}
	
	private static void InteractWithFather(EntityPlayer player, EntityInteractEvent event){
		///Give player the mysterious key
		BiGXEventTriggers.givePlayerMessage(player, BiGXTextBoxDialogue.firstQuestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.firstQuestMsgTitle);
		if (!BiGXEventTriggers.givePlayerKey(player, "Mysterious Key", BiGXTextBoxDialogue.fatherMsg))
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.fatherMsgMap);
		
		try {
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
			Quest quest;
			
			if(player.worldObj.isRemote)
			{
				quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, "Chasing Quest", "Chasing Quest Description", BiGX.instance().clientContext.getQuestManager());
				quest.addTasks(new QuestTaskChasing(BiGX.instance().clientContext.getQuestManager(), player, ws, 0, 1));
				BiGX.instance().clientContext.getQuestManager().addAvailableQuestList(quest);
				BiGX.instance().clientContext.getQuestManager().activateQuest(quest);
			}
			else
			{
				quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, "Chasing Quest", "Chasing Quest Description", BiGX.instance().serverContext.getQuestManager());
				quest.addTasks(new QuestTaskChasing(BiGX.instance().serverContext.getQuestManager(), player, ws, 0, 1));
				BiGX.instance().serverContext.getQuestManager().addAvailableQuestList(quest);
				BiGX.instance().serverContext.getQuestManager().activateQuest(quest);
			}
		} catch (QuestException e) {
			e.printStackTrace();
		}
	}
	
	private static void InteractWithWeaponsMerchant(EntityPlayer player, EntityInteractEvent event){
		System.out.println("Interacting with Weapons NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createMarketCurrency(traderInterface.inventoryCurrency, Item.getItemById(266));
		if (traderInterface.inventorySold.items.isEmpty())
			createMarketSold(traderInterface.inventorySold);
	}
	
	private static void InteractWithBlacksmith(EntityPlayer player, EntityInteractEvent event){
		System.out.println("Interacting with Blacksmith NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createBlacksmithCurrency(traderInterface.inventoryCurrency);
		if (traderInterface.inventorySold.items.isEmpty())
			createBlacksmithSold(traderInterface.inventorySold);
		System.out.println(traderInterface.inventoryCurrency.items);
		System.out.println(traderInterface.inventoryCurrency.getInventoryStackLimit());
	}
	
	
	//////Private Helper Methods: Traders
	//////////WeaponsMerchant Market
	private static void createMarketCurrency(NpcMiscInventory inventoryCurrency, Item currency){
		inventoryCurrency.setInventorySlotContents(0, new ItemStack(currency));
		inventoryCurrency.setInventorySlotContents(1, new ItemStack(currency,3));
		inventoryCurrency.setInventorySlotContents(2, new ItemStack(currency,9));
		inventoryCurrency.setInventorySlotContents(3, new ItemStack(currency,27));
		inventoryCurrency.setInventorySlotContents(4, new ItemStack(currency,81));
	}
	
	private static void createMarketSold(NpcMiscInventory inventorySold){
		inventorySold.setInventorySlotContents(0, new ItemStack(Item.getItemById(268)));
		inventorySold.setInventorySlotContents(1, new ItemStack(Item.getItemById(267)));
		inventorySold.setInventorySlotContents(2, new ItemStack(CustomItems.swordBronze));
		inventorySold.setInventorySlotContents(3, new ItemStack(CustomItems.swordMithril));
		inventorySold.setInventorySlotContents(4, new ItemStack(CustomItems.swordEmerald));
	}
	
	//////////Blacksmith Market
	private static void createBlacksmithCurrency(NpcMiscInventory inventoryCurrency){
		inventoryCurrency.setInventorySlotContents(0, new ItemStack(Item.getItemById(268))); //wooden sword
		inventoryCurrency.setInventorySlotContents(18, new ItemStack(Item.getItemById(4420))); //water elemental
		inventoryCurrency.setInventorySlotContents(1, new ItemStack(Item.getItemById(268)));
		inventoryCurrency.setInventorySlotContents(19, new ItemStack(Item.getItemById(4421))); //fire elemental
		inventoryCurrency.setInventorySlotContents(2, new ItemStack(Item.getItemById(268)));
		inventoryCurrency.setInventorySlotContents(20, new ItemStack(Item.getItemById(4419))); //earth elemental
		inventoryCurrency.setInventorySlotContents(3, new ItemStack(Item.getItemById(268)));
		inventoryCurrency.setInventorySlotContents(21, new ItemStack(Item.getItemById(4422))); //air elemental
	}
	
	private static void createBlacksmithSold(NpcMiscInventory inventorySold){
		inventorySold.addItemStack(new ItemStack(CustomItems.swordFrost));
		inventorySold.addItemStack(new ItemStack(CustomItems.swordDemonic));
		inventorySold.addItemStack(new ItemStack(CustomItems.glaiveFrost));
		inventorySold.addItemStack(new ItemStack(CustomItems.glaiveDemonic));
	}
}
