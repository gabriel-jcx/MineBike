package com.ramon.hellow.client;

import java.awt.Event;

import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import com.ramon.hellow.BiGXPacketHandler;
import com.ramon.hellow.CommonProxy;
import com.ramon.hellow.Context;
import com.ramon.hellow.Main;
import com.ramon.hellow.item.ModItems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{
	
	ClientEventHandler clientEvents;
	
	public static BiGXNetClient bigxclient;
    
    private static String ServerIPAddress = "192.168.0.100";
	private static int ServerPortNumber = 1331;
	private static int tempDeviceId = 100;
	
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		Context context = Main.instance().context;
		clientEvents = new ClientEventHandler(context);
		FMLCommonHandler.instance().bus().register(clientEvents);
    	MinecraftForge.EVENT_BUS.register(clientEvents);
    	MinecraftForge.EVENT_BUS.register(new GuiStats(context,Minecraft.getMinecraft()));
    	MinecraftForge.EVENT_BUS.register(new GuiQuest(context,Minecraft.getMinecraft()));
    }
	
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		bigxclient = new BiGXNetClient(ServerIPAddress, ServerPortNumber);
		bigxclient.setReceiveListener(new BiGXNetClientListener() {
			
			@Override
			public void onMessageReceive(Event event, BiGXNetPacket packet) {
				BiGXPacketHandler.Handle(packet);
			}
		});
		
		/**
		 * Sending a packet example from the client to the server
		 */
		//Ask for speed
		BiGXNetPacket packet = new BiGXNetPacket(BiGXPacketHandler.START, 0x0100, 0x2805, new byte[]{0});
		BiGXPacketHandler.sendPacket(packet);
		//Ask for heartrate
		packet = new BiGXNetPacket(BiGXPacketHandler.START, 0x0100, 0x2005, new byte[]{0});
		BiGXPacketHandler.sendPacket(packet);
		
	}

}