package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.GuiScreenQuest;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestTileEntity;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class HandleQuestMessageOnClient implements IMessage {
	Quest quest;
	Trigger trigger;
	String questType;
	int questId;
	
	public HandleQuestMessageOnClient() {
	}
	
	public HandleQuestMessageOnClient(Quest quest, Trigger trigger) {
		this.quest = quest;
		this.trigger = trigger;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		System.out.println("Quest count: "+BiGX.instance().context.questManager.playerQuestsMapping.size());
		trigger = Trigger.valueOf(compound.getString("trigger"));
		questType = compound.getString("type");
		questId = compound.getInteger("ID");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("type", quest.getType());
		compound.setInteger("ID", quest.getID());
		compound.setString("trigger", trigger.toString());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<HandleQuestMessageOnClient,IMessage> {

		@Override
		public IMessage onMessage(HandleQuestMessageOnClient message, MessageContext ctx) {
			System.out.println("Recevied Trigger[" + message.trigger.toString() + "]");
			
			// TODO: Need to separate bigx client context with bigx server context
			switch(message.trigger)
			{
			case MakeQuest:
				message.quest = BiGX.instance().context.questManager.makeQuest(message.questType,message.questId);
//				message.quest.setOriginalWorld(Minecraft.getMinecraft().thePlayer.getEntityWorld());
	    		
	    		// TODO: Need a logic to be synchronize player list.
				//message.quest.addPlayer(Minecraft.getMinecraft().thePlayer.getDisplayName(), Main.instance().context);
	            System.out.println("Quest Created on the client side. State[" + message.quest.getStateMachine().toString() + "]");
				
	            BiGX.instance().context.questManager.setSuggestedQuest(message.quest);

	            HandleQuestMessageOnServer packet = new HandleQuestMessageOnServer(message.quest, Trigger.MakeQuestACK);  
				BiGX.network.sendToServer(packet);
				break;
			case NotifyQuest:
				BiGX.instance().context.questManager.unsetQuestPopupShown();
				message.quest = BiGX.instance().context.questManager.getSuggestedQuest();
				message.quest.triggerStateChange(message.trigger);
	            System.out.println("Quest Suggested State[" + message.quest.getStateMachine().toString() + "]");
				break;
			default:
				message.quest = BiGX.instance().context.questManager.getQuest();
				message.quest.triggerStateChange(message.trigger);
	            System.out.println("Quest State[" + message.quest.getStateMachine().toString() + "]");
				break;
			}

			return null;
		}

	}

}
