package org.ngs.bigx.minecraft;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.levelUp.ChestSystem;
import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTask;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskTutorial;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
		ItemStack key = new ItemStack(Item.getItemById(4424));//new ItemStack(Item.getItemById(4424));
		key.setStackDisplayName(name); 
		if (!player.inventory.hasItemStack(key)){
			if (message != "" && !player.worldObj.isRemote)
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