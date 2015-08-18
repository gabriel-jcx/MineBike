package org.ngs.bigx.minecraft;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

import org.ngs.bigx.minecraft.client.ClientProxy;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraft.client.Minecraft;

public class BiGXPacketHandler {
	
	public static final int START = 0x1101;
	public static final int STOP = 0x1102;
	
	public static boolean Handle(BiGXNetPacket packet) {
		/*System.out.println( "[From Server] Packet Information "
				+ "command[" + packet.commandId + "] "
				+ "SourceDevice[" + packet.sourceDevice + "] "
				+ "deviceEvent[" + packet.deviceEvent + "] "
				+ "data[" + new String(packet.data) + "]");*/
		ByteBuffer buf = ByteBuffer.wrap(packet.data,1,packet.DATALENGTH-1);
		buf.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		Context context = Main.instance().context;
		switch (packet.deviceEvent) {
			case org.ngs.bigx.dictionary.protocol.specification.dataType.ROTATIONSTATE:
				if (Minecraft.getMinecraft().thePlayer!=null) {
					int old_rotation = context.rotation;
					int new_rotation = buf.getInt();
					if (new_rotation!=context.rotation) {
						context.rotation = new_rotation;
						if (old_rotation>330&&new_rotation<30) {
							old_rotation -= 360;
						}
						double change = (new_rotation-old_rotation);
						/*if (change<0) {
							change = 0;
						}*/
						context.setSpeed( (float) Math.min( 0.4, Math.max( context.getSpeed() + change / 1000 , 0 ) ) );
					} 
				}
			break;
			case org.ngs.bigx.dictionary.protocol.specification.dataType.HEART:
				context.heartrate = buf.getInt();
			break;
			case org.ngs.bigx.dictionary.protocol.specification.dataType.ROTATE:
				break;
			case 0x1102:
				context.connected = true;
			break;
		}
		return true;
	}
	
	public static void sendPacket(BiGXNetPacket packet) {
		try {
			ClientProxy.bigxclient.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
