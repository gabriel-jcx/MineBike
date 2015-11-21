package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class QuestRun extends Quest {
	
	int distance = 200;

	public QuestRun() throws Exception {
		super();
	}

	@Override
	public String getType() {
		return "run";
	}

	@Override
	public void setProperties(Map<String, String> arguments) {
		distance = Integer.valueOf(arguments.get("distance"));
	}

	@Override
	public Map<String, String> getProperties() {
		Map<String,String> arguments = new HashMap();
		arguments.put("distance",String.valueOf(distance));
		return arguments;
	}

	@Override
	public String getHint(EntityPlayer player) {
		return "Run: "+distance+"m";
	}

	@Override
	public String getName() {
		return getTypeName();
	}

	@Override
	public Boolean checkComplete(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateWorld(World world,int posX, int posY, int posZ) {
		for (int i=-5;i<distance;i++) {
			for(int j=-2;j<+2;j++) {
				world.setBlock(posX+i,posY,posZ+j,Blocks.grass);
			}
		}
		
		super.generateWorld(world, posX, posY, posZ);
	}

	@Override
	public void questTick() {
		// TODO Auto-generated method stub
		
	}
}
