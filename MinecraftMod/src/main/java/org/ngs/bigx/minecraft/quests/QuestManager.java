package org.ngs.bigx.minecraft.quests;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
import org.ngs.bigx.dictionary.objects.game.properties.Stage;
import org.ngs.bigx.dictionary.objects.game.properties.StageSettings;
import org.ngs.bigx.dictionary.protocol.Specification.GameTagType;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXEventTriggers;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.GuiLeaderBoard;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.LeaderboardRow;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeArea;
import org.ngs.bigx.minecraft.quests.chase.TerrainBiomeAreaIndex;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestTask;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.entity.EntityCustomNpc;

/**
 * QuestDemo - Active Quest   - Quest Detail
 *            \ Player (Local) \ List of Players
 *                              \ Quest Events List - RUN/REWARD/DONE
 *                                                   \ Types Quest Requirements
 * @author localadmin
 *
 */
public class QuestManager {
	private HashMap<String, Quest> availableQuestList;		// String is the ID of the quest
	private String activeQuestId;
	private EntityPlayer player;
	private BigxClientContext clientContext;
	private BigxServerContext serverContext;
	
	public QuestManager(BigxClientContext clientContext, BigxServerContext serverContext, EntityPlayer p) {
		player = p;
		this.availableQuestList = new HashMap<String, Quest>();
		activeQuestId = Quest.QUEST_ID_STRING_NONE;
		this.clientContext = clientContext;
		this.serverContext = serverContext;
		
		if(this.clientContext != null)
			this.clientContext.setQuestManager(this);
	}
	
	public void setClientContext(BigxClientContext clientContext) {
		this.clientContext = clientContext;
	}
	
	public void setServerContext(BigxServerContext serverContext) {
		this.serverContext = serverContext;
	}

	public void setActiveQuest(String questid) throws QuestException {
		if(!questid.equals(Quest.QUEST_ID_STRING_NONE))
		{
			if(this.availableQuestList.get(questid) == null)
			{
				throw new QuestException("The requeseted Quest is not available");
			}
		}
		
		Quest quest = this.availableQuestList.get(this.activeQuestId);
		if(quest != null)
			quest.stop();

		activeQuestId = questid;
		
		if(questid.equals(Quest.QUEST_ID_STRING_NONE))
		{
			System.out.println("[BiGX] QUEST_ID_STRING_NONE");
			return;
		}
		
		quest = this.availableQuestList.get(questid);
		this.activateQuest(quest);
	}
	
	private void deactivateQuest(Quest quest) throws QuestException
	{
		if(quest == null)
		{
			throw new QuestException("The Quest to be activated is null");
		}
		
		quest.stop();
	}
	
	private void activateQuest(Quest quest) throws QuestException
	{
		if(quest == null)
		{
			throw new QuestException("The Quest to be activated is null");
		}
		
		quest.run();
	}
	
	public boolean addAvailableQuestList(Quest quest) throws QuestException
	{
		if( (quest == null) || (quest.getQuestId().equals("")) )
		{
			throw new QuestException("Failed to add a quest to the available quest list");
		}
		
		if(this.availableQuestList.get(quest.getQuestId()) != null)
		{
			return false;
		}
		
		this.availableQuestList.put(quest.getQuestId(), quest);
		return true;
	}
	
	public String getActiveQuestId() {
		return activeQuestId;
	}
	
	// WARNING: volatile - use this to read information, not set new information
	public Quest getActiveQuest() {
		return availableQuestList.get(activeQuestId);
	}
	
	// WARNING: volatile - use this to read information, not set new information
	public IQuestTask getActiveQuestTask() {
		if(availableQuestList.get(activeQuestId) == null)
			return null;
		
		return availableQuestList.get(activeQuestId).getCurrentQuestTask();
	}
	
	public IQuestTask getQuestTaskFightAndChasing() {
		if(availableQuestList.get(Quest.QUEST_ID_STRING_FIGHT_CHASE) == null)
		{
			System.out.println("[BiGX] Fight and chase is not enabled yet.");
			return null;
		}
		
		return availableQuestList.get(Quest.QUEST_ID_STRING_FIGHT_CHASE).getCurrentQuestTask();
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public List<ItemStack> getActiveQuestRewards() throws QuestException {
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		return activeQuest.GetRewardItems();
	}
	
	public int getActiveQuestRewardXP() throws QuestException {
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		return activeQuest.GetRewardXP();
	}
	
	public boolean CheckActiveQuestCompleted() throws QuestException {
		if (activeQuestId == Quest.QUEST_ID_STRING_NONE) {
			return false;
		}
		
		Quest activeQuest = this.availableQuestList.get(activeQuestId);
		
		if(activeQuest == null)
		{
			throw new QuestException("Active Quest is out of sync");
		}
		
		return activeQuest.IsComplete();
	}
	
	public void questTick()
	{
		synchronized (this) {
			this.notifyAll();
		}
	}

	public BigxClientContext getClientContext() {
		return clientContext;
	}

	public BigxServerContext getServerContext() {
		return serverContext;
	}

	public HashMap<String, Quest> getAvailableQuestList() {
		return availableQuestList;
	}
}
