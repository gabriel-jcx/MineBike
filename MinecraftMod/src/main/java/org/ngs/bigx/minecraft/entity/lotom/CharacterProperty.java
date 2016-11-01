package org.ngs.bigx.minecraft.entity.lotom;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.ngs.bigx.minecraft.entity.lotom.BackpackProperty.backpackReturnCode;
import org.ngs.bigx.minecraft.entity.lotom.stat.EntityStat;
import org.ngs.bigx.minecraft.entity.lotom.stat.ISyncedStat;

public class CharacterProperty extends EntityStat implements ISyncedStat {
	public static enum characterReturnCode {
		success, failure, bagIsFull, bagIsNotActive
	};
	//add something for going too fast
	private int weight; 					// The unit is Kg
	private int totalWeight;
	private float speedRate;				// Current speed of the player
	private float speedDecreaseRate;		// The rate at which the speed of the player is decreased
	private int strength;					// Abstracted strength: Pedaling Resistance down
	private int skill;						// Abstracted skill: Skill to craft items
	private int luck;						// Abstracted luck: Luck to mine minerals
	private BackpackProperty[] backpacks = new BackpackProperty[2];	// Two backpacks per character at most (OPTIONAL)
	private int exp;
	private int coins;
	

	public CharacterProperty(String name) {
		super(name);
		this.backpacks[0] = new BackpackProperty();
		this.backpacks[1] = new BackpackProperty();
		this.initProperty();
	}
	
	public void initProperty()
	{
		this.weight = 60;
		this.speedRate = 0.0f;
		this.speedDecreaseRate = 0.05f;
		this.strength = 10;
		this.skill = 10;
		this.luck = 10;
		this.backpacks[0].activateBackpack();
		this.backpacks[1].deactivateBackpack();
		this.totalWeight = this.weight;
		this.exp = 0;
		this.coins = 0;
	}

	
	//Handling Weight and Backpacks
	public int getMaxWeightCharCanHandle()
	{
		return this.strength * 6;
	}
	
	public int getRemaintWeightLeftToMax()
	{
		return this.getMaxWeightCharCanHandle() - this.totalWeight;
	}
	
	public characterReturnCode insertAnItemToBackpack(int backpackIndex, ItemLotom item)
	{
		if(!this.backpacks[backpackIndex].isActivated())
			return characterReturnCode.bagIsNotActive;
		
		backpackReturnCode backpackrc = this.backpacks[backpackIndex].insertAnItem(item);
		
		switch(backpackrc)
		{
		case success:
			return characterReturnCode.success;
		case bagIsFull:
			return characterReturnCode.bagIsFull;
		};

		return characterReturnCode.failure; 
	}
	
	
	//Handling Speed
	public float getSpeedRate()
	{
		return this.speedRate;
	}
	
	public void setSpeedRate(float newSpeedRate)
	{
		this.speedRate = newSpeedRate;
	}
	
	public void decreaseSpeedByTime()
	{
		if (this.speedRate > 0){
			this.speedRate -= this.speedDecreaseRate;
		}
		if (this.speedRate < 0){
			this.speedRate = 0;
		}
	}
	
	public void increaseEXPby(int awardedEXP)
	{
		this.exp += awardedEXP;
	}
	
	public int getCoinAmount()
	{
		return this.coins;
	}
	
	public void addCoins(int addedCoins)
	{
		this.coins += addedCoins;
	}
	
	public void subCoins(int subtractedCoins)
	{
		this.coins -= subtractedCoins;
	}
	
	//Handling Resistance
	public double getResistanceSettings()
	{
		return this.totalWeight - this.strength*6;
	}

	
	//Other important methods
    public String getValue() {
    	// @TODO Need to find a way to steam the class
    	String rVal = "" + weight + "\t"
    			+ totalWeight + "\t"
    			+ speedRate + "\t"
    			+ speedDecreaseRate + "\t"
    			+ strength + "\t"
    			+ skill + "\t"
    			+ luck + "\t"
    			+ exp + "\t"
    			+ coins + "\t"
    			;
    	
    	System.out.println("[BiGX] GET VALUE CHAR PROPERTY");
    	
        return "";
    }
    
    public void setValue(String value)
    {
    	// @TODO Need to find a way to unstream a string to this class
    	String[] arrayOfValue = value.split("\t");
    	
    	if(arrayOfValue.length < 9)
    	{
    		System.out.println("[BiGX] Character Property is Empty");
    		return;
    	}
    	this.weight = Integer.parseInt(arrayOfValue[0]);
    	this.totalWeight = Integer.parseInt(arrayOfValue[1]);
    	this.speedRate = Float.parseFloat(arrayOfValue[2]);
    	this.speedDecreaseRate = Float.parseFloat(arrayOfValue[3]);
    	this.strength = Integer.parseInt(arrayOfValue[4]);
    	this.skill = Integer.parseInt(arrayOfValue[5]);
    	this.luck = Integer.parseInt(arrayOfValue[6]);
    	this.exp = Integer.parseInt(arrayOfValue[7]);
    	this.coins = Integer.parseInt(arrayOfValue[8]);
    }

	@Override
    public boolean isDataSynced(NBTTagCompound compound) {
        return this.getValue() == compound.getString("Value");
    }

    @Override
    public EntityStat init(Entity entity, World world) {
        return new CharacterProperty(this.getName());
    }

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.setValue(compound.getString("Value"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
        compound.setString("Value", this.getValue());
	}
}
