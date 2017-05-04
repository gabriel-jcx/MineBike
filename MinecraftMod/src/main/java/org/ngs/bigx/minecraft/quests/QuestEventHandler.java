package org.ngs.bigx.minecraft.quests;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import noppes.npcs.entity.EntityCustomNpc;

import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class QuestEventHandler {
	
	private static long duplicateAttackEventPreventorTimeStamp = 0;
	
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
//				System.out.println("[BiGX] Interact with the Thief HP["+CommonEventHandler.getTheifHealthCurrent()+"/"+CommonEventHandler.getTheifHealthMax()+"] Lv["+CommonEventHandler.getTheifLevel()+"]");
				if(ClientEventHandler.getHandler().playerQuestManager.getActiveQuestTask() instanceof QuestTaskChasing)
				{
					QuestTaskChasing questTaskChasing = (QuestTaskChasing) ClientEventHandler.getHandler().playerQuestManager.getActiveQuestTask();
					
					if (event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] == null)
						questTaskChasing.deductThiefHealth(null);
					else
						questTaskChasing.deductThiefHealth(event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem].getItem());
				}
				else if(ClientEventHandler.getHandler().playerQuestManager.getActiveQuestTask() instanceof QuestTaskChasingFire)
				{
					QuestTaskChasingFire questTaskChasingFire = (QuestTaskChasingFire) ClientEventHandler.getHandler().playerQuestManager.getActiveQuestTask();
					
					if (event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] == null)
						questTaskChasingFire.deductThiefHealth(null);
					else
						questTaskChasingFire.deductThiefHealth(event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem].getItem());
				}
				
//				event.entityPlayer.worldObj.playSoundEffect(event.entityPlayer.posX + 0.5D, event.entityPlayer.posY + 0.5D, event.entityPlayer.posZ + 0.5D, "minebike:sounds/hit1", 1.0f, 1.0f);
				event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "minebike:hit" + hit, 1.0f, 1.0f);
			}
		}
	}
}
