package org.ngs.bigx.minecraft.quests.interfaces;

import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public interface IQuestEventItemUse {
	public void onItemUse(PlayerUseItemEvent.Start event);
}
