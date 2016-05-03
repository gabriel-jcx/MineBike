package org.ngs.bigx.minecraft.entity.item;

import org.ngs.bigx.minecraft.Main;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;





public abstract class EntityMinecar extends EntityMinecart
{
	
	
	public EntityMinecar(World p_i1712_1_) {
		super(p_i1712_1_);
		// TODO Auto-generated constructor stub
		
	}
	
	public EntityMinecar(World p_i1713_1_, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_) {
		super(p_i1713_1_, p_i1713_2_, p_i1713_4_, p_i1713_6_);
		// TODO Auto-generated constructor stub
	}

	
	
	
	public static void mainRegistry(){
		registerEntity();
	}
	
	public static void registerEntity(){
		createEntity(EntityTutMob.class, "mineCar", 0xFF000D, 0x001EFF);
	}
	
	public static void createEntity(Class entityClass, String entityName, int solidColor, int spotColor){
		int randId = EntityRegistry.findGlobalUniqueEntityId();
		
		EntityRegistry.registerGlobalEntityID(entityClass, entityName, randId);
		EntityRegistry.registerModEntity(entityClass, entityName, randId, Main.modInstance, 64, 1, true);
		EntityRegistry.addSpawn(entityClass, 2, 0, 1, EnumCreatureType.creature, BiomeGenBase.forest);
		createEgg(randId, solidColor, spotColor);
	
	}
	
	public static void createEgg(int randId, int solidColor, int spotColor){
		EntityList.entityEggs.put(Integer.valueOf(randId), new EntityList.EntityEggInfo(randId, solidColor,spotColor));
	
	}
	
	
	
	
}
