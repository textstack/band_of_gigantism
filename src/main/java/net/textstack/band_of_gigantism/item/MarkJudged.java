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
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkJudged extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    /*int marks_duration = c.marks_duration.get();
    float mark_judged_damage = c.mark_judged_damage.get().floatValue();
    float mark_judged_speed = c.mark_judged_speed.get().floatValue();
    boolean description_enable = c.description_enable.get();*/

    public MarkJudged(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.getWearer();

        //deal near-mortal damage, prevent healing for 10 seconds
        living.attackEntityFrom(MarkDamageSource.BOG_JUDGED, living.getMaxHealth()-1);
        living.addPotionEffect(new EffectInstance(ModEffects.RECOVERING.get(),c.marks_duration.get(),0,false,false));
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_judged_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_judged_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE));
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_judged_description_0"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_judged_speed.get().floatValue(), LoreStatHelper.Stat.SPEED, true));
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_judged_description_1"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_judged_description_shift_0"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_judged_description_shift_1"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("a95728cb-942d-475a-b664-f96b4ba4b0e4"),
                BandOfGigantism.MODID+":attack_damage_modifier_obl", c.mark_judged_damage.get(), AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("6cc188ba-2451-497b-8031-53d60682d55e"),
                BandOfGigantism.MODID+":attack_attack_knockback_modifier_obl", c.mark_judged_speed.get(), AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
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
