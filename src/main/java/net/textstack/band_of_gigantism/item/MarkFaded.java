package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.item.base.MarkItem;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkFaded extends MarkItem {

    public MarkFaded(Properties properties) {
        super(properties, ModDamageSources.BOG_FADED);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_faded_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_faded_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));
            tooltip.add(LoreStatHelper.displayStat(c.mark_faded_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE, true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_faded_healing.get().floatValue(), LoreStatHelper.Stat.HEALING, true));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_faded_description_shift_1"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("2c52f25b-62b1-439c-a167-dc15ada9a0d4"),
                BandOfGigantism.MODID + ":attack_damage_modifier_faded", c.mark_faded_damage.get(), AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }
}
