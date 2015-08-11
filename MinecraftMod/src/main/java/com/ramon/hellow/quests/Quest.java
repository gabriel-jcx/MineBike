package com.ramon.hellow.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class Quest {
	private String name;
	private boolean completed;
	private List<EntityPlayer> players;
	
	public Quest(String name,boolean completed) {
		this.name = name;
		this.completed = completed;
		players = new ArrayList<EntityPlayer>();
	}
	
	public void addPlayer(EntityPlayer player) {
		players.add(player);
	}
	
	public void addPlayers(List<EntityPlayer> players) {
		this.players.addAll(players);
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public String getName() {
		return name;
	}
}
