package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Map;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.entity.block.QuestRFMChest;
import org.ngs.bigx.minecraft.quests.maze.Maze;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class QuestRunFromMummy extends Quest {

	private int itemsCollected = 0;
	private int countDeadend = 0;
	private Maze maze;
	private int sizeMaze = 10;

	public QuestRunFromMummy(int ID) throws Exception {
		super(ID);
	}
	
	@Override
	protected void setRemainingToEndVar()
	{
		this.timeLimit = 180;
	}

	@Override
	public String getType() {
		return "runFromMummy";
	}

	@Override
	public void setProperties(Map<String, String> arguments) {
		itemsCollected = Integer.valueOf(arguments.get("itemsCollected"));
		countDeadend = Integer.valueOf(arguments.get("deadendCount"));
	}

	@Override
	public Map<String, String> getProperties() {
		Map<String,String> arguments = new HashMap();
		arguments.put("itemsCollected",String.valueOf(itemsCollected));
		arguments.put("deadendCount",String.valueOf(countDeadend));
		return arguments;
	}

	@Override
	public String getHint(EntityPlayer player) {
		return "Progress: " + this.itemsCollected + "/" + this.countDeadend;
	}

	@Override
	public String getName() {
		return getTypeName();
	}

	@Override
	public Boolean checkComplete(String playerName) {
		EntityPlayerMP player = getPlayerEntity(playerName);
		if ((this.countDeadend != 0) && (this.itemsCollected == this.countDeadend)) {
			return true;
		}
		return false;
	}

	@Override
	public void generateWorld(World world,int posX, int posY, int posZ) {
		super.generateWorld(world, posX, posY, posZ);
	}
	
	@Override
	public void startQuest(World world,int posX, int posY, int posZ) {
		for(int j=-2;j<=+2;j++) {
			world.setBlock(posX+2,posY+1,posZ+j,Blocks.air);
		}
	}

	@Override
	public void questTick() {
	}
	
	@Override
	public void onQuestInProgress() {
		this.secondsRemainingToEnd = timeLimit;
		questTimer.schedule(questAccomplishTimerTask, timeLimit * 1000);
		if (isServerSide())
		startQuest(questWorld,questWorldX,questWorldY,questWorldZ);
	}
	
	
	@Override
	public void onQuestLoading() {
		super.onQuestLoading();
		generateMaze();
		this.countDeadend = this.maze.getCountDeadend();
	}
	
	public void generateMaze()
	{
		this.maze = new Maze(10);
		this.createMapOnWorld(1000, 1000, 65);
	}
	
	public void createHeightOfWall(int locationX, int floorheight, int locationY, Block wall)
	{
		int i=0;
		for(i=0; i<5; i++)
			this.originalWorld.setBlock(locationX, floorheight+i, locationY, wall);
	}

	public void createMapOnWorld(int locationX, int locationY, int height)
	{
		int i,j=0;
		Block wall = Blocks.sandstone.setBlockUnbreakable();
		
		/// WALL
		for(i=1; i<=this.sizeMaze; i++)
		{
			for(j=1; j<=this.sizeMaze; j++)
			{
				createHeightOfWall(locationX + 3*i - 2, height, locationY + 3*(j-1), wall);
				createHeightOfWall(locationX + 3*i - 1, height, locationY + 3*(j-1), wall);
				createHeightOfWall(locationX + 3*i - 2, height, locationY + 3*(j), wall);
				createHeightOfWall(locationX + 3*i - 1, height, locationY + 3*(j), wall);
				createHeightOfWall(locationX + 3*i, height, locationY + 3*(j) -2, wall);
				createHeightOfWall(locationX + 3*i, height, locationY + 3*(j) -1, wall);
				createHeightOfWall(locationX + 3*(i-1), height, locationY + 3*(j) -2, wall);
				createHeightOfWall(locationX + 3*(i-1), height, locationY + 3*(j) -1, wall);
				
				createHeightOfWall(locationX + 3*i, height, locationY + 3*j, wall);
				
				if(i==1 || j==1 || i==this.sizeMaze || j==this.sizeMaze)
				{
					createHeightOfWall(locationX + 3*(j-1), height, locationY + 3*(i-1), wall);
				}
			}
		}
		createHeightOfWall(locationX + 3*this.sizeMaze, height, locationY, wall);
		createHeightOfWall(locationX, height, locationY + 3*this.sizeMaze, wall);
		
		for(i=1; i<=this.sizeMaze; i++)
		{
			for(j=1; j<=this.sizeMaze; j++)
			{
				if(!this.maze.south[i][j])
				{
					createHeightOfWall(locationX + 3*i - 2, height, locationY + 3*(j-1), Blocks.air);
					createHeightOfWall(locationX + 3*i - 1, height, locationY + 3*(j-1), Blocks.air);
				}
				if(!this.maze.north[i][j])
				{
					createHeightOfWall(locationX + 3*i - 2, height, locationY + 3*j, Blocks.air);
					createHeightOfWall(locationX + 3*i - 1, height, locationY + 3*j, Blocks.air);
				}
				if(!this.maze.east[i][j])
				{
					createHeightOfWall(locationX + 3*i, height, locationY + 3*j - 2, Blocks.air);
					createHeightOfWall(locationX + 3*i, height, locationY + 3*j - 1, Blocks.air);
				}
				if(!this.maze.west[i][j])
				{
					createHeightOfWall(locationX + 3*(i-1), height, locationY + 3*j - 2, Blocks.air);
					createHeightOfWall(locationX + 3*(i-1), height, locationY + 3*j - 1, Blocks.air);
				}
			}
		}
		
		/// FLOOR and TOP
		for(i=0; i<this.sizeMaze*3; i++)
		{
			for(j=0; j<this.sizeMaze*3; j++)
			{
				/// COVER
				this.originalWorld.setBlock(locationX+i, height+5, locationY+j, Blocks.glass_pane);
				
				/// FLOOR
				this.originalWorld.setBlock(locationX+i, height-1, locationY+j, wall);
			}
		}
		
		boolean deadendspots[][] = this.maze.getDeadend();
		
		for(i=1; i<=this.sizeMaze; i++)
		{
			for(j=1; j<=this.sizeMaze; j++)
			{
				if(deadendspots[i][j])
				{
					/// PLACE THE TREASURE TO DEADENDS
					this.originalWorld.setBlock(locationX + 3*i - 2, height, locationY + 3*j - 2, Main.BlockQuestFRMCheck);
					
					/// PLACE MONSTERS
				}
			}
		}
		
		
		/// WIND BLOWING REASON SETUP
		
		/// MARK THE VISITED AREA
	}
}
