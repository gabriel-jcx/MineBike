package org.ngs.bigx.minecraft.bike;

import org.ngs.bigx.minecraft.client.ClientEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PedalingComboSoundEffect implements IPedalingComboEvent {
	
	public PedalingComboSoundEffect()
	{
		// Register the pedaling combo sound effect
		ClientEventHandler.pedalingCombo.addPedalingComboEvent(this);
	}

	@Override
	public void onLevelChange(int oldLevel, int newLevel) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if(player == null)
			return;
		
		if(oldLevel > newLevel) {
			// Level Decrease
			System.out.println("onLevelChange[sound down]["+oldLevel+"]["+newLevel+"]");
			player.worldObj.playSoundAtEntity(player, "pedalingleveldown", 1.0f, 1.0f);
		}
		else if(oldLevel < newLevel) {
			// Level Increase
			System.out.println("onLevelChange[sound up]");
			player.worldObj.playSoundAtEntity(player, "powerup", 1.0f, 1.0f);
		}
	}

	@Override
	public void onOneGaugeFilled() {
	}

}
