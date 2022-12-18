package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
import java.util.Iterator;
import java.util.List;

public class MaskDiminishment extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH, ScaleTypes.HEIGHT, ScaleTypes.STEP_HEIGHT, ScaleTypes.REACH, ScaleTypes.VISIBILITY};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM};

    public MaskDiminishment(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //reset scale
        if (c.multiply_enable.get()) {
            if (living instanceof Player player) {
                player.getPersistentData().putInt("diminishmentScale", 1000000);
            }
        }
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
            if (living instanceof Player player) {
                int prevScale = player.getPersistentData().getInt("diminishmentScale");
                int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, 1, prevScale / 1000000.0f, 0);
                ScaleHelper.rescaleMultiply(living, scalesInverse, 1, 1000000.0f / prevScale, scaleDelay);
            }
        } else {
            int scaleDelay = ScaleHelper.rescale(living, scales, 1, 0);
            ScaleHelper.rescale(living, scalesInverse, 1, scaleDelay);
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        ScaleData scaleData = scales[0].getScaleData(living);
        float scaleBase = scaleData.getBaseScale();

        if (CurioHelper.hasCurio(living, ModItems.MASK_DIMINISHMENT.get()) || CurioHelper.hasCurio(living, ModItems.GLOBETROTTERS_BAND.get())) {
            return false;
        }

        return ICurioItem.super.canEquip(slotContext, stack) && ScaleHelper.isDoneScaling(living, scales[0]) && (Math.abs(scaleBase - 1) <= 0.001f || c.multiply_enable.get());
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        return ICurioItem.super.canUnequip(slotContext, stack) && ScaleHelper.isDoneScaling(living, scales[0]);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity living = slotContext.entity();

        //scale player based on inventory fill percentage
        if (living.level.getGameTime() % 10 == 0 && ScaleHelper.isDoneScaling(living, scales[0])) {
            if (living instanceof Player player) {

                NonNullList<ItemStack> list = player.getInventory().items;
                Iterator<ItemStack> each = list.iterator();
                float count = 0;
                while (each.hasNext()) {
                    if (!each.next().isEmpty()) {
                        count++;
                    }
                }

                float newScale;

                if (c.mask_diminishment_special.get()) {
                    newScale = c.mask_diminishment_scale.get().floatValue() + (0.95f - c.mask_diminishment_scale.get().floatValue()) * (count / list.size());
                } else {
                    newScale = c.mask_diminishment_scale.get().floatValue();
                }

                if (c.multiply_enable.get()) {
                    int setScale = (int) (newScale * 1000000);
                    int prevScale = player.getPersistentData().getInt("diminishmentScale");
                    int scaleDelay = ScaleHelper.rescaleMultiply(player, scales, setScale / 1000000.0f, prevScale / 1000000.0f, 0);
                    ScaleHelper.rescaleMultiply(player, scalesInverse, 1000000.0f / setScale, 1000000.0f / prevScale, scaleDelay);
                    player.getPersistentData().putInt("diminishmentScale", setScale);
                } else {
                    int scaleDelay = ScaleHelper.rescale(player, scales, newScale, 0);
                    ScaleHelper.rescale(player, scalesInverse, 1 / newScale, scaleDelay);
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mask_diminishment_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(c.mask_diminishment_scale.get().floatValue()));

            if (c.mask_diminishment_special.get()) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mask_diminishment_description_0"));
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mask_diminishment_description_1"));
            }

            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mask_diminishment_description_shift_0"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
    }
}
