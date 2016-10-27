package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class QuestLootDatabase {
	private static Map<String, QuestLoot> database;
	
	public QuestLootDatabase() {
		database = new HashMap<String, QuestLoot>();
		
		// TODO ADD QUESTS HERE!
		// Create name for quest, then instantiate QuestLoot (coins, exp, multiple ItemStacks)
		
		// To access the database, you call GetReward(string name), which calls
		// database.get and returns the QuestLoot item associated with the quest name.
		
		database.put("SampleQuest1", new QuestLoot(25, 100,
				new ItemStack(Blocks.obsidian, 16),
				new ItemStack(Items.flint_and_steel),
				new ItemStack(Items.iron_pickaxe),
				new ItemStack(Items.golden_apple)));
	}
	
	
	public QuestLoot GetReward(String name) {
		return database.get(name);
	}
}
