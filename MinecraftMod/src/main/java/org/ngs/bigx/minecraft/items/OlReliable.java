package org.ngs.bigx.minecraft.items;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class OlReliable extends ItemFishingRod
{
	 public CustomFishHook fishHook = null;
	 
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;
	private static final String __OBFID = "CL_00000034";
	
	//Registers what the icon of the fishing rod looks like
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister Icon)
    {
        this.itemIcon = Icon.registerIcon(this.getIconString() + "_uncast");
        this.theIcon = Icon.registerIcon(this.getIconString() + "_cast");
    }
	
	//Spawns and retracts the customfishhook
	 @Override
	    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) 
	 	{
		 System.out.println(player.getCurrentEquippedItem());
		 if (player.fishEntity != null)
	        {
			 
			 //Stops the player from accidently right clicking and ending the fishing mechanic
				if(fishHook.handleHookRetraction() != 1)
				{
					//retracts hook
				 	int i = player.fishEntity.func_146034_e();
				 	item.damageItem(i, player);
				 	player.swingItem();
				 	player.removePotionEffect(2);
				} 	
	        }
	        else
	        {
	        	 world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	        	 
	        	 if (!world.isRemote)
	             {
	        		fishHook = new CustomFishHook(world, player);
	        		world.spawnEntityInWorld(fishHook);
	             }
	        	 
	        	 player.swingItem();
	        }
		 return item;
	 	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon func_94597_g()
    {
        return this.theIcon;
    }

}
