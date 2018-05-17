package org.ngs.bigx.minecraft.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.bike.PedalingToBuildEventHandler;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.skills.Skill;
import org.ngs.bigx.minecraft.client.skills.Skill.enumSkillState;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.quests.QuestTaskChasing;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiStatsSkill extends GuiScreen {
	
	public static int tick = 0;
	
	private Minecraft mc;
	private BigxClientContext context;
	
	private ResourceLocation SKILLS_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/skills.png");

	public GuiStatsSkill(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	public GuiStatsSkill(BigxClientContext c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	/**
	 * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
	 */
	public static void drawRect(int par0, int par1, int par2, int par3, int par4)
	{
	    int j1;

	    if (par0 < par2)
	    {
	        j1 = par0;
	        par0 = par2;
	        par2 = j1;
	    }

	    if (par1 < par3)
	    {
	        j1 = par1;
	        par1 = par3;
	        par3 = j1;
	    }

	    float f = (float)(par4 >> 24 & 255) / 255.0F;
	    float f1 = (float)(par4 >> 16 & 255) / 255.0F;
	    float f2 = (float)(par4 >> 8 & 255) / 255.0F;
	    float f3 = (float)(par4 & 255) / 255.0F;
	    
	    Tessellator tessellator = Tessellator.instance;
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glColor4f(f1, f2, f3, f);
	    tessellator.startDrawingQuads();
	    tessellator.addVertex((double)par0, (double)par3, 0.0D);
	    tessellator.addVertex((double)par2, (double)par3, 0.0D);
	    tessellator.addVertex((double)par2, (double)par1, 0.0D);
	    tessellator.addVertex((double)par0, (double)par1, 0.0D);
	    tessellator.draw();
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	    GL11.glDisable(GL11.GL_BLEND);
	}

	private float lerp(float a, float b, float f) 
	{
	    return (a * (1.0f - f)) + (b * f);
	}
	
	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if(event.isCancelable() || event.type != event.type.TEXT || !context.modEnabled)
	    {      
	      return;
	    }
    	int xPos = 2;
    	int yPos = 2;
    	FontRenderer fontRendererObj;
    	String text;

    	if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
	    	
	    	GL11.glPushMatrix();
	    	
			    GL11.glTranslatef(mcWidth - 30, mcHeight/2 - 40, 0); 
			    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			    GL11.glEnable(GL11.GL_BLEND);
			    
			    /**
			     * Selected Skill Highlight
			     */
			    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
		        drawTexturedModalRect(-2, -2 + 30 * context.getCurrentGameState().getSkillManager().getSelectedSkillIndex() 
		        		, 100, 0, 24 , 24);
			    
			    /**
			     * Skills Background
			     */
			    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
		        drawTexturedModalRect(0, 0, 0, 0, 20 , 20);
		        
			    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
		        drawTexturedModalRect(0, 30, 20, 0, 20 , 20);
		        
			    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
		        drawTexturedModalRect(0, 60, 40, 0, 20 , 20);
		        
			    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
		        drawTexturedModalRect(0, 90, 0, 20, 20 , 20);
		        
		        /**
		         * Update Heart Rate Text
		         */
		        text = "" + context.heartrate;
		    	
	        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
	    		fontRendererObj.drawString(text, 10-fontRendererObj.getStringWidth(text)/2, 96, 0x0);

		        /**
		         * Skills Status
		         */
		        ArrayList<Skill> skillArray = (ArrayList<Skill>) context.getCurrentGameState().getSkillManager().getSkills();
		        Skill aSkill = skillArray.get(0);
		        switch(aSkill.getSkillState())
		        {
		        case IDLE:
		        	if(!aSkill.hasEnoughMana())
		        	{
					    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
				        drawTexturedModalRect(0, 0, 60, 0, 20 , 20);
		        	}
		        	break;
		        case LOCKED:
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 0, 80, 0, 20 , 20);
			        break;
		        case EFFECTIVE:
		        	int effectivetimeLeft = (int) ((double)aSkill.getEffectiveTimeCurrent() * 20.0 / (double)aSkill.getEffectiveTimeMax());
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 0, 60, 0, 20 , 20-effectivetimeLeft);
		        	break;
		        case COOLTIME:
		        	int cooltimeLeft = (int) ((double)aSkill.getCoolTimeCurrent() * 20.0 / (double)aSkill.getCoolTimeMax());
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 0, 60, 0, 20 , cooltimeLeft);
		        	break;
		        default:
		        	break;
		        }
		        
		        aSkill = skillArray.get(1);
		        switch(aSkill.getSkillState())
		        {
		        case IDLE:
		        	if(!aSkill.hasEnoughMana())
		        	{
					    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
				        drawTexturedModalRect(0, 30, 60, 0, 20 , 20);
		        	}
		        	break;
		        case LOCKED:
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 30, 80, 0, 20 , 20);
			        break;
		        case EFFECTIVE:
		        	int effectivetimeLeft = (int) ((double)aSkill.getEffectiveTimeCurrent() * 20.0 / (double)aSkill.getEffectiveTimeMax());
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 30, 60, 0, 20 , 20-effectivetimeLeft);
		        	break;
		        case COOLTIME:
		        	int cooltimeLeft = (int) ((double)aSkill.getCoolTimeCurrent() * 20.0 / (double)aSkill.getCoolTimeMax());
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 30, 60, 0, 20 , cooltimeLeft);
		        	break;
		        default:
		        	break;
		        }
		        
		        aSkill = skillArray.get(2);
		        switch(aSkill.getSkillState())
		        {
		        case IDLE:
		        	if(!aSkill.hasEnoughMana())
		        	{
					    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
				        drawTexturedModalRect(0, 60, 60, 0, 20 , 20);
		        	}
		        	break;
		        case LOCKED:
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 60, 80, 0, 20 , 20);
			        break;
		        case EFFECTIVE:
		        	int effectivetimeLeft = (int) ((double)aSkill.getEffectiveTimeCurrent() * 20.0 / (double)aSkill.getEffectiveTimeMax());
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 60, 60, 0, 20 , 20-effectivetimeLeft);
		        	break;
		        case COOLTIME:
		        	int cooltimeLeft = (int) ((double)aSkill.getCoolTimeCurrent() * 20.0 / (double)aSkill.getCoolTimeMax());
				    mc.renderEngine.bindTexture(SKILLS_TEXTURE);
			        drawTexturedModalRect(0, 60, 60, 0, 20 , cooltimeLeft);
		        	break;
		        default:
		        	break;
		        }
	    	GL11.glPopMatrix();
    	}
    }
}
