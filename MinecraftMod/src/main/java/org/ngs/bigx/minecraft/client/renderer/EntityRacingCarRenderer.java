package org.ngs.bigx.minecraft.client.renderer;

import net.minecraft.client.Minecraft;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.RefStrings;
import org.ngs.bigx.minecraft.entity.item.EntityRacingCar;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EntityRacingCarRenderer extends RenderLiving {
	private static final ResourceLocation mobTextures = new ResourceLocation(BiGX.TEXTURE_PREFIX , "/textures/items/entityRacingCar.png");

	public EntityRacingCarRenderer(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(Minecraft.getMinecraft().getRenderManager(),p_i1262_1_, p_i1262_2_);
	}

	protected ResourceLocation getEntityTexture(EntityRacingCar entity) {
		return mobTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityRacingCar)entity);
	}

}
