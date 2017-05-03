package org.ngs.bigx.minecraft.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BigxClientContext;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import betterquesting.utils.JsonIO;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiLeaderBoard extends GuiScreen {	
	private Minecraft mc;

	private ResourceLocation LEADERBOARD_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/leaderboard.png");
	
	private BigxClientContext context;
	
	private static ArrayList<LeaderboardRow> leaderboardRows = new ArrayList<LeaderboardRow>();
	
	private static boolean isShown = false;
	
	public GuiLeaderBoard(Minecraft mc) {
		super();
		//refreshLeaderBoard();
		this.mc = mc;
	}
	
	public GuiLeaderBoard(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
	}
	
	public static void writeToLeaderboard(LeaderboardRow row) {
		
		File leaderboardFile = new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), "Leaderboard.json");
		if (!leaderboardFile.exists()) {
			try {
				leaderboardFile.createNewFile();
			} catch (IOException e) {
				System.out.println(new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), "Leaderboard.json"));
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		row.rank = getRowRank(row);
		if (row.rank.equals("RANK") || Integer.parseInt(row.rank) > 10 || Integer.parseInt(row.rank) < 1) {
			return;
		}
		
		JsonObject j = new JsonObject();
		j = JsonIO.ReadFromFile(leaderboardFile);
		if(j != null)
		{
			if (j.get(row.rank) != null) {
				// Shift every leaderboard item >= rank (lesser scores) down one to make room for the new row
				JsonObject newJ = new JsonObject();
				
				for(int i=9; i>=Integer.parseInt(row.rank); i--)
				{
					JsonElement tempJsonElement = j.get(""+i);
					
					if(tempJsonElement != null)
					{
						JsonObject tempRow = tempJsonElement.getAsJsonObject();
						tempRow.addProperty("rank", Integer.toString(i + 1));
						newJ.add(""+(i+1), tempRow);
					}
				}
				
				j = newJ;

				JsonObject jsonRow = new JsonObject();
				jsonRow.addProperty("rank", row.rank);
				jsonRow.addProperty("name", row.name);
				jsonRow.addProperty("level", row.level);
//				jsonRow.addProperty("stat_1", row.stat_1);
				jsonRow.addProperty("time_elapsed", row.time_elapsed);
				j.add(row.rank, jsonRow);
			}
		}
		else{
			j = new JsonObject();

			JsonObject jsonRow = new JsonObject();
			jsonRow.addProperty("rank", row.rank);
			jsonRow.addProperty("name", row.name);
			jsonRow.addProperty("level", row.level);
//			jsonRow.addProperty("stat_1", row.stat_1);
			jsonRow.addProperty("time_elapsed", row.time_elapsed);
			j.add(row.rank, jsonRow);
		}
		
		JsonIO.WriteToFile(leaderboardFile, j);
	}
	
	public static void refreshLeaderBoard()
	{
		File leaderboardFile = new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), "Leaderboard.json");
		if (!leaderboardFile.exists()) {
			try {
				leaderboardFile.createNewFile();
			} catch (IOException e) {
				System.out.println(new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), "Leaderboard.json"));
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			JsonObject j = new JsonObject();
			j = JsonIO.ReadFromFile(leaderboardFile);
			
			if (j != null) {
				
				boolean foundFirst = false;
				int place = 1;
				for (int i = 0; i < 10; ++i) {
					if (j.get(""+(i+1)) != null) {
						if (!foundFirst) {
							foundFirst = true;
						} else {
							++place;
						}
						JsonObject jobj = j.getAsJsonObject(""+(i+1));
						jobj.addProperty("rank", Integer.toString(place));
						j.remove(""+(i+1));
						j.add(Integer.toString(place), jobj);
					}
				}
				
				leaderboardRows = new ArrayList<LeaderboardRow>();
				LeaderboardRow row = new LeaderboardRow();
				row.rank = "Rank";
				row.name = "Name";
				row.level = "Level";
//				row.stat_1 = "Blocks";
				row.time_elapsed = "Time";
				leaderboardRows.add(row);
				
				for(int i=0; i< 10; i++)
				{
					if(j.get(""+(i+1)) != null)
					{
						JsonObject entries = j.get(""+(i+1)).getAsJsonObject();
						row = new LeaderboardRow();
						row.rank = entries.get("rank").getAsString();
						row.name = entries.get("name").getAsString();
						if(row.name.length() > 15)
						{
							row.name = row.name.substring(0, 12) + "...";
						}
						row.level = entries.get("level").getAsString();
//						row.stat_1 = entries.get("stat_1").getAsString();
						row.time_elapsed = entries.get("time_elapsed").getAsString();
						leaderboardRows.add(row);
					}
				}
			}
		}
	}
	
	private static String getRowRank(LeaderboardRow row) {
		refreshLeaderBoard();
		for (LeaderboardRow r : leaderboardRows) {
			System.out.println(r.time_elapsed + " " + row.time_elapsed);
			if(!r.time_elapsed.equals("Time"))
				if (Double.parseDouble(r.time_elapsed) <= Double.parseDouble(row.time_elapsed))
					return r.rank;
		}
		return "1";
	}
	
	public static void addLeaderBoardRow(LeaderboardRow row)
	{
		leaderboardRows.add(row);
	}
	
	public static void showLeaderBoard(boolean isShown)
	{
		if (isShown)
			refreshLeaderBoard();
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
        	
	        	for(int i=0; i<leaderboardRows.size(); i++)
	        	{
	        		String rank = leaderboardRows.get(i).rank;
	        		String name = leaderboardRows.get(i).name;
	        		String level = leaderboardRows.get(i).level;
//	        		String stat_1 = leaderboardRows.get(i).stat_1;
	        		String time_elapsed = leaderboardRows.get(i).time_elapsed;
	
		        	fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		    		fontRendererObj.drawString(rank, -120, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(name, -90, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(level, 5, 32 + i*14, 0xFFFFFF);
//		    		fontRendererObj.drawString(stat_1, 40, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(time_elapsed, 75, 32 + i*14, 0xFFFFFF);
	        	}
        	
        	GL11.glPopMatrix();
    	}
    }
}