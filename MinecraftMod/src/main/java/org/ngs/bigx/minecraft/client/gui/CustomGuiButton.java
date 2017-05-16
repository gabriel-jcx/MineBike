/**
 * http://www.minecraftforge.net/forum/topic/29503-1710-changing-button-texture-solved-but-have-questions/
 */

package org.ngs.bigx.minecraft.client.gui;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class CustomGuiButton extends GuiButton
{
	public ResourceLocation buttonTexture;

	public CustomGuiButton(int id, int x, int y, int width, int height, String displayString, String textureName)
	{
		super(id, x, y, width, height, displayString);
		buttonTexture = new ResourceLocation(BiGX.TEXTURE_PREFIX , textureName);
	}
	 
	/**
     * Draws this button to the screen.
     */
	@Override
	public void drawButton(Minecraft minecraft, int xCoord, int yCoord)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = minecraft.fontRenderer;
            minecraft.getTextureManager().bindTexture(buttonTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = xCoord >= this.xPosition && yCoord >= this.yPosition && xCoord < this.xPosition + this.width && yCoord < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
//            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(minecraft, xCoord, yCoord);
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.field_146123_n)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 4) / 2, l);
        }
    }
}