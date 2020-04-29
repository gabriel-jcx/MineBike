package org.ngs.bigx.minecraft;

import net.minecraft.world.DimensionType;
import org.ngs.bigx.minecraft.networking.BiGXGuiHandler;
import org.ngs.bigx.minecraft.quests.custom.FishingQuest;
import org.ngs.bigx.minecraft.quests.custom.MinerQuest;
import org.ngs.bigx.minecraft.quests.custom.OverCookedQuest;
import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDark;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderEmpty;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFishing;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderMineRun;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderOvercooked;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderSoccer;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderTRON;

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
