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
import net.minecraft.init.Items;
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
		completed = true;//completed = isAllItemPossessed();
	}
	
	public boolean isAllItemPossessed()
	{
		System.out.println("Checking to see if player has all the items...");
		boolean allItemPossessed = true;
		
		// CHECK IF THE PLAYER HAS THE FOLLOWING ITEMS
		if (!player.inventory.hasItemStack(new ItemStack(Item.getItemById(50)))){//Item.getItemFromBlock(Block.getBlockFromName("TORCH"))))) {
			System.out.print("\nPlayer Doesn't have Torch");
			allItemPossessed &= false;
		}
		if(!player.inventory.hasItem(Items.potionitem)){
			System.out.print("\nPlayer Doesn't have Potion");
			allItemPossessed &= false;
		}
		if(!player.inventory.hasItem(Items.gold_ingot)){
			System.out.print("\nPlayer Doesn't have Gold");
			allItemPossessed &= false;
		}
		if(!player.inventory.hasItem(Items.wooden_sword)){
			System.out.print("\nPlayer Doesn't have Sword");
			allItemPossessed &= false;
		}
		
		System.out.println("Does Player have all the items?: " + allItemPossessed);
		return allItemPossessed;
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
				if(player.worldObj.isRemote){
					handlePlayTimeOnClient();	
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
		if (BiGXEventTriggers.checkEntityInArea(target, NpcLocations.trainingBot.addVector(0, -1, 0), NpcLocations.trainingBot.addVector(1, 0, 1)))
			if (player.inventory.mainInventory[player.inventory.currentItem] != null)
				deductThiefHealth(player.inventory.mainInventory[player.inventory.currentItem].getItem());
			else
				deductThiefHealth(null);
		if (botHealth <= 0){
//			target.entityDropItem(new ItemStack(Item.getItemById(50)), 1);
			target.setDead();
			player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(50)));
		}
	}
	
	
	private void deductThiefHealth(Item itemOnHands)
	{
		int deduction = 1;
		if (itemOnHands != null && itemOnHands.getUnlocalizedName().equals("item.swordWood"))
				deduction = 2;
	
		botHealth -= deduction;
		
		GuiDamage.addDamageText(deduction, 255, 10, 10);
	}
}