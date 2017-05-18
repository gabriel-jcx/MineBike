package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcLocations;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestTaskTutorial extends QuestTask implements IQuestEventNpcInteraction {
	private EntityCustomNpc npc;
	
	public QuestTaskTutorial(QuestManager questManager, EntityPlayer p, EntityCustomNpc n) {
		this(questManager, p, n, true);
	}
	
	
	public QuestTaskTutorial(QuestManager questManager, EntityPlayer p, EntityCustomNpc n, boolean required) {
		super(questManager, required);
		player = p;
		npc = n;
		isRequired = required;
	}
	
	@Override
	public void CheckComplete() {
	}
	
	public boolean isAllItemPossessed()
	{
		boolean allItemPossessed = true;
		
		// CHECK IF THE PLAYER HAS THE FOLLOWING ITEMS
		if (!player.inventory.hasItemStack(new ItemStack(Item.getItemFromBlock(Block.getBlockFromName("TORCH"))))) {
			allItemPossessed &= false;
		}
		if(!player.inventory.hasItem(Items.potionitem)){
			allItemPossessed &= false;
		}
		if(!player.inventory.hasItem(Items.gold_ingot)){
			allItemPossessed &= false;
		}
		if(!player.inventory.hasItem(Items.wooden_sword)){
			allItemPossessed &= false;
		}
		
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

	@Override
	public void run() { }

	@Override
	public void init() { }


	@Override
	public void unregisterEvents() { 
		synchronized (questManager) {
			QuestEventHandler.unregisterQuestEventCheckComplete(this);
		}
	}

	@Override
	public void registerEvents() {
		synchronized (questManager) {
			QuestEventHandler.registerQuestEventCheckComplete(this);
		}
	}

	@Override
	public void onCheckCompleteEvent() {
		CheckComplete();
	}


	@Override
	public void onNpcInteraction(EntityInteractEvent e) {
		if(BiGXEventTriggers.checkEntityInArea(e.target, NpcLocations.scientists, NpcLocations.scientists.addVector(1, 1, 1)))
		{
			if(isAllItemPossessed())
			{
				this.completed = true;
			}
		}
	}
}
