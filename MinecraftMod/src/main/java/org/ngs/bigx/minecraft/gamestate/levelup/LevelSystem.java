package org.ngs.bigx.minecraft.gamestate.levelup;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
//import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class LevelSystem {
	private int playerLevel = 1; ///the player's current level
	private int playerExp = 0; ///player's current exp
	
	public LevelSystem()
	{
		this.resetLevelSystem();
	}
	
	public void resetLevelSystem()
	{
		this.playerLevel = 1;
		this.playerExp = 0;
	}
	
	public int getPlayerLevel(){
		return playerLevel;
	}
	
	public int getPlayerExp(){
		return playerExp;
	}
	
	public boolean levelUp(){
		playerLevel++;
		System.out.println("[BiGX] playerLevel[" + playerLevel + "]");
		return true;
	}
	
	public boolean incExp(int exp){
		//increases experience needed to level up
//		System.out.println("[BiGX] experience: " + playerExp);
//		System.out.println("[BiGX] level: " + playerLevel);
		playerExp += exp;
//		System.out.println("[BiGX] should be true: " + (exp >= (playerLevel * 100)));
//		System.out.println("[BiGX] exp req: " + (playerLevel * 100));
		if (playerExp >= (playerLevel * 100))
			return levelUp();
		return false;
	}
	
	public void setPlayerLevel(int level){
		playerLevel = 1;
		playerExp = 0;
	}
	
	///Rewards based on levels
	public void giveLevelUpRewards(EntityPlayer player){
		System.out.println(playerLevel);
		
		ItemStack reward = null;
		
		switch(playerLevel)
		{
		case 2:
			reward = new ItemStack(Item.getItemById(4421));
			System.out.println("Level2");
			break;
		case 3:
			reward = new ItemStack(Item.getItemById(4420));
			System.out.println("Level3");
			break;
		}
		
		if (reward != null){
			player.inventory.addItemStackToInventory(reward);
			GuiMessageWindow.showMessage(BiGXTextBoxDialogue.gotReward + reward.getDisplayName());	
		}
	}
	
	public void getLevelRewards(EntityPlayer player, int virtualCurrency, int thiefLevel){
		int level = thiefLevel;
		
		switch(level)
		{
		case 1:
			level123rewards(player, virtualCurrency, thiefLevel);
		case 2:
			level123rewards(player, virtualCurrency, thiefLevel);
		case 3:
			level123rewards(player, virtualCurrency, thiefLevel);
		case 4:
			level4rewards(player);
		case 5:
			level5rewards(player);
		}
	}
	
	public void level123rewards(EntityPlayer player, int virtualCurrency, int thiefLevel){
		BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency*thiefLevel); ///Give player gold from coins
		switch(thiefLevel){
		case 2:
			player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4420)));
		case 3:
			BiGXEventTriggers.givePlayerKey(player, "Burned Key", "");
//		case #:
//			BiGXEventTriggers.givePlayerKey(player, "Damp Key", "");
//		case #:
//			BiGXEventTriggers.givePlayerKey(player, "Dusty Key", "");
//		case #:
//			BiGXEventTriggers.givePlayerKey(player, "Light Key", "");
		}
//		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.levelUpMsg);
	}
	
	public void level4rewards(EntityPlayer player){
		player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4421)));
//		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.levelUpMsg);
	}
	
	public void level5rewards(EntityPlayer player){
		player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4420)));
//		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.levelUpMsg);
	}
	
}
