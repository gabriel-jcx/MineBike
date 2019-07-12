package org.ngs.bigx.minecraft.quests.custom;

import java.time.Clock;
import java.util.ArrayList;

import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.items.OvercookedHamburger;
import org.ngs.bigx.minecraft.items.OvercookedHamburgerbun;
import org.ngs.bigx.minecraft.items.OvercookedLettuce;
import org.ngs.bigx.minecraft.items.OvercookedSandwichbread;
import org.ngs.bigx.minecraft.npcs.custom.ChefGusteau;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderOvercooked;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class OverCookedQuest extends CustomQuestAbstract
{
	public static final int OVERCOOKEDDIMENSIONID  = WorldProviderOvercooked.overcookedDimID;
		
	//TODO: change name of ingredients to mention locations
	private ArrayList<ChunkCoordinates> ingredients; //holds ingredient plate chunkcoordinates
	private ArrayList<Recipe> recipes; //holds all different recipes (10)
	private ArrayList<Recipe> orderList; //holds the orders being displayed to the gui, removes orders when completed
	
	private final int ORDERCOMPLETE = 20; //points given for each completed order
//	private final int ORDERMISSED = -10; //points lost for each failed order (order expired)
	private int score; //holds total point values. if greater than this.progress, change progress to score
	private int ticks; //stores the number of ticks passed once players enter the kitchen
	private boolean countTicks; // when countTicks is true, begin incrementing ticks
	
	private Clock clock;
	long lastTime;
	
	
	public OverCookedQuest()
	{
		super();
		
		this.progress = 0;
		this.name = "Overcooked Quest";
		this.completed = false;
		this.started = false;	
		
		ingredients = new ArrayList<ChunkCoordinates>();
		setIngredientsList();
		
		recipes = new ArrayList<Recipe>();
		setRecipesList();
		
		orderList = new ArrayList<Recipe>();
		
		score = 0;
		ticks = 0;
		countTicks = false;
		
		clock = Clock.systemDefaultZone();
		lastTime = clock.millis();
		
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
		QuestTeleporter.teleport(player, OVERCOOKEDDIMENSIONID, 134, 10, 55);
		
		HudRectangle rectangle = new HudRectangle(100, 100, 200, 200, 0x53a80888);
		HudManager.registerRectangle(rectangle);
		
		lastTime = clock.millis();
		
		super.start();
	}
	
	//TODO: change naming to mention locations
	private void setIngredientsList()
	{
		ingredients.add(new ChunkCoordinates(-2,10,70)); // 0=bowl
		ingredients.add(new ChunkCoordinates(-1,10,28)); // 1=chicken
		ingredients.add(new ChunkCoordinates(-21,10,16)); // 2=bread
		ingredients.add(new ChunkCoordinates(-52,10,48)); // 3=lettuce
		ingredients.add(new ChunkCoordinates(-52,10,76)); // 4=bun
		ingredients.add(new ChunkCoordinates(-18,10,81)); // 5=potato
	}
	
	private void setRecipesList()
	{
		Item sbread = Item.getItemById(4846);
		Item hbun = Item.getItemById(4847);
		Item chicken = Item.getItemById(366);
		Item lettuce = Item.getItemById(4850);
		Item potato = Item.getItemById(392);
		Item bowl =Item.getItemById(281);
		
		//all ingredients + bread
		Item[] all = new Item[] {chicken, lettuce, potato, bowl};
		recipes.add(new Recipe(sbread , all));
		recipes.add(new Recipe(hbun , all)); 
		
		//no chicken, everything else
		Item[] veg = new Item[] {lettuce, potato, bowl};
		recipes.add(new Recipe(sbread, veg));
		recipes.add(new Recipe(hbun, veg));
		
		//no lettuce, everything else
		Item[] nogreen = new Item[] {chicken, lettuce, bowl};
		recipes.add(new Recipe(sbread, nogreen));
		recipes.add(new Recipe(hbun, nogreen));
		
		//no potato, everything else
		Item[] nopot = new Item[] {chicken, lettuce, bowl};
		recipes.add(new Recipe(sbread, nopot));
		recipes.add(new Recipe(hbun, nopot));
		
		//just chicken~
		Item[] plain = new Item[] {chicken, bowl};
		recipes.add(new Recipe(sbread, plain));
		recipes.add(new Recipe(hbun, plain));
	}
	
	
	
	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{
		if(countTicks)
			ticks++;
		
		//creative mode
		//System.out.println("ticking");		
		if(!event.player.capabilities.isCreativeMode)
			event.player.setGameType(WorldSettings.getGameTypeById(1));

		teleport(event);
		
		useFurnace(event);
		maxOneIngredient(event);
		giveIngredient(event);
		
		if(ticks % 20 == 0)
		{
			useCraftingTable(event);
			turnIn(event);
		}
		if(clock.millis()>=lastTime + randomTime())
		{
			generateOrder();
			lastTime = clock.millis();
			System.out.println("new order generated!!!!!!!!!!!");
		}
	}
	
	private int randomTime()
	{
		return ((int)(Math.random()*(25-17)) + 17) * 1000;
	}
	
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		QuestTeleporter.teleport(player, 0, (int) ChefGusteau.LOCATION.xCoord, (int) ChefGusteau.LOCATION.yCoord, (int) ChefGusteau.LOCATION.zCoord);
		super.complete();
		started = false;
	}
	
	//teleports the player from the instruction room to the kitchen to start the game
	private void teleport(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();		
		if((coord.posX >= 126 && coord.posX <= 128) && (coord.posZ >= 54 && coord.posZ <= 56))
		{
			QuestTeleporter.teleport(player, OVERCOOKEDDIMENSIONID, -21, 10, 46);
			countTicks = true;
		}
	}
	
	//limits player to a maximum of one of each ingredient in inventory at a time
	//doesn't touch other items in player's inventory; only ingredients
	private void maxOneIngredient(TickEvent.PlayerTickEvent event)
	{
		InventoryPlayer inventory = event.player.inventory;
		
		//goes through player's inventory
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
        	
            //if there's something in the stack...
            if (stack != null)
            {
            	//checks if ingredients are in player's inventory
            	boolean areLettuce = stack.getItem().getUnlocalizedName().equals("item.lettuce");
            	boolean arePotatoes = stack.getItem().getUnlocalizedName().equals("item.potato");
            	boolean areSBread = stack.getItem().getUnlocalizedName().equals("item.sandwichbread");
            	boolean areHBun = stack.getItem().getUnlocalizedName().equals("item.hamburgerbun");
            	boolean areSandwiches = stack.getItem().getUnlocalizedName().equals("item.sandwich");
            	boolean areBurgers = stack.getItem().getUnlocalizedName().equals("item.hamburger");
            	boolean areChickens = stack.getItem().getUnlocalizedName().equals("item.chickenRaw");
            	boolean areBowls = stack.getItem().getUnlocalizedName().equals("item.bowl");
            	boolean areCooked = stack.getItem().getUnlocalizedName().equals("item.chickenCooked");
            	
            	//if there is more than one ingredient in player inventory, reduce it to one
            	if(areLettuce || arePotatoes || areSBread || areHBun || areSandwiches || areBurgers || areChickens || areBowls || areCooked) 
            		if(stack.stackSize > 1)
               			stack.stackSize = 1;
            }
        }
	}
	
	//if the player is on an ingredient space, place an item stack in inventory with the ingredient
	//if player has the ingredient in inventory, does not add anything
	private void giveIngredient(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();
		if(returnIngredientType(coord) >= 0)
		{
			NewItemStack ingredient = createIngredientStack(returnIngredientType(coord));
			ItemStack stack = ingredient.getStack();
			String name = ingredient.getName();
			
			InventoryPlayer inventory = event.player.inventory;
			
			for(int i = 0; i < inventory.getSizeInventory(); i++)
	        {
	            ItemStack temp = inventory.getStackInSlot(i);
	            //if there's something in the stack...
	            if (temp != null)
	            {	            	
	            	//if there's already a stack with the item, do nothing
	            	if(temp.getItem().getUnlocalizedName().equals("item." + name)) 
	            		return;
	            }
	        }
			event.player.inventory.addItemStackToInventory(stack);
		 }
	}
	
	//returns the int that corresponds to the ingredient in the ingredients arraylist
	//if not on an ingredient space, return -1
	private int returnIngredientType(ChunkCoordinates coord)
	{
		for(int i = 0; i<ingredients.size(); i++)
		{
			if(onIngredient(ingredients.get(i), coord))
				return i;
		}
		return -1;
	}
	
	//returns true if player is on the ingredient passed in as item
	private boolean onIngredient(ChunkCoordinates item, ChunkCoordinates coord)
	{
		int xMax = item.posX;
		int xMin = xMax - 3;
		
		int zMax = item.posZ;
		int zMin = zMax - 3;
	
		return (coord.posX >= xMin && coord.posX <= xMax) && (coord.posZ >= zMin && coord.posZ <= zMax);
	}
	
	//returns a NewItemStack with an ItemStack containing one ingredient and its name as a string
	private NewItemStack createIngredientStack(int num)
	{
		Item item = Item.getItemById(256); // defaults item to a shovel
		String name = "";
		switch(num)
		{
			case 0:
				item = Item.getItemById(281); //bowl
				name = "bowl";
				break;
			case 1:
				item = Item.getItemById(365); //chicken
				name = "chickenRaw";
				break;
			case 2:
				item = Item.getItemById(4846); //sandwich bread
				name = "sandwichbread";
				break;
			case 3:
				item = Item.getItemById(4850); //lettuce
				name = "lettuce";
				break;
			case 4:
				item = Item.getItemById(4847); //bun
				name = "hamburgerbun";
				break;
			case 5:
				item = Item.getItemById(392); //potato
				name = "potato";
				break;				
		}
		return new NewItemStack(new ItemStack(item, 1), name);
	}
	
	//if on furnace plates, remove a raw chicken and add a cooked chicken if possible
	private void useFurnace(TickEvent.PlayerTickEvent event)
	{
		if(onFurnace(event))
		{
			InventoryPlayer inventory = event.player.inventory;
			
			for(int i = 0; i < inventory.getSizeInventory(); i++)
			{
				ItemStack stack = inventory.getStackInSlot(i);
				if(stack != null)
				{
					if(stack.getItem().getUnlocalizedName().equals("item.chickenRaw"))
					{
						event.player.inventory.consumeInventoryItem(Item.getItemById(365));
						ItemStack cooked = new ItemStack(Item.getItemById(366)); //cooked  chicken
						event.player.inventory.addItemStackToInventory(cooked);
					}
				}
			}
		}
	}

	//returns true of player is on furnace plate
	private boolean onFurnace(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();
		return (coord.posX >= -54 && coord.posX <= -51) && (coord.posZ >= 14 && coord.posZ <= 23);
	}

	//if on crafting table plates, use ingredients in player's inventory to craft a sandwich or burger if possible
	private void useCraftingTable(TickEvent.PlayerTickEvent event)    //HELP HERE
	{
		if(onCrafting(event)) //if on crafting table
		{
			for(int i = 0; i < orderList.size(); i++) //for the size of the order list
			{
				if(orderList.get(i).canMake(event)) //for eat order in the order list, if it can be made with the inventory
				{
					giveAndRemove(event, i); //give order and remove the ingredients
					return;
				}
			}
			//display gui indicating that nothing can be made with ingredients in inventory
		}
	}
	
	//give player the order and remove the ingredients from player inventory
	private void giveAndRemove(TickEvent.PlayerTickEvent event, int i)
	{
		Item[] main = orderList.get(i).getInsides();
		InventoryPlayer inventory = event.player.inventory;
		
		for(int k = 0; k < inventory.getSizeInventory(); k++)
		{
			ItemStack stack = inventory.getStackInSlot(k);
			if(stack != null)
			{
				for(int j = 0; j < main.length; j++)
				{
					if(stack.getItem().getUnlocalizedName().equals(main[j].getUnlocalizedName()) || 
							stack.getItem().getUnlocalizedName().equals(orderList.get(i).getType().getUnlocalizedName()))
						event.player.inventory.consumeInventoryItem(stack.getItem());
				}
            		
			}
		}
		if(orderList.get(i).getType().getUnlocalizedName().equals("item.sandwichbread"))
			event.player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4849),1));
		else
			event.player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4848),1)); 
	}
	
	//returns true if player is on crafting table plates
	private boolean onCrafting(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();
		return (coord.posX >= -5 && coord.posX <= -2) && ((coord.posZ >= 75 && coord.posZ <= 78) || 
				(coord.posZ >= 71 && coord.posZ <= 74));
	}
	
	//randomly chooses a recipe to put as the next order, adds it to order list
	private void generateOrder()
	{
		int temp = (int)(Math.random()*10);
		if(orderList.size()<5)
			orderList.add(recipes.get(temp));
	}
	
	//expire or just keep stacking and let them go as fast as they can without the pressure?
