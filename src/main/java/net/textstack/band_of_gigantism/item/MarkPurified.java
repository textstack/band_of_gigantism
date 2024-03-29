package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.item.base.MarkItem;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkPurified extends MarkItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MarkPurified(Properties properties) {
        super(properties, ModDamageSources.BOG_PURIFIED, ChatFormatting.GRAY);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.entity();

        //remove the variable modifiers
        AttributeMap map = living.getAttributes();
        map.removeAttributeModifiers(this.createAttributeMap(living));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        //reapply modifiers
        if (living.level.getGameTime() % 10 == 0) {
            if (living instanceof Player player) {
                AttributeMap map = player.getAttributes();
                map.removeAttributeModifiers(this.createAttributeMap(player)); //required to ensure max heatlh is added properly
                map.addTransientAttributeModifiers(this.createAttributeMap(player));
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_purified_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_purified_description_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    //variable modifiers, able to change as the user wears the curio
    private Multimap<Attribute, AttributeModifier> createAttributeMap(LivingEntity player) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        double armor = Math.floor(player.getAttributeValue(Attributes.ARMOR) * c.mark_purified_ratio.get().floatValue() + player.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * c.mark_purified_ratio_tough.get().floatValue());
        armor = armor + armor % 2; //nobody likes a max health value with a partial heart in it

        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("e8c9458e-7db4-41d9-8edf-ae545af2224a"),
                BandOfGigantism.MODID + ":attack_max_health_modifier_purified", armor, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("0fa8852d-e782-4749-80c2-17164a3fbf1e"),
                BandOfGigantism.MODID + ":attack_armor_modifier_purified", -1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        attributesDefault.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("9a37453b-68f7-41db-add7-d08252df6d3d"),
                BandOfGigantism.MODID + ":attack_armor_toughness_modifier_purified", -1, AttributeModifier.Operation.MULTIPLY_TOTAL));

        return attributesDefault;
    }
}
