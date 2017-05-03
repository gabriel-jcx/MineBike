package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Quest {
	protected String id; 		// Quest ID by type
	
	private String name, description;
	private String[] requirements;
	private List<EntityPlayer> players;
	public List<IQuestEvent> events;
	private List<ItemStack> rewardItems;
	private int rewardXP, rewardCoins;
	
	public Quest(String id) {
		this(id, "Quest", "I'm a quest!");
	}
	
	public Quest(String id, String n, String d) {
		this(id, n, d, new String[1]);
	}
	
	public Quest(String id, String n, String d, String[] requirements) {
		this.id = id;
		events = new ArrayList<IQuestEvent>();
		rewardItems = new ArrayList<ItemStack>();
		name = n;
		description = d;
		this.requirements = requirements;
	}
	
	public String getQuestId()
	{
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String[] getRequirements() {
		return requirements;
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
