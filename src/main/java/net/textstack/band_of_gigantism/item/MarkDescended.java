package net.textstack.band_of_gigantism.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.config.BOGItems;
import net.textstack.band_of_gigantism.config.BOGMarks;
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

public class MarkDescended extends Item implements ICurioItem {

    BOGConfig c = BOGConfig.INSTANCE;

    /*private static final int marks_duration = c.marks_duration.get();
    private static final int mark_descended_ascend = c.mark_descended_ascend.get();
    private static final int mark_descended_duration = c.mark_descended_duration.get();
    private static final int mark_descended_armor = c.mark_descended_armor.get();
    private static final float mark_descended_regeneration = c.mark_descended_regeneration.get().floatValue();
    private static final boolean description_enable = c.description_enable.get();*/

    public MarkDescended(Properties properties) {
        super(properties);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        //deal near-mortal damage, prevent healing
        LivingEntity living = slotContext.getWearer();
        if (!CurioHelper.hasCurio(living, ModItems.MARK_DESCENDED.get())) { //this method is called whenever nbt changes, make sure not to kill for that
            living.attackEntityFrom(MarkDamageSource.BOG_DESCENDED, living.getMaxHealth()-1);
            living.addPotionEffect(new EffectInstance(ModEffects.RECOVERING.get(),c.marks_duration.get(),0,false,false));
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ICurioItem.super.curioTick(identifier, index, livingEntity, stack);

        //either lower the stored position (if the player is lower) or inflict strains of ascent (if player is >5 blocks higher)
        int posY = (int) Math.ceil(livingEntity.getPositionVec().y);
        if (stack.getTag() != null) {
            int prevPosY = this.getPosY(stack);

            if (posY < prevPosY) {
                this.setPosY(stack, posY);
            } else if (posY >= prevPosY + c.mark_descended_ascend.get()) {
                int amp;
                if (prevPosY >= 64) amp = 0;
                else if (prevPosY > 48) amp = 1;
                else if (prevPosY > 32) amp = 2;
                else if (prevPosY > 16) amp = 3;
                else if (prevPosY > 0) amp = 4;
                else amp = 5;
                livingEntity.addPotionEffect(new EffectInstance(ModEffects.STRAINS_OF_ASCENT.get(), c.mark_descended_duration.get(), amp, false, false));
                this.setPosY(stack, posY);
            }
        } else {
            this.setPosY(stack,posY);
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (worldIn.isRemote) {
            return;
        }

        //try to remove the posY tag for the next equip (this can't be done in onUnequip for some reason)
        LivingEntity theDude = (LivingEntity) entityIn; //responsible variable names for a responsible developer
        if (!CurioHelper.hasCurio(theDude, ModItems.MARK_DESCENDED.get())) {
            stack.getOrCreateTag().remove("posY");
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (worldIn==null||!BOGItems.descriptionEnable()) return; //c.description_enable.get()

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_descended_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(BOGMarks.markDescendedArmor(), LoreStatHelper.Stat.ARMOR)); //c.mark_descended_armor.get()
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_descended_description_0"));
            tooltip.add(LoreStatHelper.displayStat((float) BOGMarks.markDescendedRegeneration(), LoreStatHelper.Stat.REGENERATION,true)); //c.mark_descended_regeneration.get().floatValue()
            //tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_descended_description_1"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_descended_description_shift_0"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_descended_description_shift_1"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("ba5827f8-8e41-4cdf-b5de-78bbe345a559"),
                BandOfGigantism.MODID + ":armor_modifier_descended", c.mark_descended_armor.get(), AttributeModifier.Operation.ADDITION));

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

    //get posy tag
    private int getPosY(ItemStack stack) {
        return stack.getOrCreateTag().getInt("posY");
    }

    //store new posy tag
    private void setPosY(ItemStack stack, int posY) {
        stack.getOrCreateTag().putInt("posY", posY);
    }
}