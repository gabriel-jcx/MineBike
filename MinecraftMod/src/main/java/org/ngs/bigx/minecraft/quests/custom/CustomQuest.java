package org.ngs.bigx.minecraft.quests.custom;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.npcs.custom.jeff;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.custom.helpers.Utils;

//import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class CustomQuest extends CustomQuestAbstract 
{
	private boolean pickedUpItem;
	
	public CustomQuest()
	{
		super();
		progress = 0;
		name = "CustomQuest";
		completed = false;
		pickedUpItem = false;
		
		register();
	}
	
	
	@Override
	public void start()
	{
		progress = 0;
		super.start();
	}
	
	//@Override
	public void entityInteractEvent(PlayerInteractEvent.EntityInteract event)
	//public void Event(PlayerInteractEvent event)
	{
//		System.out.println("sub class entity interact!");
//		completed = true;
//		complete();
		//PlayerInteractEvent.EntityInteract event1 = event.EntityInteract;
		if (Utils.checkInArea(event, jeff.LOCATION))
		{
			if (pickedUpItem)
			{
				GuiMessageWindow.showMessage("Thank you for picking up the item.");
				progress = 2;
				super.complete();
			}
		}
	}
	
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		progress = 1;
		System.out.println(event.getItem().toString());
		System.out.println("sub class item pickup event!!!");
		System.out.println("Talk to jeff to turn the quest in now...");
		
		GuiMessageWindow.showMessage("Talk to jeff to turn the quest in now...");
		
		pickedUpItem = true;
	}


	@Override
	public void setDifficulty(Difficulty difficultyIn) {
		// TODO Auto-generated method stub
		
	}
}
