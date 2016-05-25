package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class QuestTank extends Quest {
	private int coinPoint = 0;

	public QuestTank(int ID) throws Exception {
		super(ID);
	}

	@Override
	protected void setRemainingToEndVar() {
		// Set the time limit to three mins
		this.timeLimit = 180;
	}

	@Override
	public String getType() {
		return "tank";
	}

	@Override
	public void setProperties(Map<String, String> arguments) {
		coinPoint = Integer.valueOf(arguments.get("coinPoint"));
	}

	@Override
	public Map<String, String> getProperties() {
		Map<String,String> arguments = new HashMap();
		arguments.put("coinPoint",String.valueOf(coinPoint));
		return arguments;
	}

	@Override
	public String getHint(EntityPlayer player) {
		return "Point: " + this.coinPoint;
	}

	@Override
	public String getName() {
		return this.getTypeName();
	}

	@Override
	public Boolean checkComplete(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void questTick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuest(World world, int posX, int posY, int posZ) {
		// TODO Auto-generated method stub
		
	}

}
