package org.ngs.bigx.minecraft.quests.custom;

import java.time.Clock;
import java.util.ArrayList;

import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudTexture;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract.Difficulty;

public class Display {

	private HudTexture[] orders; //array of the textures on screen
	private int expireTime; //time till order expires in milliseconds
	private long[] startTimes; //holds the time each order started
	private HudRectangle[] blinkers; // array of blinkers
	private Difficulty difficulty;
	
	private Clock clock;
	
	public Display(Difficulty dif)
	{
		orders = new HudTexture[5];
		startTimes = new long[5];
		clock = Clock.systemDefaultZone();
		blinkers = new HudRectangle [5];
		
		difficulty = dif;
		setDifficulty();
	}
	
	//returns true if there are ten seconds until order expires
	//changes the color of the blinker to red
	public boolean tenSecLeft(int i)
	{
		long m = clock.millis();
		if(blinkers[i] != null && m >= startTimes[i] + expireTime - 10000)
		{
			blinkers[i].color = 0xcc322dff;
			if(m/400 %2 == 0)
				orders[i].alpha = 255;
			else
				orders[i].alpha = 127;
			return true;
		}
		return false;
	}
	
	public void setDifficulty(Difficulty dif)
	{
		difficulty = dif;
		setDifficulty();
	}
	
	//changes the expire time of the orders based on the difficulty
	private void setDifficulty()
	{
		if(difficulty == null) //if difficulty is not specified, default it to medium
			expireTime = 35000;
		else //if difficulty is specified, set expireTime to corresponding values
		{
			switch(difficulty)
			{
			case EASY:
				expireTime = 40000;
				break;
			case MEDIUM:
				expireTime = 35000;
				break;
			case HARD:
				expireTime = 30000;
				break;
			}
		}
	}
	
	public boolean expire(int i)
	{
		long m = clock.millis();
		if(m >= startTimes[i] + expireTime)
		{
			removeOrderTexture(i);
			return true;
		}
		return false;
	}
	
	//unregisters all the orders when quest is finished
	public void unregisterAll() {
		for(HudTexture texture: orders)
		{
			HudManager.unregisterTexture(texture);
		}
		for(HudRectangle rectangle: blinkers)
		{
			HudManager.unregisterRectangle(rectangle);
		}
	}
	
	//creates a new HudTexture, adds it to orders[] at int i and registers the texture with the hudmanager
	public void createOrderTexture(String name, int i)
	{
		HudTexture texture = null;
		switch(i) {
		case 0:
			texture = new HudTexture(50,10,70,70,name);
			break;
		case 1:
			texture = new HudTexture(120,10,70,70,name);
			break;
		case 2:
			texture = new HudTexture(190,10,70,70,name);
			break;
		case 3:
			texture = new HudTexture(260,10,70,70,name);
			break;
		case 4:
			texture = new HudTexture(330,10,70,70,name);
			break;
		}
		
		if(texture!=null)
		{
			orders[i] = texture;
			startTimes[i] = clock.millis();
			HudManager.registerTexture(orders[i]);
			setBlinkers(i);
		}
	}
	
	public void setBlinkers(int i)
	{
		HudRectangle blinker = new HudRectangle(80, 85, 10, 10, 0x2dd65aff);

		switch(i) {
		case 0:
			break;
		case 1:
			blinker.x += 70;
			break;
		case 2:
			blinker.x += 140;
			break;
		case 3:
			blinker.x += 210;
			break;
		case 4:
			blinker.x += 280;
			break;
		}
		HudManager.registerRectangle(blinker);
		blinkers[i] = blinker;
	}

	//unregisters the texture at int i, changes orders[i] to null, and adjusts the sequence of orders on the screen if necessary
	public void removeOrderTexture(int i)
	{
		HudManager.unregisterTexture(orders[i]);
		HudManager.unregisterRectangle(blinkers[i]);
		if(i<4)
		{
			for(int j = i; j <= 3; j++)
			{
				startTimes[j] = startTimes[j+1];
				orders[j] = orders[j+1];
				if(orders[j] != null)
				{
					orders[j].x -= 70;	 
				}
				blinkers[j] = blinkers[j+1];
				if(blinkers[j] != null)
				{
					blinkers[j].x -= 70;	 
				}
			}
		}
		else
		{
			orders[i] = null;
			blinkers[i] = null;
			startTimes[i] = 0;
		}
	}
}
