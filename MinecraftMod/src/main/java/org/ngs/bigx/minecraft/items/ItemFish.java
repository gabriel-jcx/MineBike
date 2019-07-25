package org.ngs.bigx.minecraft.items;
import java.util.List;


import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;

/**
 * CREDIT TO NightKosh FOR THE CUSTOM FISH AND CODE
 */
public class ItemFish extends ItemFood
{
	//Stores what tier the fish is
public int rarity;
	
public ItemFish(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_, int rarity) {
		super(p_i45339_1_, p_i45339_2_, p_i45339_3_);
		this.rarity = rarity;
		// TODO Auto-generated constructor stub
	}

//Returns what tier of fish it is
public int getRarity()
{
	return rarity;
}

@Override
public int func_150905_g(ItemStack stack) {
	super.func_150905_g(stack);
    return EnumFishType.values()[stack.getItemDamage()].getHealAmount();
}

@Override
public float func_150906_h(ItemStack stack) {
	super.func_150906_h(stack);
    return EnumFishType.values()[stack.getItemDamage()].getSaturationModifier();
}

@Override
//Triggers what happens with each of the Custom fish
protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
    EnumFishType fishType = EnumFishType.values()[stack.getItemDamage()];
	String name = stack.getDisplayName();
    System.out.println(stack.getDisplayName());
    System.out.println(stack.getItemDamage());
    System.out.println(fishType);
    
    if(name.equals("Cursed Koi"))
    {
    	player.addPotionEffect(new PotionEffect(19, 200, 1));
        player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Blaze Pike"))
    {
    	player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
        player.setFire(5);
    }
    else if(name.equals("Bonefish"))
    {
    	player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Spookyfish"))
    {
    	player.addPotionEffect(new PotionEffect(15, 300, 2));
        player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Withered Crucian"))
    {
    	player.addPotionEffect(new PotionEffect(20, 200, 1));
        player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Cave Trout"))
    {
    	player.addPotionEffect(new PotionEffect(11, 200, 1));
    }
    else if(name.equals("Mandarinfish"))
    {
    	player.addExperience(20);
    }

    super.onFoodEaten(stack, worldIn, player);
}

}