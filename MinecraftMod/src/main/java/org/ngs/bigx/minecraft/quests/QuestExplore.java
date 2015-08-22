package org.ngs.bigx.minecraft.quests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ngs.bigx.minecraft.Main;
import org.ngs.bigx.minecraft.worldgen.structures.WorldStructure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class QuestExplore extends Quest {

	private WorldStructure structure;
	
	public QuestExplore(boolean completed) {
		super(completed);
	}

	@Override
	public String getTypeName() {
		return StatCollector.translateToLocal("quest.type.explore");
	}
	
	@Override
	public String getType() {
		return "explore";
	}
	
	@Override
	public String getHint(EntityPlayer player) {
		if (player.getEntityWorld()==structure.getWorld()) {
			return "X: "+(player.posX-structure.getX())+" Y: "+(player.posY-structure.getY())+" Z: "+(player.posZ-structure.getZ());
		}
		return "";
	}
	
	@Override
	public void setProperties(Map<String,String> arguments) {
		structure = Main.instance().context.getWorldStructure(Integer.valueOf(arguments.get("structure")));
	}
	
	@Override
	public Map<String,String> getProperties() {
		Map<String,String> arguments = new HashMap();
		arguments.put("structure",String.valueOf(structure.getID()));
		return arguments;
	}

	@Override
	public String getName() {
		return structure.getName();
	}
}