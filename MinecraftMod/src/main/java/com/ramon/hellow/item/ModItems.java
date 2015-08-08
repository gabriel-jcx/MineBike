package com.ramon.hellow.item;
import com.ramon.hellow.Context;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.client.main.Main;
import net.minecraft.creativetab.CreativeTabs;

public final class ModItems {
	public static Item Bike;
	
	public static final void init(Context con){
		Bike = new Bike(con);
	}

}
