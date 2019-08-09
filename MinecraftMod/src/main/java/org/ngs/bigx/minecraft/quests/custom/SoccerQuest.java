package org.ngs.bigx.minecraft.quests.custom;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.client.gui.hud.HudTexture;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.custom.helpers.Utils;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.DataAI;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import scala.actors.threadpool.Arrays;

public class SoccerQuest extends CustomQuestAbstract
{
	private int playerScore;
	private int enemyScore;
	
	private boolean ballInit;
	private SoccerBall ball;
	
	public static final int SOCCERDIMENSIONID = 106;
	public static final double SOCCER_Y_LEVEL = 10.0;
	
	private int ticksInsideGoal;
	
	//the red box is the enemies score
	private HudRectangle redBox;
	private HudRectangle blueBox;
	
	private HudString redScoreBoard;
	private HudString blueScoreBoard;
	
	//used for displaying Messages when either player scores
	private HudString scoreMessageString;
	private static final String WIN_MESSAGE = "YOU SCORED!";
	private static final String LOSE_MESSAGE = "Your opponent scored...";
	
	private HudRectangle clockRectangle;
	private HudString clockString;
	
	//timekeeping
	private long lastTime;
	private long startTime;
	private Clock gameClock;
	private int secondsElapsed;
	public static final int GAME_TIME_IN_SECONDS = 300;
	
	//npc/pathfinding stuff
	static EntityCustomNpc npc;
	static NpcCommand command;
	private int npcSpeed;
	static ForgeDirection runDirection;
	public static int[] npcPath = new int[3];
	public static List<int[]> npcPathList = new ArrayList<int[]>();
	
	//indicates whether the soccer dimension is loaded
	static boolean worldLoaded = false;
	
	public SoccerQuest()
	{
		super();	
		progress = 0;
		name = "SoccerQuest";
		completed = false;
		
		//playerScore
		playerScore = 0;
		enemyScore = 0;
		
		ticksInsideGoal = 0;
		
		ballInit = false;
		
		npcSpeed = 7;
		
		//hud stuff
		//location of the red and blue boxes that surround the text boxes
		redBox = new HudRectangle(-100, 30, 40, 40, 0xff0000ff, true, false);
		blueBox = new HudRectangle(60, 30, 40, 40, 0x0000ffff, true, false);
		
		//this is the position of the red and blue score text boxes
		redScoreBoard = new HudString(-85, 42, "0", 2.0f, true, false);
		blueScoreBoard = new HudString(70, 42, "0", 2.0f, true, false);
		
		//clock hud elements
		clockRectangle = new HudRectangle(-30, 30, 60, 30, 0x000000ff, true, false);
		clockString = new HudString(
			0, 35,
			Integer.toString(GAME_TIME_IN_SECONDS/ 60) +
				" : " + 
				((GAME_TIME_IN_SECONDS % 60 < 10) ? "0" : "") +
				Integer.toString(GAME_TIME_IN_SECONDS % 60),
			2.0f,
			true, false
		);
		
		
		instructionTextureLocations = new ResourceLocation[]  
			{	
			new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/instructions/SoccerQuestInstruction1.png"),
			new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/instructions/SoccerQuestInstruction2.png"),
			new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/instructions/SoccerQuestInstruction3.png"),
			};
		instructionStringContents = new String[]
			{
			"Kick the Creeper by running into it",
			"Hit it into the red goal to score a point",
			"Score more than Raul to win",
			};
		
		
//		instructionsStarted = false;
		
		//message that displays when you get the ball in the goal
		
		gameClock = Clock.systemDefaultZone();
		
		this.register();
	}
	
	public void endGame()
	{
		HudManager.unregisterRectangle(blueBox);
		HudManager.unregisterRectangle(redBox);
		
		HudManager.unregisterString(blueScoreBoard);
		HudManager.unregisterString( redScoreBoard);
		
		HudManager.unregisterRectangle(clockRectangle);
		HudManager.unregisterString(clockString);
		
		QuestTeleporter.teleport(player, 0, (int) Raul.LOCATION.xCoord, (int) Raul.LOCATION.yCoord, (int) Raul.LOCATION.zCoord);
		started = false;
		ballInit = false;
		worldLoaded = false;
		
		if (ball != null)
			ball.isDead = true;
		if(npc != null)
			npc.isDead = true;
		super.complete();
		completed = false;
	}
	
