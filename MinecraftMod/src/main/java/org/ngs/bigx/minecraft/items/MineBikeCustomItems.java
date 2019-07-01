package org.ngs.bigx.minecraft.items;

import java.util.ArrayList;

import org.ngs.bigx.minecraft.entity.item.JahCoin;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.RegistryNamespaced;

public class MineBikeCustomItems 
{
	public static final RegistryNamespaced itemRegistry = GameData.getItemRegistry();
	public static ArrayList<Item> createItems()
	{
		ArrayList<Item> returned = new ArrayList<Item>();
		
		try {
			returned.add(makeItem(JahCoin.class, "JahCoin", "customnpcs:jahcoin"));
			returned.add(makeItem(OlReliable.class, "OlReliable", "customnpcs:fishing_rod"));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//add your item in this format 
		//returned.add(makeItem(myItem.class, "myItemName");
		
		return returned;
	}
	
	public static Item makeItem(Class itemClass, String name, String ResourceLocation) throws InstantiationException, IllegalAccessException
	{
		return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.tabMisc).setTextureName(ResourceLocation);
	}
}