//	public boolean orderExpire(int start, int current) //HELP HERE
//	{
//		
//	}
	
	//if on turn in plates, find the correct order on the order list
	//removes the order from inventory and order list, award points
	public void turnIn(TickEvent.PlayerTickEvent event) //CAN WE DO IDS HERE
	{
		if(onTurnIn(event))
		{
			InventoryPlayer inventory = event.player.inventory;
			ItemStack order = null;
			
			for(int k = 0; k < inventory.getSizeInventory(); k++)
			{
				ItemStack stack = inventory.getStackInSlot(k);
				if(stack!=null)
					System.out.println(stack.getItem().getUnlocalizedName());
				if(order == null && stack != null)
				{
					if(stack.getItem().getUnlocalizedName().equals("item.hamburger") || 
							stack.getItem().getUnlocalizedName().equals("item.sandwich"))
					{
						order = stack;
						System.out.println("order is set");
					}
				}
			}
			boolean swap = false;
			int num = 0;
			int times = 0;
			
			if(orderList.size()>5)
				times = 5;
			else
				times = orderList.size();
			
			for(int p = 0; p < times; p++)
			{
				if(order!=null && swap == false)
				{
					String bread = orderList.get(p).getType().getUnlocalizedName();

					if(bread.equals("item.hamburgerbun") && order.getItem().getUnlocalizedName().equals("item.sandwich") || 
							bread.equals("item.sandwichbread") && order.getItem().getUnlocalizedName().equals("item.hamburger")){}
					else
					{
						System.out.println("match");
						swap = true;
						num = p;
					}
				}
			}
			if(swap)
			{
				System.out.println("should be swapping");
				System.out.println("order list size: " + orderList.size());
				event.player.inventory.consumeInventoryItem(order.getItem());
				score += ORDERCOMPLETE;
				System.out.println("adding to score: " +score);
				orderList.remove(num);
			}
		}
//			go through orders, match the id with the first order that has that same id
//			remove the order from inventory and order list
//			award points for the order [[give tip based on time completed?]]
	}
	
	public boolean onTurnIn(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();
		return (coord.posX >= -48 && coord.posX <= -43) && (coord.posZ >= 13 && coord.posZ <= 16);
	}
}
