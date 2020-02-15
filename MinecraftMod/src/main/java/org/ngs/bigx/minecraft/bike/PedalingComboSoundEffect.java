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
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		if(player == null)
			return;
		
		if(oldLevel > newLevel) {
			// Level Decrease
			System.out.println("onLevelChange[sound down]["+oldLevel+"]["+newLevel+"]");
			//player.world.playSoundAtEntity(player, "minebike:pedalingleveldown", 1.0f, 1.0f);
			//Todo:player.playSound("minebike:pedalingleveldown", 1.0f, 1.0f);
		}
		else if(oldLevel < newLevel) {
			// Level Increase
			System.out.println("onLevelChange[sound up]");
			//Todo:player.world.playSound(player, "minebike:powerup", 1.0f, 1.0f);
		}
	}

	@Override
	public void onOneGaugeFilled() {
	}

}
