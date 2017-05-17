package org.ngs.bigx.minecraft.quests.chase;

import java.util.ArrayList;
import java.util.Random;

public class TerrainBiomeDef {
	public ArrayList<TerrainBiomeArea> areas = new ArrayList<TerrainBiomeArea>();
	
	public TerrainBiomeArea getRandomArea()
	{
		Random rand = new Random();

		int size = this.areas.size();
		int randomNumber = -1;
		
		if(size != 0)
		{
			randomNumber = rand.nextInt((int)(size*1.5));
		}
		
		if(randomNumber == -1)
			return null;
		else if(randomNumber >= size)
			return new TerrainBiomeArea();
		else
			return this.areas.get(randomNumber);
	}
}
