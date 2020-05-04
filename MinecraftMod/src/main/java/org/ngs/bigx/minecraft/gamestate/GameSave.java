package org.ngs.bigx.minecraft.gamestate;

import java.util.ArrayList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.skills.SkillManager;

@SideOnly(Side.CLIENT)
public class GameSave {
	private long timestamp = 0;
	
	// PLAYER STATE

	private ArrayList<String> itemOnItemStack;		// Items by item id

	// LOCATION (SAVED LOCATION IN GAME)
	private int posX;
	private int posY;
	private int posZ;
	private int posDim;
	
	// UNLOCKED QUEST LIST
	private ArrayList<String> accomplishedQuestId;
	
	// AVAILABLE SKILLS
	private SkillManager skillManager;
	
	public GameSave()
	{
		this.itemOnItemStack = new ArrayList<String>();
		this.posDim = 0;
		this.posX = 0;
		this.posY = 0;
		this.posZ = 0;
		this.accomplishedQuestId = new ArrayList<String>();
		this.skillManager = new SkillManager(ClientEventHandler.pedalingCombo);
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	public void setSkillManager(SkillManager skillManager) {
		this.skillManager = skillManager;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<String> getItemOnItemStack() {
		return itemOnItemStack;
	}

	public void setItemOnItemStack(ArrayList<String> itemOnItemStack) {
		this.itemOnItemStack = itemOnItemStack;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getPosZ() {
		return posZ;
	}

	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

	public int getPosDim() {
		return posDim;
	}

	public void setPosDim(int posDim) {
		this.posDim = posDim;
	}

	public ArrayList<String> getAccomplishedQuestId() {
		return accomplishedQuestId;
	}

	public void setAccomplishedQuestId(ArrayList<String> accomplishedQuestId) {
		this.accomplishedQuestId = accomplishedQuestId;
	}
}
