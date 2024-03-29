package net.textstack.band_of_gigantism.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BOGConfig {
    public static final ForgeConfigSpec SPEC;
    public static final BOGConfig INSTANCE;
    static {
        Pair<BOGConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BOGConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }

    //general
    public final ForgeConfigSpec.IntValue scale_speed;
    public final ForgeConfigSpec.BooleanValue description_enable;
    public final ForgeConfigSpec.BooleanValue multiply_enable;
    public final ForgeConfigSpec.DoubleValue general_scale_limit;
    public final ForgeConfigSpec.BooleanValue recovery_allhits;
    public final ForgeConfigSpec.IntValue recovery_duration;
    public final ForgeConfigSpec.DoubleValue recovery_chance;
    public final ForgeConfigSpec.DoubleValue recovery_minimum_damage;
    public final ForgeConfigSpec.BooleanValue recovery_show_particles;

    //marks general
    public final ForgeConfigSpec.IntValue marks_duration;
    public final ForgeConfigSpec.BooleanValue marks_color_chat;

    //band of gigantism
    public final ForgeConfigSpec.DoubleValue band_generic_scale;

    //lesser band of gigantism
    public final ForgeConfigSpec.DoubleValue lesser_band_generic_scale;

    //band of dwarfism
    public final ForgeConfigSpec.DoubleValue shrink_band_generic_scale;

    //misshapen band
    public final ForgeConfigSpec.DoubleValue band_basic_min_scale;
    public final ForgeConfigSpec.DoubleValue band_basic_max_scale;
    public final ForgeConfigSpec.DoubleValue band_basic_scale;

    //band of crustaceous convergence
    public final ForgeConfigSpec.DoubleValue band_crustaceous_scale;
    public final ForgeConfigSpec.DoubleValue band_crustaceous_limit_scale;
    public final ForgeConfigSpec.IntValue band_crustaceous_duration;
    public final ForgeConfigSpec.BooleanValue band_crustaceous_heal;

    //false hand
    public final ForgeConfigSpec.DoubleValue false_hand_flat_resistance;
    public final ForgeConfigSpec.IntValue false_hand_time;

    //globetrotter's band
    public final ForgeConfigSpec.DoubleValue band_globetrotters_scale;
    public final ForgeConfigSpec.IntValue band_globetrotters_limit;
    public final ForgeConfigSpec.DoubleValue band_globetrotters_limit_scale;
    public final ForgeConfigSpec.DoubleValue band_globetrotters_damage;

    //band of passion
    public final ForgeConfigSpec.DoubleValue band_passion_scale;
    public final ForgeConfigSpec.DoubleValue band_passion_scale_level;
    public final ForgeConfigSpec.DoubleValue band_passion_limit_scale;

    //band of apathy
    public final ForgeConfigSpec.DoubleValue band_apathy_scale;
    public final ForgeConfigSpec.DoubleValue band_apathy_scale_level;
    public final ForgeConfigSpec.DoubleValue band_apathy_limit_scale;

    //mask of diminishment
    public final ForgeConfigSpec.DoubleValue mask_diminishment_scale;
    public final ForgeConfigSpec.BooleanValue mask_diminishment_special;

    //mark of the descended
    public final ForgeConfigSpec.IntValue mark_descended_ascend;
    public final ForgeConfigSpec.IntValue mark_descended_duration;
    public final ForgeConfigSpec.IntValue mark_descended_armor;
    public final ForgeConfigSpec.DoubleValue mark_descended_regeneration;

    //mark of the faded
    public final ForgeConfigSpec.DoubleValue mark_faded_healing;
    public final ForgeConfigSpec.DoubleValue mark_faded_flat_resistance;
    public final ForgeConfigSpec.DoubleValue mark_faded_damage;

    //mark of the forgotten
    public final ForgeConfigSpec.IntValue mark_forgotten_duration;
    public final ForgeConfigSpec.DoubleValue mark_forgotten_critical_damage;
    public final ForgeConfigSpec.DoubleValue mark_forgotten_resistance;

    //mark of the judged
    public final ForgeConfigSpec.IntValue mark_judged_duration;
    public final ForgeConfigSpec.DoubleValue mark_judged_damage;
    public final ForgeConfigSpec.DoubleValue mark_judged_speed;

    //mark of the purified
    public final ForgeConfigSpec.DoubleValue mark_purified_ratio;
    public final ForgeConfigSpec.DoubleValue mark_purified_ratio_tough;

    //mark of the unknown
    public final ForgeConfigSpec.DoubleValue mark_unknown_healing;
    public final ForgeConfigSpec.DoubleValue mark_unknown_flat_resistance;
    public final ForgeConfigSpec.DoubleValue mark_unknown_speed;
    public final ForgeConfigSpec.IntValue mark_unknown_health;
    public final ForgeConfigSpec.IntValue mark_unknown_time;

    //mark of the obliterated
    public final ForgeConfigSpec.DoubleValue mark_obliterated_damage;
    public final ForgeConfigSpec.DoubleValue mark_obliterated_knockback;
    public final ForgeConfigSpec.IntValue mark_obliterated_armor;
    public final ForgeConfigSpec.IntValue mark_obliterated_armor_toughness;
    public final ForgeConfigSpec.IntValue mark_obliterated_health;
    public final ForgeConfigSpec.BooleanValue mark_obliterated_bypassinvuln;

    //mirapoppy
    public final ForgeConfigSpec.DoubleValue mirapoppy_chance;
    public final ForgeConfigSpec.DoubleValue mirapoppy_chance_double;
    public final ForgeConfigSpec.IntValue mirapoppy_radius;
    public final ForgeConfigSpec.DoubleValue mira_gin_chance;

    //scale types
    public final ForgeConfigSpec.BooleanValue width;
    public final ForgeConfigSpec.BooleanValue height;
    public final ForgeConfigSpec.BooleanValue step_height;
    public final ForgeConfigSpec.BooleanValue reach;
    public final ForgeConfigSpec.BooleanValue visibility;
    public final ForgeConfigSpec.BooleanValue knockback;
    public final ForgeConfigSpec.BooleanValue held_item;
    public final ForgeConfigSpec.BooleanValue attack_speed;
    public final ForgeConfigSpec.BooleanValue defense;

    public BOGConfig(ForgeConfigSpec.Builder BUILDER) {
        BUILDER.push("general");
        BUILDER.comment("Change what scale types the mod can affect").push("scale types");

            width = BUILDER.comment("Width scaling").define("width", true);
            height = BUILDER.comment("Height scaling").define("height", true);
            step_height = BUILDER.comment("Step height scaling").define("step_height", true);
            reach = BUILDER.comment("Reach scaling").define("reach", true);
            visibility = BUILDER.comment("Visibility scaling").define("visibility", true);
            knockback = BUILDER.comment("Knockback scaling").define("knockback", true);
            held_item = BUILDER.comment("Held item scaling").define("held_item", true);
            attack_speed = BUILDER.comment("Attack speed scaling").define("attack_speed", true);
            defense = BUILDER.comment("Defense scaling").define("defense", true);
        BUILDER.pop();

            scale_speed = BUILDER.comment("Scaling speed in ticks per unit of scale change. (default 30)").defineInRange("scale_speed",30,1,Integer.MAX_VALUE);
            description_enable = BUILDER.comment("Whether items have detailed descriptions. Disable if you hate information.").define("description_enable", true);
            multiply_enable = BUILDER.comment("Whether scaling items take into account the player's current scale, allowing multiple to be worn. This may be necessary if there are other mods that use Pehkui. (default true)").define("multiply_enable",true);
            general_scale_limit = BUILDER.comment("The absolute max scale that any item in Band of Gigantism can scale the player to. (default 16.0)").defineInRange("general_scale_limit",16.0,1.0,64.0);
            recovery_allhits = BUILDER.comment("Whether players are inflicted with a regen-blocking debuff whenever they are damaged. (default false)").define("recovery_allhits", false);
            recovery_duration = BUILDER.comment("The length the regen-blocking debuff will be applied for, in ticks. (default 80)").defineInRange("recovery_duration", 80, 10, Integer.MAX_VALUE);
            recovery_chance = BUILDER.comment("The chance for the regen-blocking debuff to be applied [1.0 = 100%]. (default 1.0)").defineInRange("recovery_chance", 1.0, 0.0, 1.0);
            recovery_minimum_damage = BUILDER.comment("The minimum amount of damage required before the regen-blocking debuff can be applied, not taking into account armor/resistances. (default 2.0)").defineInRange("recovery_minimum_damage",2.0, 0.0, Double.MAX_VALUE);
            recovery_show_particles = BUILDER.comment("Whether the regen-blocking debuff shows particles [doesn't affect mark removal] (default false)").define("recovery_show_particles", false);
        BUILDER.pop().push("items").comment("Change item scaling").push("scaling");

                band_generic_scale = BUILDER.comment("(default 2.0)").defineInRange("band_generic_scale",2.0,0.01,32.0);
                lesser_band_generic_scale = BUILDER.comment("(default 1.5)").defineInRange("lesser_band_generic_scale",1.5,0.01,32.0);
                shrink_band_generic_scale = BUILDER.comment("(default 0.65)").defineInRange("shrink_band_generic_scale",0.65,0.01,32.0);
                band_globetrotters_scale = BUILDER.comment("(default 2.0)").defineInRange("band_globetrotters_scale",2.0,0.01,32.0);
                band_globetrotters_limit_scale = BUILDER.comment("maximum possible scale (default 16.0)").defineInRange("band_globetrotters_limit_scale",16.0,0.01,32.0);
                mask_diminishment_scale = BUILDER.comment("(default 0.1)").defineInRange("mask_diminishment_scale",0.1,0.01,32.0);
                band_crustaceous_scale = BUILDER.comment("(default 1.25)").defineInRange("band_crustaceous_scale",1.25,0.01,32);
                band_crustaceous_limit_scale = BUILDER.comment("maximum possible scale (default 4.0)").defineInRange("band_crustaceous_limit_scale",4.0,0.01,32);
                band_basic_scale = BUILDER.comment("(default 0.5)").defineInRange("band_basic_scale",0.5,0.01,32);
                band_basic_min_scale = BUILDER.comment("minimum possible scale (default 0.3)").defineInRange("band_basic_min_scale",0.3,0.01,32);
                band_basic_max_scale = BUILDER.comment("maximum possible scale (default 0.9)").defineInRange("band_basic_max_scale",0.9,0.01,32);
                band_passion_scale = BUILDER.comment("(default 1.1)").defineInRange("band_passion_scale",1.1,0.01,32);
                band_passion_scale_level = BUILDER.comment("scale change per level (default 0.04)").defineInRange("band_passion_scale_level",0.04,0.0,8.0);
                band_passion_limit_scale = BUILDER.comment("maximum possible scale (default 4.0)").defineInRange("band_passion_limit_scale",4.0,0.01,32);
                band_apathy_scale = BUILDER.comment("(default 0.9)").defineInRange("band_apathy_scale",0.9,0.01,32);
                band_apathy_scale_level = BUILDER.comment("scale change per level (default 0.04)").defineInRange("band_apathy_scale_level",0.04,0.0,8.0);
                band_apathy_limit_scale = BUILDER.comment("minimum possible scale (default 0.25)").defineInRange("band_apathy_limit_scale",0.25,0.01,32);
            BUILDER.pop().comment("Mask of Diminishment settings").push("mask_diminishment");

                mask_diminishment_special = BUILDER.comment("Whether having items in the inventory reduces scaling. (default true)").define("mask_diminishment_special",true);
            BUILDER.pop().comment("Ancient Globetrotter's Band settings").push("ancient_globetrotters_band");

                band_globetrotters_limit = BUILDER.comment("Maximum amount of time a globetrotter's band can accumulate; when this value is reached the player will be set to the limit scale. This value increases by 1 every 5 seconds. (default 72000)").defineInRange("band_globetrotters_limit",72000,1,Integer.MAX_VALUE);
                band_globetrotters_damage = BUILDER.comment("Amount to increase all damage by when equipping. (default -0.9)").defineInRange("band_globetrotters_damage",-0.9,-Float.MAX_VALUE,Float.MAX_VALUE);
            BUILDER.pop().comment("Band of Crustaceous Convergence settings").push("band_crustaceous");

                band_crustaceous_duration = BUILDER.comment("Duration between getting hit and regenerating, in ticks. (default 200)").defineInRange("band_crustaceous_timer",200,1,Integer.MAX_VALUE);
                band_crustaceous_heal = BUILDER.comment("If enabled, the regen effect will apply healing rather than set health directly, allowing the regen to be modified by other items. (default true)").define("band_crustaceous_heal",true);
            BUILDER.pop().comment("False Hand settings").push("false_hand");

                false_hand_flat_resistance = BUILDER.comment("Amount of flat damage resistance added when equipping while flipped (default -2.0)").defineInRange("false_hand_flat_resistance",-2.0,-Float.MAX_VALUE,Float.MAX_VALUE);
                false_hand_time = BUILDER.comment("Amount of time for a flipped false hand to flip back, in seconds. (default 180)").defineInRange("false_hand_time",180,1,Integer.MAX_VALUE);
            BUILDER.pop().comment("Mirapoppy/Mira-Gin settings").push("mirapoppy");

                mirapoppy_chance = BUILDER.comment("Chance for experience dropped to double (default 0.25)").defineInRange("mirapoppy_chance",0.25,0,1);
                mirapoppy_chance_double = BUILDER.comment("Chance for experience dropped to double AGAIN after it has already doubled (default 0.25)").defineInRange("mirapoppy_chance_double",0.25,0,1);
                mirapoppy_radius = BUILDER.comment("Radius of the placed mirapoppy (default 3)").defineInRange("mirapoppy_radius",3,1,Integer.MAX_VALUE);
                mira_gin_chance = BUILDER.comment("Chance for the mira effect to negate damage when applied to players [1.0 = 100%] (default 0.10)").defineInRange("mira_gin_chance",0.1,0.0,1.0);
            BUILDER.pop().push("marks");

                marks_duration = BUILDER.comment("Duration of the regen-blocking debuff applied when removing marks, in ticks [separate from recovery_duration]. (default 200)").defineInRange("marks_duration",200,1,Integer.MAX_VALUE);
                marks_color_chat = BUILDER.comment("Whether marked players have their chat messages colored. (default true)").define("marks_color_chat",true);
                BUILDER.comment("Mark of the Descended settings").push("mark_descended");

                    mark_descended_duration = BUILDER.comment("Duration of the Strains of Ascent effect when applied. (default 100)").defineInRange("mark_descended_duration",100,1,Integer.MAX_VALUE);
                    mark_descended_ascend = BUILDER.comment("Amount of blocks to ascend before Strains of Ascent is applied. (default 5)").defineInRange("mark_descended_ascend",5,1,Integer.MAX_VALUE);
                    mark_descended_armor = BUILDER.comment("Amount of armor to add when equipping. (default 6)").defineInRange("mark_descended_armor",6,Integer.MIN_VALUE,Integer.MAX_VALUE);
                    mark_descended_regeneration = BUILDER.comment("Amount to add to the player's regeneration when equipping. (default 0.5)").defineInRange("mark_descended_regeneration",0.5,-Float.MAX_VALUE,Float.MAX_VALUE);
                BUILDER.pop().comment("Mark of the Faded settings").push("mark_faded");

                    mark_faded_healing = BUILDER.comment("Amount to add to the player's healing when equipping. (default -0.25)").defineInRange("mark_faded_healing",-0.25,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_faded_damage = BUILDER.comment("Amount to increase the player's damage by when equipping. (default 0.5)").defineInRange("mark_faded_damage",0.5,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_faded_flat_resistance = BUILDER.comment("Amount of flat damage resistance added when equipping. (default 2.0)").defineInRange("mark_faded_flat_resistance",2.0,-Float.MAX_VALUE,Float.MAX_VALUE);
                BUILDER.pop().comment("Mark of the Forgotten settings").push("mark_forgotten");

                    mark_forgotten_duration = BUILDER.comment("Duration to apply the health-bar-hiding effect when getting hit, in ticks. (default 100)").defineInRange("mark_forgotten_duration",100,1,Integer.MAX_VALUE);
                    mark_forgotten_critical_damage = BUILDER.comment("Amount to add to the player's critical damage when equipping. (default 0.5)").defineInRange("mark_forgotten_critical_damage",0.5,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_forgotten_resistance = BUILDER.comment("Amount to add to the player's damage resistance when equipping. (default 0.15)").defineInRange("mark_forgotten_resistance",0.15,-Float.MAX_VALUE,Float.MAX_VALUE);
                BUILDER.pop().comment("Mark of the Judged settings").push("mark_judged");

                    mark_judged_duration = BUILDER.comment("Duration of the strength effect applied to nearby mobs. (default 300)").defineInRange("mark_judged_duration",300,1,Integer.MAX_VALUE);
                    mark_judged_damage = BUILDER.comment("Amount to increase all attack damage by when equipping. (default 4.0)").defineInRange("mark_judged_damage",4.0,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_judged_speed = BUILDER.comment("Amount to increase speed by when equipping. (default 0.1)").defineInRange("mark_judged_speed",0.1,-Float.MAX_VALUE,Float.MAX_VALUE);
                BUILDER.pop().comment("Mark of the Purified settings").push("mark_purified");

                    mark_purified_ratio = BUILDER.comment("Ratio of health added per armor point. (default 1.0)").defineInRange("mark_purified_ratio",1.0,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_purified_ratio_tough = BUILDER.comment("Ratio of health added per armor toughness point. (default 1.0)").defineInRange("mark_purified_ratio_tough",1.0,-Float.MAX_VALUE,Float.MAX_VALUE);
                BUILDER.pop().comment("Mark of the Unknown settings").push("mark_unknown");

                    mark_unknown_healing = BUILDER.comment("Amount to add or remove healing by. (default 0.25)").defineInRange("mark_unknown_healing",0.25,0,Float.MAX_VALUE);
                    mark_unknown_flat_resistance = BUILDER.comment("Amount to add or remove all incoming damage by. (default 0.75)").defineInRange("mark_unknown_flat_resistance",0.75,0,Float.MAX_VALUE);
                    mark_unknown_speed = BUILDER.comment("Upper/lower limit of speed changes. (default 0.5)").defineInRange("mark_unknown_speed",0.5,0,Float.MAX_VALUE);
                    mark_unknown_health = BUILDER.comment("Upper/lower limit of max health changes; upper limit is doubled. (default 10)").defineInRange("mark_unknown_health",10,0,Integer.MAX_VALUE);
                    mark_unknown_time = BUILDER.comment("Amount of time before the mark's stats are randomized again. (default 300)").defineInRange("mark_unknown_time",300,1,Integer.MAX_VALUE);
                BUILDER.pop().comment("Mark of the Obliterated settings").push("mark_obliterated");

                    mark_obliterated_damage = BUILDER.comment("Amount to increase the player's damage by when equipping. (default 0.25)").defineInRange("mark_obliterated_damage",0.25,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_obliterated_knockback = BUILDER.comment("Amount to increase the player's knockback dealt when equipping. (default 0.25)").defineInRange("mark_obliterated_knockback",0.25,-Float.MAX_VALUE,Float.MAX_VALUE);
                    mark_obliterated_armor = BUILDER.comment("Amount to add to the player's armor when equipping. (default 2)").defineInRange("mark_obliterated_armor",2,Integer.MIN_VALUE,Integer.MAX_VALUE);
                    mark_obliterated_armor_toughness = BUILDER.comment("Amount to add to the player's armor toughness when equipping. (default 2)").defineInRange("mark_obliterated_armor_toughness",2,Integer.MIN_VALUE,Integer.MAX_VALUE);
                    mark_obliterated_health = BUILDER.comment("Amount to add to the player's max health when equipping. (default 6)").defineInRange("mark_obliterated_health",6,Integer.MIN_VALUE,Integer.MAX_VALUE);
                    mark_obliterated_bypassinvuln = BUILDER.comment("Whether the mark's killing effect bypasses invulnerability").define("mark_obliterated_bypassinvuln", false);
                BUILDER.pop().pop().pop();
    }
}
