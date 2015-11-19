package org.ngs.bigx.minecraft.client;

import java.util.Calendar;
import java.util.Date;

import org.ngs.bigx.minecraft.Context;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;

public class GuiMenu extends GuiMainMenu {
	
	private Context context;
	
	@Override
	public void initGui()
    {
		super.initGui();
		int x = this.width / 2 + 102;
		int y = this.height / 4 + 48 + 72 + 12;
		int width = 100;
		int height = 20;
		this.buttonList.add(new GuiButton(13,x,y,width,height,"Connection"));
    }
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		super.actionPerformed(button);
		if (button.id==13 && context!=null && context.modEnabled) {
			this.mc.displayGuiScreen(new GuiConnection(context,this));
		}
    }
	
	public void setContext(Context context) {
		this.context = context;
	}
}