package net.textstack.band_of_gigantism.config;

import net.textstack.band_of_gigantism.misc.StatListener;

public final class BOGItems {
    private BOGItems() { throw new IllegalAccessError("Utility Class"); }

    public static boolean descriptionEnable() {
        return StatListener.getItemStats().description_enable;
    }

    public static double bandGenericScale() {
        return StatListener.getItemStats().band_generic_scale;
    }

    public static double lesserBandGenericScale() {
        return StatListener.getItemStats().lesser_band_generic_scale;
    }

    public static double shrinkBandGenericScale() {
        return StatListener.getItemStats().shrink_band_generic_scale;
    }

    public static double bandGlobetrottersScale() {
        return StatListener.getItemStats().band_globetrotters_scale;
    }

    public static int bandGlobetrottersLimit() {
        return StatListener.getItemStats().band_globetrotters_limit;
    }

    public static double bandGlobetrottersLimitScale() {
        return StatListener.getItemStats().band_globetrotters_limit_scale;
    }

    public static double bandGlobetrottersDamage() {
        return StatListener.getItemStats().band_globetrotters_damage;
    }

    public static double maskDiminishmentScale() {
        return StatListener.getItemStats().mask_diminishment_scale;
    }

    public static boolean maskDiminishmentSpecial() {
        return StatListener.getItemStats().mask_diminishment_special;
    }
}
