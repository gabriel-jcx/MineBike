package org.ngs.bigx.minecraft.quests.custom.helpers;

import java.util.ArrayList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class CustomQuestEventHandler 
{	
	public CustomQuestEventHandler()
	{
		System.out.println("CustomQuestEventHandler has been registered");
	}
	
	//real class data members down here v
	
	private static ArrayList<CustomQuestAbstract> quests = new ArrayList<CustomQuestAbstract>();
	
	//adding a quest to the event handler. Effectively making it an "active" quest.
	public static void registerQuest(CustomQuestAbstract quest) 
	{
		//prevent double registering
		if (quests.contains(quest))
		{
			System.out.println("Quest already registered!!!");
		}	
		else
		{
			quests.add(quest);
		}	
	}
	
	public static void unregisterQuest(CustomQuestAbstract quest)
	{
		if (quests.contains(quest))
		{
			quests.remove(quest);
		}
		else
		{
			System.out.println("cannot unregister quest, it is not registered!");
		}
	}
	
	/*
	 * Registered quests are effectively a list of "active" quests.
	 * Any quest that is registered here is being kept track of and being updated when the 
	 */
	
	//these are our subscribed event listeners - look at minecraft forge documentation to figure these out
	//these methods all do exactly the same thing, they just call the quests respective method for certain events
	
	//Note: the reason we use the standard for loop and not an enhanced one is because it will cause ConcurrentModificationException
	//if we use an enhanced for loop and we are unregistering quests.
	
	@SubscribeEvent
	public void onItemUse(PlayerUseItemEvent.Start event)
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).onItemUse(event);
		}
	}
	
	@SubscribeEvent
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).onItemPickUp(event);
		}
	}
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).onPlayerTickEvent(event);
		}
	}
	
	public void onAttackEntityEvent(AttackEntityEvent event)
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).onAttackEntityEvent(event);
		}
	}
	
	@SubscribeEvent
	public void entityInteractEvent(EntityInteractEvent event)
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).entityInteractEvent(event);
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).onEntityJoinWorld(event);
		}
	}
	
	@SubscribeEvent
	public void onQuit(ClientDisconnectionFromServerEvent event)
	{
		for(int i = 0; i < quests.size(); i++)
		{
			quests.get(i).onQuit(event);
		}
	}
}
