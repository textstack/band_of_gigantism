package net.textstack.band_of_gigantism.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

public class FryingPan extends SwordItem {
    public FryingPan(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {

        Level worldIn = attacker.getLevel();
        if (worldIn.isClientSide()) {return super.hurtEnemy(stack, target, attacker);}

        target.playSound(ModSoundEvents.PAN_HIT.get(),0.3f,1);

        return super.hurtEnemy(stack, target, attacker);
    }
}
