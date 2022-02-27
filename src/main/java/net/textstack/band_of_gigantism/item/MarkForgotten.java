package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MarkForgotten extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    /*private static final int marks_duration = c.marks_duration.get();
    private static final float mark_forgotten_critical_damage = c.mark_forgotten_critical_damage.get().floatValue();
    private static final float mark_forgotten_resistance = c.mark_forgotten_resistance.get().floatValue();
    private static final boolean description_enable = c.description_enable.get();*/

    public MarkForgotten(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        //deal near-mortal damage, prevent healing for 10 seconds
        LivingEntity living = slotContext.getWearer();
        living.attackEntityFrom(MarkDamageSource.BOG_FORGOTTEN, living.getMaxHealth()-1);
        living.addPotionEffect(new EffectInstance(ModEffects.RECOVERING.get(),c.marks_duration.get(),0,false,false));
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_forgotten_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_forgotten_resistance.get().floatValue(), LoreStatHelper.Stat.RESISTANCE, true));
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_forgotten_description_0"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_forgotten_critical_damage.get().floatValue(), LoreStatHelper.Stat.CRITICAL_DAMAGE, true));
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_forgotten_description_1"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_forgotten_description_shift_0"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_forgotten_description_shift_1"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Nonnull
    @Override
    public ICurio.DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean showAttributesTooltip(String identifier, ItemStack stack) {
        return false;
    }
}
