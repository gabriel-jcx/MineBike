package org.ngs.bigx.minecraft.networking;

import org.ngs.bigx.minecraft.BiGXContainerTileEntity;
import org.ngs.bigx.minecraft.client.gui.BiGXGuiTileEntity;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class BiGXGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new BiGXContainerTileEntity(player.inventory, (TileEntityQuestChest) world.getTileEntity(x,y,z));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new BiGXGuiTileEntity(player.inventory, (TileEntityQuestChest) world.getTileEntity(x,y,z));
		}
		return null;
	}

}
