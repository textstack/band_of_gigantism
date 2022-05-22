package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
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
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarkDescended extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MarkDescended(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        //deal near-mortal damage, prevent healing
        LivingEntity living = slotContext.entity();
        if (!CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) { //this method is called whenever nbt changes, make sure not to kill for that
            living.hurt(MarkDamageSource.BOG_DESCENDED, living.getMaxHealth() - 1);
            living.addEffect(new MobEffectInstance(ModEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity living = slotContext.entity();

        //either lower the stored position (if the player is lower) or inflict strains of ascent (if player is >5 blocks higher)
        int posY = (int) Math.ceil(living.blockPosition().getY());
        if (stack.getTag() != null) {
            int prevPosY = this.getPosY(stack);

            if (posY < prevPosY) {
                this.setPosY(stack, posY);
            } else if (posY >= prevPosY + c.mark_descended_ascend.get()) {
                int amp;
                if (prevPosY >= 64) amp = 0;
                else if (prevPosY > 48) amp = 1;
                else if (prevPosY > 32) amp = 2;
                else if (prevPosY > 16) amp = 3;
                else if (prevPosY > 0) amp = 4;
                else amp = 5;
                living.addEffect(new MobEffectInstance(ModEffects.STRAINS_OF_ASCENT.get(), c.mark_descended_duration.get(), amp, false, false));
                this.setPosY(stack, posY);
            }
        } else {
            this.setPosY(stack, posY);
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (worldIn.isClientSide) {
            return;
        }

        //update mark's pos when not equipped
        LivingEntity living = (LivingEntity) entityIn;
        if (worldIn.getGameTime() % 10 == 0 && !CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) {
            int posY = (int) Math.ceil(living.blockPosition().getY());
            this.setPosY(stack, posY);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_descended_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_descended_armor.get(), LoreStatHelper.Stat.ARMOR));
            tooltip.add(LoreStatHelper.displayStat(c.mark_descended_regeneration.get().floatValue(), LoreStatHelper.Stat.REGENERATION, true));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_descended_description_shift_0"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_descended_description_shift_1"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("ba5827f8-8e41-4cdf-b5de-78bbe345a559"),
                BandOfGigantism.MODID + ":armor_modifier_descended", c.mark_descended_armor.get(), AttributeModifier.Operation.ADDITION));

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

    //get posy tag
    private int getPosY(ItemStack stack) {
        return stack.getOrCreateTag().getInt("posY");
    }

    //store new posy tag
    private void setPosY(ItemStack stack, int posY) {
        stack.getOrCreateTag().putInt("posY", posY);
    }
}
