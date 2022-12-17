package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
import java.util.List;

public class BandBasic extends Item implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH, ScaleTypes.HEIGHT, ScaleTypes.STEP_HEIGHT,
            ScaleTypes.REACH, ScaleTypes.VISIBILITY, ScaleTypes.KNOCKBACK};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM, ScaleTypes.ATTACK_SPEED};

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

        int setScale;

        if (stack.getOrCreateTag().getInt("crafted") == 1) {
            setScale = stack.getOrCreateTag().getInt("scale");
        } else {
            setScale = (int) (c.band_basic_scale.get() * 10000.0);
        }

        //set scale
        if (c.multiply_enable.get()) {
            int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, setScale / 10000.0f, 1, 0);
            ScaleHelper.rescaleMultiply(living, scalesInverse, 10000.0f / setScale, 1, scaleDelay);
        } else {
            int scaleDelay = ScaleHelper.rescale(living, scales, setScale / 10000.0f, 0);
            ScaleHelper.rescale(living, scalesInverse, 10000.0f / setScale, scaleDelay);
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

            int setScale;

            if (stack.getOrCreateTag().getInt("crafted") == 1) {
                setScale = stack.getOrCreateTag().getInt("scale");
            } else {
                setScale = (int) (c.band_basic_scale.get() * 10000.0);
            }

            int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, 1, setScale / 10000.0f, 0);
            ScaleHelper.rescaleMultiply(living, scalesInverse, 1, 10000.0f / setScale, scaleDelay);
        } else {
            int scaleDelay = ScaleHelper.rescale(living, scales, 1, 0);
            ScaleHelper.rescale(living, scalesInverse, 1, scaleDelay);
        }
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(stack, level, player);

        stack.getOrCreateTag().putInt("crafted", 1);
        stack.setHoverName(Component.translatable("tooltip.band_of_gigantism.band_basic_reveal"));

        double scaleRange = Math.abs(c.band_basic_max_scale.get() - c.band_basic_min_scale.get());
        double scaleLower = Math.min(c.band_basic_max_scale.get(), c.band_basic_min_scale.get());

        int setScale = (int) (((1.0 - Math.abs(Math.random() + Math.random() - 1.0)) * scaleRange + scaleLower) * 10000.0);
        stack.getOrCreateTag().putInt("scale", setScale);
        stack.getOrCreateTag().putInt("timeLeft", (int) (Math.random() * 22 + 2));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (worldIn.isClientSide) {
            return;
        }

        LivingEntity living = (LivingEntity) entityIn;

        if (worldIn.getGameTime() % 100 == 0 && stack.getOrCreateTag().getInt("crafted") == 1 && ScaleHelper.isDoneScaling(living, scales[1])) {

            int timeLeft = stack.getOrCreateTag().getInt("timeLeft");
            if (timeLeft <= 0) {
                double scaleRange = Math.abs(c.band_basic_max_scale.get() - c.band_basic_min_scale.get());
                double scaleLower = Math.min(c.band_basic_max_scale.get(), c.band_basic_min_scale.get());

                int setScale = (int) (((1.0 - Math.abs(Math.random() + Math.random() - 1.0)) * scaleRange + scaleLower) * 10000.0);

                //set scale
                if (CurioHelper.hasCurio(living, ModItems.GLOBETROTTERS_BAND.get())) {
                    if (c.multiply_enable.get()) {
                        int prevSetScale = stack.getOrCreateTag().getInt("scale");
                        int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, setScale / 10000.0f, prevSetScale / 10000.0f, 0);
                        ScaleHelper.rescaleMultiply(living, scalesInverse, 10000.0f / setScale, 10000.0f / prevSetScale, scaleDelay);
                    } else {
                        int scaleDelay = ScaleHelper.rescale(living, scales, setScale / 10000.0f, 0);
                        ScaleHelper.rescale(living, scalesInverse, 10000.0f / setScale, scaleDelay);
                    }
                }

                stack.getOrCreateTag().putInt("scale", setScale);
                stack.getOrCreateTag().putInt("timeLeft", (int) (Math.random() * 22 + 2));
            } else {
                stack.getOrCreateTag().putInt("timeLeft", timeLeft - 1);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            if (stack.getOrCreateTag().getInt("crafted") == 1) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.band_basic_description_flavor"));
            } else {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.shrink_band_generic_description_flavor"));
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));

                tooltip.add(LoreStatHelper.displayScale(c.band_basic_scale.get().floatValue()));
            }
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.band_apathy_description_shift_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.band_generic_description_shift_1"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        ScaleData scaleData = scales[0].getScaleData(living);
        float scaleBase = scaleData.getBaseScale();

        return ICurioItem.super.canEquip(slotContext, stack) && ScaleHelper.isDoneScaling(living, scales[0]) && (Math.abs(scaleBase - 1) <= 0.001f || c.multiply_enable.get());
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        return ICurioItem.super.canUnequip(slotContext, stack) && ScaleHelper.isDoneScaling(living, scales[0]);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerVariants() { //property function has a new mystery integer I just named "thing" for now
        ItemProperties.register(ModItems.BAND_BASIC.get(), new ResourceLocation(BandOfGigantism.MODID, "crafted"), (stack, world, entity, thing) -> stack.getOrCreateTag().getInt("crafted"));
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 1.0f);
    }
}
