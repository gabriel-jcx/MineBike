package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.entity.EntityCustomNpc;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.npcs.NpcEvents;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventAttack;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventCheckComplete;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemUse;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class QuestEventHandler {
	private int tickCount = 0;
	private int tickCountOneSecond = 0;
	private static long duplicateAttackEventPreventorTimeStamp = 0;

	public static final int tickCountUpperLimit = 10;
	
	private static List<IQuestEventAttack> questEventAttackList = new ArrayList<IQuestEventAttack>();
	private static List<IQuestEventItemUse> questEventItemUseList = new ArrayList<IQuestEventItemUse>();
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionList = new ArrayList<IQuestEventNpcInteraction>();
	private static List<IQuestEventCheckComplete> questEventCheckCompleteList = new ArrayList<IQuestEventCheckComplete>();
	
	private static List<IQuestEventAttack> questEventAttackListAdd = new ArrayList<IQuestEventAttack>();
	private static List<IQuestEventItemUse> questEventItemUseListAdd = new ArrayList<IQuestEventItemUse>();
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionListAdd = new ArrayList<IQuestEventNpcInteraction>();
	private static List<IQuestEventCheckComplete> questEventCheckCompleteListAdd = new ArrayList<IQuestEventCheckComplete>();
	
	private static List<IQuestEventAttack> questEventAttackListDel = new ArrayList<IQuestEventAttack>();
	private static List<IQuestEventItemUse> questEventItemUseListDel = new ArrayList<IQuestEventItemUse>();
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionListDel = new ArrayList<IQuestEventNpcInteraction>();
	private static List<IQuestEventCheckComplete> questEventCheckCompleteListDel = new ArrayList<IQuestEventCheckComplete>();
	
	public QuestEventHandler()
	{
	}
	
	public static void registerQuestEventAttack(IQuestEventAttack questEventAttack)
	{
		synchronized (questEventAttackListAdd) {
			questEventAttackListAdd.add(questEventAttack);
		}
	}
	
	public static void unregisterQuestEventAttack(IQuestEventAttack questEventAttack)
	{
		synchronized (questEventAttackListDel) {
			questEventAttackListDel.remove(questEventAttack);
		}
	}
	
	public static void registerQuestEventItemUse(IQuestEventItemUse questEventItemUse)
	{
		synchronized (questEventItemUseListAdd) {
			questEventItemUseListAdd.add(questEventItemUse);
		}
	}
	
	public static void unregisterQuestEventItemUse(IQuestEventItemUse questEventItemUse)
	{
		synchronized (questEventItemUseListDel) {
			questEventItemUseListDel.remove(questEventItemUse);
		}
	}

	public static void registerQuestEventNpcInteraction(IQuestEventNpcInteraction questEventNpcInteraction)
	{
		synchronized (questEventNpcInteractionListAdd) {
			questEventNpcInteractionListAdd.add(questEventNpcInteraction);
		}
	}
	
	public static void unregisterQuestEventNpcInteraction(IQuestEventNpcInteraction questEventNpcInteraction)
	{
		synchronized (questEventNpcInteractionListDel) {
			questEventNpcInteractionListDel.remove(questEventNpcInteraction);
		}
	}
	
	public static void registerQuestEventCheckComplete(IQuestEventCheckComplete questEventCheckComplete)
	{
		synchronized (questEventCheckCompleteListAdd) {
			questEventCheckCompleteListAdd.add(questEventCheckComplete);
		}
	}
	
	public static void unregisterQuestEventCheckComplete(IQuestEventCheckComplete questEventCheckComplete)
	{
		synchronized (questEventCheckCompleteListDel) {
			questEventCheckCompleteListDel.remove(questEventCheckComplete);
		}
	}

	@SubscribeEvent
	public void onItemUse(PlayerUseItemEvent.Start event) {
		synchronized (questEventItemUseList) {
			for(IQuestEventItemUse questEventItemUse : questEventItemUseList)
			{
				questEventItemUse.onItemUse(event);
			}
		}
		synchronized (questEventItemUseListAdd) {
			for(IQuestEventItemUse questEventItemUse : questEventItemUseListAdd)
			{
				questEventItemUseList.add(questEventItemUse);
			}
			questEventItemUseListAdd.clear();
		}
		synchronized (questEventItemUseListDel) {
			for(IQuestEventItemUse questEventItemUse : questEventItemUseListDel)
			{
				questEventItemUseList.remove(questEventItemUse);
			}
			questEventItemUseListDel.clear();
		}
	}

	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		this.tickCount++;
		this.tickCountOneSecond++;
		
		if(tickCountOneSecond >= 50)
		{
			tickCountOneSecond = 0;

			if(questEventCheckCompleteList != null)
			{
				synchronized (questEventCheckCompleteList) {
					for(IQuestEventCheckComplete questEventCheckComplete : questEventCheckCompleteList)
					{
						if(questEventCheckComplete != null)
							questEventCheckComplete.onCheckCompleteEvent();
					}
				}
				synchronized (questEventCheckCompleteListAdd) {
					for(IQuestEventCheckComplete questEventCheckComplete : questEventCheckCompleteListAdd)
					{
						if(questEventCheckComplete != null)
							questEventCheckCompleteList.add(questEventCheckComplete);
					}
					questEventCheckCompleteListAdd.clear();
				}
				synchronized (questEventCheckCompleteListDel) {
					for(IQuestEventCheckComplete questEventCheckComplete : questEventCheckCompleteListDel)
					{
						if(questEventCheckComplete != null)
							questEventCheckCompleteList.remove(questEventCheckComplete);
					}
					questEventCheckCompleteListDel.clear();
				}
			}
		}
			
		// Every 200 ms
		if(tickCount >= tickCountUpperLimit)
		{
			tickCount = 0;
			
			if (!event.player.worldObj.isRemote)
			{
				// SERVER CODE
				if(BiGX.instance().serverContext.getQuestManager() != null)
				{
					// This tick will trigger to check each quest tasks of active quest
					BiGX.instance().serverContext.getQuestManager().questTick();	
				}
				else
				{
					BiGX.instance().serverContext.setQuestManager(new QuestManager(BiGX.instance().serverContext, event.player));
				}
			}
			else
			{
				// CLIENT CODE
				if(BiGX.instance().clientContext.getQuestManager() == null)
				{
					BiGX.instance().clientContext.setQuestManager(new QuestManager(BiGX.instance().clientContext, event.player));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onAttackEntityEvent(AttackEntityEvent event) {
		EntityCustomNpc target;

		event.target.getEntityId();
		if(event.target.getClass().getName().equals("noppes.npcs.entity.EntityCustomNpc"))
		{
			target = (EntityCustomNpc)event.target;
			
			if(target.display.name.equals("Thief"))
			{
				if( (System.currentTimeMillis() - duplicateAttackEventPreventorTimeStamp) < 100 )
					return;
				else
					duplicateAttackEventPreventorTimeStamp = System.currentTimeMillis();
				
				Random r = new Random();
				int hit = r.nextInt(4)+1;

				synchronized (questEventAttackList) {
					for(IQuestEventAttack questEventAttack : questEventAttackList)
					{
						questEventAttack.onAttackEntityEvent(event);
					}
				}
				synchronized (questEventAttackListAdd) {
					for(IQuestEventAttack questEventAttack : questEventAttackListAdd)
					{
						questEventAttackList.add(questEventAttack);
					}
					questEventAttackListAdd.clear();
				}
				synchronized (questEventAttackListDel) {
					for(IQuestEventAttack questEventAttack : questEventAttackListDel)
					{
						questEventAttackList.remove(questEventAttack);
					}
					questEventAttackListDel.clear();
				}
				
				event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "minebike:hit" + hit, 1.0f, 1.0f);
			}
		}
	}
	
	@SubscribeEvent
	public void entityInteractEvent(EntityInteractEvent e) {
		synchronized (questEventNpcInteractionList) {
			for(IQuestEventNpcInteraction questEventNpcInteraction : questEventNpcInteractionList)
			{
				if(questEventNpcInteraction != null)
					questEventNpcInteraction.onNpcInteraction(e);;
			}
		}
		synchronized (questEventNpcInteractionListAdd) {
			for(IQuestEventNpcInteraction questEventNpcInteraction : questEventNpcInteractionListAdd)
			{
				if(questEventNpcInteraction != null)
					questEventNpcInteractionList.add(questEventNpcInteraction);
			}
			questEventNpcInteractionListAdd.clear();
		}
		synchronized (questEventNpcInteractionListDel) {
			for(IQuestEventNpcInteraction questEventNpcInteraction : questEventNpcInteractionListDel)
			{
				if(questEventNpcInteraction != null)
					questEventNpcInteractionList.remove(questEventNpcInteraction);
			}
			questEventNpcInteractionListDel.clear();
		}
	}
	
	@SubscribeEvent
	public void onQuit(ClientDisconnectionFromServerEvent event) {
		System.out.println("onQuit(ClientDisconnectionFromServerEvent event)");
		
		if(BiGX.instance().serverContext == null)
			return;
		
		if(BiGX.instance().serverContext.getQuestManager() == null)
			return;
		
		try {
			BiGX.instance().serverContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_NONE);
		} catch (QuestException e) {
			e.printStackTrace();
		}
		
		
		if(BiGX.instance().clientContext == null)
			return;
		
		
		if(BiGX.instance().clientContext.getQuestManager() == null)
			return;
		
		
		try {
			BiGX.instance().clientContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_NONE);
		} catch (QuestException e) {
			e.printStackTrace();
		}
	}
}
