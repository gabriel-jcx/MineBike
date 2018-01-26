package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventAttack;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventCheckComplete;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemPickUp;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemUse;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventRewardSession;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventRewardSession.eQuestEventRewardSessionType;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import noppes.npcs.entity.EntityCustomNpc;

public class QuestEventHandler {
	private int tickCount = 0;
	private int tickCountOneSecond = 0;
	private static long duplicateAttackEventPreventorTimeStamp = 0;

	public static final int tickCountUpperLimit = 10;
	
	private static List<IQuestEventAttack> questEventAttackList = new ArrayList<IQuestEventAttack>();
	private static List<IQuestEventItemUse> questEventItemUseList = new ArrayList<IQuestEventItemUse>();
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionList = new ArrayList<IQuestEventNpcInteraction>();
	private static List<IQuestEventCheckComplete> questEventCheckCompleteList = new ArrayList<IQuestEventCheckComplete>();
	private static List<IQuestEventItemPickUp> questEventItemPickUpList = new ArrayList<IQuestEventItemPickUp>();
	private static List<IQuestEventRewardSession> questEventRewardSessionList = new ArrayList<IQuestEventRewardSession>();
	
	private static List<IQuestEventAttack> questEventAttackListAdd = new ArrayList<IQuestEventAttack>();
	private static List<IQuestEventItemUse> questEventItemUseListAdd = new ArrayList<IQuestEventItemUse>();
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionListAdd = new ArrayList<IQuestEventNpcInteraction>();
	private static List<IQuestEventCheckComplete> questEventCheckCompleteListAdd = new ArrayList<IQuestEventCheckComplete>();
	private static List<IQuestEventItemPickUp> questEventItemPickUpListAdd = new ArrayList<IQuestEventItemPickUp>();
	private static List<IQuestEventRewardSession> questEventRewardSessionListAdd = new ArrayList<IQuestEventRewardSession>();
	
	private static List<IQuestEventAttack> questEventAttackListDel = new ArrayList<IQuestEventAttack>();
	private static List<IQuestEventItemUse> questEventItemUseListDel = new ArrayList<IQuestEventItemUse>();
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionListDel = new ArrayList<IQuestEventNpcInteraction>();
	private static List<IQuestEventCheckComplete> questEventCheckCompleteListDel = new ArrayList<IQuestEventCheckComplete>();
	private static List<IQuestEventItemPickUp> questEventItemPickUpListDel = new ArrayList<IQuestEventItemPickUp>();
	private static List<IQuestEventRewardSession> questEventRewardSessionListDel = new ArrayList<IQuestEventRewardSession>();
	
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

	public static void registerQuestEventItemPickUp(IQuestEventItemPickUp questEventItemPickUp)
	{
		synchronized (questEventItemPickUpListAdd) {
			questEventItemPickUpListAdd.add(questEventItemPickUp);
		}
	}
	
	public static void unregisterQuestEventItemPickUp(IQuestEventItemPickUp questEventItemPickUp)
	{
		synchronized (questEventItemPickUpListDel) {
			questEventItemPickUpListDel.remove(questEventItemPickUp);
		}
	}

	public static void registerQuestEventRewardSession(IQuestEventRewardSession questEventRewardSession)
	{
		synchronized (questEventRewardSessionListAdd) {
			questEventRewardSessionListAdd.add(questEventRewardSession);
		}
	}
	
	public static void unregisterQuestEventRewardSession(IQuestEventRewardSession questEventRewardSession)
	{
		synchronized (questEventRewardSessionListDel) {
			questEventRewardSessionListDel.remove(questEventRewardSession);
		}
	}
	
