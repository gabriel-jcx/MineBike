package org.ngs.bigx.minecraft.npcs.custom;

import java.util.ArrayList;

public class CustomNPCStorage 
{
	public static ArrayList<CustomNPCAbstract> genCustomNPCs()
	{
		ArrayList<CustomNPCAbstract> customNpcs = new ArrayList<CustomNPCAbstract>();
		customNpcs.add(new Raul());
		customNpcs.add(new ChefGusteau());
		customNpcs.add(new Chum());
		customNpcs.add(new Flynn());
		customNpcs.add(new MinerNPC());
		return customNpcs;
	}
}
