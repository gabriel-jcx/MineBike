package org.ngs.bigx.minecraft.items;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

//import net.minecraftforge.fml.common.registry.GameData;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.entity.MoverType;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
//import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BlockPositionMapping;
import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;

public class CustomFishHook extends EntityFishHook
{

    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public int shake;
    public OlReliable rod;
    public EntityPlayer angler;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private int fishPosRotationIncrements;
    private double fishX;
    private double fishY;
    private double fishZ;
    private double fishYaw;
    private double fishPitch;
    @SideOnly(Side.CLIENT)
    private double clientMotionX;
    @SideOnly(Side.CLIENT)
    private double clientMotionY;
    @SideOnly(Side.CLIENT)
    private double clientMotionZ;
    private static final String __OBFID = "CL_00001663";
    private boolean justSpawned;
    
    
    //Pulling Mechanic Variables
    
    //When beginPull is true the custom fishing mechanic will start in the onUpdate method
    private boolean beginPull = false;
    
    /*When fishing mechanic starts the tick count will count every tick that happens in a second
      This is used to keep track of each second*/
    private int tickCount = 0;
    
    //Activated when the required power level is achieved and counts how long they maintain the required power
    private int tickSuccess = 0;
    
    //Activated when the required power level is under half of the required power for too long
    private int tickFail = 0;
    
    //The number of times they click, used to increase the power level
    //HERE FOR DEBUGGING
	private double clickRate = 0;
	
	//Checks to see if the Power Level GUI has reached maximum height
	private int checkHeight = 0;
	
	//Holds the time in seconds needed to hold the power level
	private static int time = 5;
	
	//Holds the time in seconds needed to fail
		private static int timeF = 5;
	
	//Checks to see if the holdTimer GUI is initialized
	private boolean checkTime = false;
	
	//Checks to see if the failTime GUI is initialized
	private boolean checkFail = false;
	
	public static int fishingLocation = 0;
	
	//Checks the rarity of the fish in order to adjust the difficulty
	public static int difficulty; // 4 7 6 3
	
	//Sets the required power level they need to reach in order to catch the fish, based on difficulty
	public static int requiredPower;
	
	//The Number of fish the player had caught
	public static int numFish = 0;
	
	//Determines how much clickRate is decreased by, Used to make catching faster if power above certain limit
	public static int addPower;
	
	//Used to make catching go twice as fast when bonus is active
	public static int doubleTime = 1;
	
	CustomEntityItem entityitem;
	
	/*
	 * 
	 */
	private List<ArrayList<WeightedRandom>> fishingSpots = new ArrayList<ArrayList<WeightedRandom>>();
	
	
	//The Gui for the Power Level Display
 	public static HudString powerString = new HudString(-125, 20, "POWER LEVEL", true, false);
 	public static HudRectangle powerLvl = new HudRectangle(-145, 60, 40, 0, 0xe4344aff, true, true); // -145 60 40 -210
 	public static HudRectangle boxLeft = new HudRectangle(-148, 63, 3, -215, 0x000000ff, true, true);
 	public static HudRectangle boxRight = new HudRectangle(-105, 63, 3, -215, 0x000000ff, true, true);
 	public static HudRectangle boxBottom = new HudRectangle(-148, 63, 46, -3, 0x000000ff, true, true);
 	public static HudRectangle boxTop = new HudRectangle(-148, -152, 46, 3, 0x000000ff, true, true);
 	public static HudString holdTime = new HudString(0, 0, "Hold for " + time + " Seconds", 5, true, true);
 	public static HudString failTime = new HudString(0, 0, "Failure in " + 5 + " Seconds", 5, true, true);
	
	//List that stores the catchable custom fish
	public static List<WeightedSpawnerEntity> temp2;
	
    public static List<WeightedSpawnerEntity> func_174855_j()
    {
        return temp2;
    }

