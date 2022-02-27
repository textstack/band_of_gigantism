package net.textstack.band_of_gigantism.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

public class FryingPan extends SwordItem {
    public FryingPan(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean hitEntity(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {

        World worldIn = attacker.getEntityWorld();
        if (worldIn.isRemote()) {return super.hitEntity(stack, target, attacker);}

        target.playSound(ModSoundEvents.PAN_HIT.get(),0.5f,1);

        return super.hitEntity(stack, target, attacker);
    }
}
