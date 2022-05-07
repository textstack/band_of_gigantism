package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import net.textstack.band_of_gigantism.util.ScaleHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class MaskDiminishment extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH,ScaleTypes.HEIGHT,ScaleTypes.STEP_HEIGHT,ScaleTypes.REACH,ScaleTypes.VISIBILITY};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM};

    public MaskDiminishment(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {

        //reset scale
        LivingEntity player = slotContext.entity();
        int scaleDelay = ScaleHelper.rescale(player,scales,1,0);
        ScaleHelper.rescale(player,scalesInverse,1,scaleDelay);

        ICurioItem.super.onUnequip(slotContext, newStack, stack);
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
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity living = slotContext.entity();

        //scale player based on inventory fill percentage
        if (living.level.getGameTime() % 10 == 0 && ScaleHelper.isDoneScaling(living,scales[1])) {
            if (living instanceof Player player) {

                NonNullList<ItemStack> list = player.getInventory().items;
                Iterator<ItemStack> each = list.iterator();
                float count = 0;
                while (each.hasNext()) {
                    if (!each.next().isEmpty()) {
                        count++;
                    }
                }

                float setScale;

                if (c.mask_diminishment_special.get()) {
                    setScale = c.mask_diminishment_scale.get().floatValue() + (1 - c.mask_diminishment_scale.get().floatValue()) * (count / list.size());
                } else {
                    setScale = c.mask_diminishment_scale.get().floatValue();
                }

                int scaleDelay = ScaleHelper.rescale(player,scales,setScale,0);
                ScaleHelper.rescale(player,scalesInverse,1/setScale,scaleDelay);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mask_diminishment_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(c.mask_diminishment_scale.get().floatValue()));

            if (c.mask_diminishment_special.get()) {
                tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
                tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mask_diminishment_description_0"));
                tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mask_diminishment_description_1"));
            }

            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mask_diminishment_description_shift_0"));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return true;
    }
}
