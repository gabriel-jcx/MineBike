package org.ngs.bigx.minecraft;

import org.ngs.bigx.dictionary.objects.game.BiGXSuggestedGameProperties;
import org.ngs.bigx.minecraft.quests.QuestManager;

public class BigxContext {
	protected QuestManager QuestManagerClient;
	protected BiGX main = null;
	
	protected BiGXSuggestedGameProperties suggestedGameProperties = null;
	protected boolean suggestedGamePropertiesReady = false;
	
	public static BigxContext self = null;
}
