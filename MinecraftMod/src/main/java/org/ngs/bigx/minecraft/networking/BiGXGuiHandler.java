package org.ngs.bigx.minecraft.networking;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import org.ngs.bigx.minecraft.BiGXContainerTileEntity;
import org.ngs.bigx.minecraft.client.gui.BiGXGuiTileEntity;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;
import net.minecraftforge.fml.common.network.IGuiHandler;
//import cpw.mods.fml.common.network.IGuiHandler;

public class BiGXGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			BlockPos pos = new BlockPos(x,y,z);
			return new BiGXContainerTileEntity(player.inventory, (TileEntityQuestChest) world.getTileEntity(pos));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			BlockPos pos = new BlockPos(x,y,z);
			return new BiGXGuiTileEntity(player.inventory, (TileEntityQuestChest) world.getTileEntity(pos));
		}
		return null;
	}

}
