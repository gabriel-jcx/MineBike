package org.ngs.bigx.minecraft.entity.item;

import cpw.mods.fml.client.registry.RenderingRegistry;
import org.ngs.bigx.minecraft.client.renderer.EntityTankRenderer;
import org.ngs.bigx.minecraft.client.renderer.EntityRacingCarRenderer;

public class MineBikeEntityRegistry {
	public static void RegisterMineBikeEntities()
	{
		EntityRacingCar.mainRegistry();
		EntityTank.mainRegistry();
	}
	
	public static void RegisterMineBikeRenders()
	{
    	RenderingRegistry.registerEntityRenderingHandler(EntityTank.class, new EntityTankRenderer(new ModelTank(), 0));
    	RenderingRegistry.registerEntityRenderingHandler(EntityRacingCar.class, new EntityRacingCarRenderer(new ModelRacingCar(), 0));
    	
	}
}
