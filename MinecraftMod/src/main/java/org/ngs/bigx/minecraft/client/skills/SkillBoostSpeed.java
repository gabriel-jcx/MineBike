package org.ngs.bigx.minecraft.client.skills;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public class SkillBoostSpeed extends Skill {
	public static final double boostRate = .2; // 20% Speed Boost
	
	private int skillTypeId = 4;
	
	public SkillBoostSpeed(PedalingCombo pedalingCombo) {
		super(pedalingCombo, 5000, 2000, 960, "minebike:pedalinglevelup");
	}
}
