package org.ngs.bigx.minecraft.entity.item;

import org.ngs.bigx.minecraft.Main;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class EntityTank extends EntityMob 
{
	public final float MaxSpeed   = 10f;
	public final float SpeedRatio = 5f; 
	
	private boolean stationary = false;
	
	public EntityTank(World p_i1712_1_)
    {
        super(p_i1712_1_);
        this.setSize(1.0F, .5F);
    }
	
	public boolean isMovementCeased(){
		return stationary;
		
	}
	
	public boolean isAIEnabled(){
		return true;
	}
	
	public static void mainRegistry(){
		registerEntity();
	}
	
	public static void registerEntity(){
		createEntity(EntityTank.class, "Tank", 0xFF000D, 0x001EFF);
	}
	
	public static void createEntity(Class entityClass, String entityName, int solidColor, int spotColor){
		int randId = EntityRegistry.findGlobalUniqueEntityId();
		
		EntityRegistry.registerGlobalEntityID(entityClass, entityName, randId);
		EntityRegistry.registerModEntity(entityClass, entityName, randId, Main.modInstance, 64, 1, true);
		EntityRegistry.addSpawn(entityClass, 2, 0, 1, EnumCreatureType.creature, BiomeGenBase.forest);
		createEgg(randId, solidColor, spotColor);
	
	}
	
	public static void createEgg(int randId, int solidColor, int spotColor){
		EntityList.entityEggs.put(Integer.valueOf(randId), new EntityList.EntityEggInfo(randId, solidColor,spotColor));
	
	}
	
	/**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
	public boolean interact(EntityPlayer entityplayer)
	{
		if (riddenByEntity == null || riddenByEntity == entityplayer)
		{
			entityplayer.mountEntity(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
	{
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase)
		{
			this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
			this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
			p_70612_1_ = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.1F; //initial value 0.5F
			p_70612_2_ = ((EntityLivingBase)this.riddenByEntity).moveForward * SpeedRatio; //Forward move speed
			
			stationary = true;
			if (p_70612_2_ <= 0.0F)
			{
				p_70612_2_ *= 0.25F; //Reverse move speed
			}
			this.stepHeight = 1.0F;
			this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
			if (!this.worldObj.isRemote)
			{
				this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
				super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			}
			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;
			if (f4 > 1.0F)
			{
				f4 = 1.0F;
			}
			this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		}
		else
		{
			this.stepHeight = 0.5F;
			this.jumpMovementFactor = 0.02F;
			stationary = false;
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		}
	}
}
