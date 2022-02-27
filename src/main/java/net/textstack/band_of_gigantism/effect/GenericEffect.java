package net.textstack.band_of_gigantism.effect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.textstack.band_of_gigantism.BandOfGigantism;

public class GenericEffect extends Effect {
    public GenericEffect(int color) {
        super(EffectType.HARMFUL, color);
    }
}
