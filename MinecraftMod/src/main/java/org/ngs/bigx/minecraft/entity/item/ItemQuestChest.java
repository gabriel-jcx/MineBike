package org.ngs.bigx.minecraft.entity.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemQuestChest extends ItemBlock {

	public ItemQuestChest(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(false);
	}
	
	@Override
	public int getMetadata(int i)
	{
		return 0;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "questChest";
	}
}
