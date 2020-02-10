package noppes.npcs.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;

public class EntityAIFindShade extends EntityAIBase {

   private EntityCreature theCreature;
   private double shelterX;
   private double shelterY;
   private double shelterZ;
   private World theWorld;


   public EntityAIFindShade(EntityCreature par1EntityCreature) {
      this.theCreature = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(AiMutex.PASSIVE);
   }

   public boolean shouldExecute() {
      if(!this.theWorld.isDaytime()) {
         return false;
      } else if(!this.theWorld.canBlockSeeTheSky(MathHelper.floor(this.theCreature.posX), (int)this.theCreature.boundingBox.minY, MathHelper.floor(this.theCreature.posZ))) {
         return false;
      } else {
         Vec3d var1 = this.findPossibleShelter();
         if(var1 == null) {
            return false;
         } else {
            this.shelterX = var1.xCoord;
            this.shelterY = var1.yCoord;
            this.shelterZ = var1.zCoord;
            return true;
         }
      }
   }

   public boolean continueExecuting() {
      return !this.theCreature.getNavigator().noPath();
   }

   public void startExecuting() {
      this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, 1.0D);
   }

   private Vec3d findPossibleShelter() {
      Random var1 = this.theCreature.getRNG();

      for(int var2 = 0; var2 < 10; ++var2) {
         int var3 = MathHelper.floor(this.theCreature.posX + (double)var1.nextInt(20) - 10.0D);
         int var4 = MathHelper.floor(this.theCreature.boundingBox.minY + (double)var1.nextInt(6) - 3.0D);
         int var5 = MathHelper.floor(this.theCreature.posZ + (double)var1.nextInt(20) - 10.0D);
         float light = this.theWorld.getLightBrightness(var3, var4, var5) - 0.5F;
         if(!this.theWorld.canBlockSeeTheSky(var3, var4, var5) && light < 0.0F) {
            return new Vec3d((double)var3, (double)var4, (double)var5);
         }
      }

      return null;
   }
}
