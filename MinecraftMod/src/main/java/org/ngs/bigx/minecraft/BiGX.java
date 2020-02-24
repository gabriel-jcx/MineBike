package org.ngs.bigx.minecraft;
	
import java.util.ArrayList;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockChest.Type;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.GameRuleChangeEvent;
import org.ngs.bigx.minecraft.bike.PedalingToBuildEventHandler;
import org.ngs.bigx.minecraft.block.BlockQuestChest;
import org.ngs.bigx.minecraft.block.QuestRFMChest;
//import org.ngs.bigx.minecraft.client.renderer.MysteriousKeyRenderer;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.entity.item.EntityTank;
import org.ngs.bigx.minecraft.entity.item.MineBikeEntityRegistry;
import org.ngs.bigx.minecraft.entity.item.MysteriousKey;
import org.ngs.bigx.minecraft.entity.lotom.BikeProperty;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.entity.lotom.stat.ClientStatHandler;
import org.ngs.bigx.minecraft.entity.lotom.stat.ISyncedStat;
import org.ngs.bigx.minecraft.entity.lotom.stat.ServerStatHandler;
import org.ngs.bigx.minecraft.entity.lotom.stat.StatPacket;
import org.ngs.bigx.minecraft.entity.lotom.stat.StatRegistry;
import org.ngs.bigx.minecraft.items.MineBikeCustomItems;
import org.ngs.bigx.minecraft.networking.HandleHungerMessageOnServer;
import org.ngs.bigx.minecraft.quests.QuestEventHandler;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;
import org.ngs.bigx.utility.Names;

//import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.MouseHelper;
//import net.minecraftforge.client
//import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.CustomItems;

