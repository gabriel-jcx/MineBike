package org.ngs.bigx.minecraft.npcs;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.gui.GuiChapter;
import org.ngs.bigx.minecraft.client.gui.GuiMonsterAppears;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistException;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiChasingQuest;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiChasingQuestLevelSlotItem;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.custom.CustomNPCAbstract;
import org.ngs.bigx.minecraft.npcs.custom.CustomNPCStorage;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestEventHandler;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskTalk;
import org.ngs.bigx.minecraft.quests.QuestTaskTutorial;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.client.ClientEventHandler;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;


public class NpcEvents {

	public static final String[] skillNames = { "Skill - Boost Speed","Skill - Boost Damage","Skill - Boost Mining"};
	public static int botHealth = 10;
	
	public enum villagerEnum {guard1, guard2, sergeantWeirdo, wanderer, doctor, farmer, cook, marketPlaceAd, thiefInCage, jeff };
	
	public NpcEvents() {
	}
	
	//NPC Interactions
	public 	static void InteractWithNPC(EntityPlayer player, EntityInteractEvent event){
//		System.out.println(event.target.posX + " " + event.target.posY + " " + event.target.posZ);
		if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.dad, NpcLocations.dad.addVector(1, 1, 1))) //checks to see if NPC is Dad
			InteractWithFather(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.weaponsMerchant, NpcLocations.weaponsMerchant.addVector(1, 1, 1)))  //checks to see if NPC is Weapons Merch
			InteractWithWeaponsMerchant(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.blacksmith, NpcLocations.blacksmith.addVector(1, 1, 1)))  //checks to see if NPC is Blacksmith
			InteractWithBlacksmith(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.skillSeller, NpcLocations.skillSeller.addVector(1, 1, 1)))  //checks to see if NPC is PotionSeller
			InteractWithSkillSeller(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.trader, NpcLocations.trader.addVector(1, 1, 1)))  //checks to see if NPC is Trader
			InteractWithTrader(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.trainingBot.addVector(0, -1, 0), NpcLocations.trainingBot.addVector(1, 0, 1)))  //checks to see if NPC is TrainingBot
			InteractWithTrainingBot(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.officer.addVector(0, -1, 0), NpcLocations.officer.addVector(1, 0, 1)))  //checks to see if NPC is Police Officer
			InteractWithOfficer(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.tutorialExit.addVector(0, -1, 0), NpcLocations.tutorialExit.addVector(1, 0, 1)))
			InteractWithTeleportExit(player, event);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.tutorialGuy.addVector(-5, -5, -5), NpcLocations.tutorialGuy.addVector(5, 5, 5)))
			InteractWithTutorialGuy(player, event);
		

		
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.guard1.addVector(-5, -5, -5), NpcLocations.guard1.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.guard1);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.guard2.addVector(-5, -5, -5), NpcLocations.guard2.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.guard2);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.sergeantWeirdo.addVector(-5, -5, -5), NpcLocations.sergeantWeirdo.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.sergeantWeirdo);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.wanderer.addVector(-5, -5, -5), NpcLocations.wanderer.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.wanderer);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.doctor.addVector(-5, -5, -5), NpcLocations.doctor.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.doctor);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.farmer.addVector(-5, -5, -5), NpcLocations.farmer.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.farmer);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.cook.addVector(-5, -5, -5), NpcLocations.cook.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.cook);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.marketPlaceAd.addVector(-5, -5, -5), NpcLocations.marketPlaceAd.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.marketPlaceAd);
		else if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.thiefInCage.addVector(-5, -5, -5), NpcLocations.thiefInCage.addVector(5, 5, 5)))
			InteractWithVillagers(villagerEnum.thiefInCage);
		
		//checking the custom NPCs for proximity and then interacting with them
		for(CustomNPCAbstract npc : CustomNPCStorage.customNPCs)
		{
			if (BiGXEventTriggers.checkEntityInArea(event.target, npc.getLocation().addVector(-5, -5, -5), npc.getLocation().addVector(5, 5, 5)))
				npc.onInteraction();
		}
		
		if(player.worldObj.provider.dimensionId == 105)
		{
			if (BiGXEventTriggers.checkEntityInArea(event.target, NpcLocations.baddestGuy.addVector(-3, -3, -3), NpcLocations.baddestGuy.addVector(3, 3, 3)))
			{
				InteractWithDungeonBoss(player, event);
			}
			
			System.out.println("Interaction with Ifrit dim[" + player.worldObj.provider.dimensionId + "]");
		}
	}
	
	private static void InteractWithVillagers(villagerEnum villagerEnum){
		System.out.println("Interacting with Villagers...");
		
		String text = "Hello! How are you?";
		
		switch(villagerEnum)
		{
		case guard1:
			text = "The cave is on the northern\nmountain. Go north!";
			break;
		case guard2:
			text = "There are monsters out \nthere. Be careful.";
			break;
		case cook:
			text = "Watsup dude!";
			break;
		case doctor:
			text = "So sad... Recently there are monsters\neverywhere. They hurt people...";
			break;
		case farmer:
			text = "Is the harvest time\napproaching...?";
			break;
		case marketPlaceAd:
			text = "Oh Market Place!!! \nI want better swords!!!!";
			break;
		case sergeantWeirdo:
			text = "Shhh... Monsters are incoming";
			break;
		case thiefInCage:
			text = "Grrrrrr..... This item is \nuseless now... You take it.";
			break;
		case wanderer:
			text = "Wow... I've already seen\nten monsters today...";
			break;
		case jeff:
			text = "My name is jeff.";
			break;
		default:
			text = "Hello! How are you?";
			break;
		}
		
		GuiMessageWindow.showMessage(text);
	}
	
	private static void InteractWithTeleportExit(EntityPlayer player, EntityInteractEvent event) {
		// TODO Add confirmation GUI? "Want to try the tutorial again?"
		if (player.worldObj.isRemote)
			QuestTeleporter.teleport(player, 0, 91, 78, 243);
	}
	
	private static void InteractWithFather(EntityPlayer player, EntityInteractEvent event){
		if (!player.worldObj.isRemote)
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.fatherMsg);
		
		try {
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
			Quest quest;
			
			if(player.worldObj.isRemote)
			{
				System.out.println("InteractWithFather Quest Generation: CLIENT");
				
				if(BiGX.instance().clientContext.getQuestManager().getActiveQuest() == null) {
					quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, BiGXTextBoxDialogue.questChase1Title, BiGXTextBoxDialogue.questChase1Description, BiGX.instance().clientContext.getQuestManager());
				}
				else {
					if(BiGX.instance().clientContext.getQuestManager().getAvailableQuestList().get(Quest.QUEST_ID_STRING_CHASE_REG) != null)
						quest = BiGX.instance().clientContext.getQuestManager().getActiveQuest();
					else
						quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, BiGXTextBoxDialogue.questChase1Title, BiGXTextBoxDialogue.questChase1Description, BiGX.instance().clientContext.getQuestManager());
				}
				
				QuestEventHandler.unregisterAllQuestEventRewardSession();
				QuestTaskChasing questTaskChasing = new QuestTaskChasing(new LevelSystem(), BiGX.instance().clientContext.getQuestManager(), player, ws, 1, 4);
				QuestEventHandler.registerQuestEventRewardSession(questTaskChasing);
				quest.addTasks(questTaskChasing);
				if(BiGX.instance().clientContext.getQuestManager().addAvailableQuestList(quest))
					BiGX.instance().clientContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_CHASE_REG);
				
				// Unlock the next chapter and open Gui Chapter
				if(GuiChapter.getChapterNumber() == 1)
				{
					GuiChapter.proceedToNextChapter();
					org.ngs.bigx.minecraft.client.ClientEventHandler.flagOpenChapterGui = true;
				}
			}
			else
			{
				System.out.println("InteractWithFather Quest Generation: SERVER");
				
				if(BiGX.instance().serverContext.getQuestManager().getActiveQuest() == null)
				{
					quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, BiGXTextBoxDialogue.questChase1Title, BiGXTextBoxDialogue.questChase1Description, BiGX.instance().serverContext.getQuestManager());
				}
				else
				{
					if(BiGX.instance().serverContext.getQuestManager().getAvailableQuestList().get(Quest.QUEST_ID_STRING_CHASE_REG) != null)
						quest = BiGX.instance().serverContext.getQuestManager().getActiveQuest();
					else
						quest = new Quest(Quest.QUEST_ID_STRING_CHASE_REG, BiGXTextBoxDialogue.questChase1Title, BiGXTextBoxDialogue.questChase1Description, BiGX.instance().serverContext.getQuestManager());
				}

				QuestEventHandler.unregisterAllQuestEventRewardSession();
				QuestTaskChasing questTaskChasing = new QuestTaskChasing(new LevelSystem(), BiGX.instance().serverContext.getQuestManager(), player, ws, 1, 4);
				QuestEventHandler.registerQuestEventRewardSession(questTaskChasing);
				quest.addTasks(questTaskChasing);
				if(BiGX.instance().serverContext.getQuestManager().addAvailableQuestList(quest))
					BiGX.instance().serverContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_CHASE_REG);
			}
		} catch (QuestException e) {
			e.printStackTrace();
		}
	}
	
	private static void InteractWithDungeonBoss(EntityPlayer player, EntityInteractEvent event) {
		if (!player.worldObj.isRemote) 
		{
			
			// This gets triggered when the screen closes
			System.out.println("[BiGX] Start Boss Fight And Chase Quest");									
			Quest quest = BiGX.instance().serverContext.getQuestManager().getAvailableQuestList().get(Quest.QUEST_ID_STRING_FIGHT_CHASE);
			
			try {
				BiGX.instance().serverContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_FIGHT_CHASE);
				BiGX.instance().clientContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_FIGHT_CHASE);
				((QuestTaskFightAndChasing)quest.getCurrentQuestTask()).isBoss = true;
				int posy = ((int) player.posY == player.posY)?(int) player.posY:((int) player.posY)+1;
				((QuestTaskFightAndChasing)quest.getCurrentQuestTask()).setPreviousLocationBeforeTheQuest(105, (int)player.posX, posy, (int)player.posZ);
				((QuestTaskFightAndChasing)quest.getCurrentQuestTask()).handleQuestStart();
			} catch (QuestException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void InteractWithOfficer(EntityPlayer player, EntityInteractEvent event){
		System.out.println("Interacting with Officer...");
		GuiMessageWindow.showMessage("We're kind of busy here.\nCome back later.");
		//Giving player teleportation potion
//		boolean gavePotion = BiGXEventTriggers.givePlayerPotion(player, "Teleportation Potion - Quest", 
//				BiGXTextBoxDialogue.officerRequestHelp);
//		if (!player.worldObj.isRemote && gavePotion)
//			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.officerPotionHelp);
	}
	
	private static void InteractWithTrainingBot(EntityPlayer player, EntityInteractEvent event){
		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.instructionsAttackNPC);
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
	
	private static void InteractWithSkillSeller(EntityPlayer player, EntityInteractEvent event){
		System.out.println("Interacting with Skill Seller NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createSkillCurrency(traderInterface.inventoryCurrency, Item.getItemById(266));
		if (traderInterface.inventorySold.items.isEmpty())
			createSkillSold(traderInterface.inventorySold);
	}
	
	private static void InteractWithTrader(EntityPlayer player, EntityInteractEvent event){
		System.out.println("Interacting with Trader NPC");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;
		npc.advanced.setRole(1);
		RoleTrader traderInterface = (RoleTrader) npc.roleInterface;
		
		if (traderInterface.inventoryCurrency.items.isEmpty())
			createTraderCurrency(traderInterface.inventoryCurrency);
		if (traderInterface.inventorySold.items.isEmpty())
			createTraderSold(traderInterface.inventorySold);
	}
	
	private static void InteractWithTutorialGuy(EntityPlayer player, EntityInteractEvent event) {
		System.out.println("Interact with Tutorial Guy");
		EntityCustomNpc npc = (EntityCustomNpc) event.target;

		// Unlock the next chapter and open Gui Chapter
		if(GuiChapter.getChapterNumber() == 2)
		{
			GuiChapter.proceedToNextChapter();
		}
		
		// TODO add confirmation prompt
		if (player.worldObj.isRemote) {
			QuestTeleporter.teleport(player, 102, 512, 65, 0);
		}
	}
	
	//////Private Helper Methods: Traders
	//////////WeaponsMerchant Market
	private static void createWeaponsCurrency(NpcMiscInventory inventoryCurrency, Item currency){
		inventoryCurrency.setInventorySlotContents(0, new ItemStack(currency));
		inventoryCurrency.setInventorySlotContents(1, new ItemStack(currency,9));
		inventoryCurrency.setInventorySlotContents(2, new ItemStack(currency,27));
		inventoryCurrency.setInventorySlotContents(3, new ItemStack(currency,81));
//		inventoryCurrency.setInventorySlotContents(4, new ItemStack(currency,81));
	}
	
	private static void createWeaponsSold(NpcMiscInventory inventorySold){
		inventorySold.setInventorySlotContents(0, new ItemStack(Item.getItemById(268)));
		inventorySold.setInventorySlotContents(1, new ItemStack(Item.getItemById(267)));
		inventorySold.setInventorySlotContents(2, new ItemStack(CustomItems.swordBronze));
		inventorySold.setInventorySlotContents(3, new ItemStack(CustomItems.swordMithril));
//		inventorySold.setInventorySlotContents(4, new ItemStack(CustomItems.swordEmerald));
	}
	
	//////////Blacksmith Market
	private static void createBlacksmithCurrency(NpcMiscInventory inventoryCurrency){
		//Exchanges for Swords
//		inventoryCurrency.setInventorySlotContents(0, new ItemStack(Item.getItemById(268))); //wooden sword
//		inventoryCurrency.setInventorySlotContents(18, new ItemStack(Item.getItemById(4420)));//water elemental
//		inventoryCurrency.setInventorySlotContents(1, new ItemStack(Item.getItemById(268)));
//		inventoryCurrency.setInventorySlotContents(19, new ItemStack(Item.getItemById(4421)));//fire elemental
//		inventoryCurrency.setInventorySlotContents(2, new ItemStack(Item.getItemById(268)));
//		inventoryCurrency.setInventorySlotContents(20, new ItemStack(Item.getItemById(4419)));//earth elemental
//		inventoryCurrency.setInventorySlotContents(3, new ItemStack(Item.getItemById(268)));
//		inventoryCurrency.setInventorySlotContents(21, new ItemStack(Item.getItemById(4422)));//air elemental
	}
		
	
	private static void createBlacksmithSold(NpcMiscInventory inventorySold){
		//Swords
//		inventorySold.addItemStack(new ItemStack(CustomItems.swordFrost));
//		inventorySold.addItemStack(new ItemStack(CustomItems.swordDemonic));
//		inventorySold.addItemStack(new ItemStack(CustomItems.glaiveFrost));
//		inventorySold.addItemStack(new ItemStack(CustomItems.glaiveDemonic));
	}
	
	//////////Skill Seller Market
	private static void createSkillCurrency(NpcMiscInventory inventoryCurrency, Item currency){
		int i = 0;
		for (String name : skillNames){
			inventoryCurrency.setInventorySlotContents(i++, new ItemStack(currency,15));
		}
	}
	
	private static void createSkillSold(NpcMiscInventory inventorySold){
		int i = 0;
		for (String name : skillNames){
			ItemStack p = new ItemStack(Items.enchanted_book);
			p.setStackDisplayName(name);
			inventorySold.setInventorySlotContents(i++, p);
		}
	}
	
	
	//////////Trader Market
	private static void createTraderCurrency(NpcMiscInventory inventoryCurrency){
		inventoryCurrency.setInventorySlotContents(0,  new ItemStack(Item.getItemById(266),1));
		inventoryCurrency.setInventorySlotContents(1,  new ItemStack(Item.getItemById(3),32)); //Dirt block
		inventoryCurrency.setInventorySlotContents(2,  new ItemStack(Item.getItemById(4),8)); //Cobblestone Block
		inventoryCurrency.setInventorySlotContents(3,  new ItemStack(Item.getItemById(5),8)); //Oak Wood Plank	
		inventoryCurrency.setInventorySlotContents(4,  new ItemStack(Items.wooden_sword)); //Wood Sword
		inventoryCurrency.setInventorySlotContents(5,  new ItemStack(Items.iron_sword)); //Iron Sword
		inventoryCurrency.setInventorySlotContents(6,  new ItemStack(CustomItems.swordBronze)); //Bronze Sword
		inventoryCurrency.setInventorySlotContents(7,  new ItemStack(CustomItems.swordMithril)); //Mithril Sword
		inventoryCurrency.setInventorySlotContents(8,  new ItemStack(Items.feather,6)); //Feather
		inventoryCurrency.setInventorySlotContents(9,  new ItemStack(Items.blaze_powder,6)); //Burn Element thing
		inventoryCurrency.setInventorySlotContents(10, new ItemStack(Blocks.cactus,12)); //Burn Element thing
		inventoryCurrency.setInventorySlotContents(11, new ItemStack(Item.getItemById(37),12));
		inventoryCurrency.setInventorySlotContents(12, new ItemStack(Item.getItemById(295),12));
		inventoryCurrency.setInventorySlotContents(13, new ItemStack(Item.getItemById(265),2));
		inventoryCurrency.setInventorySlotContents(14, new ItemStack(Item.getItemById(46),2));
		inventoryCurrency.setInventorySlotContents(15, new ItemStack(Item.getItemById(366),4));
		inventoryCurrency.setInventorySlotContents(16, new ItemStack(Item.getItemById(260),6));
	}
	
	private static void createTraderSold(NpcMiscInventory inventorySold){
//		inventorySold.setInventorySlotContents(0, new ItemStack(Item.getItemById(3),32)); //Dirt block
//		inventorySold.setInventorySlotContents(1, new ItemStack(Item.getItemById(4), 8)); //Cobblestone Block
//		inventorySold.setInventorySlotContents(2, new ItemStack(Item.getItemById(5), 8)); //Oak Wood Plank
//		inventorySold.setInventorySlotContents(3, new ItemStack(Item.getItemById(266),1)); //Gold Ingots
//		inventorySold.setInventorySlotContents(4, new ItemStack(Item.getItemById(266),1));
//		inventorySold.setInventorySlotContents(5, new ItemStack(Item.getItemById(266),1));
//		inventorySold.setInventorySlotContents(6, new ItemStack(Item.getItemById(266),1));
//		inventorySold.setInventorySlotContents(7, new ItemStack(Item.getItemById(266),3));
//		inventorySold.setInventorySlotContents(8, new ItemStack(Item.getItemById(266),9));
//		inventorySold.setInventorySlotContents(9, new ItemStack(Item.getItemById(266),27));
//		inventorySold.setInventorySlotContents(10, new ItemStack(Item.getItemById(266), 81));
//		inventorySold.setInventorySlotContents(11, new ItemStack(Item.getItemById(266),1));
//		inventorySold.setInventorySlotContents(12, new ItemStack(Item.getItemById(266),1));

		inventorySold.setInventorySlotContents(0, new ItemStack(Item.getItemById(5), 8)); //Oak Wood Plank
		inventorySold.setInventorySlotContents(1, new ItemStack(Item.getItemById(266),1)); //Gold Ingots
		inventorySold.setInventorySlotContents(2, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(3, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(4, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(5, new ItemStack(Item.getItemById(266),9));
		inventorySold.setInventorySlotContents(6, new ItemStack(Item.getItemById(266),27));
		inventorySold.setInventorySlotContents(7, new ItemStack(Item.getItemById(266), 81));
		inventorySold.setInventorySlotContents(8, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(9, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(10, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(11, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(12, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(13, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(14, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(15, new ItemStack(Item.getItemById(266),1));
		inventorySold.setInventorySlotContents(16, new ItemStack(Item.getItemById(266),1));
	}

}
