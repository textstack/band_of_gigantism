package net.textstack.band_of_gigantism.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MarkStats {
    public static final String FILE = "marks";

    public static final Codec<MarkStats> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.INT.fieldOf("mark_descended_armor").forGetter(i->i.mark_descended_armor),
                    Codec.DOUBLE.fieldOf("mark_descended_regeneration").forGetter(i->i.mark_descended_regeneration),
                    Codec.DOUBLE.fieldOf("mark_faded_healing").forGetter(i->i.mark_faded_healing),
                    Codec.DOUBLE.fieldOf("mark_faded_flat_resistance").forGetter(i->i.mark_faded_flat_resistance),
                    Codec.DOUBLE.fieldOf("mark_faded_damage").forGetter(i->i.mark_faded_damage),
                    Codec.DOUBLE.fieldOf("mark_forgotten_critical_damage").forGetter(i->i.mark_forgotten_critical_damage),
                    Codec.DOUBLE.fieldOf("mark_forgotten_resistance").forGetter(i->i.mark_forgotten_resistance),
                    Codec.DOUBLE.fieldOf("mark_judged_damage").forGetter(i->i.mark_judged_damage),
                    Codec.DOUBLE.fieldOf("mark_judged_speed").forGetter(i->i.mark_judged_speed),
                    Codec.DOUBLE.fieldOf("mark_unknown_healing").forGetter(i->i.mark_unknown_healing),
                    Codec.DOUBLE.fieldOf("mark_unknown_flat_resistance").forGetter(i->i.mark_unknown_flat_resistance),
                    Codec.DOUBLE.fieldOf("mark_unknown_speed").forGetter(i->i.mark_unknown_speed),
                    Codec.INT.fieldOf("mark_unknown_health").forGetter(i->i.mark_unknown_health)
            ).apply(inst,MarkStats::new));

    public final int mark_descended_armor;
    public final double mark_descended_regeneration;

    public final double mark_faded_healing;
    public final double mark_faded_flat_resistance;
    public final double mark_faded_damage;

    public final double mark_forgotten_critical_damage;
    public final double mark_forgotten_resistance;

    public final double mark_judged_damage;
    public final double mark_judged_speed;

    public final double mark_unknown_healing;
    public final double mark_unknown_flat_resistance;
    public final double mark_unknown_speed;
    public final int mark_unknown_health;

    public MarkStats(int mark_descended_armor, double mark_descended_regeneration, double mark_faded_healing,
                     double mark_faded_flat_resistance, double mark_faded_damage, double mark_forgotten_critical_damage,
                     double mark_forgotten_resistance, double mark_judged_damage, double mark_judged_speed,
                     double mark_unknown_healing, double mark_unknown_flat_resistance, double mark_unknown_speed,
                     int mark_unknown_health) {

        this.mark_descended_armor = mark_descended_armor;
        this.mark_descended_regeneration = mark_descended_regeneration;

        this.mark_faded_healing = mark_faded_healing;
        this.mark_faded_flat_resistance = mark_faded_flat_resistance;
        this.mark_faded_damage = mark_faded_damage;

        this.mark_forgotten_critical_damage = mark_forgotten_critical_damage;
        this.mark_forgotten_resistance = mark_forgotten_resistance;

        this.mark_judged_damage = mark_judged_damage;
        this.mark_judged_speed = mark_judged_speed;

        this.mark_unknown_healing = mark_unknown_healing;
        this.mark_unknown_flat_resistance = mark_unknown_flat_resistance;
        this.mark_unknown_speed = mark_unknown_speed;
        this.mark_unknown_health = mark_unknown_health;
    }
}
