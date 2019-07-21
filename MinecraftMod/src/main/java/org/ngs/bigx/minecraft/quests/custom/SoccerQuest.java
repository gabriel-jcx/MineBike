package org.ngs.bigx.minecraft.quests.custom;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.client.gui.hud.HudTexture;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
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
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.DataAI;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

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
	
	private HudString youScoredMessage;
	
	long lastTime;
	
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
		
		//hud stuff
		//location of the red and blue boxes that surround the text boxes
		redBox = new HudRectangle(-100, 30, 40, 40, 0xff0000ff, true, false);
		blueBox = new HudRectangle(60, 30, 40, 40, 0x0000ffff, true, false);
		
		//this is the position of the red and blue score text boxes
		redScoreBoard = new HudString( 70, 42, "0", 2.0f, true, false);
		blueScoreBoard = new HudString(-85, 42, "0", 2.0f, true, false);
		
		//message that displays when you get the ball in the goal
		
		this.register();
	}
	
	//this is the stuff that ends the game
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		HudManager.unregisterRectangle(blueBox);
		HudManager.unregisterRectangle(redBox);
		
		HudManager.unregisterString(blueScoreBoard);
		HudManager.unregisterString(redScoreBoard);
		
		QuestTeleporter.teleport(player, 0, (int) Raul.LOCATION.xCoord, (int) Raul.LOCATION.yCoord, (int) Raul.LOCATION.zCoord);
		started = false;
		ballInit = false;
		worldLoaded = false;
		ball.isDead = true;
		npc.isDead = true;
		super.complete();
		completed = false;
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
	
	static EntityCustomNpc npc;
	static NpcCommand command;
	
	public static int[] npcPath = new int[3];
	public static List<int[]> npcPathList = new ArrayList<int[]>();
	
	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{	
		//if the world is not loaded or the event happened on the client, skip
		if (!worldLoaded)
			return;
		if (event.world.provider.worldObj.isRemote)
			return;
		
		//init the ball and npc
		if (!ballInit && event.world.provider.dimensionId == SoccerQuest.SOCCERDIMENSIONID)
		{	
			NpcCommand.removeNpc(Raul.NAME, SoccerQuest.SOCCERDIMENSIONID);
			
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
			npc = NpcCommand.spawnNpc(1, 10, 15, (WorldServer)event.world.provider.worldObj, Raul.NAME, Raul.TEXTURE);
			command = new NpcCommand(BigxClientContext.getInstance(), npc);
			
			npcPathList.add(npcPath);
			
			command.setPath(npcPathList);
			command.setSpeed(30);
			
//			npc.ai = new DataAI(npc) {
//				public int[] path = SoccerQuest.npcPath;
//				@Override
//				public int[] getCurrentMovingPath()
//				{
//					return path;
//				}
//				
//				@Override
//				public List getMovingPath()
//				{
//					return npcPathList;
//				}
//			};
//			npc.ai.movingPause = false;
//			npc.ai.movingPos = 0;
//			npc.ai.startPos = npcPath;
//			npc.ai.walkingRange = 100;
//			npc.ai.movingPattern = 0;
//			npc.ai.movingType = EnumMovingType.MovingPath;
			
			
			//spawns the ball
			ball = new SoccerBall(event.world.provider.worldObj);
			ball.setPosition(0, 10, 0);
			ball.setLocationAndAngles(0, 10, 0, 0.0f, 0.0f);
			
			event.world.provider.worldObj.spawnEntityInWorld(ball);
			
			ballInit = true;
		}
		
		//pathfinding for the npc
		if (npc != null)
		{		
			//make him follow the ball
//			((int[])movingPath.get(0))[0] = (int)ball.getPosition(1.0f).xCoord;
//			((int[])movingPath.get(0))[1] = (int)ball.getPosition(1.0f).yCoord;
//			((int[])movingPath.get(0))[2] = (int)ball.getPosition(1.0f).zCoord;
//			
//			((int[])movingPath.get(1))[0] = (int)ball.getPosition(1.0f).xCoord;
//			((int[])movingPath.get(1))[1] = (int)ball.getPosition(1.0f).yCoord;
//			((int[])movingPath.get(1))[2] = (int)ball.getPosition(1.0f).zCoord;
//			
//			npc.ai.startPos = (int[])movingPath.get(0);
//			
//			npc.ai.setMovingPath(movingPath);
//			
//			npc.ai.movingPos = 0;
			
			npcPath[0] = (int)ball.getPosition(1.0f).xCoord;
			npcPath[1] = (int)ball.getPosition(1.0f).yCoord;
			npcPath[2] = (int)ball.getPosition(1.0f).zCoord;
			
			npcPathList.set(0, npcPath);
			
			command.enableMoving(true);
		}
		
		//this is responsible for handling the ball entering the goal and updating score
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
					QuestTeleporter.teleport(player, SOCCERDIMENSIONID, 1, (int)SoccerQuest.SOCCER_Y_LEVEL, -15);
					npc.setPosition(1.0, SoccerQuest.SOCCER_Y_LEVEL*1.0d, 15);
				}//finish resetting game
			}
		}
	}
	
	public static boolean entityInsideBlueGoal(EntityLiving e)
	{
		double x = e.getPosition(1.0f).xCoord;
		double z = e.getPosition(1.0f).zCoord;
		
		return (x < 3.0 && x  > -2.0) && z < -24.8;
	}
	
	public static boolean entityInsideRedGoal(EntityLiving e)
	{
		double x = e.getPosition(1.0f).xCoord;
		double z = e.getPosition(1.0f).zCoord;
		return (x < 3.0 && x  > -2.0) && z > 25.8;
	}
		
	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{	
		if (!worldLoaded)
		{
			return;
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
		//end hud stuff
		
//		teleport them to the soccer arena
		QuestTeleporter.teleport(player, SOCCERDIMENSIONID, 1, 14, -15);
		
		super.start();
	}

	@Override
	public void setDifficulty(Difficulty difficultyIn) {
		// TODO Auto-generated method stub
		
	}
}
