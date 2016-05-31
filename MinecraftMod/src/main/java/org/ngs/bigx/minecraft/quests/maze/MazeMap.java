package org.ngs.bigx.minecraft.quests.maze;

public class MazeMap implements java.io.Serializable{
	public int N;                 // dimension of maze
	public boolean[][] north;     // is there a wall to north of cell i, j
	public boolean[][] east;
	public boolean[][] south;
	public boolean[][] west;
	public boolean[][] deadend;
}
