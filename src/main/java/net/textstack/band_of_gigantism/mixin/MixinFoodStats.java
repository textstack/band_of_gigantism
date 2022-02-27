package net.textstack.band_of_gigantism.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraft.world.GameRules;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Objects;

@Mixin(FoodStats.class)
public class MixinFoodStats {

    @Redirect( //replaces the "getBoolean" function to always output false when the player has recovering/faded
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"))
    private boolean onTick(GameRules instance, GameRules.RuleKey<GameRules.BooleanValue> key, PlayerEntity player) {

        //prevents hunger-based regen from working, otherwise it would uselessly deplete itself
        if (CurioHelper.hasCurio(player, ModItems.MARK_FADED.get())||Objects.requireNonNull(player).isPotionActive(ModEffects.RECOVERING.get())) {
            return false;
        } else {return player.world.getGameRules().getBoolean(key);}
    }
}
