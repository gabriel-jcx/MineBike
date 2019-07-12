package org.ngs.bigx.minecraft.items;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.items.EnumFishType;


public class CustomFishHook extends EntityFishHook
{

	public static final List<WeightedRandomFishable> JUNK = Arrays.asList(new WeightedRandomFishable[] {(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10)});
    public static final List<WeightedRandomFishable> TREASURE = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).func_150707_a()});
    public static final List<WeightedRandomFishable> FISH = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13), new WeightedRandomFishable(new ItemStack(Items.fish, 0), 10)});
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
    
    
    //Pulling Mechanic Variables
    
    //When beginPull is true the custom fishing mechanic will start in the onUpdate method
    private boolean beginPull = false;
    
    /*When fishing mechanic starts the tick count will count every tick that happens in a second
      This is used to keep track of each second*/
    private int tickCount = 0;
    
    //Activated when the required power level is achieved and counts how long they maintain the required power
    private int tickSuccess = 0;
    
    //The number of times they click, used to increase the power level
	private double clickRate = 0;
	
	//Checks to see if the Power Level GUI has reached maximum height
	private int checkHeight = 0;
	
	//The Gui for the Power Level Display
 	HudString powerString = new HudString(-125, 20, "POWER LEVEL", true, false);
	HudRectangle powerLvl = new HudRectangle(-145, 60, 40, 0, 0xe4344aff, true, true); // -145 60 40 -210
	HudRectangle boxLeft = new HudRectangle(-148, 63, 3, -215, 0x000000ff, true, true);
	HudRectangle boxRight = new HudRectangle(-105, 63, 3, -215, 0x000000ff, true, true);
	HudRectangle boxBottom = new HudRectangle(-148, 63, 46, -3, 0x000000ff, true, true);
	HudRectangle boxTop = new HudRectangle(-148, -152, 46, 3, 0x000000ff, true, true);
	
	//List that stores the catchable custom fish
	public static List<WeightedRandomFishable> temp2;
	
    public static List<WeightedRandomFishable> func_174855_j()
    {
        return temp2;
    }

    //The Custom constructor, instantiates custom fish items
    public CustomFishHook(World worldIn, EntityPlayer fishingPlayer)
    {
    	super(worldIn, fishingPlayer);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.ignoreFrustumCheck = true;
        this.angler = fishingPlayer;
        
        if(worldIn.isRemote) System.out.println(this.angler.getCommandSenderName() + "<client");
       
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

        temp2 = new ArrayList<WeightedRandomFishable>();
        ArrayList<Item> item = BiGX.customItems;
        for(Item temp: item)
        {
        	for (EnumFishType fish: EnumFishType.values())
			{
        		if(temp.getUnlocalizedName().equals("item.ItemFish." + fish.getName()))
        		{
//        			System.out.println(Item.getIdFromItem(temp));
        			temp2.add(new WeightedRandomFishable(new ItemStack(temp, 1), 1));
        		}
			}
        }

        
//        for(WeightedRandomFishable temp: FISH)
//        {
//        	System.out.println(temp);
//        }
    }

    public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_)
    {
        float f2 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
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
        float f3 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
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
    	
    	
    	//New Fishing Mechanic Code
    	
    	//Stops the players movement
    	angler.addPotionEffect(new PotionEffect(2, 100, 100));
    	Minecraft mc = Minecraft.getMinecraft();
    	 
    	//Start of the pull mechanic
    	if(beginPull == true)
    	{
    		tickCount++;
    		clickRate -= .6;
    		clickRate = Math.max(0, clickRate);
    		checkHeight = (int)-(clickRate * 7);
    		
    		//Makes sure Power Level GUI is below the max height
    		if(checkHeight > -210)
    		{
        		System.out.println(powerLvl.h);
    			powerLvl.h = (int)-(clickRate * 7);
    			powerLvl.color = 0xe4344aff;
    		}
    		else
    		{
    			powerLvl.h = -210;
    			powerLvl.color = 0x84E501ff;
    		}


    		//Sends in game message to player for specific data values
    		if(tickCount % 20 == 0)
    		{
    			mc.thePlayer.sendChatMessage("Power Level: " + clickRate);
    			mc.thePlayer.sendChatMessage("Height: " + powerLvl.h);
    		}
    		
    		/*Once player has achieved required power level for specified tickSuccess, GUI gets unregistered
    		 * and hook is retracted
    		 */
    		if(clickRate >= 30) //30
    		{
    			tickSuccess++;
    			if(tickSuccess>= 100) //100
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
        		}
    		}
    		else
    		{
    			tickSuccess = 0;
    		}
    	}

    	
    	
    	
    	//Normal OnUpdate Method
        if (this.fishPosRotationIncrements > 0)
        {
            double d7 = this.posX + (this.fishX - this.posX) / (double)this.fishPosRotationIncrements;
            double d8 = this.posY + (this.fishY - this.posY) / (double)this.fishPosRotationIncrements;
            double d9 = this.posZ + (this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements;
            double d1 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d1 / (double)this.fishPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(d7, d8, d9);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
            if (!this.worldObj.isRemote)
            {  
            	ItemStack itemstack = this.angler.getCurrentEquippedItem();

                if (this.angler.isDead || !this.angler.isEntityAlive() || itemstack == null || this.getDistanceSqToEntity(this.angler) > 1024.0D)
                {
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
                        this.posY = this.caughtEntity.boundingBox.minY + d12 * 0.8D;
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
                if (this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.inTile)
                {
                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                    	System.out.println("inground?");
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

            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            double d2;

            for (int i = 0; i < list.size(); ++i)
            {
                Entity entity1 = (Entity)list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.angler || this.ticksInAir >= 5))
                {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec31, vec3);

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
                movingobjectposition = new MovingObjectPosition(entity);
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
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
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

                if (this.onGround || this.isCollidedHorizontally)
                {
                    f6 = 0.5F;
                }

                byte b0 = 5;
                double d10 = 0.0D;
                double d5;

                for (int j = 0; j < b0; ++j)
                {
                    AxisAlignedBB axisalignedbb1 = this.boundingBox;
                    double d3 = axisalignedbb1.maxY - axisalignedbb1.minY;
                    double d4 = axisalignedbb1.minY + d3 * (double)j / (double)b0;
                    d5 = axisalignedbb1.minY + d3 * (double)(j + 1) / (double)b0;
                    AxisAlignedBB axisalignedbb2 = AxisAlignedBB.getBoundingBox(axisalignedbb1.minX, d4, axisalignedbb1.minZ, axisalignedbb1.maxX, d5, axisalignedbb1.maxZ);

                    if (this.worldObj.isAABBInMaterial(axisalignedbb2, Material.water))
                    {
                        d10 += 1.0D / (double)b0;
                    }
                }

                if (!this.worldObj.isRemote && d10 > 0.0D)
                {
                    WorldServer worldserver = (WorldServer)this.worldObj;
                    int k = 1;

                    if (this.rand.nextFloat() < 0.25F && this.worldObj.canLightningStrikeAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ)))
                    {
                        k = 2;
                    }

                    if (this.rand.nextFloat() < 0.5F && !this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ)))
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
                                this.playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                                f1 = (float)MathHelper.floor_double(this.boundingBox.minY);
                                worldserver.func_147487_a("bubble", this.posX, (double)(f1 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                worldserver.func_147487_a("wake", this.posX, (double)(f1 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                            }
                            else
                            {
                                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                                f1 = this.fishApproachAngle * 0.017453292F;
                                f7 = MathHelper.sin(f1);
                                f2 = MathHelper.cos(f1);
                                d5 = this.posX + (double)(f7 * (float)this.ticksCatchableDelay * 0.1F);
                                d11 = (double)((float)MathHelper.floor_double(this.boundingBox.minY) + 1.0F);
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
                                d11 = (double)((float)MathHelper.floor_double(this.boundingBox.minY) + 1.0F);
                                d6 = this.posZ + (double)(MathHelper.cos(f7) * f2 * 0.1F);
                                worldserver.func_147487_a("splash", d11, d5, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                            }

                            if (this.ticksCaughtDelay <= 0)
                            {
                                this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
                                this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                            }
                        }
                        else
                        {
                            this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                            this.ticksCaughtDelay -= EnchantmentHelper.func_151387_h(this.angler) * 20 * 5;
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
    		clickRate++;
    	}
    	else
    	{
            if (this.worldObj.isRemote)
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
                    double d6 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d4 * d4);
                    double d8 = 0.1D;
                    this.caughtEntity.motionX += d0 * d8;
                    this.caughtEntity.motionY += d2 * d8 + (double)MathHelper.sqrt_double(d6) * 0.08D;
                    this.caughtEntity.motionZ += d4 * d8;
                    b0 = 3;
                }
                else if (this.ticksCatchable > 0)
                {
                	beginPulling();
                    b0 = 1;
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
    
    //Gives player a fishable and deletes the hook entity from the world
    public void retractHook()
    {
        EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.getFishingResult());
//        System.out.println(entityitem);
        double d1 = this.angler.posX - this.posX;
        double d3 = this.angler.posY - this.posY;
        double d5 = this.angler.posZ - this.posZ;
        double d7 = (double)MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
        double d9 = 0.1D;
        entityitem.motionX = d1 * d9;
        entityitem.motionY = d3 * d9 + (double)MathHelper.sqrt_double(d7) * 0.08D;
        entityitem.motionZ = d5 * d9;
        this.worldObj.spawnEntityInWorld(entityitem);
        this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
    }
    
    //Gets the types of items and custom fish the player can catch
    private ItemStack getFishingResult()
    {
        float f = this.worldObj.rand.nextFloat();
        int i = EnchantmentHelper.func_151386_g(this.angler);
        int j = EnchantmentHelper.func_151387_h(this.angler);
        
        if (true)
        {
        	this.angler.addStat(net.minecraftforge.common.FishingHooks.getFishableCategory(f, i, j).stat, 1);
//        	return net.minecraftforge.common.FishingHooks.getRandomFishable(this.rand, f, i, j);
        	return ((WeightedRandomFishable)WeightedRandom.getRandomItem(rand, temp2)).func_150708_a(rand);
        }

        float f1 = 0.1F - (float)i * 0.025F - (float)j * 0.01F;
        float f2 = 0.05F + (float)i * 0.01F - (float)j * 0.01F;
        f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
        f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);

        if (f < f1)
        {
        	this.angler.addStat(StatList.field_151183_A, 1);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).func_150708_a(this.rand);
        }
        else
        {
            f -= f1;

            if (f < f2)
            {
            	System.out.println("Here");
            	this.angler.addStat(StatList.field_151184_B, 1);
                return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, TREASURE)).func_150708_a(this.rand);
            }
            else
            {
                float f3 = f - f2;
                this.angler.triggerAchievement(StatList.fishCaughtStat);
                return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, temp2)).func_150708_a(this.rand);
            }
        }
    }
}