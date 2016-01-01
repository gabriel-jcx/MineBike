package org.ngs.bigx.minecraft;
	
import java.awt.Event;
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
import cpw.mods.fml.relauncher.Side;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import org.ngs.bigx.minecraft.networking.ReceiveQuestMessage;
import org.ngs.bigx.minecraft.networking.UpdateHungerMessage;
import org.ngs.bigx.minecraft.networking.UpdateQuestMessage;
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
	    
	    public static final String TEXTURE_PREFIX = "pinkbox";
	    
	    private static Main instance;
	    	    
	    public Context context;
	    
	    @SidedProxy(clientSide="org.ngs.bigx.minecraft.client.ClientProxy", serverSide="org.ngs.bigx.minecraft.server.ServerProxy")
		public static CommonProxy proxy;
	    
	    @EventHandler
	    public void preInit(FMLPreInitializationEvent e) {
	    	instance = this;
	    	context = new Context(this);
	    	proxy.preInit(e);
	    	network = NetworkRegistry.INSTANCE.newSimpleChannel("BikeChannel");
	    	network.registerMessage(UpdateHungerMessage.Handler.class,UpdateHungerMessage.class,0,Side.SERVER);
	    	network.registerMessage(ReceiveQuestMessage.Handler.class, ReceiveQuestMessage.class, 1, Side.CLIENT);
	    	network.registerMessage(UpdateQuestMessage.Handler.class, UpdateQuestMessage.class, 2, Side.SERVER);
	    }
	        
	    @EventHandler
	    public void init(FMLInitializationEvent e) {
	    	proxy.init(e);
	    }
	        
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent e) {
	    	proxy.postInit(e);
	    }
	    
	    public static Main instance() {
	    	return instance;
	    }
	    
	}
