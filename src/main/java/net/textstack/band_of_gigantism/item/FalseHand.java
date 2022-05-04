package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class FalseHand extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    public FalseHand(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        if (stack.getOrCreateTag().getInt("flipped")==0) {
            stack.getOrCreateTag().putInt("timeLeft",c.false_hand_time.get()-1);
            stack.getOrCreateTag().putInt("flipped",0);
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {

            int flipped = stack.getOrCreateTag().getInt("flipped");

            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.false_hand_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            if (flipped == 0) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.false_hand_description_0"));
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.false_hand_description_1"));
            } else {
                tooltip.add(LoreStatHelper.displayStat(c.false_hand_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));

                int storedTime = stack.getOrCreateTag().getInt("timeLeft");
                if (storedTime>=60) {
                    int displayTime = 1+storedTime/60;
                    tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.false_hand_description_shift_0_minutes","\u00A76" + displayTime));
                } else {
                    if (storedTime == 0) {
                        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.false_hand_description_shift_0_second"));
                    } else {
                        int displayTime = 1+storedTime;
                        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.false_hand_description_shift_0_seconds","\u00A76" + displayTime));
                    }
                }
            }
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ICurioItem.super.curioTick(identifier, index, livingEntity, stack);

        int flipped = stack.getOrCreateTag().getInt("flipped");

        if (flipped == 1) {
            World world = livingEntity.getEntityWorld();
            if (world.getGameTime() % 20 == 0) {
                int storedTime = stack.getOrCreateTag().getInt("timeLeft");
                if (storedTime > 0) {
//                    if (storedTime >= c.false_hand_time.get()-1) { //really bad way of playing this sound
//                        livingEntity.playSound(ModSoundEvents.CARD_FLIP.get(),0.5f,1);
//                    }
                    stack.getOrCreateTag().putInt("timeLeft",storedTime - 1);
                } else {
                    livingEntity.playSound(ModSoundEvents.CARD_FLIP.get(),0.5f,1);
                    stack.getOrCreateTag().putInt("timeLeft",c.false_hand_time.get()-1);
                    stack.getOrCreateTag().putInt("flipped",0);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerVariants() {
        ItemModelsProperties.registerProperty(ModItems.FALSE_HAND.get(),new ResourceLocation(BandOfGigantism.MODID,"false_hand_flipped"), (stack, world, entity) -> stack.getOrCreateTag().getInt("flipped"));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
