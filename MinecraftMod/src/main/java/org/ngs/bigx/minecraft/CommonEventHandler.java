package org.ngs.bigx.minecraft;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistException;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistManager;
import org.ngs.bigx.minecraft.client.skills.Skill;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager.CUSTOMCOMMAND;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.npcs.NpcEvents;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestTaskTutorial;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;


public class CommonEventHandler {
	private static boolean EVENT_PLAYERSTATE_FLAG = false;
	private static boolean EVENT_PLAYERSTATE_RESET = false;
	private static boolean EVENT_PLAYERSTATE_LOAD = false;
	private static boolean EVENT_PLAYERSTATE_SAVE = false;
	
	public static void setEVENT_PLAYERSTATE_FLAG() {
		EVENT_PLAYERSTATE_FLAG = true;
	}

	public static void setEVENT_PLAYERSTATE_RESET() {
		setEVENT_PLAYERSTATE_FLAG();
		EVENT_PLAYERSTATE_RESET = true;
	}

	public static void setEVENT_PLAYERSTATE_LOAD() {
		setEVENT_PLAYERSTATE_FLAG();
		EVENT_PLAYERSTATE_LOAD = true;
	}

	public static void setEVENT_PLAYERSTATE_SAVE() {
		setEVENT_PLAYERSTATE_FLAG();
		EVENT_PLAYERSTATE_SAVE = true;
	}

	static float playerQuestPitch, playerQuestYaw;
	
	int server_tick = 0;
	int serverQuestTestTickCount = 10;
	
	private static int thiefMaxLevel = 1;
	
	public static LevelSystem levelSys = new LevelSystem();
//	public static QuestTaskChasing chaseQuest = new QuestTaskChasing();
//	public static QuestTaskChasingFire chaseQuestFire = new QuestTaskChasingFire();
	boolean chaseQuestInProgress = false;
	
	private static int onPlayerTickEventCount = 0;
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (inBounds(event.player, Vec3.createVectorHelper(118, 152, -148), Vec3.createVectorHelper(125, 146, -151)) &&
				event.player.dimension == 0) {
			QuestTeleporter.teleport(event.player, WorldProviderDungeon.dimID, 0, 64, 0);
		}
		else if (inBounds(event.player, Vec3.createVectorHelper(3, 63, 10), Vec3.createVectorHelper(-3, 68, 20)) &&
				event.player.dimension == WorldProviderDungeon.dimID) {
			QuestTeleporter.teleport(event.player, 0, 121, 163, -145);
		}
		
