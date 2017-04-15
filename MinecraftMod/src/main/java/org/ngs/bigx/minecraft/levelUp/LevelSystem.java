package org.ngs.bigx.minecraft.levelUp;

import java.util.ArrayList;

import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.GuiDamage;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class LevelSystem {
	private int playerLevel = 1; ///the player's current level
	
	//Quest-specific (levels of the thief)
	private int thiefHealthMax = 50;
	private int thiefHealthCurrent = thiefHealthMax;
	private int thiefLevel = 3;
	private boolean thiefLevelUpFlag = false;
	
	public LevelSystem() {}
	
	public int getPlayerLevel(){
		return playerLevel;
	}
	
	public int getThiefHealthMax() {
		return thiefHealthMax;
	}

	public int getThiefHealthCurrent() {
		return thiefHealthCurrent;
	}

	public int getThiefLevel() {
		return thiefLevel;
	}
	
	public void levelUp(){
		playerLevel++;
	}
	

	public void initThiefStat()
	{
		thiefHealthMax = 50;
		thiefHealthCurrent = thiefHealthMax;
		thiefLevel = 1;
	}
	
	public void thiefLevelUp()
	{
		thiefLevel ++;
		
		thiefHealthMax = 50 + (int) Math.pow(3, thiefLevel);
		thiefHealthCurrent = thiefHealthMax;
	}
	
	public void setThiefLevel(int level)
	{
		thiefLevel = level;
		
		thiefHealthMax = 50 + (int) Math.pow(3, thiefLevel);
		thiefHealthCurrent = thiefHealthMax;
	}
	
	public int deductThiefHealth(Item itemOnHands, int virtualCurrency)
	{
		int deduction = 1;
		if (itemOnHands != null) {
			if(itemOnHands.getUnlocalizedName().equals("item.npcBronzeSword"))
			{
				deduction = 3;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.npcFrostSword"))
			{
				deduction = 9;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.npcMithrilSword"))
			{
				deduction = 27;
			}
			else if(itemOnHands.getUnlocalizedName().equals("item.npcEmeraldSword"))
			{
				deduction = 81;
			}
		}
		
		thiefHealthCurrent -= deduction;
		
		virtualCurrency += deduction;
		
		if(thiefHealthCurrent <= 0)
		{
			thiefHealthCurrent = 0;
			thiefLevelUpFlag = true;
		}
		
		GuiDamage.addDamageText(deduction, 255, 10, 10);
		
		return virtualCurrency;
	}
	
	public void getLevelRewards(EntityPlayer player, int virtualCurrency){
		int level = thiefLevel;
		
		switch(level)
		{
		case 1:
			level123rewards(player, virtualCurrency);
		case 2:
			level123rewards(player, virtualCurrency);
		case 3:
			level123rewards(player, virtualCurrency);
		case 4:
			level4rewards(player);
		case 5:
			level5rewards(player);
		}
	}
	
	public void level123rewards(EntityPlayer player, int virtualCurrency){
		BiGXEventTriggers.GivePlayerGoldfromCoins(player, virtualCurrency*thiefLevel); ///Give player gold from coins
		switch(thiefLevel){
		case 3:
			BiGXEventTriggers.givePlayerKey(player, "Burned Key", "");
		}
	}
	
	public void level4rewards(EntityPlayer player){
		player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4421)));
		GuiMessageWindow.showMessage(BiGXTextBoxDialogue.levelUpMsg);
	}
	
	public void level5rewards(EntityPlayer player){
		player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4420)));
	}
	
}
