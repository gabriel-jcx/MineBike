package org.ngs.bigx.minecraft.entity.lotom.stat;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncedStat {

    public boolean isDataSynced(NBTTagCompound compound);

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public String getName();

}
