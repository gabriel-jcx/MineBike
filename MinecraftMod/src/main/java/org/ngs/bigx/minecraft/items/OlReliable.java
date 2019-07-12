package org.ngs.bigx.minecraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class OlReliable extends ItemFishingRod
{
	 public static final OlReliable Ol_Reliable = (OlReliable)Item.itemRegistry.getObject("fighing_rod");
	 private CustomFishHook fishHook = null;
	 
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;
	private static final String __OBFID = "CL_00000034";
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister Icon)
    {
        this.itemIcon = Icon.registerIcon(this.getIconString() + "_uncast");
        this.theIcon = Icon.registerIcon(this.getIconString() + "_cast");
    }
	
	 @Override
	    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) 
	 	{
		 System.out.println(player.getCurrentEquippedItem().getItem());
		 if (player.fishEntity != null)
	        {
			 int i = player.fishEntity.func_146034_e();
	            item.damageItem(i, player);
	            player.swingItem();
	        }
	        else
	        {
	        	 world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	        	 
	        	 if (!world.isRemote)
	             {
	        		world.spawnEntityInWorld(new CustomFishHook(world, player));
	             }
	        	 
	        	 player.swingItem();
	        }
		 
		 /*
         world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

         if (fishHook == null)
         {
             if (!world.isRemote)
             {
             	fishHook = new CustomFishHook(world, player);
                 world.spawnEntityInWorld(fishHook);
             }
         }
         else
         {
        	 fishHook.func_146034_e();
        	 fishHook.isDead = true;
        	 fishHook = null;
        	 player.fishEntity.func_146034_e();
        }


         player.swingItem();

*/
		 return item;
	 	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon func_94597_g()
    {
        return this.theIcon;
    }
}
