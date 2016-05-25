package org.ngs.bigx.minecraft.entity.item;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.RefStrings;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTank extends RenderLiving {
	private static final ResourceLocation mobTextures = new ResourceLocation(Main.TEXTURE_PREFIX , "/textures/items/entityTank.png");

	public RenderTank(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
	}

	protected ResourceLocation getEntityTexture(EntityTank entity) {
		return mobTextures;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityTank)entity);
	}

}
