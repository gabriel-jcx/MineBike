package org.ngs.bigx.minecraft.entity.lotom;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityCommand extends EntityLivingBase implements ICommandSender {
	
	public EntityCommand(World world) {
		super(world);
	}
	
	public void sendCommand(String command) {
		//MinecraftServer server = MinecraftServer.getServer();
		
		//if (server == null)
		//	System.out.println("SERVER IS NULL D:");
		
		//ICommandManager manager = server.getCommandManager();
		//manager.executeCommand(this, command);
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C01PacketChatMessage(command));
	}
	
	@Override public ItemStack getHeldItem() { return null; }
	@Override public ItemStack getEquipmentInSlot(int slot) { return null; }
	@Override public void setCurrentItemOrArmor(int slot, ItemStack item) { }
	@Override public ItemStack[] getLastActiveItems() { return null; }
    @Override public boolean isEntityInvulnerable() { return true; }
    @Override public boolean isInvisible() { return true; }

	@Override
	public void addChatMessage(IChatComponent chatComponent) {
	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return true;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
	}

	@Override
	public World getEntityWorld() {
		return worldObj;
	}
	
	public String getCommandSenderName() {
		return "Commander";
	}
}
