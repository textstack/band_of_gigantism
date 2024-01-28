package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkDescended extends MarkItem {

    public MarkDescended(Properties properties) {
        super(properties, ModDamageSources.BOG_DESCENDED, ChatFormatting.BLUE);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        //either lower the stored position (if the player is lower) or inflict strains of ascent (if player is >5 blocks higher)
        int posY = living.blockPosition().getY();
        if (stack.getTag() != null) {
            int prevPosY = this.getPosY(stack);

            if (posY < prevPosY) {
                this.setPosY(stack, posY);
            } else if (posY >= prevPosY + c.mark_descended_ascend.get()) {
                int amp;
                if (prevPosY >= 64) amp = 0;
                else if (prevPosY > 32) amp = 1;
                else if (prevPosY > 0) amp = 2;
                else if (prevPosY > -32) amp = 3;
                else if (prevPosY > -64) amp = 4;
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
            int posY = living.blockPosition().getY();
            this.setPosY(stack, posY);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_descended_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_descended_armor.get(), LoreStatHelper.Stat.ARMOR));
            tooltip.add(LoreStatHelper.displayStat(c.mark_descended_regeneration.get().floatValue(), LoreStatHelper.Stat.REGENERATION, true));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_descended_description_shift_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_descended_description_shift_1"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("ba5827f8-8e41-4cdf-b5de-78bbe345a559"),
                BandOfGigantism.MODID + ":armor_modifier_descended", c.mark_descended_armor.get(), AttributeModifier.Operation.ADDITION));

        return attributesDefault;
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
