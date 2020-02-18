package org.ngs.bigx.minecraft.quests;

import net.minecraft.block.BlockSign;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class QuestTileEntity extends TileEntity {
	
	public String[] signText = new String[8];

    public void setLines(String l0, String l1, String l2, String l3, String l4, String l5, String l6, String l7) {
	    signText[0] = l0;
	    signText[1] = l1;
	    signText[2] = l2;
	    signText[3] = l3;
	    signText[4] = l4;
	    signText[5] = l5;
	    signText[6] = l6;
	    signText[7] = l7;
	    world.markBlockRangeForRenderUpdate(getPos(),getPos());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for (int i = 0; i < 8; ++i) {
        	signText[i] = nbt.getString("L" + i);
        	if (signText[i].length() > 1)
        		signText[i] = signText[i].substring(0, 1);
        }
   
//        System.out.println("DEF " + signText[0] + " " + signText[1] + " " + signText[2] + " " + signText[3] + " " + signText[4] + " " + signText[5] + " " + signText[6] + " " + signText[7]);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    	super.writeToNBT(nbt);
        for (int i = 0; i < 8; ++i) 
                nbt.setString("L" + i, signText[i]);
   		return nbt;
//        System.out.println("ABC " + signText[0] + " " + signText[1] + " " + signText[2] + " " + signText[3] + " " + signText[4] + " " + signText[5] + " " + signText[6] + " " + signText[7]);
    }

//	protected QuestBlock(Class p_i45426_1_, boolean p_i45426_2_) {
//		super(p_i45426_1_, p_i45426_2_);
//		this.setHardness(50.0F);
//		this.setResistance(6000000.0F);
//		this.
//	}
	

}
