package org.ngs.bigx.minecraft.entity.item;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class MineBikeEntityRegistry {
	public static void RegisterMineBikeEntities()
	{
		EntityRacingCar.mainRegistry();
		EntityTank.mainRegistry();
	}
	
	public static void RegisterMineBikeRederings()
	{
    	RenderingRegistry.registerEntityRenderingHandler(EntityTank.class, new RenderTank(new ModelTank(), 0));
    	RenderingRegistry.registerEntityRenderingHandler(EntityRacingCar.class, new RenderRacingCar(new ModelRacingCar(), 0));
	}
}
