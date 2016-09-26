package org.ngs.bigx.minecraft.entity.lotom.stat;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityStat {

    private final String name;

    public EntityStat(String name) {
        this.name = StatRegistry.getName(name);
    }

    public final String getName() {
        return this.name;
    }

    /**
     * Fired during initialization of stats for specific entity. IMPORTANT:
     * return a new enitity-specific instance of this stat or things are not
     * going to work very well
     * 
     * @param entity
     * @param world
     * @return a new instance of this stat
     */
    public abstract EntityStat init(Entity entity, World world);

    public abstract void readFromNBT(NBTTagCompound compound);

    public abstract void writeToNBT(NBTTagCompound compound);
}