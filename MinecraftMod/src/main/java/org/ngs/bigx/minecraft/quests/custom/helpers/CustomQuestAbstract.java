package org.ngs.bigx.minecraft.quests.custom.helpers;

import java.time.Clock;

import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.client.gui.hud.HudTexture;
import org.ngs.bigx.minecraft.gamestate.CustomQuestJson;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
import org.ngs.bigx.minecraft.quests.custom.SoccerQuest;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;

public abstract class CustomQuestAbstract 
{	
	public static EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	
	protected int progress;
	protected String name;
	protected boolean completed;
	protected boolean started;
	
	//used for displaying instructions at the start of the game
	protected ResourceLocation[] instructionTextureLocations;
	protected String[] instructionStringContents;
	private HudTexture instructionTexture;
	private HudString instructionString;
	//flags for instructions
	private boolean instructionsStarted;
	private boolean instructionsDone;
	
	
	//timekeeping, also used in the instructions method.
	private long  instructionStartTime;
	private Clock instructionClock;
	
	
	public enum Difficulty
	{
		EASY,
		MEDIUM,
		HARD;
	}
	
	//
	public CustomQuestAbstract()
	{
		instructionClock = Clock.systemDefaultZone();
	}
	
	public void loadFromJson(CustomQuestJson json)
	{
		progress = json.getProgress();
		name = json.getName();
		completed = json.getCompleted();
		started = json.getStarted();
	}
	
	//this must be called in constructor or your quest will not work
	public void register()
	{
		GameSaveManager.registerQuest(this);
	}
	
	//methods below are meant to be called with super
	
	//will show the instructions when called.
	//returns true if they are still showing, false otherwise
	//should be continuously called until it returns false
	public boolean showingInstructions()
	{
		if (instructionsDone)
			return false;
		
		//executed once
		if (!instructionsStarted)
		{
			instructionStartTime = instructionClock.millis();
			instructionsStarted = true;
			
			instructionTexture = new HudTexture(0, 0, HudManager.mcWidth, HudManager.mcHeight, "");
			instructionTexture.resourceLocation = instructionTextureLocations[0];
			instructionString = new HudString(0, HudManager.mcHeight - 200, instructionStringContents[0], true, false);
			instructionString.scale = 2.5f;
			
			HudManager.registerTexture(instructionTexture);
			HudManager.registerString(instructionString);
			
			return true;
		}
		
		int currentInstruction = (int) ((instructionClock.millis() - instructionStartTime )/ 3000);
		
		System.out.println("Current instruction" + currentInstruction);
		//this if statement should be executed once
		if (currentInstruction >= instructionTextureLocations.length)
		{
			//the game starts here
			HudManager.unregisterTexture(instructionTexture);
			HudManager.unregisterString(instructionString);
			instructionsDone = true;
			return false;
		}
		else
		{
			System.out.println("\t" + instructionTextureLocations[currentInstruction].getResourcePath());
			instructionString.text = instructionStringContents[currentInstruction];
			instructionTexture.resourceLocation = instructionTextureLocations[currentInstruction];
			return true;
		}
	}
	
	//the quest is registered upon starting
	public void start()
	{
		started = true;
		CustomQuestEventHandler.registerQuest(this);
	}
	
	public int getProgress()
	{
		return progress;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public boolean isComplete()
	{
		return completed;
	}
	
	public void complete()
	{
		completed = true;
		CustomQuestEventHandler.unregisterQuest(this);
	}
	
	public abstract void setDifficulty(Difficulty difficultyIn);
	
	
	//methods below are meant to be overridden
	
	//EVENT HANDLING METHODS, they do nothing by default in the abstract class
	//override them in a sub class to add functionality!
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{

	}
	
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{
		
	}
	
	public void onItemUse(PlayerUseItemEvent.Start event)
	{
		
	}
	
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		
	}
	
	public void onAttackEntityEvent(AttackEntityEvent event)
	{
		
	}
	
	public void entityInteractEvent(EntityInteractEvent event)
	{
		
	}
	
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		
	}
	
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		
	}
	
	public void onQuit(ClientDisconnectionFromServerEvent event)
	{
		
	}
}
