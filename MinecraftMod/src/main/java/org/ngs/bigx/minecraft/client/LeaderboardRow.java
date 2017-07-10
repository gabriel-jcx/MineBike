package org.ngs.bigx.minecraft.client;

public class LeaderboardRow implements Comparable {
	public String rank;
	public String name;
	public String level;
	public String time_elapsed;
	
	public int compareTo(Object o)
	{
		LeaderboardRow objectToCompare = (LeaderboardRow) o;
		
		if(time_elapsed.equals(objectToCompare.time_elapsed))
            return 0;
		
        return (Double.parseDouble(objectToCompare.time_elapsed) < Double.parseDouble(time_elapsed)) ? 1 : -1;
	}
}
