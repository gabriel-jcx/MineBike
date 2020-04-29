package org.ngs.bigx.minecraft.context;


import org.ngs.bigx.minecraft.BiGX;

import net.minecraft.world.WorldServer;
//import net.minecraft.world.biome.BiomeGenBase;

public class BigxServerContext extends BigxContext {
	private WorldServer WorldServer;
	
	public BigxServerContext(BiGX main)
	{
		super(main);
		serverSelf = this;
	}
	

	public static BigxContext getInstance()
	{
		return serverSelf;
	}
}
