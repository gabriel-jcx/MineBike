package org.ngs.bigx.minecraft;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.event.world.NoteBlockEvent;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.levelUp.ChestSystem;
import org.ngs.bigx.minecraft.npcs.NpcEvents;
import org.ngs.bigx.minecraft.npcs.NpcLocations;

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
	public static void onRightClick(PlayerInteractEvent event, EntityPlayer player){
		DoorLocked(event, player);
	}
	
	public static void DoorLocked(PlayerInteractEvent event, EntityPlayer player){
		PlayerInteractEvent.RightClickBlock obj;
		// TODO: need to figure out how to check if event is righclickBlock;
		//if(event.equals()){
		//if(event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)){
		if(true){
			//Logger logger;
			logger.info("Error, this should not reach here!");
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
	
	public static void attackNPC(AttackEntityEvent event){

		if (BiGXEventTriggers.checkEntityInArea(event.getTarget(), NpcLocations.trainingBot.addVector(0, -1, 0), NpcLocations.trainingBot.addVector(1, 0, 1))){
			int deduction = 1;
			EnumHand hand = EnumHand.MAIN_HAND;
			if(event.getEntityPlayer().getHeldItem(hand) != null && event.getEntityPlayer().getHeldItem(hand).getDisplayName().contains(("Wooden Sword")))
			//if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getDisplayName().contains("Wooden Sword"))
				deduction = 2; //System.out.println(event.entityPlayer.getHeldItem().getDisplayName());
			if (event.getTarget().world.isRemote){
				//if(event.getTarget().world.isRemote){
				GuiDamage.addDamageText(deduction, 255, 10, 10);
				NpcEvents.botHealth -= deduction;
			}
			if (NpcEvents.botHealth <= 0){
				givePlayerKey(event.getEntityPlayer(), "Shiny Key", "You got the Shiny Key!");
				if (botHitClient && botHitServer) {
					NpcEvents.botHealth = 10;
					botHitClient = false;
					botHitServer = false;
				}
				//event.getTarget().setDead();
			}
		}
	}
	
	public static void GivePlayerGoldfromCoins(EntityPlayer player, int numOfCoins){
		int numOfGold = convertCoinsToGold(numOfCoins);
		String rewardStr = BiGXTextBoxDialogue.gotReward + numOfGold + " Gold Ingots!";
		GuiMessageWindow.showMessageAndImage(rewardStr, GuiMessageWindow.GOLDBAR_TEXTURE);
		
		System.out.println("Number of coins: " + numOfCoins);
		System.out.println("Number of gold: " + numOfGold);
		
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
	
	public static boolean givePlayerKey(EntityPlayer player, String name, String message) {
		if (message != "" && player.world.isRemote) {
			GuiMessageWindow.showMessage(message);
			botHitClient = true;
		}
		System.out.println(message != "" && player.world.isRemote);
		if (!player.world.isRemote) {
			ItemStack key = new ItemStack(Item.getItemById(4532));//new ItemStack(Item.getItemById(4424));
			key.setStackDisplayName(name);
			for (ItemStack item : player.inventory.mainInventory)
				if (item != null && item.getDisplayName().contains(name))
					return false;
			player.inventory.addItemStackToInventory(key);
			botHitServer = true;
		}
		return true;
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
	
	public static int convertCoinsToGold(int numCoins){
		return Math.floorDiv(numCoins, 10);
	}

	public static boolean givePlayerPotion(EntityPlayer player, String potionName, String message){
		boolean hasPotion = false;
		for (ItemStack items : player.inventory.mainInventory){
			if (items != null){
				System.out.println(items.getDisplayName());
				if (items.getDisplayName().contains(potionName)){
					hasPotion = true;
					break;
				}
			}
		}
		
		if (!hasPotion){
			ItemStack p = new ItemStack(Items.POTIONITEM);
			p.setStackDisplayName(potionName);
			player.inventory.addItemStackToInventory(p);
			if (!player.world.isRemote)
				GuiMessageWindow.showMessage(message);
		}
		return hasPotion;
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