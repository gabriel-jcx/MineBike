package org.ngs.bigx.minecraft.client;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.BiGXPacketHandler;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.BiGXConnectionStateManagerClass;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiConnection extends GuiScreen {
	private final GuiScreen back;
	protected String name = "Connection";
	private static Context context;
	
	public GuiConnection(Context context,GuiScreen back) {
		this.back = back;
		this.context = context;
	}
	
	@Override
	public void initGui() {
		this.name = I18n.format("gui.connection.title", new Object[0]);
		
		this.buttonList.add(new GuiButton(1, this.width / 2 - 150 , this.height / 6 + 12 , 100 , 20 , 
				I18n.format("gui.connection.connect", new Object[0])));

		this.buttonList.add(new GuiButton(2, this.width / 2 + 50 , this.height / 6 + 12 , 100 , 20 , 
				I18n.format("gui.connection.test", new Object[0])));
		
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100 , this.height / 6 + 168, 
				I18n.format("gui.done", new Object[0])));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id)
		{
		case 1:
			try {
				BiGXPacketHandler.connect(context.bigxclient);
			} catch (SocketException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (BiGXNetException e1) {
				e1.printStackTrace();
			} catch (BiGXInternalGamePluginExcpetion e1) {
				e1.printStackTrace();
			}
			
			try {
				startConnectionManagerTimer(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			break;
		case 3:
			this.mc.displayGuiScreen(this.back);
			break;
		default:
			System.out.println("Clicked Unimplemented Button");
			break;
		};
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.name, this.width / 2, 15, 16777215);
        String msg = EnumChatFormatting.RED+I18n.format("gui.connection.connected.false", new Object[0]);
        if (context.bigxclient.getIsConnected() == true) {
        	msg = EnumChatFormatting.GREEN+I18n.format("gui.connection.connected.true", new Object[0]);
        }
        this.drawCenteredString(this.fontRendererObj, msg, this.width / 2, this.height / 6 + 40, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
	
	public synchronized void startConnectionManagerTimer(int delayInMilisecond) throws Exception
	{
		if(context.connectionStateManager.timer != null)
		{
			throw new Exception("Connection Management Timer is running.");
		}
		
		context.connectionStateManager.timer = new Timer();
		context.connectionStateManager.isTimerRunning = true;
		context.connectionStateManager.State = org.ngs.bigx.minecraft.BiGXConnectionStateManagerClass.connectionStateEnum.CONNECTING;
		context.connectionStateManager.numberOfTries = 0;
		context.connectionStateManager.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(context.bigxclient.getIsConnected()){
					try {
						stopConnectionManagerTimer();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					context.connectionStateManager.numberOfTries++;
					
					if(context.connectionStateManager.numberOfTries >= BiGXConnectionStateManagerClass.MAXTRY)
					{
						try {
							stopConnectionManagerTimer();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					try {
						BiGXPacketHandler.connect(context.bigxclient);
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (BiGXNetException e) {
						e.printStackTrace();
					} catch (BiGXInternalGamePluginExcpetion e) {
						e.printStackTrace();
					}
					
					System.out.println("[BiGXContext] connection try... " + context.connectionStateManager.numberOfTries);
				}
			}
		}, delayInMilisecond, delayInMilisecond);
	}
	
	public synchronized void stopConnectionManagerTimer() throws Exception
	{
		if(context.connectionStateManager.timer == null)
		{
			throw new Exception("Connection Management Timer is no running.");
		}
		
		context.connectionStateManager.timer.cancel();
		context.connectionStateManager.timer = null;
		context.connectionStateManager.isTimerRunning = false;
	}
}