package org.ngs.bigx.minecraft.npcs.custom;

import java.util.ArrayList;

public class CustomNPCStorage 
{
	public static ArrayList<CustomNPCAbstract> genCustomNPCs()
	{
		ArrayList<CustomNPCAbstract> customNpcs = new ArrayList<CustomNPCAbstract>();
		customNpcs.add(new Raul());
		customNpcs.add(new MinerNPC());
		return customNpcs;
	}
}
