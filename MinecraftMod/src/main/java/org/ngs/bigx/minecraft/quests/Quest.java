package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.client.Textbox;
import org.ngs.bigx.minecraft.quests.QuestEvent.eventType;
import org.ngs.bigx.minecraft.quests.QuestStateManager.State;
import org.ngs.bigx.minecraft.quests.QuestStateManager.Trigger;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderQuests;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Quest implements QuestStateManagerListener{
	public HashMap<String,QuestPlayer> players;
	private boolean worldExists = false;
	private QuestStateManager stateManager;
	protected WorldServer questWorld;
	protected int questWorldX=0;
	protected int questWorldY=64;
	protected int questWorldZ=0;
	protected Timer questTimer;
	protected World originalWorld = null;
	private TimerTask questPeriodicTimerTask;
	private TimerTask questCountdownTimerTask;
	protected TimerTask questAccomplishTimerTask;
	private boolean teleportRequired = false;

	protected int secondsRemainingToStart = 5;
	protected int secondsRemainingToEnd;
	protected int timeLimit;
	
	protected abstract void setRemainingToEndVar();
	
	private int ID;

	public boolean isTeleportRequired() {
		return teleportRequired;
	}

	public void setTeleportRequired(boolean teleportRequired) {
		this.teleportRequired = teleportRequired;
	}
	
	public Quest(int ID) throws Exception {
		this.ID = ID;
		players = new HashMap<String,QuestPlayer>();
		stateManager = new QuestStateManager(this);
		questTimer = new Timer();
		questPeriodicTimerTask = new TimerTask(){
			@Override
			public void run() {
				if(stateManager.getQuestState() == State.WaitToStart)
				{
					secondsRemainingToStart--;
					if (secondsRemainingToStart==0) {
						start();
					}
				}
				else if(stateManager.getQuestState() == State.QuestInProgress)
				{
					secondsRemainingToEnd --;
					// TODO: Update the Quest Count Down for accomplishment
					if (secondsRemainingToEnd<=0) {
						fail();
					}
				}
			}
		};
		questAccomplishTimerTask = new TimerTask() {
			@Override
			public void run () {
				// TODO: End the quest
				
			}
		};
	}
	
	public void addPlayer(String playerName,Context context) {
		QuestPlayer player = new QuestPlayer(playerName,getPlayerEntity(playerName));
		players.put(playerName,player);
		context.questManager.playerQuestsMapping.put(playerName,this);
		System.out.println("Quest count: "+context.questManager.playerQuestsMapping.size());
//		this.notification();
	}
	
	public void addPlayers(List<String> players,Context context) {
		for (String player:players) {
			addPlayer(player,context);
		}
	}
	
	public void returnPlayer(String playerName) {
		QuestPlayer player = players.get(playerName);
		
		if (player==null) return;
		if (!this.isTeleportRequired()) return;
		
		new QuestTeleporter(questWorld).teleport(player.getEntity(), player.getWorld(),(int) player.posX,(int) player.posY,(int) player.posZ);
	}
	
	public void notification()
	{
		try {
			this.stateManager.triggerQuestTransition(Trigger.NotifyQuest);
			
			if(this.isServerSide())
			{
				// TODO: SEND A NOTIFICATION TO CLIENTS!
				System.out.println("[BIGX] NotifyQuestPlayers");
				Main.instance().context.questEventQueue.add(new QuestEvent(this, eventType.NotifyQuestPlayers));
			}
		} catch (Exception e) {
			System.out.println("The quest state is not in a right state.");
			e.printStackTrace();
		}
	}
	
	/***
	 * The function completes the current on going quest when the quest was done successfully.
	 */
	public void complete() {
		try {
			this.stateManager.triggerQuestTransition(Trigger.SuccessQuest);			
		} catch (Exception e) {
			System.out.println("The quest state is not in a right state.");
			e.printStackTrace();
		}
	}
	
	public void load()
	{
		System.out.println("load()");
		try {
			//this.stateManager.triggerQuestTransition(Trigger.AcceptQuestAndTeleport);
		} catch (Exception e) {
			System.out.println("The quest state is not in a right state.");
			e.printStackTrace();
		}

		setRemainingToEndVar();
	}
	
	public void start() {
		try {
			this.stateManager.triggerQuestTransition(Trigger.StartQuest);
		} catch (Exception e) {
			System.out.println("The quest state is not in a right state.");
			e.printStackTrace();
		}
	}
	
	public void fail() {
		try {
			this.stateManager.triggerQuestTransition(Trigger.FailureQuest);
		} catch (Exception e) {
			System.out.println("The quest state is not in a right state.");
			e.printStackTrace();
		}
	}
	
	public State getStateMachine()
	{
		return this.stateManager.getQuestState();
	}

	public void onQuestPending() {
		load();
		System.out.println("Quest Pending...");
	}

	public void onQuestInactive() {
		// TODO Auto-generated method stub
		// TODO: Teleport Back to the default world.
		System.out.println("Quest went into inactive...");
	}

	public void onQuestLoading() {
		System.out.println("Quest Loading...");
		
		if (!isServerSide()) return;
		
		questWorld = MinecraftServer.getServer().worldServerForDimension(WorldProviderQuests.dimID);
		
		new Thread()
		{

			@Override
			public void run() {
				generateWorld(questWorld,questWorldX,questWorldY,questWorldZ);
			}
			
		}.start();
	}

	public void onQuestWaitToStart() {
		if (!isServerSide()) return;
		for (QuestPlayer player:players.values()) {
			player.getInfo();
			if (isServerSide()){
				if(this.isTeleportRequired())
					new QuestTeleporter(questWorld).teleport(player.getEntity(), questWorld);
			}
		}
		this.questTimer.schedule(questPeriodicTimerTask, 0, 1000);
	}

	protected EntityPlayerMP getPlayerEntity(String playerName) {
		return MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
	}

	public void onQuestInProgress() {
		// TODO Auto-generated method stub
		this.secondsRemainingToEnd = timeLimit;
		questTimer.schedule(questAccomplishTimerTask, timeLimit * 1000);
		if (isServerSide())
		startQuest(questWorld,questWorldX,questWorldY,questWorldZ);
	}

	public void onQuestPaused() {
		// TODO Auto-generated method stub
		
	}

	public void onQuestAccomplished() {
		// TODO Auto-generated method stub
		
	}

	public void onQuestFailed() {
		for (QuestPlayer player:players.values()) {
			if (isServerSide())
			returnPlayer(player.getName());
		}
	}

	public void onRewardSelection() {
		// TODO Auto-generated method stub
		
	}

	public void onRetryOrEndTheQuest() {
		// TODO Auto-generated method stub
		
	}
	
	public String getTypeName() {
		return StatCollector.translateToLocal("quest.type."+getType()+".name");
	}
	public abstract String getType();
	public abstract void setProperties(Map<String,String> arguments);
	public abstract Map<String, String> getProperties();
	public abstract String getHint(EntityPlayer player);
	public abstract String getName();
	public abstract Boolean checkComplete(String playerName);
	public void generateWorld(World world,int posX,int posY,int posZ)
	{
		// TODO: Generate World &  teleport the user
		
		try {
			this.stateManager.triggerQuestTransition(Trigger.TeleportDone);			
		} catch (Exception e) {
			System.out.println("The quest state is not in a right state.");
			e.printStackTrace();
		}
	}
	public abstract void questTick();
	public abstract void startQuest(World world,int posX,int posY,int posZ);
		
	public List<String> getPlayers() {
		return (List<String>) players.keySet();
	}
	
	public Textbox getFullDescription(int width,FontRenderer font,EntityPlayer player) {
		String msg = "";
		Textbox box = new Textbox(width);
		box.addLine(EnumChatFormatting.BOLD+getName(),font);
		
		switch(this.stateManager.getQuestState())
		{
		case QuestPending:
			msg = "Quest Pending";
			break;
		case QuestAccomplished:
			msg = "Quest Accomplished";
			break;
		case QuestFailed:
			msg = "Quest Failed";
			break;
		case QuestLoading:
			msg = "Loading....";
			break;
		case WaitToStart:
			// TODO: add quest description
			msg = "Quest starting soon. Seconds left: "+secondsRemainingToStart+" "+getHint(player);
			break;
		case QuestInProgress:
			msg = "Quest in Progress. Seconds left: "+secondsRemainingToEnd+" "+getHint(player);
			break;
		case QuestPaused:
			msg = "Quest Paused";
			break;
		default:
			msg = "[" + this.stateManager.getQuestState().toString() + "]The Quest is in the middle of NO WHERE!!! Please check state machine!";
			break;
		}
		
		box.addLine(EnumChatFormatting.DARK_GREEN + msg,font);
		
		return box;
	}
	
	public static Quest makeQuest(String type,int ID) {
		if (type.equals("run")) {
			try {
				Quest returnQuest = new QuestRun(ID);
				returnQuest.setTeleportRequired(false);
				return returnQuest;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (type.equals("runFromMummy")) {
			try {
				Quest returnQuest = new QuestRunFromMummy(ID);
				returnQuest.setTeleportRequired(false);
				return returnQuest;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Integer getTimeLimit() {
		return timeLimit;
	}
	
	private void tick() {
		if (timeLimit>0) {
			timeLimit--;
		}
		this.questTick();
	}
	
	protected boolean isServerSide() {
		if (questWorld==null) {
			for (QuestPlayer p:players.values()) {
				if (p.getWorld().isRemote) {
					return false;
				}
			}
			return true;
		}
		return !questWorld.isRemote;
	}
	
	public int getID() {
		return ID;
	}

	public void triggerStateChange(Trigger trigger) {
		try {
			stateManager.triggerQuestTransition(trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setOriginalWorld(World orgWorld)
	{
		this.originalWorld = orgWorld;
	}
}