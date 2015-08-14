package com.ramon.hellow.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

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
}
