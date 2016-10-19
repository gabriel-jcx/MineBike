package org.ngs.bigx.minecraft;

import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class BiGXContainerTileEntity extends Container {
	
	private TileEntityQuestChest te;
	private int slotPixelSize = 18;
	private int slotsXOffset = 8;
	private int slotsYOffset = 54;
	
	public BiGXContainerTileEntity(IInventory playerInventory, TileEntityQuestChest te) {
		this.te = te;
		
		// Slots 0-17, IDs 0-17
		for (int y = 0; y < 2; ++y) {
			for (int x = 0; x < 9; ++x) {
				addSlotToContainer(new Slot(te, x + y * 2, slotsXOffset + x * slotPixelSize, slotsYOffset + y * slotPixelSize));
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return te.isUseableByPlayer(player);
	}

}
