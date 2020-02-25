package org.ngs.bigx.minecraft;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
//import org.bukkit.block.Block;
import org.ngs.bigx.minecraft.client.ClientEventHandler;
import org.ngs.bigx.minecraft.client.GuiMessageWindow;
import org.ngs.bigx.minecraft.client.area.Area;
import org.ngs.bigx.minecraft.client.area.Area.AreaTypeEnum;
import org.ngs.bigx.minecraft.client.gui.GuiMonsterAppears;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistException;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistManager;
import org.ngs.bigx.minecraft.client.skills.Skill;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.gamestate.GameSave;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager;
import org.ngs.bigx.minecraft.gamestate.GameSaveManager.CUSTOMCOMMAND;
import org.ngs.bigx.minecraft.gamestate.levelup.LevelSystem;
import org.ngs.bigx.minecraft.npcs.NpcCommand;
import org.ngs.bigx.minecraft.npcs.NpcEvents;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestTaskFightAndChasing;
import org.ngs.bigx.minecraft.quests.QuestTaskTutorial;
import org.ngs.bigx.minecraft.quests.worlds.QuestTeleporter;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderDungeon;
import org.ngs.bigx.minecraft.quests.worlds.WorldProviderFlats;

import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.NPCEntityHelper;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.event.entity.player.Entit
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;


public class CommonEventHandler {

	private static boolean EVENT_PLAYERSTATE_FLAG = false;
	private static boolean EVENT_PLAYERSTATE_RESET = false;
	private static boolean EVENT_PLAYERSTATE_LOAD = false;
	private static boolean EVENT_PLAYERSTATE_SAVE = false;
	
	public static boolean flagOpenMonsterEncounter = false;
	
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
	
	int fightAndChaseQuestTick = 0;
//	int fightAndChaseQuestTickCount = 90*20;
	int fightAndChaseQuestTickCount = 2000; // Every 20 seconds (10 seconds is too short)
	
	private static int thiefMaxLevel = 1;
	
	public static LevelSystem levelSys = new LevelSystem();
	boolean chaseQuestInProgress = false;
	
	private static int onPlayerTickEventCount = 0;
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		if (inBounds(event.player, new Vec3d(118, 152, -148), new Vec3d(125, 146, -151)) &&
				event.player.dimension == 0) {
			QuestTeleporter.teleport(event.player, WorldProviderDungeon.dimID, 0, 64, 0);
		}
		else if (inBounds(event.player, new Vec3d(3, 63, 10), new Vec3d(-3, 68, 20)) &&
				event.player.dimension == WorldProviderDungeon.dimID) {
			QuestTeleporter.teleport(event.player, 0, 121, 163, -145);
		}
		
		if(event.player != null)
		{	
			if(!event.player.world.isRemote)
			{
				if( (event.player.world.provider.getDimension() == 0) || (event.player.world.provider.getDimension() == 105) )
				{
					if(!( (event.player.posX >= 70) && (event.player.posX <=132) &&
						(event.player.posY >= 45) && (event.player.posY <= 100) &&
						(event.player.posZ >= 140) && (event.player.posZ <=256)))
					{
						if(!Minecraft.getMinecraft().isGamePaused())
							fightAndChaseQuestTick ++;
					}

					if(fightAndChaseQuestTick >= fightAndChaseQuestTickCount)
					{
						fightAndChaseQuestTick = 0;

						if(BiGX.instance().serverContext.getQuestManager() != null)
						{
							if(BiGX.instance().serverContext.getQuestManager().getActiveQuest() != null)
							{
								if(BiGX.instance().serverContext.getQuestManager().getAvailableQuestList().get(Quest.QUEST_ID_STRING_FIGHT_CHASE) != null)
								{
									if(!( (event.player.posX >= 70) && (event.player.posX <=132) &&
										(event.player.posY >= 45) && (event.player.posY <= 100) &&
										(event.player.posZ >= 140) && (event.player.posZ <=256)))
									{
										// OPEN Monster Appears Gui
										flagOpenMonsterEncounter = true;
									}
								}
							}
						}
					}
				}
				else
				{
					fightAndChaseQuestTick = 0;
				}

				if(QuestTeleporter.teleportFlag)
				{
					if(event.player.dimension == QuestTeleporter.dimId)
					{
						event.player.setPosition(QuestTeleporter.posX, QuestTeleporter.posY, QuestTeleporter.posZ);
						QuestTeleporter.teleportFlag = false;
					}
				}
			}
		}

