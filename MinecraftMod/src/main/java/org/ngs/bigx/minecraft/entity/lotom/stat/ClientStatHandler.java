package org.ngs.bigx.minecraft.entity.lotom.stat;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientStatHandler implements IMessageHandler<StatPacket, IMessage> {

	@Override
	public IMessage onMessage(StatPacket message, MessageContext ctx) {
		World world = FMLClientHandler.instance().getWorldClient();
		Entity entity = world.getEntityByID(message.getEntityID());
		
		if (entity != null) {
			ISyncedStat stat = (ISyncedStat) StatRegistry.getStat(entity, message.getStatName());
			stat.readFromNBT(message.getTag());
		}
		return null;
	}
}
