package org.ngs.bigx.minecraft;

import java.awt.Event;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
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

import org.ngs.bigx.dictionary.objects.game.BiGXSuggestedGameProperties;
import org.ngs.bigx.dictionary.protocol.Specification;
import org.ngs.bigx.input.tobiieyex.eyeTracker;
import org.ngs.bigx.input.tobiieyex.eyeTrackerListner;
import org.ngs.bigx.input.tobiieyex.eyeTrackerUDPData;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestEvent;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.dictionary.protocol.Specification.Command;

import com.google.gson.Gson;

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
	public BiGX main = null;
	private int ID = 0;
	private boolean questsEnabled = true;
	private float rotationX;
	
	// TODO: Buffer instance to add the packet data in.
	private Hashtable<Integer, byte[]> bufferQuestDesign = new Hashtable<Integer, byte[]>();
	private int bufferQuestDesignFragmentationNumber = 0;
	private int bufferQuestDesignChunkNumber = 0;
	private BiGXSuggestedGameProperties suggestedGameProperties = null;
	private boolean suggestedGamePropertiesReady = false;
	
	public Queue<QuestEvent> questEventQueue;
	
	/* TODO: Need to be removed before production
	 * SHOE TESTING
	 */
	public float shoeEnergy = 0;
	public boolean isSubtleModeOn = false;
	///// END OF SHOE TESTING
	
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
	
	public Context(BiGX main) {
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
			public void onMessageReceive(BiGXNetPacket packet) {
				int index = 0;
				
				if(packet.commandId == Command.TX_GAME_DESIGN)
				{
					int fragmentationIndex = packet.sourceDevice; 
					int chunkIndex = packet.deviceEvent;

					// Calculate the index of the received quest design String
					index = fragmentationIndex * 256 + chunkIndex;
					
					// Push the packet data to the bufferQuestDesign
					bufferQuestDesign.put(index, Arrays.copyOfRange(packet.data, 2, packet.data.length-1));
				}
				else if(packet.commandId == Command.ACK_GAME_DESIGN_HANDSHAKE)
				{
					// Assign the  bufferQuestDesignFragmentationNumber, bufferQuestDesignChunkNumber
					bufferQuestDesignFragmentationNumber = packet.sourceDevice;
					bufferQuestDesignChunkNumber = packet.deviceEvent;
				}
				else if(packet.commandId == Command.REQ_GAME_DESIGN_DOWNLOAD_VALIDATE)
				{
					int i,j,idx=0;
					boolean downloadedQuestDesignSuccess = true;
					
					for(j=0; j<bufferQuestDesignFragmentationNumber; j++)
					{
						for(i=0; i<bufferQuestDesignChunkNumber; i++)
						{
							idx = j*256 + i;
							
							if(!bufferQuestDesign.containsKey(idx))
							{
								downloadedQuestDesignSuccess = false;
							}
						}
					}
					
					// ACK to download game design download validate
					bufferQuestDesign.clear();
					bufferQuestDesignFragmentationNumber = 0;
					bufferQuestDesignChunkNumber = 0;
					
					byte[] tempDataRef = new byte[9];
					
					if(downloadedQuestDesignSuccess)
					{
						String questDesignString = "";
						
						tempDataRef[0] = 0x1;
						
						for(idx = 0; idx<bufferQuestDesign.size(); idx++)
						{
							questDesignString += Arrays.toString(bufferQuestDesign.get(idx));
						}
						
						try{
							Gson gson = new Gson();
							suggestedGameProperties = new Gson().fromJson(questDesignString, BiGXSuggestedGameProperties.class);
							suggestedGamePropertiesReady = true;
						}
						catch (Exception ee){
							suggestedGameProperties = null;
							suggestedGamePropertiesReady = false;
							ee.printStackTrace();
						}
					}
					else
					{
						tempDataRef[0] = 0x0;
						suggestedGamePropertiesReady = false;
					}
					
					packet = new BiGXNetPacket(Command.ACK_GAME_DESIGN_DOWNLOAD_VALIDATE, 
							0, 0, tempDataRef);
					BiGXPacketHandler.sendPacket(bigxclient, packet);
				}
				else
				{
					BiGXPacketHandler.Handle(bigxclient, packet);
				}
			}
			
			@Override
			public void onConnectedMessageReceive() {
				System.out.println("This MC is connected to BiGX Game Controller");
				
				// Prepare & Initialize the buffer to cache the received quest data
				bufferQuestDesign.clear();
				bufferQuestDesignFragmentationNumber = 0;
				bufferQuestDesignChunkNumber = 0;
				
				// Request Quest Design
				BiGXNetPacket packet = new BiGXNetPacket(org.ngs.bigx.dictionary.protocol.Specification.Command.REQ_GAME_DESIGN_HANDSHAKE, 
						0, 0, new byte[9]);
				BiGXPacketHandler.sendPacket(bigxclient, packet);
			}
		});
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void increaseShoeEnergy(float shoeEnergy) {
		this.shoeEnergy += shoeEnergy;
		
		if( (this.shoeEnergy > 500) )
		{
			this.shoeEnergy = 500;
		}
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
