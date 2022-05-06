package net.textstack.band_of_gigantism.util;

import net.minecraft.network.chat.TranslatableComponent;

public class LoreStatHelper {

    public enum StrangeType {
        TITLE,
        TOOLTIP
    }

    public enum Stat {
        DAMAGE,
        CRITICAL_DAMAGE,
        RESISTANCE,
        FLAT_RESISTANCE,
        HEALING,
        REGENERATION,
        ARMOR,
        MAX_HEALTH,
        SPEED,
        KNOCKBACK,
        ARMOR_TOUGHNESS
    }

    /**
     * Creates a stylized display for the stat provided, to be shown in tooltips.
     * To use, put the function directly in tooltip.add() like so: tooltip.add(LoreStatHelper.displayStat(display,statType,isPercent));
     *
     * @param display   the stat to show; should be the same as what would be entered into attributes
     * @param statType  the type of stat (see the Stat enum definition)
     * @param isPercent whether to display as a percentage, optional
     * @return the TranslationTextComponent of the display
     */
    public static TranslatableComponent displayStat(float display, Stat statType, boolean isPercent) {

        String stat = switch (statType) {
            case DAMAGE -> "damage";
            case CRITICAL_DAMAGE -> "critical_damage";
            case RESISTANCE -> "resistance";
            case FLAT_RESISTANCE -> "flat_resistance";
            case HEALING -> "healing";
            case REGENERATION -> "regeneration";
            case ARMOR -> "armor";
            case MAX_HEALTH -> "max_health";
            case SPEED -> "speed";
            case KNOCKBACK -> "knockback";
            case ARMOR_TOUGHNESS -> "armor_toughness";
        };

        String isNegative;
        if (display < 0) isNegative = "_negative";
        else isNegative = "";

        String value;
        if (isPercent) {
            int display_percent = (int) (Math.abs(display)*100);
            value = "\u00A76" + display_percent + "%";
        } else {
            float display_out = Math.abs(display);
            if (display_out%1==0) {
                int display_out_solid = (int) display_out;
                value = "\u00A76" + display_out_solid;
            } else {
                value = "\u00A76" + display_out;
            }
        }

        return new TranslatableComponent("tooltip.band_of_gigantism."+stat+isNegative,value);
    }

    public static TranslatableComponent displayStat(float display, Stat statType) {
        return displayStat(display, statType, false);
    }

    /**
     * Creates a special stylized display for items that change the player's scale, to be shown in tooltips.
     * To use, put the function directly in tooltip.add() like so: tooltip.add(LoreStatHelper.displayScale(display));
     *
     * @param display the new scale the player will be in when equipping
     * @return the TranslationTextComponent of the display
     */
    public static TranslatableComponent displayScale(float display) {

        int display_percent = (int) (Math.abs(display-1)*100);

        if (display < 1) {
            return new TranslatableComponent("tooltip.band_of_gigantism.shrink_band_generic_description_percent", "\u00A76" + display_percent + "%");
        } else {
            return new TranslatableComponent("tooltip.band_of_gigantism.band_generic_description_percent", "\u00A76" + display_percent + "%");
        }
    }

    /**
     * Displays a strange name according to kill count, to be appended to another TranslationTextComponent
     *
     * @param kills the number of kills to check against
     * @param type whether the text will be in a tooltip or title
     * @return the TranslationTextComponent of the name
     */
    public static TranslatableComponent displayStrangeName(int kills, StrangeType type) {

        //this is how coding works right?
        String nameKey = "strange_0";
        if (kills>=8500) nameKey = "strange_8500";
        else if (kills>=7616) nameKey = "strange_7616";
        else if (kills>=7500) nameKey = "strange_7500";
        else if (kills>=5000) nameKey = "strange_5000";
        else if (kills>=2500) nameKey = "strange_2500";
        else if (kills>=1500) nameKey = "strange_1500";
        else if (kills>=1000) nameKey = "strange_1000";
        else if (kills>=999) nameKey = "strange_999";
        else if (kills>=750) nameKey = "strange_750";
        else if (kills>=500) nameKey = "strange_500";
        else if (kills>=350) nameKey = "strange_350";
        else if (kills>=275) nameKey = "strange_275";
        else if (kills>=225) nameKey = "strange_225";
        else if (kills>=175) nameKey = "strange_175";
        else if (kills>=135) nameKey = "strange_135";
        else if (kills>=100) nameKey = "strange_100";
        else if (kills>=70) nameKey = "strange_70";
        else if (kills>=45) nameKey = "strange_45";
        else if (kills>=25) nameKey = "strange_25";
        else if (kills>=10) nameKey = "strange_10";

        //determine color
        String color = switch (type) {
            case TITLE -> "title_";
            case TOOLTIP -> "tooltip_";
        };

        return new TranslatableComponent("tooltip.band_of_gigantism."+color+nameKey);
    }
}
