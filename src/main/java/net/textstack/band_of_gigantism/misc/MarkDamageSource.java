package net.textstack.band_of_gigantism.misc;

import net.minecraft.util.DamageSource;

public class MarkDamageSource extends DamageSource {
    public MarkDamageSource(String damageTypeIn) {
        super(damageTypeIn);
    }

    public static final DamageSource BOG_OBLITERATED = (new DamageSource("mark_obliterated")).
            setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode();
    public static final DamageSource BOG_FADED = (new DamageSource("mark_faded")).
            setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource BOG_FORGOTTEN = (new DamageSource("mark_forgotten")).
            setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource BOG_PURIFIED = (new DamageSource("mark_purified")).
            setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource BOG_UNKNOWN = (new DamageSource("mark_unknown")).
            setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource BOG_DESCENDED = (new DamageSource("mark_descended")).
            setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource BOG_JUDGED = (new DamageSource("mark_judged")).
            setDamageBypassesArmor().setDamageIsAbsolute();
}
