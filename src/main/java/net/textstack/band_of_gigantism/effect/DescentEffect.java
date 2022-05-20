package net.textstack.band_of_gigantism.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import org.jetbrains.annotations.NotNull;

public class DescentEffect extends Effect {
    public DescentEffect(int color) {
        super(EffectType.HARMFUL, color);
    }

    @Override
    public void performEffect(@NotNull LivingEntity entityLivingBaseIn, int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);

        //apply damage
        if (amplifier >= 11) { //>10? bye idiot
            entityLivingBaseIn.attackEntityFrom(MarkDamageSource.BOG_DESCENDED, Float.MAX_VALUE);
        } else {
            entityLivingBaseIn.attackEntityFrom(MarkDamageSource.BOG_DESCENDED, (amplifier/2.0f)+1);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int i = 80 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}