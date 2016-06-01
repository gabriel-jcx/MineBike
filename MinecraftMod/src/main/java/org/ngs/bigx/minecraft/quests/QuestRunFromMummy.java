package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.Map;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.entity.block.QuestRFMChest;
import org.ngs.bigx.minecraft.quests.maze.Maze;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class QuestRunFromMummy extends Quest {

	public static int itemsCollected = 0;
	public static int countDeadend = 0;
	private Maze maze;
	private int sizeMaze = 10;

	public QuestRunFromMummy(int ID) throws Exception {
		super(ID);
	}
	
	@Override
	protected void setRemainingToEndVar()
	{
		this.timeLimit = 420;
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

		if(this.originalWorld == null)
			return;
		
		if(isServerSide())
		{
			generateMaze();
			this.countDeadend = this.maze.getCountDeadend();
		}
	}
	
	@Override
	public void onQuestWaitToStart()
	{
		super.onQuestWaitToStart();

		{
			int i,j=0;
			
			/// CHECK THE TREASURE
			for(i=0; i<30; i++)
			{
				for(j=0; j<30; j++)
				{
					if(Minecraft.getMinecraft().theWorld.getBlock(1524 + i, 65, 411 + j).getClass() == Main.BlockQuestFRMCheck.getClass())
					{
						this.countDeadend++;
					}
				}
			}
		}
	}
	
	public void onQuestAccomplished()
	{
		super.onQuestAccomplished();
		
		if(this.originalWorld == null)
			return;

		int i,j=0;
		
		for(i=0; i<this.sizeMaze*3-1; i++)
		{
			for(j=0; j<this.sizeMaze*3-1; j++)
			{
				createHeightOfWall(1525 + i, 65, 412 + j, Blocks.lava);
				createHeightOfWall(1525 + i, 65, 412 + j, Blocks.air);
			}
		}
		
		this.countDeadend = 0;
	}
	
	public void generateMaze()
	{
		if(this.originalWorld == null)
			return;
		
		this.maze = new Maze(10);
		this.createMapOnWorld(1524, 65, 411);
	}
	
	public void createHeightOfWall(int locationX, int floorheight, int locationY, Block wall)
	{	
		int i=0;
		for(i=0; i<5; i++)
			this.originalWorld.setBlock(locationX, floorheight+i, locationY, wall);
	}

	public void createMapOnWorld(int locationX, int height, int locationY)
	{
		int i,j=0;
		Block wall = Blocks.sandstone;
		
		/// Clean up the previous shape
		for(i=0; i<this.sizeMaze*3; i++)
		{
			for(j=0; j<this.sizeMaze*3; j++)
			{
				createHeightOfWall(locationX + i, height, locationY + j, Blocks.lava);
				createHeightOfWall(locationX + i, height, locationY + j, Blocks.air);
			}
		}
		
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
				
				this.originalWorld.setBlock(locationX + 3*i -1 , height+5, locationY + 3*j -1, Blocks.glowstone);
				
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
				this.originalWorld.setBlock(locationX+i, height+5, locationY+j, wall);
				
				if( ((j%3)==1) && ((i%3)==1) )
				{
					/// LIGHT
					this.originalWorld.setBlock(locationX+i, height+5, locationY+j, Blocks.glowstone);
				}
				
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
					/// PLACE MONSTERS
//					this.originalWorld.setBlock(locationX + 3*i - 2, height, locationY + 3*j - 2, Main.BlockQuestFRMCheck);
					World worldd = this.originalWorld;
					
					if(isServerSide()){
						/// TREASRUE
						this.originalWorld.setBlock(locationX + 3*i - 2, height, locationY + 3*j - 2, Main.BlockQuestFRMCheck);
						this.countDeadend++;
						
						/// MONSTERS
						EntitySlime monster = new EntitySlime(worldd);
						monster.setPosition(locationX + 3*i - 2, height+1, locationY + 3*j - 2);
						worldd.spawnEntityInWorld(monster);
					}
				}
			}
		}
		
		/// MAKE A DOOR
		for(i=0; i<2; i++)
		{
			for(j=0; j<12; j++)
			{
				createHeightOfWall(locationX + 2 - j, height, locationY + 1 - i, Blocks.air);
			}
		}
		
		/// WIND BLOWING REASON SETUP
		
		/// MARK THE VISITED AREA
	}
}
