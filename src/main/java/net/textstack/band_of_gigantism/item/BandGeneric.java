package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
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
import java.util.List;


public class BandGeneric extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    private final int itemVal;
    private final boolean setShiny;
    private final SoundEvent setEquipSound;
    private final ScaleType[] scales = {ScaleTypes.WIDTH, ScaleTypes.HEIGHT, ScaleTypes.STEP_HEIGHT, ScaleTypes.DEFENSE,
            ScaleTypes.REACH, ScaleTypes.VISIBILITY, ScaleTypes.KNOCKBACK};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM, ScaleTypes.ATTACK_SPEED};

    public BandGeneric(Properties properties, int itemVal, boolean isShiny, SoundEvent equipSound) {
        super(properties);
        this.itemVal = itemVal;
        this.setShiny = isShiny;
        this.setEquipSound = equipSound;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //check if already equipped
        if (slotContext.entity() instanceof Player player) {
            switch (itemVal) {
                case 0 -> {
                    if (player.getPersistentData().getBoolean("genericEquip")) {
                        return;
                    }
                    player.getPersistentData().putBoolean("genericEquip", true);
                }
                case 1 -> {
                    if (player.getPersistentData().getBoolean("lesserEquip")) {
                        return;
                    }
                    player.getPersistentData().putBoolean("lesserEquip", true);
                }
                case 2 -> {
                    if (player.getPersistentData().getBoolean("shrinkEquip")) {
                        return;
                    }
                    player.getPersistentData().putBoolean("shrinkEquip", true);
                }
            }
        }

        float setScale = switch (itemVal) {
            case 0 -> c.band_generic_scale.get().floatValue();
            case 1 -> c.lesser_band_generic_scale.get().floatValue();
            case 2 -> c.shrink_band_generic_scale.get().floatValue();
            default -> 1;
        };

        //set scale
        if (c.multiply_enable.get()) {
            int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, setScale, 1, 0); //this weird thing with scaleDelay ensures these have the same delay
            ScaleHelper.rescaleMultiply(living, scalesInverse, 1.0f / setScale, 1, scaleDelay);
        } else {
            int scaleDelay = ScaleHelper.rescale(living, scales, setScale, 0); //this weird thing with scaleDelay ensures these have the same delay
            ScaleHelper.rescale(living, scalesInverse, 1 / setScale, scaleDelay);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        //clear tag from list
        if (slotContext.entity() instanceof Player player) {
            switch (itemVal) {
                case 0 -> player.getPersistentData().putBoolean("genericEquip", false);
                case 1 -> player.getPersistentData().putBoolean("lesserEquip", false);
                case 2 -> player.getPersistentData().putBoolean("shrinkEquip", false);
            }
        }

        float setScale = switch (itemVal) {
            case 0 -> c.band_generic_scale.get().floatValue();
            case 1 -> c.lesser_band_generic_scale.get().floatValue();
            case 2 -> c.shrink_band_generic_scale.get().floatValue();
            default -> 1;
        };

        //reset scale
        if (c.multiply_enable.get()) {
            int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, 1, setScale, 0); //this weird thing with scaleDelay ensures these have the same delay
            ScaleHelper.rescaleMultiply(living, scalesInverse, 1, 1.0f / setScale, scaleDelay);
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

        if (CurioHelper.hasCurio(living, ModItems.GLOBETROTTERS_BAND.get()) || CurioHelper.hasCurio(living, stack.getItem())) {
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
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {

            float setScale = switch (itemVal) {
                case 0 -> c.band_generic_scale.get().floatValue();
                case 1 -> c.lesser_band_generic_scale.get().floatValue();
                case 2 -> c.shrink_band_generic_scale.get().floatValue();
                default -> 1;
            };

            if (setScale < 1) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.shrink_band_generic_description_flavor"));
            } else {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.band_generic_description_flavor"));
            }
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(setScale));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.band_generic_description_shift_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.band_generic_description_shift_1"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(setEquipSound, 1.0f, 1.0f);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return setShiny;
    }
}
