package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.minecraft.client.Textbox;
import org.ngs.bigx.minecraft.quests.QuestStateManager.State;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class Quest implements QuestStateManagerListener{
	private List<String> players;
	private boolean worldExists = false;
	private int timeLimit = 0;
	private QuestStateManager stateManager;
	
	public Quest(boolean completed) throws Exception {
		players = new ArrayList<String>();
		stateManager = new QuestStateManager(this);
	}
	
	public void addPlayer(String player) {
		players.add(player);
		//Get EntityPlayer object for the player
		//Teleport the player to the new world
	}
	
	public void addPlayers(List<String> players) {
		this.players.addAll(players);
	}
	
	public void complete() {
	}
	
	public String getTypeName() {
		return StatCollector.translateToLocal("quest.type.")+getType();
	}
	public abstract String getType();
	public abstract void setProperties(Map<String,String> arguments);
	public abstract Map<String, String> getProperties();
	public abstract String getHint(EntityPlayer player);
	public abstract String getName();
	public abstract Boolean checkComplete(String playerName);
	public abstract void generateWorld(World world,double posX,double posY,double posZ);
	public abstract void questTick();
		
	public List<String> getPlayers() {
		return players;
	}
	
	public Textbox getFullDescription(int width,FontRenderer font) {
		String msg = "";
		Textbox box = new Textbox(width);
		box.addLine(EnumChatFormatting.BOLD+getName(),font);
		
		switch(this.stateManager.getQuestState())
		{
		case QuestAccomplished:
			msg = "Quest Accomplished";
			break;
		case QuestFailed:
			msg = "Quest Failed";
			break;
		case QuestLoading:
			msg = "Loading....";
			break;
		case QuestInProgress:
			msg = "Quest in Progress";
			break;
		case QuestPaused:
			msg = "Quest Paused";
			break;
		}
		
		box.addLine(EnumChatFormatting.DARK_GREEN + msg,font);
		
		if (this.stateManager.getQuestState() == State.QuestAccomplished) {
			box.addLine(EnumChatFormatting.DARK_GREEN+"Completed",font);
		}
		else if (this.stateManager.getQuestState() == State.QuestAccomplished) {
			box.addLine(EnumChatFormatting.DARK_GREEN+"Completed",font);
		}
		return box;
	}
	
	public static Quest makeQuest(String type,boolean completed) {
		if (type.equals("run")) {
			try {
				return new QuestRun(completed);
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

}