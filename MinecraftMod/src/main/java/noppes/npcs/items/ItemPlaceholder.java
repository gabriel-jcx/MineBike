package noppes.npcs.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemPlaceholder extends ItemBlock {

   public ItemPlaceholder(Block p_i45328_1_) {
      super(p_i45328_1_);
      this.setHasSubtypes(true);
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName(par1ItemStack) + "_" + par1ItemStack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return super.field_150939_a.getIcon(0, par1);
   }
}
