package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarkPurified extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MarkPurified(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.entity();

        //deal near-mortal damage, prevent healing for 10 seconds
        living.hurt(ModDamageSources.BOG_PURIFIED, living.getMaxHealth() - 1);
        living.addEffect(new MobEffectInstance(ModEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));

        //remove the variable modifiers
        AttributeMap map = living.getAttributes();
        map.removeAttributeModifiers(this.createAttributeMap(living));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return ICurioItem.super.canEquip(slotContext, stack) && !CurioHelper.hasCurio(slotContext.entity(), ModItems.MARK_PURIFIED.get());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

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
