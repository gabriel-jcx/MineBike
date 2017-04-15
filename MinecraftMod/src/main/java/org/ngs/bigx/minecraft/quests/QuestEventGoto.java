package org.ngs.bigx.minecraft.quests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class QuestEventGoto implements IQuestEvent {

	private boolean completed;
	public Vec3 edge1, edge2;
	public EntityPlayer player;
	
	public QuestEventGoto(EntityPlayer p, Vec3 e1, Vec3 e2) {
		player = p;
		edge1 = e1;
		edge2 = e2;
	}
	
	@Override
	public boolean IsComplete() {
		return completed;
	}

	@Override
	public void Run() {
		
	}

	@Override
	public void CheckComplete() {
		if ((player.posX >= edge1.xCoord && player.posX <= edge2.xCoord || player.posX <= edge1.xCoord && player.posX >= edge2.xCoord) &&
				(player.posY >= edge1.yCoord && player.posY <= edge2.yCoord || player.posY <= edge1.yCoord && player.posY >= edge2.yCoord) &&
				(player.posZ >= edge1.zCoord && player.posZ <= edge2.zCoord || player.posZ <= edge1.zCoord && player.posZ >= edge2.zCoord))
			completed = true;
	}
}
