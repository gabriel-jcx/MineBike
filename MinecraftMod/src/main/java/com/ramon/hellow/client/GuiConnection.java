package com.ramon.hellow.client;

import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import com.ramon.hellow.BiGXPacketHandler;
import com.ramon.hellow.Context;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiConnection extends GuiScreen {
	private final GuiScreen back;
	protected String name = "Connection";
	private Context context;
	
	public GuiConnection(Context context,GuiScreen back) {
		this.back = back;
		this.context = context;
	}
	
	@Override
	public void initGui() {
		this.name = I18n.format("gui.connection.title", new Object[0]);
		this.buttonList.add(new GuiButton(1, this.width / 2 - 150 , this.height / 6 + 12 , 100 , 20 , I18n.format("gui.connection.connect", new Object[0])));
		this.buttonList.add(new GuiButton(2, this.width / 2 + 50 , this.height / 6 + 12 , 100 , 20 , I18n.format("gui.connection.test", new Object[0])));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100 , this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id==2) {
			context.connected = false;
			BiGXNetPacket packet = new BiGXNetPacket(BiGXPacketHandler.START, 0x0100, 0x1101, new byte[0]);
			BiGXPacketHandler.sendPacket(packet);
		}
		if (button.id==3) {
			this.mc.displayGuiScreen(this.back);
		}
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.name, this.width / 2, 15, 16777215);
        String msg = EnumChatFormatting.RED+I18n.format("gui.connection.connected.false", new Object[0]);
        if (context.connected==true) {
        	msg = EnumChatFormatting.GREEN+I18n.format("gui.connection.connected.true", new Object[0]);
        }
        this.drawCenteredString(this.fontRendererObj, msg, this.width / 2, this.height / 6 + 40, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}