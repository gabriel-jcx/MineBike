package org.ngs.bigx.minecraft.quests.custom;

import org.ngs.bigx.minecraft.npcs.custom.ChefGusteau;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderOvercooked;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class OverCookedQuest extends CustomQuestAbstract
{
	public static final int OVERCOOKEDDIMENSIONID  = WorldProviderOvercooked.overcookedDimID;
	
	public OverCookedQuest()
	{
		super();
		
		this.progress = 0;
		this.name = "Overcooked Quest";
		this.completed = false;
		this.started = false;	
		
		register();
	}
	
	@Override
	public void start() 	
	{
		if (isStarted())
			return;
		progress = 0;
		
		//teleport them to the overcooked arena
		//WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.OVERCOOKEDDIMENSIONID);
		QuestTeleporter.teleport(player, OVERCOOKEDDIMENSIONID, -21, 10, 46);
		
		super.start();
	}
	
	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{
		//creative mode
		System.out.println("ticking");		
		if(!event.player.capabilities.isCreativeMode)
			event.player.setGameType(WorldSettings.getGameTypeById(1));
		
		event.player.getPlayerCoordinates();
		
		InventoryPlayer inventory = event.player.inventory;
	    inventory.getStackInSlot(0);
	    inventory.getSizeInventory();
	        for(int i = 0; i < inventory.getSizeInventory(); i++)
	        {
	            ItemStack stack = inventory.getStackInSlot(i);
	        	
	            if (stack != null)
	            {
	            	//gets the unlocalized name of each item in the inventory!
	            	//System.out.println(stack.getItem().getUnlocalizedName());
	
	            	boolean areLettuce = stack.getItem().getUnlocalizedName().equals("item.lettuce");
	            	boolean arePotatoes = stack.getItem().getUnlocalizedName().equals("item.potato");
	            	boolean areSBread = stack.getItem().getUnlocalizedName().equals("item.sandwichbread");
	            	boolean areHBun = stack.getItem().getUnlocalizedName().equals("item.hamburgerbun");
	            	boolean areSandwiches = stack.getItem().getUnlocalizedName().equals("item.sandwich");
	            	boolean areBurgers = stack.getItem().getUnlocalizedName().equals("item.hamburger");
	            	boolean areChickens = stack.getItem().getUnlocalizedName().equals("item.chickenRaw");
	            	boolean areBowls = stack.getItem().getUnlocalizedName().equals("item.bowls");
	            	
	            	if(areLettuce || arePotatoes || areSBread || areHBun || areSandwiches || areBurgers || areChickens || areBowls) 
	            	{
	            		if(stack.stackSize > 1)
	            		{
	            			stack.stackSize = 1;
	            		}
	            	}
	            }
	        }
	}	
	
//	@Override
//	public void onAttackEntityEvent(AttackEntityEvent event)
//	{
//		QuestTeleporter.teleport(player, 0, (int) ChefGusteau.LOCATION.xCoord, (int) ChefGusteau.LOCATION.yCoord, (int) ChefGusteau.LOCATION.zCoord);
//		super.complete();
//		started = false;
//	}
	
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{

	}
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) ChefGusteau.LOCATION.xCoord, (int) ChefGusteau.LOCATION.yCoord, (int) ChefGusteau.LOCATION.zCoord);
		super.complete();
		started = false;
	}
}
