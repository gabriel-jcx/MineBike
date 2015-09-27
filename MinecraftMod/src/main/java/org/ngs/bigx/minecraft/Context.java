package org.ngs.bigx.minecraft;

import java.awt.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.worldgen.structures.Structure;
import org.ngs.bigx.minecraft.worldgen.structures.StructureGarden;
import org.ngs.bigx.minecraft.worldgen.structures.StructureTower;
import org.ngs.bigx.minecraft.worldgen.structures.WorldStructure;
import org.ngs.bigx.minecraft.worldgen.themes.Theme;
import org.ngs.bigx.minecraft.worldgen.themes.ThemeDesert;
import org.ngs.bigx.minecraft.worldgen.themes.ThemeIce;
import org.ngs.bigx.minecraft.worldgen.themes.ThemeNorman;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClientListener;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class Context {
	public BiGXNetClient bigxclient;
	public int heartrate = 80;
	private float speed = 0;
	public float resistance = 0;
	public boolean bump = false;
	public Block block = Blocks.air;
	public int rotation = 0;
	public Main main = null;
	private Quest currentQuest = null;
	private boolean questPopupShown = true;
	private int ID = 0;
	private boolean structuresEnabled = false;
	private boolean questsEnabled = false;
	public Map<EntityPlayerMP,Quest> currentQuests;
	
	public enum Resistance {
		NONE(0),LOW(4),MLOW(7),MID(10),MHIGH(13),HIGH(16);
		
		private float resistance;
		
		Resistance(float res) {
			resistance = res;
		}
		
		public float getResistance() {
			return resistance;
		}
	}
	
	/*
	 * BiGX Related Members
	 */
	public BiGXConnectionStateManagerClass connectionStateManager;
	public static String ipAddress = "localhost";
	public static int port = 1331;
	
	public HashMap<Block,Resistance> resistances = new HashMap<Block,Resistance>();
	
	public List<Block> canGenerateOn = new ArrayList<Block>();
	
	public List<Theme> themes = new ArrayList<Theme>();
	
	public List<Structure> structures = new ArrayList<Structure>();
	
	public Context(Main main) {
		this.main = main;
		
		resistances.put(Blocks.air, Resistance.NONE);
		resistances.put(Blocks.ice, Resistance.LOW);
		resistances.put(Blocks.stone, Resistance.MLOW);
		resistances.put(Blocks.cobblestone, Resistance.MLOW);
		resistances.put(Blocks.grass, Resistance.MID);
		resistances.put(Blocks.dirt, Resistance.MID);
		resistances.put(Blocks.sand, Resistance.MHIGH);
		resistances.put(Blocks.gravel, Resistance.MHIGH);
		resistances.put(Blocks.water, Resistance.HIGH);
		
		canGenerateOn.add(Blocks.grass);
		canGenerateOn.add(Blocks.dirt);
		canGenerateOn.add(Blocks.sand);
		canGenerateOn.add(Blocks.stone);
		canGenerateOn.add(Blocks.stained_hardened_clay);
		canGenerateOn.add(Blocks.hardened_clay);
		
		themes.add(new ThemeDesert());
		themes.add(new ThemeNorman());
		themes.add(new ThemeIce());
		
		structures.add(new StructureTower());
		structures.add(new StructureGarden());
	}
	
	public void initBigX() {
		this.connectionStateManager = new BiGXConnectionStateManagerClass();
		this.bigxclient = new BiGXNetClient(Context.ipAddress, Context.port);
		this.bigxclient.setReceiveListener(new BiGXNetClientListener() {
			
			@Override
			public void onMessageReceive(Event event, BiGXNetPacket packet) {
				BiGXPacketHandler.Handle(bigxclient, packet);
			}
			
			@Override
			public void onConnectedMessageReceive(Event event) {
				System.out.println("This MC is connected to BiGX Game Controller");
			}
		});
	}
	
	public Theme getTheme(BiomeGenBase biome,Random random) {
		int i = random.nextInt(themes.size());
		int j = 0;
		int size = themes.size();
		while(j<size) {
			Theme th = themes.get(i);
			if ( th.getBiomes().contains(biome) ) {
				return th;
			}
			i++;
			if (i>=themes.size()) {
				i-=themes.size();
			}
			j++;
		}
		return null;
	}
	
	public Structure getStructure(Theme theme, Random random) {
		int i = random.nextInt(structures.size());
		int j = 0;
		int size = structures.size();
		while(j<size) {
			Structure struct = structures.get(i);
			List<Class> themes = struct.getBannedThemes();
			boolean can_use = true;
			//Again this is probably the wrong way to do this! needs serious consideration tomorrow
			for (Object cl:themes) {
				if (theme.getClass().equals((Class) cl)) {
					can_use = false;
					break;
				}
			}
			if (can_use) {
				return struct;
			}
			i++;
			if (i>=themes.size()) {
				i-=themes.size();
			}
			j++;
		}
		return null;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setQuest(Quest quest) {
		this.currentQuest = quest;
		questPopupShown = false;
	}
	
	public Quest getQuest() {
		return currentQuest;
	}
	
	public Boolean hasQuestPopupShown() {
		return questPopupShown;
	}
	
	public void showQuestPopup() {
		questPopupShown = true;
	}
	
	
	public Structure getStructure(String name) {
		for (Structure structure:structures) {
			if (structure.getName().equals(name)) {
				return structure;
			}
		}
		return null;
	}
	
	public Theme getTheme(String name) {
		for (Theme theme:themes) {
			if (theme.getName().equals(name)) {
				return theme;
			}
		}
		return null;
	}

	public WorldStructure getWorldStructure(Integer valueOf) {
		return null;
	}
	
	public int getID() {
		ID++;
		return ID;
	}
	
	public boolean checkStructuresEnabled() {
		return structuresEnabled;
	}
	
	public boolean checkQuestsEnabled() {
		if (!checkStructuresEnabled()) {
			return false;
		}
		return questsEnabled;
	}
}
