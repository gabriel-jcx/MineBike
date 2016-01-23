package org.ngs.bigx.input.tobiieyex;

import java.io.Console;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.ngs.bigx.input.tobiieyex.eyeTrackerUDPData.DataIdentifier;

public class eyeTracker extends Thread{
	private DatagramSocket socket;
	private Vector<eyeTrackerListner> eyetrackerlistners;
	
	private final String EYEXUDPSERVER_IP = "127.0.0.1";
	private final int EYEXUDPSERVER_PORT = 30000;
	
	private final int EYEXUDPCLINET_PORT = 30001;
	
	public eyeTracker() throws SocketException
	{
		this.socket = new DatagramSocket(EYEXUDPCLINET_PORT);
		this.eyetrackerlistners = new Vector<eyeTrackerListner>();
	}
	
	public void connect() throws IOException
	{
		byte[] bytearray;
		eyeTrackerUDPData data = new eyeTrackerUDPData();
		InetAddress address = InetAddress.getByName(EYEXUDPSERVER_IP);
		
		data.cmdCommand = DataIdentifier.HeartBeat;
		data.X = 0;
		data.Y = 0;
		
		bytearray = data.ToByte();
		
		DatagramPacket sendPacket = new DatagramPacket(bytearray, bytearray.length,
				address, EYEXUDPSERVER_PORT);
		
		this.socket.send(sendPacket);
	}
	
	public void run() 
	{
		while (true) 
		{
			byte[] data = new byte[Integer.SIZE*3 + Double.SIZE*2];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
				eyeTrackerUDPData trackerData = new eyeTrackerUDPData(data);
				
				for (eyeTrackerListner listner : this.eyetrackerlistners) {
					listner.onMessageReceive(null, trackerData);
					//System.out.println("x[" + trackerData.X + "] y[" + trackerData.Y + "]");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addEyeTrackerListener(eyeTrackerListner listner)
	{
		this.eyetrackerlistners.add(listner);
	}
	
	public void removeEyeTrackerListener(eyeTrackerListner listner)
	{
		this.eyetrackerlistners.remove(listner);
	}
}
