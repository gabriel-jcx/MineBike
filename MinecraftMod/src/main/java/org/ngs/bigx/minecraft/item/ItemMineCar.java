package org.ngs.bigx.minecraft.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMinecart;

public class ItemMineCar{

	public static void mainRegistry(){
		initializeItem();
		registerItem();
	}
	
	public static Item mineCar;
	
	public static void initializeItem(){
		mineCar = new Item().setUnlocalizedName("mineCar").setCreativeTab(CreativeTabs.tabTransport);
	}
	
	public static void registerItem(){
		GameRegistry.registerItem(mineCar, mineCar.getUnlocalizedName());
	}


}