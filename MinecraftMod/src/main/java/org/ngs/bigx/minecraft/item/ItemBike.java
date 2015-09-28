package org.ngs.bigx.minecraft.item;

import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Main;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBike extends Item {
	Context context;
	
	public static final double MAXBIKESPEED = 0.8;
	
	public ItemBike(Context con) {
		context = con;
		setUnlocalizedName("bike");
		setTextureName(Main.TEXTURE_PREFIX+":bike");
		GameRegistry.registerItem(this, "bike");
	}
	
	public ItemStack onItemRightClick(ItemStack i, World w, EntityPlayer p)
    {
		context.setSpeed((float) (Math.min(0.4,context.getSpeed()+.04)));
        return i;
    }
}
