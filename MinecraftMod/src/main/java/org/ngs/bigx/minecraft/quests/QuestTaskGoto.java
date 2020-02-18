package org.ngs.bigx.minecraft.quests;

import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class QuestTaskGoto extends QuestTask {

	public Vec3d edge1, edge2;
	
	private int questTypeId = 2;
	
	public QuestTaskGoto(QuestManager questManager, EntityPlayer p, Vec3d e1, Vec3d e2, boolean required) {
		super(questManager, true);
		player = p;
		edge1 = e1;
		edge2 = e2;
		isRequired = required;
	}
	
	public QuestTaskGoto(QuestManager questManager, EntityPlayer p, Vec3d e1, Vec3d e2) {
		this(questManager, p,e1,e2,false);
	}

	@Override
	public void CheckComplete() {
		if ((player.posX >= edge1.x && player.posX <= edge2.x || player.posX <= edge1.x && player.posX >= edge2.x) &&
				(player.posY >= edge1.y && player.posY <= edge2.y || player.posY <= edge1.y && player.posY >= edge2.y) &&
				(player.posZ >= edge1.z && player.posZ <= edge2.z || player.posZ <= edge1.z && player.posZ >= edge2.z))
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
	public void unregisterEvents() { 
		synchronized (questManager) {
			QuestEventHandler.unregisterQuestEventCheckComplete(this);
		}
	}

	@Override
	public void registerEvents() {
		synchronized (questManager) {
			QuestEventHandler.registerQuestEventCheckComplete(this);
		}
	}

	@Override
	public void onCheckCompleteEvent() {
		CheckComplete();
	}
}
