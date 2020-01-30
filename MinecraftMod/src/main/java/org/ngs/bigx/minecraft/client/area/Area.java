package org.ngs.bigx.minecraft.client.area;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.ResourceLocation;


public class Area
{
	public enum AreaTypeEnum
	{
		EVENT, ROOM, BUILDING, PLACE, WORLD, NOTASSIGNED
	}

	public Area(Vec3d pointA, Vec3d pointB, String name, AreaTypeEnum type, int dimension, int id)
	{
		this.pointA = pointA;
		this.pointB = pointB;
		this.name = name;
		this.type = type;
		this.dimension = dimension;
		this.id = id;
	}
	
	private int id = 0;
	public Vec3d pointA;
	public Vec3d pointB;
	public String name = "";
	public AreaTypeEnum type = AreaTypeEnum.NOTASSIGNED;
	public int dimension = 0;
	
	public int getId()
	{
		return this.id;
	}
	
	@Override
	public boolean equals(Object object)
	{
		Area area = (Area)object;
		boolean returnValue = false;
		
		if(((Area)object).dimension != this.dimension)
			return returnValue;
		
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
