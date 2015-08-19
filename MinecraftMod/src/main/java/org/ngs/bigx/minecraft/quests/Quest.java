package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;

import org.ngs.bigx.minecraft.client.Textbox;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class Quest {
	private String name;
	private boolean completed;
	private List<String> players;
	
	public Quest(String name,boolean completed) {
		this.name = name;
		this.completed = completed;
		players = new ArrayList<String>();
	}
	
	public void addPlayer(String player) {
		players.add(player);
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
	
	public String getName() {
		return name;
	}
	
	public List<String> getPlayers() {
		return players;
	}
	
	public Textbox getFullDescription(int width,FontRenderer font) {
		Textbox box = new Textbox(width);
		box.addLine(EnumChatFormatting.BOLD+name,font);
		if (completed) {
			box.addLine(EnumChatFormatting.DARK_GREEN+"Completed",font);
		}
		else {
			box.addLine(EnumChatFormatting.YELLOW+"In Progress",font);
		}
		return box;
	}
}
