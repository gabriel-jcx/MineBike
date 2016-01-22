package org.ngs.bigx.input.tobiieyex;

import java.nio.ByteBuffer;
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
//    	byte[] subArrayVar = new byte[4];
//    	
//        //The first four bytes are for the Command
////        this.cmdCommand = (DataIdentifier)BitConverter.ToInt32(data, 0);
//    	System.arraycopy(data, 0, subArrayVar, 0, 4);
//    	this.cmdCommand = DataIdentifier.fromInt(java.nio.ByteBuffer.wrap(subArrayVar).getInt());
//
//        //The next four store the length of the name
//    	System.arraycopy(data, 4, subArrayVar, 0, 4);
//        int xLen = java.nio.ByteBuffer.wrap(subArrayVar).getInt();
//
//        //The next four store the length of the message
//    	System.arraycopy(data, 8, subArrayVar, 0, 4);
//        int yLen = java.nio.ByteBuffer.wrap(subArrayVar).getInt();
//
//        //Makes sure that strName has been 
//        //passed in the array of bytes
//        if (xLen > 0)
//        {
//        	subArrayVar = new byte[xLen];
//        	System.arraycopy(data, 12, subArrayVar, 0, xLen);
//            this.X = java.nio.ByteBuffer.wrap(subArrayVar).getDouble();
//        }
//        else
//        {
//            this.X = 0;
//        }
//
//        //This checks for a null message field
//        if (yLen > 0)
//        {
//        	subArrayVar = new byte[yLen];
//        	System.arraycopy(data, 12 + xLen, subArrayVar, 0, yLen);
//            this.Y = java.nio.ByteBuffer.wrap(subArrayVar).getDouble();
//        }
//        else
//        {
//            this.Y = 0;
//        }
    	
    	ByteBuffer buf = ByteBuffer.wrap(data);

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

        //First four are for the Command
    	buf.putInt(cmdCommand.getValue());
    	buf.putInt(Integer.SIZE);
    	buf.putInt(Integer.SIZE);
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
