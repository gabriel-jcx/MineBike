package org.ngs.bigx.minecraft.entity.lotom.stat;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.google.common.collect.Maps;

public class ExtendedProperties implements IExtendedEntityProperties {

	private static String IDENTIFIER = "ExtendedEntityStats";
	
	private Map <String, EntityStat> stats = Maps.newHashMap();
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		for (EntityStat stat : this.stats.values()) {
			NBTTagCompound subTag = new NBTTagCompound();
			stat.writeToNBT(subTag);
			compound.setTag(stat.getName(), subTag);
		}
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		for (EntityStat stat : this.stats.values()) {
			NBTTagCompound subTag = compound.getCompoundTag(stat.getName());
			
			if (subTag != null) {
				stat.readFromNBT(subTag);
			}
		}
	}
	
	@Override
	public void init(Entity entity, World world) {
		for (EntityStat stat : StatRegistry.registry.get(entity.getClass())) {
			EntityStat clone = stat.init(entity, world);
			this.stats.put(clone.getName(), clone);
		}
	}
	
	protected static void register(Entity entity) {
		IDENTIFIER = entity.registerExtendedProperties(IDENTIFIER, new ExtendedProperties());
	}
	
	protected static ExtendedProperties get(Entity entity) {
		return (ExtendedProperties) entity.getExtendedProperties(IDENTIFIER);
	}
	
	protected EntityStat getStat(String name) {
		return this.stats.get(name);
	}
}