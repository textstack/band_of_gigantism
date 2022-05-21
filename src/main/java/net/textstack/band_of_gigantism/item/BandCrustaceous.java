package net.textstack.band_of_gigantism.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModEffects;
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

public class BandCrustaceous extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH,ScaleTypes.HEIGHT,ScaleTypes.STEP_HEIGHT,ScaleTypes.KNOCKBACK,
            ScaleTypes.REACH,ScaleTypes.VISIBILITY,ScaleTypes.ATTACK,ScaleTypes.MOTION};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM,ScaleTypes.ATTACK_SPEED};

    public BandCrustaceous(Properties properties) {
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
        if (c.multiply_enable.get()) {
            if (living instanceof Player player) {
                int setScale = (int)Math.ceil(1000000*c.band_crustaceous_scale.get().floatValue());
                int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, setScale/1000000.0f, 1, 0);
                ScaleHelper.rescaleMultiply(living, scalesInverse, 1000000.0f/setScale, 1, scaleDelay);
                player.getPersistentData().putInt("crustaceousScale", setScale);
            }
        } else {
            int scaleDelay = ScaleHelper.rescale(living,scales,c.band_crustaceous_scale.get().floatValue(),0);
            ScaleHelper.rescale(living,scalesInverse,1.0f/c.band_crustaceous_scale.get().floatValue(),scaleDelay);
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
                int prevScale = player.getPersistentData().getInt("crustaceousScale");
                int scaleDelay = ScaleHelper.rescaleMultiply(living, scales, 1, prevScale / 1000000.0f, 0);
                ScaleHelper.rescaleMultiply(living, scalesInverse, 1, 1000000.0f / prevScale, scaleDelay);
            }
        } else {
            int scaleDelay = ScaleHelper.rescale(living, scales, 1, 0);
            ScaleHelper.rescale(living, scalesInverse, 1, scaleDelay);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity living = slotContext.entity();

        if (living.getLevel().isClientSide) {
            return;
        }

        if (living instanceof Player player) {
            if (player.hasEffect(ModEffects.CRABBY.get())) {
                if (player.getFoodData().getFoodLevel()<18) {
                    player.removeEffect(ModEffects.CRABBY.get());
                }
            } else {
                if (!CurioHelper.hasCurio(player,ModItems.MARK_FADED.get())&&player.getFoodData().getFoodLevel()>=18&&(player.getMaxHealth()-player.getHealth())>0) {
                    player.addEffect(new MobEffectInstance(ModEffects.CRABBY.get(),c.band_crustaceous_duration.get(),0,false,false));
                } else if (player.level.getGameTime()%40==0&& ScaleHelper.isDoneScaling(living,scales[1])) {
                    if (c.multiply_enable.get()) {
                        int prevScale = player.getPersistentData().getInt("crustaceousScale");
                        int setScale = Math.max(prevScale-50000,(int)Math.ceil(c.band_crustaceous_scale.get().floatValue()*1000000.0f));
                        int scaleDelay = ScaleHelper.rescaleMultiply(player, scales, setScale/1000000.0f, prevScale/1000000.0f, 0);
                        ScaleHelper.rescaleMultiply(player, scalesInverse, 1000000.0f/setScale, 1000000.0f/prevScale, scaleDelay);
                        player.getPersistentData().putInt("crustaceousScale",setScale);
                    } else {
                        ScaleData scaleData = ScaleTypes.WIDTH.getScaleData(player);
                        float newScale = Math.max(scaleData.getTargetScale()-0.05f,c.band_crustaceous_scale.get().floatValue());
                        int scaleDelay = ScaleHelper.rescale(player,scales,newScale,0);
                        ScaleHelper.rescale(player,scalesInverse,1.0f/newScale,scaleDelay);
                    }
                }
            }
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
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayScale(c.band_crustaceous_scale.get().floatValue()));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_shift_0"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_shift_1"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            if (Minecraft.getInstance().player != null) {
                if (CurioHelper.hasCurio(Minecraft.getInstance().player, ModItems.MARK_FADED.get())) {
                    tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_faded_0"));
                    tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_faded_1"));
                    tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_faded_2"));
                } else {
                    tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_0"));
                    tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.band_crustaceous_description_1"));
                }
            }
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
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_TURTLE,1.0f,1.0f);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return true;
    }
}
