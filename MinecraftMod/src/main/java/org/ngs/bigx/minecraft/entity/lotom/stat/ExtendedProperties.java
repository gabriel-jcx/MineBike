package org.ngs.bigx.minecraft.entity.lotom.stat;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
//import net.minecraftforge.common.IExtendedEntityProperties;

import com.google.common.collect.Maps;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//public class ExtendedProperties implements IExtendedEntityProperties {
public class ExtendedProperties  implements ICapabilityProvider {
	private static String IDENTIFIER = "ExtendedEntityStats";

	private Map <String, EntityStat> stats = Maps.newHashMap();

	public void saveNBTData(NBTTagCompound compound) {
		for (EntityStat stat : this.stats.values()) {
			NBTTagCompound subTag = new NBTTagCompound();
			stat.writeToNBT(subTag);
			compound.setTag(stat.getName(), subTag);
		}
	}


	public void loadNBTData(NBTTagCompound compound) {
		for (EntityStat stat : this.stats.values()) {
			NBTTagCompound subTag = compound.getCompoundTag(stat.getName());

			if (subTag != null) {
				stat.readFromNBT(subTag);
			}
		}
	}


	public void init(Entity entity, World world) {
		for (EntityStat stat : StatRegistry.registry.get(entity.getClass())) {
			EntityStat clone = stat.init(entity, world);
			this.stats.put(clone.getName(), clone);
		}
	}

	@SubscribeEvent
    public void expandCapa(AttachCapabilitiesEvent event) {

    }

    protected static void register(Entity entity) {

//		IDENTIFIER = entity.registerExtendedProperties(IDENTIFIER, new ExtendedProperties());
	}

	protected static ExtendedProperties get(Entity entity) {
		// TODO: figureout how to  get Extended Properties
		//return (ExtendedProperties) entity.getExtendedProperties(IDENTIFIER);
		return null;
	}

	protected EntityStat getStat(String name) {
		return this.stats.get(name);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return false;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return null;
	}
}