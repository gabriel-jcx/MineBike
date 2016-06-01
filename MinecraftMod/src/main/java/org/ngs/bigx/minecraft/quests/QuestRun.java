package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class QuestRun extends Quest {
	
	int distance = 200;

	public QuestRun(int ID) throws Exception {
		super(ID);
	}
	
	@Override
	protected void setRemainingToEndVar()
	{
		this.timeLimit = 30;
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
		return "Run: "+(distance-(int) player.posX)+"m";
	}

	@Override
	public String getName() {
		return getTypeName();
	}

	@Override
	public Boolean checkComplete(String playerName) {
		EntityPlayerMP player = getPlayerEntity(playerName);
		if (player.posX>distance) {
			return true;
		}
		return false;
	}

	@Override
	public void generateWorld(World world,int posX, int posY, int posZ) {
		int bottomZ = 1466;
        int topZ = 1628;
        int leftX = 1624;
        int rightX = 1421;
        int width = topZ-bottomZ;
        int length = leftX-rightX;
        int groundLevel = 65;
        System.out.println("INSIDE GENERATE WORLD");
        //left wall
        for (int x = 0; x <= width; x++){
                for (int i = 0; i <= 35; i++)
                        this.originalWorld.setBlock(leftX, groundLevel+i, bottomZ+x, Blocks.bedrock);
        }
        //right wall
        for (int x = 0; x <= width; x++){
                for (int i = 0; i <= 35; i++)
                	this.originalWorld.setBlock(rightX, groundLevel+i, bottomZ+x, Blocks.bedrock);
        }
        //bottom wall
        for (int x = 0; x <= length; x++){
                for (int i = 0; i <= 35; i++)
                	this.originalWorld.setBlock(rightX+x, groundLevel+i, bottomZ, Blocks.bedrock);
        }
        //top wall
        for (int x = 0; x <= length; x++){
                for (int i = 0; i <= 35; i++)
                	this.originalWorld.setBlock(rightX+x, groundLevel+i, topZ, Blocks.bedrock);
        }

		
		
		for (int i=-5;i<distance;i++) {
			for(int j=-2;j<=+2;j++) {
				world.setBlock(posX+i,posY,posZ+j,Blocks.grass);
			}
		}
		for(int j=-2;j<=+2;j++) {
			world.setBlock(posX+2,posY+1,posZ+j,Blocks.fence);
		}
		super.generateWorld(world, posX, posY, posZ);
	}
	
	@Override
	public void startQuest(World world,int posX, int posY, int posZ) {
		for(int j=-2;j<=+2;j++) {
			world.setBlock(posX+2,posY+1,posZ+j,Blocks.air);
		}
//		QuestTileEntity QuestMessage = new QuestTileEntity();
//		QuestMessage.setLines("String1", "String2", "String3", "String4", "String5", "String6", "String7", "String8");
//		world.setTileEntity(posX, posY, posZ, QuestMessage);
	}

	@Override
	public void questTick() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onQuestInProgress() {
		this.secondsRemainingToEnd = timeLimit;
		questTimer.schedule(questAccomplishTimerTask, timeLimit * 1000);
		if (isServerSide())
		startQuest(questWorld,questWorldX,questWorldY,questWorldZ);
	}

	@Override
	public void addQuestInitiator(int locationX, int height, int locationY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeQuestInitiator(int locationX, int height, int locationY) {
		// TODO Auto-generated method stub
		
	}
}
