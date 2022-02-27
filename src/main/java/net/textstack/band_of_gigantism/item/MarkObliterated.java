package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkObliterated extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    public MarkObliterated(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {

        //kill
        slotContext.getWearer().attackEntityFrom(MarkDamageSource.BOG_OBLITERATED, Float.MAX_VALUE);

        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_obliterated_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE,true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_knockback.get().floatValue(), LoreStatHelper.Stat.KNOCKBACK,true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_health.get(), LoreStatHelper.Stat.MAX_HEALTH));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_armor.get(), LoreStatHelper.Stat.ARMOR));
            tooltip.add(LoreStatHelper.displayStat(c.mark_obliterated_armor_toughness.get(), LoreStatHelper.Stat.ARMOR_TOUGHNESS));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_obliterated_description_shift_0"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        //yes, this item ACTUALLY gives buffs.
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("a95728cb-942d-475a-b664-f96b4ba4b0e4"),
                BandOfGigantism.MODID+":attack_damage_modifier_obl", c.mark_obliterated_damage.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(UUID.fromString("6cc188ba-2451-497b-8031-53d60682d55e"),
                BandOfGigantism.MODID+":attack_attack_knockback_modifier_obl", c.mark_obliterated_knockback.get(), AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("82b7d207-e22e-4d21-930d-fa2f7898453e"),
                BandOfGigantism.MODID+":attack_armor_modifier_obl", c.mark_obliterated_armor.get(), AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("b043fb33-51d9-48b5-9857-2944a3dcfdae"),
                BandOfGigantism.MODID+":attack_armor_toughness_modifier_obl", c.mark_obliterated_armor_toughness.get(), AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("5a15b94f-8ec1-4989-a41e-de8e9effd418"),
                BandOfGigantism.MODID+":attack_max_health_modifier_obl", c.mark_obliterated_health.get(), AttributeModifier.Operation.ADDITION));

        return attributesDefault;
    }

    @Nonnull
    @Override
    public ICurio.DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_DROP;
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
