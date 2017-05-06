package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class QuestTaskGoto extends QuestTask {

	public Vec3 edge1, edge2;
	
	public QuestTaskGoto(QuestManager questManager, EntityPlayer p, Vec3 e1, Vec3 e2, boolean required) {
		super(questManager, true);
		player = p;
		edge1 = e1;
		edge2 = e2;
		isRequired = required;
	}
	
	public QuestTaskGoto(QuestManager questManager, EntityPlayer p, Vec3 e1, Vec3 e2) {
		this(questManager, p,e1,e2,false);
	}

	@Override
	public void run(final LevelSystem levelSys) {
		
	}

	@Override
	public void CheckComplete() {
		if ((player.posX >= edge1.xCoord && player.posX <= edge2.xCoord || player.posX <= edge1.xCoord && player.posX >= edge2.xCoord) &&
				(player.posY >= edge1.yCoord && player.posY <= edge2.yCoord || player.posY <= edge1.yCoord && player.posY >= edge2.yCoord) &&
				(player.posZ >= edge1.zCoord && player.posZ <= edge2.zCoord || player.posZ <= edge1.zCoord && player.posZ >= edge2.zCoord))
			completed = true;
	}

	@Override
	public String getTaskDescription() {
		return "QuestTaskGoto Description";
	}

	@Override
	public String getTaskName() {
		return QuestTaskGoto.class.toString();
	}

	@Override
	public void run() {	}

	@Override
	public void init() { }

	@Override
	public void unregisterEvents() { }

	@Override
	public void registerEvents() {
	}
}
