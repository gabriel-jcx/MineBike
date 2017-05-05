package org.ngs.bigx.minecraft.client;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXPacketHandler;
import org.ngs.bigx.minecraft.client.area.Area;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistException;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistManager;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.quests.Quest;
import org.ngs.bigx.minecraft.quests.QuestException;
import org.ngs.bigx.minecraft.quests.QuestManager;
import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MouseHelper;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ClientEventHandler {
		private BigxClientContext context;
		public static KeyBinding keyBindingTogglePedalingMode;
		public static KeyBinding keyBindingMoveForward;
		public static KeyBinding keyBindingMoveBackward;
		public static KeyBinding keyBindingToggleMouse;
		public static KeyBinding keyBindingToggleQuestListGui;
		public static KeyBinding keyBindingToggleBike;
		
		private static final double PLAYER_DEFAULTSPEED = 0.10000000149011612D;
		private static final MouseHelper defaultMouseHelper = new MouseHelper();
		
		private static ClientEventHandler handler;
		
		private static int demo =0;
		
		public ClientEventHandler(BigxClientContext con) {
			context = con;
			handler = this;
		}

		public static ClientEventHandler getHandler() {
			return handler;
		}
		
		boolean enableLock = false, enableBike = true;
		
		private boolean showLeaderboard;
		private int leaderboardSeconds;
		
		@SubscribeEvent
		public void onLivingJump(LivingJumpEvent event) {
			if (enableLock)
				event.entity.motionY = 0;
		}
		
		@SubscribeEvent
		public void onKeyInput(KeyInputEvent event) {
			if (Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed()) {
				Minecraft.getMinecraft().thePlayer.setSprinting(false);
			}
			if (keyBindingToggleMouse.isPressed()) {
				enableLock = !enableLock;
				System.out.println("Movement/Look lock: " + enableLock);
				
			}
			if (keyBindingToggleBike.isPressed()) {
				enableBike = !enableBike;
				System.out.println("Toggle Bike Movement: " + enableBike);
			}
			if(keyBindingToggleQuestListGui.isPressed())
			{
				Minecraft mc = Minecraft.getMinecraft();
				GuiQuestlistManager guiQuestlistManager = new GuiQuestlistManager((BigxClientContext)BigxClientContext.getInstance(), mc);
				// TODO populate guiQuestlistManager with playerQuestManager quest list items
//				String[] demoquestreq = new String[5];
//				demoquestreq[0] = "quest[" + demo + "] req 1";
//				demoquestreq[1] = "quest[" + demo + "] req 2";
//				demoquestreq[2] = "quest[" + demo + "] req 3";
//				demoquestreq[3] = "quest[" + demo + "] req 4";
//				demoquestreq[4] = "quest[" + demo + "] req 5";
//				
//				try {
//					guiQuestlistManager.addQuestReference(new Quest(QuestEventChasing.id + demo, "demo chasing quest", "demo chasing quest desc", demoquestreq));
//					demo++;
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//				} catch (GuiQuestlistException e) {
//					e.printStackTrace();
//				}
				
				if(mc.currentScreen == null)
					mc.displayGuiScreen(guiQuestlistManager);
				System.out.println("Display Quest List");
			}
		}
		
		@SubscribeEvent
		public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		}
		
		@SubscribeEvent
		public void entityAttacked(LivingHurtEvent event)
		{
			if(event.entityLiving.getClass().getName().equals(EntityLiving.class.getName()))
			{
				EntityLiving attackedEnt = (EntityLiving) event.entityLiving;
				DamageSource attackSource = event.source;
				if (attackSource.getSourceOfDamage() != null)
				{
					EntityPlayer player = (EntityPlayer) attackSource.getSourceOfDamage();
					if(player.getHeldItem() != null)
					{
						ItemStack itemstack = player.getHeldItem();
						if (itemstack.getDisplayName().equals("Baton"))
						{
							NBTTagCompound tag = itemstack.getTagCompound();
							int damageAmmount = tag.getInteger("Damage");
							event.ammount = damageAmmount;
						}
					}
				}
			}
		}
		
		//Called whenever the client ticks
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public void onClientTick(TickEvent.ClientTickEvent event) {
			if ((Minecraft.getMinecraft().thePlayer!=null) 
					&& (event.phase==TickEvent.Phase.END)) {
				if(context.getQuestManager() == null)
				{
					context.setQuestManager(new QuestManager(Minecraft.getMinecraft().thePlayer));
				}
				
				QuestManager playerQuestManager = context.getQuestManager();
				
				if (playerQuestManager != null && playerQuestManager.getActiveQuestId() != "NONE") {
					try {
						if (playerQuestManager.CheckQuestTaskCompleted()) {
							if (playerQuestManager.CheckActiveQuestCompleted()) {
								// QUEST DONE!
								for (ItemStack i : playerQuestManager.getActiveQuestRewards())
									playerQuestManager.getPlayer().inventory.addItemStackToInventory(i);
								playerQuestManager.getPlayer().addExperience(playerQuestManager.getActiveQuestRewardXP());
								playerQuestManager.setActiveQuest("NONE");
							}
						}
					} catch (QuestException e) {
						e.printStackTrace();
					}
				}
				
				// Handling Player Skills
				EntityPlayer p = Minecraft.getMinecraft().thePlayer;
				// Degrade the current player's speed
				BiGX.characterProperty.decreaseSpeedByTime();
				p.capabilities.setPlayerWalkSpeed(BiGX.characterProperty.getSpeedRate());
				
				//Dealing with locking keys
				if (enableLock) {
					p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0D);
					//Minecraft.getMinecraft().mouseHelper = BiGX.disableMouseHelper;
					p.jumpMovementFactor = 0f;
					p.capabilities.setPlayerWalkSpeed(0f);
					p.capabilities.setFlySpeed(0f);
					p.setJumping(false);
					p.motionX = 0; p.motionZ = 0;
					p.setSprinting(false);
					p.rotationPitch = 0f;
					p.rotationYaw = 0f;
				} else {
					p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(PLAYER_DEFAULTSPEED);
					Minecraft.getMinecraft().mouseHelper = defaultMouseHelper;
					p.capabilities.setFlySpeed(0.05F);
					p.capabilities.setPlayerWalkSpeed(0.1F);
				}
				
				/*
				 * CHARACTER MOVEMENT LOGIC
				 */
				{
					float moveSpeed = context.getSpeed()/4;
					double xt = Math.cos(Math.toRadians(p.getRotationYawHead()+90)) * moveSpeed;
					double zt = Math.sin(Math.toRadians(p.getRotationYawHead()+90)) * moveSpeed;
					if (enableBike)
						p.setVelocity(xt, p.motionY, zt);
				}  ////// END OF "CHARACTER MOVEMENT LOGIC"
				
				
				// Detect if there is area changes where the player is in
				ClientAreaEvent.detectAreaChange(p);
				
				if(ClientAreaEvent.isAreaChange())
				{
					ClientAreaEvent.unsetAreaChangeFlag();

					if (ClientAreaEvent.previousArea.type == Area.AreaTypeEnum.ROOM) {
						if (showLeaderboard) {
							showLeaderboard = false;
							final Timer leaderboardTimer = new Timer();
							
							final TimerTask leaderboardTimerTask = new TimerTask() {
								@Override
								public void run() {
									GuiLeaderBoard.showLeaderBoard(true);
									if (leaderboardSeconds++ >= 5) {
										GuiLeaderBoard.showLeaderBoard(false);
										leaderboardSeconds = 0;
										leaderboardTimer.cancel();
									}
								}
							};
							leaderboardTimer.scheduleAtFixedRate(leaderboardTimerTask, 0, 1000);
						}
					} else {
						showLeaderboard = true;
					}
					if(ClientAreaEvent.previousArea != null)
						GuiMessageWindow.showMessage(ClientAreaEvent.previousArea.name);
					else
						GuiMessageWindow.showMessage("Out of Continent Pangea...");
				}
				
				if( (p.rotationPitch < -45) && (context.getRotationY() < 0) ) {	}
				else if( (p.rotationPitch > 45) && (context.getRotationY() > 0) ) {	}
				else {
					p.rotationPitch += context.getRotationY();
				}
				context.setRotationY(0);
				
				//* EYE TRACKING *//
				//System.out.println("pitch[" + p.rotationPitch + "] yaw[" + p.rotationYaw + "] head[" + p.rotationYawHead + "] X[" + context.getRotationX() + "]");
				if( (context.getRotationX() < .5) && (context.getRotationX() > -.5)) {
					p.rotationYaw += context.getRotationX() / 8;
				}
				else if( (context.getRotationX() < 1.0) && (context.getRotationX() > -1.0)) {
					p.rotationYaw += context.getRotationX() / 4;
				}
				else if( (context.getRotationX() < 1.5) && (context.getRotationX() > -1.5)) {
					p.rotationYaw += context.getRotationX() / 2;
				}
				else {
					p.rotationYaw += context.getRotationX();
				}
				
				context.setRotationX(0);
				
				// Obtain the block under the main character and set the resistance
				Block b = p.getEntityWorld().getBlock((int) p.posX,(int) p.posY-2,(int) p.posZ);
				if (b==Blocks.air) {
					b = p.getEntityWorld().getBlock((int) p.posX, (int) p.posY-3,(int) p.posZ);
				}

				float new_resistance = context.resistance;
				if (b!=null) {
					if (context.resistances.containsKey(b)) {
						new_resistance = context.resistances.get(b).getResistance();
					}
					else{
						new_resistance = 1;
					}
				}
				if (new_resistance!=context.resistance) {
					System.out.println("New resistance old[" + new_resistance + "] new[" + context.resistance + "]");
					context.resistance = new_resistance;
					ByteBuffer buf = ByteBuffer.allocate(5);
					buf.put((byte) 0x00);
					buf.put((byte) ((byte) ((int)context.resistance) & 0xFF));
					buf.put((byte) ((byte) (((int)context.resistance) & 0xFF00)>>8));
					BiGXNetPacket packet = new BiGXNetPacket(org.ngs.bigx.dictionary.protocol.Specification.Command.REQ_SEND_DATA, 0x0100, 
							org.ngs.bigx.dictionary.protocol.Specification.DataType.RESISTANCE, buf.array());
					BiGXPacketHandler.sendPacket(context.bigxclient, packet);
				}
			}
		}
		
		@SubscribeEvent
		public void onGuiOpen(GuiOpenEvent event) {
			if (event.gui instanceof GuiMainMenu) {
				GuiMenu gui = new GuiMenu();
				gui.setContext(context);
				event.gui = gui;
			}
		}
}
