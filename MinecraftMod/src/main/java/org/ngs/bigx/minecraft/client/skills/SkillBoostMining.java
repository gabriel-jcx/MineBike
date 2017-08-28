package org.ngs.bigx.minecraft.client.skills;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public class SkillBoostMining extends Skill {
	public static final double boostRate = 16; // Mining Boost by 16 (equivalent to the strongest mining tool)
	
	public SkillBoostMining(PedalingCombo pedalingCombo) {
		super(pedalingCombo, 5000, 2000, 960, "minebike:pedalinggaugeup");
	}
}