@Mod (modid = BiGX.MODID, name = BiGX.MODNAME, version = BiGX.VERSION)

	public class BiGX {

		public static final boolean isDemoMode = true; 
	    public static final String MODID = "bike";
	    public static final String MODNAME = "Bike Mod";
	    public static final String VERSION = "1.0.0";

	    public static final String BIGXSERVERIP = "128.195.54.50";
//	    public static final String BIGXSERVERIP = "192.168.0.51";
//	    public static final String BIGXSERVERIP = "192.168.0.151";
	    
	    public static SimpleNetworkWrapper network;
	    
	    public static final String TEXTURE_PREFIX = "minebike";
	    
	    private static BiGX instance;

	    // TODO: need to figure out which function to set the Name of the QuestFRMCheck
		// TODO: figure out what this freaking 538 means.....
		// 1. setRegistryName("QuestRFMLucky);
		// 2. setUnlocalizedName("QuestFRMLucky");
		//BlockChest.Type = new BlockChest.Type(538); //
	    //public static final Block BlockQuestFRMCheck = (new QuestRFMChest(Type.BASIC)).setRegistryName("QuestRFMLucky"); //setBlockName("QuestRFMLucky");


	    public static final BlockQuestChest blockQuestChest = new BlockQuestChest();
	    
	    public static CharacterProperty characterProperty;
	    public static BikeProperty bikeProperty;	

	    public BigxClientContext clientContext; // BigxClientContext for client and BigxServerContext for server
	    public BigxServerContext serverContext; // BigxClientContext for client and BigxServerContext for server
	    
	    public static MouseHelper disableMouseHelper;

	    public static Item MysteriousKey;
	    
	    public static ArrayList<Item> customItems = MineBikeCustomItems.createItems();
	    
	    /*
	     * Instead of using magic numbers, an enum will differentiate our GUIs when
	     * calling EntityPlayer.openGui().
	     */
	    
	    public enum GUI_ENUM {
	    	QUEST_COMPLETE
	    }
	    
	    @Instance(BiGX.MODID)
	    public static BiGX modInstance;
	    
	    
	    @SidedProxy(clientSide="org.ngs.bigx.minecraft.client.ClientProxy", serverSide="org.ngs.bigx.minecraft.server.ServerProxy")
		public static CommonProxy proxy;
	    
	    @EventHandler
	    public void preInit(FMLPreInitializationEvent e) {
			// Item Register Event is now moved to CommonEventHandler under registerBlocks()
//	    	for(Item item : customItems)
//	    	{
//////				GameRegistry.registerTileEntity(item);
////	    		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
//	    		//GameRegistry.registerTileEntity(item, item.getUnlocalizedName().substring(5));
//				//GameRegistry.registerEntitySelector(item, item.getUnlocalizedName().substring(5));
//				;
//	    	}
			System.out.println("Register Entities STARTED");

			MineBikeEntityRegistry.RegisterMineBikeEntities();
			System.out.println("Register Entities DONE");
	    	instance = this;
	    	clientContext = new BigxClientContext(this);
	    	serverContext = new BigxServerContext(this);
	    	
	    	proxy.preInit(e);
	    	network = NetworkRegistry.INSTANCE.newSimpleChannel("BikeChannel");
	    	network.registerMessage(HandleHungerMessageOnServer.Handler.class,HandleHungerMessageOnServer.class,0,Side.SERVER);

	    	network.registerMessage(ServerStatHandler.class, StatPacket.class, 3, Side.SERVER);
	    	network.registerMessage(ClientStatHandler.class, StatPacket.class, 4, Side.CLIENT);
	    	//TODO: figure out how to register TileENtity
			System.out.println("PreInit Done");
			// Block registration is now under CommonEventhandler registerBlock()

//	    	GameRegistry.registerBlock(BlockQuestFRMCheck, "QuestRFMLucky");
//	    	GameRegistry.registerBlock(blockQuestChest, Names.Blocks.QUEST_CHEST);
	    	
	    	//MysteriousKey = new MysteriousKey(4770).setUnlocalizedName("MysteriousKey").setTextureName("bike:MysteriousKey");
	    }
	        
	    @EventHandler
	    public void init(FMLInitializationEvent e) {
	    	proxy.init(e);

	    	QuestEventHandler questEventHandler = new QuestEventHandler();
			FMLCommonHandler.instance().bus().register(questEventHandler);
	    	MinecraftForge.EVENT_BUS.register(questEventHandler);
	    	
	    	PedalingToBuildEventHandler pedalingToBuild = new PedalingToBuildEventHandler();
			FMLCommonHandler.instance().bus().register(pedalingToBuild);
	    	MinecraftForge.EVENT_BUS.register(pedalingToBuild);
	    	
	    	GameRegistry.registerTileEntity(TileEntityQuestChest.class, Names.TileEntities.QUEST_CHEST);
	    	
	    	//GameRegistry.registerItem(MysteriousKey, "MysteriousKey ");
	    	
	    	//MinecraftForgeClient.registerItemRenderer(BiGX.MysteriousKey, (IItemRenderer)new MysteriousKeyRenderer());
	    }
	        
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent e) {
	    	proxy.postInit(e);
	    	
	    	characterProperty = new CharacterProperty("currentPlayerLoTomProperty");
	    	StatRegistry.registerStat(characterProperty, EntityPlayer.class);
	    	System.out.println("[BiGX] Character Property Init Done");
	    	
	    	bikeProperty = new BikeProperty("currentBikeLoTomProperty");
	    	StatRegistry.registerStat(bikeProperty, EntityTank.class);
	    	System.out.println("[BiGX] Bike Property Init Done");
	    	
	    	disableMouseHelper = new MouseHelper() {
	    		@Override
	    		public void mouseXYChange() {
	    			deltaX = 0;
	    			deltaY = 0;
	    		}
	    	};
	    	
	    	
	    }
	    
	    public static BiGX instance() {
	    	return instance;
	    }
	    
	    public static void requestStatUpdate(ISyncedStat stat, Entity entity) {
	        network.sendToServer(new StatPacket(stat, entity));
	    }

	    public static void sendStatUpdate(ISyncedStat stat, Entity entity) {
	    	network.sendToDimension(new StatPacket(stat, entity), entity.world.provider.getDimension());
	    }

	    private void registerHooks() {
	        MinecraftForge.EVENT_BUS.register(new StatRegistry());
	    }
	}
