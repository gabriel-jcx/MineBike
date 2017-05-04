package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class QuestTaskGoto implements IQuestTask {

	private boolean completed;
	public Vec3 edge1, edge2;
	public EntityPlayer player;
	
	public QuestTaskGoto(EntityPlayer p, Vec3 e1, Vec3 e2) {
		player = p;
		edge1 = e1;
		edge2 = e2;
	}
	
	@Override
	public boolean IsComplete() {
		return completed;
	}

	@Override
	public void Run(final LevelSystem levelSys) {
		
	}

	@Override
	public void CheckComplete() {
		if ((player.posX >= edge1.xCoord && player.posX <= edge2.xCoord || player.posX <= edge1.xCoord && player.posX >= edge2.xCoord) &&
				(player.posY >= edge1.yCoord && player.posY <= edge2.yCoord || player.posY <= edge1.yCoord && player.posY >= edge2.yCoord) &&
				(player.posZ >= edge1.zCoord && player.posZ <= edge2.zCoord || player.posZ <= edge1.zCoord && player.posZ >= edge2.zCoord))
			completed = true;
	}

	@Override
	public boolean IsMainTask() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTaskDescrption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskName() {
		// TODO Auto-generated method stub
		return null;
	}
}
