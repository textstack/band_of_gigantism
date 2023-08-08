package net.textstack.band_of_gigantism.registry;

import net.minecraft.world.damagesource.DamageSource;

public class ModDamageSources extends DamageSource {
    public ModDamageSources(String damageTypeIn) {
        super(damageTypeIn);
    }

    public static final DamageSource BOG_OBLITERATED = (new DamageSource("mark_obliterated")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_OBLITERATED_INVULN = (new DamageSource("mark_obliterated_invuln")).
            bypassArmor().bypassMagic().bypassInvul();
    public static final DamageSource BOG_FADED = (new DamageSource("mark_faded")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_FORGOTTEN = (new DamageSource("mark_forgotten")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_PURIFIED = (new DamageSource("mark_purified")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_UNKNOWN = (new DamageSource("mark_unknown")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_DESCENDED = (new DamageSource("mark_descended")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_JUDGED = (new DamageSource("mark_judged")).
            bypassArmor().bypassMagic();
    public static final DamageSource BOG_MIRA = (new DamageSource("mira")).
            bypassArmor().bypassMagic();
}
