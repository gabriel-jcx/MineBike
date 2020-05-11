package org.ngs.bigx.minecraft;

import java.util.Timer;


// This contains a ENUM state of the BiGX connection
public class BiGXConnectionStateManagerClass
{
	public connectionStateEnum State;
	public Timer timer;
	public boolean isTimerRunning;
	public int numberOfTries;
	
	public static final int MAXTRY = 5;	
	// Connection Enumeration
	public enum connectionStateEnum 
	{
		DISCONNECTED(0),
		CONNECTING(1),
		CONNECTED(2);
		
		private final int value;
	    private connectionStateEnum(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }

	    public static connectionStateEnum fromInt(int i) {
	        for (connectionStateEnum b : connectionStateEnum.values()) {
	            if (b.getValue() == i) { return b; }
	        }
	        return null;
	    }
	};
	
	public BiGXConnectionStateManagerClass()
	{
		this.State = connectionStateEnum.DISCONNECTED;
		this.isTimerRunning = false;
		this.numberOfTries = 0;
	}
}