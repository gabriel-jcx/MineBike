package org.ngs.bigx.minecraft;

import java.awt.Event;

import org.ngs.bigx.minecraft.item.ModItems;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderQuests;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	CommonEventHandler events = new CommonEventHandler();
	
	public void preInit(FMLPreInitializationEvent e) {
		ModItems.init(Main.instance().context);
		DimensionManager.registerProviderType(WorldProviderQuests.dimID, WorldProviderQuests.class, true);
		DimensionManager.registerDimension(WorldProviderQuests.dimID, WorldProviderQuests.dimID);
		FMLCommonHandler.instance().bus().register(events);
    	MinecraftForge.EVENT_BUS.register(events);
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
    	
    }
}
