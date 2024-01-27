package net.textstack.band_of_gigantism.item.base;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.util.CurioHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;

public class MarkItem extends Item implements ICurioItem {

    /**
     * The DamageSource inflicted when the wearer unequips this mark. If this is null, the wearer will not take
     * damage when unequipping the mark.
     */
    private final DamageSource unequipDamageType;

    protected static final BOGConfig c = BOGConfig.INSTANCE; // this will be inherited by all marks

    public MarkItem(Properties p_41383_, @Nullable DamageSource unequipDamageType) {
        super(p_41383_);

        this.unequipDamageType = unequipDamageType;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (unequipDamageType == null) return; // used for obliterated, which does not kill on unequip

        //deal near-mortal damage, prevent healing
        LivingEntity living = slotContext.entity();
        if (!CurioHelper.hasCurio(living, this)) { //this method is called whenever nbt changes, make sure not to kill for that
            living.hurt(unequipDamageType, living.getMaxHealth() - 1);
            living.addEffect(new MobEffectInstance(ModEffects.RECOVERING.get(), c.marks_duration.get(), 0, false, false));
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
}
