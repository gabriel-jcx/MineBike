	package com.ramon.hellow;

	
import java.awt.Event;

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
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetNullPointerException;

import com.ramon.hellow.networking.ReceiveQuestMessage;
import com.ramon.hellow.networking.UpdateHungerMessage;


@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
	public class Main {

	    public static final String MODID = "bike";
	    public static final String MODNAME = "Bike Mod";
	    public static final String VERSION = "1.0.0";
	    
	    public static SimpleNetworkWrapper network;
	    
	    public static final String TEXTURE_PREFIX = "pinkbox";
	    
	    private static Main instance;
	    
	    public static WorldGen worldGen = new WorldGen();
	    
	    public Context context;
	    
	    @SidedProxy(clientSide="com.ramon.hellow.client.ClientProxy", serverSide="com.ramon.hellow.server.ServerProxy")
		public static CommonProxy proxy;
	    
	    @EventHandler
	    public void preInit(FMLPreInitializationEvent e) {
	    	instance = this;
	    	context = new Context(this);
	    	proxy.preInit(e);
	    	System.out.println("\n\n\n");
	    	System.out.println(e.getClass() + "");
	    	System.out.println("\n\n\n");
	    	network = NetworkRegistry.INSTANCE.newSimpleChannel("BikeChannel");
	    	network.registerMessage(UpdateHungerMessage.Handler.class,UpdateHungerMessage.class,0,Side.SERVER);
	    	network.registerMessage(ReceiveQuestMessage.Handler.class, ReceiveQuestMessage.class, 1, Side.CLIENT);
	    }
	        
	    @EventHandler
	    public void init(FMLInitializationEvent e) {
	    	proxy.init(e);
	    	System.out.println("\n\n\n");
	    	System.out.println(e.getClass() + "");
	    	System.out.println("\n\n\n");
	    	GameRegistry.registerWorldGenerator(worldGen,10);
	    }
	        
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent e) {
	    	proxy.postInit(e);
	    	System.out.println("\n\n\n");
	    	System.out.println(e.getClass() + "");
	    	System.out.println("\n\n\n");
	    }
	    
	    public static Main instance() {
	    	return instance;
	    }
	    
	}
