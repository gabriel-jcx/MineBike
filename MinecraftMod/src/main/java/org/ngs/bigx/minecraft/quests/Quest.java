package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Quest {
	protected String id; 		// Quest ID by type
	
	private String name, description;
	private List<EntityPlayer> players;
	public List<IQuestEvent> events;
	private List<ItemStack> rewardItems;
	private int rewardXP, rewardCoins;
	
	public Quest() {
		events = new ArrayList<IQuestEvent>();
		rewardItems = new ArrayList<ItemStack>();
		name = "Quest";
		description = "I'm a quest!";
	}
	
	public Quest(String n, String d) {
		events = new ArrayList<IQuestEvent>();
		rewardItems = new ArrayList<ItemStack>();
		name = n;
		description = d;
	}
	
	public void AddPlayer(EntityPlayer player) {
		players.add(player);
	}
	
	public IQuestEvent getCurrentQuestEvent() {
		for (IQuestEvent e : events) {
			if (!e.IsComplete())
				return e;
		}
		return null;
	}
	
	public boolean IsComplete() {
		for (IQuestEvent questEvent : events) {
			if (!questEvent.IsComplete())
				return false;
		}
		return true;
	}
	
	public List<ItemStack> GetRewardItems() {
		return rewardItems;
	}
	
	public int GetRewardXP() {
		return rewardXP;
	}
	
	public int GetRewardCoins() {
		return rewardCoins;
	}
}
