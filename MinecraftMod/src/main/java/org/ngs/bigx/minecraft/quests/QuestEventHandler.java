package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import noppes.npcs.entity.EntityCustomNpc;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventAttack;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventItemUse;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventNpcInteraction;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class QuestEventHandler {
	private int tickCount = 0;
	private static long duplicateAttackEventPreventorTimeStamp = 0;

	public static final int tickCountUpperLimit = 17;
	
	private static List<IQuestEventAttack> questEventAttackList;
	private static List<IQuestEventItemUse> questEventItemUseList;
	private static List<IQuestEventNpcInteraction> questEventNpcInteractionList;
	
	
	public QuestEventHandler()
	{
		questEventAttackList = new ArrayList<IQuestEventAttack>();
		questEventItemUseList = new ArrayList<IQuestEventItemUse>();
	}
	
	public static void registerQuestEventAttack(IQuestEventAttack questEventAttack)
	{
		questEventAttackList.add(questEventAttack);
	}
	
	public static void unregisterQuestEventAttack(IQuestEventAttack questEventAttack)
	{
		questEventAttackList.remove(questEventAttack);
	}
	
	public static void registerQuestEventItemUse(IQuestEventItemUse questEventItemUse)
	{
		questEventItemUseList.add(questEventItemUse);
	}
	
	public static void unregisterQuestEventItemUse(IQuestEventItemUse questEventItemUse)
	{
		questEventItemUseList.remove(questEventItemUse);
	}

	public static void registerQuestEventNpcInteraction(IQuestEventNpcInteraction questEventNpcInteraction)
	{
		questEventNpcInteractionList.add(questEventNpcInteraction);
	}
	
	public static void unregisterQuestEventNpcInteraction(IQuestEventNpcInteraction questEventNpcInteraction)
	{
		questEventNpcInteractionList.remove(questEventNpcInteraction);
	}

	@SubscribeEvent
	public void onItemUse(PlayerUseItemEvent.Start event) {
		for(IQuestEventItemUse questEventItemUse : questEventItemUseList)
		{
			questEventItemUse.onItemUse(event);
		}
	}

	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		this.tickCount++;
		
		// Every 300 ms
		if(tickCount >= tickCountUpperLimit)
		{
			tickCount = 0;
			
			if (!event.player.worldObj.isRemote)
			{
				// SERVER CODE
				// This tick will trigger to check each quest tasks of active quest
				BiGX.instance().serverContext.getQuestManager().questTick();
			}
			else
			{
				// CLIENT CODE
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
				
				for(IQuestEventAttack questEventAttack : questEventAttackList)
				{
					questEventAttack.onAttackEntityEvent(event);
				}
				
				event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "minebike:hit" + hit, 1.0f, 1.0f);
			}
		}
	}
}
