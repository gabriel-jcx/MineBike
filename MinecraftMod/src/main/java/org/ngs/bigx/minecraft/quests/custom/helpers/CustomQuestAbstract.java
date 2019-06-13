package org.ngs.bigx.minecraft.quests.custom.helpers;

import org.ngs.bigx.minecraft.gamestate.CustomQuestJson;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public abstract class CustomQuestAbstract 
{	
	public static EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	
	protected int progress;
	protected String name;
	protected boolean completed;
	protected boolean started;
	
	//
	public CustomQuestAbstract()
	{
		
	}
	
	public void loadFromJson(CustomQuestJson json)
	{
		progress = json.getProgress();
		name = json.getName();
		completed = json.getCompleted();
		started = json.getStarted();
	}
	
	public void register()
	{
		GameSaveManager.registerQuest(this);
	}
	
	//methods below are meant to be called with super
	
	//the quest is registered upon starting
	public void start()
	{
		started = true;
		CustomQuestEventHandler.registerQuest(this);
	}
	
	public int getProgress()
	{
		return progress;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public boolean isComplete()
	{
		return completed;
	}
	
	public void complete()
	{
		completed = true;
		CustomQuestEventHandler.unregisterQuest(this);
	}
	
	
	//methods below are meant to be overridden
	
	//EVENT HANDLING METHODS, they do nothing by default in the abstract class
	//override them in a sub class to add functionality!
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{

	}
	
	public void onItemUse(PlayerUseItemEvent.Start event)
	{
		
	}
	
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		
	}
	
	public void onAttackEntityEvent(AttackEntityEvent event)
	{
	
	}
	
	public void entityInteractEvent(EntityInteractEvent event)
	{
		
	}
	
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		
	}
	
	public void onQuit(ClientDisconnectionFromServerEvent event)
	{
		
	}
}
