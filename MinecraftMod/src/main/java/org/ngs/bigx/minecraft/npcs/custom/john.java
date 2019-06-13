package org.ngs.bigx.minecraft.npcs.custom;

import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.quests.custom.CustomQuest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class john extends CustomNPCAbstract 
{
	public static final String NAME = "john";
	public static final Vec3 LOCATION = Vec3.createVectorHelper(116, 70, 230);
	public static final String TEXTURE = "customnpcs:textures/entity/humanmale/GansterSteve.png";
	
	private boolean swordGiven;
	
	public john()
	{
		//important that these are set in the constructor
		name = NAME;
		location = LOCATION;
		texture = TEXTURE;
		
		swordGiven = false;
	}

	
	@Override
	public void onInteraction(EntityPlayer player, EntityInteractEvent event) 
	{
		GuiMessageWindow.showMessage("Hello, I understand \nyou come for the sword.");
		GuiMessageWindow.showMessage("Here it is.\nUse it wisely.");
		
		World world = Minecraft.getMinecraft().thePlayer.worldObj;
		
		world = MinecraftServer.getServer().getEntityWorld();
		
		
		if (!world.isRemote && !swordGiven)
		{
			swordGiven = true;
			Item sword = Item.getItemById(276);
			
			ItemStack is = new ItemStack(sword, 1);
			is.setStackDisplayName("Excalibur");
			
			EntityItem item = new EntityItem(world, LOCATION.xCoord, LOCATION.yCoord, LOCATION.zCoord, is);
			world.spawnEntityInWorld(item);

		}
	}

}
