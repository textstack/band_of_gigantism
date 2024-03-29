package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.item.base.MarkItem;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkUnknown extends MarkItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MarkUnknown(Properties properties) {
        super(properties, ModDamageSources.BOG_UNKNOWN, ChatFormatting.GREEN);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.entity();
        if (!CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {
            AttributeMap map = living.getAttributes();
            map.removeAttributeModifiers(this.createAttributeMap(stack));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity living = slotContext.entity();

        if (living.level.getGameTime() % 20 == 0) {
            if (living instanceof Player player) {

                //inflict effect
                int[] random = effectValues(stack);
                MobEffect effect = switch (random[0]) {
                    case 0 -> MobEffects.POISON; //
                    case 1 -> MobEffects.SATURATION;
                    case 2 -> MobEffects.DAMAGE_RESISTANCE; //
                    case 3 -> MobEffects.DAMAGE_BOOST;
                    case 4 -> MobEffects.DIG_SLOWDOWN; //
                    case 5 -> MobEffects.DIG_SPEED;
                    case 6 -> MobEffects.HUNGER; //
                    case 7 -> MobEffects.SLOW_FALLING;
                    case 8 -> MobEffects.WEAKNESS; //
                    case 9 -> MobEffects.NIGHT_VISION;
                    default -> MobEffects.GLOWING;
                };
                living.addEffect(new MobEffectInstance(effect, 220, random[1], false, false));

                //reapply modifiers
                AttributeMap map = player.getAttributes();
                map.addTransientAttributeModifiers(this.createAttributeMap(stack));
            }
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (worldIn.isClientSide) {
            return;
        }

        //tick down mr. timer, resets the seed at 0
        if (worldIn.getGameTime() % 20 == 0) {
            int storedTime = this.getInt(stack);
            if (storedTime > 0) {
                this.setInt(stack, "timeLeft", storedTime - 1);
            } else {
                this.setInt(stack, "timeLeft", c.mark_unknown_time.get() - 1);
                this.setInt(stack, "random", (int) (Math.random() * 12800)); //"random" IS the seed, fyi
            }
        }
    }

    //i can't believe it's all tooltip!
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {

            int[] randomAttributes = attributeValues(stack);
            int[] randomEffects = effectValues(stack);
            int randomRegen = regenValue(stack);
            int storedTime = this.getInt(stack);

            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));

            //time
            if (storedTime >= 60) {
                int displayTime = 1 + storedTime / 60;
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_0_minutes", "§6" + displayTime));
            } else {
                if (storedTime == 0) {
                    tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_0_second"));
                } else {
                    int displayTime = 1 + storedTime;
                    tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_0_seconds", "§6" + displayTime));
                }
            }

            //health
            if (randomAttributes[0] > 0) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_1_positive", "§6" + randomAttributes[0]));
            } else if (randomAttributes[0] < 0) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_1_negative", "§6" + randomAttributes[0]));
            }

            //speed
            if (randomAttributes[1] > 0) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_2_positive", "§6" + randomAttributes[1] + "%"));
            } else if (randomAttributes[1] < 0) {
                tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_2_negative", "§6" + randomAttributes[1] + "%"));
            }

            //regen
            switch (randomRegen) {
                case 5, 6, 1 -> tooltip.add(LoreStatHelper.displayStat(-c.mark_unknown_healing.get().floatValue(), LoreStatHelper.Stat.HEALING, true));
                case 7, 8, 2 -> tooltip.add(LoreStatHelper.displayStat(c.mark_unknown_healing.get().floatValue(), LoreStatHelper.Stat.HEALING, true));
            }

            //flat damage resist/vuln
            switch (randomRegen) {
                case 5, 7, 3 -> tooltip.add(LoreStatHelper.displayStat(-c.mark_unknown_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));
                case 6, 8, 4 -> tooltip.add(LoreStatHelper.displayStat(c.mark_unknown_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));
            }

            //effect
            String effect = switch (randomEffects[0]) {
                case 0 -> "effect.minecraft.poison";
                case 1 -> "effect.minecraft.saturation";
                case 2 -> "effect.minecraft.resistance";
                case 3 -> "effect.minecraft.strength";
                case 4 -> "effect.minecraft.mining_fatigue";
                case 5 -> "effect.minecraft.haste";
                case 6 -> "effect.minecraft.hunger";
                case 7 -> "effect.minecraft.slow_falling";
                case 8 -> "effect.minecraft.weakness";
                case 9 -> "effect.minecraft.night_vision";
                default -> "tooltip.band_of_gigantism.obfuscated";
            };
            MutableComponent effectComponent = Component.translatable(effect).withStyle(ChatFormatting.GOLD);

            String amp = switch (randomEffects[1]) {
                case 1 -> "§6II ";
                case 2 -> "§6III ";
                default -> "";
            };
            tooltip.add(effectComponent.append(Component.translatable("tooltip.band_of_gigantism.mark_unknown_description_shift_4", amp)));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("9983936a-d314-4e1a-8c12-84e792e0f0f9"),
                BandOfGigantism.MODID + ":luck_modifier_unknown", 1, AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    //variable modifiers, able to change as the user wears the curio
    private Multimap<Attribute, AttributeModifier> createAttributeMap(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        int[] random = attributeValues(stack);

        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("b2ae2c86-fcdb-478a-b8f8-24d452ea716f"),
                BandOfGigantism.MODID + ":attack_max_health_modifier_unknown", random[0], AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("b2ae2c86-fcdb-478a-b8f8-24d452ea716f"),
                BandOfGigantism.MODID + ":attack_movement_speed_modifier_unknown", random[1] / 100.0f, AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    //converts random into directly usable values for the modifiers
    private int[] attributeValues(ItemStack stack) {

        int[] random = getRandom(stack);
        int[] output = {0, 0};

        output[0] = (int) ((random[0] / 6400.0f) * c.mark_unknown_health.get() - c.mark_unknown_health.get());
        if (output[0] > 0) {
            output[0] = output[0] * 2;
        } else {
            output[0] = output[0] + output[0] % 2;
        }

        output[1] = (int) (((random[1] / 6400.0f) * c.mark_unknown_speed.get().floatValue() - c.mark_unknown_speed.get().floatValue()) * 100);

        return output;
    }

    //converts random into directly usable values for effects
    private int[] effectValues(ItemStack stack) {

        int[] random = getRandom(stack);
        int[] output = {0, 0};

        output[0] = random[5] + 2 * (int) Math.floor(Math.log10((random[3] / 1828.57142858f) + 1) / 0.181843587945f); //effect
        output[1] = 2 - (int) Math.floor(Math.log10((random[4] / 1828.57142858f) + 1) / 0.301029995664f); //effect amp

        return output;
    }

    //converts random into directly usable values for regen/resist
    public static int regenValue(ItemStack stack) {

        int[] random = getRandom(stack);

        return random[2] / 1280;
    }

    //generates 6 pseudorandom values from the seed
    private static int[] getRandom(ItemStack stack) {
        int randomSeed = stack.getOrCreateTag().getInt("random");
        int[] random = {0, 0, 0, 0, 0, 0};
        random[0] = ((randomSeed * 8121) + 28411) % 12800;
        random[1] = ((random[0] * 8121) + 28411) % 12800;
        random[2] = ((random[1] * 8121) + 28411) % 12800;
        random[3] = ((random[2] * 8121) + 28411) % 12800;
        random[4] = ((random[3] * 8121) + 28411) % 12800;
        random[5] = ((random[4] * 8121) + 28411) % 2;
        return random;
    }

    //get time left
    private int getInt(ItemStack stack) {
        return stack.getOrCreateTag().getInt("timeLeft");
    }

    //set int (both for seed and time)
    private void setInt(ItemStack stack, String tag, int set) {
        stack.getOrCreateTag().putInt(tag, set);
    }
}
