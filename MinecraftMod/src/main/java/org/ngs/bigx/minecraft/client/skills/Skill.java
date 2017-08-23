package org.ngs.bigx.minecraft.client.skills;

import org.ngs.bigx.minecraft.bike.PedalingCombo;

public abstract class Skill {
	public enum enumSkillState {
		LOCKED,
		IDLE,
		EFFECTIVE,
		COOLTIME,
	}

	protected enumSkillState skillState = enumSkillState.LOCKED;
	
	protected PedalingCombo pedalingCombo;
	
	protected int requiredMana = 100;
	
	protected int level = 1;

	protected long effectiveTimeTimestamp = 0;
	protected int effectiveTimeMax = 3000;
	protected int effectiveTimeCurrent = 0;

	protected long coolTimeTimestamp = 0;
	protected int coolTimeMax = 10000;	// unit is the milisecond
	protected int coolTimeCurrent = 0;	// Same unit
	
	public Skill(PedalingCombo pedalingCombo, int effectiveTimeMax, int coolTimeMax, int requiredMana)
	{
		this.pedalingCombo = pedalingCombo;
		this.effectiveTimeMax = effectiveTimeMax;
		this.coolTimeMax = coolTimeMax;
		this.requiredMana = requiredMana;
	}
	
	public boolean hasEnoughMana()
	{	
		return this.pedalingCombo.hasEnoughGauge(requiredMana);
	}
	
	public boolean use()
	{
		if(!this.hasEnoughMana())
			return false;
		
		if(!switchState(enumSkillState.EFFECTIVE))
			return false;
		
		this.effectiveTimeTimestamp = System.currentTimeMillis();
		this.effectiveTimeCurrent = this.effectiveTimeMax;

		return this.pedalingCombo.decreaseGauge(requiredMana);
	}
	
	public boolean switchState(enumSkillState skillState)
	{
		switch(this.skillState)
		{
		case LOCKED:
			return false;
		case IDLE:
			if(skillState != enumSkillState.EFFECTIVE)
				return false;
			break;
		case EFFECTIVE:
			if(skillState != enumSkillState.COOLTIME)
				return false;
			break;
		case COOLTIME:
			if(skillState != enumSkillState.IDLE)
				return false;
			break;
		};
		
		this.skillState = skillState;
		
		return true;
	}
	
	public void updateTime()
	{
		switch(this.skillState)
		{
		case EFFECTIVE:
			this.updateEffectiveTime();
			break;
		case COOLTIME:
			this.updateCoolTime();
			break;
		default:
			break;
		}
	}
	
	private void updateEffectiveTime()
	{
		if(this.skillState != enumSkillState.EFFECTIVE)
			return;
		
		this.effectiveTimeCurrent = (int) (this.effectiveTimeMax - (System.currentTimeMillis() - this.effectiveTimeTimestamp));
		
		if(this.effectiveTimeCurrent < 0)
		{
			this.switchState(enumSkillState.COOLTIME);
			this.coolTimeTimestamp = System.currentTimeMillis();
			this.coolTimeCurrent = this.coolTimeMax;
			this.effectiveTimeCurrent = 0;
		}
	}

	private void updateCoolTime()
	{
		if(this.skillState != enumSkillState.COOLTIME)
			return;
		
		this.coolTimeCurrent = (int) (this.coolTimeMax - (System.currentTimeMillis() - this.coolTimeTimestamp));
		
		if(this.coolTimeCurrent < 0)
		{
			this.switchState(enumSkillState.IDLE);
			this.resetSkill();
		}
	}
	
	public void unlockSkillState()
	{
		this.skillState = enumSkillState.IDLE;
	}
	
	public void resetSkill()
	{
		this.skillState = enumSkillState.IDLE;
		this.effectiveTimeTimestamp = 0;
		this.coolTimeTimestamp = 0;
		this.coolTimeCurrent = this.coolTimeMax;
		this.effectiveTimeCurrent = this.effectiveTimeMax;
	}

	public enumSkillState getSkillState() {
		return skillState;
	}

	public int getRequiredMana() {
		return requiredMana;
	}

	public int getEffectiveTimeMax() {
		return effectiveTimeMax;
	}

	public int getEffectiveTimeCurrent() {
		return effectiveTimeCurrent;
	}

	public int getCoolTimeMax() {
		return coolTimeMax;
	}

	public int getCoolTimeCurrent() {
		return coolTimeCurrent;
	}
}
