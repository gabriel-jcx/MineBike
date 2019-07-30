package org.ngs.bigx.minecraft.entity.item;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.renderer.EntityRacingCarRenderer;
import org.ngs.bigx.minecraft.client.renderer.EntityTankRenderer;
import org.ngs.bigx.minecraft.items.CustomPainting;
import org.ngs.bigx.minecraft.items.RenderCustomPainting;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class MineBikeEntityRegistry {
	public static void RegisterMineBikeEntities()
	{
		EntityRacingCar.mainRegistry();
		EntityTank.mainRegistry();
		EntityRegistry.registerModEntity(CustomPainting.class, "CustomPainting", 4895, BiGX.modInstance, 160, Integer.MAX_VALUE, false);
	}
	
	public static void RegisterMineBikeRenders()
	{
    	RenderingRegistry.registerEntityRenderingHandler(EntityTank.class, new EntityTankRenderer(new ModelTank(), 0));
    	RenderingRegistry.registerEntityRenderingHandler(EntityRacingCar.class, new EntityRacingCarRenderer(new ModelRacingCar(), 0));
    	RenderingRegistry.registerEntityRenderingHandler(CustomPainting.class, new RenderCustomPainting());
    	
	}
}
