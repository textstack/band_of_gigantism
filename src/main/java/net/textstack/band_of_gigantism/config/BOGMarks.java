package net.textstack.band_of_gigantism.config;

import net.textstack.band_of_gigantism.misc.StatListener;

public final class BOGMarks {
    private BOGMarks() { throw new IllegalAccessError("Utility Class"); }

    public static int markDescendedArmor() {
        return StatListener.getMarkStats().mark_descended_armor;
    }

    public static double markDescendedRegeneration() {
        return StatListener.getMarkStats().mark_descended_regeneration;
    }

    public static double markFadedHealing() {
        return StatListener.getMarkStats().mark_faded_healing;
    }

    public static double markFadedFlatResistance() {
        return StatListener.getMarkStats().mark_faded_flat_resistance;
    }

    public static double markFadedDamage() {
        return StatListener.getMarkStats().mark_faded_damage;
    }

    public static double markForgottenCriticalDamage() {
        return StatListener.getMarkStats().mark_forgotten_critical_damage;
    }

    public static double markForgottenResistance() {
        return StatListener.getMarkStats().mark_forgotten_resistance;
    }

    public static double markJudgedDamage() {
        return StatListener.getMarkStats().mark_judged_damage;
    }

    public static double markJudgedSpeed() {
        return StatListener.getMarkStats().mark_judged_speed;
    }

    public static double markUnknownHealing() {
        return StatListener.getMarkStats().mark_unknown_healing;
    }

    public static double markUnknownFlatResistance() {
        return StatListener.getMarkStats().mark_unknown_flat_resistance;
    }

    public static double markUnknownSpeed() {
        return StatListener.getMarkStats().mark_unknown_speed;
    }

    public static double markUnknownHealth() {
        return StatListener.getMarkStats().mark_unknown_health;
    }

    public static double markObliteratedDamage() {
        return StatListener.getObliteratedStats().mark_obliterated_damage;
    }

    public static double markObliteratedKnockback() {
        return StatListener.getObliteratedStats().mark_obliterated_knockback;
    }

    public static int markObliteratedArmor() {
        return StatListener.getObliteratedStats().mark_obliterated_armor;
    }

    public static int markObliteratedArmorToughness() {
        return StatListener.getObliteratedStats().mark_obliterated_armor_toughness;
    }

    public static int markObliteratedHealth() {
        return StatListener.getObliteratedStats().mark_obliterated_health;
    }
}
