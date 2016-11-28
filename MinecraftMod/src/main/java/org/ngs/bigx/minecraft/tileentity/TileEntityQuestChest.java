package org.ngs.bigx.minecraft.tileentity;

import java.util.HashMap;
import java.util.Map;

import org.ngs.bigx.minecraft.BiGX;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityQuestChest extends TileEntity implements IInventory {
	
	private static final int chestSize = 18;
	private static final int stackLimit = 64;
	
	private int facing;
	private int playerUsing;
	
	String userName;
	
	public Map<String, ItemStack[]> playerContents;
	public ItemStack[] chestContents;
	private String customName;
	
	public TileEntityQuestChest()
	{
		chestContents = new ItemStack[getSizeInventory()];
		playerContents = new HashMap<String, ItemStack[]>();
		
		playerContents.put("", chestContents);
	}
	
	public boolean activate(World world, int x, int y, int z, EntityPlayer player) {
		
		if (this.isUseableByPlayer(player)) {
			if (playerContents.get(player.getDisplayName()) == null) {
				playerContents.put(player.getDisplayName(), new ItemStack[getSizeInventory()]);
			}
			player.openGui(BiGX.instance(), BiGX.GUI_ENUM.QUEST_COMPLETE.ordinal(), world, x, y, z);
		}
		return true;
	}
	
	public String getCustomName()
	{
		return customName;
	}
	
	public void setCustomName(String newName)
	{
		customName = newName;
	}
	
	@Override
	public int getSizeInventory() {
		return chestSize;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getStackInSlot("", i);
		/*
		if (!indexInInventory(i))
		{
			return null;
		}
		return chestContents[i];
		*/
	}
	
	public ItemStack getStackInSlot(String p, int i) {
		// TODO figure out method of using p to access player-specific inventories
		// (currently defaults to blank string for default inventory)
		if (!indexInInventory(i))
		{
			return null;
		}
		return playerContents.get(p)[i];
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return decrStackSize("", index, count);
		/*
	    if (getStackInSlot(index) != null) {
	        ItemStack itemStack;

	        if (getStackInSlot(index).stackSize <= count) {
	            itemStack = getStackInSlot(index);
	            setInventorySlotContents(index, null);
	            markDirty();
	            return itemStack;
	        } else {
	            itemStack = getStackInSlot(index).splitStack(count);

	            if (getStackInSlot(index).stackSize <= 0) {
	                setInventorySlotContents(index, null);
	            } else {
	                //Just to show that changes happened
	                setInventorySlotContents(index, this.getStackInSlot(index));
	            }

	            markDirty();
	            return itemStack;
	        }
	    } else {
	        return null;
	    }
	    */
	}
	
	public ItemStack decrStackSize(String p, int i, int count) {
		if (getStackInSlot(p, i) != null) {
			ItemStack itemStack;
			
			if (getStackInSlot(p, i).stackSize <= count) {
				itemStack = getStackInSlot(p, i);
				setInventorySlotContents(p, i, null);
				markDirty();
				return itemStack;
			} else {
				itemStack = getStackInSlot(p, i).splitStack(count);
				
				if (getStackInSlot(p, i).stackSize <= 0) {
					setInventorySlotContents(p, i, null);
				} else {
					setInventorySlotContents(p, i, this.getStackInSlot(p, i));
				}
				
				markDirty();
				return itemStack;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return getStackInSlotOnClosing("", i);
		/*
		ItemStack itemStack = getStackInSlot(i);
		setInventorySlotContents(i, null);
		return itemStack;
		*/
	}
	
	public ItemStack getStackInSlotOnClosing(String p, int i) {
		ItemStack itemStack = getStackInSlot(p, i);
		setInventorySlotContents(p, i, null);
		return itemStack;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		setInventorySlotContents("", i, itemStack);
		/*
		if (indexInInventory(i))
		{
			return;
		}
		// Split item stacks when stack limits are reached
		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
		if (itemStack != null && itemStack.stackSize == 0)
		{
			itemStack = null;
		}
		chestContents[i] = itemStack;
		markDirty();
		*/
	}

	public void setInventorySlotContents(String p, int i, ItemStack itemStack) {
		if (indexInInventory(i))
		{
			return;
		}
		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
		if (itemStack != null && itemStack.stackSize == 0)
		{
			itemStack = null;
		}
		playerContents.get(p)[i] = itemStack;
		markDirty();
	}
	
	@Override
	public String getInventoryName() {
		if (hasCustomInventoryName())
		{
			return customName;
		}
		else
		{
			return "TileEntityQuestChest";
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		return customName != null && !customName.equals("");
	}

	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		if (worldObj == null)
		{
			return true;
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}
		return entityPlayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openInventory() {
		if (worldObj == null)
		{
			return;
		}
		playerUsing++;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, BiGX.blockQuestChest, 1, playerUsing);
	}

	@Override
	public void closeInventory() {
		if (worldObj == null)
		{
			return;
		}
		playerUsing--;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, BiGX.blockQuestChest, 1, playerUsing);
	}
	
	public void setFacing(int facing)
	{
		this.facing = facing;
	}
	
	public int getFacing()
	{
		return facing;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

	    NBTTagList list = new NBTTagList();
	    
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    nbt.setTag("Items", list);

	    if (this.hasCustomInventoryName()) {
	        nbt.setString("CustomName", this.getCustomName());
	    }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

	    NBTTagList list = nbt.getTagList("Items", 10);
	    
	    for (int i = 0; i < list.tagCount(); ++i) {
	        NBTTagCompound stackTag = list.getCompoundTagAt(i);
	        int slot = stackTag.getByte("Slot") & 255;
	        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
	    }

	    if (nbt.hasKey("CustomName", 8)) {
	        this.setCustomName(nbt.getString("CustomName"));
	    }
	}
	
	public boolean indexInInventory(int i) {
		return (i > 0 || i <= getSizeInventory());
	}
}
