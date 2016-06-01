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
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HandleQuestMessageOnServer implements IMessage {
	Quest quest;
	Trigger trigger;
	String questType;
	int questId;
	
	public HandleQuestMessageOnServer() { }
	
	public HandleQuestMessageOnServer(Quest quest,Trigger trigger) {
		this.quest = quest;	
		this.trigger = trigger;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		System.out.println("Quest count: "+Main.instance().context.questManager.playerQuestsMapping.size());
		quest = Main.instance().context.questManager.getQuestFromPlayerQuestMap(compound.getInteger("ID"));
		trigger = Trigger.valueOf(compound.getString("trigger"));
		questType = compound.getString("type");
		questId = compound.getInteger("ID");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("type", quest.getType());
		compound.setInteger("ID",quest.getID());
		compound.setString("trigger", trigger.toString());
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<HandleQuestMessageOnServer,IMessage> {

		@Override
		public IMessage onMessage(HandleQuestMessageOnServer message, MessageContext ctx) {
			System.out.println("Recevied Trigger[" + message.trigger.toString() + "]");
			
			if (message.quest==null) {
				System.out.println("[ERROR-NGS] no quest??");
				return null;
			}
			
			switch(message.trigger)
			{
			case MakeQuestACK:
				message.quest.notification();
				World worldd = message.quest.getOriginalWorld();
				if(!worldd.isRemote){
					EntityCreeper creeper = new EntityCreeper(worldd);
					creeper.setPosition(67, 64, 250);
					worldd.spawnEntityInWorld(creeper);
				}
				break;
			default:
				System.out.println(message.quest.getStateMachine().toString()+" trigger->"+message.trigger.toString());
	            message.quest.triggerStateChange(message.trigger);
				break;
			}
			
			return null;
		}

	}

}
