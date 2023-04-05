package net.textstack.band_of_gigantism.util;

import net.minecraft.world.entity.Entity;
import net.textstack.band_of_gigantism.config.BOGConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class ScaleHelper {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canScale(BOGConfig c, ScaleType scale) {
        if (ScaleTypes.WIDTH.equals(scale)) {
            return c.width.get();
        } else if (ScaleTypes.HEIGHT.equals(scale)) {
            return c.height.get();
        } else if (ScaleTypes.STEP_HEIGHT.equals(scale)) {
            return c.step_height.get();
        } else if (ScaleTypes.DEFENSE.equals(scale)) {
            return c.defense.get();
        } else if (ScaleTypes.REACH.equals(scale)) {
            return c.reach.get();
        } else if (ScaleTypes.VISIBILITY.equals(scale)) {
            return c.visibility.get();
        } else if (ScaleTypes.KNOCKBACK.equals(scale)) {
            return c.knockback.get();
        } else if (ScaleTypes.HELD_ITEM.equals(scale)) {
            return c.held_item.get();
        } else if (ScaleTypes.ATTACK_SPEED.equals(scale)) {
            return c.attack_speed.get();
        }
        return true;
    }

    /**
     * Scales specified ScaleTypes of the entity to the set value
     *
     * @param entity   the entity to modify
     * @param scales   the list of ScaleTypes to affect
     * @param value    scale to set to
     * @param setDelay set the amount of time to change scale, set to 0 for auto delay
     * @return the delay used for the first ScaleType in the scales array, or the set delay if set
     */
    public static int rescale(Entity entity, ScaleType[] scales, float value, int setDelay) {
        BOGConfig c = BOGConfig.INSTANCE;
        ScaleHelper scaleHelper = new ScaleHelper();

        int tickDelay = Math.max(setDelay, 0);
        for (ScaleType scale : scales) {
            if (!scaleHelper.canScale(c, scale)) {
                continue;
            }

            ScaleData scaleData = scale.getScaleData(entity);
            float scaleTarget = scaleData.getTargetScale();

            float scaleDifference = Math.abs(scaleTarget - value) / Math.max(scaleTarget, value);
            if (scaleDifference > 0.001f) {

                float newScale = Math.max(Math.min(value, c.general_scale_limit.get().floatValue()), 0.001f);

                if (setDelay <= 0 && scale == scales[0]) {
                    tickDelay = (int) Math.max(Math.ceil(Math.abs(scaleTarget - value) * BOGConfig.INSTANCE.scale_speed.get()), 5);
                }

                scaleData.setTargetScale(newScale);
                scaleData.setScaleTickDelay(tickDelay);
            }
        }
        return tickDelay;
    }

    /**
     * Scales specified ScaleTypes of the entity by the specified multiplier
     *
     * @param entity       the entity to modify
     * @param scales       the list of ScaleTypes to affect
     * @param value        scale to set the player to, assuming initial scale is 1
     * @param currentShift the amount the player has already been scaled by; set to 1 for first time
     * @param setDelay     set the amount of time to change scale, set to 0 for auto delay
     * @return the delay used for the first ScaleType in the scales array, or the set delay if set
     */
    public static int rescaleMultiply(Entity entity, ScaleType[] scales, float value, float currentShift, int setDelay) {
        BOGConfig c = BOGConfig.INSTANCE;
        ScaleHelper scaleHelper = new ScaleHelper();

        int tickDelay = Math.max(setDelay, 0);
        for (ScaleType scale : scales) {
            if (!scaleHelper.canScale(c, scale)) {
                continue;
            }

            ScaleData scaleData = scale.getScaleData(entity);
            float scaleTarget = scaleData.getTargetScale();
            float valueAdjust = value * (1.0f / currentShift) * scaleTarget;

            float scaleDifference = Math.abs(scaleTarget - valueAdjust) / Math.max(scaleTarget, valueAdjust);
            if (scaleDifference > 0.001f) {

                float newScale = Math.max(Math.min(valueAdjust, c.general_scale_limit.get().floatValue()), 0.001f);

                if (setDelay <= 0 && scale == scales[0]) {
                    tickDelay = (int) Math.max(Math.ceil(Math.abs(scaleTarget - valueAdjust) * BOGConfig.INSTANCE.scale_speed.get()), 5);
                }

                scaleData.setTargetScale(newScale);
                scaleData.setScaleTickDelay(tickDelay);
            }
        }
        return tickDelay;
    }

    /**
     * Detects if an entity's base scale has reached their target scale
     *
     * @param entity    the entity to query
     * @param reference the ScaleType to query
     * @return true if done, false otherwise
     */
    public static boolean isDoneScaling(Entity entity, ScaleType reference) {

        ScaleData scaleData = reference.getScaleData(entity);
        float scaleTarget = scaleData.getTargetScale();
        float scaleBase = scaleData.getBaseScale();
        float scaleDifference = Math.abs(scaleTarget - scaleBase) / Math.max(scaleTarget, scaleBase);

        // this surely won't cause problems in the future
        return scaleDifference <= 0.001f;
    }
}
