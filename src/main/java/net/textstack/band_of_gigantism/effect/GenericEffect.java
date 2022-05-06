package net.textstack.band_of_gigantism.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class GenericEffect extends MobEffect {
    public GenericEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }
}
