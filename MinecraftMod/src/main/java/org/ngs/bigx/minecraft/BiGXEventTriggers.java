package org.ngs.bigx.minecraft;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.levelUp.ChestSystem;
import org.ngs.bigx.minecraft.levelUp.LevelSystem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BiGXEventTriggers {	
	
	private boolean soundTriggerEntered = false;
	
	public static void onRightClick(PlayerInteractEvent event, EntityPlayer player){
		DoorLocked(event, player);
	}
	
	public static void DoorLocked(PlayerInteractEvent event, EntityPlayer player){
		if(event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)){
			if(checkClickedInArea(event, -156,71,344, -156,71,345) && checkEntityInArea(player, -158,71,344, -156,72,345))
				GuiMessageWindow.showMessage(BiGXTextBoxDialogue.doorLocked);;
		}
	}
	
	public void MusicPlaying(EntityPlayer player){
		if (checkEntityInArea(player, -167, 70, 343, -166, 74, 346) && player.dimension == 100 && !soundTriggerEntered){
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.soundComment);
			soundTriggerEntered = true;
		}
	}
	
	public static void chestInteract(PlayerInteractEvent e, World w, LevelSystem levelSys){
		ChestSystem.interactWithChests(e, levelSys);
//		if (e.x == ChestSystem.caveChest.xCoord && e.y == ChestSystem.caveChest.yCoord && e.z == ChestSystem.caveChest.zCoord) {
//			System.out.println("CHEST FOUND");
//			TileEntityChest c = (TileEntityChest)w.getTileEntity(e.x, e.y, e.z);
//			ItemStack b = createMessage(BiGXTextBoxDialogue.caveChestMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.caveChestMsgTitle);
//			c.setInventorySlotContents(0, b);
//		}
//		
//		if (e.x == ChestSystem.secretChest.xCoord && e.y == ChestSystem.secretChest.yCoord && e.z == ChestSystem.secretChest.zCoord){
//			System.out.println("SECRET CHEST FOUND");
////			ChestLocked(e, e.entityPlayer);
//			TileEntityChest c = (TileEntityChest)w.getTileEntity(e.x, e.y, e.z);
//			ItemStack b = createMessage(BiGXTextBoxDialogue.secretRoomMsg, BiGXTextBoxDialogue.QuestMsgAuthor, BiGXTextBoxDialogue.secretRoomMsgTitle);
//			c.setInventorySlotContents(0, b);
//			
//			for (int i = 1; i <= levelSys.getThiefLevel(); ++i){
//				ItemStack p = new ItemStack(Items.potionitem);
//				p.setStackDisplayName("Teleportation Potion " + i);
//				c.setInventorySlotContents(i, p);
//			}
//		}
	}
	
	public static void GivePlayerGoldfromCoins(EntityPlayer player, int numOfCoins){
		int numOfGold = convertCoinsToGold(numOfCoins);
		String rewardStr = BiGXTextBoxDialogue.gotReward + numOfGold + " Gold Ingots!";
		GuiMessageWindow.showGoldBar(rewardStr);
		
		if (numOfGold > 0)
			for (int i = 0; i < numOfGold; i++)
				player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(266))); ///Add reward to inventory
	}
	
	public static void givePlayerMessage(EntityPlayer player, String message, String author, String title){
		ItemStack b = createMessage(message, author, title);
		if (!player.inventory.hasItemStack(b))
			player.inventory.addItemStackToInventory(b);
	}
	
	public static ItemStack createMessage(String message, String author, String title){
		ItemStack b = new ItemStack(Items.written_book);
		NBTTagList pages = new NBTTagList();
		pages.appendTag(new NBTTagString(message));
		b.stackTagCompound = new NBTTagCompound();
		b.stackTagCompound.setTag("author", new NBTTagString(author));
		b.stackTagCompound.setTag("title", new NBTTagString(title));
		b.stackTagCompound.setTag("pages", pages);
		return b;
	}
	
	
	public static boolean givePlayerKey(EntityPlayer player, String name, String message){
		ItemStack key = new ItemStack(Item.getItemById(4424));
		key.setStackDisplayName(name);
		if (!player.inventory.hasItemStack(key)){
			if (message != "")
				GuiMessageWindow.showMessage(message);
			player.inventory.addItemStackToInventory(key);
			return true;
		}
		return false;
	}
	
	public static boolean checkEntityInArea(Entity entity, double xCoord, double yCoord, double zCoord, double xCoord2, double yCoord2, double zCoord2){
		if (entity.posX >= xCoord && entity.posX <= xCoord2)
			if (entity.posY >= yCoord && entity.posY <= yCoord2)
				if (entity.posZ >= zCoord && entity.posZ <= zCoord2)
					return true;
		return false;
	}
	
	public static boolean checkEntityInArea(Entity entity, Vec3 xyz1, Vec3 xyz2){
		return checkEntityInArea(entity, xyz1.xCoord, xyz1.yCoord, xyz1.zCoord, xyz2.xCoord, xyz2.yCoord, xyz2.zCoord);
	}
	
	//Private helper methods
	private static boolean checkClickedInArea(PlayerInteractEvent event, int x1, int y1, int z1, int x2, int y2, int z2){
		if (event.x >= x1 && event.x <= x2)
			if (event.y >= y1 && event.y <= y2)
				if (event.z >= z1 && event.z <= z2)
					return true;
		return false;
	}
	
	private static int convertCoinsToGold(int numCoins){
		return Math.floorDiv(numCoins, 100);
	}
	
}