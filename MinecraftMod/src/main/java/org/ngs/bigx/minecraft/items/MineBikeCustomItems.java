package org.ngs.bigx.minecraft.items;


import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.registries.GameData;
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
			Item painting = new ItemCustomPainting(CustomPainting.class);
			painting.setTextureName("customnpcs:painting").setUnlocalizedName("CustomPainting");
			returned.add(painting);
	    	for (EnumFishType fish: EnumFishType.values())
			  {
          Item item = new ItemFish(fish.getHealAmount(), fish.getSaturationModifier(), false, fish.getWeight(), fish.getName());
          item.setUnlocalizedName("ItemFish." + fish.getName());
          item.setTextureName("customnpcs:" + fish.getName());
          returned.add(item);
			  }
      
      	returned.add(makeItem(OvercookedLettuce.class, "lettuce", "customnpcs:minebike/lettuce"));
        returned.add(makeItem(OvercookedHamburger.class, "hamburger", "customnpcs:minebike/hamburger"));
        returned.add(makeItem(OvercookedSandwich.class, "sandwich", "customnpcs:minebike/sandwich"));
        returned.add(makeItem(OvercookedHamburgerbun.class, "hamburgerbun", "customnpcs:minebike/hamburgerbun"));
        returned.add(makeItem(OvercookedSandwichbread.class, "sandwichbread", "customnpcs:minebike/sandwichbread"));
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
		//return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.MISC).setTextureName(ResourceLocation);
		return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.MISC).setUnlocalizedName(ResourceLocation);
	}
}