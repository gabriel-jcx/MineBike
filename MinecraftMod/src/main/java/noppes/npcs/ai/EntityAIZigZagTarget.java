package noppes.npcs.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.ai.RandomPositionGeneratorAlt;
import noppes.npcs.constants.AiMutex;

public class EntityAIZigZagTarget extends EntityAIBase {

   private EntityCreature theEntity;
   private EntityLivingBase targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private int entityPosX;
   private int entityPosY;
   private int entityPosZ;
   private double field_75425_f;
   private float field_75426_g;


   public EntityAIZigZagTarget(EntityCreature par1EntityCreature, double par2, float par4) {
      this.theEntity = par1EntityCreature;
      this.field_75425_f = par2;
      this.field_75426_g = par4;
      this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
   }

   public boolean shouldExecute() {
      this.targetEntity = this.theEntity.getAttackTarget();
      if(this.targetEntity == null) {
         return false;
      } else if(this.targetEntity.getDistanceSqToEntity(this.theEntity) < (double)(this.field_75426_g * this.field_75426_g)) {
         return false;
      } else {
         PathEntity pathentity = this.theEntity.getNavigator().getPathToEntityLiving(this.targetEntity);
         if(pathentity != null && (float)pathentity.getCurrentPathLength() >= this.field_75426_g) {
            PathPoint pathpoint = pathentity.getPathPointFromIndex(MathHelper.floor((double)this.field_75426_g / 2.0D));
            this.entityPosX = pathpoint.xCoord;
            this.entityPosY = pathpoint.yCoord;
            this.entityPosZ = pathpoint.zCoord;
            Vec3d vec3 = RandomPositionGeneratorAlt.findRandomTargetBlockTowards(this.theEntity, (int)this.field_75426_g, 3, new Vec3d((double)this.entityPosX, (double)this.entityPosY, (double)this.entityPosZ));
            if(vec3 != null && this.targetEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.targetEntity.getDistanceSq((double)this.entityPosX, (double)this.entityPosY, (double)this.entityPosZ)) {
               this.movePosX = vec3.xCoord;
               this.movePosY = vec3.yCoord;
               this.movePosZ = vec3.zCoord;
               return true;
            }
         }

         return false;
      }
   }

   public boolean continueExecuting() {
      return !this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity(this.theEntity) > (double)(this.field_75426_g * this.field_75426_g);
   }

   public void startExecuting() {
      this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.field_75425_f);
   }

   public void updateTask() {
      this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
   }
}
