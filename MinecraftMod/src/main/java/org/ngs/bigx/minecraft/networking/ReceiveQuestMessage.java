package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.client.GuiScreenQuest;
import org.ngs.bigx.minecraft.quests.Quest;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class ReceiveQuestMessage implements IMessage {
	Quest quest;
	
	public ReceiveQuestMessage() {
	}
	
	public ReceiveQuestMessage(Quest quest) {
		this.quest = quest;	
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		quest = new Quest(compound.getString("name"),compound.getBoolean("complete"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("name", quest.getName());
		compound.setBoolean("complete", quest.getCompleted());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<ReceiveQuestMessage,IMessage> {

		@Override
		public IMessage onMessage(ReceiveQuestMessage message, MessageContext ctx) {
			GuiScreenQuest gui = new GuiScreenQuest(Minecraft.getMinecraft().thePlayer,message.quest);
			Minecraft.getMinecraft().displayGuiScreen(gui);
			System.out.println(message.quest.getName());
			return null;
		}

	}

}
