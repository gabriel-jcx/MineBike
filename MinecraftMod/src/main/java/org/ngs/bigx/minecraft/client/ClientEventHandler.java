package org.ngs.bigx.minecraft.client;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import net.minecraft.client.audio.Sound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import org.ngs.bigx.dictionary.objects.game.BiGXGameTag;
import org.ngs.bigx.dictionary.protocol.Specification;
import org.ngs.bigx.minecraft.BiGX;
import org.ngs.bigx.minecraft.CommonEventHandler;
import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.minecraft.bike.IPedalingComboEvent;
import org.ngs.bigx.minecraft.bike.PedalingCombo;

import org.ngs.bigx.minecraft.client.skills.Skill.enumSkillState;
import org.ngs.bigx.minecraft.client.skills.SkillBoostDamage;
import org.ngs.bigx.minecraft.client.skills.SkillBoostMining;
import org.ngs.bigx.minecraft.context.BigxClientContext;
import org.ngs.bigx.minecraft.context.BigxContext;
import org.ngs.bigx.minecraft.context.BigxServerContext;
import org.ngs.bigx.minecraft.context.BigxContext.LOGTYPE;

import org.ngs.bigx.net.gameplugin.common.BiGXNetPacket;
import org.ngs.bigx.net.gameplugin.exception.BiGXInternalGamePluginExcpetion;
import org.ngs.bigx.net.gameplugin.exception.BiGXNetException;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
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
	public static KeyBinding keyBindingToggleControllerInstructionMenu;
	public static KeyBinding keyBindingToggleAlchemyMenu;

	public static KeyBinding keyBindingSwitchToLeftItem;
	public static KeyBinding keyBindingSwitchToRightItem;	
	
	public static int animTickSwitch;
	public static final int animTickSwitchLength = 5;
	public static int animTickFade;
	public static final int animTickFadeLength = 40;
	public static final int animTickFadeTime = 10;
	public static int animTickChasingFade;
	public static final int animTickChasingFadeLength = 20;

	private static int gaugeTaggingTickCount = 0;
	private static int hungerTickCount = 0;
	
	private static final int gaugeTaggingTickCountMax = 5*10*3; // Every Three Seconds
	private static final int hungerTickCountMax = 5*10*10; // Every Ten Seconds
	
	private static final double PLAYER_DEFAULTSPEED = 0.10000000149011612D;

	@SideOnly(Side.CLIENT)
	private static MouseHelper defaultMouseHelper;
	
	public static boolean flagOpenChapterGui = false;
	public static boolean flagChapterCorrectionFromLoading = false;

	public static boolean flagOverrideResistance = false;
	public float overrideResistanceValue = 0;
	
	private static ClientEventHandler handler;
	private String previousSong;
	private ArrayList<String> previousSongs = new ArrayList();
	
	public ClientEventHandler(BigxClientContext con) {
		defaultMouseHelper = new MouseHelper();
		context = con;
		handler = this;
	}

	public static ClientEventHandler getHandler() {
		return handler;
	}

	// set enable bike to true if using bike
	boolean enableLock = false, enableBike = true;
	public static int pedalingModeState = 0; // 0: moving, 1:mining, 2:building
	public static PedalingCombo pedalingCombo = new PedalingCombo();
	
	private boolean showLeaderboard;
	private int leaderboardSeconds;

	private long previousLocationLogTimeStamp = System.currentTimeMillis();
	private static Object previousLocationLogTimeStampLock = new Object();

	public static double playerWorldHeight;

	// elevation resistance data
	public long timer_previous = System.currentTimeMillis();
	public int upChange = 0;
	public int downChange = 0;
	public int previousElevationType = 0;
	public float elevationResistance = 0;

	
	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent event) {
		if (enableLock)
			event.getEntity().motionY = 0;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(KeyInputEvent event) 
	{

		if (Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed()) {
			Minecraft.getMinecraft().player.setSprinting(false);
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
			//enableBike = !enableBike;
			//System.out.println("Toggle Bike Movement: " + enableBike);
		}
//		if(keyBindingToggleBuildingGui.isPressed())
//		{
//			Minecraft mc = Minecraft.getMinecraft();
//			GuiBuildinglistManager guiBuildinglistManager = new GuiBuildinglistManager((BigxClientContext)BigxClientContext.getInstance(), mc);
//			
//			ArrayList<String> buildingIdList = new ArrayList<String>();
//			buildingIdList.add("default.tree");
//			buildingIdList.add("default.waterbasin");
//			buildingIdList.add("default.bush");
//			buildingIdList.add("default.store");
//			buildingIdList.add("default.cactus");
//			
//			try {
//				guiBuildinglistManager.addBuildingReferences(buildingIdList);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if(mc.currentScreen == null)
//				mc.displayGuiScreen(guiBuildinglistManager);
//			System.out.println("Display Building Id List");
//		}
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
		if (keyBindingSwitchToLeftItem.isPressed()) {
			Minecraft mc = Minecraft.getMinecraft();
			
			if(mc.player != null)
			{
				EntityPlayerSP entPlayerSP = mc.player;

				entPlayerSP.inventory.currentItem --;
				
				if(entPlayerSP.inventory.currentItem < 0)
				{
					entPlayerSP.inventory.currentItem = 8;
				}
			}
		}
		if (keyBindingSwitchToRightItem.isPressed()) {
			Minecraft mc = Minecraft.getMinecraft();
			
			if(mc.player != null)
			{
				EntityPlayerSP entPlayerSP = mc.player;
				
				entPlayerSP.inventory.currentItem ++;
				
				if(entPlayerSP.inventory.currentItem > 8)
				{
					entPlayerSP.inventory.currentItem = 0;
				}
			}		
		}
	}
	

	
	@SubscribeEvent
	public void entityAttacked(LivingHurtEvent event)
	{
		if(event.getSource().getTrueSource() instanceof EntityPlayer)
		{
			if(context.getCurrentGameState().getSkillManager().getSkills().get(1).getSkillState() == enumSkillState.EFFECTIVE)
			{
				event.setAmount(event.getAmount()+(float)SkillBoostDamage.boostRate);
				//event.ammount += SkillBoostDamage.boostRate;
//				System.out.println("Damage Boost["+event.ammount+"]");
			}
		}
		
		if(event.getEntityLiving().getClass().getName().equals(EntityLiving.class.getName()))
		{
			EntityLiving attackedEnt = (EntityLiving) event.getEntityLiving();
			DamageSource attackSource = event.getSource();
			if (attackSource.getTrueSource() != null)
			{
				EntityPlayer entPlayer = (EntityPlayer) attackSource.getTrueSource();
				if(entPlayer.getHeldItem(EnumHand.MAIN_HAND) != null)
				{
					ItemStack itemstack = entPlayer.getHeldItem(EnumHand.MAIN_HAND);
					if (itemstack.getDisplayName().equals("Baton"))
					{
						NBTTagCompound tag = itemstack.getTagCompound();
						int damageAmmount = tag.getInteger("Damage");
						event.setAmount(damageAmmount);
					}
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if ((Minecraft.getMinecraft().player != null)
				&& (event.phase == TickEvent.Phase.END)) {

			// create the player entity representing the found player
			EntityPlayer entPlayer = Minecraft.getMinecraft().player;

			// animation for GuiStats bike mode
			animateBikeGuiStats();

			// adjust player stats based on weather
			adjustPlayerWeatherStats(entPlayer);

			// Degrade the current player's speed
			//	BiGX.characterProperty.decreaseSpeedByTime();
			//	p.capabilities.setPlayerWalkSpeed(BiGX.characterProperty.getSpeedRate());

			// handle the locking keys
			handleLockingKeys(entPlayer);

			/**
			 * CHARACTER MOVEMENT LOGIC
			 */
			// check if the bike is enabled. if so, adjust the player's speed according to set bike values
			if(enableBike)
				adjustBikeSpeed(entPlayer);

			// handle Pedaling mode: Level/Gauge Events
			adjustPedalingAttributes(entPlayer);

			// Detect if there is area changes where the player is in
			// NOTE: DELETED THE PLAYER MUSIC CODE
			adjustRotation(entPlayer);
			/**
			 * END OF CHARACTER MOVEMENT LOGIC
			 */

			// adjustHungerBar();

			/**
			 * PEDAL RESISTANCE LOGIC
			 */
			// set the pedaling resistance
			// this variable will be used as the argument for the resistance change
//			float resistance;
//
//			// if we are not overriding the resistance elsewhere in MineBike, use the block/incline resistance
//			if(!flagOverrideResistance) {
//				resistance = getResistance(entPlayer);
//			}
//			// else, get the overriden resistance value set by the public static function "setOverrideResistanceValue"
//			else
//			{
//				resistance = getOverrideResistance();
//			}
//
//			updateResistance(resistance);
			/**
			 * END OF PEDAL RESISTANCE LOGIC
			 */
		}
	}

	/**
	 * onClientTick Methods
	 */
	public void animateBikeGuiStats()
	{
		if (animTickSwitch < animTickSwitchLength) {
			animTickSwitch++;
		}
		if (animTickFade < animTickFadeLength + animTickFadeTime) {
			animTickFade++;
		}
		if (animTickChasingFade < animTickChasingFadeLength + animTickFadeTime) {
			animTickChasingFade++;
		}
	}

	public void adjustPlayerWeatherStats(EntityPlayer inEntPlayer)
	{
		if (inEntPlayer.world.rainingStrength > 0.0f) {
			inEntPlayer.world.setRainStrength(0.0f);
		}
	}

	public void handleLockingKeys(EntityPlayer inEntPlayer)
	{
		if(enableLock){
			inEntPlayer.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
			//Minecraft.getMinecraft().mouseHelper = BiGX.disableMouseHelper;
			inEntPlayer.jumpMovementFactor = 0f;
			inEntPlayer.capabilities.setPlayerWalkSpeed(0f);
			inEntPlayer.capabilities.setFlySpeed(0f);
			inEntPlayer.setJumping(false);
			inEntPlayer.motionX = 0; inEntPlayer.motionZ = 0;
			inEntPlayer.setSprinting(false);
			inEntPlayer.rotationPitch = 0f;
			inEntPlayer.rotationYaw = 0f;
		} else {
			inEntPlayer.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(PLAYER_DEFAULTSPEED);
			if(inEntPlayer.world.isRemote)
				Minecraft.getMinecraft().mouseHelper = defaultMouseHelper;
			inEntPlayer.capabilities.setFlySpeed(0.05F);
			inEntPlayer.capabilities.setPlayerWalkSpeed(0.1F);
		}
	}

	public void adjustBikeSpeed(EntityPlayer inEntPlayer)
	{
		float moveSpeed = context.getSpeed() / 4;
		double xt = Math.cos(Math.toRadians(inEntPlayer.getRotationYawHead() + 90)) * moveSpeed;
		double zt = Math.sin(Math.toRadians(inEntPlayer.getRotationYawHead() + 90)) * moveSpeed;
		inEntPlayer.setVelocity(xt, inEntPlayer.motionY, zt);
	}

	public void adjustPedalingAttributes(EntityPlayer inEntPlayer)
	{
		if(levelUpSoundPlayFlag)
		{
			levelUpSoundPlayFlag = false;
			SoundEvent se = new SoundEvent(new ResourceLocation("minebike:pedalinglevelup"));
			inEntPlayer.playSound(se, 1.0f, 1.0f);
			//inEntPlayer.world.playSoundAtEntity(inEntPlayer, "minebike:pedalinglevelup", 1.0f, 1.0f);
		}

		if(levelDownSoundPlayFlag)
		{
			levelDownSoundPlayFlag = false;
			SoundEvent se = new SoundEvent(new ResourceLocation("minebike:pedalingleveldown"));
			inEntPlayer.playSound(se,1.0f,1.0f);
			//inEntPlayer.world.playSound(inEntPlayer,inEntPlayer.getPosition(),se,SoundCategory.,1.0f,1.0f );
			//inEntPlayer.world.playSoundAtEntity(inEntPlayer, "minebike:pedalingleveldown", 1.0f, 1.0f);
		}

		if(gaugeUpSoundPlayFlag)
		{
			gaugeUpSoundPlayFlag = false;
			SoundEvent se = new SoundEvent(new ResourceLocation("minebike:pedalinggaugeup"));
			inEntPlayer.playSound(se, 1.0f, 1.0f);
			//inEntPlayer.world.playSoundAtEntity(inEntPlayer "minebike:pedalinggaugeup", 1.0f, 1.0f);
		}

		// Pedaling Combo
		pedalingCombo.decreaseGauge();

		gaugeTaggingTickCount++;

		if(gaugeTaggingTickCount == gaugeTaggingTickCountMax)
		{
			gaugeTaggingTickCount = 0;

			sendGaugeGameTag(pedalingCombo.getGaugePercentage());
		}
		// end of Pedaling Combo
	}

	public void adjustHungerBar()
	{
		//	NOTE: the following logic set the hunger bar
		//	hungerTickCount++;
		//
		//	if(hungerTickCount == hungerTickCountMax)
		//	{
		//		hungerTickCount = 0;
		//
		//		Minecraft.getMinecraft().player.sendChatMessage("/effect @p 23 2 255");
		//	}
	}

	public void adjustRotation(EntityPlayer inEntPlayer)
	{
		if( (inEntPlayer.rotationPitch < -45) && (context.getRotationY() < 0) ) {	}
		else if( (inEntPlayer.rotationPitch > 45) && (context.getRotationY() > 0) ) {	}
		else {
			inEntPlayer.rotationPitch += context.getRotationY();
		}
		context.setRotationY(0);

		//* EYE TRACKING *//
		//System.out.println("pitch[" + p.rotationPitch + "] yaw[" + p.rotationYaw + "] head[" + p.rotationYawHead + "] X[" + context.getRotationX() + "]");
		if( (context.getRotationX() < .5) && (context.getRotationX() > -.5)) {
			inEntPlayer.rotationYaw += context.getRotationX() / 8;
		}
		else if( (context.getRotationX() < 1.0) && (context.getRotationX() > -1.0)) {
			inEntPlayer.rotationYaw += context.getRotationX() / 4;
		}
		else if( (context.getRotationX() < 1.5) && (context.getRotationX() > -1.5)) {
			inEntPlayer.rotationYaw += context.getRotationX() / 2;
		}
		else {
			inEntPlayer.rotationYaw += context.getRotationX();
		}

		context.setRotationX(0);
	}

	public float getResistance(EntityPlayer inEntPlayer)
	{
		Block current_block = inEntPlayer.getEntityWorld().getBlockState(new BlockPos((int) inEntPlayer.posX,
											(int) inEntPlayer.posY-2,(int) inEntPlayer.posZ)).getBlock();


		if (current_block == Blocks.AIR) {
			current_block = inEntPlayer.getEntityWorld().getBlockState(new BlockPos((int) inEntPlayer.posX,
											(int) inEntPlayer.posY-3,(int) inEntPlayer.posZ)).getBlock();
		}

		// set a temporary resistance value = current resistance
		float newBlockResistance = context.resistance;

		// enter if a block was successfully read
		if (current_block != null) {
			// if the read block has a resistance value attached to it, get it
			if (context.resistances.containsKey(current_block)) {
				newBlockResistance = context.resistances.get(current_block).getResistance();
			}
			// the read block was not attached to a resistance. set to default resistance
			else{
				newBlockResistance = 1;
			}
		}

		// get a resistance value based on change in elevation
		getElevationResistance(inEntPlayer);

		// add the elevation resistance to the block resistance
		float tempResistance = newBlockResistance + elevationResistance;

		// if combined resistance is greater than max allowed, set to max
		if(tempResistance > 5)
			tempResistance = 5;
		// else if combined resistance is less than minimum allowed, set to minimum
		else if(tempResistance < 0)
			tempResistance = 0;

		// return the combined block + elevation resistance
		return tempResistance;
	}

	public void getElevationResistance(EntityPlayer inEntPlayer)
	{
		long timer_current =  System.currentTimeMillis();

		// every 1.5 seconds, enter and read the elevation change
		if(timer_current - timer_previous >= 1500)
		{
			double tempPlayerHeight = playerWorldHeight;  // temp_height = old_height
			playerWorldHeight = inEntPlayer.posY; // old_height = current height

			// check if the player increased elevation
			if((playerWorldHeight - tempPlayerHeight) > 0) {
				// only if the elevation change has remained for at least 2 entries (3 seconds), change the resistance
				if(previousElevationType == 2) {
					if(upChange < 3)
						upChange++;

					// increment any built up downhill changes in case the player suddenly goes back down hill
					// so it will be a more gradual change
					if(downChange < 0)
						downChange++;

					// store the new elevation resistance
					elevationResistance = upChange;
				}

				// 2 == previously increased in elevation
				previousElevationType = 2;
			}
			// check if player has decreased elevation
			else if ((playerWorldHeight - tempPlayerHeight) < 0) {
				// only if the elevation change has remained for at least 2 entries (3 seconds), change the resistance
				if(previousElevationType == 1) {
					// decrement the downhill change
					if(downChange > -3)
						downChange--;

					// decrement any built up up hill changes in case the player suddenly goes back up hill
					// so it will be a more gradual change
					if(upChange > 0)
						upChange--;

					// store the new elevation resistance
					elevationResistance = downChange;
				}

				// 1 == previously decreased in elevation
				previousElevationType = 1;
			}
			else if((playerWorldHeight - tempPlayerHeight) == 0){
				// only if the elevation change has remained for at least 2 entries (3 seconds), change the resistance
				if(previousElevationType == 0) {
					if(upChange > 0)
						upChange--;
					if(downChange < 0)
						downChange++;

					elevationResistance = 0;
				}

				// 0 == previously no change in elevation (flat ground)
				previousElevationType = 0;
			}

			timer_previous = System.currentTimeMillis();
			// System.out.println("Elevation Resistance: " + elevationResistance);
		}
	}

	public void updateResistance(float inResistance)
	{
		// if the incoming resistance value is different than the current resistance, update the current resistance
		if (inResistance != context.resistance) {
			System.out.println("Changing Resistance -- Old[" + context.resistance + "] -- new[" + inResistance + "]");
			context.resistance = inResistance;
			ByteBuffer buf = ByteBuffer.allocate(5);
			buf.put((byte) 0x00);
			buf.put((byte) ((byte) ((int)context.resistance) & 0xFF));
			buf.put((byte) ((byte) (((int)context.resistance) & 0xFF00)>>8));
			BiGXNetPacket packet = new BiGXNetPacket(org.ngs.bigx.dictionary.protocol.Specification.Command.REQ_SEND_DATA, 0x0100,
					org.ngs.bigx.dictionary.protocol.Specification.DataType.RESISTANCE, buf.array());
			BiGXPacketHandler.sendPacket(context.bigxclient, packet);

			sendResistanceGameTag((int)context.resistance);
		}
	}

	public float getOverrideResistance()
	{
		return overrideResistanceValue;
	}

	public void setOverrideResistanceValue(Float inResistance)
	{
		overrideResistanceValue = inResistance;
	}
	/**
	 * End of onClientTick methods
	 */

	private void addTheCurrenTrack(String songToBeAdded) {
		synchronized (previousSongs) {
			previousSongs.add(songToBeAdded);	
		}
	}

	private void stopPreviousTracks(String chosenSong) {
		synchronized (previousSongs) {
			for(String previousSong : previousSongs)
			{
				if(!chosenSong.equals(previousSong))
				{
					Minecraft.getMinecraft().player.sendChatMessage("/playsoundb " + previousSong + " stop");
					System.out.println("bigx Song["+previousSong+"]");
				}
			}
			
			previousSongs.clear();	
		}
	}

	public static void sendResistanceGameTag(int resistanceId)
	{
		// SEND GAME TAG - Quest 0x(GAME TAG[0xFF])(questActivityTagEnum [0xF])
		try {
			int resistanceTypeEnum = (0xfff & resistanceId);
			BiGXGameTag biGXGameTag = new BiGXGameTag();
			biGXGameTag.setTagName("" + (Specification.GameTagType.GAMETAG_ID_RESISTANCE_BEGINNING | resistanceTypeEnum));
			
			BigxClientContext.sendGameTag(biGXGameTag);
			
			BigxContext.logWriter(LOGTYPE.TAG, "" + Specification.GameTagType.GAMETAG_ID_RESISTANCE_BEGINNING + "\t" + resistanceTypeEnum);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (BiGXNetException e) {
			e.printStackTrace();
		} catch (BiGXInternalGamePluginExcpetion e) {
			e.printStackTrace();
		}
	}

	public static void sendGaugeGameTag(int gaugePercentage)
	{
		// SEND GAME TAG - Quest 0x(GAME TAG[0xFF])(questActivityTagEnum [0xF])
		try {
			gaugePercentage = gaugePercentage<0?0:gaugePercentage;
			gaugePercentage = gaugePercentage>100?100:gaugePercentage;
			
			int locationTypeEnum = (0xfff & gaugePercentage);
			BiGXGameTag biGXGameTag = new BiGXGameTag();
			biGXGameTag.setTagName("" + (Specification.GameTagType.GAMETAG_ID_GAUGE_BEGINNING | locationTypeEnum));
			
			BigxClientContext.sendGameTag(biGXGameTag);
			
			BigxContext.logWriter(LOGTYPE.TAG, "" + Specification.GameTagType.GAMETAG_ID_GAUGE_BEGINNING + "\t" + locationTypeEnum);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (BiGXNetException e) {
			e.printStackTrace();
		} catch (BiGXInternalGamePluginExcpetion e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
//		if (event.getGui() instanceof GuiMainMenu) {
//			GuiMenu gui = new GuiMenu();
//			gui.setContext(context);
//			event.setGui(gui);
//		}

	}

	/**
	 * Pedaling to Mining
	 * @param event
	 */
	@SubscribeEvent
	public void damagePlayerFromPunching(PlayerEvent.BreakSpeed event)
	{
//		if(context.getCurrentGameState().getSkillManager().getSkills().get(2).getSkillState() == enumSkillState.EFFECTIVE)
//		{
//			event.setNewSpeed((float) (event.getOriginalSpeed() + SkillBoostMining.boostRate));
////			System.out.println("Mining Boost old["+event.originalSpeed+"] new["+event.newSpeed+"]");
//		}
		
		if(pedalingModeState == 1)
		{
			float damage = (((float)this.context.rpm) / 10F) - 3F;
			
			if((float)this.context.rpm != 0)
			{
				damage = (((float)this.context.rpm) / 10F) - 3F;
				damage = damage<0?0:damage;
				damage *= 2;
				damage += event.getOriginalSpeed();
				damage = damage>=15?15:damage;
				event.setNewSpeed(damage);
			}
		}
		else if(pedalingModeState == 2)
		{
//			if(PedalingToBuildEventHandler.buildingId.equals(""))
//				return;
//			BlockPos pos = event.getPos();
//			PedalingToBuildEventHandler.pedalingToBuild = new PedalingToBuild(pos.getX(), pos.getY()+1, pos.getZ(), 9, PedalingToBuildEventHandler.buildingId);
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
