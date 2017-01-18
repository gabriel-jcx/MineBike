package org.ngs.bigx.minecraft.quests;

import java.io.File;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class QuestChasing extends Quest {
	
	private double chaseProgress;
	File questData;
	
	public QuestChasing(int ID) {
		super(ID);
		questData = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "assets/minebike/sample_stage.json");
	}

	@Override
	protected void setRemainingToEndVar() {
		
	}

	@Override
	public void addQuestInitiator(int locationX, int height, int locationY) {
		
	}

	@Override
	public void removeQuestInitiator(int locationX, int height, int locationY) {
		
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public void setProperties(Map<String, String> arguments) {
		
	}

	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	@Override
	public String getHint(EntityPlayer player) {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Boolean checkComplete(String playerName) {
		return null;
	}

	@Override
	public void questTick() {
		
	}

	@Override
	public void startQuest(World world, int posX, int posY, int posZ) {
		
	}
}
