package org.ngs.bigx.minecraft.quests;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;

import org.ngs.bigx.dictionary.objects.game.BiGXGameTag;
import org.ngs.bigx.dictionary.protocol.Specification;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestEventCheckComplete;
import org.ngs.bigx.minecraft.quests.interfaces.IQuestTask;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public abstract class QuestTask implements IQuestTask, IQuestEventCheckComplete, Runnable {
	protected boolean completed;
	public EntityPlayer player;
	protected boolean isRequired;
	protected QuestManager questManager;
	protected boolean isActive = false;
	protected BigxClientContext clientContext;
	protected BigxServerContext serverContext;
	
	protected int questTypeId = 0;
	
	public static enum QuestActivityTagEnum{  // CAPPED by 15
		STARTED(1),
		PAUSED(2),
		ACCOMPLESHED(3),
		FAILED(4),
		STOPPED(5);
		
		private int questActivityTagEnum = 0;
		
		QuestActivityTagEnum(int res) {
			questActivityTagEnum = res;
		}
		
		public int getQuestActivityTagEnum() {
			return questActivityTagEnum;
		}
	};
	
	public QuestTask(QuestManager questManager, boolean isMainTask)
	{
		this.questManager = questManager;
		this.isRequired = isMainTask;
		this.clientContext = questManager.getClientContext();
		this.serverContext = questManager.getServerContext();
	}
	
	public QuestManager getQuestManager() {
		return questManager;
	}

	public void setQuestManager(QuestManager questManager) {
		this.questManager = questManager;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * Determines this task is optional or not
	 * @return false if the task is optional. true, otherwise.
	 */
	public boolean IsMainTask()
	{
		return this.isRequired;
	}
	
	protected void sendQuestGameTag(QuestActivityTagEnum questActivityTagEnum)
	{
		// SEND GAME TAG - Quest 0x(GAME TAG[0xFF])(questActivityTagEnum [0xF])
		try {
			int questTypeEnum = ((0xff & questTypeId) << 4) | (0xf & questActivityTagEnum.getQuestActivityTagEnum());
			BiGXGameTag biGXGameTag = new BiGXGameTag();
			biGXGameTag.setTagName("" + (Specification.GameTagType.GAMETAG_ID_QUEST_BEGINNING | questTypeEnum));
			
			BigxClientContext.sendGameTag(biGXGameTag);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiGXNetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiGXInternalGamePluginExcpetion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void activateTask()
	{
		this.isActive = true;
		this.registerEvents();
		new Thread(this).start();
		
		this.sendQuestGameTag(QuestActivityTagEnum.STARTED);
	}
	
	public void reactivateTask()
	{
		this.isActive = true;
		new Thread(this).start();
		
		this.sendQuestGameTag(QuestActivityTagEnum.STARTED);
	}
	
	public void deactivateTask()
	{
		this.isActive = false;
		this.unregisterEvents();
		
		this.sendQuestGameTag(QuestActivityTagEnum.STOPPED);
	}
	
	public abstract void init();
	public abstract void unregisterEvents();
	public abstract void registerEvents();
}
