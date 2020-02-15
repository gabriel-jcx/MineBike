package org.ngs.bigx.minecraft.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.context.BigxClientContext;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class GuiLeaderBoard extends GuiScreen {	
	private Minecraft mc;

	private ResourceLocation LEADERBOARD_TEXTURE = new ResourceLocation(BiGX.TEXTURE_PREFIX, "textures/GUI/leaderboard.png");
	
	private BigxClientContext context;
	
	private GuiTextField textField;
	
	private static ArrayList<LeaderboardRow> leaderboardRows = new ArrayList<LeaderboardRow>();
	
	private static boolean isShown = false;
	
	public GuiLeaderBoard(Minecraft mc) {
		super();
		this.mc = mc;
		fontRendererObj = mc.fontRenderer;
	}
	
	public GuiLeaderBoard(BigxClientContext c, Minecraft mc) {
		this(mc);
		context = c;
		fontRendererObj = mc.fontRenderer;
	}
	
	public static ArrayList<LeaderboardRow> getLeaderboardRows() {
		return leaderboardRows;
	}
	
	private static File getLeaderboardFile() {
		File leaderboardFile = new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), "Leaderboard.json");
		if (!leaderboardFile.exists()) {
			try {
				leaderboardFile.createNewFile();
			} catch (IOException e) {
				System.out.println(new File(new File(Minecraft.getMinecraft().mcDataDir, "saves"), "Leaderboard.json"));
				e.printStackTrace();
			}
		}
		return leaderboardFile;
	}
	
	private static Leaderboard readLeaderboard() throws IOException {
		File leaderboardFile = getLeaderboardFile();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(leaderboardFile));
		Leaderboard leaderboard = gson.fromJson(reader, Leaderboard.class);
		reader.close();
		return leaderboard;
	}
	
	public static boolean writeToLeaderboard(LeaderboardRow row) throws IOException
	{
		File leaderboardFile = getLeaderboardFile();
		Leaderboard leaderboard = readLeaderboard();
		ArrayList<LeaderboardRow> leaderboardRowToBeRemoved = new ArrayList<LeaderboardRow>();
		
		if(leaderboard == null)
		{
			leaderboard = new Leaderboard();
		}
		
		leaderboard.leaderboardRows.add(row);
		
		Collections.sort(leaderboard.leaderboardRows);
		
		for(int i = 0; i < leaderboard.leaderboardRows.size(); i++)
		{
			LeaderboardRow leaderboardRow = leaderboard.leaderboardRows.get(i);
			
			leaderboardRow.rank = "" + (i + 1);
			
			if(i >= 10)
			{
				leaderboardRowToBeRemoved.add(leaderboardRow);
			}
		}
		
		for(LeaderboardRow leaderboardRow : leaderboardRowToBeRemoved)
		{
			leaderboard.leaderboardRows.remove(leaderboardRow);
		}
		
		// Write it back
		FileOutputStream fOut = new FileOutputStream(leaderboardFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        Gson gson = new Gson();
        myOutWriter.append(gson.toJson(leaderboard));
        myOutWriter.close();
        fOut.close();
		
		return leaderboard.leaderboardRows.contains(row);
	}
	
	public static void refreshLeaderBoard() throws IOException
	{
		Leaderboard leaderboard = readLeaderboard();
		
		if(leaderboard == null)
		{
			leaderboard = new Leaderboard();
		}
		
		Collections.sort(leaderboard.leaderboardRows);
		
		for(int i = 0; i < leaderboard.leaderboardRows.size(); i++)
		{
			LeaderboardRow leaderboardRow = leaderboard.leaderboardRows.get(i);
			leaderboardRow.rank = "" + (i + 1);
		}
		
		leaderboardRows = new ArrayList<LeaderboardRow>();
		LeaderboardRow row = new LeaderboardRow();
		row.rank = "Rank";
		row.name = "Name";
		row.level = "Level";
		row.time_elapsed = "Time";
		row.combo = "Best Combo";
		leaderboardRows.add(row);
		
		for(LeaderboardRow leaderboardRow : leaderboard.leaderboardRows)
		{
			leaderboardRows.add(leaderboardRow);
		}
	}
	
	private static String getRowRank(LeaderboardRow row) {
		try {
			refreshLeaderBoard();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		{
			try {
				refreshLeaderBoard();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		GuiLeaderBoard.isShown = isShown;
	}
	
	public static void addLeaderBoardRows(ArrayList<LeaderboardRow> rows)
	{
		try {
			refreshLeaderBoard();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (rows == null)
		{
			return;
		}
		
		for (LeaderboardRow row : rows)
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
	    
    	if( !GuiLeaderBoard.isShown )
    	{
    		return;
    	}
    	
    	if (textField == null)
    	{
    		textField = new GuiTextField(mc.fontRenderer, 0, 0, 0, 0);
    	}
    	
    	if (mc.player != null) {
	    	EntityPlayer p = mc.player;
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
	        		String bestcombo = leaderboardRows.get(i).combo;
	        		String time_elapsed = leaderboardRows.get(i).time_elapsed;
	
		    		fontRendererObj.drawString(rank, -120, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(name, -90, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(level, 5, 32 + i*14, 0xFFFFFF);
//		    		fontRendererObj.drawString(stat_1, 40, 32 + i*14, 0xFFFFFF);
		    		fontRendererObj.drawString(bestcombo, 30, 32 + i*42, 0xFFFFFF);
		    		fontRendererObj.drawString(time_elapsed, 75, 32 + i*14, 0xFFFFFF);
	        	}
        	
        	GL11.glPopMatrix();
    	}
    }
}