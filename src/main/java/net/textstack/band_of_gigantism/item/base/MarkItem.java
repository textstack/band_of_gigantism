package net.textstack.band_of_gigantism.item.base;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.data.BogDamageTypes;
import net.textstack.band_of_gigantism.registry.BogEffects;
import net.textstack.band_of_gigantism.util.CurioHelper;
import net.textstack.band_of_gigantism.util.MarkHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;

public class MarkItem extends Item implements ICurioItem {

    /**
     * The ResourceKey for the DamageType the player will take upon unequipping the mark. If this is null, the wearer will not take
     * damage when unequipping the mark.
     */
    @Nullable
    private final ResourceKey<DamageType> unequipDamageType;

    /**
     * The formatting code appended to the beginning of a player's chat message if they are wearing the mark.
     * Order of priority is based on the order of registry.
     */
    @Nullable
    private final ChatFormatting formatting;

    protected static final BOGConfig c = BOGConfig.INSTANCE; // this will be inherited by all marks

    public MarkItem(Properties p_41383_, @Nullable ResourceKey<DamageType> unequipDamageType, @Nullable ChatFormatting formatting) {
        super(p_41383_);

        this.unequipDamageType = unequipDamageType;
        this.formatting = formatting;

        MarkHelper.addMark(this);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (unequipDamageType == null) return;

        //deal near-mortal damage, prevent healing
        LivingEntity living = slotContext.entity();
        if (!CurioHelper.hasCurio(living, this)) { //this method is called whenever nbt changes, make sure not to kill for that
            living.hurt(BogDamageTypes.damageSource(living.level(), unequipDamageType), living.getMaxHealth() - 1);
            living.addEffect(new MobEffectInstance(BogEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !CurioHelper.hasCurio(slotContext.entity(), this);
    }

    @NotNull
    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return new ArrayList<>();
    }

    @Nullable
    public ChatFormatting getChatFormatting() {
        return formatting;
    }
}
