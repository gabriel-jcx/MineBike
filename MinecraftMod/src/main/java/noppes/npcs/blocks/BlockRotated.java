package noppes.npcs.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumFacing;
import noppes.npcs.blocks.tiles.TileColorable;

public abstract class BlockRotated extends BlockContainer {

   private Block block;
   public int renderId = -1;


   protected BlockRotated(Block block) {
      super(block.getMaterial());
      this.block = block;
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      int l = MathHelper.floor((double)(par5EntityLivingBase.rotationYaw * (float)this.maxRotation() / 360.0F) + 0.5D) & this.maxRotation() - 1;
      l %= this.maxRotation();
      TileColorable tile = (TileColorable)par1World.getTileEntity(par2, par3, par4);
      tile.rotation = l;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      this.setBlockBoundsBasedOnState(world, x, y, z);
      return super.getCollisionBoundingBoxFromPool(world, x, y, z);
   }

   public int maxRotation() {
      return 4;
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

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, EnumFacing side) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {}

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int meta) {
      return this.block.getIcon(p_149691_1_, meta);
   }
}
