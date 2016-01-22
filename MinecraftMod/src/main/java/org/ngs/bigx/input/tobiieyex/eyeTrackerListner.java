package org.ngs.bigx.input.tobiieyex;

import java.awt.Event;
import java.util.EventListener;

public interface eyeTrackerListner extends EventListener {
	public void onMessageReceive(Event event, eyeTrackerUDPData trackerData);
}
