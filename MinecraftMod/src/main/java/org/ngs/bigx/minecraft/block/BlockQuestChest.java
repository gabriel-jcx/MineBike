package org.ngs.bigx.minecraft.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
//import org.apache.commons.lang.enums.Enum;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;
import org.ngs.bigx.utility.Names;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraft.util.EnumFacing;

public class BlockQuestChest extends BlockContainer {

	Random random;

	public BlockQuestChest(Material mat) {
		super(mat);
	}

	public BlockQuestChest() {
		super(Material.ROCK);

//		setBlockName(Names.Blocks.QUEST_CHEST);
//		// Affects block breaking particles -- does NOT change chest texture!
//		// (see TileEntityQuestChestRenderer to identify the chest model texture)
//		setBlockTextureName("log_oak");
//		setCreativeTab(CreativeTabs.tabDecorations);

		random = new Random();
	}

    //@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    //@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    //@Override
    public int getRenderType()
    {
        return 22;
    }

    protected boolean canSilkHarvest()
    {
    	return false;
    }

    //@Override
    public void breakBlock(World world, int i, int j, int k, Block block, int n)
    {
    	/*
    	TileEntityQuestChest te = (TileEntityQuestChest) world.getTileEntity(i, j, k);
    	if (te != null)
    	{
    		for (int l = 0; l < te.getSizeInventory(); l++)
    		{
    			ItemStack itemStack = te.getStackInSlot(l);
    			if (itemStack == null)
    			{
    				// Skip null slots
    				continue;
    			}
    			// Scatter drops
    			float f1 = random.nextFloat() * 0.8F + 0.1F;
    			float f2 = random.nextFloat() * 0.8F + 0.1F;
                float f3 = random.nextFloat() * 0.8F + 0.1F;
                while (itemStack.stackSize > 0)
                {
                    int i1 = random.nextInt(21) + 10;
                    if (i1 > itemStack.stackSize)
                    {
                        i1 = itemStack.stackSize;
                    }
                    itemStack.stackSize -= i1;
                    EntityItem entityItem = new EntityItem(world, (float) te.xCoord + f1, (float) te.yCoord + 1F + f2, (float) te.zCoord + f3,
                            new ItemStack(itemStack.getItem(), i1, itemStack.getItemDamage()));
                    float f4 = 0.05F;
                    entityItem.motionX = (float) random.nextGaussian() * f4;
                    entityItem.motionY = (float) random.nextGaussian() * f4 + 0.2F;
                    entityItem.motionZ = (float) random.nextGaussian() * f4;
                    if (itemStack.hasTagCompound())
                    {
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                    }
                    world.spawnEntityInWorld(entityItem);
                }
    		}
    	}
    	*/
        BlockPos pos = new BlockPos(i,j,k);
        //IBlockState state = new IBlockState(block,n);
    	super.breakBlock(world,pos,block.getDefaultState());
		//);
    }

    /*
    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
    	super.onBlockAdded(world, i, j, k);
    	world.markBlockForUpdate(i, j, k);
    }
    */

    //@Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityLiving, ItemStack itemStack)
    {
    	byte chestFacing = 0;
    	int facing = MathHelper.floor((double) ((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

    	switch(facing)
    	{
    	case 0:
    		chestFacing = 2;
    		break;
    	case 1:
    		chestFacing = 5;
    		break;
    	case 2:
    		chestFacing = 3;
    		break;
    	case 3:
    		chestFacing = 4;
    		break;
    	default:
    		break;
    	}

    	TileEntity tileEntity = world.getTileEntity(new BlockPos(i, j, k));
    	if (tileEntity != null && tileEntity instanceof TileEntityQuestChest)
    	{
    		TileEntityQuestChest teQuestChest = (TileEntityQuestChest) tileEntity;
    		teQuestChest.setFacing(chestFacing);
    		world.markBlockRangeForRenderUpdate(new BlockPos(i, j, k),new BlockPos(i, j, k));
    	}
    }

    //@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3)
    {
    	TileEntity tE = world.getTileEntity(new BlockPos(i,j,k));

    	if (!world.isRemote)
    	{
    		return ((TileEntityQuestChest) tE).activate(world, i, j, k, player);
    	}
    	return true;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityQuestChest();
	}


}
