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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class john extends CustomNPCAbstract 
{
	public static final String NAME = "john";
	public static final Vec3d LOCATION = new Vec3d(116, 70, 230);//new Vec3d
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
	public void onInteraction(EntityPlayer player, PlayerInteractEvent.EntityInteract event) 
	{
		GuiMessageWindow.showMessage("Hello, I understand \nyou come for the sword.");
		GuiMessageWindow.showMessage("Here it is.\nUse it wisely.");
		
		//World world = Minecraft.getMinecraft().player.world;
		
		//world = MinecraftServer.getServer()
		World world = Minecraft.getMinecraft().player.getEntityWorld();
		
		
		if (!world.isRemote && !swordGiven)
		{
			swordGiven = true;
			Item sword = Item.getItemById(276);
			
			ItemStack is = new ItemStack(sword, 1);
			is.setStackDisplayName("Excalibur");
			
			EntityItem item = new EntityItem(world, LOCATION.x, LOCATION.y, LOCATION.z, is);
			world.spawnEntity(item);

		}
	}

}
