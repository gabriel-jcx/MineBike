package org.ngs.bigx.minecraft.items;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import org.ngs.bigx.minecraft.items.CustomPainting.EnumArt;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

// TODO: figure out how to set the direction and the hanging direction of the item.
public class CustomPainting extends EntityHanging implements IEntityAdditionalSpawnData
{
    public CustomPainting.EnumArt art;

    public CustomPainting(World p_i1599_1_)
    {
        super(p_i1599_1_);
    }

    //public CustomPainting(World p_i1600_1_, int p_i1600_2_, int p_i1600_3_, int p_i1600_4_, int p_i1600_5_)
    public CustomPainting(World p_i1600_1_,BlockPos pos, int p_i1600_5_)
    {

        super(p_i1600_1_, pos);
        ArrayList<EnumArt> arraylist = new ArrayList<EnumArt>();
        CustomPainting.EnumArt[] aenumart = CustomPainting.EnumArt.values();
        int i1 = aenumart.length;

        for (int j1 = 0; j1 < i1; ++j1)
        {
            CustomPainting.EnumArt enumart = aenumart[j1];
            this.art = enumart;
            this.setDirection(p_i1600_5_);

            if (this.onValidSurface())
            {
                arraylist.add(enumart);
            }
        }

        if (!arraylist.isEmpty())
        {
            this.art = (CustomPainting.EnumArt)arraylist.get(this.rand.nextInt(arraylist.size()));
        }

        this.setDirection(p_i1600_5_);
    }

    @SideOnly(Side.CLIENT)
    public CustomPainting(World p_i1601_1_, BlockPos pos, int p_i1601_5_, String p_i1601_6_)
    {
        //BlockPos pos = new BlockPos(p_i1601_2_, p_i1601_3_, p_i1601_4_);
        this(p_i1601_1_,pos, p_i1601_5_);
        CustomPainting.EnumArt[] aenumart = CustomPainting.EnumArt.values();
        int i1 = aenumart.length;

        for (int j1 = 0; j1 < i1; ++j1)
        {
            CustomPainting.EnumArt enumart = aenumart[j1];

            if (enumart.title.equals(p_i1601_6_))
            {
                this.art = enumart;
                break;
            }
        }

        this.setDirection(p_i1601_5_);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.setString("Motive", this.art.title);
        super.writeEntityToNBT(p_70014_1_);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        String s = p_70037_1_.getString("Motive");
        CustomPainting.EnumArt[] aenumart = CustomPainting.EnumArt.values();
        int i = aenumart.length;

        for (int j = 0; j < i; ++j)
        {
            CustomPainting.EnumArt enumart = aenumart[j];

            if (enumart.title.equals(s))
            {
                this.art = enumart;
            }
        }

        if (this.art == null)
        {
            this.art = CustomPainting.EnumArt.Kebab;
        }

        super.readEntityFromNBT(p_70037_1_);
    }

    public int getWidthPixels()
    {
        return this.art.sizeX;
    }

    public int getHeightPixels()
    {
        return this.art.sizeY;
    }

    /**
     * Called when this entity is broken. Entity parameter may be null.
     */
    public void onBroken(Entity p_110128_1_)
    {
        if (p_110128_1_ instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)p_110128_1_;

            if (entityplayer.capabilities.isCreativeMode)
            {
                return;
            }
        }

        this.entityDropItem(new ItemStack(MineBikeCustomItems.itemMap.get("item.CustomPainting")), 0.0F);
    }

    @Override
    public void playPlaceSound() {
        //TODO: implement playPlaceSound
    }

    public static enum EnumArt
    {
        Kebab("Kebab", 16, 16, 0, 0),
        Hamburger("HamBurger", 64, 64, 64, 64),
        HamburgerBun("HamBurgerBun", 64, 64, 128, 64),
        Lettuce("Lettuce", 64, 64, 256, 0),
        Sandwich("Sandwich", 64, 64, 320, 0),
        SandwichBread("SandwichBread", 64, 64, 384, 0),
        Potato("Potato", 64, 64, 448, 0),
        Bowl("Bowl", 64, 64, 256, 64),
        RawChicken("RawChicken", 64, 64, 320, 64),
//        Aztec("Aztec", 16, 16, 16, 0),
//        Alban("Alban", 16, 16, 32, 0),
//        Aztec2("Aztec2", 16, 16, 48, 0),
//        Bomb("Bomb", 16, 16, 64, 0),
//        Plant("Plant", 16, 16, 80, 0),
//        Wasteland("Wasteland", 16, 16, 96, 0),
//        Pool("Pool", 32, 16, 0, 32),
//        Courbet("Courbet", 32, 16, 32, 32),
//        Sea("Sea", 32, 16, 64, 32),
//        Sunset("Sunset", 32, 16, 96, 32),
//        Creebet("Creebet", 32, 16, 128, 32),
//        Wanderer("Wanderer", 16, 32, 0, 64),
//        Graham("Graham", 16, 32, 16, 64),
//        Match("Match", 32, 32, 0, 128),
//        Bust("Bust", 32, 32, 32, 128),
//        Stage("Stage", 32, 32, 64, 128),
//        Void("Void", 32, 32, 96, 128),
//        SkullAndRoses("SkullAndRoses", 32, 32, 128, 128),
//        Wither("Wither", 32, 32, 160, 128),
//        Fighters("Fighters", 64, 32, 0, 96),
//        Pointer("Pointer", 64, 64, 0, 192),
//        Pigscene("Pigscene", 64, 64, 64, 192),
//        BurningSkull("BurningSkull", 64, 64, 128, 192),
//        Skeleton("Skeleton", 64, 48, 192, 64),
//        DonkeyKong("DonkeyKong", 64, 48, 192, 112),
    	JahFish("JahFish", 64, 64, 192, 192);
        /** Holds the maximum length of paintings art title. */
        public static final int maxArtTitleLength = "SkullAndRoses".length();
        /** Painting Title. */
        public final String title;
        public final int sizeX;
        public final int sizeY;
        public final int offsetX;
        public final int offsetY;

        private static final String __OBFID = "CL_00001557";

        private EnumArt(String p_i1598_3_, int p_i1598_4_, int p_i1598_5_, int p_i1598_6_, int p_i1598_7_)
        {
            this.title = p_i1598_3_;
            this.sizeX = p_i1598_4_;
            this.sizeY = p_i1598_5_;
            this.offsetX = p_i1598_6_;
            this.offsetY = p_i1598_7_;
        }
    }



    @Override
    public void writeSpawnData(ByteBuf buffer) {
           EnumArt[] var2 = EnumArt.values();
           buffer.writeInt(this.hangingDirection);
            for (int var3 = 0; var3 < var2.length; ++var3)
            {
            if (var2[var3] == this.art)
            {
                buffer.writeInt(var3);
            return;
            }
            }
            }        
    

    @Override
    public void readSpawnData(ByteBuf additionalData) {    
        this.hangingDirection = additionalData.readInt();
    int var2 = additionalData.readInt();
    this.art = EnumArt.values()[var2];
    }}