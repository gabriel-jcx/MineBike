package noppes.npcs.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.blocks.BlockTrigger;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileNpcContainer;
import noppes.npcs.blocks.tiles.TileWeaponRack;

public class BlockWeaponRack extends BlockTrigger {

   public BlockWeaponRack() {
      super(Blocks.planks);
   }

   public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(par1World.isRemote) {
         return true;
      } else {
         int meta = par1World.getBlockMetadata(i, j, k);
         if(meta >= 7) {
            --j;
         }

         TileWeaponRack tile = (TileWeaponRack)par1World.getTileEntity(i, j, k);
         float hit = hitX;
         if(tile.rotation == 2) {
            hit = 1.0F - hitX;
         }

         if(tile.rotation == 3) {
            hit = 1.0F - hitZ;
         }

         if(tile.rotation == 1) {
            hit = hitZ;
         }

         int selected = 2 - (int)((double)hit / 0.34D);
         ItemStack item = player.getCurrentEquippedItem();
         ItemStack weapon = tile.getStackInSlot(selected);
         if(item == null && weapon != null) {
            tile.setInventorySlotContents(selected, (ItemStack)null);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, weapon);
            par1World.markBlockForUpdate(i, j, k);
            this.updateSurrounding(par1World, i, j, k);
         } else {
            if(item == null || item.getItem() == null || item.getItem() instanceof ItemBlock) {
               return true;
            }

            if(item != null && weapon == null) {
               tile.setInventorySlotContents(selected, item);
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               par1World.markBlockForUpdate(i, j, k);
               this.updateSurrounding(par1World, i, j, k);
            }
         }

         return true;
      }
   }

   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
      par3List.add(new ItemStack(par1, 1, 1));
      par3List.add(new ItemStack(par1, 1, 2));
      par3List.add(new ItemStack(par1, 1, 3));
      par3List.add(new ItemStack(par1, 1, 4));
      par3List.add(new ItemStack(par1, 1, 5));
   }

   public int damageDropped(int par1) {
      return par1 % 7;
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      if(!par1World.isAirBlock(par2, par3 + 1, par4)) {
         par1World.setBlockToAir(par2, par3, par4);
      } else {
         int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
         l %= 4;
         TileColorable tile = (TileColorable)par1World.getTileEntity(par2, par3, par4);
         tile.rotation = l;
         par1World.setBlockMetadataWithNotify(par2, par3, par4, par6ItemStack.getItemDamage(), 2);
         par1World.setBlock(par2, par3 + 1, par4, this, par6ItemStack.getItemDamage() + 7, 2);
      }

   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      this.setBlockBoundsBasedOnState(world, x, y, z);
      return super.getCollisionBoundingBoxFromPool(world, x, y, z);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      if(meta >= 7) {
         --y;
      }

      TileEntity tileentity = world.getTileEntity(x, y, z);
      if(!(tileentity instanceof TileColorable)) {
         super.setBlockBoundsBasedOnState(world, x, y, z);
      } else {
         TileColorable tile = (TileColorable)tileentity;
         float xStart = 0.0F;
         float zStart = 0.0F;
         float xEnd = 1.0F;
         float zEnd = 1.0F;
         if(tile.rotation == 0) {
            zStart = 0.7F;
         } else if(tile.rotation == 2) {
            zEnd = 0.3F;
         } else if(tile.rotation == 3) {
            xStart = 0.7F;
         } else if(tile.rotation == 1) {
            xEnd = 0.3F;
         }

         if(meta >= 7) {
            this.setBlockBounds(xStart, -1.0F, zStart, xEnd, 0.8F, zEnd);
         } else {
            this.setBlockBounds(xStart, 0.0F, zStart, xEnd, 1.8F, zEnd);
         }

      }
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return var2 < 7?new TileWeaponRack():null;
   }

   public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_) {
      if(p_149681_5_ >= 7 && p_149681_1_.getBlock(p_149681_2_, p_149681_3_ - 1, p_149681_4_) == this) {
         p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ - 1, p_149681_4_);
      } else if(p_149681_5_ < 7 && p_149681_1_.getBlock(p_149681_2_, p_149681_3_ + 1, p_149681_4_) == this) {
         p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ + 1, p_149681_4_);
      }

   }

   public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
      TileNpcContainer tile = (TileNpcContainer)world.getTileEntity(x, y, z);
      if(tile != null) {
         tile.dropItems(world, x, y, z);
         world.notifyBlockChange(x, y, z, block);
         super.breakBlock(world, x, y, z, block, p_149749_6_);
      }
   }
}
