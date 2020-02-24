package org.ngs.bigx.minecraft.items;


import java.rmi.registry.Registry;
import java.time.Clock;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.ngs.bigx.minecraft.BiGX;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class OlReliable extends ItemFishingRod
{

	public CustomFishHook fishHook = null;
	public static Clock clock;
	public static boolean clockTimer = false;
	//Stores the time when the minigame is started, used to track how much time has passed
	public static long lastTime = 0;
	//How Long the Minigame will be in seconds
	int time = 3;
	static //Tracks how long in seconds it's been since lastTime
	int timeElapsed = 0;
	 
	@SideOnly(Side.CLIENT)
//	private IIcon theIcon;
	private static final String __OBFID = "CL_00000034";
	
//	Registers what the icon of the fishing rod looks like

	// TODO: figure out if this function is actually used since couldn't find usage on the function
//	@SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister Icon)
//    {
//        this.itemIcon = Icon.registerIcon(this.getIconString() + "_uncast");
//		this.theIcon = Icon.registerIcon(this.getIconString() + "_cast");
//    }
//	@SubscribeEvent
//	public void registerIcons(RegistryEvent.)
//	//Spawns and retracts the customfishhook
	 @Override
	    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
	 	{
		 System.out.println(player.getHeldEquipment());
		 ItemStack itemStack = player.getHeldItem(handIn);
		 if (player.fishEntity != null)
	        {

			 //Stops the player from accidently right clicking and ending the fishing mechanic
				if(fishHook.handleHookRetraction() != 1)
				{
					//retracts hook
				 	int i = player.fishEntity.handleHookRetraction();
					itemStack.damageItem(i, player);

				 	player.swingArm(handIn);
				 	player.removePotionEffect(Potion.getPotionById(2));
				}
	        }
	        else
	        {
				 SoundEvent se = new SoundEvent(new ResourceLocation("random.bow"));
				 player.playSound(se,0.5f, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	        	 //world.playSound(player, player.getPosition(),se, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

	        	 if (!world.isRemote)
	             {
	        		fishHook = new CustomFishHook(world, player);
	        		world.spawnEntity(fishHook);
	             }

	        	 player.swingArm(handIn);
	        }

		 return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	 	}
	
	 public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_)
	 {
		 if(clockTimer == true)
			 delayMove();
	 }
	 
	 public static void delayMove()
	 {
		 timeElapsed = (int)(clock.millis() - lastTime) / 1000;

		 if(timeElapsed >= 5)
		 {
		 BiGX.instance().clientContext.lock(false);
		 timeElapsed = 0;
		 clockTimer = false;

		 }
	 }
	// TODO: figure out if we need to return the Icon
//	@Override
//    @SideOnly(Side.CLIENT)
//    public IIcon func_94597_g()
//    {
//        return this.theIcon;
//    }

}
