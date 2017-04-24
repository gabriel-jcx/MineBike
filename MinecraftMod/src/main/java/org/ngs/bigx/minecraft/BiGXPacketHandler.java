package org.ngs.bigx.minecraft;

import java.io.DataInputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.ClientProxy;
import org.ngs.bigx.minecraft.quests.QuestEventChasing;
import org.ngs.bigx.minecraft.quests.QuestEventChasingFire;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraft.client.Minecraft;

public class BiGXPacketHandler {
	
	public static boolean Handle(BiGXNetClient client, BiGXNetPacket packet) {
		ByteBuffer buf = ByteBuffer.wrap(packet.data,1,packet.DATALENGTH-1);
		buf.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		Context context = BiGX.instance().context;
		
//		System.out.println("Receiving Data");

		
		switch (packet.deviceEvent) {
			case org.ngs.bigx.dictionary.protocol.Specification.DataType.ROTATIONSTATE:
				if (Minecraft.getMinecraft().thePlayer!=null) {
					int old_rotation = context.rotation;
					int new_rotation = buf.getInt();
					if (new_rotation!=context.rotation) {
						context.rotation = new_rotation;
						if (old_rotation>330&&new_rotation<30) {
							old_rotation -= 360;
						}
						double change = (new_rotation-old_rotation);

						context.setSpeed( (float) Math.min( BiGXConstants.MAXBIKESPEED, Math.max( context.getSpeed() + change / 1000 , 0 ) ) );
					} 
				}
			break;
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
					
					if ((ClientEventHandler.getHandler().questDemo != null) && (ClientEventHandler.getHandler().questDemo.getQuest() != null) )
					{
						if (ClientEventHandler.getHandler().questDemo.getQuest().getCurrentQuestEvent() instanceof QuestEventChasing)
						{
							chasingQuestOnGoing = ((QuestEventChasing)ClientEventHandler.getHandler().questDemo.getQuest().getCurrentQuestEvent()).chasingQuestOnGoing;
							chasingQuestOnCountDown = ((QuestEventChasing)ClientEventHandler.getHandler().questDemo.getQuest().getCurrentQuestEvent()).chasingQuestOnCountDown;
							speedchange = QuestEventChasing.speedchange;
						}
						else if (ClientEventHandler.getHandler().questDemo.getQuest().getCurrentQuestEvent() instanceof QuestEventChasingFire)
						{
							chasingQuestOnGoing = ((QuestEventChasingFire)ClientEventHandler.getHandler().questDemo.getQuest().getCurrentQuestEvent()).chasingQuestOnGoing;
							chasingQuestOnCountDown = ((QuestEventChasingFire)ClientEventHandler.getHandler().questDemo.getQuest().getCurrentQuestEvent()).chasingQuestOnCountDown;
							speedchange = QuestEventChasingFire.speedchange;
						}
					}
//					 System.out.println("revceived value [" + change + "] Value that will be applied [" + ((double)change) + "]");
					
					if(chasingQuestOnGoing)
					{
						if(!chasingQuestOnCountDown)
						{
							maxSpeed = QuestEventChasing.chaseRunBaseSpeed + speedchange;
							
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
						context.setSpeed((float)(change * (BiGXConstants.MAXBIKESPEED / 10.0)));
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
