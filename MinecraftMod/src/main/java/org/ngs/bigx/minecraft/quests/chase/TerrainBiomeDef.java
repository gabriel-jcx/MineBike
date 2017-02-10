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
			randomNumber = rand.nextInt(size);
		
		if(randomNumber == -1)
			return null;
		else
			return this.areas.get(randomNumber);
	}
}
