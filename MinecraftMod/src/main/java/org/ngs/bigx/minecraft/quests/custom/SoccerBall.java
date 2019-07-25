package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.BiGX;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SoccerBall extends EntityCreeper 
{
	public static boolean registered = register();
	
	private double hitMotionX;
	private double hitMotionZ;
	private boolean justHit;
	
	private boolean justReset;
	
	public static boolean register()
	{
		EntityRegistry.registerModEntity(SoccerBall.class, "SoccerBall",
				EntityRegistry.findGlobalUniqueEntityId(), BiGX.MODID, 64, 5, true);
		return true;
	}
	
	public SoccerBall(World p_i1733_1_) {
		super(p_i1733_1_);
		// TODO Auto-generated constructor stub
		this.justHit = false;
		this.justReset = false;
	}
	
	@Override
	public boolean getPowered()
    {
        return false;
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.setPosition(this.getPosition(1.0f).xCoord, SoccerQuest.SOCCER_Y_LEVEL, this.getPosition(1.0f).zCoord);
		this.setVelocity(this.motionX, 0.0, this.motionZ);
		double x = this.getPosition(1.0f).xCoord;
		double y = this.getPosition(1.0f).yCoord;
		double z = this.getPosition(1.0f).zCoord;
		
		if (justHit)
		{
			this.setVelocity(hitMotionX, 0.0d, hitMotionX);
			this.motionX = hitMotionX;
			this.motionZ = hitMotionZ;
			justHit = false;
		}
		
		//dont do anything if it goes in the goal
		if(SoccerQuest.entityInsideBlueGoal(this) || SoccerQuest.entityInsideRedGoal(this))
			return;
		//after this line we know we are not in the goal
		
		if(x < -30.0 || x > 31.0)
		{
			this.setPosition(x < -30 ? -29.9 : 30.9, 10.0, z);
			this.setVelocity(this.motionX*-1.0, 0.0, this.motionZ);
		}
		if(z < -49.0 || z > 50.0)
		{
			this.setPosition(x, 10.0, z < -49 ? -48.9 : 49.9);
			this.setVelocity(this.motionX, 0.0, this.motionZ* -1);
		}
		
		if (justReset)
		{
			this.setPosition(0.0, 10.0, 0.0);
			this.setVelocity(0.0, 0.0, 0.0);
			justReset = false;
		}
		
		this.setHealth(20.0f);
	}
	
	@Override
	public boolean isAIEnabled()
	{
		return false;
	}

	@Override
	public int getCreeperState()
	{
		return -1;
	}
	
	@Override
	public boolean isInRangeToRender3d(double d1, double d2, double d3)
	{
		return true;
	}
		
	@Override
	public boolean isInRangeToRenderDist(double d1)
	{
		return true;
	}
	
	public void getHitFrom(double x, double y, double z)
	{
		//we only care about x and z
		double xDiff = -1*(x - this.posX);
		double zDiff = -1*(z - this.posZ);
		
		System.out.println("player z : " + z + "this z : " + this.posZ);
		double HIT_SPEED = 0.3;
		
		System.out.println("Ball getting hit!");
		System.out.println(xDiff + " " + zDiff);
		
		double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double cos  = xDiff/dist;
		double sin  = zDiff/dist;
		
		this.addVelocity((HIT_SPEED)*(cos), 0, (HIT_SPEED)*(sin));
		this.velocityChanged = true;
	}
	
	public void getHitTowards(double x, double y, double z)
	{
		//we only care about x and z
		double xDiff = (x - this.posX);
		double zDiff = (z - this.posZ);
		
		double HIT_SPEED = 0.3;
		
		double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double cos  = xDiff/dist;
		double sin  = zDiff/dist;
		
		this.addVelocity((HIT_SPEED)*(cos), 0, (HIT_SPEED)*(sin));
		this.velocityChanged = true;
	}
	
	
	public void reset()
	{
		this.setPosition(0.0, 10.0, 0.0);
		this.setVelocity(0.0, 0.0, 0.0);
		this.justReset = true;
	}
}
