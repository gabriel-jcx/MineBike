package org.ngs.bigx.minecraft.quests.custom;

import java.time.Clock;
import java.util.ArrayList;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.client.gui.hud.HudManager;
import org.ngs.bigx.minecraft.client.gui.hud.HudRectangle;
import org.ngs.bigx.minecraft.client.gui.hud.HudString;
import org.ngs.bigx.minecraft.client.gui.hud.HudTexture;
import org.ngs.bigx.minecraft.items.MineBikeCustomItems;
import org.ngs.bigx.minecraft.items.OvercookedHamburger;
import org.ngs.bigx.minecraft.items.OvercookedHamburgerbun;
import org.ngs.bigx.minecraft.items.OvercookedLettuce;
import org.ngs.bigx.minecraft.items.OvercookedSandwichbread;
import org.ngs.bigx.minecraft.npcs.custom.ChefGusteau;
import org.ngs.bigx.minecraft.npcs.custom.Raul;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract;
import org.ngs.bigx.minecraft.quests.custom.helpers.CustomQuestAbstract.Difficulty;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderOvercooked;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
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
		
	private boolean teleported; //true if player has been teleported to kitchen, false if not
	
	private ArrayList<ChunkCoordinates> ingredientCoordinates; //holds ingredient plate chunkcoordinates
	private ArrayList<Recipe> recipes; //holds all different recipes (10)
	private ArrayList<Recipe> orderList; //holds the orders being displayed to the gui, removes orders when completed
	private ArrayList<Recipe> completedOrders; //the orders that have been completed
	private ArrayList<String> orderTextures; // holds the textures for all possible orders to be displayed on screen
	
	private final int ORDERCOMPLETE = 20; //points given for each completed order
	private final int ORDERMISSED = -10; //points lost for each failed order (order expired)
	private int score; //holds total point values. if greater than this.progress, change progress to score
	private int ticks; //stores the number of ticks passed once players enter the kitchen
	private boolean countTicks; // when countTicks is true, begin incrementing ticks
	
	private HudRectangle rectangle; // rectangle displaying score
	private HudString scores; // string displaying "score:"
	private HudString num; // string displaying player's score
	
	private Display display;
	private Difficulty dif; // enum with difficulty. if not set, defaults to MEDIUM
	private int goal;
	
	private Clock clock;
	private long lastTime;
	
	private long TIME; // master time in milliseconds
	private HudString TIMER; // string displaying the time left to players
	private HudRectangle rectangletimer; // rectangle displaying time left to players

	private boolean gameover; // true if time is up and game is over
	
	private ItemStack[] playerInventory;
	private boolean gotInv;
	
	public OverCookedQuest()
	{
		super();
		
		this.progress = 0;
		this.name = "Overcooked Quest";
		this.completed = false;
		this.started = false;	
		
		teleported = false;
		
		ingredientCoordinates = new ArrayList<ChunkCoordinates>();
		setIngredientCoordinatesList();
		
		recipes = new ArrayList<Recipe>();
		setRecipesList();
		
		completedOrders = new ArrayList<Recipe>();
		orderList = new ArrayList<Recipe>();
		
		orderTextures = new ArrayList<String>();
		setTexturesList();
		
		score = 0;
		ticks = 0;
		countTicks = false;
		
		rectangle = new HudRectangle(580,280, 50,50, 0x4aa188);
		scores = new HudString(590,290, "Score:");
		num = new HudString(600, 305, "0");
		
 		display = new Display(dif);
		
		clock = Clock.systemDefaultZone();
		lastTime = clock.millis();
		
		TIME = 0;
		rectangletimer = new HudRectangle(520, 280, 50, 50, 0x9850eb88);
		TIMER = new HudString(537, 302, "5:00");
		
		goal = 240;
		
		gameover = false;
		
		playerInventory = new ItemStack[36];
		gotInv = false;
		
		register();
	}
	
	//start quest
	@Override
	public void start() 	
	{
		if (isStarted())
			return;
		progress = 0;
		
		//teleport them to the overcooked arena
		//WorldServer ws = MinecraftServer.getServer().worldServerForDimension(this.OVERCOOKEDDIMENSIONID);
		QuestTeleporter.teleport(player, OVERCOOKEDDIMENSIONID, 134, 10, 55);
		
		lastTime = clock.millis();
		
		super.start();
	}
	

	//adds coordinates to the list of ingredient coordinates
	private void setIngredientCoordinatesList()
	{
		ingredientCoordinates.add(new ChunkCoordinates(-2,10,70)); // 0=bowl
		ingredientCoordinates.add(new ChunkCoordinates(-1,10,28)); // 1=chicken
		ingredientCoordinates.add(new ChunkCoordinates(-21,10,16)); // 2=bread
		ingredientCoordinates.add(new ChunkCoordinates(-52,10,48)); // 3=lettuce
		ingredientCoordinates.add(new ChunkCoordinates(-52,10,76)); // 4=bun
		ingredientCoordinates.add(new ChunkCoordinates(-18,10,81)); // 5=potato
	}
	
	//adds recipes to recipe list
	private void setRecipesList()
	{
		Item sbread = MineBikeCustomItems.itemMap.get("item.sandwichbread");
		Item hbun = MineBikeCustomItems.itemMap.get("item.hamburgerbun");
		Item chicken = Item.getItemById(366);
		Item lettuce = MineBikeCustomItems.itemMap.get("item.lettuce");
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
		Item[] nogreen = new Item[] {chicken, potato, bowl};
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

	//adds texture names to list of texture strings
	//textures have the order and its components in a 100x100 png to display to players
	//textures have the same indices as their recipe in the recipes ArrayList
	private void setTexturesList()
	{
		//all ingredients
		orderTextures.add("textures/GUI/sandwich-all.png");
		orderTextures.add("textures/GUI/burger-all.png");
		
		//no chicken
		orderTextures.add("textures/GUI/sandwich-nochicken.png");
		orderTextures.add("textures/GUI/burger-nochicken.png");
		
		//no lettuce		
		orderTextures.add("textures/GUI/sandwich-nolettuce.png");
		orderTextures.add("textures/GUI/burger-nolettuce.png");
		
		//no potato
		orderTextures.add("textures/GUI/sandwich-nopotato.png");
		orderTextures.add("textures/GUI/burger-nopotato.png");
		
		//just chicken
		orderTextures.add("textures/GUI/sandwich-chickenonly.png");
		orderTextures.add("textures/GUI/burger-chickenonly.png");
	}
	
	@Override
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) 
	{
		//save player's inventory before emptying it for the game
		if(gotInv)
		{
			saveInventory(event);
			gotInv = false;
		}
		
		//once player has teleported to kitchen, begin counting ticks
		if(countTicks)
			ticks++;
		
		//keeps player in adventure mode (can't break blocks)
//		if(event.player.capabilities.allowEdit)
//	           event.player.setGameType(WorldSettings.getGameTypeById(2));
		
		//creative mode
		//System.out.println("ticking");		
		if(!event.player.capabilities.isCreativeMode)
			event.player.setGameType(WorldSettings.getGameTypeById(1));
		if(!teleported)
			teleport(event);
		
//		useFurnace(event);
	
		maxOneIngredient(event);
		giveIngredient(event);
		
		if(ticks % 20 == 0)
		{
			useCraftingTable(event);
			turnIn(event);
			
			//updates score on screen
			Integer temp = new Integer(score);
			num.text = temp.toString();
			
			if(orderList.size()>0)
				orderExpire();
		}
		
		//clock on screen
		if(clock.millis()>=lastTime + randomTime() && countTicks)
		{
			generateOrder();
			lastTime = clock.millis();
			for(Recipe r: orderList)
			{
				System.out.println("recipe in list: " + r.getType().getUnlocalizedName());
			}
			for(Recipe r: completedOrders)
			{
				System.out.println("in completed list: " + r.getType().getUnlocalizedName());
			}
		}
		
		int secondsPassed = (int) ((clock.millis() - TIME) / 1000);
		int totalTime = (int)(2 * 60) - secondsPassed; // total time player is in the kitchen (edit number 60 is being multiplied by
		if(totalTime == 0)
		{
			gameover = true;
			returnInventory(event);
		}
		else if(totalTime % 60 <10)
			TIMER.text = (totalTime/60) + ":0" + (totalTime % 60);
		else
			TIMER.text = (totalTime/60) + ":" + (totalTime % 60);
	}

	@Override
	public void onWorldTickEvent(TickEvent.WorldTickEvent event)
	{
		if(gameover)
		{
			endQuest(event);
			gameover = false;
		}
	}
	
	//saves player inventory and clears their inventory before game start
	private void saveInventory(TickEvent.PlayerTickEvent event)
	{
		//36 is the size of player's inventory
		for(int i = 0; i < 36; i ++)
		{
			ItemStack item = event.player.inventory.getStackInSlot(i);
			if(item!=null) {
				playerInventory[i] = item;
				event.player.inventory.decrStackSize(i, item.stackSize);
			}
		}
	}
	
	//returns player's inventory to them and gets rid of items from the game
	private void returnInventory(TickEvent.PlayerTickEvent event)
	{
		//there are 36 slots in the player's inventory
		for(int i = 0; i < 36; i ++)
		{
			ItemStack item = playerInventory[i];
			if(item!=null) {
				event.player.inventory.addItemStackToInventory(item);
			}
		}
	}
	
	//returns a random amount of milliseconds btw 17000 and 25000
	private int randomTime()
	{
		return ((int)(Math.random()*(25-17)) + 17) * 1000;
	}
	
	//ends the quest when an item is picked up
	@Override
	public void onItemPickUp(EntityItemPickupEvent event)
	{
		teleported = true;
		super.complete();
		started = false;
		display.unregisterAll();
		HudManager.unregisterRectangle(rectangle);
		HudManager.unregisterString(scores);
		HudManager.unregisterString(num);
		HudManager.unregisterRectangle(rectangletimer);
		HudManager.unregisterString(TIMER);
		QuestTeleporter.teleport(player, 0, (int) ChefGusteau.LOCATION.xCoord - 3, (int) ChefGusteau.LOCATION.yCoord, (int) ChefGusteau.LOCATION.zCoord);
	}
	
	//teleports the player from the instruction room to the kitchen to start the game
	//generates the first order (the rest will be generated in onPlayerTickEvent
	private void teleport(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();		
		if((coord.posX >= 126 && coord.posX <= 128) && (coord.posZ >= 54 && coord.posZ <= 56))
		{
			QuestTeleporter.teleport(player, OVERCOOKEDDIMENSIONID, -21, 10, 46);
			countTicks = true;
			gotInv = true;
			generateOrder();

			HudManager.registerRectangle(rectangle);
			HudManager.registerString(scores);
			HudManager.registerString(num);
			HudManager.registerRectangle(rectangletimer);
			HudManager.registerString(TIMER);
			
			TIME = clock.millis();			
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
            	boolean areChickens = stack.getItem().getUnlocalizedName().equals("item.chickenCooked");
            	boolean areBowls = stack.getItem().getUnlocalizedName().equals("item.bowl");
            	
            	//if there is more than one ingredient in player inventory, reduce it to one
            	if(areLettuce || arePotatoes || areSBread || areHBun || areChickens || areBowls) 
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
		for(int i = 0; i<ingredientCoordinates.size(); i++)
		{
			if(onIngredient(ingredientCoordinates.get(i), coord))
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
		Item item = Item.getItemById(256); // defaults item to a shovel because you can't make an empty stack
		String name = "";
		switch(num)
		{
			case 0:
				item = Item.getItemById(281); //bowl
				name = "bowl";
				break;
			case 1:
				item = Item.getItemById(366); //chicken
				name = "chickenCooked";
				break;
			case 2:
				item = MineBikeCustomItems.itemMap.get("item.sandwichbread"); //sandwich bread
				name = "sandwichbread";
				break;
			case 3:
				item = MineBikeCustomItems.itemMap.get("item.lettuce"); //lettuce
				name = "lettuce";
				break;
			case 4:
				item = MineBikeCustomItems.itemMap.get("item.hamburgerbun"); //bun
				name = "hamburgerbun";
				break;
			case 5:
				item = Item.getItemById(392); //potato
				name = "potato";
				break;				
		}
		return new NewItemStack(new ItemStack(item, 1), name);
	}

	//if on crafting table plates, use ingredients in player's inventory to craft a sandwich or burger if possible
	private void useCraftingTable(TickEvent.PlayerTickEvent event)
	{
		if(onCrafting(event)) //if on crafting table
		{
			for(int i = 0; i < orderList.size(); i++) //for the size of the order list
			{
				System.out.println("order " + i + "is going to check if it can be made");
				if(orderList.get(i).canMake(event)) //for eat order in the order list, if it can be made with the inventory
				{
					System.out.println("order " + i +"can be made~~");
					giveAndRemove(event, i); //give order and remove the ingredients
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
		//System.out.println("removed items...");
		if(orderList.get(i).getType().getUnlocalizedName().equals("item.sandwichbread"))
			event.player.inventory.addItemStackToInventory(new ItemStack(MineBikeCustomItems.itemMap.get("item.sandwich"),1));
		else
			event.player.inventory.addItemStackToInventory(new ItemStack(MineBikeCustomItems.itemMap.get("item.hamburger"),1)); 
		
		//System.out.println("gave order...");
		completedOrders.add(orderList.get(i));
		//System.out.println("added order to completedOrders");
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
		{
			orderList.add(recipes.get(temp));
			display.createOrderTexture(orderTextures.get(temp), orderList.size()-1); // displays a texture on the screen of the order
		}
	}
	
	//expire or just keep stacking and let them go as fast as they can without the pressure?
	public void orderExpire() 
	{
		if(orderList.size()>0) {
			display.tenSecLeft(0);
			if(display.expire(0))
			{
				Recipe order = orderList.get(0);
				orderList.remove(0);
				boolean duplicate = false;
				for(int y = 0; y < orderList.size(); y++)
				{
					if(orderList.get(y).equals(order))
						duplicate = true;
				}
				for(int r = 0; r < completedOrders.size(); r++)
				{
					if(completedOrders.get(r).equals(order) && !duplicate)
					{
						completedOrders.remove(r);
						r--;
					}
				}
				score += ORDERMISSED;
			}
		}
	}
	
	//if on turn in plates, find the correct order on the order list
	//removes the order from inven
  //tory and order list, award points
	public void turnIn(TickEvent.PlayerTickEvent event) //CAN WE DO IDS HERE
	{
		//ADD TIPS BASED ON TIME
		if(onTurnIn(event))
		{
			boolean removed = false;
			//Recipe r : completedOrders
			for(Recipe r: completedOrders)
			{
				//System.out.println("looking at completed orders ");
				for(int k = 0; k < orderList.size(); k++)
				{
					if(orderList.get(k).equals(r))
					{
						if(r.getType().getUnlocalizedName().equals("item.sandwichbread"))
						{
							event.player.inventory.consumeInventoryItem(MineBikeCustomItems.itemMap.get("item.sandwich"));
							//System.out.println("sandwich consumed");
							orderList.remove(r);
							removed = true;
							display.removeOrderTexture(k);
							score += ORDERCOMPLETE;
						}
						else if(r.getType().getUnlocalizedName().equals("item.hamburgerbun"))
						{
							event.player.inventory.consumeInventoryItem(MineBikeCustomItems.itemMap.get("item.hamburger"));
							//System.out.println("burger consumed");
							orderList.remove(r);
							removed = true;
							display.removeOrderTexture(k);
							score += ORDERCOMPLETE;
						}
					}
				}
			}
			if(removed)
				completedOrders.clear();
		}	
	}
	
	//returns true if player is on turn in plates
	public boolean onTurnIn(TickEvent.PlayerTickEvent event)
	{
		ChunkCoordinates coord = event.player.getPlayerCoordinates();
		return (coord.posX >= -48 && coord.posX <= -43) && (coord.posZ >= 13 && coord.posZ <= 16);
	}
  
	//setting difficulty changes the time that it takes for orders to expire as well as the minimum goal of points necessary
	//to 'win' the game (goal = the minimum score necessary)(display handles the expiration of the orders, so difficulty is
	//sent to the display to change the time each order has before it expires
	public void setDifficulty(Difficulty difficultyIn) 
	{
		dif = difficultyIn;
		display.setDifficulty(dif);
		
		switch(dif)
		{
		case EASY:
			goal = 200;
			break;
		case MEDIUM:
			goal = 240;
			break;
		case HARD:
			goal = 280;
			break;
		}
	}
	
	//once the timer is completed, sends the player to a different part of the world to collect reward
	//unregisters all HUD elements 
	private void endQuest(TickEvent.WorldTickEvent event)
	{
		QuestTeleporter.teleport(player, OVERCOOKEDDIMENSIONID, 365, 13, 185);
		
		if(score >= goal)
		{
			EntityItem entityitem1 = new EntityItem(event.world.provider.worldObj, 365, 20, 177, new ItemStack(Items.gold_ingot,4));
	        event.world.provider.worldObj.spawnEntityInWorld(entityitem1);
		}
		else
		{
			EntityItem entityitem1 = new EntityItem(event.world.provider.worldObj, 365, 20, 177, new ItemStack(Items.gold_ingot,2));
	        event.world.provider.worldObj.spawnEntityInWorld(entityitem1);
		}
		HudManager.unregisterRectangle(rectangletimer);
		HudManager.unregisterString(TIMER);
		display.unregisterAll();
	}
}

////if on furnace plates, remove a raw chicken and add a cooked chicken if possible
//private void useFurnace(TickEvent.PlayerTickEvent event)
//{
//	if(onFurnace(event))
//	{
//		InventoryPlayer inventory = event.player.inventory;
//		
//		for(int i = 0; i < inventory.getSizeInventory(); i++)
//		{
//			ItemStack stack = inventory.getStackInSlot(i);
//			if(stack != null)
//			{
//				if(stack.getItem().getUnlocalizedName().equals("item.chickenRaw"))
//				{
//					event.player.inventory.consumeInventoryItem(Item.getItemById(365));
//					ItemStack cooked = new ItemStack(Item.getItemById(366)); //cooked  chicken
//					event.player.inventory.addItemStackToInventory(cooked);
//				}
//			}
//		}
//	}
//}

////returns true of player is on furnace plate
//private boolean onFurnace(TickEvent.PlayerTickEvent event)
//{
//	ChunkCoordinates coord = event.player.getPlayerCoordinates();
//	return (coord.posX >= -54 && coord.posX <= -51) && (coord.posZ >= 14 && coord.posZ <= 23);
//}
