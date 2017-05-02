package org.ngs.bigx.minecraft.client;

import java.awt.Event;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.lwjgl.input.Keyboard;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXPacketHandler;
import org.ngs.bigx.minecraft.CommonProxy;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistManager;
import org.ngs.bigx.minecraft.client.renderer.TileEntityQuestChestRenderer;
import org.ngs.bigx.minecraft.entity.item.EntityTank;
import org.ngs.bigx.minecraft.entity.item.MineBikeEntityRegistry;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	ClientEventHandler clientEvents;
	
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		Context context = BiGX.instance().context;
		clientEvents = new ClientEventHandler(context);
		FMLCommonHandler.instance().bus().register(clientEvents);
    	MinecraftForge.EVENT_BUS.register(clientEvents);
    	MinecraftForge.EVENT_BUS.register(new GuiStats(context,Minecraft.getMinecraft()));
    	MinecraftForge.EVENT_BUS.register(new GuiDamage(context,Minecraft.getMinecraft()));
    	MinecraftForge.EVENT_BUS.register(new GuiLeaderBoard(context,Minecraft.getMinecraft()));
    	MinecraftForge.EVENT_BUS.register(new GuiMessageWindow(context,Minecraft.getMinecraft()));
    	
    	try {
			context.initBigX();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    	ClientEventHandler.keyBindingToggleMouse = new KeyBinding("org.ngs.bigx.keyBindingToggleMouse.desc", Keyboard.KEY_P, "ChaseQuestLock");
    	ClientEventHandler.keyBindingToggleQuestListGui = new KeyBinding("org.ngs.bigx.keyBindingToggleQuestListGui.desc", Keyboard.KEY_K, "QuestListGui");
    	ClientEventHandler.keyBindingToggleBike = new KeyBinding("", Keyboard.KEY_MINUS, "ToggleBike");
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleMouse);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleQuestListGui);
    	
    	MineBikeEntityRegistry.RegisterMineBikeRenders();
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuestChest.class, new TileEntityQuestChestRenderer());
    }
	
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
//		try {
//			BiGXPacketHandler.connect(BiGX.instance().context.bigxclient);
//		} catch (SocketException e1) {
//			e1.printStackTrace();
//		} catch (UnknownHostException e1) {
//			e1.printStackTrace();
//		} catch (BiGXNetException e1) {
//			e1.printStackTrace();
//		} catch (BiGXInternalGamePluginExcpetion e1) {
//			e1.printStackTrace();
//		}
	}
	
	
	
	
}