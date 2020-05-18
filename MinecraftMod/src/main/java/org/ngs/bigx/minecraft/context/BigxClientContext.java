package org.ngs.bigx.minecraft.context;

import java.awt.Event;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.dictionary.objects.game.BiGXGameTag;
import org.ngs.bigx.dictionary.objects.game.BiGXSuggestedGameProperties;
import org.ngs.bigx.dictionary.objects.game.GameServerList;
import org.ngs.bigx.dictionary.objects.game.GameServerStatus;
import org.ngs.bigx.dictionary.protocol.Specification.Command;
import org.ngs.bigx.input.tobiieyex.eyeTracker;
import org.ngs.bigx.input.tobiieyex.eyeTrackerListner;
import org.ngs.bigx.input.tobiieyex.eyeTrackerUDPData;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXConnectionStateManagerClass;
import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
//import org.ngs.bigx.minecraft.bike.PedalingComboSoundEffect;
//import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
//import org.ngs.bigx.minecraft.client.gui.GuiChapter;
import org.ngs.bigx.minecraft.gamestate.GameSave;
import org.ngs.bigx.minecraft.gamestate.GameSaveConfig;
import org.ngs.bigx.minecraft.gamestate.GameSaveList;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
//import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import com.google.gson.Gson;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class BigxClientContext extends BigxContext implements eyeTrackerListner {
	// CLIENT
	public static BiGXNetClient bigxclient;
	private Timer bigxclientTimer;
	private static int bigxclientConnectionTryCount = 0;
	public static final int bigxclientConnectionTryMaxCount = 10;
	private static final int bigxclientTimerTimeout = 5000;

	public String BiGXUserName;
	
	public int heartrate = 80;
	private float speed = 0;
	public int rpm = 0;
	public float resistance = 0;
	private float rotationX;		// EYETRACKING
	
	private Hashtable<Integer, byte[]> bufferQuestDesign = new Hashtable<Integer, byte[]>();
	private int bufferQuestDesignFragmentationNumber = 0;
	private int bufferQuestDesignChunkNumber = 0;
	
	private static boolean isMiddlwareIPFileAvailable = false;
	private static boolean isMiddlwareIPAvailable = false;
	private static boolean isGameSaveRead = false;
	private static boolean isBiGXConnected = false;
	private static boolean isPlayerLoadedInWorld = false;

	// This should be moved into or a specific minebike folder
	public static final String gameServerListFileName = System.getProperty("user.home") + "\\bigxGameServerList.dat";
	private static Object MiddlewareIPReadMutex = new Object();
	private static GameServerList gameServerList = null;

	private static GameSaveList gameSaveList = null;
	private static GameSave currentGameState = null;
	
