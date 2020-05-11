package org.ngs.bigx.minecraft.client.skills;

import net.minecraft.client.Minecraft;

import org.ngs.bigx.minecraft.context.BigxClientContext;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkillEventHandler {
	private BigxClientContext context;
	private static SkillEventHandler handler;
	private static int tickCount = 0;
	
	private int skillTypeId = 5;
	
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
		if ((Minecraft.getMinecraft().player!=null) && (event.phase==TickEvent.Phase.END)) {
			tickCount++;
			
			if(tickCount < 5)
			{
//				System.out.println("tick["+tickCount+"]");
				return;
			}
			
			tickCount = 0;
			
			// Update the Skill Time Variables
//			BigxClientContext.getCurrentGameState().getSkillManager().updateSkillTime();
		}
		else {
//			tickCount = 0;
		}
	}
}
