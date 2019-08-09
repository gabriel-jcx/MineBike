package org.ngs.bigx.minecraft.npcs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDark;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import noppes.npcs.constants.EnumAnimation;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class NpcCommand {

	private EntityCustomNpc npc;
	private int role; //0=no role, 1=transporter

	private static boolean npcSpawnFlag = false;
	private static ArrayList<Integer> npcSpawnDimensionId = new ArrayList<Integer>();

	private static boolean theifOnFightAndChaseQuestSpawnFlag = false;
	private static boolean theifOnRegularChaseQuestSpawnFlag = false;
	private static boolean theifOnFireChaseQuestSpawnFlag = false;

	private static BigxContext bigxContext;
	
	private double runStartX, runStartZ;
	
	private static Object NPCCOMMANDLOCK = new Object();
	public static boolean hasFallen = false;
	public static boolean isSiting = false;
	
	public void setRunStartX(int posX)
	{
		runStartX = posX;
	}
	
	public NpcCommand(BigxContext bigxContext, EntityCustomNpc npc) {
		this.npc = npc;
		this.role = 0;
		this.bigxContext = bigxContext;
	}

	public void setNPC(EntityCustomNpc npc) {
		this.npc = npc;
		this.role = 0;
	}
	
	public static void putItemsInNpcInventory(EntityCustomNpc npc, ItemStack... items) {
		if (items == null)
			return;
	
		// Check DataInventory.setInventorySlotContents() for index information
		int itemIterator = 0;
		for (int i = npc.inventory.getSizeInventory()-1; i >= 4; --i) {
			if (npc.inventory.getStackInSlot(i) != null) {
				npc.inventory.setInventorySlotContents(i, items[itemIterator]);
				if (++itemIterator == items.length) {
					return;
				}
			}
		}
	}
	
	public static EntityCustomNpc spawnNpc(int x, int y, int z, WorldServer w, String name, String texture) {
		EntityCustomNpc npc = new EntityCustomNpc(w);
		npc.display.name = name;
		npc.setPosition(x, y, z);
		npc.display.texture = texture;
	
		npc.ai.startPos = new int[]{
	    		MathHelper.floor_double(x),
	    		MathHelper.floor_double(y),
	    		MathHelper.floor_double(z)};

		npc.attackEntityAsMob(Minecraft.getMinecraft().thePlayer);
		
		w.spawnEntityInWorld(npc);
	    npc.setHealth(10000f);
	
	    return npc;
	}
	
	public static EntityCustomNpc spawnNpc(int x, int y, int z, WorldServer w, String name) {
		EntityCustomNpc npc = new EntityCustomNpc(w);
		npc.display.name = name;
		npc.setPosition(x, y, z);
	
		npc.ai.startPos = new int[]{
	    		MathHelper.floor_double(x),
	    		MathHelper.floor_double(y),
	    		MathHelper.floor_double(z)};
	
		w.spawnEntityInWorld(npc);
	    npc.setHealth(10000f);
	
	    return npc;
	}
	
	public static void triggerSpawnTheifOnChaseQuest() {
		theifOnRegularChaseQuestSpawnFlag = true;
	}
	
	public static void triggerSpawnTheifOnFireChaseQuest() {
		theifOnFireChaseQuestSpawnFlag = true;
	}
	
	public static void spawnTheifOnRegularChaseQuest(BigxContext context) {	
		if(theifOnRegularChaseQuestSpawnFlag) {
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderDark.dimID);
			QuestTaskChasing questTaskChasing = (QuestTaskChasing)bigxContext.getQuestManager().getActiveQuestTask();
			EntityCustomNpc npc;
			NpcCommand command;
	
			if(questTaskChasing == null)
				return;
	
			theifOnRegularChaseQuestSpawnFlag = false;
	
			npc = NpcCommand.spawnNpc(0, 11, 20, ws, "Thief");
			npc.ai.stopAndInteract = false;
//			npc.faction.isAggressiveToPlayer(true)
	
			questTaskChasing.setNpc(npc);
	
			command = new NpcCommand(context, npc);
			command.setSpeed(10);
			command.enableMoving(false);
			command.runInDirection(ForgeDirection.SOUTH);
	
			questTaskChasing.setNpcCommand(command);
		}
	}
	
	public static void setNpcSpawnFlag() {
		npcSpawnFlag = true;
	}
	
	public static void addNpcSpawnDimensionId(int dimensionId) {
		npcSpawnDimensionId.clear();
		npcSpawnDimensionId.add(dimensionId);
	}
	
	public static void spawnNpcInDB() {
	//		if(!npcSpawnFlag)
	//			return;
	
		for(int dimensionId : npcSpawnDimensionId) {
			npcSpawnFlag = false;
	
			WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimensionId);
	
			// NPC CHECKING
			List listOfNpc = NpcCommand.getCustomNpcsInDimension(dimensionId);
			for (String name : NpcDatabase.NpcNames(dimensionId)) {
				int found = 0;
				for (Object obj : listOfNpc)
					if (((EntityCustomNpc)obj).display.name.equals(name))
						++found;
	
				if (found == 0) {
					System.out.println("No '" + name + "' found in this dimension...");
					NpcDatabase.spawn(worldServer, name, dimensionId);
				} else if (found > 1) {
					List<EntityCustomNpc> list = new ArrayList<EntityCustomNpc>();
					for (Object obj : listOfNpc)
						if (((EntityCustomNpc)obj).display.name.equals(name))
							list.add((EntityCustomNpc)obj);
					NpcDatabase.sortFurthestSpawn(list);
					for (int i = 0; i < list.size()-1; ++i)
						list.get(i).delete();
				}
			}
		}
	}
	
	public List getPath() {
		return npc.ai.getMovingPath();
	}
	
	public void setPath(List l) {
		npc.ai.setMovingPath(l);
	}
	
	public void addPathPoint(int[] coords) {
		// TODO Finish writing!
		/*
		// We have to wait until the npc is on the ground
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				PathEntity pathEntity = npc.getNavigator().getPathToXYZ(coords[0], coords[1], coords[2]);
				if (pathEntity != null) {
					List coordList = new ArrayList();
					int coordIndex = 0;
					for (int i = 0; i < 100; i++) {
						if (pathEntity.getPathPointFromIndex(i).equals(pathEntity.getFinalPathPoint())) {
							break;
						}
					}
					timer.cancel();
				}
			}
		}, 0, 1000);
		
		//while (!pathEntity.getPathPointFromIndex(coordIndex).equals(pathEntity.getFinalPathPoint())) {
		//	coordList.add(pathEntity.getPathPointFromIndex(coordIndex));
		//}
		//coordList.add(pathEntity.getFinalPathPoint());
		//for(Object point : coordList) {
		//	System.out.println(((PathPoint)point).toString());
		//}
		List path = npc.ai.getMovingPath();
		int[] lastPos = (int[])path.get(path.size()-1);
		float dx = coords[0] - lastPos[0];
		float dy = coords[1] - lastPos[1];
		float dz = coords[2] - lastPos[2];
		double dist = (double)MathHelper.sqrt_double(dx*dx + dy*dy + dz*dz);
		//
		//double yaw = Math.atan2(dz, dx);
		//double pitch = Math.atan2(Math.sqrt(dz*dz + dx*dx), dy) + Math.PI;
		//
		for (float i = 0; i < dist; i += CustomNpcs.NpcNavRange) {
			path.add(new int[] {
					coords[0] + (int)(dx * i / dist),
					coords[1] + (int)(dy * i / dist),
					coords[2] + (int)(dz * i / dist)});
		}
		path.add(coords);
		npc.ai.setMovingPath(path);
		*/
	}
	
	public void correctRunningDirection(final ForgeDirection direction)
	{
		synchronized (NPCCOMMANDLOCK)
		{
			int yy = (int)npc.posY;
			npc.ai.startPos = new int[]{(int)npc.posX, (int)npc.posY, (int)npc.posZ};
			npc.ai.setMovingPath(new ArrayList());
			switch(direction) {
			case NORTH:
				npc.ai.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ - 20});
				npc.ai.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ - 30});
				break;
			case SOUTH:
				npc.ai.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ + 20});
				npc.ai.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ + 30});
				break;
			case EAST:
				npc.ai.getMovingPath().add(new int[]{(int) npc.posX + 20, yy, (int)runStartZ});
				npc.ai.getMovingPath().add(new int[]{(int) npc.posX + 30, yy, (int)runStartZ});
				break;
			case WEST:
				npc.ai.getMovingPath().add(new int[]{(int) npc.posX - 20, yy, (int)runStartZ});
				npc.ai.getMovingPath().add(new int[]{(int) npc.posX - 30, yy, (int)runStartZ});
				break;
			default:
				System.out.println("[BiGX] Direction to NO WHERE!");
				break;
			}
			
			npc.ai.getMovingPath().remove(0);
			
			if( (npc.motionZ == 0) && (!npc.isDead))
			{
				npc.reset();
				npc.setHealth(10000f);
				npc.setFaction(0);
				setSpeed(10);
				enableMoving(true);
//					System.out.println("[BiGX] AI : [" + runStartX + "][" + npc.getSpeed() + "]");
//					System.out.println("[BiGX] CQ Var: [" + npc.ai.movingType + "] [" + npc.ai.movingPause + "] [" + npc.getSpeed() + "]");
			}
		}
	}
	
	public void runInDirection(final ForgeDirection direction) {
		final Timer timer = new Timer();

//		runStartX = npc.posX;
		runStartX = 0;
		runStartZ = npc.posZ;
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (npc.hasDied) {
					timer.cancel();
				}
				
				if(NpcCommand.hasFallen)
				{
					npc.ai.movingType = EnumMovingType.Standing;
					npc.ai.movingPause = true;
					
					if(NpcCommand.isSiting)
						npc.ai.animationType = EnumAnimation.SITTING;
					else
						npc.ai.animationType = EnumAnimation.CRAWLING;
				}
				else
				{
					npc.ai.movingType = EnumMovingType.MovingPath;
					npc.ai.animationType = EnumAnimation.NONE;
					correctRunningDirection(direction);
				}
			}
		}, 0, 400);
	}
	
	public void addPathPoint(int x, int y, int z) {
		addPathPoint(new int[]{x, y, z});
	}
	
	public static List getAllCustomNpcs() {
		List list = new ArrayList();
		WorldServer[] ws = MinecraftServer.getServer().worldServers;
		for (int i = 0; i < ws.length; i++) {
			Iterator iter = ws[i].loadedEntityList.iterator();
			while (iter.hasNext()) {
				Entity entity = (Entity)iter.next();
		         if(entity instanceof EntityNPCInterface) {
		            list.add((EntityNPCInterface)entity);
		         }
			}
		}
		return list;
	}
	
	public static List getCustomNpcsInDimension(int dimID) {
		List list = new ArrayList();
		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(dimID);
		
		synchronized (ws.loadedEntityList) {
			Iterator iter = ws.loadedEntityList.iterator();
			while (iter.hasNext()) {
				Entity entity = (Entity)iter.next();
		         if(entity instanceof EntityNPCInterface) {
		            list.add((EntityNPCInterface)entity);
		         }
			}
		}
		
		return list;
	}
	
	public static void removeNpc(String name, int dimension) {
		for (Object obj : getCustomNpcsInDimension(dimension)) {
			if (obj instanceof EntityNPCInterface) {
				if (((EntityNPCInterface)obj).display.name == name) {
					((EntityNPCInterface)obj).spawnExplosionParticle();
					((EntityNPCInterface)obj).delete();
				}
			}
		}
	}
	
	public void setSpeed(int speed) {
		npc.ai.setWalkingSpeed(speed);
	}
	
	public int getSpeed() {
		return npc.ai.getWalkingSpeed();
	}
	
	public void enableMoving(boolean enable) {
		if (enable) {
			npc.ai.movingType = EnumMovingType.MovingPath;
			npc.ai.canSprint = true;
			npc.ai.movingPause = false;
		} else {
			npc.ai.movingType = EnumMovingType.Standing;
			npc.ai.canSprint = false;
			npc.ai.movingPause = true;
		}
		npc.updateTasks();
		npc.setHealth(10000f);
	}
	
	public void makeTransporter(boolean enable) {
		if (enable) {
			role = 1;
		}
	}
	
	public boolean isInteracting() {
		return npc.isInteracting();
	}
}