package com.ramon.hellow.client;

import java.nio.ByteBuffer;

import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import com.ramon.hellow.BiGXPacketHandler;
import com.ramon.hellow.CommonEventHandler;
import com.ramon.hellow.Context;
import com.ramon.hellow.Main;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ClientEventHandler {
	
		public Context context;
		
		public ClientEventHandler(Context con) {
			context = con;
		}
		
		int client_tick = 0;
		//Called whenever the client ticks
		@SubscribeEvent
		public void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getMinecraft().thePlayer!=null&&event.phase==TickEvent.Phase.END) {
				client_tick++;
				//20 ticks = 1 second
				if (client_tick==20) {
					client_tick = 0;
				}
				if (client_tick==0||client_tick==10) {
					context.bump = !context.bump;
				}
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				context.setSpeed((float) Math.max(0,context.getSpeed()-0.01));
				float moveSpeed = context.getSpeed();
				//getRotationYawHead() returns player's angle in degrees - 90
				double xt = Math.cos(Math.toRadians(p.getRotationYawHead()+90)) * moveSpeed;
				double zt = Math.sin(Math.toRadians(p.getRotationYawHead()+90)) * moveSpeed;
				p.setVelocity(xt, p.motionY, zt);
				//p.capabilities.setPlayerWalkSpeed(1-(context.resistance/16));
				Block b = p.getEntityWorld().getBlock((int) p.posX,(int) p.posY-2,(int) p.posZ);
				context.block= b;
				float new_resistance = context.resistance;
				if (b!=null) {
					if (context.resistances.containsKey(b)) {
						new_resistance = context.resistances.get(b).getResistance();
					}
				}
				if (new_resistance!=context.resistance) {
					context.resistance = new_resistance;
					ByteBuffer buf = ByteBuffer.allocate(5);
					buf.put((byte) 0x00);
					buf.putFloat(context.resistance);
					BiGXNetPacket packet = new BiGXNetPacket(BiGXPacketHandler.START, 0x0100, 0x2819, buf.array());
					BiGXPacketHandler.sendPacket(packet);
				}
			}
		}
}
