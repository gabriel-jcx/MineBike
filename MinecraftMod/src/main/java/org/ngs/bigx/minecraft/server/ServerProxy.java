package org.ngs.bigx.minecraft.server;

import org.ngs.bigx.minecraft.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy {
	ServerEventHandler serverEvents = new ServerEventHandler();
	
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		FMLCommonHandler.instance().bus().register(serverEvents);
    	MinecraftForge.EVENT_BUS.register(serverEvents);
    }
}
