package org.ngs.bigx.minecraft.items;
import java.util.List;

import org.ngs.bigx.minecraft.entity.item.JahCoin;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;

/**
 * Advanced Fishing
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemFish extends ItemFood 
{

public ItemFish(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_) {
		super(p_i45339_1_, p_i45339_2_, p_i45339_3_);
		// TODO Auto-generated constructor stub
	}

//	public ItemFish(int heal, float saturation, boolean wolfLike, String name) {
//        super(heal, saturation, wolfLike);
//        this.setUnlocalizedName("ItemFish." + name);
//        this.setTextureName("customnpcs:jahcoin");
//        this.setCreativeTab(CreativeTabs.tabFood);
//    }

//public ItemFish(int heal, float saturation, boolean wolfLike) 
//	{
//		super(heal, saturation, wolfLike);
//	}
//
//public ItemFish(int heal, boolean wolfLike) 
//{
//	this(heal, 0.6F, wolfLike);
//}




//@SideOnly(Side.CLIENT)
//public void getSubItems(CreativeTabs tab, List<ItemStack> items) {
//    if (tab.getTabIndex() == 6) {
//        for (EnumFishType fish : EnumFishType.values()) {
//            items.add(new ItemStack(this, 1, fish.ordinal()));
//        }
//    }
//}

@Override
public int func_150905_g(ItemStack stack) {
	super.func_150905_g(stack);
    return EnumFishType.values()[stack.getItemDamage()].getHealAmount();
}

@Override
public float func_150906_h(ItemStack stack) {
	super.func_150906_h(stack);
    return EnumFishType.values()[stack.getItemDamage()].getSaturationModifier();
}

@Override
protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
    EnumFishType fishType = EnumFishType.values()[stack.getItemDamage()];
	String name = stack.getDisplayName();
    System.out.println(stack.getDisplayName());
    System.out.println(stack.getItemDamage());
    System.out.println(fishType);
    
    if(name.equals("Cursed Koi"))
    {
    	player.addPotionEffect(new PotionEffect(19, 200, 1));
        player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Blaze Pike"))
    {
    	player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
        player.setFire(5);
    }
    else if(name.equals("Bonefish"))
    {
    	player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Spookyfish"))
    {
    	player.addPotionEffect(new PotionEffect(15, 300, 2));
        player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Withered Crucian"))
    {
    	player.addPotionEffect(new PotionEffect(20, 200, 1));
        player.addPotionEffect(new PotionEffect(9, 200, 1));
        player.addPotionEffect(new PotionEffect(17, 300, 2));
    }
    else if(name.equals("Cave Trout"))
    {
    	player.addPotionEffect(new PotionEffect(11, 200, 1));
    }
    else if(name.equals("Mandarinfish"))
    {
    	player.addExperience(20);
    }
//    
//    switch (fishType) {
//        case BLUE_JELLYFISH:
//        	System.out.println("a");
//        	break;
//        case GREEN_JELLYFISH:
//        	System.out.println("b");
//        	break;
//        case CURSED_KOI:
//        	System.out.println("Why");
//            player.addPotionEffect(new PotionEffect(19, 200, 1));
//            player.addPotionEffect(new PotionEffect(9, 200, 1));
//            player.addPotionEffect(new PotionEffect(17, 300, 2));
//            break;
//        case MAGMA_JELLYFISH:
//        	System.out.println("c");
//        	break;
//        case FLAREFIN_KOI:
//        	System.out.println("d");
//        	break;
//        case BLAZE_PIKE:
//        	System.out.println("Please");
//            player.addPotionEffect(new PotionEffect(9, 200, 1));
//            player.addPotionEffect(new PotionEffect(17, 300, 2));
//            player.setFire(5);
//            break;
//        case OBSIDIAN_BREAM:
//        	break;
//        case SANDY_BASS:
//        	break;
//        case MUD_TUNA:
//        	break;
//        case EXPLOSIVE_CRUCIAN:
//        	break;
//        case NETHER_STURGEON:
//        	break;
//        case QUARTZ_CHUB:
//        	break;
//        case ENDER_SHAD:
//        	break;
//        case RED_SHROOMFIN:
//        	break;
//        case BROWN_SHROOMFIN:
//        	break;
//        case FUNGI_CATFISH:
//        	break;
//        case SWAMP_PLAICE:
//        	break;
//        case CRYSTAL_MULLET:
//        	break;
//        case CHARGED_BULLHEAD:
//        	break;
//        case ABYSSAL_LURKER:
//        	break;
//        case MAGIKARP:
//        	break;
//        case BONE_FISH:
//            player.addPotionEffect(new PotionEffect(9, 200, 1));
//            player.addPotionEffect(new PotionEffect(17, 300, 2));
//            break;
//        case SPOOKYFIN:
//            player.addPotionEffect(new PotionEffect(15, 300, 2));
//            player.addPotionEffect(new PotionEffect(9, 200, 1));
//            player.addPotionEffect(new PotionEffect(17, 300, 2));
//            break;
//        case WITHERED_CRUCIAN:
//            player.addPotionEffect(new PotionEffect(20, 200, 1));
//            player.addPotionEffect(new PotionEffect(9, 200, 1));
//            player.addPotionEffect(new PotionEffect(17, 300, 2));
//            break;
//        case CAVE_TROUT:
//            player.addPotionEffect(new PotionEffect(11, 200, 1));
//            break;
//        case MANDARINFISH:
//            player.addExperience(20);
//            break;
//	default:
//		break;
//    }

    super.onFoodEaten(stack, worldIn, player);
}

//@SideOnly(Side.CLIENT)
//public void registerIcons(IIconRegister p_94581_1_)
//{
//    EnumFishType[] afishtype = EnumFishType.values();
//    int i = afishtype.length;
//
//    for (int j = 0; j < i; ++j)
//    {
//        EnumFishType fishtype = afishtype[j];
//        p_94581_1_.registerIcon("customnpcs:" + fishtype.getName());
//    }
//}

//	public static final String name[] = {""};
//	public static final String location[] = {""};
//	public static final String name[] = {""};
//	public enum EnumFishType {
//	    BLUE_JELLYFISH("blue_jellyfish", 1), //0
//	    MAGMA_JELLYFISH("magma_jellyfish", 1), //1
//	    MUD_TUNA("mud_tuna", 2), //2
//	    FROST_MINNOW("frost_minnow", 2), //3
//	    PIRANHA("piranha", 2), //4
//	    GOLDEN_KOI("golden_koi", 2), //5
//	    SPECULAR_SNAPPER("specular_snapper", 2), //6
//	    CAVE_TROUT("cave_trout", 2), //7
//	    OBSIDIAN_BREAM("obsidian_bream", 1), //8
//	    NETHER_STURGEON("nether_sturgeon", 1), //9
//	    QUARTZ_CHUB("quartz_chub", 1), //10
//	    FLAREFIN_KOI("flarefin_koi", 1), //11
//	    BLAZE_PIKE("blaze_pike", 1), //12
//	    ENDER_SHAD("ender_shad", 1), //13
//	    PEARL_SARDINE("pearl_sardine", 2), //14
//	    CHORUS_KOI("chorus_koi", 2), //15
//	    EXPLOSIVE_CRUCIAN("explosive_crucian", 1), //16
//	    RUFFE("ruffe", 2), //17
//	    SPARKLING_EEL("sparkling_eel", 2), //18
//	    ANGELFISH("angelfish", 2), //19
//	    ANGLER_FISH("angler_fish", 2), //20
//	    SPONGE_EATER("sponge_eater", 2), //21
//	    SNOWY_WALLEYE("snowy_walleye", 2), //22
//	    SQUID("squid", 1), //23
//	    WITHERED_CRUCIAN("withered_crucian", 1), //24
//	    SANDY_BASS("sandy_bass", 1), //25
//	    MANDARINFISH("mandarinfish", 3), //26
//	    RED_SHROOMFIN("red_shroomfin", 2), //27
//	    BROWN_SHROOMFIN("brown_shroomfin", 2), //28
//	    FUNGI_CATFISH("fungi_catfish", 1), //29
//	    SWAMP_PLAICE("swamp_plaice", 1), //30
//	    CRYSTAL_MULLET("crystal_mullet", 1), //31
//	    CHARGED_BULLHEAD("charged_bullhead", 1), //32
//	    ABYSSAL_LURKER("abyssal_lurker", 1), //33
//	    SUNFISH("sunfish", 2), //34
//	    GLACIER_ANCHOVY("glacier_anchovy", 1), //35
//	    CATFISH("catfish", 3, 0.2F), //36
//	    PIKE("pike", 2, 0.2F), //37
//	    MAGIKARP("magikarp", 1, 0.05F), //38
//	    GREEN_JELLYFISH("green_jellyfish", 1), //39
//	    BONE_FISH("bone_fish", 1, 0.05F), //40
//	    CURSED_KOI("cursed_koi", 1), //41
//	    SPOOKYFIN("spookyfin", 1); //42
//
//	    private String name;
//	    private int healAmount;
//	    private float saturationModifier;
//	    private ItemFood item;
//	    
//	    EnumFishType(String name, int healAmount) {
//	        this(name, healAmount, 0.1F);
//	    }
//
//	    EnumFishType(String name, int healAmount, float saturationModifier) {
//	        this.name = name;
//	        this.healAmount = healAmount;
//	        this.saturationModifier = saturationModifier;
//	    }
//
////	    EnumFishType()
////	    {
////	    	this.item = new ItemFish(0, 0, false);
//////	    	this.item.setUnlocalizedName("ItemFish." + getName());
//////			this.item.setTextureName("customnpcs:" + getName()).setCreativeTab(CreativeTabs.tabFood);
////	    }
////	    
////	    EnumFishType(String name, int healAmount) {
////	        this(name, healAmount, 0.1F);
////	        this.item = new ItemFish(healAmount, .6F, false);
//////	        this.item.setUnlocalizedName("ItemFish." + getName());
//////			this.item.setTextureName("customnpcs:" + getName()).setCreativeTab(CreativeTabs.tabFood);
////	    }
////
////	    EnumFishType(String name, int healAmount, float saturationModifier) {
////	        this.name = name;
////	        this.healAmount = healAmount;
////	        this.saturationModifier = saturationModifier;
////	        this.item = new ItemFish(healAmount, saturationModifier, false);
//////	        this.item.setUnlocalizedName("ItemFish." + getName());
//////			this.item.setTextureName("customnpcs:magikarp");
////	    }
//	    
//	    public Item getItem()
//	    {
//	    	return this.item;
//	    }
//
//	    public String getName() {
//	        return this.name;
//	    }
//
//	    public int getHealAmount() {
//	        return healAmount;
//	    }
//
//	    public float getSaturationModifier() {
//	        return saturationModifier;
//	    }
//	}
//	
//	
//	
//   
//    @SideOnly(Side.CLIENT)
//    public void getSubItems(CreativeTabs tab, List<ItemStack> items) {
//        if (tab.getTabIndex() == 6) {
//            for (EnumFishType fish : EnumFishType.values()) {
//                items.add(new ItemStack(this, 1, fish.ordinal()));
//            }
//        }
//    }
//
//    @Override
//    public int func_150905_g(ItemStack stack) {
//    	super.func_150905_g(stack);
//        return EnumFishType.values()[stack.getItemDamage()].getHealAmount();
//    }
//
//    @Override
//    public float func_150906_h(ItemStack stack) {
//    	super.func_150906_h(stack);
//        return EnumFishType.values()[stack.getItemDamage()].getSaturationModifier();
//    }
//    
//    @Override
//    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
//        ItemFish.EnumFishType fishType = ItemFish.EnumFishType.values()[stack.getItemDamage()];
//    	
//        switch (fishType) {
//            case BLUE_JELLYFISH:
//            case GREEN_JELLYFISH:
//            case CURSED_KOI:
//                player.addPotionEffect(new PotionEffect(19, 200, 1));
//                player.addPotionEffect(new PotionEffect(9, 200, 1));
//                player.addPotionEffect(new PotionEffect(17, 300, 2));
//                break;
//            case MAGMA_JELLYFISH:
//            case FLAREFIN_KOI:
//            case BLAZE_PIKE:
//                player.addPotionEffect(new PotionEffect(9, 200, 1));
//                player.addPotionEffect(new PotionEffect(17, 300, 2));
//                player.setFire(5);
//                break;
//            case OBSIDIAN_BREAM:
//            case SANDY_BASS:
//            case MUD_TUNA:
//            case EXPLOSIVE_CRUCIAN:
//            case NETHER_STURGEON:
//            case QUARTZ_CHUB:
//            case ENDER_SHAD:
//            case RED_SHROOMFIN:
//            case BROWN_SHROOMFIN:
//            case FUNGI_CATFISH:
//            case SWAMP_PLAICE:
//            case CRYSTAL_MULLET:
//            case CHARGED_BULLHEAD:
//            case ABYSSAL_LURKER:
//            case MAGIKARP:
//            case BONE_FISH:
//                player.addPotionEffect(new PotionEffect(9, 200, 1));
//                player.addPotionEffect(new PotionEffect(17, 300, 2));
//                break;
//            case SPOOKYFIN:
//                player.addPotionEffect(new PotionEffect(15, 300, 2));
//                player.addPotionEffect(new PotionEffect(9, 200, 1));
//                player.addPotionEffect(new PotionEffect(17, 300, 2));
//                break;
//            case WITHERED_CRUCIAN:
//                player.addPotionEffect(new PotionEffect(20, 200, 1));
//                player.addPotionEffect(new PotionEffect(9, 200, 1));
//                player.addPotionEffect(new PotionEffect(17, 300, 2));
//                break;
//            case CAVE_TROUT:
//                player.addPotionEffect(new PotionEffect(11, 200, 1));
//                break;
//            case MANDARINFISH:
//                player.addExperience(20);
//                break;
//		default:
//			break;
//        }
//
//        super.onFoodEaten(stack, worldIn, player);
//    }

//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister p_94581_1_)
//    {
//        EnumFishType[] afishtype = EnumFishType.values();
//        int i = afishtype.length;
//
//        for (int j = 0; j < i; ++j)
//        {
//            EnumFishType fishtype = afishtype[j];
//            p_94581_1_.registerIcon("customnpcs:" + fishtype.getName());
//        }
//    }
    
//    @Override
//    public String getUnlocalizedName(ItemStack stack) {
//        return "item.advanced-fishing." + EnumFishType.values()[stack.getItemSpriteNumber()].getName();
//    }
}