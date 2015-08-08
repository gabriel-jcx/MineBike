package com.ramon.hellow.networking;

import com.jcraft.jogg.Buffer;
import com.ramon.hellow.CommonEventHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;

public class UpdateHungerMessage implements IMessage {
	
	private int hunger;
	
	public UpdateHungerMessage() {
		
	}
	
	public UpdateHungerMessage(int hunger) {
		this.hunger = hunger;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		hunger = ByteBufUtils.readVarInt(buf, 2);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf,hunger, 2);
	}
	
	public static class Handler implements IMessageHandler<UpdateHungerMessage,IMessage> {

		@Override
		public IMessage onMessage(UpdateHungerMessage message, MessageContext ctx) {
			CommonEventHandler.setFoodLevel(ctx.getServerHandler().playerEntity, message.hunger);
			return null;
		}

	}


}
