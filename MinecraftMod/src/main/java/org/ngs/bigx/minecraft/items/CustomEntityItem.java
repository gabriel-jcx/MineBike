package org.ngs.bigx.minecraft.items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CustomEntityItem extends EntityItem{

	//Makes the custom fish immune to fire
	public CustomEntityItem(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_, double p_i1710_6_,
			ItemStack p_i1710_8_) {
		super(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_, p_i1710_8_);
		this.isImmuneToFire = true;
		// TODO Auto-generated constructor stub
	}

}
