package org.ngs.bigx.minecraft;

import java.util.ArrayList;
import java.util.List;

import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.worldgen.structures.WorldStructure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public class BikeWorldData extends WorldSavedData {
	
	private static final String IDENTIFIER = "bikemod";
	private List<WorldStructure> structs = new ArrayList<WorldStructure>();
	private List<Quest> quests = new ArrayList<Quest>();

	public BikeWorldData(String identifier) {
		super(identifier);
	}
	
	public BikeWorldData() {
		super(IDENTIFIER);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("structures", Constants.NBT.TAG_COMPOUND);
		Context context = Main.instance().context;
		int i;
		for (i=0;i<list.tagCount();i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			WorldStructure struct = new WorldStructure(compound.getString("name"), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"), DimensionManager.getWorld(compound.getInteger("world")), context.getStructure(compound.getString("structure")), context.getTheme(compound.getString("theme")),compound.getInteger("id"));
			structs.add(struct);
		}
		NBTTagList questList = nbt.getTagList("quest",Constants.NBT.TAG_COMPOUND);
		for(i=0;i<questList.tagCount();i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			Quest quest = Quest.makeQuest(compound.getString("type"));
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
		for (WorldStructure struct: structs) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", struct.getName());
			tag.setInteger("x", struct.getX());
			tag.setInteger("y", struct.getY());
			tag.setInteger("z", struct.getZ());
			tag.setString("structure", struct.getStructure().getName());
			tag.setString("theme", struct.getTheme().getName());
			tag.setInteger("world", struct.getWorld().provider.dimensionId);
			structList.appendTag(tag);
		}
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
			tag.setString("state", quest.getStateMachine().toString());
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
	
	public void updateStructures(List<WorldStructure> list) {
		structs = new ArrayList<WorldStructure>(list);
		markDirty();
	}
	
	public List<WorldStructure> getStructures() {
		return structs;
	}
	
	public void addStructure(WorldStructure struct) {
		structs.add(struct);
		markDirty();
	}
	
	public List<Quest> getQuests() {
		return quests;
	}
	
	public void addQuest(Quest q) {
		quests.add(q);
		markDirty();
	}

}
