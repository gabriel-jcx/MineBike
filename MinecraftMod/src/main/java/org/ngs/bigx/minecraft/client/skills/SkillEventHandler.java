package org.ngs.bigx.minecraft.client.skills;

import net.minecraft.client.Minecraft;

import org.ngs.bigx.minecraft.context.BigxClientContext;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SkillEventHandler {
	private BigxClientContext context;
	private static SkillEventHandler handler;
	private static int tickCount = 0;
	
	public SkillEventHandler(BigxClientContext context)
	{
		this.context = context;
		SkillEventHandler.handler = this;
	}
	
	//Called whenever the client ticks
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if ((Minecraft.getMinecraft().thePlayer!=null) && (event.phase==TickEvent.Phase.END)) {
			tickCount++;
			
			if(tickCount < 15)
				return;
			
			tickCount = 0;
			
			// Update the Skill Time Variables
			BigxClientContext.getCurrentGameState().getSkillManager().updateSkillTime();
		}
		else {
			tickCount = 0;
		}
	}
}
