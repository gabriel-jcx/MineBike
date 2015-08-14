package com.ramon.hellow;

import java.util.ArrayList;
import java.util.List;

import com.ramon.hellow.quests.Quest;
import com.ramon.hellow.worldgen.structures.WorldStructure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
		for (int i=0;i<list.tagCount();i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			WorldStructure struct = new WorldStructure(compound.getString("name"), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"), DimensionManager.getWorld(compound.getInteger("world")), context.getStructure(compound.getString("structure")), context.getTheme(compound.getString("theme")));
			structs.add(struct);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		for (WorldStructure struct: structs) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", struct.getName());
			tag.setInteger("x", struct.getX());
			tag.setInteger("y", struct.getY());
			tag.setInteger("z", struct.getZ());
			tag.setString("structure", struct.getStructure().getName());
			tag.setString("theme", struct.getTheme().getName());
			tag.setInteger("world", struct.getWorld().provider.dimensionId);
			list.appendTag(tag);
		}
		nbt.setTag("structures", list);
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
