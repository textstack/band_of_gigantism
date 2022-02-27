package net.textstack.band_of_gigantism.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ObliteratedStats {
    public static final String FILE = "obliterated";

    public static final Codec<ObliteratedStats> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.DOUBLE.fieldOf("mark_obliterated_damage").forGetter(i->i.mark_obliterated_damage),
                    Codec.DOUBLE.fieldOf("mark_obliterated_knockback").forGetter(i->i.mark_obliterated_knockback),
                    Codec.INT.fieldOf("mark_obliterated_armor").forGetter(i->i.mark_obliterated_armor),
                    Codec.INT.fieldOf("mark_obliterated_armor_toughness").forGetter(i->i.mark_obliterated_armor_toughness),
                    Codec.INT.fieldOf("mark_obliterated_health").forGetter(i->i.mark_obliterated_health)
            ).apply(inst,ObliteratedStats::new));

    public final double mark_obliterated_damage;
    public final double mark_obliterated_knockback;
    public final int mark_obliterated_armor;
    public final int mark_obliterated_armor_toughness;
    public final int mark_obliterated_health;

    public ObliteratedStats(double mark_obliterated_damage, double mark_obliterated_knockback,
                            int mark_obliterated_armor, int mark_obliterated_armor_toughness, int mark_obliterated_health) {

        this.mark_obliterated_damage = mark_obliterated_damage;
        this.mark_obliterated_knockback = mark_obliterated_knockback;
        this.mark_obliterated_armor = mark_obliterated_armor;
        this.mark_obliterated_armor_toughness = mark_obliterated_armor_toughness;
        this.mark_obliterated_health = mark_obliterated_health;
    }
}
