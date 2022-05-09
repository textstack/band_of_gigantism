package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
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
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import net.textstack.band_of_gigantism.util.ScaleHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//formerly "ancient_band_generic"; there may still be some files or references set to this old name
public class BandGlobetrotters extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH,ScaleTypes.HEIGHT,ScaleTypes.STEP_HEIGHT,ScaleTypes.DEFENSE,
            ScaleTypes.REACH,ScaleTypes.VISIBILITY,ScaleTypes.MOTION};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM,ScaleTypes.ATTACK_SPEED};

    public BandGlobetrotters(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //calculate scale
        int storedTime = this.getStoredEnergy(stack);
        float storedTimeMaxToScale = c.band_globetrotters_limit.get() / Math.abs(c.band_globetrotters_limit_scale.get().floatValue()-c.band_globetrotters_scale.get().floatValue());
        float newScale = c.band_globetrotters_scale.get().floatValue()+storedTime/storedTimeMaxToScale;

        //set scale
        int scaleDelay = ScaleHelper.rescale(living,scales,newScale,0);
        ScaleHelper.rescale(living,scalesInverse,1.0f/newScale,scaleDelay);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //reset scale
        int scaleDelay = ScaleHelper.rescale(living,scales,1,0);
        ScaleHelper.rescale(living,scalesInverse,1,scaleDelay);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        ScaleData scaleData = scales[1].getScaleData(living);
        float scaleBase = scaleData.getBaseScale();

        return ICurioItem.super.canEquip(slotContext, stack) && ScaleHelper.isDoneScaling(living,scales[1]) && Math.abs(scaleBase-1) <= 0.001f;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        return ICurioItem.super.canUnequip(slotContext, stack) && ScaleHelper.isDoneScaling(living,scales[1]);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (worldIn.isClientSide) {
            return;
        }

        //tick up the thingie
        LivingEntity theDude = (LivingEntity) entityIn;
        if (worldIn.getGameTime() % 100 == 0 &&!CurioHelper.hasCurio(theDude, ModItems.GLOBETROTTERS_BAND.get())) {
            int storedTime = this.getStoredEnergy(stack);
            if (storedTime < 72000) {
                this.setStoredEnergy(stack, storedTime + 1);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            int storedTime = this.getStoredEnergy(stack);
            float limit = c.band_globetrotters_limit.get();
            float scale = c.band_globetrotters_scale.get().floatValue();
            float limitScale = c.band_globetrotters_limit_scale.get().floatValue();

            float storedTimeMaxToScale = limit / Math.abs(limitScale-scale);
            float newScale = scale+storedTime/storedTimeMaxToScale;
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.globetrotters_band_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(newScale));
            tooltip.add(LoreStatHelper.displayStat(c.band_globetrotters_damage.get().floatValue(), LoreStatHelper.Stat.DAMAGE,true));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.globetrotters_band_description_shift_0"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_generic_description_shift_1"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.globetrotters_band_description_shift_2"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.globetrotters_band_description_shift_3"));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("cf6f717f-99fc-4057-8eac-ffcbd1f465a1"),
                BandOfGigantism.MODID+":attack_damage_modifier", c.band_globetrotters_damage.get(), AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_NETHERITE,1.0f,1.0f);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return new ArrayList<>();
    }

    //get amount of size gain
    private int getStoredEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt("sizeGain");
    }

    //set size gain, limited to 72000 (it takes 100 hours exactly to reach this)
    private void setStoredEnergy(ItemStack stack, int energy) {
        int newStoredTime = Math.min(energy, c.band_globetrotters_limit.get());
        stack.getOrCreateTag().putInt("sizeGain", newStoredTime);
    }
}