    //The Custom constructor, instantiates custom fish items
    public CustomFishHook(World worldIn, EntityPlayer fishingPlayer)
    {
    	super(worldIn, fishingPlayer);
    	justSpawned = true;
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.ignoreFrustumCheck = true;
        this.angler = fishingPlayer;
        
        if(worldIn.isRemote) System.out.println(this.angler.getName() + "<client");
       
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(fishingPlayer.posX, fishingPlayer.posY + (double)fishingPlayer.getEyeHeight(), fishingPlayer.posZ, fishingPlayer.rotationYaw, fishingPlayer.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);

        
        //adds all the common fish to every fish location
        for(int i = 0; i < 7; i++)
        {
        	//fishingSpots.add(new ArrayList<WeightedRandomFishable>());
            fishingSpots.add(new ArrayList<WeightedRandom>());
        	for (EnumFishType fish: EnumFishType.values())
			{
        		Item temp = MineBikeCustomItems.itemMap.get("item.ItemFish." + fish.getName());
                List<Item> collection=null;
                collection.add(temp);
        		if(fish.getType() == 0)
        			//fishingSpots.get(i).add(new WeightedRandomFishable(new ItemStack(temp, 1), fish.getWeight()));
                    fishingSpots.get(i).add( new WeightedRandom.getRandomItem(collection, fish.getWeight()));
			}
        }
            
        
        //Adds all spot specific fish to the correct fishing location
        	for (EnumFishType fish: EnumFishType.values())
			{
        		Item temp = MineBikeCustomItems.itemMap.get("item.ItemFish." + fish.getName());
        		if(fish.getType() != 0)
        		{
        		fishingSpots.get(fish.getType() - 1).add(new WeightedRandomFishable(new ItemStack(temp, 1), fish.getWeight()));
        		}
			}
        
        this.isImmuneToFire = true;
    }

