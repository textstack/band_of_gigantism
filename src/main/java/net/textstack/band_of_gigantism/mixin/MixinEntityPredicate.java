package net.textstack.band_of_gigantism.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({TargetGoal.class})
public class MixinEntityPredicate {

    @Shadow
    protected
    LivingEntity targetMob;
    @Shadow
    @Final
    @Mutable
    protected
    Mob mob;

    final BOGConfig c = BOGConfig.INSTANCE;

    public MixinEntityPredicate() {
    }

    @Inject(
            method = {"canContinueToUse"},
            at = {@At(value = "TAIL")}
            //cancellable = true
    )
    private void onCanContinueToUse(CallbackInfoReturnable<Double> info) {

        LivingEntity target = this.mob.getTarget();

        //give entities strength when targetting a judged player
        if (target != null || (target = this.targetMob) != null)
            if (CurioHelper.hasCurio(target, ModItems.MARK_JUDGED.get()) && mob.getClassification(false) == MobCategory.MONSTER) {
                mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, c.mark_judged_duration.get(), 4, false, true));
            }
    }
}
