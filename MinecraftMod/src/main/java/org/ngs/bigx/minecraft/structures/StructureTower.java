package org.ngs.bigx.minecraft.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ngs.bigx.minecraft.themes.Theme;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.world.World;

public class StructureTower extends Structure {
	
	public StructureTower() {
		name = "tower";
		width = 6;
		length = 6;
		depth = 3;
		height = 25;
	}
	
	private static final int storyHeight = 4;
		
	private int getRadius() { return width/2; }
	
	public void generate(World world,int x,int y,int z,Theme theme,Random random) {
		int minStories = 2;
		int maxStories = 5;
		int stories = minStories + random.nextInt(maxStories-minStories);
		int height = stories * storyHeight;
		int radius = getRadius();
		//Walls
		ArrayList<Point> points = getCircle(x,y,z,radius);
		for(Point point : points) {
			buildWall(world, point.x, point.z, y, theme, random,height);
		}
		//Floors
		for (int s=0;s<stories;s++) {
			points = getDisc(x,y+s*storyHeight,z,getRadius()-1,false);
			for (Point point:points) {
				Block bl = theme.getFloor().getBlock();
				int mt = theme.getFloor().getMeta();
				if (random.nextInt(5)<1) {
					bl = theme.getAlternateFloor().getBlock();
					mt = theme.getAlternateFloor().getMeta();
				}
				world.setBlock(point.x, point.y,point.z, bl,mt,1+2);
			}
		}
		//Roof
		int roofRadius = radius + 1;
		int roofHeight = height-1;
		while(roofRadius>=0) {
			points = getDisc(x,y+roofHeight,z,roofRadius,true);
			for(Point point : points) {
				Block bl = theme.getRoof().getBlock();
				int mt = theme.getRoof().getMeta();
				world.setBlock(point.x, y+roofHeight, point.z, bl, mt, 1+2);
			}
			roofHeight++;
			roofRadius--;
		}
		decorate(world,x,y,z,theme,random,stories);
	}
	
	public void decorate(World world,int x,int y,int z,Theme theme,Random random,int stories) {
		
		//Torches
		for(int s=0;s<stories;s++) {
			world.setBlock(x-width/2+1, y+s*storyHeight+2, z, theme.getTorch().getBlock(), 1, 1+3);
			world.setBlock(x+width/2-1, y+s*storyHeight+2, z, theme.getTorch().getBlock(), 2, 1+3);
		}
		
		//Ladder
		for(int i=1;i<(stories-1)*storyHeight+1;i++) {
			world.setBlock(x, y+i, z-length/2+1, Blocks.ladder, 3, 1+2);
		}
		
		//Centerpiece
		world.setBlock(x, y, z, theme.getCenter().getBlock(), theme.getCenter().getMeta(), 1+2);
		
		//Doorway
		world.setBlock(x, y+1, z+length/2, Blocks.air);
		world.setBlock(x, y+2, z+length/2, Blocks.air);
		
		//Door
		int orientation = 3;
		ItemDoor.placeDoorBlock(world, x, y+1, z+length/2, orientation, theme.getDoor().getBlock());
		
		//Lever
		if (theme.getDoor().getBlock()==Blocks.iron_door) {
			world.setBlock(x-1, y+2, z+length/2+1, Blocks.lever, 3, 1+2);
		}
	}
	
	private void buildWall(World world,int i,int j,int y,Theme theme,Random random,int height) {
		for (int k=y;k<y+height;k++) {
			//Walls
			Block bl = theme.getWall().getBlock();
			int mt = theme.getWall().getMeta();
			if (random.nextInt(5)<1) {
				bl = theme.getAlternateWall().getBlock();
				mt = theme.getAlternateWall().getMeta();
			}
			world.setBlock(i, k, j, bl, mt, 1+2);
		}
	}
	
	private class Point {
		int x;
		int y;
		int z;
		
		public Point(int x,int y,int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	private ArrayList<Point> getCircle(int x,int y,int z,int radius) {
		ArrayList<Point> points = new ArrayList<Point>();
		if (radius==0) {
			points.add(new Point(x,y,z));
			return points;
		}
		int curX = radius;
		int curZ = 0;
		int decisionOver2 = 1 - curX;
		//Circle algorithm (thanks wikipedia)
		while(curX>=curZ) {
			points.add(new Point(x+curX,y,z+curZ));
			points.add(new Point(x+curZ,y,z+curX));
			points.add(new Point(x-curX,y,z+curZ));
			points.add(new Point(x-curZ,y,z+curX));
			points.add(new Point(x-curX,y,z-curZ));
			points.add(new Point(x-curZ,y,z-curX));
			points.add(new Point(x+curX,y,z-curZ));
			points.add(new Point(x+curZ,y,z-curX));
			curZ++;
		    if (decisionOver2<=0)
		    {
		      decisionOver2 += 2 * curZ + 1;   // Change in decision criterion for y -> y+1
		    }
		    else
		    {
		      curX--;
		      decisionOver2 += 2 * (curZ - curX) + 1;   // Change for y -> y+1, x -> x-1
		    }
		}
		return points;
	}
	
	private ArrayList<Point> getDisc(int x,int y,int z, int radius,boolean outline) {
		ArrayList<Point> points = new ArrayList<Point>();
		if (radius==0) {
			points.add(new Point(x,y,z));
			return points;
		}
		for(int i=x-radius;i<=x+radius;i++) {
			for (int j=z-radius;j<=z+radius;j++) {
				if ((Math.pow(i-x,2)+Math.pow(j-z,2))<=Math.pow(radius+0.5,2)) {
					if (!outline||(Math.pow(i-x,2)+Math.pow(j-z,2))>=Math.pow(radius-1,2)) {
						points.add(new Point(i,y,j));
					}
				}
			}
		}
		return points;
	}
}