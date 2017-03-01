package org.ngs.bigx.minecraft.client.area;

import net.minecraft.util.Vec3;

public class Area
{
	public enum AreaTypeEnum
	{
		ROOM, BUILDING, PLACE, WORLD, NOTASSIGNED
	}
	
	public Area(Vec3 pointA, Vec3 pointB, String name, AreaTypeEnum type)
	{
		this.pointA = pointA;
		this.pointB = pointB;
		this.name = name;
		this.type = type;
	}
	
	public Vec3 pointA;
	public Vec3 pointB;
	public String name = "";
	public AreaTypeEnum type = AreaTypeEnum.NOTASSIGNED;
	
	@Override
	public boolean equals(Object object)
	{
		Area area = (Area)object;
		boolean returnValue = false;
		
		if(this.pointA.distanceTo(area.pointA)==0)
		{
			if(this.pointB.distanceTo(area.pointB)==0)
			{
				returnValue = true;
			}
		}
		
		return returnValue;
	}
}
