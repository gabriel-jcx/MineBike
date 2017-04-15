package org.ngs.bigx.minecraft;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import org.ngs.bigx.minecraft.networking.BiGXGuiHandler;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {
	
	CommonEventHandler events = new CommonEventHandler();
	
	public void preInit(FMLPreInitializationEvent e) {
		DimensionManager.registerProviderType(WorldProviderFlats.dimID, WorldProviderFlats.class, true);
		DimensionManager.registerDimension(WorldProviderFlats.dimID, WorldProviderFlats.dimID);
		
		DimensionManager.registerProviderType(WorldProviderFlats.fireQuestDimID, WorldProviderFlats.class, true);
		DimensionManager.registerDimension(WorldProviderFlats.fireQuestDimID, WorldProviderFlats.fireQuestDimID);
		
		DimensionManager.registerProviderType(WorldProviderDungeon.dimID, WorldProviderDungeon.class, true);
		DimensionManager.registerDimension(WorldProviderDungeon.dimID, WorldProviderDungeon.dimID);
		
		DimensionManager.registerProviderType(WorldProviderDungeon.fireQuestDimID, WorldProviderDungeon.class, true);
		DimensionManager.registerDimension(WorldProviderDungeon.fireQuestDimID, WorldProviderDungeon.fireQuestDimID);
		
		FMLCommonHandler.instance().bus().register(events);
    	MinecraftForge.EVENT_BUS.register(events);
    	MinecraftForge.TERRAIN_GEN_BUS.register(events);
    }

    public void init(FMLInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(BiGX.modInstance, new BiGXGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
    	
    }
    
    
}
