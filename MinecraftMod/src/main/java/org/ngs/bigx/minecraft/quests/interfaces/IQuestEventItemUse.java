package org.ngs.bigx.minecraft.quests.interfaces;
//import org.bukkit.event.player.P
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public interface IQuestEventItemUse {
	public void onItemUse(LivingEntityUseItemEvent.Start event);
}
