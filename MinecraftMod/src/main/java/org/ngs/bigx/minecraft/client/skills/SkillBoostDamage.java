package org.ngs.bigx.minecraft.client.skills;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public class SkillBoostDamage extends Skill {
	public static final double boostRate = .4; // 20% Damage Boost
	
	public SkillBoostDamage(PedalingCombo pedalingCombo) {
		super(pedalingCombo, 3000, 5000, 150);
	}
}
