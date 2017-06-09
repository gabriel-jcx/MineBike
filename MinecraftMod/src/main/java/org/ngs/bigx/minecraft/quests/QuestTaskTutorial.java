package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcLocations;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventAttack;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskTutorial extends QuestTask implements IQuestEventNpcInteraction {
	private EntityCustomNpc npc;
	private int botHealth;
	
	public QuestTaskTutorial(QuestManager questManager, EntityPlayer p, EntityCustomNpc n) {
		this(questManager, p, n, true);
	}
	
	
	public QuestTaskTutorial(QuestManager questManager, EntityPlayer p, EntityCustomNpc n, boolean required) {
		super(questManager, required);
		player = p;
		npc = n;
		isRequired = required;
		botHealth = 10;
	}
	
	@Override
	public void CheckComplete() {
		System.out.println("Checking Quest as Complete");
		completed = isAllItemPossessed();
//		try {
//			if (completed)
//				questManager.setActiveQuest(Quest.QUEST_ID_STRING_NONE);
//		} catch (QuestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static boolean checkItems(IInventory inventory, Item item)
	{
		boolean hasItems = false;
		for (int slot = 0; slot < inventory.getSizeInventory(); ++slot)
		{
			ItemStack itemstack = inventory.getStackInSlot(slot);
			if (itemstack != null && itemstack.getItem() == item)
			{
				hasItems = true;
				break;
			}
		}
		return hasItems;
	}
	
	public boolean isAllItemPossessed()
	{
		int count = 0;

//		boolean allItemPossessed = true;
		
		if(!checkItems(player.inventory, Item.getItemById(Block.getIdFromBlock(Blocks.torch))))
		{
			System.out.println("Player Doesn't have Torch");
			return false;
//			allItemPossessed &= false;
		}
		
		if(!checkItems(player.inventory, Item.getItemById(266)))
		{
			System.out.println("Player Doesn't have Gold");
			return false;
//			allItemPossessed &= false;
		}
		
		if(!checkItems(player.inventory, Item.getItemById(268)))
		{
			System.out.println("Player Doesn't have Sword");
			return false;
//			allItemPossessed &= false;
		}
		
		if(!checkItems(player.inventory, Item.getItemById(373)))
		{
			System.out.println("Player Doesn't have Potion");
			return false;
//			allItemPossessed &= false;
		}

//		System.out.println("tutorial doen["+allItemPossessed+"] count[" + (count++) + "]");
		
//		System.out.println("Does Player have all the items?: " + allItemPossessed);
		System.out.println("Player has all the required items!");
		return true;
//		return allItemPossessed;
	}

	@Override
	public String getTaskDescription() {
		return "COLLECT SOME ITEMS";
	}

	@Override
	public String getTaskName() {
		return "Tutorial";
	}

	public void handlePlayTimeOnClient()
	{	
	}


	@Override
	public void run()
	{
		System.out.println("[BiGX] Quest Tutorial Started");
		synchronized (questManager) {
			init();
			
			while(isActive)
			{
				try {
					questManager.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void init() { }


	@Override
	public void unregisterEvents() {
		if(!player.worldObj.isRemote){
			QuestEventHandler.unregisterQuestEventCheckComplete(this);
		}
	}
	
	@Override
	public void registerEvents() {
		if(!player.worldObj.isRemote)
		{
			QuestEventHandler.registerQuestEventCheckComplete(this);
		}
	}

	@Override
	public void onCheckCompleteEvent() {
		CheckComplete();
	}


	@Override
	public void onNpcInteraction(EntityInteractEvent e) {
		System.out.println("Interacting with NPC!!!");
		if(BiGXEventTriggers.checkEntityInArea(e.target, NpcLocations.scientists, NpcLocations.scientists.addVector(1, 1, 1)))
		{
			if(isAllItemPossessed())
			{
				this.completed = true;
			}
		}
	}
	
	public void hitEntity(EntityPlayer player, EntityLivingBase target){
		if (BiGXEventTriggers.checkEntityInArea(target, NpcLocations.trainingBot.addVector(0, -1, 0), NpcLocations.trainingBot.addVector(1, 0, 1))){
			if (player.inventory.mainInventory[player.inventory.currentItem] != null)
				deductThiefHealth(player.inventory.mainInventory[player.inventory.currentItem].getItem(), player.worldObj.isRemote);
			else
				deductThiefHealth(null, player.worldObj.isRemote);

			if (botHealth <= 0){
				if (player.worldObj.isRemote)
					GuiMessageWindow.showMessage("You got the torch!");
				player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(50)));
				botHealth = 10;
			}
		}
	}
	
	
	private void deductThiefHealth(Item itemOnHands, boolean isRemote)
	{
		int deduction = 1;
		if (itemOnHands != null && itemOnHands.getUnlocalizedName().equals("item.swordWood"))
				deduction = 2;
	 
		botHealth -= deduction;
		
		if (isRemote)
			GuiDamage.addDamageText(deduction, 255, 10, 10);
	}
}
