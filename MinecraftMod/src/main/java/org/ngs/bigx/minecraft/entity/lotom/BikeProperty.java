package org.ngs.bigx.minecraft.entity.lotom;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.ngs.bigx.minecraft.entity.lotom.BackpackProperty.backpackReturnCode;
import org.ngs.bigx.minecraft.entity.lotom.stat.EntityStat;
import org.ngs.bigx.minecraft.entity.lotom.stat.ISyncedStat;

public class BikeProperty extends EntityStat implements ISyncedStat {
	public static enum characterReturnCode {
		success, failure, bagIsFull, bagIsNotActive
	};
	
	private int weight = 60; 					// The unit is Kg
	private int resistence = 0;					// Base resistence for bike?
	private float defaultSpeedRate = 10;
	private float speedRate;

	public BikeProperty(String name) {
		super(name);
		this.initProperty();
	}
	
	public void initProperty()
	{
		this.weight = 60;
		this.resistence = 0;
		this.speedRate = defaultSpeedRate;
	}
	
	public float getSpeedRate()
	{
		return this.speedRate;
	}
	
	public void resetSpeedRate()
	{
		this.speedRate = defaultSpeedRate;
	}
	
	public void speedBoost() 	//Special power of the bike. Here until can find a better place.
	{
		this.speedRate = 20.0f;
	}
	

    public String getValue() {
    	// @TODO Need to find a way to steam the class
    	String rVal = "" + weight + "\t"
    			+ speedRate + "\t"
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
    	this.resistence = Integer.parseInt(arrayOfValue[1]);
    	this.speedRate = Float.parseFloat(arrayOfValue[2]);
    }

	@Override
    public boolean isDataSynced(NBTTagCompound compound) {
        return this.getValue() == compound.getString("Value");
    }

    @Override
    public EntityStat init(Entity entity, World world) {
        return new BikeProperty(this.getName());
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