    //Handles what happens when the hook is cast
    public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_)
    {
        float f2 = MathHelper.sqrt(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
        p_146035_1_ /= (double)f2;
        p_146035_3_ /= (double)f2;
        p_146035_5_ /= (double)f2;
        p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_1_ *= (double)p_146035_7_;
        p_146035_3_ *= (double)p_146035_7_;
        p_146035_5_ *= (double)p_146035_7_;
        this.motionX = p_146035_1_;
        this.motionY = p_146035_3_;
        this.motionZ = p_146035_5_;
        float f3 = MathHelper.sqrt(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_146035_1_, p_146035_5_) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_146035_3_, (double)f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    /*Contains the new Fishing Mechanic
     * The start of the onUpdate method outlines the custom fishing mechanic
     */
    @Override
    public void onUpdate()
    {
    	this.onEntityUpdate();
    	this.extinguish();
    	
    	BiGX.instance().clientContext.lock(true);
    	
    	if (justSpawned)
    	{
        	BiGX.instance().clientContext.lock(true);
        	justSpawned = false;
    	}
    	
    	
    	//New Fishing Mechanic Code
    	
    	//Causes the fishing mechanic to instantly trigger when the hook bobs bellow the water
    	if (beginPull == false && this.ticksCatchable > 0)
        {
        	beginPulling();
        	entityitem = new CustomEntityItem(this.world, this.posX, this.posY, this.posZ, this.getFishingResult());
        }
    	
    	//Stops the players movement
//    	angler.addPotionEffect(new PotionEffect(2, 100, 1000000000));
    	Minecraft mc = Minecraft.getMinecraft();
    	 
    	//Start of the pull mechanic
    	if(beginPull == true)
    	{
    		tickCount++;
    		//ClickRate here for debugging
    		clickRate -= .6;
//    		clickRate += .4;
    		clickRate = Math.max(0, clickRate);
    		checkHeight = getHeight();
    		
    		/*
    		 * Checks to see if the player failed to catch the fish
    		 * Waits 3 seconds for player to start fishing then if they are under half the power level
    		 * the failTime will start
    		 */
    		if(tickCount >= 80)
    		{
    			if(powerLvl.h >= -105)
    			{
    				tickFail++;
    				
        			if(checkFail == false)
        			{
        				failTime.text = "Failure in " + 5 + " Seconds";
        				HudManager.registerString(failTime);
        				checkFail = true;
        			}
        			
        			if(tickFail % 20 == 0)
        			{
        				timeF -= 1;
        				failTime.text = "Failure in " + timeF + " Seconds";
        			}
    				
    				if(tickFail >= 100)
    				{
            			HudManager.unregisterRectangle(powerLvl);
            			HudManager.unregisterRectangle(boxLeft);
            			HudManager.unregisterRectangle(boxRight);
            			HudManager.unregisterRectangle(boxBottom);
            			HudManager.unregisterRectangle(boxTop);
            			HudManager.unregisterString(powerString);
            			HudManager.unregisterString(failTime);
            			angler.fishEntity.func_146034_e();
            			checkFail = false;
            			timeF = 5;
            			beginPull = false;
    				}
    			}
    			else
    			{
    				timeF = 5;
        			checkFail = false;
        			tickFail = 0;
        			HudManager.unregisterString(failTime);
    			}
    		}
    		
    		//Changes the power bar's color and adjusts its height
    		if(checkHeight > -210)
    		{
    			powerLvl.h = checkHeight;
    			powerLvl.color = color(powerLvl.h);
    		}
        
    		else if(checkHeight >= 0)
    		{
    			powerLvl.h = 0;
    		}
    		//If the max height is reacher, make sure the height and color remains the same
    		else
    		{
    			powerLvl.h = -210;
    			powerLvl.color = 0x65f040ff;
    		}


    		//Sends in game message to player for specific data values
    		if(tickCount % 20 == 0)
    		{
    			mc.player.sendChatMessage("Power Level: " + clickRate);
    			mc.player.sendChatMessage("Height: " + powerLvl.h);
    			mc.player.sendChatMessage("CHANGE: " + BiGXPacketHandler.change);
    		}
    		
    		/*Once player has achieved required power level for specified tickSuccess, GUI gets unregistered
    		 * and hook is retracted
    		 */
    		if(getPower() >= getRequiredPower()) //30 52.5
    		{
    			HudManager.unregisterString(failTime);
    			if(getPower() >= getBonus())
    				doubleTime = 2;
    			else
    				doubleTime = 1;
    			
    			if(checkTime == false)
    			{
    				holdTime.text = "Hold for " + 5 + " Seconds";
    				HudManager.registerString(holdTime);
    				checkTime = true;
    			}
    			
    			tickSuccess++;
    			
    			//If the player is going a specified speed above required power, catching takes half the time
    			if(tickSuccess % ((int)(5) / doubleTime) == 0)
    			{
    				if(powerLvl.color == 0x65f040ff)
    					powerLvl.color = 0x40bd24ff;
    				else
    					powerLvl.color = 0x65f040ff;
    					
    			}
    			
    			//If the player is going a specified speed above required power, catching takes half the time
    			if(tickSuccess % (20 / doubleTime) == 0)
    			{
    				time -= 1;
    				holdTime.text = "Hold for " + time + " Seconds";
    			}
    			
    			//When time = 0 that means they have caught the fish so it unregisters everything and ends
    			//the mechanic
    			if(time == 0)
        		{
        			retractHook();
        			angler.fishEntity.func_146034_e();
        			beginPull = false;
        			HudManager.unregisterRectangle(powerLvl);
        			HudManager.unregisterRectangle(boxLeft);
        			HudManager.unregisterRectangle(boxRight);
        			HudManager.unregisterRectangle(boxBottom);
        			HudManager.unregisterRectangle(boxTop);
        			HudManager.unregisterString(powerString);
        			HudManager.unregisterString(failTime);
        			HudManager.unregisterString(holdTime);
        			checkTime = false;
        			doubleTime = 1;
        			clickRate = 0;
        		}
    		}
    		else
    		{
    			time = 5;
    			tickSuccess = 0;
    			HudManager.unregisterString(holdTime);
    			doubleTime = 1;
    			checkTime = false;
    		}
    	}

    	
    	
    	
    	//Normal OnUpdate Method
    	//DONT WORRY ABOUT THE REST IN ONUPDATE, IT SERVES TO FUNCTION AS A VANILLA FISHING ROD
        if (this.fishPosRotationIncrements > 0)
        {
            double d7 = this.posX + (this.fishX - this.posX) / (double)this.fishPosRotationIncrements;
            double d8 = this.posY + (this.fishY - this.posY) / (double)this.fishPosRotationIncrements;
            double d9 = this.posZ + (this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements;
            double d1 = MathHelper. wrapDegrees(this.fishYaw - (double)this.rotationYaw);
//            double d1 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d1 / (double)this.fishPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(d7, d8, d9);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
            if (!this.world.isRemote)
            {  
            	ItemStack itemstack = this.angler.getCurrentEquippedItem();

                if (this.angler.isDead || !this.angler.isEntityAlive() || itemstack == null || itemstack.getItem() != MineBikeCustomItems.itemMap.get("item.OlReliable") || this.getDistanceSqToEntity(this.angler) > 1024.0D)
                {
                	unRegister();
                	beginPull = false;
                    this.setDead();
                    this.angler.fishEntity = null;
                    return;
                }

                if (this.caughtEntity != null)
                {
                    if (!this.caughtEntity.isDead)
                    {
                        this.posX = this.caughtEntity.posX;
                        double d12 = (double)this.caughtEntity.height;
                        this.posY = this.caughtEntity.getEntityBoundingBox().minY + d12 * 0.8D;
                        this.posZ = this.caughtEntity.posZ;
                        return;
                    }

                    this.caughtEntity = null;
                }
            }

            if (this.shake > 0)
            {
                --this.shake;
            }

            if (this.inGround)
            {
                if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)) == this.inTile)
                {
                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else
            {
                ++this.ticksInAir;
            }

            Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec31, vec3);
            vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec3 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
            }

            Entity entity = null;

            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            //List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            double d2;

            for (int i = 0; i < list.size(); ++i)
            {
                Entity entity1 = (Entity)list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.angler || this.ticksInAir >= 5))
                {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f, (double)f, (double)f);
                    RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null)
                    {
                        d2 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d2 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d2;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit != null)
                {
                    if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0F))
                    {
                        this.caughtEntity = movingobjectposition.entityHit;
                    }
                }
                else
                {
                    this.inGround = true;
                }
            }

            if (!this.inGround)
            {
                this.move(MoverType.SELF,this.motionX, this.motionY, this.motionZ);
                //this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float f5 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

                for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f5) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
                {
                    ;
                }

                while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
                {
                    this.prevRotationPitch += 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw < -180.0F)
                {
                    this.prevRotationYaw -= 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
                {
                    this.prevRotationYaw += 360.0F;
                }

                this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
                this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
                float f6 = 0.92F;

                if (this.onGround || this.isCollidedHorizontally)//this.isCollidedHorizontally)
                {
                    f6 = 0.5F;
                }

                byte b0 = 5;
                double d10 = 0.0D;
                double d5;

                for (int j = 0; j < b0; ++j)
                {
                    AxisAlignedBB axisalignedbb1 = this.getEntityBoundingBox();
                    //AxisAlignedBB axisalignedbb1 = this.boundingBox;
                    double d3 = axisalignedbb1.maxY - axisalignedbb1.minY;
                    double d4 = axisalignedbb1.minY + d3 * (double)j / (double)b0;
                    d5 = axisalignedbb1.minY + d3 * (double)(j + 1) / (double)b0;
                    AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb1.minX, d4, axisalignedbb1.minZ, axisalignedbb1.maxX, d5, axisalignedbb1.maxZ);
                    if(this.world.isMaterialInBB(axisalignedbb2,Material.WATER));
                    //if (this.world.isAABBInMaterial(axisalignedbb2, Material.water))
                    {
                        d10 += 1.0D / (double)b0;
                    }
                    if(this.world.isMaterialInBB(axisalignedbb2,Material.LAVA))
                    //if (this.worldObj.isAABBInMaterial(axisalignedbb2, Material.lava))
                    {
                        d10 += 1.0D / (double)b0;
                    }
                }

                if (!this.world.isRemote && d10 > 0.0D)
                {
                    WorldServer worldserver = (WorldServer)this.world;
                    int k = 1;

                    if (this.rand.nextFloat() < 0.25F && this.world.canLightningStrikeAt(MathHelper.floor(this.posX), MathHelper.floor(this.posY) + 1, MathHelper.floor(this.posZ)))
                    {
                        k = 2;
                    }

                    if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY) + 1, MathHelper.floor(this.posZ))))
                    {
                        --k;
                    }

                    if (this.ticksCatchable > 0)
                    {
                        --this.ticksCatchable;

                        if (this.ticksCatchable <= 0)
                        {
                            this.ticksCaughtDelay = 0;
                            this.ticksCatchableDelay = 0;
                        }
                    }
                    else
                    {
                        float f1;
                        float f2;
                        double d6;
                        float f7;
                        double d11;

                        if (this.ticksCatchableDelay > 0)
                        {
                            this.ticksCatchableDelay -= k;

                            if (this.ticksCatchableDelay <= 0)
                            {
                                this.motionY -= 0.20000000298023224D;
                                SoundEvent se = new SoundEvent(new ResourceLocation("random.splash"));
                                this.playSound(se, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                                f1 = (float)MathHelper.floor(this.getEntityBoundingBox().minY);

                                // TODO: Figure out what this func_147487_a is renamed to
                                worldserver.func_147487_a("bubble", this.posX, (double)(f1 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                worldserver.func_147487_a("wake", this.posX, (double)(f1 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                this.ticksCatchable = MathHelper.getInt(this.rand, 10, 30);
                            }
                            else
                            {
                                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                                f1 = this.fishApproachAngle * 0.017453292F;
                                f7 = MathHelper.sin(f1);
                                f2 = MathHelper.cos(f1);
                                d5 = this.posX + (double)(f7 * (float)this.ticksCatchableDelay * 0.1F);
                                d11 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                                d6 = this.posZ + (double)(f2 * (float)this.ticksCatchableDelay * 0.1F);

                                if (this.rand.nextFloat() < 0.15F)
                                {
                                	worldserver.func_147487_a("bubble", d11, d5 - 0.10000000149011612D, d6, 1, (double)f7, 0.1D, (double)f2, 0.0D);
                                }

                                float f3 = f7 * 0.04F;
                                float f4 = f2 * 0.04F;
                                worldserver.func_147487_a("wake", d11, d5, d6, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                                worldserver.func_147487_a("wake", d11, d5, d6, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                            }
                        }
                        else if (this.ticksCaughtDelay > 0)
                        {
                            this.ticksCaughtDelay -= k;
                            f1 = 0.15F;

                            if (this.ticksCaughtDelay < 20)
                            {
                                f1 = (float)((double)f1 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
                            }
                            else if (this.ticksCaughtDelay < 40)
                            {
                                f1 = (float)((double)f1 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
                            }
                            else if (this.ticksCaughtDelay < 60)
                            {
                                f1 = (float)((double)f1 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
                            }

                            if (this.rand.nextFloat() < f1)
                            {
                                f7 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
                                f2 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
                                d5 = this.posX + (double)(MathHelper.sin(f7) * f2 * 0.1F);
                                d11 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                                d6 = this.posZ + (double)(MathHelper.cos(f7) * f2 * 0.1F);
                                worldserver.func_147487_a("splash", d11, d5, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                            }

                            if (this.ticksCaughtDelay <= 0)
                            {
                                this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
                                this.ticksCatchableDelay = 20;//MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                            }
                        }
                        else
                        {
                            this.ticksCaughtDelay = 20;//MathHelper.getRandomIntegerInRange(this.rand, 20, 20);
                        }
                    }

                    if (this.ticksCatchable > 0)
                    {
                        this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
                    }
                }

                d2 = d10 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * d2;

                if (d10 > 0.0D)
                {
                    f6 = (float)((double)f6 * 0.9D);
                    this.motionY *= 0.8D;
                }

                this.motionX *= (double)f6;
                this.motionY *= (double)f6;
                this.motionZ *= (double)f6;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }
    

    /*Checks to see what type of event is happening when player retracts hook
     * b0 = 0 inAir
     * b0 = 1 caughtFish
     * b0 = 2 inGround
     * b0 = 3 hookedOnEntity
     */
    public int handleHookRetraction()
    {
    	byte b0 = 0;
    	
    	//If they have caught a fish, activate new pull mechanic
    	if(beginPull == true)
    	{
    		b0 = 1;
//    		clickRate -= 20;
    		clickRate += 10;
    	}
    	else
    	{
            if (this.world.isRemote)
            {
                return 0;
            }
            else
            {
                
                if (this.caughtEntity != null)
                {
                    double d0 = this.angler.posX - this.posX;
                    double d2 = this.angler.posY - this.posY;
                    double d4 = this.angler.posZ - this.posZ;
                    double d6 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2 + d4 * d4);
                    double d8 = 0.1D;
                    this.caughtEntity.motionX += d0 * d8;
                    this.caughtEntity.motionY += d2 * d8 + (double)MathHelper.sqrt(d6) * 0.08D;
                    this.caughtEntity.motionZ += d4 * d8;
                    b0 = 3;
                }

                if (this.inGround)
                {
                    b0 = 2;
                }
            }            
    	}
    	return b0;
    }

    //Starts fishing mechanic and registers the Power Level GUI
    public void beginPulling()
    {
    	beginPull = true;
        HudManager.registerRectangle(powerLvl);
        HudManager.registerRectangle(boxLeft);
        HudManager.registerRectangle(boxRight);
        HudManager.registerRectangle(boxBottom);
        HudManager.registerRectangle(boxTop);
        HudManager.registerString(powerString);
    }
    
    //Used to blend the color of the power bar depending on its height
    public int color(int height)
    {
    	int temp = Math.abs(height);
    	int redHeight = temp - 117;
    	int greenValue = 0x00400000;
    	int redValue = 0xf0000000;
    	int alpha = 0x000040ff;
    	int addGreen = 0;
    	int subRed = 0;
    	if(temp <= 117 && temp >= 0)
    	{
    		addGreen += (int)(temp * 1.5);
    		greenValue = greenValue + (0x00010000 * addGreen);
    	}
    	else if(temp > 117 && temp <= 210)
    	{
    		greenValue = 0x00f00000;
    		subRed = (int)(redHeight * 1.5);
    		redValue = redValue - (0x01000000 * subRed);
    	}
    	return redValue + greenValue + alpha;
    }
    
    //Gives player a fishable and deletes the hook entity from the world
    //Also keeps track of how many fish the player has caught which is used in FishingQuest
    //to keep track of their minigame progress
    public void retractHook()
    {
    	OlReliable.clock = Clock.systemDefaultZone();
    	OlReliable.lastTime = OlReliable.clock.millis();
	    OlReliable.clockTimer = true;
	    OlReliable.delayMove();
        double d1 = this.angler.posX - this.posX;
        double d3 = this.angler.posY - this.posY;
        double d5 = this.angler.posZ - this.posZ;
        double d7 = (double)MathHelper.sqrt(d1 * d1 + d3 * d3 + d5 * d5);
        double d9 = 0.1D;
        entityitem.motionX = d1 * d9;
        entityitem.motionY = d3 * d9 + (double)MathHelper.sqrt(d7) * 0.08D;
        entityitem.motionZ = d5 * d9;
        ItemStack fish = entityitem.getItem();
        ResourceLocation fishLocation = new ResourceLocation("customnpcs", "textures/items/" + ((ItemFish)fish.getItem()).getName() + ".png");
        GuiMessageWindow.showMessageAndImage("You Caught a " + fish.getDisplayName() + "!", fishLocation, false);
        System.out.println("You Caught a " + fish.getDisplayName() + "!");
        System.out.println("You Caught a " + fish.getDisplayName() + "!");
        this.world.spawnEntity(entityitem);
        switch(difficulty)
        {
	    	case 4:
	    		 System.out.println("Easy");
	    		 numFish++;
	    		 break;
			case 7:
				 System.out.println("Uncommon");
				 numFish += 2;
				 break;
			case 6:
				 System.out.println("Rare");
				 numFish += 3;
				 break;
			case 3:
				 System.out.println("Legendary");
				 numFish += 4;
				 break;
        }

        this.angler.world.spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
    }
    
    //Unregisters all the GUI related to the fishing mechanic
    public static void unRegister()
    {
		HudManager.unregisterRectangle(powerLvl);
		HudManager.unregisterRectangle(boxLeft);
		HudManager.unregisterRectangle(boxRight);
		HudManager.unregisterRectangle(boxBottom);
		HudManager.unregisterRectangle(boxTop);
		HudManager.unregisterString(powerString);
		HudManager.unregisterString(holdTime);
		HudManager.unregisterString(failTime);
    }
    
    //Gets the speed the player must achieve in order to catch the specified tier of fish
    /*
     * TIERS OF FISH
     * 4 = COMMON
     * 7 = UNCOMMON
     * 6 = RARE
     * 3 = LEGENDARY
     */
    private double getRequiredPower()
    {
    	//4 7 6 3
    	switch(difficulty)
    	{
    		case 4:
    			return 50; //15
    		case 7:
    			return 60; //21
    		case 6:
    			return 70; //30
    		case 3:
    			return 80; //35
    		default:
    			return 60; //21
    	}
    }
    
    private int getPower()
    {
    	return (BiGXPacketHandler.change * 4);
    }
    
    //Returns the how much the height of the power bar is incrementing based on the tier of fish
    private int getHeight()
    {
    	return (int) (-210 * ((double)(getPower()) / (double) getRequiredPower()));
    }
    
    //Returns how much extra speed the player must achieve before catching goes twice as fast
    private int getBonus()
    {
    	//4 7 6 3
    	switch(difficulty)
    	{
	    	case 4:
				return 60;
			case 7:
				return 70;
			case 6:
				return 80;
			case 3:
				return 90;
			default:
				return 70;
    	}
    }
    
    @Override
    public void setDead()
    {
    	OlReliable.clock = Clock.systemDefaultZone();
    	OlReliable.lastTime = OlReliable.clock.millis();
	    OlReliable.clockTimer = true;
	    OlReliable.delayMove();
    	super.setDead();
    }
    
    //Gets the types of items and custom fish the player can catch
    private ItemStack getFishingResult()
    {
    	ItemStack itemstack;
    	this.angler.triggerAchievement(StatList.FISH_CAUGHT);
    	switch(fishingLocation) 
    	{
    		case 1:
    			System.out.println("Lake Worked");
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(0))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    		case 2:
    			System.out.println("Spooky Worked");
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(1))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    		case 3:
    			System.out.println("Glacial Worked");
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(2))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    		case 4:
    			System.out.println("Koi Pond Worked");
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(3))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    		case 5:
    			System.out.println("Deep Sea Worked");
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(4))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    		case 6:
    			System.out.println("Nether Worked");
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(5))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    		default:
    			System.out.println("defualt Worked");
    			int rand = (int)(Math.random() * 6) + 1;
                itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, fishingSpots.get(rand))).func_150708_a(this.rand);
                difficulty = ((ItemFish)(itemstack.getItem())).getRarity();
                break;
    	}
    	return itemstack;
    }
}