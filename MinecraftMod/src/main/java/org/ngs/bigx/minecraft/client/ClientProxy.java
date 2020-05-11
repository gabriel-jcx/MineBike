package org.ngs.bigx.minecraft.client;

import java.awt.Event;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.lwjgl.input.Keyboard;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonProxy;
import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;

import org.ngs.bigx.minecraft.client.skills.SkillEventHandler;
import org.ngs.bigx.minecraft.context.BigxClientContext;


import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
//import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	ClientEventHandler clientEvents;
	SkillEventHandler skillEventHandler;
	
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		BigxClientContext context = (BigxClientContext) BiGX.instance().clientContext;
		clientEvents = new ClientEventHandler(context);
		FMLCommonHandler.instance().bus().register(clientEvents);
    	MinecraftForge.EVENT_BUS.register(clientEvents);
    	skillEventHandler = new SkillEventHandler(context);
		FMLCommonHandler.instance().bus().register(skillEventHandler);
    	MinecraftForge.EVENT_BUS.register(skillEventHandler);

    	
//    	MinecraftForge.EVENT_BUS.register(new GuiStats(context,Minecraft.getMinecraft()));
//    	MinecraftForge.EVENT_BUS.register(new HudManager(Minecraft.getMinecraft()));
//    	MinecraftForge.EVENT_BUS.register(new GuiLocation(context,Minecraft.getMinecraft()));
//    	MinecraftForge.EVENT_BUS.register(new GuiStatsSkill(context,Minecraft.getMinecraft()));
//    	MinecraftForge.EVENT_BUS.register(new GuiDamage(context,Minecraft.getMinecraft()));
//    	MinecraftForge.EVENT_BUS.register(new GuiLeaderBoard(context,Minecraft.getMinecraft()));
//    	MinecraftForge.EVENT_BUS.register(new GuiMessageWindow(context,Minecraft.getMinecraft()));
    	
    	try {
			context.initBigX();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    	ClientEventHandler.keyBindingToggleMouse = new KeyBinding("Chase Quest Lock", Keyboard.KEY_P, "Bike Mod");
    	ClientEventHandler.keyBindingUseSkills = new KeyBinding("Use Skills", Keyboard.KEY_J, "Bike Mod");
    	ClientEventHandler.keyBindingSwitchSkills = new KeyBinding("Switch Skills", Keyboard.KEY_K, "Bike Mod");
//    	ClientEventHandler.keyBindingToggleBuildingGui = new KeyBinding("Building GUI", Keyboard.KEY_N, "Bike Mod");
    	ClientEventHandler.keyBindingToggleBike = new KeyBinding("Toggle Bike Input", Keyboard.KEY_MINUS, "Bike Mod");
    	ClientEventHandler.keyBindingToggleBikeToMining = new KeyBinding("Toggle Bike Mining", Keyboard.KEY_M, "Bike Mod");
    	ClientEventHandler.keyBindingToggleControllerInstructionMenu = new KeyBinding("Toggle Controller Menu", Keyboard.KEY_Q, "Bike Mod");
    	ClientEventHandler.keyBindingToggleAlchemyMenu = new KeyBinding("Toggle Alchemy Menu", Keyboard.KEY_N, "Bike Mod");

    	ClientEventHandler.keyBindingSwitchToLeftItem = new KeyBinding("Switch to the Left Item", Keyboard.KEY_X, "Bike Mod");
    	ClientEventHandler.keyBindingSwitchToRightItem = new KeyBinding("Switch to the Right Item", Keyboard.KEY_C, "Bike Mod");
    	
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleMouse);
//    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleBuildingGui);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingUseSkills);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingSwitchSkills);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleBikeToMining);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleControllerInstructionMenu);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingToggleAlchemyMenu);

    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingSwitchToLeftItem);
    	ClientRegistry.registerKeyBinding(ClientEventHandler.keyBindingSwitchToRightItem);
    	
//    	MineBikeEntityRegistry.RegisterMineBikeRenders();
//    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuestChest.class, new TileEntityQuestChestRenderer());
    }
	
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
}