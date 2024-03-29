package net.textstack.band_of_gigantism.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.ScaleHelper;
import org.jetbrains.annotations.NotNull;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class CrabbyEffect extends MobEffect {

    final BOGConfig c = BOGConfig.INSTANCE;

    private final ScaleType[] scales = {ScaleTypes.WIDTH, ScaleTypes.HEIGHT, ScaleTypes.STEP_HEIGHT, ScaleTypes.KNOCKBACK,
            ScaleTypes.REACH, ScaleTypes.VISIBILITY, ScaleTypes.ATTACK, ScaleTypes.MOTION};
    private final ScaleType[] scalesInverse = {ScaleTypes.HELD_ITEM, ScaleTypes.ATTACK_SPEED};

    public CrabbyEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entityLivingBaseIn, int amplifier) {
        super.applyEffectTick(entityLivingBaseIn, amplifier);

        float healthLost = entityLivingBaseIn.getMaxHealth() - entityLivingBaseIn.getHealth();

        if (entityLivingBaseIn instanceof Player player) {
            FoodData foodData = player.getFoodData();
            int foodLevel = foodData.getFoodLevel();
            if (foodLevel >= 18) {
                float saturation = foodData.getSaturationLevel();
                float foodLevelSubtract = foodLevel + saturation - healthLost;
                float healReduce = Math.min(foodLevelSubtract - 17.0f, 0.0f);

                foodData.setSaturation(Math.max(saturation - healthLost, 0.0f));
                foodData.setFoodLevel((int) Math.max(foodLevelSubtract, 17.0f));

                if (c.band_crustaceous_heal.get()) {
                    float heal = healthLost + healReduce;
                    heal = net.minecraftforge.event.ForgeEventFactory.onLivingHeal(entityLivingBaseIn, heal);
                    if (heal > 0.0f) {
                        float f = entityLivingBaseIn.getHealth();
                        if (f > 0.0f) {
                            float healDiv = 1.0f - f / (f + heal);
                            entityLivingBaseIn.setHealth(Math.min(f + heal, entityLivingBaseIn.getMaxHealth()));

                            if (CurioHelper.hasCurio(entityLivingBaseIn, ModItems.BAND_CRUSTACEOUS.get()) && heal >= 6.0f) {
                                if (c.multiply_enable.get()) {
                                    int prevScale = player.getPersistentData().getInt("crustaceousScale");
                                    int setScale = (int) (Math.min(prevScale * (1 + healDiv), c.band_crustaceous_limit_scale.get().floatValue() * 1000000.0f));
                                    int scaleDelay = ScaleHelper.rescaleMultiply(player, scales, setScale / 1000000.0f, prevScale / 1000000.0f, 0);
                                    ScaleHelper.rescaleMultiply(player, scalesInverse, 1000000.0f / setScale, 1000000.0f / prevScale, scaleDelay);
                                    player.getPersistentData().putInt("crustaceousScale", setScale);
                                } else {
                                    ScaleData scaleData = ScaleTypes.WIDTH.getScaleData(entityLivingBaseIn);
                                    float newScale = Math.min(scaleData.getTargetScale() * (1 + healDiv), c.band_crustaceous_limit_scale.get().floatValue());
                                    int scaleDelay = ScaleHelper.rescale(entityLivingBaseIn, scales, newScale, 0);
                                    ScaleHelper.rescale(entityLivingBaseIn, scalesInverse, 1.0f / newScale, scaleDelay);
                                }
                            }
                        }
                    }
                } else {
                    float healthLostDiv = 1.0f - entityLivingBaseIn.getHealth() / (entityLivingBaseIn.getMaxHealth() + healReduce);
                    entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth() + healReduce);

                    if (CurioHelper.hasCurio(entityLivingBaseIn, ModItems.BAND_CRUSTACEOUS.get()) && healthLost >= 6.0f) {
                        if (c.multiply_enable.get()) {
                            int prevScale = player.getPersistentData().getInt("crustaceousScale");
                            int setScale = (int) (Math.min(prevScale * (1 + healthLostDiv), c.band_crustaceous_limit_scale.get().floatValue() * 1000000.0f));
                            int scaleDelay = ScaleHelper.rescaleMultiply(player, scales, setScale / 1000000.0f, prevScale / 1000000.0f, 0);
                            ScaleHelper.rescaleMultiply(player, scalesInverse, 1000000.0f / setScale, 1000000.0f / prevScale, scaleDelay);
                            player.getPersistentData().putInt("crustaceousScale", setScale);
                        } else {
                            ScaleData scaleData = ScaleTypes.WIDTH.getScaleData(entityLivingBaseIn);
                            float newScale = Math.min(scaleData.getTargetScale() * (1 + healthLostDiv), c.band_crustaceous_limit_scale.get().floatValue());
                            int scaleDelay = ScaleHelper.rescale(entityLivingBaseIn, scales, newScale, 0);
                            ScaleHelper.rescale(entityLivingBaseIn, scalesInverse, 1.0f / newScale, scaleDelay);
                        }
                    }
                }
            }
        } else {
            if (c.band_crustaceous_heal.get()) {
                float heal = healthLost;
                heal = net.minecraftforge.event.ForgeEventFactory.onLivingHeal(entityLivingBaseIn, heal);
                if (heal > 0.0f) {
                    float f = entityLivingBaseIn.getHealth();
                    if (f > 0.0f) {
                        entityLivingBaseIn.setHealth(Math.min(f + heal, entityLivingBaseIn.getMaxHealth()));
                    }
                }
            } else {
                entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration == 1;
    }
}