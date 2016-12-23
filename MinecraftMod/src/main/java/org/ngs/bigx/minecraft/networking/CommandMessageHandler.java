package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.entity.lotom.EntityCommand;
import org.ngs.bigx.minecraft.entity.lotom.EntityCommandFactory;
import org.ngs.bigx.minecraft.entity.lotom.EntityCommandPlayer;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.command.ICommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

public class CommandMessageHandler implements IMessageHandler<CommandMessage, IMessage> {

	@Override
	public IMessage onMessage(CommandMessage message, MessageContext ctx) {
		MinecraftServer server = MinecraftServer.getServer();
		WorldServer world = server.worldServerForDimension(WorldProviderFlats.dimID);
		ServerConfigurationManager manager = server.getConfigurationManager();
		EntityCommand commander = EntityCommandFactory.get(world);
		System.out.println(message.command);
		for (Object c : server.getCommandManager().getPossibleCommands(commander)) {
			System.out.println(((ICommand)c).getCommandName());
		}
		//System.out.println();
		//for (String s : server.getConfigurationManager().func_152606_n())
		//	System.out.println(s);
		server.getCommandManager().executeCommand(server, message.command);
		return null;
	}
}
