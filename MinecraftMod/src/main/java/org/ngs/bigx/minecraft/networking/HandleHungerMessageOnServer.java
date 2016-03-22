package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.CommonEventHandler;

import com.jcraft.jogg.Buffer;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;

public class HandleHungerMessageOnServer implements IMessage {
	
	private int hunger;
	
	public HandleHungerMessageOnServer() {
		
	}
	
	public HandleHungerMessageOnServer(int hunger) {
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
	
	public static class Handler implements IMessageHandler<HandleHungerMessageOnServer,IMessage> {

		@Override
		public IMessage onMessage(HandleHungerMessageOnServer message, MessageContext ctx) {
			return null;
		}

	}


}
