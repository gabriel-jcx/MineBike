package org.ngs.bigx.minecraft;

import java.util.ArrayList;
import java.util.List;

import org.ngs.bigx.minecraft.quests.Quest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public class BikeWorldData extends WorldSavedData {
	
	private static final String IDENTIFIER = "bikemod";
	private List<Quest> quests = new ArrayList<Quest>();

	public BikeWorldData(String identifier) {
		super(identifier);
	}
	
	public BikeWorldData() {
		super(IDENTIFIER);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList questList = nbt.getTagList("quest",Constants.NBT.TAG_COMPOUND);
		for(int i=0;i<questList.tagCount();i++) {
			NBTTagCompound compound = questList.getCompoundTagAt(i);
			Quest quest = Quest.makeQuest(compound.getString("type"),compound.getBoolean("completed"));
			NBTTagList players = compound.getTagList("players", Constants.NBT.TAG_STRING);
			for (i=0;i<players.tagCount();i++) {
				String player = players.getStringTagAt(i);
				quest.addPlayer(player);
			}
			quests.add(quest);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList structList = new NBTTagList();
		nbt.setTag("structures", structList);
		NBTTagList questList = new NBTTagList();
		for (Quest quest: quests) {
			NBTTagCompound tag = new NBTTagCompound();
			NBTTagList playerList = new NBTTagList();
			List<String> players = quest.getPlayers();
			for (String player:players) {
				NBTTagString p = new NBTTagString(player);
				playerList.appendTag(p);
			}
			tag.setTag("players", playerList);
			tag.setBoolean("completed",quest.getCompleted());
			questList.appendTag(tag);
		}
		nbt.setTag("quests",questList);
	}
	
	public static BikeWorldData get(World world) {
		BikeWorldData data = (BikeWorldData) world.mapStorage.loadData(BikeWorldData.class, IDENTIFIER);
		if (data==null) {
			data = new BikeWorldData();
			world.mapStorage.setData(IDENTIFIER, data);
		}
		return data;
	}
	
	public List<Quest> getQuests() {
		return quests;
	}
	
	public void addQuest(Quest q) {
		quests.add(q);
		markDirty();
	}

}
