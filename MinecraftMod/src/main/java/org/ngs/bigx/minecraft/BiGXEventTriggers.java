package org.ngs.bigx.minecraft;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.event.world.NoteBlockEvent;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import java.util.logging.Logger;

public class BiGXEventTriggers {
	private static Logger logger;
	private boolean soundTriggerEntered = false;
	private static boolean botHitClient = false, botHitServer = false;

	// IT seems this funciton is not used HMMMMMM...

//	public static void DoorLocked(PlayerInteractEvent event, EntityPlayer player){
//		PlayerInteractEvent.RightClickBlock obj;
//		// TODO: need to figure out how to check if event is righclickBlock;
//		//if(event.equals()){
//		//if(event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)){
//		if(true){
//			//Logger logger;
//			logger.info("Error, this should not reach here!");
//			if(checkClickedInArea(event, -156,71,344, -156,71,345) && checkEntityInArea(player, -158,71,344, -156,72,345))
//				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.doorLocked);;
//		}
//	}
//
//	public void MusicPlaying(EntityPlayer player){
//		if (checkEntityInArea(player, -167, 70, 343, -166, 74, 346) && player.dimension == 100 && !soundTriggerEntered){
//			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.soundComment);
//			soundTriggerEntered = true;
//		}
//	}
	


	

	
	public static void givePlayerMessage(EntityPlayer player, String message, String author, String title){
		ItemStack b = createMessage(message, author, title);
		if (!player.inventory.hasItemStack(b))
			player.inventory.addItemStackToInventory(b);
	}
	
	public static ItemStack createMessage(String message, String author, String title){
		ItemStack b = new ItemStack(Items.WRITTEN_BOOK);
		NBTTagList pages = new NBTTagList();
		pages.appendTag(new NBTTagString(message));


		//TODO: RE-write the NBTTagCompound for create message
		//b.stackTagCompound = new NBTTagCompound();
		//b.stackTagCompound.setTag("author", new NBTTagString(author));
		//b.stackTagCompound.setTag("title", new NBTTagString(title));
		//b.stackTagCompound.setTag("pages", pages);
		return b;
	}
	

	
	public static boolean checkEntityInArea(Entity entity, double xCoord, double yCoord, double zCoord, double xCoord2, double yCoord2, double zCoord2){
		if (entity.posX >= xCoord && entity.posX <= xCoord2)
			if (entity.posY >= yCoord && entity.posY <= yCoord2)
				if (entity.posZ >= zCoord && entity.posZ <= zCoord2)
					return true;
		return false;
	}
	
	public static boolean checkEntityInArea(Entity entity, Vec3d xyz1, Vec3d xyz2){
		return checkEntityInArea(entity, xyz1.x, xyz1.y, xyz1.z, xyz2.x, xyz2.y, xyz2.z);
	}

	
	//Private helper methods
	private static boolean checkClickedInArea(PlayerInteractEvent event, int x1, int y1, int z1, int x2, int y2, int z2){
		//event.get
		BlockPos position = event.getPos();
		int x = position.getX();
		int y = position.getY();
		int z = position.getZ();
		if (position.getX() >= x1 && x <= x2)
			if (y >= y1 && y <= y2)
				if (z >= z1 && z <= z2)
					return true;
		return false;
	}
	
}