package org.ngs.bigx.minecraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.sql.SQLOutput;

public class ItemCustomPainting extends Item
{

    private final Class hangingEntityClass;

    public ItemCustomPainting(Class entityClass)
    {
    	super();
        this.hangingEntityClass = entityClass;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    public EnumFacing getEnumFacingDirection(int val) {
        if (val == EnumFacing.UP.getIndex()) {
            return EnumFacing.UP;
        }else if(val == EnumFacing.DOWN.getIndex()){
            return EnumFacing.DOWN;
        }else if(val == EnumFacing.NORTH.getIndex()){
            return EnumFacing.NORTH;
        }else if(val == EnumFacing.SOUTH.getIndex()){
            return EnumFacing.SOUTH;
        }else if(val == EnumFacing.WEST.getIndex()){
            return EnumFacing.WEST;
        }else if(val == EnumFacing.EAST.getIndex()){
            return EnumFacing.EAST;
        }
        System.out.println("ERROR!!!!!! this passing a wrong value into getEnumFacingDirection, 0 <= val <= 6");
        return null;
    }
    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (p_77648_7_ == 0)
        {
            return false;
        }
        else if (p_77648_7_ == 1)
        {
            return false;
        }
        else
        {

            EnumFacing facingdir = getEnumFacingDirection(p_77648_7_);

//            int i1 = Direction.facingToDirection[p_77648_7_];
            EntityHanging entityhanging = this.createHangingEntity(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, facingdir);
            BlockPos position = new BlockPos(p_77648_4_, p_77648_5_, p_77648_6_);
            if (!p_77648_2_.canPlayerEdit(position, facingdir, p_77648_1_))
            {
                return false;
            }
            else
            {
                if (entityhanging != null && entityhanging.onValidSurface())
                {
                    if (!p_77648_3_.isRemote)
                    {
                        p_77648_3_.spawnEntity(entityhanging);
                    }
                    p_77648_1_.setCount(p_77648_1_.getCount() - 1 );
                    //--p_77648_1_.stackSize;
                }

                return true;
            }
        }
    }

    private EntityHanging createHangingEntity(World p_82810_1_, int p_82810_2_, int p_82810_3_, int p_82810_4_, EnumFacing p_82810_5_)
    {
        BlockPos pos = new BlockPos(p_82810_2_, p_82810_3_, p_82810_4_);
        return new CustomPainting(p_82810_1_,pos, p_82810_5_);
    }
    
}
