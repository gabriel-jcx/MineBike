package org.ngs.bigx.minecraft.entity.lotom.stat;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class StatPacket implements IMessage {

	 private NBTTagCompound tag;

	 public StatPacket() {
	 }

	 public StatPacket(ISyncedStat stat, Entity entity) {
		 this.tag = new NBTTagCompound();
		 this.tag.setString("Name", stat.getName());
		 this.tag.setInteger("ID", entity.getEntityId());
		 NBTTagCompound subTag = new NBTTagCompound();
		 stat.writeToNBT(subTag);
		 this.tag.setTag("SubTag", subTag);
	 }

	 @Override
	 public void fromBytes(ByteBuf buf) {
		 this.tag = ByteBufUtils.readTag(buf);
	 }

	 @Override
	 public void toBytes(ByteBuf buf) {
		 ByteBufUtils.writeTag(buf, this.tag);
	 }

	 public String getStatName() {
		 return this.tag.getString("Name");
	 }

	 public int getEntityID() {
		 return this.tag.getInteger("ID");
	 }

	 public NBTTagCompound getTag() {
		 return this.tag.getCompoundTag("SubTag");
	 }
	}
