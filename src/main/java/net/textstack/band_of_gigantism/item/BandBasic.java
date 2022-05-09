package net.textstack.band_of_gigantism.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.util.ScaleHelper;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import javax.annotation.Nonnull;

public class BandBasic extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH,ScaleTypes.HEIGHT,ScaleTypes.STEP_HEIGHT,ScaleTypes.DEFENSE,
            ScaleTypes.REACH,ScaleTypes.VISIBILITY,ScaleTypes.KNOCKBACK};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM,ScaleTypes.ATTACK_SPEED};

    public BandBasic(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //set scale
        int scaleDelay = ScaleHelper.rescale(living,scales,c.band_crustaceous_scale.get().floatValue(),0);
        ScaleHelper.rescale(living,scalesInverse,1.0f/c.band_crustaceous_scale.get().floatValue(),scaleDelay);
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
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_IRON,1.0f,1.0f);
    }
}
