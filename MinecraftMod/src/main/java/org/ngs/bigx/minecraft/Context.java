package org.ngs.bigx.minecraft;

import java.awt.Event;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Spliterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.ngs.bigx.input.tobiieyex.eyeTracker;
import org.ngs.bigx.input.tobiieyex.eyeTrackerListner;
import org.ngs.bigx.input.tobiieyex.eyeTrackerUDPData;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestEvent;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class Context implements eyeTrackerListner {
	public BiGXNetClient bigxclient;
	public String BiGXUserName;
	public int heartrate = 80;
	private float speed = 0;
	public float resistance = 0;
	public boolean bump = false;
	public Block block = Blocks.air;
	public int rotation = 0;
	public Main main = null;
	private int ID = 0;
	private boolean questsEnabled = true;
	private float rotationX;
	public Queue<QuestEvent> questEventQueue;
	
	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	private float rotationY;
	
	public int timeSpent = 0;
	public int timeSpentSmall = 0;
	
	public boolean modEnabled = true;
	
	public QuestManager questManager;
	
	public enum Resistance {
		NONE(0),LOW(2),MLOW(3),MID(4),MHIGH(5),HIGH(7);
		
		private float resistance;
		
		Resistance(float res) {
			resistance = res;
		}
		
		public float getResistance() {
			return resistance;
		}
	}
	
	/*
	 * BiGX Related Members
	 */
	public BiGXConnectionStateManagerClass connectionStateManager;
	public static String ipAddress = "localhost";
	public static int port = 1331;
	
	public HashMap<Block,Resistance> resistances = new HashMap<Block,Resistance>();
	public eyeTracker eTracker;
	
	public Context(Main main) {
		this.main = main;
		this.BiGXUserName = "User_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		this.questEventQueue = new LinkedList<QuestEvent>(); 
				
		resistances.put(Blocks.air, Resistance.NONE);
		resistances.put(Blocks.ice, Resistance.LOW);
		resistances.put(Blocks.stone, Resistance.MLOW);
		resistances.put(Blocks.cobblestone, Resistance.MLOW);
		resistances.put(Blocks.grass, Resistance.MID);
		resistances.put(Blocks.dirt, Resistance.MID);
		resistances.put(Blocks.sand, Resistance.MHIGH);
		resistances.put(Blocks.gravel, Resistance.MHIGH);
		resistances.put(Blocks.water, Resistance.HIGH);
		
		questManager = new QuestManager();
		
		try {
			this.eTracker = new eyeTracker();
			this.eTracker.addEyeTrackerListener(this);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initBigX() {
		this.connectionStateManager = new BiGXConnectionStateManagerClass();
		this.bigxclient = new BiGXNetClient(Context.ipAddress, Context.port);
		this.bigxclient.setReceiveListener(new BiGXNetClientListener() {
			
			@Override
			public void onMessageReceive(Event event, BiGXNetPacket packet) {
				BiGXPacketHandler.Handle(bigxclient, packet);
			}
			
			@Override
			public void onConnectedMessageReceive(Event event) {
				System.out.println("This MC is connected to BiGX Game Controller");
			}
		});
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	
		
	public int getID() {
		ID++;
		return ID;
	}
		
	public boolean checkQuestsEnabled() {
		return questsEnabled;
	}
	
	public void unloadWorld() {
		questManager.unloadWorld();
	}

	@Override
	public void onMessageReceive(Event event, eyeTrackerUDPData trackerData) {
		this.rotationX = (float) trackerData.X;
		this.rotationY = (float) trackerData.Y;
	}
}
