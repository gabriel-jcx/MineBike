package org.ngs.bigx.minecraft.quests.custom;

import net.minecraft.item.ItemStack;

public class NewItemStack {

	private ItemStack myItemStack;
	private String myName;
	
	public NewItemStack(ItemStack stack, String name)
	{
		myItemStack = stack;
		myName = name;
	}
	
	public ItemStack getStack()
	{
		return myItemStack;
	}
	
	public String getName()
	{
		return myName;
	}
	
}
