package org.ngs.bigx.minecraft.entity.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MysteriousKey extends Item {

	public MysteriousKey(int i)
	{
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
}
