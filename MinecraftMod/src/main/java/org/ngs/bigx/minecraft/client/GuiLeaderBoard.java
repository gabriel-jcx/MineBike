package org.ngs.bigx.minecraft.client;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.Context;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestRunFromMummy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiLeaderBoard extends GuiScreen {	
	private Minecraft mc;

	private ResourceLocation LEADERBOARD_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/leaderboard.png");
	
	private Context context;
	
	private static ArrayList<LeaderboardRow> leaderboradRows = new ArrayList<LeaderboardRow>();
	
	private static boolean isShown = false;
	
	public GuiLeaderBoard(Minecraft mc) {
		super();
		refreshLeaderBoard();
		this.mc = mc;
	}
	
	public GuiLeaderBoard(Context c,Minecraft mc) {
		this(mc);
		context = c;
	}
	
	public static void refreshLeaderBoard()
	{
		leaderboradRows = new ArrayList<LeaderboardRow>();
		
		LeaderboardRow row = new LeaderboardRow();
		row.rank = "RANK";
		row.name = "NAME";
		row.level = "LEVEL";
		row.stat_1 = "STAT";
		row.totalscore = "SCORE";
		
		leaderboradRows.add(row);
		
		row = new LeaderboardRow();
		row.rank = "1";
		row.name = "John Morrison";
		row.level = "3";
		row.stat_1 = "23";
		row.totalscore = "104297";
		
		leaderboradRows.add(row);
		
		row = new LeaderboardRow();
		row.rank = "2";
		row.name = "Jamison Fawkes";
		row.level = "2";
		row.stat_1 = "21";
		row.totalscore = "71921";
		
		leaderboradRows.add(row);
		
		row = new LeaderboardRow();
		row.rank = "3";
		row.name = "Satya Vaswani";
		row.level = "1";
		row.stat_1 = "12";
		row.totalscore = "685";
		
		leaderboradRows.add(row);
	}
	
	public static void addLeaderBoardRow(LeaderboardRow row)
	{
		leaderboradRows.add(row);
	}
	
	public static void showLeaderBoard(boolean isShown)
	{
		GuiLeaderBoard.isShown = isShown;
	}
	
	public static void addLeaderBoardRows(ArrayList<LeaderboardRow> rows)
	{
		refreshLeaderBoard();
		
		if(rows == null)
		{
			return;
		}
		
		for(LeaderboardRow row:rows)
		{
			addLeaderBoardRow(row);
		}
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

	@SubscribeEvent
    public void eventHandler(RenderGameOverlayEvent event) {
	    if(event.isCancelable() || event.type != event.type.TEXT || !context.modEnabled)
	    {      
	      return;
	    }
	    
    	FontRenderer fontRendererObj;
    	
    	if( !GuiLeaderBoard.isShown )
    	{
    		return;
    	}

    	if (mc.thePlayer != null) {
	    	EntityPlayer p = mc.thePlayer;
		    ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
	    	int mcWidth = sr.getScaledWidth();
	    	int mcHeight = sr.getScaledHeight();
    		
	    	GL11.glPushMatrix();
	    	
			    GL11.glTranslatef(mcWidth/2, 18f, 0); 
			    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			    GL11.glEnable(GL11.GL_BLEND);
			    mc.renderEngine.bindTexture(LEADERBOARD_TEXTURE);
		        drawTexturedModalRect(-128, 0, 0, 0, 256 , 150);
        	
	        	for(int i=0; i<leaderboradRows.size(); i++)
	        	{
	        		String rank = leaderboradRows.get(i).rank;
	        		String name = leaderboradRows.get(i).name;
	        		String level = leaderboradRows.get(i).level;
	        		String stat_1 = leaderboradRows.get(i).stat_1;
	        		String totalscore = leaderboradRows.get(i).totalscore;
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(rank, -120, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(name, -90, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(level, 5, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(stat_1, 45, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(totalscore, 75, 32 + i*14, 0xFFFFFF);
	        	}
        	
        	GL11.glPopMatrix();
    	}
    }
}