package org.ngs.bigx.minecraft.entity.lotom;

import java.util.ArrayList;

public class BackpackProperty extends ItemLotom {
	public static enum backpackType {
		small(0), medium(1), large(2);
		
		private final int value;
		private backpackType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static backpackType fromInt(int i) {
			for (backpackType b : backpackType.values()) {
	            if (b.getValue() == i) { return b; }
	        }
	        return null;
		}
	};
	public static final int[] backpackMaxVolumeByType = {16, 64, 256};
	public static final int[] backpackWeightByType = {1, 3, 9};
	public static enum backpackReturnCode {
		success, bagIsFull, 
	};
	
	private boolean isActivated = false;	// Initially the player has only on backpack
	private backpackType backpackSize = backpackType.small;
	private ArrayList items;
	private int totalWeight;

	private int volumeMax;
	
	public BackpackProperty()
	{
		this.init();
	}
	
	public void init()
	{
		this.isActivated = false;
		this.items = new ArrayList();
		this.initToSize(backpackType.small);
	}
	
	public void initToSize(backpackType type)
	{
		int typeInInt = type.getValue();
		
		this.backpackSize = type;
		this.volumeMax = backpackMaxVolumeByType[typeInInt];
		this.weight = backpackWeightByType[typeInInt];
		this.totalWeight = this.weight;
		this.items.removeAll(null);
	}
	
	public int getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public backpackReturnCode insertAnItem(ItemLotom item)
	{
		if( (item.getWeight() + this.getTotalWeight()) > this.volumeMax )
		{
			return backpackReturnCode.bagIsFull;
		}
		
		this.items.add(item);
		this.setTotalWeight(this.getTotalWeight() + item.getWeight());
		
		return backpackReturnCode.success;
	}
	
	public backpackReturnCode removeAnItem(int index)
	{
		ItemLotom item = (ItemLotom) this.items.remove(index);
		
		this.setTotalWeight(this.getTotalWeight() - item.getWeight());
		
		return backpackReturnCode.success;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void activateBackpack() {
		this.init();
		this.isActivated = true;
	}

	public void deactivateBackpack() {
		this.isActivated = false;
	}

	public backpackType getBackpackSize() {
		return backpackSize;
	}

	public void setBackpackSize(backpackType backpackSize) {
		this.backpackSize = backpackSize;
	}

	public ItemLotom[] getItems() {
		return (ItemLotom[]) items.toArray();
	}

	public void setItems(ItemLotom[] items) {
		this.items.removeAll(null);
		
		for(ItemLotom item : items)
			this.items.add(items);
	}

	public int getVolumeMax() {
		return volumeMax;
	}

	public void setVolumeMax(int volumeMax) {
		this.volumeMax = volumeMax;
	}
	
	
}
