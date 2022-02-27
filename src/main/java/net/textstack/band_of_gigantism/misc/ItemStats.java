package net.textstack.band_of_gigantism.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ItemStats {
    public static final String FILE = "items";

    public static final Codec<ItemStats> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.BOOL.fieldOf("description_enable").forGetter(i -> i.description_enable),
                    Codec.DOUBLE.fieldOf("band_generic_scale").forGetter(i -> i.band_generic_scale),
                    Codec.DOUBLE.fieldOf("lesser_band_generic_scale").forGetter(i->i.lesser_band_generic_scale),
                    Codec.DOUBLE.fieldOf("shrink_band_generic_scale").forGetter(i->i.shrink_band_generic_scale),
                    Codec.DOUBLE.fieldOf("band_globetrotters_scale").forGetter(i->i.band_globetrotters_scale),
                    Codec.INT.fieldOf("band_globetrotters_limit").forGetter(i->i.band_globetrotters_limit),
                    Codec.DOUBLE.fieldOf("band_globetrotters_limit_scale").forGetter(i->i.band_globetrotters_limit_scale),
                    Codec.DOUBLE.fieldOf("band_globetrotters_damage").forGetter(i->i.band_globetrotters_damage),
                    Codec.DOUBLE.fieldOf("mask_diminishment_scale").forGetter(i->i.mask_diminishment_scale),
                    Codec.BOOL.fieldOf("mask_diminishment_special").forGetter(i->i.mask_diminishment_special)
            ).apply(inst,ItemStats::new));

    public final boolean description_enable;

    public final double band_generic_scale;

    public final double lesser_band_generic_scale;

    public final double shrink_band_generic_scale;

    public final double band_globetrotters_scale;
    public final int band_globetrotters_limit;
    public final double band_globetrotters_limit_scale;
    public final double band_globetrotters_damage;

    public final double mask_diminishment_scale;
    public final boolean mask_diminishment_special;

    public ItemStats(boolean description_enable, double band_generic_scale, double lesser_band_generic_scale,
                     double shrink_band_generic_scale, double band_globetrotters_scale, int band_globetrotters_limit, double band_globetrotters_limit_scale,
                     double band_globetrotters_damage, double mask_diminishment_scale, boolean mask_diminishment_special) {

        this.description_enable = description_enable;

        this.band_generic_scale = band_generic_scale;

        this.lesser_band_generic_scale = lesser_band_generic_scale;

        this.shrink_band_generic_scale = shrink_band_generic_scale;

        this.band_globetrotters_limit = band_globetrotters_limit;
        this.band_globetrotters_limit_scale = band_globetrotters_limit_scale;
        this.band_globetrotters_scale = band_globetrotters_scale;
        this.band_globetrotters_damage = band_globetrotters_damage;

        this.mask_diminishment_scale = mask_diminishment_scale;
        this.mask_diminishment_special = mask_diminishment_special;
    }
}
