package org.ngs.bigx.minecraft.bike;

import java.io.DataInputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXConstants;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.ClientProxy;
import org.ngs.bigx.minecraft.client.gui.GuiAlchemy;
import org.ngs.bigx.minecraft.client.gui.GuiChapter;
import org.ngs.bigx.minecraft.client.skills.Skill.enumSkillState;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraft.client.Minecraft;

public class BiGXPacketHandler {
	
	public static boolean Handle(BiGXNetClient client, BiGXNetPacket packet) {
		ByteBuffer buf = ByteBuffer.wrap(packet.data,1,packet.DATALENGTH-1);
		buf.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		BigxClientContext context = (BigxClientContext) BiGX.instance().clientContext;
		
//		System.out.println("Receiving Data");

		
		switch (packet.deviceEvent) {
			case org.ngs.bigx.dictionary.protocol.Specification.DataType.HEART:
				context.heartrate = buf.getInt();
			break;
			case org.ngs.bigx.dictionary.protocol.Specification.DataType.MOVE_FORWARDBACKWARD:
				if (Minecraft.getMinecraft().thePlayer!=null) {
					int change = packet.data[1] | (packet.data[2] << 8);
					double maxSpeed = .0;
					
					if(change >= 512) {
						change -= 512;
						change *= -1;
					}
					
					boolean chasingQuestOnGoing = false;
					boolean chasingQuestOnCountDown = false;
					float speedchange = 0f;
					int speedBoostTickCountLeft = 0;
					boolean isSpeedBoostSkillOn = (context.getCurrentGameState().getSkillManager().getSkills().get(0).getSkillState() == enumSkillState.EFFECTIVE);
					
					QuestManager questManager = context.getQuestManager(); 
					
					if ((questManager != null) && (questManager.getActiveQuest() != null) )
					{
						if (questManager.getActiveQuest().getCurrentQuestTask() instanceof QuestTaskChasing)
						{
							QuestTaskChasing questTaskChasing = (QuestTaskChasing) questManager.getActiveQuest().getCurrentQuestTask();
							chasingQuestOnGoing = questTaskChasing.isChasingQuestOnGoing();
							chasingQuestOnCountDown = questTaskChasing.isChasingQuestOnCountDown();
							speedchange = questTaskChasing.getSpeedchange();
							speedBoostTickCountLeft = questTaskChasing.getSpeedBoostTickCountLeft();
						}
					}
//					 System.out.println("revceived value [" + change + "] Value that will be applied [" + ((double)change) + "]");
					
					if(Minecraft.getMinecraft().currentScreen instanceof GuiAlchemy)
					{
						GuiAlchemy.increasePedalingSpeed(change);
					}
					else
					{
						if(chasingQuestOnGoing)
						{
							if(!chasingQuestOnCountDown)
							{
								maxSpeed = QuestTaskChasing.chaseRunBaseSpeed + speedchange;
								
								if(speedBoostTickCountLeft > 0)
								{
									maxSpeed *= 1.3f;
									change *= 1.3f;
								}
								
								if(context.getSpeed() + ((double)change) >= 0){
									context.setSpeed( (float) Math.min( maxSpeed, Math.max( change * (BiGXConstants.MAXBIKESPEED / 10.0), 0 ) ) );
								}
								else{
									context.setSpeed( (float) Math.max( maxSpeed * -1, Math.min( change * (BiGXConstants.MAXBIKESPEED / 10.0), 0 ) ) );
								}
							}
							else{
								context.setSpeed(0);
							}
						}
						else
						{
							switch(ClientEventHandler.pedalingModeState)
							{
							case 0:
								context.setSpeed((float)(change * (BiGXConstants.MAXBIKESPEED / 10.0)));
								
								/**
								 * Speed Boost By Pedaling Gauge
								 */
								if(isSpeedBoostSkillOn)
								{
									context.setSpeed(context.getSpeed() * 1.3f);
								}
								break;
							case 2:
								if(change < 0)
									change *= -1;
								
								if(PedalingToBuildEventHandler.pedalingToBuild != null)
									PedalingToBuildEventHandler.pedalingToBuild.proceed(change);
								break;
							};
							
							// Increase Fuel for Combo
							{
								int fuel = change;
								if(fuel < 0)
									fuel *= -1;
								ClientEventHandler.pedalingCombo.increaseGauge(fuel);
							}
						}
					}
				}
			break;
			case org.ngs.bigx.dictionary.protocol.Specification.DataType.ROTATE:
				context.rpm = buf.getInt();
				int change = packet.data[1] | (packet.data[2] << 8);
				//if(change == 0){context.setSpeed(0);}
				break;
			case org.ngs.bigx.dictionary.protocol.Specification.DataType.TIMELAPSE_HEARTRATEREQUIREMENT:
				context.timeSpent = packet.data[1];
				context.timeSpentSmall = packet.data[2];
			break;
		}
		return true;
	}
	
	public static void sendPacket(BiGXNetClient client, BiGXNetPacket packet) {
		try {
			client.send(packet);
		} catch (Exception e) {}
	}

	public synchronized static void connect(BiGXNetClient client) throws SocketException, UnknownHostException, BiGXNetException, BiGXInternalGamePluginExcpetion
	{
		try {
			client.connect();	
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
