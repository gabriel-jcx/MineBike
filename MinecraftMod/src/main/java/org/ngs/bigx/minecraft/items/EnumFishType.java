package org.ngs.bigx.minecraft.items;

import net.minecraft.item.ItemStack;

/**
 * CREDIT TO NightKosh FOR THE CUSTOM FISH AND CODE
 */

//Stores all the Custom Fish
public enum EnumFishType {
	    BLUE_JELLYFISH("blue_jellyfish", 0, 4, 1), //0
	    MAGMA_JELLYFISH("magma_jellyfish", 6, 7, 1), //1
	    MUD_TUNA("mud_tuna", 0, 4, 2), //2
	    FROST_MINNOW("frost_minnow", 3, 7, 2), //3
	    PIRANHA("piranha", 2, 7, 2), //4
	    GOLDEN_KOI("golden_koi", 4, 3, 2), //5
	    SPECULAR_SNAPPER("specular_snapper", 1, 6, 2), //6
	    CAVE_TROUT("cave_trout", 1, 7, 2), //7
	    OBSIDIAN_BREAM("obsidian_bream", 2, 3, 1), //8
	    NETHER_STURGEON("nether_sturgeon", 6, 7, 1), //9
	    QUARTZ_CHUB("quartz_chub", 0, 4, 1), //10
	    FLAREFIN_KOI("flarefin_koi", 4, 7, 1), //11
	    BLAZE_PIKE("blaze_pike", 6, 6, 1), //12
	    ENDER_SHAD("ender_shad", 2, 7, 1), //13
	    PEARL_SARDINE("pearl_sardine", 1, 6, 2), //14
	    CHORUS_KOI("chorus_koi", 4, 6, 2), //15
	    EXPLOSIVE_CRUCIAN("explosive_crucian", 2, 6, 1), //16
	    RUFFE("ruffe", 1, 7, 2), //17
	    SPARKLING_EEL("sparkling_eel", 5, 7, 2), //18
	    ANGELFISH("angelfish", 5, 6, 2), //19
	    ANGLER_FISH("angler_fish", 5, 3, 2), //20
	    SPONGE_EATER("sponge_eater", 3, 7, 2), //21
	    SNOWY_WALLEYE("snowy_walleye", 3, 6, 2), //22
	    SQUID("squid", 5, 7, 1), //23
	    WITHERED_CRUCIAN("withered_crucian", 0, 4, 1), //24
	    SANDY_BASS("sandy_bass", 3, 6, 1), //25
	    MANDARINFISH("mandarinfish", 3, 3, 3), //26
	    RED_SHROOMFIN("red_shroomfin", 6, 7, 2), //27
	    BROWN_SHROOMFIN("brown_shroomfin", 0, 4, 2), //28
	    FUNGI_CATFISH("fungi_catfish", 6, 6, 1), //29
	    SWAMP_PLAICE("swamp_plaice", 1, 7, 1), //30
	    CRYSTAL_MULLET("crystal_mullet", 5, 6, 1), //31
	    CHARGED_BULLHEAD("charged_bullhead", 6, 3, 1), //32
	    ABYSSAL_LURKER("abyssal_lurker", 5, 7, 1), //33
	    SUNFISH("sunfish", 4, 6, 2), //34
	    GLACIER_ANCHOVY("glacier_anchovy", 3, 7, 1), //35
	    CATFISH("catfish", 4, 7, 3, 0.2F), //36
	    PIKE("pike", 0, 4, 2, 0.2F), //37
	    MAGIKARP("magikarp", 1, 3, 1, 0.05F), //38
	    GREEN_JELLYFISH("green_jellyfish", 0, 4, 1), //39
	    BONE_FISH("bone_fish", 2, 6, 1, 0.05F), //40
	    CURSED_KOI("cursed_koi", 4, 7, 1), //41
	    SPOOKYFIN("spookyfin", 2, 7, 1); //42

	    private String name;
	    /*The type and spot where fish is located
	     * 
	     */
	    private int type;
	    //Rarity of fish, Bigger the weight = higher chance of getting fish
	    private int weight;
	    
	    //Health added when eaten
	    private int healAmount;
	    
	    //The saturation the given fish has
	    private float saturationModifier;
	    
	    EnumFishType(String name, int type, int weight, int healAmount) {
	        this(name, type, weight, healAmount, 0.1F);
	    }

	    EnumFishType(String name, int type, int weight, int healAmount, float saturationModifier) {
	        this.name = name;
	        this.type = type;
	        this.weight = weight;
	        this.healAmount = healAmount;
	        this.saturationModifier = saturationModifier;
	    }

	    public String getName() {
	        return this.name;
	    }
	    
	    public int getType() {
	        return type;
	    }
	    
	    public int getWeight() {
	        return weight;
	    }

	    public int getHealAmount() {
	        return healAmount;
	    }

	    public float getSaturationModifier() {
	        return saturationModifier;
	    }
	}
	
