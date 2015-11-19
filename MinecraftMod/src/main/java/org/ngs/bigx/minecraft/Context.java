package org.ngs.bigx.minecraft;

import java.awt.Event;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class Context {
	public BiGXNetClient bigxclient;
	public String BiGXUserName;
	public int heartrate = 80;
	private float speed = 0;
	public float resistance = 0;
	public boolean bump = false;
	public Block block = Blocks.air;
	public int rotation = 0;
	public Main main = null;
	private Quest currentQuest = null;
	private boolean questPopupShown = true;
	private int ID = 0;
	private boolean questsEnabled = true;
	public Map<EntityPlayerMP,Quest> currentQuests;
	
	public int timeSpent = 0;
	public int timeSpentSmall = 0;
	
	public boolean modEnabled = true;
	
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
	
	public Context(Main main) {
		this.main = main;
		this.BiGXUserName = "User_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		resistances.put(Blocks.air, Resistance.NONE);
		resistances.put(Blocks.ice, Resistance.LOW);
		resistances.put(Blocks.stone, Resistance.MLOW);
		resistances.put(Blocks.cobblestone, Resistance.MLOW);
		resistances.put(Blocks.grass, Resistance.MID);
		resistances.put(Blocks.dirt, Resistance.MID);
		resistances.put(Blocks.sand, Resistance.MHIGH);
		resistances.put(Blocks.gravel, Resistance.MHIGH);
		resistances.put(Blocks.water, Resistance.HIGH);
		
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
	
	public void setQuest(Quest quest) {
		this.currentQuest = quest;
		questPopupShown = false;
	}
	
	public Quest getQuest() {
		return currentQuest;
	}
	
	public Boolean hasQuestPopupShown() {
		return questPopupShown;
	}
	
	public void showQuestPopup() {
		questPopupShown = true;
	}
		
	public int getID() {
		ID++;
		return ID;
	}
		
	public boolean checkQuestsEnabled() {
		return questsEnabled;
	}
}
