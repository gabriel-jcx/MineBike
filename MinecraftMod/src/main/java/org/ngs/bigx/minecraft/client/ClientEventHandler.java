package org.ngs.bigx.minecraft.client;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.BiGXTextBoxDialogue;
import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.minecraft.bike.IPedalingComboEvent;
import org.ngs.bigx.minecraft.bike.PedalingCombo;
import org.ngs.bigx.minecraft.bike.PedalingToBuild;
import org.ngs.bigx.minecraft.bike.PedalingToBuildEventHandler;
import org.ngs.bigx.minecraft.client.area.Area;
import org.ngs.bigx.minecraft.client.area.Area.AreaTypeEnum;
import org.ngs.bigx.minecraft.client.area.ClientAreaEvent;
import org.ngs.bigx.minecraft.client.gui.GuiBuildinglistManager;
import org.ngs.bigx.minecraft.client.gui.GuiQuestlistException;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiChasingQuest;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiChasingQuestLevelSlot;
import org.ngs.bigx.minecraft.client.gui.quest.chase.GuiChasingQuestLevelSlotItem;
import org.ngs.bigx.minecraft.client.skills.Skill.enumSkillState;
import org.ngs.bigx.minecraft.client.skills.SkillBoostDamage;
import org.ngs.bigx.minecraft.client.skills.SkillBoostMining;
import org.ngs.bigx.minecraft.client.skills.SkillManager;
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
import javafx.scene.shape.DrawMode;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ClientEventHandler implements IPedalingComboEvent {
	private BigxClientContext context;

	public static boolean levelUpSoundPlayFlag = false;
	public static boolean levelDownSoundPlayFlag = false;
	public static boolean gaugeUpSoundPlayFlag = false;
	
	public static KeyBinding keyBindingTogglePedalingMode;
	public static KeyBinding keyBindingMoveForward;
	public static KeyBinding keyBindingMoveBackward;
	public static KeyBinding keyBindingToggleMouse;
	public static KeyBinding keyBindingSwitchSkills;
	public static KeyBinding keyBindingUseSkills;
//	public static KeyBinding keyBindingToggleQuestListGui;
//	public static KeyBinding keyBindingToggleChasingQuestGui;
	public static KeyBinding keyBindingToggleBuildingGui;
	public static KeyBinding keyBindingToggleBike;
	public static KeyBinding keyBindingToggleBikeToMining;
	
	public static int animTickSwitch;
	public static final int animTickSwitchLength = 5;
	public static int animTickFade;
	public static final int animTickFadeLength = 40;
	public static final int animTickFadeTime = 10;
	public static int animTickChasingFade;
	public static final int animTickChasingFadeLength = 20;
	
	private static final double PLAYER_DEFAULTSPEED = 0.10000000149011612D;
	private static final MouseHelper defaultMouseHelper = new MouseHelper();
	
	private static ClientEventHandler handler;
	
	public ClientEventHandler(BigxClientContext con) {
		context = con;
		handler = this;
	}

	public static ClientEventHandler getHandler() {
		return handler;
	}
	
	boolean enableLock = false, enableBike = true;
	public static int pedalingModeState = 0; // 0: moving, 1:mining, 2:building
	public static PedalingCombo pedalingCombo = new PedalingCombo();
	
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
		if (keyBindingToggleBikeToMining.isPressed()) {
//			pedalingModeState ++;
//			pedalingModeState %= 3;
//			animTickSwitch = 0;
//			animTickFade = 0;
//			System.out.println("pedalingModeState[" + pedalingModeState + "]");
		}
		if (keyBindingToggleBike.isPressed()) {
			enableBike = !enableBike;
			System.out.println("Toggle Bike Movement: " + enableBike);
		}
		if(keyBindingToggleBuildingGui.isPressed())
		{
			Minecraft mc = Minecraft.getMinecraft();
			GuiBuildinglistManager guiBuildinglistManager = new GuiBuildinglistManager((BigxClientContext)BigxClientContext.getInstance(), mc);
			
			ArrayList<String> buildingIdList = new ArrayList<String>();
			buildingIdList.add("default.tree");
			buildingIdList.add("default.waterbasin");
			buildingIdList.add("default.bush");
			buildingIdList.add("default.store");
			buildingIdList.add("default.cactus");
			
			try {
				guiBuildinglistManager.addBuildingReferences(buildingIdList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(mc.currentScreen == null)
				mc.displayGuiScreen(guiBuildinglistManager);
			System.out.println("Display Building Id List");
		}
		if(keyBindingSwitchSkills.isPressed()) // "K"
		{
			// SWITCH SKILLs
//			System.out.println("keyBindingSwitchSkills pressed");
			
			context.getCurrentGameState().getSkillManager().switchSkill();
		}
		if(keyBindingUseSkills.isPressed()) // "J"
		{
			// SWITCH SKILLs
//			System.out.println("keyBindingUseSkills pressed");
			
			context.getCurrentGameState().getSkillManager().useCurrentlySelectedSkill();
		}
//		if(keyBindingToggleQuestListGui.isPressed())
//		{
////			System.out.println("view[" + Minecraft.getMinecraft().gameSettings.thirdPersonView + "]");
////			Minecraft.getMinecraft().gameSettings.thirdPersonView ++;
////			Minecraft mc = Minecraft.getMinecraft();
////			GuiQuestlistManager guiQuestlistManager = new GuiQuestlistManager((BigxClientContext)BigxClientContext.getInstance(), mc);
////			
////			guiQuestlistManager.resetQuestReferences();
////			
////			try {
////				Collection<Quest> questlist = context.getQuestManager().getAvailableQuestList().values();
////				
////				for(Quest quest : questlist)
////				{
////					guiQuestlistManager.addQuestReference(quest);
////				}
////			} catch (NullPointerException e) {
////				e.printStackTrace();
////			} catch (GuiQuestlistException e) {
////				e.printStackTrace();
////			}
////			
////			if(mc.currentScreen == null)
////				mc.displayGuiScreen(guiQuestlistManager);
////			System.out.println("Display Quest List");
//		}
//		if (keyBindingToggleChasingQuestGui.isPressed()) {
//			Minecraft mc = Minecraft.getMinecraft();
//			GuiChasingQuest guiChasingQuest = new GuiChasingQuest((BigxClientContext)BigxClientContext.getInstance(), mc);
//			
//			guiChasingQuest.resetChasingQuestLevels();
//			
//			try {
//				for(int i=0; i<GuiChasingQuestLevelSlot.numberOfQuestLevels; i++)
//				{
//					boolean islocked = false;
//					if(i>2)
//						islocked = true;
//					GuiChasingQuestLevelSlotItem guiChasingQuestLevelSlotItem = new GuiChasingQuestLevelSlotItem(i+1, islocked);
//					
//					guiChasingQuest.addChasingQuestLevel(guiChasingQuestLevelSlotItem);
//				}
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//			} catch (GuiQuestlistException e) {
//				e.printStackTrace();
//			}
//			
//			if(mc.currentScreen == null)
//				mc.displayGuiScreen(guiChasingQuest);
//			System.out.println("Display Chasing Quest Gui");
//		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
//			if (!event.world.isRemote && event.entity instanceof EntityPlayerMP) {
//				if (!((EntityPlayerMP)event.entity).inventory.hasItemStack(new ItemStack(Items.wooden_sword))) {
//					((EntityPlayerMP)event.entity).inventory.addItemStackToInventory(new ItemStack(Items.wooden_sword));
//				}
//				if (!((EntityPlayerMP)event.entity).inventory.hasItemStack(new ItemStack(Items.gold_ingot))) {
//					((EntityPlayerMP)event.entity).inventory.addItemStackToInventory(new ItemStack(Items.gold_ingot));
//				}
//			}
	}
	
	@SubscribeEvent
	public void entityAttacked(LivingHurtEvent event)
	{
		if(event.source.getSourceOfDamage() instanceof EntityPlayer)
		{
			if(context.getCurrentGameState().getSkillManager().getSkills().get(1).getSkillState() == enumSkillState.EFFECTIVE)
			{
				event.ammount += SkillBoostDamage.boostRate;
//				System.out.println("Damage Boost["+event.ammount+"]");
			}
		}
		
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
	
	@SubscribeEvent
	public void onBlockDrawHighlight(DrawBlockHighlightEvent event) {
		// What a helpful event for this effect
		
		// BIKE MODE - highlighting the block the player is looking at with a transparent white overlay
		
//		event.context.drawOutlinedBoundingBox(p_147590_0_, p_147590_1_);(AxisAlignedBB, int);
		
//		int minX = event.target.blockX;
//		int maxX = event.target.blockX + 1;
//		int minY = event.target.blockY;
//		int maxY = event.target.blockY + 1;
//		int minZ = event.target.blockZ;
//		int maxZ = event.target.blockZ + 1;
		
		// TODO re-enable and fix when we need to highlight blocks again!
		/*
		GL11.glPushMatrix();
		
		GL11.glTranslatef(event.target.blockX, event.target.blockY, event.target.blockZ);
		
	    Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorRGBA(0, 0, 0, 128);
        
        // -Z
        tessellator.setNormal(0, 0, -1);
        tessellator.addVertex(1.0, 0.0, 0.0);
        tessellator.addVertex(0.0, 0.0, 0.0);
        tessellator.addVertex(0.0, 0.5, 0.0);
        tessellator.addVertex(1.0, 0.5, 0.0);
        // +Z
        tessellator.setNormal(0, 0, 1);
        tessellator.addVertex(0.0, 0.0, 1.0);
        tessellator.addVertex(1.0, 0.0, 1.0);
        tessellator.addVertex(1.0, 0.5, 1.0);
        tessellator.addVertex(0.0, 0.5, 1.0);
        // -X
        tessellator.setNormal(-1, 0, 0);
        tessellator.addVertex(0.0, 0.0, 0.0);
        tessellator.addVertex(0.0, 0.0, 1.0);
        tessellator.addVertex(0.0, 0.5, 1.0);
        tessellator.addVertex(0.0, 0.5, 0.0);
        // +X
        tessellator.setNormal(1, 0, 0);
        tessellator.addVertex(1.0, 0.0, 1.0);
        tessellator.addVertex(1.0, 0.0, 0.0);
        tessellator.addVertex(1.0, 0.5, 0.0);
        tessellator.addVertex(1.0, 0.5, 1.0);
        // -Y
        tessellator.setNormal(0, -1, 0);
        tessellator.addVertex(0.0, 0.0, 1.0);
        tessellator.addVertex(0.0, 0.0, 0.0);
        tessellator.addVertex(1.0, 0.0, 0.0);
        tessellator.addVertex(1.0, 0.0, 1.0);
        // +Y
        tessellator.setNormal(0, 1, 0);
        tessellator.addVertex(1.0, 0.5, 1.0);
        tessellator.addVertex(1.0, 0.5, 0.0);
        tessellator.addVertex(0.0, 0.5, 0.0);
        tessellator.addVertex(0.0, 0.5, 1.0);
        
        tessellator.draw();
		
        GL11.glPopMatrix();
        */
	}
	
	//Called whenever the client ticks
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if ((Minecraft.getMinecraft().thePlayer!=null) 
				&& (event.phase==TickEvent.Phase.END)) {
			
			if(context.getQuestManager() == null)
			{
				context.setQuestManager(new QuestManager(context, Minecraft.getMinecraft().thePlayer));
			}

//			/**
//			 * TODO: Need to remove this testing code from the release version
//			 */
//			// Natural decrease of the pedaling gauge
//			if(ClientEventHandler.pedalingCombo.getGauge() < 10000)
//			{
//				ClientEventHandler.pedalingCombo.increaseGauge(10);
//			}
//			ClientEventHandler.pedalingCombo.decraseGauge();
			
			QuestManager playerQuestManager = context.getQuestManager();
			
			if (playerQuestManager != null && playerQuestManager.getActiveQuestId() != "NONE") {
				try {
					if (playerQuestManager.CheckActiveQuestCompleted()) {
						// QUEST DONE!
						if (playerQuestManager.getActiveQuestRewards() != null)
							for (ItemStack i : playerQuestManager.getActiveQuestRewards())
								playerQuestManager.getPlayer().inventory.addItemStackToInventory(i);
						playerQuestManager.getPlayer().addExperience(playerQuestManager.getActiveQuestRewardXP());
						playerQuestManager.setActiveQuest(Quest.QUEST_ID_STRING_NONE);
					}
				} catch (QuestException e) {
					e.printStackTrace();
				}
			}
		
			/**
			 * Animation for GuiStats bike mode
			 */
			
			if (animTickSwitch < animTickSwitchLength) {
				animTickSwitch++;
			}
			if (animTickFade < animTickFadeLength + animTickFadeTime) {
				animTickFade++;
			}
			if (animTickChasingFade < animTickChasingFadeLength + animTickFadeTime) {
				animTickChasingFade++;
			}
			
			/**
			 * END OF Animation for GuiStats bike mode
			 */
			
			// Handling Player Skills
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			
			if (p.worldObj.rainingStrength > 0.0f) {
				p.worldObj.setRainStrength(0.0f);
			}
			
			// Degrade the current player's speed
	//					BiGX.characterProperty.decreaseSpeedByTime();
	//					p.capabilities.setPlayerWalkSpeed(BiGX.characterProperty.getSpeedRate());
			
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
			
			/***
			 * PEDALING MODE: LEVEL/GAUGE EVENTS
			 */
			if(levelUpSoundPlayFlag)
			{
				levelUpSoundPlayFlag = false;
				p.worldObj.playSoundAtEntity(p, "minebike:pedalinglevelup", 1.0f, 1.0f);
			}

			if(levelDownSoundPlayFlag)
			{
				levelDownSoundPlayFlag = false;
				p.worldObj.playSoundAtEntity(p, "minebike:pedalingleveldown", 1.0f, 1.0f);
			}

			if(gaugeUpSoundPlayFlag)
			{
				gaugeUpSoundPlayFlag = false;
				p.worldObj.playSoundAtEntity(p, "minebike:pedalinggaugeup", 1.0f, 1.0f);
			}
			
			pedalingCombo.decreaseGauge();
			/**
			 * END OF "PEDALING MODE: LEVEL/GAUGE EVENTS"
			 */
			
			// Detect if there is area changes where the player is in
			ClientAreaEvent.detectAreaChange(p);
			
			if(ClientAreaEvent.isAreaChange())
			{
				ClientAreaEvent.unsetAreaChangeFlag();
				
				if(ClientAreaEvent.previousArea != null){
					if (ClientAreaEvent.previousArea.type == Area.AreaTypeEnum.ROOM){
						// (OLD) Tutorial
//						if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.placeFireRoom)
//							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.fireRoomEntrance);
//						if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.placeWaterRoom)
//							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.waterRoomEntrance);
//						if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.placeEarthRoom)
//							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.earthRoomEntrance);
//						if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.placeAirRoom)
//							GuiMessageWindow.showMessage(BiGXTextBoxDialogue.airRoomEntrance);
						// New Tutorial
						if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsPedalForward)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsPedalForward, GuiMessageWindow.PEDAL_FORWARD_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsPedalBackward)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsPedalBackward, GuiMessageWindow.PEDAL_BACKWARD_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsMine)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsMine, GuiMessageWindow.MINE_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsBuild)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsBuild, GuiMessageWindow.BUILD_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsJump)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsJump, GuiMessageWindow.JUMP_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsAttackNPC)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsAttackNPC, GuiMessageWindow.HIT_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsDashJump)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsDashJump, GuiMessageWindow.DASH_JUMP_TEXTURE, false);
						else if (ClientAreaEvent.previousArea.name == BiGXTextBoxDialogue.instructionsDrinkPotion)
							GuiMessageWindow.showMessageAndImage(BiGXTextBoxDialogue.instructionsDrinkPotion, GuiMessageWindow.POTION_TEXTURE, false);
						else
							GuiMessageWindow.showMessage(ClientAreaEvent.previousArea.name);
					}
					else{
						if (ClientAreaEvent.previousArea.type == AreaTypeEnum.EVENT)
							GuiMessageWindow.showMessage(ClientAreaEvent.previousArea.name);
					}
				}
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
	
	/**
	 * Pedaling to Mining
	 * @param event
	 */
	@SubscribeEvent
	public void damagePlayerFromPunching(PlayerEvent.BreakSpeed event)
	{
		if(context.getCurrentGameState().getSkillManager().getSkills().get(2).getSkillState() == enumSkillState.EFFECTIVE)
		{
			event.newSpeed = (float) (event.originalSpeed + SkillBoostMining.boostRate);
//			System.out.println("Mining Boost old["+event.originalSpeed+"] new["+event.newSpeed+"]");
		}
		
		if(pedalingModeState == 1)
		{
			float damage = (((float)this.context.rpm) / 10F) - 3F;
			
			if((float)this.context.rpm != 0)
			{
				damage = (((float)this.context.rpm) / 10F) - 3F;
				damage = damage<0?0:damage;
				damage *= 2;
				damage += event.originalSpeed;
				damage = damage>=15?15:damage;
				event.newSpeed = damage;
			}
		}
		else if(pedalingModeState == 2)
		{
			if(PedalingToBuildEventHandler.buildingId.equals(""))
				return;
			
			PedalingToBuildEventHandler.pedalingToBuild = new PedalingToBuild(event.x, event.y+1, event.z, 9, PedalingToBuildEventHandler.buildingId);
		}
	}

	@Override
	public void onLevelChange(int oldLevel, int newLevel) {
		if(newLevel > oldLevel)
			levelUpSoundPlayFlag = true;
		else
			levelDownSoundPlayFlag = true;
	}

	@Override
	public void onOneGaugeFilled() {
		gaugeUpSoundPlayFlag = true;
	}
}
