package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.textstack.band_of_gigantism.util.ScaleHelper;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class BandGeneric extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    private final int itemVal;
    private final boolean setShiny;
    private final SoundEvent setEquipSound;
    private final ScaleType[] scales = {ScaleTypes.WIDTH,ScaleTypes.HEIGHT,ScaleTypes.STEP_HEIGHT,ScaleTypes.DEFENSE,
            ScaleTypes.REACH,ScaleTypes.VISIBILITY,ScaleTypes.KNOCKBACK};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM,ScaleTypes.ATTACK_SPEED};

    public BandGeneric(Properties properties, int itemVal, boolean isShiny, SoundEvent equipSound) {
        super(properties);
        this.itemVal = itemVal;
        this.setShiny = isShiny;
        this.setEquipSound = equipSound;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {

        float setScale = 1;
        switch (itemVal) {
            case 0: setScale = c.band_generic_scale.get().floatValue();
                break;
            case 1: setScale = c.lesser_band_generic_scale.get().floatValue();
                break;
            case 2: setScale = c.shrink_band_generic_scale.get().floatValue();
        }

        //set scale
        LivingEntity player = slotContext.getWearer();
        int scaleDelay = ScaleHelper.rescale(player,scales,setScale,0); //this weird thing with scaleDelay ensures these have the same delay
        ScaleHelper.rescale(player,scalesInverse,1/setScale,scaleDelay);

        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {

        //reset scale
        LivingEntity player = slotContext.getWearer();
        int scaleDelay = ScaleHelper.rescale(player,scales,1,0);
        ScaleHelper.rescale(player,scalesInverse,1,scaleDelay);

        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        ScaleData scaleData = scales[1].getScaleData(livingEntity);
        float scaleBase = scaleData.getBaseScale();
        return ICurioItem.super.canEquip(identifier, livingEntity, stack) && ScaleHelper.isDoneScaling(livingEntity,scales[1]) && Math.abs(scaleBase-1) <= 0.001f;
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return ICurioItem.super.canUnequip(identifier, livingEntity, stack) && ScaleHelper.isDoneScaling(livingEntity,scales[1]);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {

            float setScale = 1;
            switch (itemVal) {
                case 0: setScale = c.band_generic_scale.get().floatValue();
                    break;
                case 1: setScale = c.lesser_band_generic_scale.get().floatValue();
                    break;
                case 2: setScale = c.shrink_band_generic_scale.get().floatValue();
            }

            if (setScale < 1) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shrink_band_generic_description_flavor"));
            } else {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.band_generic_description_flavor"));
            }
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(setScale));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.band_generic_description_shift_0"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.band_generic_description_shift_1"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(setEquipSound,1.0f,1.0f);
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return setShiny;
    }
}
