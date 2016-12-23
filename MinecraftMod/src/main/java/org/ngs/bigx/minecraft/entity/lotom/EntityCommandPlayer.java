package org.ngs.bigx.minecraft.entity.lotom;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

// WARNING: Do not instantiate EntityCommandPlayer solely!
// Use EntityCommandPlayerFactory to safely create our EntityCommandPlayer
// and avoid ID conflicts.

public class EntityCommandPlayer extends EntityPlayerMP {
	
	public EntityCommandPlayer(WorldServer world, GameProfile profile) {
		super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, profile, new ItemInWorldManager(world));
	}
	
	// Important overrides (inspired by net.minecraftforge.common.util.FakePlayer)
	@Override public boolean canCommandSenderUseCommand(int i, String s) { return true; }
    //@Override public ChunkCoordinates getPlayerCoordinates() { return new ChunkCoordinates(0,0,0); }
    @Override public boolean isEntityInvulnerable(){ return true; }
    @Override public boolean canAttackPlayer(EntityPlayer player){ return false; }
    @Override public void addStat(StatBase par1StatBase, int par2){}
    @Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z){}
    @Override public boolean isInvisible() { return true; }
    
	public void SendCommand(String command) {
		//addChatMessage(new ChatComponentText(command));
		//addChatComponentMessage(new ChatComponentText(command));
		
		//GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		//chat.printChatMessageWithOptionalDeletion(new ChatComponentText(command), 1);
	}
}
