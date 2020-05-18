package org.ngs.bigx.minecraft;

import net.minecraft.world.DimensionType;


import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	CommonEventHandler events = new CommonEventHandler();
	
	public void preInit(FMLPreInitializationEvent e) {
		// Removed custom dimension register
    }

    public void init(FMLInitializationEvent e) {
//    	NetworkRegistry.INSTANCE.registerGuiHandler(BiGX.modInstance, new BiGXGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
    	
    }
    
    
}
