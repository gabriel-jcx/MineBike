package org.ngs.bigx.minecraft.client.skills;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public class SkillBoostMining extends Skill {
	public static final double boostRate = 16; // Mining Boost by 16 (equivalent to the strongest mining tool)
	
	private int skillTypeId = 3;
	
	public SkillBoostMining(PedalingCombo pedalingCombo) {
		super(pedalingCombo, 10000, 2000, 1920, "minebike:pedalinggaugeup");
	}
}