		if (event.player.world.isRemote)
		{
			if(GameSaveManager.flagEnableChasingQuestClient)
			{
				try {
					GameSaveManager.enableChasingQuest(event.player);
				} catch (QuestException e) {
					e.printStackTrace();
				}

				GameSaveManager.flagEnableChasingQuestClient = false;

				System.out.println("[BiGX] Enabling Chasing Quest Initiated.");
			}
			else if(GameSaveManager.flagUpdatePlayerLevelClient)
			{
				GameSaveManager.flagUpdatePlayerLevelClient = false;

				GameSaveManager.updatePlayerLevel();
			}

			if(GameSaveManager.flagEnableFightAndChasingQuestClient)
			{
				try {
					GameSaveManager.enableFightAndChasingQuest(event.player);
				} catch (QuestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				GameSaveManager.flagEnableFightAndChasingQuestClient = false;
			}
		}
		else
		{
			if(GameSaveManager.flagEnableChasingQuestServer)
			{
				try {
					GameSaveManager.enableChasingQuest(event.player);
				} catch (QuestException e) {
					e.printStackTrace();
				}

				GameSaveManager.flagEnableChasingQuestServer = false;

				System.out.println("[BiGX] Enabling Chasing Quest Initiated.");
			}
			else if(GameSaveManager.flagUpdatePlayerLevelServer)
			{
				GameSaveManager.flagUpdatePlayerLevelServer = false;

				GameSaveManager.updatePlayerLevel();
			}

			if(GameSaveManager.flagEnableFightAndChasingQuestServer)
			{
				try {
					GameSaveManager.enableFightAndChasingQuest(event.player);
					GameSaveManager.flagEnableFightAndChasingQuestServer = false;
				} catch (QuestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (!event.player.world.isRemote)
		{
			if(GuiMonsterAppears.isGuiMonsterAppearsClosed)
			{
				GuiMonsterAppears.isGuiMonsterAppearsClosed = false;

				// This gets triggered when the screen closes
				System.out.println("[BiGX] Start Fight And Chase Quest");
				Quest quest = BiGX.instance().serverContext.getQuestManager().getAvailableQuestList().get(Quest.QUEST_ID_STRING_FIGHT_CHASE);

				try {
					BiGX.instance().serverContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_FIGHT_CHASE);
					BiGX.instance().clientContext.getQuestManager().setActiveQuest(Quest.QUEST_ID_STRING_FIGHT_CHASE);
					((QuestTaskFightAndChasing)quest.getCurrentQuestTask()).isBoss = false;
					int posy = ((int) event.player.posY == event.player.posY)?(int) event.player.posY:((int) event.player.posY)+1;
					int origX = (int)event.player.posX, origY = (int)event.player.posY, origZ = (int)event.player.posZ;

//					for(int i=-1; i<2; i++) // z
//					{
//						for(int j=-1; j<2; j++) // y
//						{
//							for(int k=-1; k<2; k++) // x
//							{
//								if(event.player.world.getBlock(origX + k, origY + j, origZ + i) == Blocks.AIR)
//								{
//									origX = origX + k;
//									origY = origY + j;
//									origZ = origZ + i;
//									break;
//								}
//							}
//						}
//					}
					((QuestTaskFightAndChasing)quest.getCurrentQuestTask()).setPreviousLocationBeforeTheQuest(event.player.world.provider.getDimension(), origX, origY, origZ);
					((QuestTaskFightAndChasing)quest.getCurrentQuestTask()).handleQuestStart();
				} catch (QuestException e) {
					e.printStackTrace();
				}
			}

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

	private boolean inBounds(EntityPlayer player, Vec3d edge1, Vec3d edge2) {
		return (player.posX >= edge1.x && player.posX <= edge2.x || player.posX <= edge1.x && player.posX >= edge2.x) &&
				(player.posY >= edge1.y && player.posY <= edge2.y || player.posY <= edge1.y && player.posY >= edge2.y) &&
				(player.posZ >= edge1.z && player.posZ <= edge2.z || player.posZ <= edge1.z && player.posZ >= edge2.z);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		System.out.println("[BiGX] onWorldLoad(WorldEvent.Load event)");

//		event.world.provider.setWorldTime(0);
		event.getWorld().provider.resetRainAndThunder();

		if(event.getWorld().isRemote)
		{
			NpcCommand.setNpcSpawnFlag();
			NpcCommand.addNpcSpawnDimensionId(event.getWorld().provider.getDimension());
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		BigxClientContext context = BiGX.instance().clientContext;
	}
	
	@SubscribeEvent
	public void entityInteractEvent(PlayerInteractEvent.EntityInteract e) {
		EntityPlayer player = e.getEntityPlayer();
		System.out.println("Interacting with NPC...");
		NpcEvents.InteractWithNPC(player, e);
	}
	
	@SubscribeEvent
	public void entityAttacked(LivingAttackEvent event)
	{
//		System.out.println("Attack the player!");
		EntityLivingBase attackedEnt = event.getEntityLiving();
		DamageSource attackSource = event.getSource();
		if(attackedEnt instanceof EntityPlayer)
		{
			if(attackSource == DamageSource.FALL)
			{
				System.out.println("NO FALL!!!");
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event) {
//		System.out.println("Falling...");
		if (event.getEntityLiving() != null && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			player.fallDistance = 0.1F;
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e) {
		EntityPlayer player = e.getEntityPlayer();
		World w = e.getWorld();

		if (!w.isRemote) {
			//BlockPos pos = new BlockPos();
			if (w.getBlockState(e.getPos()).getBlock()== Blocks.CHEST)
				BiGXEventTriggers.chestInteract(e, w, levelSys);
		}
	}
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Item> event)
	{
		System.out.println("CommonEventHandler: RegisterEvent Triggered");
		for (Item item : BiGX.instance().customItems){
			//System.out.println(Item.getIdFromItem(item));
			System.out.println(item.getUnlocalizedName());
			event.getRegistry().registerAll(item);
		}

	}
	@SubscribeEvent
	public void registerBlock(RegistryEvent.Register<Block> event){
		//event.getRegistry().register(BiGX.instance().BlockQuestFRMCheck);
		//event.getRegistry().register(BiGX.instance().blockQuestChest);
	}
	@SubscribeEvent
	public void onItemToss(ItemTossEvent event) {
	    Item droppedItem = event.getEntityItem().getItem().getItem();
	    if (droppedItem == Items.PAPER) {
	        event.setCanceled(true);
	        event.getPlayer().inventory.addItemStackToInventory(new ItemStack(Items.PAPER));
	    }
	    if (droppedItem == Item.getItemById(4801)) {
	        event.setCanceled(true);
	        event.getPlayer().inventory.addItemStackToInventory(new ItemStack(Item.getItemById(4801)));
	    }
	}
	
	@SubscribeEvent
	public void onPlayerUse(PlayerInteractEvent event){
		EntityPlayer p = event.getEntityPlayer();
		//if(p.)
		//p.getAct
		ItemStack itemOnPlayersHand= p.getHeldItem(EnumHand.MAIN_HAND);
		
		if (itemOnPlayersHand != null){
			if (itemOnPlayersHand.getItem() == Items.ENCHANTED_BOOK){
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
					p.inventory.addItemStackToInventory(itemOnPlayersHand);
					//p.inventory.consumeInventoryItem(itemOnPlayersHand.getItem());
					System.out.println("Skill Added!");
				}
			}
			if(itemOnPlayersHand.getItem() == Items.PAPER)
			{
				if(!p.getEntityWorld().isRemote)
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
	public void onAttackEntityEvent(AttackEntityEvent event) {
		if (event.getTarget().getEntityWorld().isRemote){
			if (event.getTarget().toString().contains("Scientist"))
				GuiMessageWindow.showMessage("Scientist: Don't hit me...");
			else if (event.getTarget().dimension == 0)
				return;
			else if (event.getTarget().dimension == 102){
				BiGXEventTriggers.attackNPC(event);
			}
			else if (BiGX.instance().clientContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_TUTORIAL){
				Quest activeQuest = BiGX.instance().clientContext.getQuestManager().getActiveQuest();
				QuestTaskTutorial tutorialTask = (QuestTaskTutorial) activeQuest.getCurrentQuestTask();
				tutorialTask.hitEntity(event.getEntityPlayer(), (EntityLivingBase) event.getTarget());
			}
		}
//		else if (BiGX.instance().serverContext.getQuestManager().getActiveQuestId() == Quest.QUEST_ID_STRING_TUTORIAL){
//			if (event.getTarget().toString().contains("Scientist"))
//				return;
//			else if (event.getTarget().dimension == 0)
//				return;
//			else if (event.getTarget().dimension == 102)
//				BiGXEventTriggers.attackNPC(event);
//			Quest activeQuest = BiGX.instance().serverContext.getQuestManager().getActiveQuest();
//			QuestTaskTutorial tutorialTask = (QuestTaskTutorial) activeQuest.getCurrentQuestTask();
//			tutorialTask.hitEntity(event.entityPlayer, (EntityLivingBase) event.getTarget());
//		}
		else {
			if (event.getTarget().dimension == 0 || event.getTarget().dimension == 102) {
				BiGXEventTriggers.attackNPC(event);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onDecoratorCreate(DecorateBiomeEvent.Decorate event) {
		if (event.getWorld().provider.getDimensionType().getName() == WorldProviderFlats.dimName) {
			if (event.getType() == DecorateBiomeEvent.Decorate.EventType.PUMPKIN) {
				// Stops the specified EventType from decorating during chunk generation
				event.setResult(Result.DENY);
			}
		}
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) throws Exception {
		if (FMLCommonHandler.instance().getMinecraftServerInstance() != null && event.phase == TickEvent.Phase.END) {
			boolean isServer = FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer();
			server_tick++;
			
			//200 ticks = 10 second
			if (server_tick==200) {
				server_tick = 0;
				
				//Making sure it remains daytime all the time
//				World current_world = MinecraftServer.getServer().getEntityWorld();

				if(Minecraft.getMinecraft().player != null)
				{
					Minecraft.getMinecraft().player.sendChatMessage("/gamerule doDaylightCycle false");
					Minecraft.getMinecraft().player.sendChatMessage("/gamemode s");
				}
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
				
				for (WorldServer ws : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
					for (EntityPlayer player : (List<EntityPlayer>) ws.playerEntities) {
						
						int pX = (int) player.posX;
						int pY = (int) player.posY;
						int pZ = (int) player.posZ;
						
						for (int xx = pX-doorCheckRadius; xx < pX+doorCheckRadius; ++xx) {
							for (int zz = pZ-doorCheckRadius; zz < pZ+doorCheckRadius; ++zz) {
								for (int yy = pY-doorCheckRadius; yy < pY+doorCheckRadius; ++yy) {
									
									if (ws.getBlockState(new BlockPos(xx,yy,zz)).getBlock() == Blocks.OAK_DOOR) {
										double blockDistance = Math.sqrt(Math.pow(Math.abs(xx-pX), 2) + Math.pow(Math.abs((yy-pY)), 2) + Math.pow(Math.abs(zz-pZ), 2));
										// Open if close
										IBlockState gottenMeta = ws.getBlockState(new BlockPos(xx, yy, zz));
										// TODO: re-write the logic for door being annoying, auto-open in range
//										int meta = gottenMeta;
//										gottenMeta.getBlock()
//
//										if (blockDistance <= doorOpenDistance && (gottenMeta >= 0 && gottenMeta < 4)) {
//											meta += 4;
//											ws.setBlockMetadataWithNotify(xx, yy, zz, meta, 3);
//											ws.playAuxSFX(1003, xx, yy, zz, 0);
//										} else if (blockDistance > doorOpenDistance && (gottenMeta >= 4 && gottenMeta < 8)){
//											meta -= 4;
//											ws.setBlockMetadataWithNotify(xx, yy, zz, meta, 3);
//											ws.playAuxSFX(1003, xx, yy, zz, 0);
//										}
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