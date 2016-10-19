package org.ngs.bigx.minecraft;
	
import org.ngs.bigx.utility.Names;

import java.awt.Event;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import org.ngs.bigx.input.tobiieyex.eyeTracker;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnClient;
import org.ngs.bigx.minecraft.block.BlockQuestChest;
import org.ngs.bigx.minecraft.block.QuestRFMChest;
import org.ngs.bigx.minecraft.entity.item.EntityTank;
import org.ngs.bigx.minecraft.entity.item.ItemQuestChest;
import org.ngs.bigx.minecraft.entity.item.MineBikeEntityRegistry;
import org.ngs.bigx.minecraft.entity.lotom.CharacterProperty;
import org.ngs.bigx.minecraft.entity.lotom.BikeProperty;
import org.ngs.bigx.minecraft.entity.lotom.stat.ClientStatHandler;
import org.ngs.bigx.minecraft.entity.lotom.stat.ISyncedStat;
import org.ngs.bigx.minecraft.entity.lotom.stat.ServerStatHandler;
import org.ngs.bigx.minecraft.entity.lotom.stat.StatPacket;
import org.ngs.bigx.minecraft.entity.lotom.stat.StatRegistry;
import org.ngs.bigx.minecraft.networking.HandleHungerMessageOnServer;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnServer;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetNullPointerException;

@Mod (modid = BiGX.MODID, name = BiGX.MODNAME, version = BiGX.VERSION)

	public class BiGX {

	    public static final String MODID = "bike";
	    public static final String MODNAME = "Bike Mod";
	    public static final String VERSION = "1.0.0";
	    
	    public static SimpleNetworkWrapper network;
	    
	    public static final String TEXTURE_PREFIX = "minebike";
	    
	    private static BiGX instance;
	    
	    public static final Block BlockQuestFRMCheck = (new QuestRFMChest(538)).setBlockName("QuestRFMLucky");
	    public static final BlockQuestChest blockQuestChest = new BlockQuestChest();
	    
	    public static CharacterProperty characterProperty;
	    public static BikeProperty bikeProperty;	
	    
	    public Context context;
	    
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
	    	MineBikeEntityRegistry.RegisterMineBikeEntities();
	    	
	    	instance = this;
	    	context = new Context(this);
	    	proxy.preInit(e);
	    	network = NetworkRegistry.INSTANCE.newSimpleChannel("BikeChannel");
	    	network.registerMessage(HandleHungerMessageOnServer.Handler.class,HandleHungerMessageOnServer.class,0,Side.SERVER);
	    	network.registerMessage(HandleQuestMessageOnClient.Handler.class, HandleQuestMessageOnClient.class, 1, Side.CLIENT);
	    	network.registerMessage(HandleQuestMessageOnServer.Handler.class, HandleQuestMessageOnServer.class, 2, Side.SERVER);

	    	network.registerMessage(ServerStatHandler.class, StatPacket.class, 3, Side.SERVER);
	    	network.registerMessage(ClientStatHandler.class, StatPacket.class, 4, Side.CLIENT);
	    	
	    	GameRegistry.registerBlock(BlockQuestFRMCheck, "QuestRFMLucky");
	    	GameRegistry.registerBlock(blockQuestChest, Names.Blocks.QUEST_CHEST);
	    	
	    	try {
				context.eTracker.start();
				context.eTracker.connect();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	        
	    @EventHandler
	    public void init(FMLInitializationEvent e) {
	    	proxy.init(e);
	    	
	    	GameRegistry.registerTileEntity(TileEntityQuestChest.class, Names.TileEntities.QUEST_CHEST);
	    }
	        
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent e) {
	    	proxy.postInit(e);
	    	
	    	characterProperty = new CharacterProperty("currentPlayerLoTomPropery");
	    	StatRegistry.registerStat(characterProperty, EntityPlayer.class);
	    	System.out.println("[BiGX] Character Property Init Done");
	    	
	    	bikeProperty = new BikeProperty("currentBikeLoTomProperty");
	    	StatRegistry.registerStat(bikeProperty, EntityTank.class);
	    	System.out.println("[BiGX] Bike Property Init Done");
	    }
	    
	    public static BiGX instance() {
	    	return instance;
	    }
	    
	    public static void requestStatUpdate(ISyncedStat stat, Entity entity) {
	        network.sendToServer(new StatPacket(stat, entity));
	    }

	    public static void sendStatUpdate(ISyncedStat stat, Entity entity) {
	    	network.sendToDimension(new StatPacket(stat, entity), entity.worldObj.provider.dimensionId);
	    }

	    private void registerHooks() {
	        MinecraftForge.EVENT_BUS.register(new StatRegistry());
	    }
	}
