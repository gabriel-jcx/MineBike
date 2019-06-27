package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.BiGX;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class SoccerBall extends EntityCreeper 
{
	public static boolean registered = register();
	
	public static boolean register()
	{
		EntityRegistry.registerModEntity(SoccerBall.class, "SoccerBall",
				EntityRegistry.findGlobalUniqueEntityId(), BiGX.MODID, 64, 5, true);
		return true;
	}
	
	public SoccerBall(World p_i1733_1_) {
		super(p_i1733_1_);
		// TODO Auto-generated constructor stub
		this.setHealth(100000.0f);
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
		
		//dont do anything if it goes in the goal
		if(SoccerQuest.entityInsideBlueGoal(this) || SoccerQuest.entityInsideRedGoal(this))
			return;
		//after this line we know we are not in the goal
		
		if(x < -15.0 || x > 16.0)
		{
			this.setPosition(x < -15 ? -14.9 : 15.9, 10.0, z);
			this.setVelocity(this.motionX*-1.0, 0.0, this.motionZ);
		}
		if(z < -25.0 || z > 26.0)
		{
			this.setPosition(x, 10.0, z < -25 ? -24.9 : 25.9);
			this.setVelocity(this.motionX, 0.0, this.motionZ* -1);
		}
	}
}
