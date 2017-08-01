package org.ngs.bigx.minecraft.bike;

import java.util.ArrayList;
import java.util.List;

public class PedalingCombo {
	private int level = 0;
	private int gauge = 0;					// Pedaling Counts * 2 (80 counts up per second)
	private int gaugeFilled = 0;			// Every Eight Seconds gauge can fill up
	private int gaugeFilledOnCurrentLevel = 0;			// Every Eight Seconds gauge can fill up

	private static final int gaugeUpperLimit = 640;		// Every Eight Seconds
	private static final int gaugeFilledUpperLimit = 3; // Every Three gauges level up (3 gauges for Level 1)
	private static final int gaugeLevelUpperLimit = 3;  // Max Level
	private static final int gaugeMaxLimit = gaugeUpperLimit*gaugeFilledUpperLimit*gaugeLevelUpperLimit;  // Max Level
	private static final int gaugeDegradationRate = 1;	// Each Tick
	
	private List<IPedalingComboEvent> pedalingComboEvents = new ArrayList<IPedalingComboEvent>();
	
	public PedalingCombo()
	{
		this.init();
	}
	
	public void init()
	{
		this.gauge = 0;
		this.calculateGaugeMechanicChange();
	}
	
	public int getLevel() {
		return level;
	}

	public int getGauge() {
		return gauge;
	}

	public int getGaugeFilledOnCurrentLevel() {
		return gaugeFilledOnCurrentLevel;
	}

	public boolean addPedalingComboEvent(IPedalingComboEvent pedalingComboEvent)
	{
		synchronized (pedalingComboEvents) {
			if(!this.pedalingComboEvents.contains(pedalingComboEvent))
			{
				this.pedalingComboEvents.add(pedalingComboEvent);
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public void removePedalingComboEvent(IPedalingComboEvent pedalingComboEvent)
	{
		synchronized (pedalingComboEvents) {
			this.pedalingComboEvents.remove(pedalingComboEvent);
		}
	}
	
	private void calculateGaugeMechanicChange()
	{
		if(this.gauge <= 0)
			this.gauge = 0;
		else if(this.gauge >= gaugeMaxLimit)
			this.gauge = gaugeMaxLimit;
		
		this.gaugeFilled = this.gauge / this.gaugeUpperLimit;
		this.level = this.gaugeFilled / gaugeFilledUpperLimit;		// 3 levels: 3 gauges * 3 Levles = 9 gauges Max
		this.gaugeFilledOnCurrentLevel = this.gaugeFilled % gaugeFilledUpperLimit;
	}
	
	public void increaseGauge(int fuel)
	{
		int oldLevel = this.level;
		int oldGaugeFilled = this.gaugeFilled;
		
		// TODO: NEED TO UNCOMMENT THIS
		this.gauge += (fuel<<1);
		this.calculateGaugeMechanicChange();
		
		if(oldLevel != this.level)
		{
			synchronized (pedalingComboEvents) {
				for(IPedalingComboEvent pedalingComboEvent : this.pedalingComboEvents)
				{
					pedalingComboEvent.onLevelChange(oldLevel, this.level);
				}
			}
		}
		else
		{
			if(oldGaugeFilled != this.gaugeFilled)
			{
				synchronized (pedalingComboEvents) {
					for(IPedalingComboEvent pedalingComboEvent : this.pedalingComboEvents)
					{
						pedalingComboEvent.onOneGaugeFilled();
					}
				}
			}
		}
	}
	
	public void decraseGauge()
	{
		int oldLevel = this.level;
		int oldGaugeFilled = this.gaugeFilled;

		this.gauge -= this.gaugeDegradationRate;
		this.calculateGaugeMechanicChange();
		
		if(oldLevel != this.level)
		{
			synchronized (pedalingComboEvents) {
				for(IPedalingComboEvent pedalingComboEvent : this.pedalingComboEvents)
				{
					pedalingComboEvent.onLevelChange(oldLevel, this.level);
				}
			}
		}
	}
}