//	private PedalingComboSoundEffect pedalingComboSoundEffect;
	
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
	
	public enum Resistance {
		NONE(0),LOW(1),MLOW(2),MID(3),MHIGH(4),HIGH(5);
		
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
//	public static String ipAddress = "128.195.55.237";
//	public static String ipAddress = "128.200.115.181";
//	public static String ipAddress = "192.168.0.53";
//	public static String ipAddress = "localhost";
//	public static String ipAddress = "192.168.0.161";
	public static String ipAddress = "192.168.0.24";
	public static int port = 1331;
	
	public HashMap<Block,Resistance> resistances = new HashMap<Block,Resistance>();
	public eyeTracker eTracker;
	
	public BigxClientContext(BiGX main) {
		super(main);
		
		clientSelf = this;

		// what is this freaking username
		this.BiGXUserName = "User_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
//		ClientAreaEvent.initArea();
				
//		resistances.put(Blocks.AIR, Resistance.NONE);
//		resistances.put(Blocks.BRICK_BLOCK, Resistance.LOW);
//		resistances.put(Blocks.STONE, Resistance.MLOW);
//		resistances.put(Blocks.COBBLESTONE, Resistance.MLOW);
//		resistances.put(Blocks.GRASS, Resistance.MID);
//		resistances.put(Blocks.DIRT, Resistance.MID);
//		resistances.put(Blocks.GRAVEL, Resistance.MHIGH);
//		resistances.put(Blocks.WATER, Resistance.HIGH);
//		resistances.put(Blocks.OBSIDIAN, Resistance.HIGH);
//		resistances.put(Blocks.SAND, Resistance.HIGH);
		
		resistances.put(Blocks.AIR, Resistance.NONE);
		resistances.put(Blocks.BRICK_BLOCK, Resistance.NONE);
		resistances.put(Blocks.STONE, Resistance.NONE);
		resistances.put(Blocks.COBBLESTONE, Resistance.NONE);
		resistances.put(Blocks.GRASS, Resistance.LOW);
		resistances.put(Blocks.DIRT, Resistance.LOW);
		resistances.put(Blocks.GRAVEL, Resistance.LOW);
		resistances.put(Blocks.WATER, Resistance.MLOW);
		resistances.put(Blocks.OBSIDIAN, Resistance.MLOW);
		resistances.put(Blocks.SAND, Resistance.MID);
		
//		this.pedalingComboSoundEffect = new PedalingComboSoundEffect();
		
		try {
			this.eTracker = new eyeTracker();
			this.eTracker.addEyeTrackerListener(this);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BigxContext getInstance()
	{
		return clientSelf;
	}
	
	public void connectBiGX() throws SocketException, UnknownHostException, BiGXNetException, BiGXInternalGamePluginExcpetion
	{		
		System.out.println("connectBiGX(Context.ipAddress[" + BigxClientContext.ipAddress + "], Context.port[" + BigxClientContext.port + "])");
		bigxclient = new BiGXNetClient(BigxClientContext.ipAddress, BigxClientContext.port);
		bigxclient.setReceiveListener(new BiGXNetClientListener() {
			
			@Override
			public void onMessageReceive(BiGXNetPacket packet) {
				//System.out.println("Received a packet from Middleware!!!");
				int index = 0;
				
				if(packet.commandId == Command.TX_GAME_DESIGN)
				{
					int fragmentationIndex = packet.sourceDevice; 
					int chunkIndex = packet.deviceEvent;

					// Calculate the index of the received quest design String
					index = fragmentationIndex * 256 + chunkIndex;
					
					// Push the packet data to the bufferQuestDesign
					byte[] temp = Arrays.copyOfRange(packet.data, 2, packet.data.length);
					
					if(temp == null)
					{
						System.out.println("TEMP BUFFER IS NULL");
					}
					bufferQuestDesign.put(index, temp);
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
						int chunknumberToIterate = 256;
						
						if((bufferQuestDesignFragmentationNumber-1) == j)
						{
							chunknumberToIterate = bufferQuestDesignChunkNumber;
						}
						for(i=0; i<chunknumberToIterate; i++)
						{
							idx = j*256 + i;
							
							if(!bufferQuestDesign.containsKey(idx))
							{
								downloadedQuestDesignSuccess = false;
							}
							if(bufferQuestDesign.get(idx) == null)
							{
								downloadedQuestDesignSuccess = false;
							}
						}
					}
					
					// ACK to download game design download validate
					bufferQuestDesignFragmentationNumber = 0;
					bufferQuestDesignChunkNumber = 0;
					
					byte[] tempDataRef = new byte[9];
					
					if(downloadedQuestDesignSuccess)
					{
						String questDesignString = "";
						
						tempDataRef[0] = 0x1;
						
						for(idx = 0; idx<bufferQuestDesign.size(); idx++)
						{
							questDesignString += new String(bufferQuestDesign.get(idx));
						}
						
						questDesignString = questDesignString.trim();
						
						try{
							suggestedGameProperties = new Gson().fromJson(questDesignString, BiGXSuggestedGameProperties.class);
							suggestedGamePropertiesReady = true;
							
							sendPatientProfileToServer(questDesignString);
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
					
					System.out.println("[NGS] Quest Design Downloaded.");
					
					bufferQuestDesign.clear();
				}
				else
				{
					//System.out.println("a packet fall into this else statment");
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
				BiGXNetPacket packet = new BiGXNetPacket(Command.REQ_GAME_DESIGN_HANDSHAKE,
						0, 0, new byte[9]);
				BiGXPacketHandler.sendPacket(bigxclient, packet);
			}
		});
		bigxclient.connect();
	}
	
	public static void sendGameTag(BiGXGameTag biGXGameTag) throws NumberFormatException, SocketException, UnknownHostException, BiGXNetException, BiGXInternalGamePluginExcpetion
	{
		if (bigxclient == null) {
			System.out.println("ERROR: bigxclient is null! (Are you on the wrong network?)");
			return;
		}
		bigxclient.sendGameEvent(Integer.parseInt(biGXGameTag.getTagName()), System.currentTimeMillis());
	}
	
	public boolean checkIPFile()
	{
		return (new File(gameServerListFileName)).exists();
	}
	
	public String extractMiddlewareIP() throws IOException
	{
		String returnValue = "";
		
		File initialFile = new File(gameServerListFileName);
		
	    InputStream in = new FileInputStream(initialFile);
		byte[] buffer = new byte[10240];
		in.read(buffer);
		String output = (new String(buffer)).trim();
		
		if( (output.length() != 0) && (!output.equals("{}")) )
		{
			gameServerList = new Gson().fromJson(output, GameServerList.class);
		}
		else if(output.equals("{}"))
		{
			gameServerList = new GameServerList();
		}
		
		in.close();
		
		for(GameServerStatus gameServerStatus : gameServerList.getGameserverlist())
		{
			// /
			String ip = InetAddress.getLocalHost().toString().split("/")[1];
			
			System.out.println("InetAddress.getLocalHost()[" + ip + "]");
			
			if(gameServerStatus.getIpserver().equals(ip))
			{
				returnValue = gameServerStatus.getIpclient();
				System.out.println("Middleware IP[" + gameServerStatus.getIpclient() + "]");
				break;
			}
		}
		
		return returnValue;
	}
	
	public void initBigX() throws IOException {
		currentGameState = new GameSave();
		
		GameSaveConfig gameSaveConfig = GameSaveManager.readGameSaveServerConfigFile();

//		GameSaveManager.serveraccount = gameSaveConfig.getServeraccount();
//		GameSaveManager.serverpassword = gameSaveConfig.getServerpassword();
		
		this.connectionStateManager = new BiGXConnectionStateManagerClass();
		
		// Start Timer to Connect
		// Try to connect the middleware server for 5 times
		this.bigxclientTimer = new Timer();
		bigxclientTimer.scheduleAtFixedRate(new TimerTask() 
			{
				@Override
				public void run() 
				{
					readPlayerProfile();
					
					handleGameSaveState();
				}
			},
		0, bigxclientTimerTimeout);
	}
	
	public static boolean getIsGameSaveRead()
	{
		return isGameSaveRead;
	}
	
	public void handleGameSaveState()
	{
		if(!isGameSaveRead)
		{
			if(suggestedGamePropertiesReady)
			{
				if(isPlayerLoadedInWorld)
				{
					// with case id read game save
					if(readGameSave(suggestedGameProperties.getPatientCaseId(), Minecraft.getMinecraft().player))
					{
						// if gameSave Read
						System.out.println("[BiGX] Game Save Read");
						
						isGameSaveRead = true;
						
//						if(Minecraft.getMinecraft().player.dimension == 0)
//							GuiChapter.setTodayWorkoutDone(QuestTaskChasing.getLevelSystem().getPlayerLevel() >= GuiChapter.getTargetedLevel());
					}
				}
				else
				{
					System.out.println("[BiGX] Player is not loaded.");
					
					if(Minecraft.getMinecraft().player != null)
					{
						isPlayerLoadedInWorld = true;
					}
				}
			}
		}
		else if(GameSaveManager.flagGameSaveContinue)
		{
			if(GameSaveManager.flagEnableChasingQuestClient | GameSaveManager.flagEnableChasingQuestServer)
			{
				System.out.println("[BiGX] Game Save is not loaded yet.");
				return;
			}
			
			if(Minecraft.getMinecraft().player != null)
			{
				BigxServerContext context = BiGX.instance().serverContext;
				
				if(context.suggestedGamePropertiesReady)
				{
					writeGameSave(context.suggestedGameProperties.getPatientCaseId());
				}
				
				System.out.println("[BiGX] Game Saved.");
			}
			
//			try
//			{
//				GuiChapter.setTodayWorkoutDone(QuestTaskChasing.getLevelSystem().getPlayerLevel() >= GuiChapter.getTargetedLevel());
//			}
//			catch(Exception ee)
//			{
//				ee.printStackTrace();
//			}
		}
		
	}
	
	public boolean readGameSave(String caseid, EntityPlayer player)
	{	
		try {
			GameSaveManager.loadCustomQuests(caseid);
			return GameSaveManager.readGameSaveByUserCaseId(caseid, player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void writeGameSave(String caseid)
	{
		try {
			GameSaveManager.writeGameSaveByUserCaseId(caseid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readPlayerProfile() 
	{
		if(isBiGXConnected)
			return;
		
		synchronized(MiddlewareIPReadMutex)
		{
			isMiddlwareIPFileAvailable = false;
			isMiddlwareIPAvailable = true;
			
			// CHECK the IP Address FILE
			isMiddlwareIPFileAvailable = checkIPFile();
			
			// Extract IP of Middleware
			if(isMiddlwareIPFileAvailable) {
				try {
					ipAddress = extractMiddlewareIP();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(!ipAddress.equals("")) {
					isMiddlwareIPAvailable = true;
				}
				else {
					// TODO: Need to disable this hard coded portion
					try{
						GameSaveConfig gameSaveConfig = GameSaveManager.readGameSaveServerConfigFile();
						ipAddress = gameSaveConfig.getIpbike();
					} catch(IOException e){
						e.printStackTrace();
					}
//					ipAddress = "128.195.54.79";
//					ipAddress = "128.200.115.181";
//					ipAddress = "128.195.55.199";
//					ipAddress = "192.168.0.53";
//					ipAddress = "192.168.0.161";
					isMiddlwareIPAvailable = true;
				}
			}
//			else {
//				return;
//			}
			
			// SUCCESS THEN CONNECT
			if(isMiddlwareIPAvailable) {
				System.out.println();
				try {
					connectBiGX();
					System.out.println();
					isBiGXConnected = true;
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (BiGXNetException e) {
					e.printStackTrace();
				} catch (BiGXInternalGamePluginExcpetion e) {
					e.printStackTrace();
				}
//				this.cancel();
			}
			else {
				return;
			}
		}
	}
	
	public static GameSave getCurrentGameState() {
		return currentGameState;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	private static boolean locked;
	public static void lock(boolean lock)
	{
		locked = lock;
	}
	
	public float getSpeed() {
		if (locked)
			return 0.0f;
		return speed;
	}
		
	public static GameSaveList getGameSaveList() {
		return gameSaveList;
	}

	public static void setGameSaveList(GameSaveList gameSaveList) {
		BigxClientContext.gameSaveList = gameSaveList;
	}

	@Override
	public void onMessageReceive(Event event, eyeTrackerUDPData trackerData) {
		this.rotationX = (float) trackerData.X;
		this.rotationY = (float) trackerData.Y;
	}
	
	// TODO: NEED TO REVISE THIS FUNCTION VERY UNSTABLE
	public void sendPatientProfileToServer(String questDesignString)
	{
		BigxServerContext bigxServerContext = BiGX.instance().serverContext;
		bigxServerContext.suggestedGameProperties = new Gson().fromJson(questDesignString, BiGXSuggestedGameProperties.class);
		bigxServerContext.suggestedGamePropertiesReady = true;
	}
}