	//this is the stuff that ends the game
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		endGame();
	}
	
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{

	}
	
	@Override
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		if (event.world.provider.dimensionId == SoccerQuest.SOCCERDIMENSIONID)
		{
			worldLoaded = true;
		}
	}
	
	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{	
		lastTime = gameClock.millis();
		//if the world is not loaded or the event happened on the client, skip
		if (!worldLoaded)
			return;
		if (event.world.provider.worldObj.isRemote)
			return;
		
		//show the instructions for the game, then init the ball and npc
		if (!ballInit && event.world.provider.dimensionId == SoccerQuest.SOCCERDIMENSIONID)
		{	
			if (super.showingInstructions())
			{
				BiGX.instance().clientContext.lock(true);
				return;
			}
			else
			{
				BiGX.instance().clientContext.lock(false);
				QuestTeleporter.teleport(player, SoccerQuest.SOCCERDIMENSIONID, 1, 10, -25);
			}
			
			//anything below here executes after the instructions
			
			startTime = gameClock.millis();
			
			
			
			NpcCommand.removeNpc(Raul.NAME + " ", SoccerQuest.SOCCERDIMENSIONID);
			
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(SoccerQuest.SOCCERDIMENSIONID);
			synchronized (ws.loadedEntityList) {
				Iterator iter = ws.loadedEntityList.iterator();
				while (iter.hasNext()) {
					Entity entity = (Entity)iter.next();
					if (!(entity instanceof EntityPlayer))
			         entity.isDead = true;
				}
			}
			
			//spawns the npc
			npc = NpcCommand.spawnNpc(1, 10, 15, (WorldServer)event.world.provider.worldObj, Raul.NAME + " ", Raul.TEXTURE);
			command = new NpcCommand(BigxClientContext.getInstance(), npc);
			command.setSpeed(npcSpeed);
			
			npcPathList.add(npcPath);
			npcPathList.add(npcPath);
			
			npc.ai = new DataAI(npc) {
				public int[] path = SoccerQuest.npcPath;
				@Override
				public int[] getCurrentMovingPath()
				{
					return path;
				}
				
				@Override
				public List getMovingPath()
				{
					return npcPathList;
				}
			};
			npc.ai.movingPause = false;
			npc.ai.movingPos = 0;
			npc.ai.startPos = npcPath;
			npc.ai.walkingRange = 100;
			npc.ai.movingPattern = 0;
			npc.ai.movingType = EnumMovingType.MovingPath;
			
			
			
			//spawns the ball
			ball = new SoccerBall(event.world.provider.worldObj);
			ball.setPosition(0, 10, 0);
			ball.setLocationAndAngles(0, 10, 0, 0.0f, 0.0f);
			
			event.world.provider.worldObj.spawnEntityInWorld(ball);
			
			ballInit = true;
		}//end init
		
		//pathfinding for the npc
		if (npc != null)
		{		
			npc.setAIMoveSpeed(100.0f);
			updateNpcPath();
		}
		
		//this is responsible for handling the ball entering the goal and updating score and clock
		if (ballInit && event.world.provider.dimensionId == SoccerQuest.SOCCERDIMENSIONID)
		{
			//if the ball is in the goal
			if (entityInsideRedGoal(ball) || entityInsideBlueGoal(ball))
			{
				ticksInsideGoal++;
				if(ticksInsideGoal >= 20)
				{
					if (entityInsideRedGoal(ball))
					{
						playerScore++;
						blueScoreBoard.text = Integer.toString(playerScore);
					}
					else if(entityInsideBlueGoal(ball))
					{
						enemyScore++;
						redScoreBoard.text = Integer.toString(enemyScore);
					}
					
					ball.reset();
					ticksInsideGoal = 0;
					
					//teleport raul and player back to the start
					QuestTeleporter.teleport(player, SOCCERDIMENSIONID, 1, (int)SoccerQuest.SOCCER_Y_LEVEL, -25);
					npc.setPosition(1.0, SoccerQuest.SOCCER_Y_LEVEL*1.0d, 15);
//					updateNpcPath();
					resetNpc(event);
					
				}//finish resetting game
			}
			
			//handle clock stuff
			secondsElapsed = (int) (gameClock.millis() - startTime) / 1000;
			int timeLeft = GAME_TIME_IN_SECONDS - secondsElapsed;
			clockString.text = 
					Integer.toString(timeLeft/ 60) +
					" : " + 
					((timeLeft % 60 < 10) ? "0" : "") +
					Integer.toString(timeLeft % 60);
			
			if (timeLeft == 0)
			{
				endGame();
			}
		
		}
	}
	
	private void resetNpc(TickEvent.WorldTickEvent event) 
	{
		npc.isDead = true;
		
		//spawns the npc
		npc = NpcCommand.spawnNpc(1, 10, 15, (WorldServer)event.world.provider.worldObj, Raul.NAME + " ", Raul.TEXTURE);
		command = new NpcCommand(BigxClientContext.getInstance(), npc);
		
		npcPathList.add(npcPath);
		npcPathList.add(npcPath);
		
		npc.ai = new DataAI(npc) {
			public int[] path = SoccerQuest.npcPath;
			@Override
			public int[] getCurrentMovingPath()
			{
				return path;
			}
			
			@Override
			public List getMovingPath()
			{
				return npcPathList;
			}
		};
		
		npc.ai.movingPause = false;
		npc.ai.movingPos = 0;
		npc.ai.startPos = npcPath;
		npc.ai.walkingRange = 100;
		npc.ai.movingPattern = 0;
		npc.ai.movingType = EnumMovingType.MovingPath;
		
		double d = npc.getPosition(1.0f).distanceTo(Vec3.createVectorHelper(0.0d, SOCCER_Y_LEVEL, 0.0d));
		double t = 1.0d/d;
		t *= 3;
		double[] newPoint = Utils.lerp(
				new double[] {npc.posX, npc.posY, npc.posZ}, 
				new double[] {0.0d, 0.0d, 0.0d}, 
				t);
		
		int[] intPoint = new int[] {(int) newPoint[0], (int) newPoint[1], (int) newPoint[2]};
		npcPath[0] = (int) newPoint[0];
		npcPath[1] = (int) newPoint[1];
		npcPath[2] = (int) newPoint[2];
		
		npcPathList.set(0, npcPath);
		npcPathList.set(1, npcPath);
		
		npc.ai.setMovingPath(npcPathList);
		
//		command.addPathPoint(intPoint);
		
		npc.ai.getMovingPath().add(intPoint);
		
		command.enableMoving(true);
		command.setSpeed(npcSpeed);
		
	}
	
	private void updateNpcPath() 
	{
		double d = npc.getPosition(1.0f).distanceTo(ball.getPosition(1.0f));
		double t = 1.0d/d;
		t *= 3;
		double[] newPoint = Utils.lerp(
				new double[] {npc.posX, npc.posY, npc.posZ}, 
				new double[] {ball.posX, ball.posY, ball.posZ}, 
				t);
		
		int[] intPoint = new int[] {(int) newPoint[0], (int) newPoint[1], (int) newPoint[2]};
		npcPath[0] = (int) newPoint[0];
		npcPath[1] = (int) newPoint[1];
		npcPath[2] = (int) newPoint[2];
		
		npcPathList.set(0, npcPath);
		npcPathList.set(1, npcPath);
		
		npc.ai.setMovingPath(npcPathList);
		
//		command.addPathPoint(intPoint);
		
		npc.ai.getMovingPath().add(intPoint);
		
		command.enableMoving(true);
		
	}

	public static boolean entityInsideBlueGoal(EntityLiving e)
	{
		double x = e.getPosition(1.0f).xCoord;
		double z = e.getPosition(1.0f).zCoord;
		
		return (x < 3.0 && x  > -2.0) && z < -48.8;
	}
	
	public static boolean entityInsideRedGoal(EntityLiving e)
	{
		double x = e.getPosition(1.0f).xCoord;
		double z = e.getPosition(1.0f).zCoord;
		return (x < 3.0 && x  > -2.0) && z > 49.8;
	}
		
	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{	
		if (!worldLoaded)
		{
			return;
		}
		
		if(event.player.capabilities.allowEdit)	
            event.player.setGameType(WorldSettings.getGameTypeById(2));
		
		if (ball == null)
			return;
		
		double HIT_SPEED = 3.0d;
		if (event.player.getPosition(1.0f).distanceTo(ball.getPosition(1.0f)) < 2)
		{
			ball.getHitFrom(event.player.posX, event.player.posY, event.player.posZ);
		}
		if (npc.getPosition(1.0f).distanceTo(ball.getPosition(1.0f)) < 2)
		{
			ball.getHitTowards(0, SOCCER_Y_LEVEL, -50);
		}
	}
	
	@Override
	public void onAttackEntityEvent(AttackEntityEvent event)
	{
		Entity e = event.target;
		if(e instanceof EntityCreeper)
		{
			e.setVelocity(e.motionX*4, 0.0, e.motionZ*4);
			((EntityCreeper)e).setHealth(20.0f);
		}	
	}
	
	@Override
	public void start()
	{
		if (isStarted())
			return;
		progress = 0;
		
		playerScore = 0;
		enemyScore = 0;
	
		
		HudManager.registerRectangle(redBox);
		HudManager.registerRectangle(blueBox);
		HudManager.registerString(redScoreBoard);
		HudManager.registerString(blueScoreBoard);
		HudManager.registerRectangle(clockRectangle);
		HudManager.registerString(clockString);
		
		//create this here so that mcWidth and height are known
//		instructionTexture = new HudTexture(0, 0, HudManager.mcWidth, HudManager.mcHeight, "textures/GUI/instructions/SoccerQuestInstruction1.png");
//		instructionString = new HudString(0, HudManager.mcHeight - 200, instructionStringContents[0], true, false);
//		instructionString.scale = 2.5f;
//		HudManager.registerTexture(instructionTexture);
//		HudManager.registerString(instructionString);
		//end hud stuff
		
		redScoreBoard.text = "0";
		blueScoreBoard.text = "0";
		
//		teleport them to the soccer arena
		QuestTeleporter.teleport(player, SOCCERDIMENSIONID, 1, 14, -25);	
		
		super.start();
	}

	@Override
	public void setDifficulty(Difficulty difficultyIn) 
	{
		// TODO Auto-generated method stub
		if (npc != null)
		{
			switch(difficultyIn)
			{
			case EASY:
				command.setSpeed(5);
			case MEDIUM:
				command.setSpeed(7);
			case HARD:
				command.setSpeed(10);
			}
		}
		
	}
}
