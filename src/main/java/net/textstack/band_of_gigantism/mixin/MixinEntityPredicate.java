package net.textstack.band_of_gigantism.mixin;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityPredicate.class})
public class MixinEntityPredicate {

    BOGConfig c = BOGConfig.INSTANCE;

    /*private static final int mark_judged_duration = c.mark_judged_duration.get();*/

    public MixinEntityPredicate() {}

    @Inject(
            method = {"canTarget"},
            at = {@At(value = "TAIL")},
            cancellable = true
    )
    private void onCanTarget (@Nullable LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Double> info) {

        //give entities strength when targetting a judged player
        if (attacker != null) //this is redundant since the method already checks this right before, but i'm not sure how to inject to that spot
        if (CurioHelper.hasCurio(target, ModItems.MARK_JUDGED.get()) && attacker.getClassification(false) == EntityClassification.MONSTER) {
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH,c.mark_judged_duration.get(),4,false,true));
        }
    }
}
