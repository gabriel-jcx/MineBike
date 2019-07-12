package org.ngs.bigx.minecraft.items;

import java.util.ArrayList;

import org.ngs.bigx.minecraft.entity.item.JahCoin;
import org.ngs.bigx.minecraft.items.EnumFishType;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import scala.collection.parallel.mutable.ParHashSetCombiner.AddingFlatHashTable;

public class MineBikeCustomItems
{
  public static final RegistryNamespaced itemRegistry = GameData.getItemRegistry();
	public static ArrayList<Item> createItems()
	{
		ArrayList<Item> returned = new ArrayList<Item>();
		
		try {
			returned.add(makeItem(OlReliable.class, "OlReliable", "customnpcs:fishing_rod"));
	    	for (EnumFishType fish: EnumFishType.values())
        {
          Item item = new ItemFish(fish.getHealAmount(), fish.getSaturationModifier(), false);
          item.setUnlocalizedName("ItemFish." + fish.getName());
          item.setTextureName("customnpcs:" + fish.getName());
          returned.add(item);
  //				GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
  //				Item.itemRegistry.addObject(id, item.getUnlocalizedName().substring(5), item);
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
		
		return returned;
	}
	
	public static Item makeItem(Class itemClass, String name, String ResourceLocation) throws InstantiationException, IllegalAccessException
	{
		return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.tabMisc).setTextureName(ResourceLocation);
	}
}
