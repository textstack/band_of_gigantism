package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.misc.MarkDamageSource;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MarkUnknown extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    public MarkUnknown(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        LivingEntity living = slotContext.getWearer();
        if (!CurioHelper.hasCurio(living, ModItems.MARK_UNKNOWN.get())) {

            //deal near-mortal damage, prevent healing for 10 seconds
            living.attackEntityFrom(MarkDamageSource.BOG_UNKNOWN, living.getMaxHealth()-1);
            living.addPotionEffect(new EffectInstance(ModEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));

            //remove the variable modifiers
            AttributeModifierManager map = living.getAttributeManager();
            map.removeModifiers(this.createAttributeMap(stack));
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ICurioItem.super.curioTick(identifier, index, livingEntity, stack);

        if (livingEntity.world.getGameTime() % 20 == 0) {
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;

                //inflict effect
                int[] random = effectValues(stack);
                Effect effect = Effects.GLOWING;
                switch (random[0]) {
                    case 0: effect = Effects.POISON; //
                            break;
                    case 1: effect = Effects.SATURATION;
                            break;
                    case 2: effect = Effects.RESISTANCE; //
                            break;
                    case 3: effect = Effects.STRENGTH;
                            break;
                    case 4: effect = Effects.MINING_FATIGUE; //
                            break;
                    case 5: effect = Effects.HASTE;
                            break;
                    case 6: effect = Effects.HUNGER; //
                            break;
                    case 7: effect = Effects.SLOW_FALLING;
                            break;
                    case 8: effect = Effects.WEAKNESS; //
                            break;
                    case 9: effect = Effects.NIGHT_VISION;
                }
                livingEntity.addPotionEffect(new EffectInstance(effect,220,random[1],false,false));

                //reapply modifiers
                AttributeModifierManager map = player.getAttributeManager();
                map.reapplyModifiers(this.createAttributeMap(stack));
            }
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (worldIn.isRemote) {
            return;
        }

        //tick down mr. timer, resets the seed at 0
        if (worldIn.getGameTime() % 20 == 0) {
            int storedTime = this.getInt(stack);
            if (storedTime > 0) {
                this.setInt(stack,"timeLeft",storedTime - 1);
            } else {
                this.setInt(stack,"timeLeft",c.mark_unknown_time.get()-1);
                this.setInt(stack,"random",(int) (Math.random()*12800)); //"random" IS the seed, fyi
            }
        }
    }

    //i can't believe it's all tooltip!
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {

            int[] randomAttributes = attributeValues(stack);
            int[] randomEffects = effectValues(stack);
            int randomRegen = regenValue(stack);
            int storedTime = this.getInt(stack);

            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_0"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));

            //time
            if (storedTime>=60) {
                int displayTime = 1+storedTime/60;
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_0_minutes","\u00A76" + displayTime));
            } else {
                if (storedTime == 0) {
                    tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_0_second"));
                } else {
                    int displayTime = 1+storedTime;
                    tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_0_seconds","\u00A76" + displayTime));
                }
            }

            //health
            if (randomAttributes[0]>0) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_1_positive","\u00A76" + randomAttributes[0]));
            } else if (randomAttributes[0]<0) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_1_negative","\u00A76" + randomAttributes[0]));
            }

            //speed
            if (randomAttributes[1]>0) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_2_positive","\u00A76" + randomAttributes[1] + "%"));
            } else if (randomAttributes[1]<0) {
                tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_2_negative","\u00A76" + randomAttributes[1] + "%"));
            }

            //regen
            switch (randomRegen) {
                case 5:
                case 6:
                case 1: tooltip.add(LoreStatHelper.displayStat(-c.mark_unknown_healing.get().floatValue(), LoreStatHelper.Stat.HEALING, true));
                    break;
                case 7:
                case 8:
                case 2: tooltip.add(LoreStatHelper.displayStat(c.mark_unknown_healing.get().floatValue(), LoreStatHelper.Stat.HEALING, true));
            }

            //flat damage resist/vuln
            switch (randomRegen) {
                case 5:
                case 7:
                case 3: tooltip.add(LoreStatHelper.displayStat(-c.mark_unknown_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));
                    break;
                case 6:
                case 8:
                case 4: tooltip.add(LoreStatHelper.displayStat(c.mark_unknown_flat_resistance.get().floatValue(), LoreStatHelper.Stat.FLAT_RESISTANCE));
            }

            //effect
            //don't know how this could be localized
            String effect = "";
            switch (randomEffects[0]) {
                case 0: effect = "\u00A76Poison";
                    break;
                case 1: effect = "\u00A76Saturation";
                    break;
                case 2: effect = "\u00A76Resistance";
                    break;
                case 3: effect = "\u00A76Strength";
                    break;
                case 4: effect = "\u00A76Mining Fatigue";
                    break;
                case 5: effect = "\u00A76Haste";
                    break;
                case 6: effect = "\u00A76Hunger";
                    break;
                case 7: effect = "\u00A76Slow Falling";
                    break;
                case 8: effect = "\u00A76Weakness";
                    break;
                case 9: effect = "\u00A76Night Vision";
            }
            String amp = "";
            switch(randomEffects[1]) {
                case 1: amp = "\u00A76II ";
                    break;
                case 2: amp = "\u00A76III ";
                    break;
            }
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_unknown_description_shift_4",effect,amp));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("9983936a-d314-4e1a-8c12-84e792e0f0f9"),
                BandOfGigantism.MODID + ":luck_modifier_unknown", 1, AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    @Nonnull
    @Override
    public ICurio.DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean showAttributesTooltip(String identifier, ItemStack stack) {
        return false;
    }

    //variable modifiers, able to change as the user wears the curio
    private Multimap<Attribute, AttributeModifier> createAttributeMap(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        int[] random = attributeValues(stack);

        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("b2ae2c86-fcdb-478a-b8f8-24d452ea716f"),
                BandOfGigantism.MODID+":attack_max_health_modifier_unknown", random[0], AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("b2ae2c86-fcdb-478a-b8f8-24d452ea716f"),
                BandOfGigantism.MODID+":attack_movement_speed_modifier_unknown", random[1]/100.0f, AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    //converts random into directly usable values for the modifiers
    private int[] attributeValues(ItemStack stack) {

        int[] random = getRandom(stack);
        int[] output = {0,0};

        output[0] = (int) ((random[0]/6400.0f)*c.mark_unknown_health.get() - c.mark_unknown_health.get());
        if (output[0] > 0) {
            output[0] = output[0] * 2;
        } else {
            output[0] = output[0] + output[0] % 2;
        }

        output[1] = (int) (((random[1]/6400.0f)*c.mark_unknown_speed.get().floatValue() - c.mark_unknown_speed.get().floatValue())*100);

        return output;
    }

    //converts random into directly usable values for effects
    private int[] effectValues(ItemStack stack) {

        int[] random = getRandom(stack);
        int[] output = {0,0};

        output[0] = random[5] + 2 * (int) Math.floor(Math.log10((random[3] / 1828.57142858f) + 1) / 0.181843587945f); //effect
        output[1] = 2 - (int) Math.floor(Math.log10((random[4]/1828.57142858f) + 1) / 0.301029995664f); //effect amp

        return output;
    }

    //converts random into directly usable values for regen/resist
    public static int regenValue(ItemStack stack) {

        int[] random = getRandom(stack);

        return random[2]/1280;
    }

    //generates 6 pseudorandom values from the seed
    private static int[] getRandom(ItemStack stack) {
        int randomSeed = stack.getOrCreateTag().getInt("random");
        int[] random = {0,0,0,0,0,0};
        random[0] = ((randomSeed*8121)+28411)%12800;
        random[1] = ((random[0]*8121)+28411)%12800;
        random[2] = ((random[1]*8121)+28411)%12800;
        random[3] = ((random[2]*8121)+28411)%12800;
        random[4] = ((random[3]*8121)+28411)%12800;
        random[5] = ((random[4]*8121)+28411)%2;
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
