package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.client.GuiScreenQuest;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestTileEntity;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
		quest = Main.instance().context.questManager.makeQuest(compound.getString("type"),compound.getInteger("ID"));
		
		// TODO: Need a logic to be synchronize player list.
		quest.addPlayer(Minecraft.getMinecraft().thePlayer.getDisplayName(), Main.instance().context);
        System.out.println("Quest Created on the client side. State[" + quest.getStateMachine().toString() + "]");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("type", quest.getType());
		compound.setInteger("ID", quest.getID());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<ReceiveQuestMessage,IMessage> {

		@Override
		public IMessage onMessage(ReceiveQuestMessage message, MessageContext ctx) {
			
            Main.instance().context.questManager.setSuggestedQuest(message.quest);

			return null;
		}

	}

}
