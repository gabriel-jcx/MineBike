package com.ramon.hellow.item;

import com.ramon.hellow.CommonEventHandler;
import com.ramon.hellow.Context;
import com.ramon.hellow.Main;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBike extends Item {
	
	Context context;
	
	public ItemBike(Context con) {
		context = con;
		setUnlocalizedName("bike");
		setCreativeTab(CreativeTabs.tabMisc);
		setTextureName(Main.TEXTURE_PREFIX+":bike");
		GameRegistry.registerItem(this, "bike");
	}
	
	public ItemStack onItemRightClick(ItemStack i, World w, EntityPlayer p)
    {
		context.setSpeed((float) (Math.min(0.4,context.getSpeed()+.04)));
        return i;
    }
}
