package org.ngs.bigx.minecraft.items;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import scala.collection.parallel.mutable.ParHashSetCombiner.AddingFlatHashTable;

public class MineBikeCustomItems
{
    public static ArrayList<Item> createItems()
    {
        ArrayList<Item> returned = new ArrayList<Item>();
        
        //this try catch is because the make Item method tries to cast an Object to an Item
        //you have to uncomment this try catch to add your items, then add them inside the try
        
        try {
            //add items here with this format:
        	  //returned.add(makeItem(myItem.class, "myItemName", "myItemResourceLocation");
          //overcooked items
        	returned.add(makeItem(OvercookedLettuce.class, "lettuce", "customnpcs:minebike/lettuce"));
        	returned.add(makeItem(OvercookedHamburger.class, "hamburger", "customnpcs:minebike/hamburger"));
        	returned.add(makeItem(OvercookedSandwich.class, "sandwich", "customnpcs:minebike/sandwich"));
        	returned.add(makeItem(OvercookedHamburgerbun.class, "hamburgerbun", "customnpcs:minebike/hamburgerbun"));
        	returned.add(makeItem(OvercookedSandwichbread.class, "sandwichbread", "customnpcs:minebike/sandwichbread"));
          
          //fishing items
			    returned.add(makeItem(OlReliable.class, "OlReliable", "customnpcs:fishing_rod"));
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return returned;
    }
    
    public static Item makeItem(Class itemClass, String name, String ResourceLocation) throws InstantiationException, IllegalAccessException
    {
        return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.tabMisc).setTextureName(ResourceLocation);
    }
}
