package org.ngs.bigx.input.tobiieyex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


public class eyeTrackerUDPData {
	public enum DataIdentifier
    {
        HeartBeat(0),
        Null(1);
        
        private final int value;
	    private DataIdentifier(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
        
        public static DataIdentifier fromInt(int i) {
	        for (DataIdentifier b : DataIdentifier.values()) {
	            if (b.getValue() == i) { return b; }
	        }
	        return null;
	    }
    }
	
	//Default constructor
    public eyeTrackerUDPData()
    {
        this.cmdCommand = DataIdentifier.Null;
        this.X = 0;
        this.Y = 0;
    }

    //Converts the bytes into an object of type Data
    public eyeTrackerUDPData(byte[] data)
    {
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	buf.order(ByteOrder.LITTLE_ENDIAN);

    	this.cmdCommand = DataIdentifier.fromInt(buf.getInt());
    	int xLen = buf.getInt();
    	int yLen = buf.getInt();
    	this.X = buf.getDouble();
    	this.Y = buf.getDouble();
    }

    //Converts the Data structure into an array of bytes
    public byte[] ToByte()
    {
    	ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE*3 + Double.SIZE*2);
    	buf.order(ByteOrder.LITTLE_ENDIAN);

        //First four are for the Command
    	buf.putInt(cmdCommand.getValue());
    	buf.putInt(Integer.SIZE / 8);
    	buf.putInt(Integer.SIZE / 8);
    	buf.putDouble(this.X);
    	buf.putDouble(this.Y);

        return buf.array();
    }

    //Name by which the client logs into the room
    public double X;
    //Message text
    public double Y;
    //Command type (login, logout, send message, etc)
    public DataIdentifier cmdCommand;
}
