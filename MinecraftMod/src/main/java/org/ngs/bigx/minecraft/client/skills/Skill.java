package org.ngs.bigx.minecraft.client.skills;

import java.net.SocketException;
import java.net.UnknownHostException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import org.ngs.bigx.dictionary.objects.game.BiGXGameTag;
import org.ngs.bigx.dictionary.protocol.Specification;
import org.ngs.bigx.minecraft.bike.PedalingCombo;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxContext.LOGTYPE;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

public abstract class Skill {
	public enum enumSkillState {
		LOCKED,
		IDLE,
		EFFECTIVE,
		COOLTIME,
	}
	
	protected int skillTypeId = 1;

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

	protected String soundEffectName = "minebike:powerup";
	
	public Skill(PedalingCombo pedalingCombo, int effectiveTimeMax, int coolTimeMax, int requiredMana, String soundEffectName)
	{
		this.pedalingCombo = pedalingCombo;
		this.effectiveTimeMax = effectiveTimeMax;
		this.coolTimeMax = coolTimeMax;
		this.requiredMana = requiredMana;
		this.soundEffectName = soundEffectName;
	}
	
	public boolean hasEnoughMana()
	{	
		return this.pedalingCombo.hasEnoughGauge(requiredMana);
	}
	
	public void playSkillOnSoundEffect()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.playSound(soundEffectName, 1.0f, 1.0f);
	}
	
	public boolean use()
	{
		if(!this.hasEnoughMana())
			return false;
		
		if(!switchState(enumSkillState.EFFECTIVE))
			return false;
		
		this.playSkillOnSoundEffect();
		
		// SEND GAME TAG - Skill 0x(GAME TAG[0xF])(SKILL ID[0xF])(Gauge Percentage[0xFF])
		try {
			int gaugePercentage = (100 * this.pedalingCombo.getGauge()) / this.pedalingCombo.gaugeMaxLimit;
			int skillTypeEnum = ((0xf & skillTypeId) << 8) | (0xff & gaugePercentage);
			BiGXGameTag biGXGameTag = new BiGXGameTag();
			biGXGameTag.setTagName("" + (Specification.GameTagType.GAMETAG_ID_SKILL_BEGINNING | skillTypeEnum));
			
			BigxClientContext.sendGameTag(biGXGameTag);
			
			BigxContext.logWriter(LOGTYPE.TAG, "" + Specification.GameTagType.GAMETAG_ID_SKILL_BEGINNING + "\t" + skillTypeEnum + "\t" + gaugePercentage);
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
