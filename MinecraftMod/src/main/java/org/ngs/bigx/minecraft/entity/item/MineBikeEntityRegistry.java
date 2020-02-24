package org.ngs.bigx.minecraft.entity.item;

import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.util.ResourceLocation;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.renderer.EntityRacingCarRenderer;
import org.ngs.bigx.minecraft.client.renderer.EntityTankRenderer;
import org.ngs.bigx.minecraft.items.CustomPainting;
import org.ngs.bigx.minecraft.items.RenderCustomPainting;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MineBikeEntityRegistry {
	public static void RegisterMineBikeEntities()
	{
		EntityRacingCar.mainRegistry();
		EntityTank.mainRegistry();
		ResourceLocation loc = new ResourceLocation("minebike_resource");
		EntityRegistry.registerModEntity(loc,CustomPainting.class, "CustomPainting", 4895,
				BiGX.modInstance, 160, Integer.MAX_VALUE, false, 0,0 );
		//EntityRegistry.registerModEntity(CustomPainting.class, "CustomPainting", 4895, BiGX.modInstance, 160, Integer.MAX_VALUE, false);
	}
	
	public static void RegisterMineBikeRenders()
	{
		// TODO: write the renderFactory class for each renderer
//    	RenderingRegistry.registerEntityRenderingHandler(EntityTank.class, new EntityTankRenderer(new ModelTank(), 0));
//    	RenderingRegistry.registerEntityRenderingHandler(EntityTank.class, EntityTankRenderer.getFac);
////    	RenderingRegistry.registerEntityRenderingHandler(EntityRacingCar.class, new EntityRacingCarRenderer(new ModelRacingCar(), 0));
////		RenderingRegistry.registerEntityRenderingHandler(CustomPainting.class,new RenderCustomPainting().getRenderManager());
////    	RenderingRegistry.registerEntityRenderingHandler(CustomPainting.class, new RenderCustomPainting());
    	
	}
}
