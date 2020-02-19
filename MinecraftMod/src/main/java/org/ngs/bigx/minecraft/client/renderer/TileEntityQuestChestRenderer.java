package org.ngs.bigx.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.tileentity.TileEntityQuestChest;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityQuestChestRenderer extends TileEntitySpecialRenderer {
	
	private final ModelChest model;
	
	public TileEntityQuestChestRenderer()
	{
		this.model = new ModelChest();
	}
	
	private void adjustRotatePivotViaMeta(World world, int i, int j, int k)
	{
		int meta = world.getBlockMetadata(i, j, k);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		bindTexture(new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/tileentity/questChest.png"));
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		int facing = ((TileEntityQuestChest)tileEntity).getFacing();
		int k = 0;
		switch (facing) {
		case 2:
			k = 180;
			break;
		case 3:
			k = 0;
			break;
		case 4:
			k = 90;
			break;
		case 5:
			k = -90;
			break;
		}
		GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		model.renderAll();
		GL11.glPopMatrix();
	}
	
	private void adjustLightFixture(World world, int i, int j, int k, Block block)
	{
		Tessellator tess = Tessellator.instance;
		
		float brightness = block.getLightValue(world, i, j, k);
		int skyBrightness = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int mod = skyBrightness % 65536;
		int div = skyBrightness / 65536;
		
		tess.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) mod, div);
	}
}
