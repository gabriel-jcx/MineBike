package org.ngs.bigx.minecraft.entity.lotom;

import org.ngs.bigx.minecraft.BiGXConnectionStateManagerClass.connectionStateEnum;

public class BackpackProperty {
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
	
	private boolean isActivated = false;	// Initially the player has only on backpack
	private backpackType backpackSize = backpackType.small;
	private Object[] items;
	private int weight;
	private int volumeMax;
	
	public void init()
	{
		this.isActivated = false;
		this.items = new Object[256];
		this.initToSize(backpackType.small);
	}
	
	public void initToSize(backpackType type)
	{
		int typeInInt = type.getValue();
		
		this.backpackSize = type;
		this.volumeMax = backpackMaxVolumeByType[typeInInt];
		this.weight = backpackWeightByType[typeInInt];
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void activateBackpack() {
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

	public Object[] getItems() {
		return items;
	}

	public void setItems(Object[] items) {
		this.items = items;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getVolumeMax() {
		return volumeMax;
	}

	public void setVolumeMax(int volumeMax) {
		this.volumeMax = volumeMax;
	}
	
	
}
