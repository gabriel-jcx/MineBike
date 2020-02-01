package noppes.npcs.blocks;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.util.math.AxisAlignedBB;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.IIcon;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.BlockChair;
import noppes.npcs.blocks.tiles.TileColorable;
import noppes.npcs.blocks.tiles.TileCouchWood;

public class BlockCouchWood extends BlockContainer {

   public int renderId = -1;


   public BlockCouchWood() {
      super(Material.wood);
   }

   public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
      ItemStack item = player.inventory.getCurrentItem();
      if(item != null && item.getItem() == Items.dye) {
         int meta = par1World.getBlockMetadata(i, j, k);
         if(meta >= 7) {
            --j;
         }

         TileColorable tile = (TileColorable)par1World.getTileEntity(i, j, k);
         int color = BlockColored.func_150031_c(item.getItemDamage());
         if(tile.color != color) {
            NoppesUtilServer.consumeItemStack(1, player);
            tile.color = color;
            par1World.markBlockForUpdate(i, j, k);
         }

         return true;
      } else {
         return BlockChair.MountBlock(par1World, i, j, k, player);
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
      return par1;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int x, int y, int z) {
      return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)y + 0.5D, (double)(z + 1));
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      l %= 4;
      TileCouchWood tile = (TileCouchWood)par1World.getTileEntity(par2, par3, par4);
      tile.rotation = l;
      tile.color = 15 - par6ItemStack.getItemDamage();
      par1World.setBlockMetadataWithNotify(par2, par3, par4, par6ItemStack.getItemDamage(), 2);
      this.updateModel(par1World, par2, par3, par4, tile);
      this.onNeighborBlockChange(par1World, par2 + 1, par3, par4, this);
      this.onNeighborBlockChange(par1World, par2 - 1, par3, par4, this);
      this.onNeighborBlockChange(par1World, par2, par3, par4 + 1, this);
      this.onNeighborBlockChange(par1World, par2, par3, par4 - 1, this);
      this.updateModel(par1World, par2, par3, par4, tile);
      par1World.markBlockForUpdate(par2, par3, par4);
   }

   public void onNeighborBlockChange(World worldObj, int x, int y, int z, Block block) {
      if(!worldObj.isRemote && block == this) {
         TileEntity tile = worldObj.getTileEntity(x, y, z);
         if(tile != null && tile instanceof TileCouchWood) {
            this.updateModel(worldObj, x, y, z, (TileCouchWood)tile);
            worldObj.markBlockForUpdate(x, y, z);
         }
      }
   }

   private void updateModel(World world, int x, int y, int z, TileCouchWood tile) {
      if(!world.isRemote) {
         int meta = tile.getBlockMetadata();
         if(tile.rotation == 0) {
            tile.hasLeft = this.compareTiles(tile, x - 1, y, z, world, meta);
            tile.hasRight = this.compareTiles(tile, x + 1, y, z, world, meta);
         } else if(tile.rotation == 2) {
            tile.hasLeft = this.compareTiles(tile, x + 1, y, z, world, meta);
            tile.hasRight = this.compareTiles(tile, x - 1, y, z, world, meta);
         } else if(tile.rotation == 1) {
            tile.hasLeft = this.compareTiles(tile, x, y, z - 1, world, meta);
            tile.hasRight = this.compareTiles(tile, x, y, z + 1, world, meta);
         } else if(tile.rotation == 3) {
            tile.hasLeft = this.compareTiles(tile, x, y, z + 1, world, meta);
            tile.hasRight = this.compareTiles(tile, x, y, z - 1, world, meta);
         }

      }
   }

   private boolean compareTiles(TileCouchWood tile, int x, int y, int z, World world, int meta) {
      int meta2 = world.getBlockMetadata(x, y, z);
      if(meta2 != meta) {
         return false;
      } else {
         TileEntity tile2 = world.getTileEntity(x, y, z);
         if(tile2 != null && tile2 instanceof TileCouchWood) {
            TileCouchWood couch = (TileCouchWood)tile2;
            return tile.rotation == couch.rotation;
         } else {
            return false;
         }
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return this.renderId;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {}

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int meta) {
      return Blocks.planks.getIcon(p_149691_1_, meta);
   }

   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileCouchWood();
   }
}
