package net.textstack.band_of_gigantism.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.registry.BogEffects;
import net.textstack.band_of_gigantism.registry.BogItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.Objects;

@Mixin(FoodData.class)
public class MixinFoodStats {
    @Redirect( //replaces the "getBoolean" function to always output false when the player has recovering/faded
            method = "tick",
            remap = false,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean onTick(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Player player) {
        //prevents hunger-based regen from working, otherwise it would uselessly deplete itself
        if (CurioHelper.hasCurio(player, BogItems.MARK_FADED.get()) || Objects.requireNonNull(player).hasEffect(BogEffects.RECOVERING.get()) || CurioHelper.hasCurio(player, BogItems.BAND_CRUSTACEOUS.get())) {
            return false;
        }

        try (Level level = player.level()) {
            return level.getGameRules().getBoolean(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
