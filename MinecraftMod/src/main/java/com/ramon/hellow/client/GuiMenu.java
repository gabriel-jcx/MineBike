package com.ramon.hellow.client;

import java.util.Calendar;
import java.util.Date;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;

public class GuiMenu extends GuiMainMenu {
	
	@Override
	public void initGui()
    {
		super.initGui();
		int i = this.height / 4 + 48;
		this.buttonList.add(new GuiButton(13,this.width / 2 + 102,i+72+12,100,20,"Connection"));
    }
	
	@Override
	protected void actionPerformed(GuiButton button)
    {
		super.actionPerformed(button);
		if (button.id==13) {
			this.mc.displayGuiScreen(new GuiConnection(this));
		}
    }
}