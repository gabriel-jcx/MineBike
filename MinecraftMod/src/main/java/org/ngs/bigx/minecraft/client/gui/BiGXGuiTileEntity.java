package org.ngs.bigx.minecraft.client.gui;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXContainerTileEntity;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class BiGXGuiTileEntity extends GuiContainer {
	
	private IInventory playerInventory;
	private TileEntityQuestChest te;
	
	public BiGXGuiTileEntity(IInventory playerInventory, TileEntityQuestChest te) {
		super(new BiGXContainerTileEntity(playerInventory, te));
		
		this.playerInventory = playerInventory;
		this.te = te;
		
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
		// TODO Draw the following items here:
		// * GUI background image
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/questcomplete.png"));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		// TODO Draw the following items here:
		// * Text strings
		// * GUI Button
	}
}
