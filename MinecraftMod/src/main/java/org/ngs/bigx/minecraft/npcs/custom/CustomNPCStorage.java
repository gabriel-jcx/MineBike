package org.ngs.bigx.minecraft.npcs.custom;

import java.util.ArrayList;

public class CustomNPCStorage 
{
	public static ArrayList<CustomNPCAbstract> customNPCs = genCustomNPCs();
	
	public static ArrayList<CustomNPCAbstract> genCustomNPCs()
	{
		ArrayList<CustomNPCAbstract> returned = new ArrayList<CustomNPCAbstract>();
		returned.add(new Raul());
		
		return returned;
	}
}
