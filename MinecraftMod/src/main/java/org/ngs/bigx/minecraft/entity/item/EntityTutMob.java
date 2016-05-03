package org.ngs.bigx.minecraft.entity.item;

import org.ngs.bigx.minecraft.Main;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityTutMob extends EntityAnimal{//extends EntityAnimal

	/** AI task for player control. */
    private final EntityAIControlledByPlayer aiControlledByPlayer;
	
	
	public EntityTutMob(World p_i1712_1_)
    {
        super(p_i1712_1_);
        this.setSize(1.0F, 1.0F);
        this.tasks.addTask(0, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
        
    }
	
	protected boolean isAIEnabled()
	{
	    return true;
	}
	
	protected void updateAITasks()
    {
        super.updateAITasks();
    }
	
	public boolean canBeSteered()
    {
		return true;
    }
	
	@Override
	public EntityAgeable createChild(EntityAgeable p_90011_1_) {
		return new EntityTutMob(this.worldObj);
	}
	
	/**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_)
    {
        if (this.riddenByEntity == null || this.riddenByEntity == p_70085_1_)
        {
            p_70085_1_.mountEntity(this);
            return true;
        }
        else
        {
            return false;
        }
    }
	

	  

	  /**
	     * Return the AI task for player control.
	     */
	    public EntityAIControlledByPlayer getAIControlledByPlayer()
	    {
	        return this.aiControlledByPlayer;
	    }


	



	
	
	

}
