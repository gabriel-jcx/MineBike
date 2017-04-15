package org.ngs.bigx.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.npcs.NpcDatabase;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class NpcCommand {
	
	private EntityCustomNpc npc;
	private int role; //0=no role, 1=transporter
	
	private static boolean npcSpawnFlag = false;
	
	public NpcCommand(EntityCustomNpc npc) {
		this.npc = npc;
		this.role = 0;
	}
	
	public void setNPC(EntityCustomNpc npc) {
		this.npc = npc;
		this.role = 0;
	}
	
	public static EntityCustomNpc spawnNpc(float x, float y, float z, World w, String name) {
		EntityCustomNpc npc = new EntityCustomNpc(w);
		npc.display.name = name;
		npc.setPosition(x, y, z);
		
		npc.ai.startPos = new int[]{
	    		MathHelper.floor_double(x),
	    		MathHelper.floor_double(y),
	    		MathHelper.floor_double(z)};
		
		w.spawnEntityInWorld(npc);
	    npc.setHealth(999999999f);;
	    
	    return npc;
	}
	
	public static void setNpcSpawnFlag()
	{
		npcSpawnFlag = true;
	}
	
	public static void spawnNpcInDB(World world)
	{
		if(!npcSpawnFlag)
			return;
		
		if (world.provider.dimensionId == 0){
			npcSpawnFlag = false;
//			System.out.println("DIMENSION ID == 0");
			
			WorldServer ws = MinecraftServer.getServer().worldServerForDimension(0);
			
			// NPC CHECKING
			for (String name : NpcDatabase.NpcNames()) {
				int found = 0;
				for (Object obj : NpcCommand.getCustomNpcsInDimension(0))
					if (((EntityCustomNpc)obj).display.name.equals(name))
						++found;
				if (found == 0) {
					NpcDatabase.spawn(ws, name);
				} else if (found > 1) {
					List<EntityCustomNpc> list = new ArrayList<EntityCustomNpc>();
					for (Object obj : NpcCommand.getCustomNpcsInDimension(0))
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
	
	public void runInDirection(final ForgeDirection direction) {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (npc.hasDied) {
					timer.cancel();
				}
				//System.out.println("AI : " + npc.motionX + " " + npc.motionZ);
				int yy = (int)npc.posY;
				npc.ai.startPos = new int[]{(int)npc.posX, (int)npc.posY, (int)npc.posZ};
				npc.ai.setMovingPath(new ArrayList());
				switch(direction) {
				case NORTH:
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX, yy, (int)npc.posZ - 20});
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX, yy, (int)npc.posZ - 30});
					break;
				case SOUTH:
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX, yy, (int)npc.posZ + 20});
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX, yy, (int)npc.posZ + 30});
					break;
				case EAST:
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX + 20, yy, (int)npc.posZ});
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX + 30, yy, (int)npc.posZ});
					break;
				case WEST:
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX - 20, yy, (int)npc.posZ});
					npc.ai.getMovingPath().add(new int[]{(int) npc.posX - 30, yy, (int)npc.posZ});
					break;
				}
				npc.ai.getMovingPath().remove(0);
			}
		}, 0, 1000);
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
		Iterator iter = ws.loadedEntityList.iterator();
		while (iter.hasNext()) {
			Entity entity = (Entity)iter.next();
	         if(entity instanceof EntityNPCInterface) {
	            list.add((EntityNPCInterface)entity);
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
	}
	
	public void makeTransporter(boolean enable){
		if (enable){
			role = 1;
		}
	}
	
	public void transportPlayer(){
		WorldServer ws = MinecraftServer.getServer().worldServerForDimension(WorldProviderFlats.dimID);
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (ws != null) {
			QuestTeleporter teleporter = new QuestTeleporter(ws);
			teleporter.teleport(player, ws);
		}
	}
	
	public boolean isInteracting(){
		return npc.isInteracting();
	}
}
