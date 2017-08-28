package org.ngs.bigx.minecraft.client.skills;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public class SkillManager {
	private int selectedSkillIndex = 0; // 0: moving, 1:better hit damage, 2:better mining damage
	private PedalingCombo pedalingCombo;
	
	private List<Skill> skills;
	
	public enum enumUseSkillResult {
		SUCCESS, 
		FAIL_LOCKED,
		FAIL_NOT_ENOUGH_MANA,
		FAIL_SKILL_IN_USE,
		FAIL_COOLTIME,
		FAIL_SKILLS_NOT_REGISTERED,
		FAIL_UNKNOWN,
	}
	
	public SkillManager(PedalingCombo pedalingCombo)
	{
		this.pedalingCombo = pedalingCombo;
		this.skills = new ArrayList<Skill>();
		
		this.skills.add(new SkillBoostSpeed(pedalingCombo));
		this.skills.add(new SkillBoostDamage(pedalingCombo));
		this.skills.add(new SkillBoostMining(pedalingCombo));
		
//		/**
//		 * TODO: Need to remove this tesing purpose code at the release
//		 */
//		for(Skill skill : this.skills)
//		{
//			skill.unlockSkillState();
//		}
	}
	
	public void switchSkill()
	{
		this.selectedSkillIndex ++;
		
		if(this.selectedSkillIndex >= this.skills.size())
			this.selectedSkillIndex = 0;
	}
	
	public Skill getCurrentlySelectedSkill()
	{
		return this.skills.get(selectedSkillIndex);
	}

	public enumUseSkillResult useCurrentlySelectedSkill() {
		Skill selectedSkill = this.getCurrentlySelectedSkill();
		
		if(selectedSkill == null)
			return enumUseSkillResult.FAIL_SKILLS_NOT_REGISTERED;
		
		if(selectedSkill.use())
			return enumUseSkillResult.SUCCESS;
		else
		{
			switch(selectedSkill.getSkillState())
			{
			case LOCKED:
				return enumUseSkillResult.FAIL_LOCKED;
			case IDLE:
				if(!selectedSkill.hasEnoughMana())
					return enumUseSkillResult.FAIL_NOT_ENOUGH_MANA;
				else
					return enumUseSkillResult.FAIL_UNKNOWN;
			case EFFECTIVE:
				return enumUseSkillResult.FAIL_SKILL_IN_USE;
			case COOLTIME:
				return enumUseSkillResult.FAIL_COOLTIME;
			default:
				return enumUseSkillResult.FAIL_UNKNOWN;
			}
		}
	}
	
	public void updateSkillTime()
	{
		for(Skill skill : this.skills)
		{
			skill.updateTime();
		}
	}

	public int getSelectedSkillIndex() {
		return selectedSkillIndex;
	}

	public List<Skill> getSkills() {
		return skills;
	}
}
