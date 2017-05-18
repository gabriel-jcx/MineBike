package org.ngs.bigx.minecraft.npcs;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
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
		if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.potionSeller, NpcLocations.potionSeller.addVector(1, 1, 1)))  //checks to see if NPC is PotionSeller
			InteractWithPotionSeller(player, event);
	}
	
	private static void InteractWithFather(EntityPlayer player, EntityInteractEvent event){
		///Give player the mysterious key
//		BiGXEventTriggers.givePlayerMessage(player, BiGXTextBoxDialogue.firstQuestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.firstQuestMsgTitle);
		if (!BiGXEventTriggers.givePlayerKey(player, "Mysterious Key", BiGXTextBoxDialogue.fatherMsg) && !player.worldObj.isRemote)
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.fatherMsgMap);
		else{
			if (!player.worldObj.isRemote){
				GuiMessageWindow.showBook(BiGXTextBoxDialogue.questBookObtained);
				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.questBookInstructions);	
			}
		}
		
		try {
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
			Quest quest;
			
			if(player.worldObj.isRemote)
			{
				System.out.println("InteractWithFather Quest Generation: CLIENT");
				quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, BiGXTextBoxDialogue.questChase1Title, BiGXTextBoxDialogue.questChase1Description, BiGX.instance().clientContext.getQuestManager());
				quest.addTasks(new QuestTaskChasing(new LevelSystem(), BiGX.instance().clientContext.getQuestManager(), player, ws, 1, 4));
				if(BiGX.instance().clientContext.getQuestManager().addAvailableQuestList(quest))
					BiGX.instance().clientContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_CHASE_REG);
			}
			else
			{
				System.out.println("InteractWithFather Quest Generation: SERVER");
				quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, BiGXTextBoxDialogue.questChase1Title, BiGXTextBoxDialogue.questChase1Description, BiGX.instance().serverContext.getQuestManager());
				quest.addTasks(new QuestTaskChasing(new LevelSystem(), BiGX.instance().serverContext.getQuestManager(), player, ws, 1, 4));
				if(BiGX.instance().serverContext.getQuestManager().addAvailableQuestList(quest))
					BiGX.instance().serverContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_CHASE_REG);
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
			createWeaponsCurrency(traderInterface.inventoryCurrency, Item.getItemById(266));
		if (traderInterface.inventorySold.items.isEmpty())
			createWeaponsSold(traderInterface.inventorySold);
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
	}
	
	private static void InteractWithPotionSeller(EntityPlayer player, EntityInteractEvent event){
		System.out.println("Interacting with Potion Seller NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createPotionCurrency(traderInterface.inventoryCurrency, Item.getItemById(266));
		if (traderInterface.inventorySold.items.isEmpty())
			createPotionSold(traderInterface.inventorySold);
	}
	
	
	//////Private Helper Methods: Traders
	//////////WeaponsMerchant Market
	private static void createWeaponsCurrency(NpcMiscInventory inventoryCurrency, Item currency){
		inventoryCurrency.setInventorySlotContents(0, new ItemStack(currency));
		inventoryCurrency.setInventorySlotContents(1, new ItemStack(currency,3));
		inventoryCurrency.setInventorySlotContents(2, new ItemStack(currency,9));
		inventoryCurrency.setInventorySlotContents(3, new ItemStack(currency,27));
		inventoryCurrency.setInventorySlotContents(4, new ItemStack(currency,81));
	}
	
	private static void createWeaponsSold(NpcMiscInventory inventorySold){
		inventorySold.setInventorySlotContents(0, new ItemStack(Item.getItemById(268)));
		inventorySold.setInventorySlotContents(1, new ItemStack(Item.getItemById(267)));
		inventorySold.setInventorySlotContents(2, new ItemStack(CustomItems.swordBronze));
		inventorySold.setInventorySlotContents(3, new ItemStack(CustomItems.swordMithril));
		inventorySold.setInventorySlotContents(4, new ItemStack(CustomItems.swordEmerald));
	}
	
	//////////Blacksmith Market
	private static void createBlacksmithCurrency(NpcMiscInventory inventoryCurrency){
		//Exchanges for Swords
		inventoryCurrency.setInventorySlotContents(0, new ItemStack(Item.getItemById(268))); //wooden sword
		inventoryCurrency.setInventorySlotContents(18, new ItemStack(Item.getItemById(4420)));//water elemental
		inventoryCurrency.setInventorySlotContents(1, new ItemStack(Item.getItemById(268)));
		inventoryCurrency.setInventorySlotContents(19, new ItemStack(Item.getItemById(4421)));//fire elemental
		inventoryCurrency.setInventorySlotContents(2, new ItemStack(Item.getItemById(268)));
		inventoryCurrency.setInventorySlotContents(20, new ItemStack(Item.getItemById(4419)));//earth elemental
		inventoryCurrency.setInventorySlotContents(3, new ItemStack(Item.getItemById(268)));
		inventoryCurrency.setInventorySlotContents(21, new ItemStack(Item.getItemById(4422)));//air elemental
	}
		
	
	private static void createBlacksmithSold(NpcMiscInventory inventorySold){
		//Swords
		inventorySold.addItemStack(new ItemStack(CustomItems.swordFrost));
		inventorySold.addItemStack(new ItemStack(CustomItems.swordDemonic));
		inventorySold.addItemStack(new ItemStack(CustomItems.glaiveFrost));
		inventorySold.addItemStack(new ItemStack(CustomItems.glaiveDemonic));
	}
	
	//////////Potion Seller Market
	private static void createPotionCurrency(NpcMiscInventory inventoryCurrency, Item currency){
		inventoryCurrency.setInventorySlotContents(0, new ItemStack(currency,10));
//		inventoryCurrency.setInventorySlotContents(1, new ItemStack(currency,10));
//		inventoryCurrency.setInventorySlotContents(2, new ItemStack(currency,10));
//		inventoryCurrency.setInventorySlotContents(3, new ItemStack(currency,10));
	}
	
	private static void createPotionSold(NpcMiscInventory inventorySold){
		ItemStack p = new ItemStack(Items.potionitem);
		p.setStackDisplayName("Teleportation Potion - Village");
		inventorySold.setInventorySlotContents(0, p);
	}
}
