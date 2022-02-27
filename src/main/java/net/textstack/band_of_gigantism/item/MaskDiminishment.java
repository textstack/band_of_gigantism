package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ICurioItem.super.curioTick(identifier, index, livingEntity, stack);

        //scale player based on inventory fill percentage
        if (livingEntity.world.getGameTime() % 10 == 0 && ScaleHelper.isDoneScaling(livingEntity,scales[1])) {
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;

                NonNullList<ItemStack> list = player.inventory.mainInventory;
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
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mask_diminishment_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(c.mask_diminishment_scale.get().floatValue()));

            if (c.mask_diminishment_special.get()) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mask_diminishment_description_0"));
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mask_diminishment_description_1"));
            }

            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mask_diminishment_description_shift_0"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }
}
