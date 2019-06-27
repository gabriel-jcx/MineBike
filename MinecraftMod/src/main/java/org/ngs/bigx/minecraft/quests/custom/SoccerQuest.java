package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.WorldEvent;

public class SoccerQuest extends CustomQuestAbstract
{
	private int playerScore;
	private int enemyScore;
	
	private boolean ballInit;
	private SoccerBall ball;
	
	public static final int SOCCERDIMENSIONID = 106;
	public static final double SOCCER_Y_LEVEL = 10.0;
	
	public SoccerQuest()
	{
		super();
		progress = 0;
		name = "SoccerQuest";
		completed = false;
		
		playerScore = 0;
		enemyScore = 0;
		
		ballInit = false;
		
		register();
	}
	static boolean pickup = false;
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		if (!pickup)
		{
			pickup = true;
			return;
		}
		QuestTeleporter.teleport(player, 0, (int) Raul.LOCATION.xCoord, (int) Raul.LOCATION.yCoord, (int) Raul.LOCATION.zCoord);
		started = false;
		ballInit = false;
		worldLoaded = false;
		super.complete();
		completed = false;
	}
	
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
//		EntityCreeper creeper = new EntityCreeper(event.world.provider.worldObj);
//		creeper.setPosition(0, 14, 0);
//		event.world.provider.worldObj.spawnEntityInWorld(creeper);
	}
	
	static boolean worldLoaded = false;
	@Override
	public void onWorldLoadEvent(WorldEvent.Load event)
	{
		if (event.world.provider.dimensionId == SoccerQuest.SOCCERDIMENSIONID)
			worldLoaded = true;
	}
	
//	static EntityCreeper creeper;
	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{
//		if (creeper != null)
//		{
//			creeper.setPosition(creeper.getPosition(1.0f).xCoord, 11.0, creeper.getPosition(1.0f).zCoord);
////			creeper.setLocationAndAngles(p_70012_1_, p_70012_3_, p_70012_5_, p_70012_7_, p_70012_8_);
//			creeper.setVelocity(creeper.motionX, 0.0, creeper.motionZ);
//			creeper.setVelocity(creeper.motionX*2, 0.0, creeper.motionZ);
//			if(creeper.isCollidedHorizontally)
//			{
////				if(creeper.getPosition(1.0f).xCoord > )
//				
//			}
//		}
		
		//if the world is not loaded or the event happend on the client, skip
		if (!worldLoaded)
			return;
		if (event.world.provider.worldObj.isRemote)
			return;
	
		if (!ballInit)
			System.out.println("World ID: " + event.world.provider.dimensionId + ", Ballinit: " + ballInit);
		
		if (!ballInit && event.world.provider.dimensionId == SoccerQuest.SOCCERDIMENSIONID)
		{	
//			if ( == "Hello")
			
//			EntityCreeper ball = new EntityCreeper(event.world.provider.worldObj);
//			ball.setPosition(0, 14, 0);
			
			System.out.println("Spawning creeper!");
//			creeper = new EntityCreeper(event.world.provider.worldObj);
//			EntityMinecartEmpty creeper = new EntityMinecartEmpty(event.world.provider.worldObj);
			ball = new SoccerBall(event.world.provider.worldObj);
			ball.setPosition(0, 10, 0);
			ball.setLocationAndAngles(0, 10, 0, 0.0f, 0.0f);
			
			event.world.provider.worldObj.spawnEntityInWorld(ball);
			ballInit = true;
		}
		else
		{
			
		}
	}
		
	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{	
		if(!event.player.capabilities.isCreativeMode)
			event.player.setGameType(WorldSettings.getGameTypeById(1));
	}
	
	@Override
	public void start() 	
	{
		if (isStarted())
			return;
		progress = 0;
		
		//teleport them to the soccer arena
//		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.SOCCERDIMENSIONID);
		QuestTeleporter.teleport(player, SOCCERDIMENSIONID, 1, 70, 0);
		
		super.start();
	}
}
