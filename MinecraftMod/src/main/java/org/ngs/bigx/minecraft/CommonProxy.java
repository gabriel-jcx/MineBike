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
		DimensionType flatDType = DimensionType.register("Flats","Yunho?" ,WorldProviderFlats.dimID,WorldProviderFlats.class, true);
		//DimensionManager.registerProviderType(WorldProviderFlats.dimID, WorldProviderFlats.class, true);
		DimensionManager.registerDimension(WorldProviderFlats.dimID, flatDType);

		DimensionType fireQuestDType = DimensionType.register("Fire","Yunho?" ,WorldProviderFlats.fireQuestDimID,WorldProviderFlats.class, true);
		//DimensionManager.registerProviderType(WorldProviderFlats.fireQuestDimID, WorldProviderFlats.class, true);
		DimensionManager.registerDimension(WorldProviderFlats.fireQuestDimID, fireQuestDType);

		DimensionType DarkQuestDType = DimensionType.register("Dark","?" ,WorldProviderDark.dimID,WorldProviderDark.class, true);
		//DarkQuestDType.createDimension();
		//DimensionManager.registerProviderType(WorldProviderDark.dimID, WorldProviderDark.class, true);
		DimensionManager.registerDimension(WorldProviderDark.dimID, DarkQuestDType);

		DimensionType DungeonQuestDType = DimensionType.register("Dungeon","?" ,WorldProviderDungeon.dimID,WorldProviderDungeon.class, true);
		//DimensionManager.registerProviderType(WorldProviderDungeon.dimID, WorldProviderDungeon.class, true);
		DimensionManager.registerDimension(WorldProviderDungeon.dimID, DungeonQuestDType);

		DimensionType fireQuestDUType = DimensionType.register("Fire","?" ,WorldProviderDungeon.fireQuestDimID,WorldProviderDungeon.class, true);
		//DimensionManager.registerProviderType(WorldProviderDungeon.fireQuestDimID, WorldProviderDungeon.class, true);
		DimensionManager.registerDimension(WorldProviderDungeon.fireQuestDimID,  fireQuestDUType);

		DimensionType EmptyQuestDType = DimensionType.register("Empty","?" ,WorldProviderEmpty.dimID,WorldProviderEmpty.class, true);
		//DimensionManager.registerProviderType(WorldProviderEmpty.dimID, WorldProviderEmpty.class, true);
		DimensionManager.registerDimension(WorldProviderEmpty.dimID, EmptyQuestDType);
		
		//soccer dimension
		DimensionType SoccerQuestDType = DimensionType.register("Soccer","?" ,SoccerQuest.SOCCERDIMENSIONID, WorldProviderSoccer.class, true);
		//DimensionManager.registerProviderType(SoccerQuest.SOCCERDIMENSIONID, WorldProviderSoccer.class, true);
		DimensionManager.registerDimension(SoccerQuest.SOCCERDIMENSIONID, SoccerQuestDType);

		DimensionType FishingQuestDType = DimensionType.register("Fishing","?" ,FishingQuest.FISHINGDIMENSIONID, WorldProviderFishing.class, true);
		//DimensionManager.registerProviderType(FishingQuest.FISHINGDIMENSIONID, WorldProviderFishing.class, true);
		DimensionManager.registerDimension(FishingQuest.FISHINGDIMENSIONID, FishingQuestDType);
    
		//overcooked dimension
		DimensionType OverCookedQuestDType = DimensionType.register("OverCooked","?" ,OverCookedQuest.OVERCOOKEDDIMENSIONID, WorldProviderOvercooked.class, true);
		//DimensionManager.registerProviderType(OverCookedQuest.OVERCOOKEDDIMENSIONID, WorldProviderOvercooked.class, true);
		DimensionManager.registerDimension(OverCookedQuest.OVERCOOKEDDIMENSIONID,  OverCookedQuestDType);
		
		//mineRun dimension
		DimensionType MinerQuestDType = DimensionType.register("Miner","?" ,MinerQuest.MINERUNDIMENSIONID,WorldProviderMineRun.class, true);
		//DimensionManager.registerProviderType(MinerQuest.MINERUNDIMENSIONID, WorldProviderMineRun.class, true);
		DimensionManager.registerDimension(MinerQuest.MINERUNDIMENSIONID, MinerQuestDType);
		
		//DimensionManager.registerProviderType(WorldProviderTRON.TRONDIMENSIONID, WorldProviderTRON.class, true);
		DimensionType tronDType = DimensionType.register("Tron","2019_Summer" ,WorldProviderTRON.TRONDIMENSIONID,WorldProviderTRON.class, true);
		DimensionManager.registerDimension(WorldProviderTRON.TRONDIMENSIONID, tronDType);
		
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