		if (!event.player.worldObj.isRemote)
		{
			try {
				onPlayerTickEventCount++;
				
				if(onPlayerTickEventCount >= 50)
					onPlayerTickEventCount = 0;
				
				if(onPlayerTickEventCount == 0)
				{
					if(EVENT_PLAYERSTATE_FLAG)
					{
						if(EVENT_PLAYERSTATE_LOAD)
						{
							GameSaveManager.sendCustomCommand((BigxClientContext)BigxClientContext.getInstance(), BiGX.BIGXSERVERIP, CUSTOMCOMMAND.GETGAMESAVES);
							EVENT_PLAYERSTATE_LOAD = false;
						}
						if(EVENT_PLAYERSTATE_RESET)
						{
							/**
							 * 
							 * 
							 * 
							 * FILL THIS IN TO IMPLEMENT THE GAME STATE RESET FEATURE
							 * 
							 * 
							 */
							EVENT_PLAYERSTATE_RESET = false;
						}
						if(EVENT_PLAYERSTATE_SAVE)
						{
							GameSaveManager.sendCustomCommand((BigxClientContext)BigxClientContext.getInstance(), BiGX.BIGXSERVERIP, CUSTOMCOMMAND.SETGAMESAVES);
							EVENT_PLAYERSTATE_SAVE = false;
						}
						
						if(!(EVENT_PLAYERSTATE_LOAD || EVENT_PLAYERSTATE_RESET || EVENT_PLAYERSTATE_SAVE))
							EVENT_PLAYERSTATE_FLAG = false;
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean inBounds(EntityPlayer player, Vec3 edge1, Vec3 edge2) {
		return (player.posX >= edge1.xCoord && player.posX <= edge2.xCoord || player.posX <= edge1.xCoord && player.posX >= edge2.xCoord) &&
				(player.posY >= edge1.yCoord && player.posY <= edge2.yCoord || player.posY <= edge1.yCoord && player.posY >= edge2.yCoord) &&
				(player.posZ >= edge1.zCoord && player.posZ <= edge2.zCoord || player.posZ <= edge1.zCoord && player.posZ >= edge2.zCoord);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		System.out.println("[BiGX] onWorldLoad(WorldEvent.Load event)");
		
//		event.world.provider.setWorldTime(0);
		event.world.provider.resetRainAndThunder();
		
		if(event.world.isRemote)
		{	
			NpcCommand.setNpcSpawnFlag();
			NpcCommand.addNpcSpawnDimensionId(event.world.provider.dimensionId);
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
//		BigxContext context = BiGX.instance().context;
		
		//TODO: Implement proper cleanup when the game is exited
		//The event which is called by the server shutting down needs to be located and used
		//instead of this event which is called whenever the game is paused.
		
		//context.unloadWorld();
	}
	
	@SubscribeEvent
	public void entityInteractEvent(EntityInteractEvent e) {
		EntityPlayer player = e.entityPlayer;
		System.out.println("Interacting with NPC...");
		NpcEvents.InteractWithNPC(player, e);
	}
	
	@SubscribeEvent
	public void entityAttacked(LivingAttackEvent event)
	{
//		System.out.println("Attack the player!");
		EntityLivingBase attackedEnt = event.entityLiving;
		DamageSource attackSource = event.source;
		if(attackedEnt instanceof EntityPlayer)
		{
			if(attackSource == DamageSource.fall)
			{
				System.out.println("NO FALL!!!");
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event) {
//		System.out.println("Falling...");
		if (event.entityLiving != null && event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			player.fallDistance = 0.1F;
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e) {
		EntityPlayer player = e.entityPlayer;
		World w = e.world;

		if (!w.isRemote) {
			if (w.getBlock(e.x, e.y, e.z) == Blocks.chest)
				BiGXEventTriggers.chestInteract(e, w, levelSys);
		}
	}
	
	@SubscribeEvent
	public void onItemToss(ItemTossEvent event) {
	    Item droppedItem = event.entityItem.getEntityItem().getItem();
	    if (droppedItem == Items.paper) {
	        event.setCanceled(true);
	        event.player.inventory.addItemStackToInventory(new ItemStack(Items.paper));
	    }
	    if (droppedItem == Item.getItemById(4801)) {
	        event.setCanceled(true);
	        event.player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4801)));
	    }
	}
	
	@SubscribeEvent
	public void onPlayerUse(PlayerInteractEvent event){
		EntityPlayer p = event.entityPlayer;
//		itemOnPlayersHand.getDisplayName().contains("Teleportation Potion")
//		if(itemOnPlayersHand.getItem() == Items.paper)
//		if (!p.inventory.hasItem(Items.paper)){
//			p.inventory.addItemStackToInventory(new ItemStack(Items.paper));
//		}
//		if (!p.inventory.hasItem(Item.getItemById(4801))){
//			p.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4801)));	
//		}
		
		ItemStack itemOnPlayersHand= p.getHeldItem();
		
		if (itemOnPlayersHand != null){
//			System.out.println("Item Name["+itemOnPlayersHand.getDisplayName()+"]");
			if (itemOnPlayersHand.getItem() == Items.enchanted_book){
				if (itemOnPlayersHand.getDisplayName().contains("Skill")){
					System.out.println("Adding Skill!");	
					BigxClientContext context = BiGX.instance().clientContext;
					String unlockedSkillName = itemOnPlayersHand.getDisplayName().substring(7).replace(" ", "");
					System.out.println(unlockedSkillName);
					List<Skill> skills = BigxClientContext.getCurrentGameState().getSkillManager().getSkills();
					for (Skill skill : skills){
						if (skill.getClass().getName().contains(unlockedSkillName)){
							skill.unlockSkillState();
						}
					}
					p.inventory.consumeInventoryItem(itemOnPlayersHand.getItem());
//					p.inventory.clearInventory(itemOnPlayersHand.getItem(), -1);
					System.out.println("Skill Added!");
				}
			}
			if(itemOnPlayersHand.getItem() == Items.paper)
			{
				if(!p.worldObj.isRemote)
					return;
				
				Minecraft mc = Minecraft.getMinecraft();
				GuiQuestlistManager guiQuestlistManager = new GuiQuestlistManager((BigxClientContext)BigxClientContext.getInstance(), mc);
				
				guiQuestlistManager.resetQuestReferences();
				
				try {
					Collection<Quest> questlist = BiGX.instance().clientContext.getQuestManager().getAvailableQuestList().values();
					
					for(Quest quest : questlist)
					{
						guiQuestlistManager.addQuestReference(quest);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (GuiQuestlistException e) {
					e.printStackTrace();
				}
				
				if(mc.currentScreen == null)
					mc.displayGuiScreen(guiQuestlistManager);
				System.out.println("Display Quest List");
			}
			else if(itemOnPlayersHand.getDisplayName().contains("Phone"))
			{
				ClientEventHandler.pedalingModeState ++;
				ClientEventHandler.pedalingModeState %= 3;
				ClientEventHandler.animTickSwitch = 0;
				ClientEventHandler.animTickFade = 0;
				
				ClientEventHandler.pedalingCombo.init();
				
				System.out.println("pedalingModeState[" + ClientEventHandler.pedalingModeState + "]");
			}
		}
	}
	
	@SubscribeEvent
	public void onItemUse(final PlayerUseItemEvent.Start event) {
		System.out.println("Item being used...");
		System.out.println(BiGX.instance().clientContext.getQuestManager().getActiveQuestId());
		if (event.item.getDisplayName().contains("Past"))
			QuestTeleporter.teleport(event.entityPlayer, 0, 88, 78, 243);
		if (event.item.getDisplayName().contains("Tutorial"))
			QuestTeleporter.teleport(event.entityPlayer, 102, 512, 65, 0);
	}
	
	@SubscribeEvent
	public void onAttackEntityEvent(AttackEntityEvent event) {
		if (event.target.worldObj.isRemote){
			if (event.target.toString().contains("Scientist"))
				GuiMessageWindow.showMessage("Scientist: Don't hit me...");
			else if (event.target.dimension == 0)
				return;
			else if (event.target.dimension == 102){
				BiGXEventTriggers.attackNPC(event);
			}
			else if (BiGX.instance().clientContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_TUTORIAL){
				Quest activeQuest = BiGX.instance().clientContext.getQuestManager().getActiveQuest();
				QuestTaskTutorial tutorialTask = (QuestTaskTutorial) activeQuest.getCurrentQuestTask();
				tutorialTask.hitEntity(event.entityPlayer, (EntityLivingBase) event.target);
			}
		}
//		else if (BiGX.instance().serverContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_TUTORIAL){
//			if (event.target.toString().contains("Scientist"))
//				return;
//			else if (event.target.dimension == 0)
//				return;
//			else if (event.target.dimension == 102)
//				BiGXEventTriggers.attackNPC(event);
//			Quest activeQuest = BiGX.instance().serverContext.getQuestManager().getActiveQuest();
//			QuestTaskTutorial tutorialTask = (QuestTaskTutorial) activeQuest.getCurrentQuestTask();
//			tutorialTask.hitEntity(event.entityPlayer, (EntityLivingBase) event.target);
//		}
		else {
			if (event.target.dimension == 0 || event.target.dimension == 102) {
				BiGXEventTriggers.attackNPC(event);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onDecoratorCreate(DecorateBiomeEvent.Decorate event) {
		if (event.world.provider.getDimensionName() == WorldProviderFlats.dimName) {
			if (event.type == DecorateBiomeEvent.Decorate.EventType.PUMPKIN) {
				// Stops the specified EventType from decorating during chunk generation
				event.setResult(Result.DENY);
			}
		}
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) throws Exception {
		if (MinecraftServer.getServer() != null && event.phase == TickEvent.Phase.END) {
			boolean isServer = MinecraftServer.getServer().isDedicatedServer();
			server_tick++;
			
			//200 ticks = 10 second
			if (server_tick==200) {
				server_tick = 0;
				
				//Making sure it remains daytime all the time
//				World current_world = MinecraftServer.getServer().getEntityWorld();

				if(Minecraft.getMinecraft().thePlayer != null)
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/gamerule doDaylightCycle false");
//				current_world.setWorldTime(8000);
			}
			
			if( (server_tick%60) == 0 )
			{
				NpcCommand.spawnNpcInDB();
			}
			
			/**
			 * Solution to doors being annoying -- auto-open in range.
			 * NOTE: Only do this for Wooden Doors. Iron Doors should be
			 * used for more important things... example: puzzle solving.
			 */
			if ((server_tick%10) == 0) {
				int doorOpenDistance = 5;
				int doorCheckRadius = 10;
				
				for (WorldServer ws : MinecraftServer.getServer().worldServers) {
					for (EntityPlayer player : (List<EntityPlayer>) ws.playerEntities) {
						
						int pX = (int) player.posX;
						int pY = (int) player.posY;
						int pZ = (int) player.posZ;
						
						for (int xx = pX-doorCheckRadius; xx < pX+doorCheckRadius; ++xx) {
							for (int zz = pZ-doorCheckRadius; zz < pZ+doorCheckRadius; ++zz) {
								for (int yy = pY-doorCheckRadius; yy < pY+doorCheckRadius; ++yy) {
									
									if (ws.getBlock(xx, yy, zz) == Blocks.wooden_door) {
										double blockDistance = Math.sqrt(Math.pow(Math.abs(xx-pX), 2) + Math.pow(Math.abs((yy-pY)), 2) + Math.pow(Math.abs(zz-pZ), 2));
										// Open if close
										int gottenMeta = ws.getBlockMetadata(xx, yy, zz);
										int meta = gottenMeta;
										
										if (blockDistance <= doorOpenDistance && (gottenMeta >= 0 && gottenMeta < 4)) {
											meta += 4;
											ws.setBlockMetadataWithNotify(xx, yy, zz, meta, 3);
											ws.playAuxSFX(1003, xx, yy, zz, 0);
										} else if (blockDistance > doorOpenDistance && (gottenMeta >= 4 && gottenMeta < 8)){
											meta -= 4;
											ws.setBlockMetadataWithNotify(xx, yy, zz, meta, 3);
											ws.playAuxSFX(1003, xx, yy, zz, 0);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}