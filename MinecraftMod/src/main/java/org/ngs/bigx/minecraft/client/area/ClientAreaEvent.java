package org.ngs.bigx.minecraft.client.area;

import java.util.ArrayList;

import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.client.area.Area.AreaTypeEnum;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class ClientAreaEvent {
	private static boolean areaChangedFlag = false;
	
	public static Area previousArea = null;

	private static ArrayList<Area> areaListEvent = new ArrayList<Area>();
	private static ArrayList<Area> areaListRoom = new ArrayList<Area>();
	private static ArrayList<Area> areaListBuilding = new ArrayList<Area>();
	private static ArrayList<Area> areaListPlace = new ArrayList<Area>();	// e.g CITY(VILLAGE) / CASE / CASTLE... 
	private static ArrayList<Area> areaListWorld = new ArrayList<Area>();
	
	public static boolean isAreaChange()
	{
		return areaChangedFlag;
	}
	
	public static void unsetAreaChangeFlag()
	{
		areaChangedFlag = false;
	}
	
	public static void detectAreaChange(EntityPlayer player)
	{
		if(previousArea == null)
		{
			previousArea = new Area(Vec3.createVectorHelper(0, 0, 1.0), Vec3.createVectorHelper(0, 0, 0), 
					"Nowhere", AreaTypeEnum.NOTASSIGNED, 0);
		}
		
		Area currentArea = detectCurrentArea(player);
		
		if(currentArea == null)
		{
			currentArea = new Area(Vec3.createVectorHelper(-100000, 0, -100000), Vec3.createVectorHelper(100000, 0, 100000), 
					"Nowhere", AreaTypeEnum.NOTASSIGNED, player.dimension);
		}
		
		if(!compareAreas(currentArea, previousArea))
		{
			areaChangedFlag = true;
			previousArea = currentArea;
		}
	}
	
	
	
	public static void initArea()
	{
		addArea(new Area(Vec3.createVectorHelper(-176, 65, 334), Vec3.createVectorHelper(-171, 73, 338), 
				BiGXTextBoxDialogue.placeCaveHiddenRoom, AreaTypeEnum.ROOM, 0), AreaTypeEnum.ROOM);
		
		addArea(new Area(Vec3.createVectorHelper(-167, 70, 343), Vec3.createVectorHelper(-166, 74, 346), 
				BiGXTextBoxDialogue.soundComment, AreaTypeEnum.ROOM, 0), AreaTypeEnum.ROOM);
		
		addArea(new Area(Vec3.createVectorHelper(-61, 77, 8), Vec3.createVectorHelper(-56, 78, 11), 
				BiGXTextBoxDialogue.wakeUpMsg, AreaTypeEnum.EVENT, 0), AreaTypeEnum.EVENT);
		
		addArea(new Area(Vec3.createVectorHelper(-175, 65, 335), Vec3.createVectorHelper(-172, 73, 337), 
				BiGXTextBoxDialogue.questChasePotionInfo, AreaTypeEnum.EVENT, 0), AreaTypeEnum.EVENT);
		
		addArea(new Area(Vec3.createVectorHelper(-82, 40, 3), Vec3.createVectorHelper(-50, 100, 19), 
				BiGXTextBoxDialogue.placeHome, AreaTypeEnum.BUILDING, 0), AreaTypeEnum.BUILDING);
		
		addArea(new Area(Vec3.createVectorHelper(-69, 40, 71), Vec3.createVectorHelper(-34, 100, 106), 
				BiGXTextBoxDialogue.placeMarket, AreaTypeEnum.BUILDING, 0), AreaTypeEnum.BUILDING);
		
		addArea(new Area(Vec3.createVectorHelper(-131, 40, -18), Vec3.createVectorHelper(15, 100, 118), 
				BiGXTextBoxDialogue.placeVillage, AreaTypeEnum.PLACE, 0), AreaTypeEnum.PLACE);
		
		addArea(new Area(Vec3.createVectorHelper(-211, 60, 312), Vec3.createVectorHelper(-138, 80, 367), 
				BiGXTextBoxDialogue.placeCave, AreaTypeEnum.PLACE, 0), AreaTypeEnum.PLACE);
		
		addArea(new Area(Vec3.createVectorHelper(-1000, 40, -1000), Vec3.createVectorHelper(1000, 100, 1000), 
				BiGXTextBoxDialogue.placeIslandCaprona, AreaTypeEnum.WORLD, 0), AreaTypeEnum.WORLD);
	}
	
	public static void addArea(Area areaToAdd, AreaTypeEnum areaType)
	{
		switch(areaType)
		{
		case EVENT:
			areaListEvent.add(areaToAdd);
			break;
		case ROOM:
			areaListRoom.add(areaToAdd);
			break;
		case BUILDING:
			areaListBuilding.add(areaToAdd);
			break;
		case PLACE:
			areaListPlace.add(areaToAdd);
			break;
		case WORLD:
			areaListWorld.add(areaToAdd);
			break;
		default:
			break;
		}
	}
	
	private static Area detectCurrentArea(EntityPlayer player)
	{
		Area returnArea = null;

		if((returnArea = isPlayerInAreaList(player, areaListEvent)) != null)
			return returnArea;
		else if((returnArea = isPlayerInAreaList(player, areaListRoom)) != null)
			return returnArea;
		else if((returnArea = isPlayerInAreaList(player, areaListBuilding)) != null)
			return returnArea;
		else if((returnArea = isPlayerInAreaList(player, areaListPlace)) != null)
			return returnArea;
		else if((returnArea = isPlayerInAreaList(player, areaListWorld)) != null)
			return returnArea;
		else
			return null;
	}
	
	private static Area isPlayerInAreaList(EntityPlayer player, ArrayList<Area> areaList)
	{
		Area returnArea = null;
		
		for(Area area:areaList)
		{
			returnArea = isPlayerInArea(player, area);
			
			if(returnArea != null)
				break;
		}
		
		return returnArea;
	}
	
	private static Area isPlayerInArea(EntityPlayer player, Area area)
	{
		Area returnArea = null;
		
		if(player.dimension != area.dimension)
			return returnArea;
		
		if( (player.posX >= area.pointA.xCoord) && (player.posX <= area.pointB.xCoord) )
		{
			if( (player.posY >= area.pointA.yCoord) && (player.posY <= area.pointB.yCoord) )
			{
				if( (player.posZ >= area.pointA.zCoord) && (player.posZ <= area.pointB.zCoord) )
				{
					returnArea = area;
				}
			}
		}
		
		return returnArea;
	}
	
	private static boolean compareAreas(Area areaA, Area areaB)
	{
		if(areaA == null)
			return false;
		
		if(areaB == null)
			return false;
		
		return areaA.equals(areaB);
	}
}
