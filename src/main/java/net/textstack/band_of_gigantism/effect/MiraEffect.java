package net.textstack.band_of_gigantism.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import org.jetbrains.annotations.NotNull;

public class MiraEffect extends MobEffect {
    public MiraEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity living, int amplifier) {
        super.applyEffectTick(living, amplifier);

        living.hurt(MarkDamageSource.BOG_MIRA, Float.MAX_VALUE);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration == 1;
    }
}
