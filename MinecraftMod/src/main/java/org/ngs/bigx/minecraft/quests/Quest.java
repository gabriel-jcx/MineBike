package org.ngs.bigx.minecraft.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.minecraft.client.Textbox;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderQuests;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Quest implements QuestStateManagerListener{
	private boolean completed;
	private List<String> players;
	private boolean worldExists = false;
	private int timeLimit = 0;
	private QuestStateManager stateManager;
	private WorldServer questWorld;
	private int questWorldX=0,questWorldY=64,questWorldZ=0;
	
	public Quest(boolean completed) throws Exception {
		this.completed = completed;
		players = new ArrayList<String>();
		stateManager = new QuestStateManager(this);
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

	public void onQuestInactive() {
		// TODO Auto-generated method stub
	}

	public void onQuestLoading() {
		questWorld = MinecraftServer.getServer().worldServerForDimension(WorldProviderQuests.dimID);
		generateWorld(questWorld,questWorldX,questWorldY,questWorldZ);
	}

	public void onQuestWaitToStart() {
		for (String playerName:players) {
			EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
			new QuestTeleporter(questWorld).teleport(player, questWorld);
		}
	}

	public void onQuestInProgress() {
		// TODO Auto-generated method stub
		
	}

	public void onQuestPaused() {
		// TODO Auto-generated method stub
		
	}

	public void onQuestAccomplished() {
		// TODO Auto-generated method stub
		
	}

	public void onQuestFailed() {
		// TODO Auto-generated method stub
		
	}

	public void onRewardSelection() {
		// TODO Auto-generated method stub
		
	}

	public void onRetryOrEndTheQuest() {
		// TODO Auto-generated method stub
		
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
	public abstract void generateWorld(World world,int posX,int posY,int posZ);
	public abstract void questTick();
		
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