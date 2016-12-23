package org.ngs.bigx.minecraft.networking;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import scala.Char;

public class CommandMessage implements IMessage {
	
	public String command;
	
	public CommandMessage() {}
	
	public CommandMessage(String c) {
		command = c;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		command = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, command);
	}

}
