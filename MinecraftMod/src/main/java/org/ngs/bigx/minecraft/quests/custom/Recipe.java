package org.ngs.bigx.minecraft.quests.custom;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Recipe {

	private Item orderType;
	private Item[] insideBread;
	
	public Recipe(Item type, Item[] items)
	{
		orderType = type;
		insideBread = items;
	}
	
	public Item getType()
	{
		return orderType;
	}
	
	public Item[] getInsides()
	{
		return insideBread;
	}
	
	public boolean canMake(TickEvent.PlayerTickEvent event)
	{
		int checks = insideBread.length + 4;
		InventoryPlayer inventory = event.player.inventory;
		
		for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack != null)
			{
				for(int j = 0; j < insideBread.length; j++)
				{
					if(stack.getItem().getUnlocalizedName().equals(insideBread[j].getUnlocalizedName()) || 
							stack.getItem().getUnlocalizedName().equals(orderType.getUnlocalizedName()))
					{
						checks--;
					}
				}
			}
		}
		if(checks==0)
		{
			System.out.println("TRUE BITCHES");
			return true;
		}
		System.out.println("this is returning false");
		return false;
	}
}
