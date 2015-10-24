package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.minecraft.client.Textbox;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public abstract class Quest {
	private boolean completed;
	private List<String> players;
	private boolean worldExists = false;
	
	public Quest(boolean completed) {
		this.completed = completed;
		players = new ArrayList<String>();
	}
	
	public void addPlayer(String player) {
		players.add(player);
		//Get EntityPlayer object for the player
		//Teleport the player to the new world
		EntityPlayer pl;
		pl.setPosition(pl.po)
	}
	
	public void addPlayers(List<String> players) {
		this.players.addAll(players);
	}
	
	public void complete() {
		completed = true;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public String getTypeName() {
		return StatCollector.translateToLocal("quest.type.")+getType();
	}
	public abstract String getType();
	public abstract void setProperties(Map<String,String> arguments);
	public abstract Map<String, String> getProperties();
	public abstract String getHint(EntityPlayer player);
	public abstract String getName();
		
	public List<String> getPlayers() {
		return players;
	}
	
	public Textbox getFullDescription(int width,FontRenderer font) {
		Textbox box = new Textbox(width);
		box.addLine(EnumChatFormatting.BOLD+getName(),font);
		if (completed) {
			box.addLine(EnumChatFormatting.DARK_GREEN+"Completed",font);
		}
		else {
			box.addLine(EnumChatFormatting.YELLOW+"In Progress",font);
		}
		return box;
	}
	
	public static Quest makeQuest(String type,boolean completed) {
		if (type.equals("explore")) {
			return new QuestExplore(completed);
		}
		if (type.equals("run")) {
			return new QuestRun(completed);
		}
		return null;
	}
	

}