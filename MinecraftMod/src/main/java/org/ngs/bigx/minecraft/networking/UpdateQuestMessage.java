package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.client.GuiScreenQuest;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class UpdateQuestMessage implements IMessage {
	Quest quest;
	Trigger trigger;
	
	public UpdateQuestMessage() { }
	
	public UpdateQuestMessage(Quest quest,Trigger trigger) {
		this.quest = quest;	
		this.trigger = trigger;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		System.out.println("Quest count: "+Main.instance().context.questManager.currentQuests.size());
		quest = Main.instance().context.questManager.getQuest(compound.getInteger("ID"));
		trigger = Trigger.valueOf(compound.getString("trigger"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("ID",quest.getID());
		compound.setString("trigger", trigger.toString());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<UpdateQuestMessage,IMessage> {

		@Override
		public IMessage onMessage(UpdateQuestMessage message, MessageContext ctx) {
			if (message.quest==null) {
				System.out.println("no quest");
			}
			System.out.println(message.quest.getStateMachine().toString()+" trigger->"+message.trigger.toString());
            message.quest.triggerStateChange(message.trigger);
			return null;
		}

	}

}
