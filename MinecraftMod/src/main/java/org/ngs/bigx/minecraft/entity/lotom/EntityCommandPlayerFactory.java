package org.ngs.bigx.minecraft.entity.lotom;

import java.net.SocketAddress;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

public class EntityCommandPlayerFactory {
	private static GameProfile PROFILE = new GameProfile(UUID.fromString("baec1b77-ddbf-4491-8d72-0c15c6963b42"), "");
	private static EntityCommandPlayer commandPlayer = null;
	
	public static EntityCommandPlayer get(WorldServer world) {
		if (commandPlayer == null) {
			commandPlayer = new EntityCommandPlayer(world, PROFILE);
			//MinecraftServer server = MinecraftServer.getServer();
			//ServerConfigurationManager serverManager = server.getConfigurationManager();
			//SocketAddress sa = server.func_147137_ag().addLocalEndpoint();
			//NetworkManager manager = NetworkManager.provideLocalClient(sa);
			//NetHandlerPlayServer netHandler = new NetHandlerPlayServer(server, manager, commandPlayer);
			//serverManager.initializeConnectionToPlayer(manager, commandPlayer, netHandler);
		}
		return commandPlayer;
	}
	
	public static void removeCommandPlayer() {
		// Not sure if this is how entities are removed?
		commandPlayer = null;
	}
}
