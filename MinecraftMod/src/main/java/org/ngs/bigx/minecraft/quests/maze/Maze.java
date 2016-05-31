package org.ngs.bigx.minecraft.quests.maze;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

	/******************************************************************************
	 *  Compilation:  javac Maze.java
	 *  Execution:    java Maze.java N
	 *  Dependencies: StdDraw.java
	 *
	 *  Generates a perfect N-by-N maze using depth-first search with a stack.
	 *
	 *  % java Maze 62
	 *
	 *  % java Maze 61
	 *
	 *  Note: this program generalizes nicely to finding a random tree
	 *        in a graph.
	 *
	 ******************************************************************************/

	public class Maze {
	    private int N;                 // dimension of maze
	    public boolean[][] north;     // is there a wall to north of cell i, j
	    public boolean[][] east;
	    public boolean[][] south;
	    public boolean[][] west;
	    private boolean[][] visited;
	    private boolean[][] deadend;
	    private boolean done = false;
	    private int countDeadend;
	    
	    public int getCountDeadend()
	    {
	    	return countDeadend;
	    }
	    
	    public boolean[][] getDeadend()
	    {
	    	return this.deadend;
	    }

	    public Maze(int N) {
	        this.N = N;
	        this.countDeadend = 0;
//	        StdDraw.setXscale(0, N+2);
//	        StdDraw.setYscale(0, N+2);
	        init();
	        generate();
	    }

	    private void init() {
	        // initialize border cells as already visited
	        visited = new boolean[N+2][N+2];
	        for (int x = 0; x < N+2; x++) {
	            visited[x][0] = true;
	            visited[x][N+1] = true;
	        }
	        for (int y = 0; y < N+2; y++) {
	            visited[0][y] = true;
	            visited[N+1][y] = true;
	        }


	        // initialze all walls as present
	        north = new boolean[N+2][N+2];
	        east  = new boolean[N+2][N+2];
	        south = new boolean[N+2][N+2];
	        west  = new boolean[N+2][N+2];
	        deadend = new boolean[N+2][N+2];
	        for (int x = 0; x < N+2; x++) {
	            for (int y = 0; y < N+2; y++) {
	                north[x][y] = true;
	                east[x][y]  = true;
	                south[x][y] = true;
	                west[x][y]  = true;
	                deadend[x][y]  = false;
	            }
	        }
	    }


	    // generate the maze
	    private void generate(int x, int y) {
	        visited[x][y] = true;
	        
	        if(!(!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]))
	        {
	        	deadend[x][y] = true;
	        	this.countDeadend++;
	        }

	        // while there is an unvisited neighbor
	        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

	            // pick random neighbor (could use Knuth's trick instead)
	            while (true) {
	                double r = StdRandom.uniform(4);
	                if (r == 0 && !visited[x][y+1]) {
	                    north[x][y] = false;
	                    south[x][y+1] = false;
	                    generate(x, y + 1);
	                    break;
	                }
	                else if (r == 1 && !visited[x+1][y]) {
	                    east[x][y] = false;
	                    west[x+1][y] = false;
	                    generate(x+1, y);
	                    break;
	                }
	                else if (r == 2 && !visited[x][y-1]) {
	                    south[x][y] = false;
	                    north[x][y-1] = false;
	                    generate(x, y-1);
	                    break;
	                }
	                else if (r == 3 && !visited[x-1][y]) {
	                    west[x][y] = false;
	                    east[x-1][y] = false;
	                    generate(x-1, y);
	                    break;
	                }
	            }
	        }
	    }
	    
	 // draw the maze
	    public void draw() {
	        StdDraw.setPenColor(StdDraw.RED);
	        //StdDraw.filledCircle(N/2.0 + 0.5, N/2.0 + 0.5, 0.375);
	        //StdDraw.filledCircle(1.5, 1.5, 0.375);

	        StdDraw.setPenColor(StdDraw.BLACK);
	        for (int x = 1; x <= N; x++) {
	            for (int y = 1; y <= N; y++) {
	                if (south[x][y]) StdDraw.line(x, y, x + 1, y);
	                if (north[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
	                if (west[x][y])  StdDraw.line(x, y, x, y + 1);
	                if (east[x][y])  StdDraw.line(x + 1, y, x + 1, y + 1);
	                if (deadend[x][y]) {
	                    System.out.print("x[" + x + "] y[" + y + "]\t");
	                	StdDraw.filledCircle(x + 0.5, y + 0.5, 0.375);
	                }
	            }
	        }
	        StdDraw.show(1000);
	    }

	    // generate the maze starting from lower left
	    private void generate() {
	        generate(1, 1);

	/*
	        // delete some random walls
	        for (int i = 0; i < N; i++) {
	            int x = 1 + StdRandom.uniform(N-1);
	            int y = 1 + StdRandom.uniform(N-1);
	            north[x][y] = south[x][y+1] = false;
	        }

	        // add some random walls
	        for (int i = 0; i < 10; i++) {
	            int x = N/2 + StdRandom.uniform(N/2);
	            int y = N/2 + StdRandom.uniform(N/2);
	            east[x][y] = west[x+1][y] = true;
	        }
	*/
	     
	    }
}
