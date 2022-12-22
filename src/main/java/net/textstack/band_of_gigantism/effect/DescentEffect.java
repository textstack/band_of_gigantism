package net.textstack.band_of_gigantism.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import org.jetbrains.annotations.NotNull;

public class DescentEffect extends MobEffect {
    public DescentEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entityLivingBaseIn, int amplifier) {
        super.applyEffectTick(entityLivingBaseIn, amplifier);

        //apply damage
        if (amplifier >= 11) { //>10? bye idiot
            entityLivingBaseIn.hurt(ModDamageSources.BOG_DESCENDED, Float.MAX_VALUE);
        } else {
            entityLivingBaseIn.hurt(ModDamageSources.BOG_DESCENDED, (amplifier / 2.0f) + 1);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int i = 80 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}