package org.ngs.bigx.minecraft.entity.lotom.stat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatRegistry {

protected static Map<Class<? extends Entity>, Set<EntityStat>> registry = Maps.newHashMap();

	@SubscribeEvent
	public void onEntityContruct(EntityConstructing event) {
		if (registry.containsKey(event.getEntity().getClass())) {
		//ExtendedProperties.register(event.getEntity());
		}
	}
	
	protected static String getName(String name) {
		String original = name;
		int count = 1;
	
		while (registry.containsKey(name)) {
		name = String.format("%s%d", original, count++);
		}
		
		return name;
	}
	
	public static void registerStat(EntityStat stat, Class<? extends Entity> clazz) {
		if (!registry.containsKey(clazz)) {
			registry.put(clazz, new HashSet());
		}
		
		registry.get(clazz).add(stat);
	}
	
	public static EntityStat getStat(Entity entity, String name) {
		return ExtendedProperties.get(entity).getStat(name);
	}
	

	public static EntityStat getStat(Entity entity, EntityStat stat) {
		return getStat(entity, stat.getName());
	}

}