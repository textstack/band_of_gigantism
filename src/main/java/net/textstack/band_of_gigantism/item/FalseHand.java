package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class FalseHand extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public FalseHand(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        if (stack.getOrCreateTag().getInt("flipped") == 0) {
            stack.getOrCreateTag().putInt("timeLeft", c.false_hand_time.get() - 1);
            stack.getOrCreateTag().putInt("flipped", 0);
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return ICurioItem.super.canEquip(slotContext, stack) && !CurioHelper.hasCurio(slotContext.entity(), ModItems.FALSE_HAND.get());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {

            int flipped = stack.getOrCreateTag().getInt("flipped");

            tooltip.add(Component.translatable("tooltip.band_of_gigantism.false_hand_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            if (flipped == 0) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.false_hand_description_0"));
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.false_hand_description_1"));
            } else {
                tooltip.add(LoreStatHelper.displayStat(c.false_hand_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));

                int storedTime = stack.getOrCreateTag().getInt("timeLeft");
                if (storedTime >= 60) {
                    int displayTime = 1 + storedTime / 60;
                    tooltip.add(Component.translatable("tooltip.band_of_gigantism.false_hand_description_shift_0_minutes", "§6" + displayTime));
                } else {
                    if (storedTime == 0) {
                        tooltip.add(Component.translatable("tooltip.band_of_gigantism.false_hand_description_shift_0_second"));
                    } else {
                        int displayTime = 1 + storedTime;
                        tooltip.add(Component.translatable("tooltip.band_of_gigantism.false_hand_description_shift_0_seconds", "§6" + displayTime));
                    }
                }
            }
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        int flipped = stack.getOrCreateTag().getInt("flipped");
        LivingEntity living = slotContext.entity();

        if (flipped == 1) {
            Level world = living.getLevel();
            if (world.getGameTime() % 20 == 0) {
                int storedTime = stack.getOrCreateTag().getInt("timeLeft");
                if (storedTime > 0) {
                    stack.getOrCreateTag().putInt("timeLeft", storedTime - 1);
                } else {
                    living.playSound(ModSoundEvents.CARD_FLIP.get(), 0.5f, 1);
                    stack.getOrCreateTag().putInt("timeLeft", c.false_hand_time.get() - 1);
                    stack.getOrCreateTag().putInt("flipped", 0);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerVariants() { //property function has a new mystery integer I just named "thing" for now
        ItemProperties.register(ModItems.FALSE_HAND.get(), new ResourceLocation(BandOfGigantism.MODID, "false_hand_flipped"), (stack, world, entity, thing) -> stack.getOrCreateTag().getInt("flipped"));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
