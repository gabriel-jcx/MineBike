package org.ngs.bigx.minecraft.client.skills;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public class SkillBoostDamage extends Skill {
	public static final double boostRate = 4; // Damage Boost by 4
	
	public SkillBoostDamage(PedalingCombo pedalingCombo) {
		super(pedalingCombo, 10000, 3000, 3000, "minebike:powerup");
	}
}
