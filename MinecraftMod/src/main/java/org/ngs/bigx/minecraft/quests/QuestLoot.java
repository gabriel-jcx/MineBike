package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;


public class QuestLoot {
	private ArrayList items;
	private int coinAmount;
	private int expAmount;
	
	public QuestLoot(int coins, int exp, ItemStack... items) {
		SetLoot(items);
		SetCoins(coins);
		SetExperience(exp);
	}
	
	public void SetLoot(ItemStack[] items) {
		this.items = new ArrayList();
		AddLoot(items);
	}
	
	public void AddLoot(ItemStack[] newItems) {
		for (int i = 0; i < newItems.length; i++) {
			this.items.add(newItems[i]);
		}
	}
	
	public void SetCoins(int coins) {
		coinAmount = coins;
	}
	
	public void SetExperience(int exp) {
		expAmount = exp;
	}
	
	public ItemStack[] GetLoot() {
		return (ItemStack[]) items.toArray();
	}
	
	public int GetCoins() {
		return coinAmount;
	}
	
	public int GetExperience() {
		return expAmount;
	}
}
