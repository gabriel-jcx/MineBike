package org.ngs.bigx.minecraft.block;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class QuestRFMChest extends BlockChest {

	public QuestRFMChest(int p_i45397_1_) {
		super(p_i45397_1_);
		// TODO Auto-generated constructor stub
	}

	public int idDropped(int i, Random random)
    {
        return 0;
    }
	
	//If the block's drop is a block.
    @Override
    public Item getItemDropped(int metadata, Random random, int fortune) {
        return Item.getItemFromBlock(Blocks.bed);
    }
}
