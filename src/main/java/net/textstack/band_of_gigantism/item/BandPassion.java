package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
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
import java.util.List;

public class BandPassion extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH,ScaleTypes.HEIGHT,ScaleTypes.STEP_HEIGHT,ScaleTypes.KNOCKBACK,
            ScaleTypes.REACH,ScaleTypes.VISIBILITY,ScaleTypes.MOTION};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM,ScaleTypes.ATTACK_SPEED};

    public BandPassion(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //reset scale
        if (c.multiply_enable.get()) {
            float prevScale = stack.getOrCreateTag().getFloat("setScale");
            int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, 1, prevScale, 0);
            ScaleHelper.rescaleMultiply(living, scalesInverse, 1, 1.0f/prevScale, scaleDelay);
            stack.getOrCreateTag().putFloat("setScale",1);
        } else {
            int scaleDelay = ScaleHelper.rescale(living, scales, 1, 0);
            ScaleHelper.rescale(living, scalesInverse, 1, scaleDelay);
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        ScaleData scaleData = scales[1].getScaleData(living);
        float scaleBase = scaleData.getBaseScale();

        return ICurioItem.super.canEquip(slotContext, stack) && ScaleHelper.isDoneScaling(living,scales[1]) && (Math.abs(scaleBase-1) <= 0.001f || c.multiply_enable.get());
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        return ICurioItem.super.canUnequip(slotContext, stack) && ScaleHelper.isDoneScaling(living,scales[1]);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity living = slotContext.entity();

        //scale player based on xp
        if (living.level.getGameTime() % 10 == 0 && ScaleHelper.isDoneScaling(living,scales[1])) {
            if (living instanceof Player player) {
                float xpProgress = player.experienceProgress + player.experienceLevel;
                float setScale = Math.min(c.band_passion_scale.get().floatValue()+xpProgress*c.band_passion_scale_level.get().floatValue(),
                        c.band_passion_limit_scale.get().floatValue());

                if (c.multiply_enable.get()) {
                    float prevScale = stack.getOrCreateTag().getFloat("setScale");
                    int scaleDelay = ScaleHelper.rescaleMultiply(player, scales, setScale, prevScale, 0);
                    ScaleHelper.rescaleMultiply(player, scalesInverse, 1.0f/setScale, 1.0f/prevScale, scaleDelay);
                    stack.getOrCreateTag().putFloat("setScale",setScale);
                } else {
                    int scaleDelay = ScaleHelper.rescale(player, scales, setScale, 0);
                    ScaleHelper.rescale(player, scalesInverse, 1 / setScale, scaleDelay);
                }
            }
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
        if (worldIn.getGameTime()%10==0&&c.multiply_enable.get()&&!CurioHelper.hasCurio(living, ModItems.BAND_PASSION.get())) {
            stack.getOrCreateTag().putFloat("setScale",1);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_passion_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_passion_description_0"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_passion_description_shift_0"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_generic_description_shift_1"));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GOLD,1.0f,1.0f);
    }
}