	public static void unregisterAllQuestEventRewardSession()
	{
		synchronized (questEventRewardSessionList) {
			for(int i=0; i<questEventRewardSessionList.size(); i++)
				questEventRewardSessionList.remove(0);
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
				if(!questEventItemUseList.contains(questEventItemUse))
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
			
			if (!event.player.worldObj.isRemote)
			{
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
							{
								if(!questEventCheckCompleteList.contains(questEventCheckComplete))
									questEventCheckCompleteList.add(questEventCheckComplete);
							}
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
					BiGX.instance().serverContext.setQuestManager(new QuestManager(null, BiGX.instance().serverContext, event.player));
				}
			}
			else
			{
				// CLIENT CODE
				if(BiGX.instance().clientContext.getQuestManager() == null)
				{
					BiGX.instance().clientContext.setQuestManager(new QuestManager(BiGX.instance().clientContext, null, event.player));
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
			boolean isVillain = false;
			
			for(String villanName : QuestTaskChasing.villainNames)
			{
				if(villanName.equals(target.display.name))
				{
					isVillain = true;
					break;
				}
			}
			
			if(isVillain)
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
						if(!questEventAttackList.contains(questEventAttack))
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
				{
					if(!questEventNpcInteractionList.contains(questEventNpcInteraction))
						questEventNpcInteractionList.add(questEventNpcInteraction);
				}
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
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		// PLAY MUSIC FOR CHASING QUEST HERE
		
		if (BiGX.instance().clientContext.getQuestManager() == null)
			return;
		
		GameSaveManager.flagGameSaveContinue = true;
		
		if (event.entity instanceof EntityPlayer && event.entity.dimension == WorldProviderFlats.dimID) {
			if (BiGX.instance().clientContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_CHASE_REG) {
				event.entity.worldObj.playSoundAtEntity(event.entity, "minebike:chasemusic", 1.0f, 1.0f);
			}
			
		}
	}
	
	@SubscribeEvent
	public void onQuit(ClientDisconnectionFromServerEvent event) {
		
		GameSaveManager.flagGameSaveContinue = false;
		
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
	
	@SubscribeEvent
	public void onItemPickUp(EntityItemPickupEvent event) // Server only event isremote is always flase
    {
		synchronized (questEventItemPickUpList) {
			for(IQuestEventItemPickUp questEventItemPickUp : questEventItemPickUpList)
			{
				questEventItemPickUp.onItemPickUp(event);
			}
		}
		synchronized (questEventItemPickUpListAdd) {
			for(IQuestEventItemPickUp questEventItemPickUp : questEventItemPickUpListAdd)
			{
				if(!questEventItemPickUpList.contains(questEventItemPickUp))
					questEventItemPickUpList.add(questEventItemPickUp);
			}
			questEventItemPickUpListAdd.clear();
		}
		synchronized (questEventItemPickUpListDel) {
			for(IQuestEventItemPickUp questEventItemPickUp : questEventItemPickUpListDel)
			{
				questEventItemPickUpList.remove(questEventItemPickUp);
			}
			questEventItemPickUpListDel.clear();
		}
    }
	
	public static void onQuestEventRewardSessionButtonClicked(eQuestEventRewardSessionType e)
	{
		switch(e)
		{
		case CONTINUE:
			synchronized (questEventRewardSessionList) {
				for(IQuestEventRewardSession questEventRewardSession : questEventRewardSessionList)
				{
					questEventRewardSession.onRewardSessionContinueClicked();
				}
			}
			break;
		case RETRY:
			synchronized (questEventRewardSessionList) {
				for(IQuestEventRewardSession questEventRewardSession : questEventRewardSessionList)
				{
					questEventRewardSession.onRewardSessionRetryClicked();
				}
			}
			break;
		case EXIT:
			synchronized (questEventRewardSessionList) {
				for(IQuestEventRewardSession questEventRewardSession : questEventRewardSessionList)
				{
					questEventRewardSession.onRewardSessionExitClicked();
				}
			}
			break;
		default:
			break;
		};
		
		synchronized (questEventRewardSessionListAdd) {
			for(IQuestEventRewardSession questEventRewardSession : questEventRewardSessionListAdd)
			{
				if(!questEventRewardSessionList.contains(questEventRewardSession))
					questEventRewardSessionList.add(questEventRewardSession);
			}
			questEventRewardSessionListAdd.clear();
		}
		synchronized (questEventRewardSessionListDel) {
			for(IQuestEventRewardSession questEventRewardSession : questEventRewardSessionListDel)
			{
				questEventRewardSessionList.remove(questEventRewardSession);
			}
			questEventRewardSessionListDel.clear();
		}
	}
}
