package org.ngs.bigx.minecraft;
	
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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import org.ngs.bigx.input.tobiieyex.eyeTracker;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnClient;
import org.ngs.bigx.minecraft.entity.block.QuestRFMChest;
import org.ngs.bigx.minecraft.entity.item.EntityTank;
import org.ngs.bigx.minecraft.entity.item.MineBikeEntityRegistry;
import org.ngs.bigx.minecraft.entity.item.ModelTank;
import org.ngs.bigx.minecraft.entity.item.RenderTank;
import org.ngs.bigx.minecraft.entity.lotom.stat.ClientStatHandler;
import org.ngs.bigx.minecraft.entity.lotom.stat.ISyncedStat;
import org.ngs.bigx.minecraft.entity.lotom.stat.ServerStatHandler;
import org.ngs.bigx.minecraft.entity.lotom.stat.StatPacket;
import org.ngs.bigx.minecraft.entity.lotom.stat.StatRegistry;
import org.ngs.bigx.minecraft.networking.HandleHungerMessageOnServer;
import org.ngs.bigx.minecraft.networking.HandleQuestMessageOnServer;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetNullPointerException;



@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
	public class Main {

	    public static final String MODID = "bike";
	    public static final String MODNAME = "Bike Mod";
	    public static final String VERSION = "1.0.0";
	    
	    public static SimpleNetworkWrapper network;
	    
	    public static final String TEXTURE_PREFIX = "minebike";
	    
	    private static Main instance;
	    
	    public static final Block BlockQuestFRMCheck = (new QuestRFMChest(538)).setBlockName("QuestRFMLucky");
	    	    
	    public Context context;
	    
	    @Instance(Main.MODID)
	    public static Main modInstance;
	    
	    
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
			GameRegistry.registerBlock(BlockQuestFRMCheck, "QuestRFMLucky");
	    }
	        
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent e) {
	    	proxy.postInit(e);
	    }
	    
	    public static Main instance() {
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
