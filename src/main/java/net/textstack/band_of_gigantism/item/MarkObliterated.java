package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.item.base.MarkItem;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkObliterated extends MarkItem {

    public MarkObliterated(Properties properties) {
        super(properties, null, ChatFormatting.DARK_RED);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        //check if already equipped
        if (slotContext.entity() instanceof ServerPlayer player) {
            if (player.getPersistentData().getBoolean("obliteratedEquip")) {
                return;
            }
            player.getPersistentData().putBoolean("obliteratedEquip", true);
        }

        //kill
        if (c.mark_obliterated_bypassinvuln.get()) {
            slotContext.entity().hurt(ModDamageSources.BOG_OBLITERATED_INVULN, Float.MAX_VALUE);
        } else {
            slotContext.entity().hurt(ModDamageSources.BOG_OBLITERATED, Float.MAX_VALUE);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof ServerPlayer player) {
            if (!CurioHelper.hasCurio(player, ModItems.MARK_OBLITERATED.get())) {
                player.getPersistentData().putBoolean("obliteratedEquip", false);
                slotContext.entity().hurt(ModDamageSources.BOG_OBLITERATED, Float.MAX_VALUE);
                player.addEffect(new MobEffectInstance(ModEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_obliterated_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE, true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_knockback.get().floatValue(), LoreStatHelper.Stat.KNOCKBACK, true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_health.get(), LoreStatHelper.Stat.MAX_HEALTH));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_armor.get(), LoreStatHelper.Stat.ARMOR));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_armor_toughness.get(), LoreStatHelper.Stat.ARMOR_TOUGHNESS));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_obliterated_description_shift_0"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        //yes, this item ACTUALLY gives buffs.
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("a95728cb-942d-475a-b664-f96b4ba4b0e4"),
                BandOfGigantism.MODID + ":attack_damage_modifier_obl", c.mark_obliterated_damage.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(UUID.fromString("6cc188ba-2451-497b-8031-53d60682d55e"),
                BandOfGigantism.MODID + ":attack_attack_knockback_modifier_obl", c.mark_obliterated_knockback.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("82b7d207-e22e-4d21-930d-fa2f7898453e"),
                BandOfGigantism.MODID + ":attack_armor_modifier_obl", c.mark_obliterated_armor.get(), AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("b043fb33-51d9-48b5-9857-2944a3dcfdae"),
                BandOfGigantism.MODID + ":attack_armor_toughness_modifier_obl", c.mark_obliterated_armor_toughness.get(), AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("5a15b94f-8ec1-4989-a41e-de8e9effd418"),
                BandOfGigantism.MODID + ":attack_max_health_modifier_obl", c.mark_obliterated_health.get(), AttributeModifier.Operation.ADDITION));

        return attributesDefault;
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_DROP;
    }
}
