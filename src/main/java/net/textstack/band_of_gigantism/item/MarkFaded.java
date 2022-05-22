package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarkFaded extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MarkFaded(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        //deal near-mortal damage, prevent healing
        LivingEntity living = slotContext.entity();
        living.hurt(MarkDamageSource.BOG_FADED, living.getMaxHealth() - 1);
        living.addEffect(new MobEffectInstance(ModEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_faded_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_faded_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));
            tooltip.add(LoreStatHelper.displayStat(c.mark_faded_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE, true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_faded_healing.get().floatValue(), LoreStatHelper.Stat.HEALING, true));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_faded_description_shift_1"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("2c52f25b-62b1-439c-a167-dc15ada9a0d4"),
                BandOfGigantism.MODID + ":attack_damage_modifier_faded", c.mark_faded_damage.get(), AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return new ArrayList<>();
    }
}
