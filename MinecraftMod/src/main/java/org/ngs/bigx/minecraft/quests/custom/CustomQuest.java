package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.npcs.custom.jeff;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.custom.helpers.Utils;

import net.minecraftforge.event.entity.player.EntityInteractEvent;
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
	
	@Override
	public void entityInteractEvent(EntityInteractEvent event)
	{
//		System.out.println("sub class entity interact!");
//		completed = true;
//		complete();
		
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
		System.out.println(event.item.toString());
		System.out.println("sub class item pickup event!!!");
		System.out.println("Talk to jeff to turn the quest in now...");
		
		GuiMessageWindow.showMessage("Talk to jeff to turn the quest in now...");
		
		pickedUpItem = true;
	}
}
