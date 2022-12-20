package net.textstack.band_of_gigantism.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModEffects;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MiraGin extends Item {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MiraGin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity living) {
        if (living instanceof Player player && !((Player)living).getAbilities().instabuild) {
            stack.shrink(1);
            ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(itemstack)) {
                player.drop(itemstack, false);
            }
        }

        living.addEffect(new MobEffectInstance(ModEffects.MIRA.get(), 9600, 0));
        return super.finishUsingItem(stack, level, living);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        //if (!c.description_enable.get()) return;

        String value = String.valueOf((int) (Math.abs(c.mira_gin_chance.get()) * 100));

        tooltip.add(Component.translatable("potion.withDuration",Component.translatable("effect.band_of_gigantism.mira"),"8:00").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("tooltip.band_of_gigantism.mira_gin_description",value+"%").withStyle(ChatFormatting.BLUE));
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 40;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
