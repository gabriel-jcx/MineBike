package org.ngs.bigx.minecraft.items;


import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.RegistryNamespaced;
import scala.collection.parallel.mutable.ParHashSetCombiner.AddingFlatHashTable;

public class MineBikeCustomItems
{
  public static final RegistryNamespaced itemRegistry = GameData.getItemRegistry();
  
  	public static HashMap<String, Item> itemMap;
  
	public static ArrayList<Item> createItems()
	{
		ArrayList<Item> returned = new ArrayList<Item>();
		try {
			returned.add(makeItem(OlReliable.class, "OlReliable", "customnpcs:fishing_rod"));
	    	for (EnumFishType fish: EnumFishType.values())
			{
				Item item = new ItemFish(fish.getHealAmount(), fish.getSaturationModifier(), false, fish.getWeight());
				item.setUnlocalizedName("ItemFish." + fish.getName());
				item.setTextureName("customnpcs:" + fish.getName());
				returned.add(item);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//add your item in this format 
		//returned.add(makeItem(myItem.class, "myItemName");
		
		itemMap = createMap(returned);
		
		return returned;
	}
	
	public static HashMap<String, Item> createMap(ArrayList<Item> items)
	{
		HashMap<String, Item> map = new HashMap<String, Item>();
		
		for (Item item : items)
		{
			map.put(item.getUnlocalizedName(), item);
		}
		
		return map;
	}
	
	public static Item makeItem(Class itemClass, String name, String ResourceLocation) throws InstantiationException, IllegalAccessException
	{
		return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.tabMisc).setTextureName(ResourceLocation);
	}
}