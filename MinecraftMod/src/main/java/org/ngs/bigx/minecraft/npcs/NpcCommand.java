package org.ngs.bigx.minecraft.npcs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import akka.dispatch.CachingConfig;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDark;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumFacing;
//import org.ngs.bigx.minecraft.npcs.EnumAnimation;
//import noppes.npcs.constants.EnumAnimation;

//import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.api.constants.AnimationType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

// NOTE: npc.ais.setMovingType(int type);
// 0:standing 1:Wandering, 2: Movingpath

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
	
		// Check Dataisnventory.setInventorySlotContents() for index information
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
		npc.display.setName(name);
		//npc.display.name = name;
		npc.setPosition(x, y, z);
		npc.display.setSkinTexture(texture);
		//npc.display.texture = texture;
		BlockPos pos = new BlockPos(MathHelper.floor(x),MathHelper.floor(y),MathHelper.floor(z));
		npc.ais.setStartPos(pos);
		//npc.aiss.startPos = new int[]{
	    //		MathHelper.floor(x),
	    //		MathHelper.floor(y),
		//		MathHelper.floor(z)};
		//TODO: fix the next line?
		//npc.attackEntityAsMob(Minecraft.getMinecraft().player);
		
		w.spawnEntity(npc);
	    npc.setHealth(10000f);
	
	    return npc;
	}
	
	public static EntityCustomNpc spawnNpc(int x, int y, int z, WorldServer w, String name) {
		EntityCustomNpc npc = new EntityCustomNpc(w);
		npc.display.setName(name);
		npc.setPosition(x, y, z);
		BlockPos pos = new BlockPos(MathHelper.floor(x),MathHelper.floor(y),MathHelper.floor(z));
		npc.ais.setStartPos(pos);
//		npc.ais.startPos = new int[]{
//	    		MathHelper.floor(x),
//	    		MathHelper.floor(y),
//	    		MathHelper.floor(z)};
	
		w.spawnEntity(npc);
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
			WorldServer ws = DimensionManager.getWorld(WorldProviderDark.dimID);
			//WorldServer ws = MinecraftServer.getWorld(WorldProviderDark.dimID);
			//Todo:World provider Dark
			QuestTaskChasing questTaskChasing = (QuestTaskChasing)bigxContext.getQuestManager().getActiveQuestTask();
			EntityCustomNpc npc;
			NpcCommand command;
	
			if(questTaskChasing == null)
				return;
	
			theifOnRegularChaseQuestSpawnFlag = false;
	
			npc = NpcCommand.spawnNpc(0, 11, 20, ws, "Thief");

			npc.ais.stopAndInteract = false;
			//npc.ais.stopAndInteract = false;
//			npc.faction.isAggressiveToPlayer(true)
	
			questTaskChasing.setNpc(npc);
	
			command = new NpcCommand(context, npc);
			command.setSpeed(10);
			command.enableMoving(false);
			command.runInDirection(EnumFacing.SOUTH);
	
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
	
			WorldServer worldServer = net.minecraftforge.common.DimensionManager.getWorld(WorldProviderDark.dimID);
	
			// NPC CHECKING
			List listOfNpc = NpcCommand.getCustomNpcsInDimension(dimensionId);
			for (String name : NpcDatabase.NpcNames(dimensionId)) {
				int found = 0;
				for (Object obj : listOfNpc)
					if (((EntityCustomNpc)obj).display.getName().equals(name))
						++found;
	
				if (found == 0) {
					System.out.println("No '" + name + "' found in this dimension...");
					NpcDatabase.spawn(worldServer, name, dimensionId);
				} else if (found > 1) {
					List<EntityCustomNpc> list = new ArrayList<EntityCustomNpc>();
					for (Object obj : listOfNpc)
						if (((EntityCustomNpc)obj).display.getName().equals(name))
							list.add((EntityCustomNpc)obj);
					NpcDatabase.sortFurthestSpawn(list);
					for (int i = 0; i < list.size()-1; ++i)
						list.get(i).delete();
				}
			}
		}
	}
	
	public List getPath() {
		return npc.ais.getMovingPath();
	}
	
	public void setPath(List l) {
		npc.ais.setMovingPath(l);
	}
	
	public void addPathPoint(int[] coords) {
		// TODO Finish writing!
		/*
		// We have to waist until the npc is on the ground
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				Path pathEntity = npc.getNavigator().getPathToXYZ(coords[0], coords[1], coords[2]);
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
		List path = npc.ais.getMovingPath();
		int[] lastPos = (int[])path.get(path.size()-1);
		float dx = coords[0] - lastPos[0];
		float dy = coords[1] - lastPos[1];
		float dz = coords[2] - lastPos[2];
		double dist = (double)MathHelper.sqrt(dx*dx + dy*dy + dz*dz);
		//double dist = (double)math.sqrt()
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
		npc.ais.setMovingPath(path);*/

	}
	
	public void correctRunningDirection(final EnumFacing direction)
	{
		synchronized (NPCCOMMANDLOCK)
		{
			int yy = (int)npc.posY;
			BlockPos pos = new BlockPos((int)npc.posX,(int)npc.posY,(int)npc.posZ);
			npc.ais.setStartPos(pos);
			//npc.ais.startPos = new int[]{(int)npc.posX, (int)npc.posY, (int)npc.posZ};
			npc.ais.setMovingPath(new ArrayList());
			switch(direction) {
			case NORTH:
				npc.ais.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ - 20});
				npc.ais.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ - 30});
				break;
			case SOUTH:
				npc.ais.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ + 20});
				npc.ais.getMovingPath().add(new int[]{(int) runStartX, yy, (int)npc.posZ + 30});
				break;
			case EAST:
				npc.ais.getMovingPath().add(new int[]{(int) npc.posX + 20, yy, (int)runStartZ});
				npc.ais.getMovingPath().add(new int[]{(int) npc.posX + 30, yy, (int)runStartZ});
				break;
			case WEST:
				npc.ais.getMovingPath().add(new int[]{(int) npc.posX - 20, yy, (int)runStartZ});
				npc.ais.getMovingPath().add(new int[]{(int) npc.posX - 30, yy, (int)runStartZ});
				break;
			default:
				System.out.println("[BiGX] Direction to NO WHERE!");
				break;
			}
			
			npc.ais.getMovingPath().remove(0);
			
			if( (npc.motionZ == 0) && (!npc.isDead))
			{
				npc.reset();
				npc.setHealth(10000f);
				npc.setFaction(0);
				setSpeed(10);
				enableMoving(true);
//					System.out.println("[BiGX] ais : [" + runStartX + "][" + npc.getSpeed() + "]");
//					System.out.println("[BiGX] CQ Var: [" + npc.ais.movingType + "] [" + npc.ais.movingPause + "] [" + npc.getSpeed() + "]");
			}
		}
	}
	
	public void runInDirection(final EnumFacing direction) {
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
					//npc.ais.movementType
					//npc.ais.movementType = EnumMovingType.Standing;
					npc.ais.setMovingType(0);
					npc.ais.movingPause = true;

					if(NpcCommand.isSiting)
						npc.ais.animationType = AnimationType.SIT;//EnumAnimation.SITTING;
					else
						npc.ais.animationType = AnimationType.CRAWL;
				}
				else
				{
					// 0:standing 1:Wandering, 2: Movingpath
					npc.ais.setMovingType(2);
					//npc.ais.movementType = EnumMovingType.MovingPath;
					//npc.ais.mov
					npc.ais.animationType = AnimationType.NO;
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
		WorldServer[] ws = DimensionManager.getWorlds();
		//WorldServer[] ws = MinecraftServer.getServer().worlds;
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
		WorldServer ws = DimensionManager.getWorld(dimID);
		//WorldServer ws = MinecraftServer.getServer().worldServerForDimension(dimID);
		
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
				if (((EntityNPCInterface)obj).display.getName() == name) {
					System.out.println(((EntityNPCInterface)obj).display.getName());
					System.out.println(name);
					((EntityNPCInterface)obj).spawnExplosionParticle();
					((EntityNPCInterface)obj).delete();
				}
			}
		}
	}
	
	public void setSpeed(int speed) {
		npc.ais.setWalkingSpeed(speed);
	}
	
	public int getSpeed() {
		return npc.ais.getWalkingSpeed();
	}
	
	public void enableMoving(boolean enable) {
		if (enable) {
			npc.ais.setMovingType(0);
			//npc.ais.movingType = EnumMovingType.MovingPath;
			npc.ais.canSprint = true;
			npc.ais.movingPause = false;
		} else {
			npc.ais.setMovingType(0);
			//npc.ais.movingType = EnumMovingType.Standing;
			npc.ais.canSprint = false;
			npc.ais.movingPause = true;
		}

		// TODO: figure out if the next two lines are the same
		//npc.updateTasks();
		npc.updateClient();
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