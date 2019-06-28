package org.ngs.bigx.minecraft.items;

import java.util.ArrayList;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MineBikeCustomItems
{
    public static ArrayList<Item> createItems()
    {
        ArrayList<Item> returned = new ArrayList<Item>();
        
        //this try catch is because the make Item method tries to cast an Object to an Item
//        try {
//            //
//        } catch (InstantiationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        
        //add your item in this format
        //returned.add(makeItem(myItem.class, "myItemName");
        
        return returned;
    }
    
    public static Item makeItem(Class itemClass, String name, String ResourceLocation) throws InstantiationException, IllegalAccessException
    {
        return ((Item)(itemClass.newInstance())).setUnlocalizedName(name).setFull3D().setCreativeTab(CreativeTabs.tabMisc).setTextureName(ResourceLocation);
    }
}