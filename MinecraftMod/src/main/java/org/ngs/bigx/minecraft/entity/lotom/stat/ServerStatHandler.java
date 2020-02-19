package org.ngs.bigx.minecraft.entity.lotom.stat;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerStatHandler implements IMessageHandler<StatPacket, StatPacket> {

	@Override
	public StatPacket onMessage(StatPacket message, MessageContext ctx) {
		World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
		Entity entity = world.getEntityByID(message.getEntityID());
		
		if (entity != null) {
			ISyncedStat stat = (ISyncedStat) StatRegistry.getStat(entity, message.getStatName());
			return stat.isDataSynced(message.getTag()) ? null : new StatPacket(stat, entity);
		} else {
			return null;
		}
	}

}