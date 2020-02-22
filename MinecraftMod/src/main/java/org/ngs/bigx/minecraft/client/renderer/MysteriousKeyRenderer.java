package org.ngs.bigx.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.entity.item.ModelMysteriousKey;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.IItemRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.ItemMeshDefinition;


//public class MysteriousKeyRenderer implements IItemRenderer {
public class MysteriousKeyRenderer implements ItemRenderer {

	private ModelMysteriousKey modelMysteriousKey;
	
	public static ResourceLocation texture = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/items/mysteriouskey.png");
	
	public MysteriousKeyRenderer()
	{
		modelMysteriousKey = new ModelMysteriousKey();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type)
		{
		case EQUIPPED: return true;
		case EQUIPPED_FIRST_PERSON: return true;
		default: return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch(type)
		{
		case EQUIPPED: 
		{
			GL11.glPushMatrix();
			
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			
			GL11.glScalef(.5F, .5F, .5F);

			GL11.glRotatef(90, 0, 1f, 0);
			GL11.glRotatef(130, 1f, 0, 0);
			GL11.glRotatef(20, 1f, 0, 1f);
			GL11.glRotatef(90, 1f, 0, 0);

			GL11.glTranslatef(0.1f, -1.6f, 0f);
			
			this.modelMysteriousKey.renderModel(0.0625F);
			
			GL11.glPopMatrix();
		}
		default:
			break;
		}
	}

}
