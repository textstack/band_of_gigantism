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

public class MarkJudged extends MarkItem {

    public MarkJudged(Properties properties) {
        super(properties, ModDamageSources.BOG_JUDGED);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_judged_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_judged_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE));
            tooltip.add(LoreStatHelper.displayStat(c.mark_judged_speed.get().floatValue(), LoreStatHelper.Stat.SPEED, true));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_judged_description_shift_1"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("a95728cb-942d-475a-b664-f96b4ba4b0e4"),
                BandOfGigantism.MODID + ":attack_damage_modifier_obl", c.mark_judged_damage.get(), AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("6cc188ba-2451-497b-8031-53d60682d55e"),
                BandOfGigantism.MODID + ":attack_attack_knockback_modifier_obl", c.mark_judged_speed.get(), AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }
}
