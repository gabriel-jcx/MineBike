package org.ngs.bigx.minecraft.entity.lotom;

public class CharacterProperty {
	private double oxygenLevel = 100;
	private double oxygenCosumptionRate = 0.1;	// Oxygen consumption rate per 50 milliseconds
	private double oxygenLevelMax = 100;		// Default amount of oxgygen a player can hold without any equipment
	private double weight = 60; 				// The unit is Kg
	private double speedRate = 1;
	private int strength = 10;					// Abstracted strength: Pedaling Resistance down
	private int skill = 10;						// Abstracted skill: Skill to craft items
	private int luck = 10;						// Abstracted luck: Luck to mine minerals
	private BackpackProperty[] backpacks = new BackpackProperty[2];	// Two backpacks
	
	public void initProperty()
	{
		this.oxygenLevel = 100;
		this.oxygenCosumptionRate = .1;
		this.oxygenLevelMax = 100;
		this.weight = 60;
		this.speedRate = 1.0;
		this.strength = 10;
		this.skill = 10;
		this.luck = 10;
		this.backpacks[0].
	}
}
