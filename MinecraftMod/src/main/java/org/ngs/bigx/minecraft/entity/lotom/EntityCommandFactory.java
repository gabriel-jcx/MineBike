package org.ngs.bigx.minecraft.entity.lotom;

import java.net.SocketAddress;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

public class EntityCommandFactory {
	private static EntityCommand command = null;
	
	public static EntityCommand get(WorldServer world) {
		if (command == null) {
			command = new EntityCommand(world);
		}
		return command;
	}
	
	public static void removeCommandPlayer() {
		// Not sure if this is how entities are removed?
		command = null;
	}
}
