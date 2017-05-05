package org.ngs.bigx.minecraft.context;

import org.ngs.bigx.dictionary.objects.game.BiGXSuggestedGameProperties;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.quests.QuestManager;

public class BigxContext {
	protected QuestManager QuestManagerClient;
	protected BiGX main = null;
	
	public BiGXSuggestedGameProperties suggestedGameProperties = null;
	protected boolean suggestedGamePropertiesReady = false;
	
	public static BigxContext self = null;
	
	protected QuestManager questManager = null;
	
	public BigxContext(BiGX main)
	{
		this.main = main;
	}

	public void setQuestManager(QuestManager questManager)
	{
		this.questManager = questManager;
	}
	
	public QuestManager getQuestManager()
	{
		return this.questManager;
	}

	public boolean isSuggestedGamePropertiesReady() {
		return suggestedGamePropertiesReady;
	}
}
