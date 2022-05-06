package net.textstack.band_of_gigantism.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class GoldenFryingPan extends SwordItem {

    BOGConfig c = BOGConfig.INSTANCE;

    public GoldenFryingPan(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        int strangeKills = getStrangeKills(stack);

        tooltip.add(LoreStatHelper.displayStrangeName(strangeKills, LoreStatHelper.StrangeType.TOOLTIP)
                .append(new TranslatableComponent("tooltip.band_of_gigantism.golden_frying_pan_description_flavor","\u00A78" + strangeKills)));
        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.golden_frying_pan_description_0"));
        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.golden_frying_pan_description_1"));
        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.golden_frying_pan_description_2"));
        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.golden_frying_pan_description_3"));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {

        Level worldIn = attacker.getLevel();
        if (worldIn.isClientSide()) {return super.hurtEnemy(stack, target, attacker);}

        if (target.isDeadOrDying()) {
            target.playSound(ModSoundEvents.GOLD_KILL.get(),1,1);
            addStrangeKills(stack);
            stack.setHoverName(LoreStatHelper.displayStrangeName(getStrangeKills(stack), LoreStatHelper.StrangeType.TITLE)
                    .append(new TranslatableComponent("item.band_of_gigantism.golden_frying_pan_name_cut")));
        } else {
            target.playSound(ModSoundEvents.PAN_HIT.get(),0.5f,1);
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    //get current kill count
    private int getStrangeKills(ItemStack stack) { return stack.getOrCreateTag().getInt("strangeKills"); }

    //add to kill count
    private void addStrangeKills(ItemStack stack) {
        int newCount = getStrangeKills(stack) + 1;
        stack.getOrCreateTag().putInt("strangeKills",newCount);
    }

    /*//get announce
    private String getFound(ItemStack stack) {return stack.getOrCreateTag().getString("found");}

    //set announce
    private void setFound(ItemStack stack, String string) {stack.getOrCreateTag().putString("found",string);}*/
}
