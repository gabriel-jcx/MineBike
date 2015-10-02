package org.ngs.bigx.dictionary.protocol;

public class specification {
	public class command{
		/* Game plugin sends the REQUEST messages */
		public static final int REQ_CONNECT = 0x1101;
		public static final int REQ_ECHO = 0x1103;

		/* Middleware is supposed to send the ACK message */
		public static final int ACK_CONNECT = 0x1102;
		public static final int ACK_ECHO = 0x1104;
		
		/* Typical data RX/TX */
		public static final int REQ_SEND_DATA = 0x1105;
		public static final int ACK_SEND_DATA = 0x1106;
		
		/* Extra Command */
		public static final int REQ_SET_USERNAME = 0x1107;
		public static final int ACK_SET_USERNAME = 0x1108;
		public static final int REQ_GET_USERNAME = 0x1109;
		public static final int ACK_GET_USERNAME = 0x110A;
	};

	public class dataType{
		public static final int BEGINOFENV = 0x1800; // Beginning of the Environment Sensing Category
		public static final int MASS = 0x1801; // Mass and Weight
		public static final int DISTANCE = 0x1802; // Distance and Length
		public static final int VOLUME = 0x1803; // Capacity and Volume
		public static final int AREA = 0x1804; // Area
		public static final int SPEED = 0x1805; // Speed
		public static final int TEMPERATURE = 0x1806; // Temperature
		public static final int FUELECONOMY = 0x1807; // Fuel Economy
		public static final int PRICE = 0x1808; // Price
		public static final int CURRENCY = 0x1809; // Currency
		public static final int TIME = 0x180A; // Time
		public static final int ACCELERATION = 0x180B; // Acceleration
		public static final int DENSITY = 0x180C; // Density
		public static final int PRESSURE = 0x180D; // Stress and Pressure
		public static final int ENERGY = 0x180F; // Energy and Work
		public static final int POWER = 0x1810; // Power
		public static final int FORCE = 0x1811; // Force
		public static final int TORQE = 0x1812; // Torque
		public static final int FLOWRATE = 0x1813; // Flow rate by volume/mass
		public static final int ILLUMINANCE = 0x1814; // Illuminance
		public static final int RADIATION = 0x1815; // Radiation
		public static final int COMPUTERSTORAGE = 0x1816; // Computer storage
		public static final int DATARATE = 0x1817; // Data transfer rate
		public static final int BUTTONSTATE = 0x1818; // Button States  = e.g state 0~1023 button idle/button pressed/button released)
		public static final int RESISTANCE = 0x1819; // Resistance 
		public static final int ROTATIONSTATE = 0X181A; // State of wheel
		public static final int MOVE_FORWARDBACKWARD = 0x181B; // Movement +: Forward -:Backward
		public static final int MOVE_LEFTRIGHT = 0x181C; // Movement +: Right -:Left
		public static final int ROTATE = 0x181D; // Rotation +: Clockwise -: Counter-Clockwise
		public static final int TIMELAPSE_HEARTRATEREQUIREMENT = 0x181E; // The length of time where user's heart rate is in requirement range.
		public static final int ENDOFENV = 0x1FFF; // The End of the Environment Sensing Category
	
		public static final int BEGINOFBIOSIG = 0x1000; // Beginning of the Human Body Sensing Category
		public static final int BRAIN = 0x1001; // Brain (E = G/Brain Waves Alph, beta, delta, ..., Concentration,)
		public static final int MOTIONACTIVITY = 0x1002; // Motion Activity Sensing (Accelerometers, gyroscopes, altimiters)
		public static final int BLOOD = 0x1003; // Blood Sensing (Oxygen, Pressure, Sugar)
		public static final int RESPIRATION = 0x1004; // Respiration Sensing (Breathe Rate, Lung Capacity,  Residual Volume)
		public static final int HEART = 0x1005; // Heart Sensing (ECS, Heart Rate, HeartRate Variability)
		public static final int BODYTEMPERATURE = 0x1006; // Temperature Sensing
		public static final int MUSCLE = 0x1007; // Muscle Sensing (EMG, Pressure, Strength)
		public static final int BODYCOMPOSITION = 0x1008; // Body Composition Sensing (Weight, Body Fat, Muscle Ratio...)
		public static final int ENDOFBIOSIG = 0x17FF; // The end of the Human Body Sensing Category
	};
	
	public class devices
	{
		/* First two bits are being used to say the product vendor, then the rest two indicate product model. */
		public class products
		{
			/* ARDUINO PRODUCT LINE 0x01.. */
			public static final int ARDUINO_DUEMILANOVE = 0x0101;
		};
	};
}
