package net.textstack.band_of_gigantism.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class GoldenFryingPan extends SwordItem {

    BOGConfig c = BOGConfig.INSTANCE;

    public GoldenFryingPan(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        int strangeKills = getStrangeKills(stack);

        tooltip.add(LoreStatHelper.displayStrangeName(strangeKills, LoreStatHelper.StrangeType.TOOLTIP)
                .appendSibling(new TranslationTextComponent("tooltip.band_of_gigantism.golden_frying_pan_description_flavor","\u00A78" + strangeKills)));
        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.golden_frying_pan_description_0"));
        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.golden_frying_pan_description_1"));
        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.golden_frying_pan_description_2"));
        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.golden_frying_pan_description_3"));
    }

    @Override
    public boolean hitEntity(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {

        World worldIn = attacker.getEntityWorld();
        if (worldIn.isRemote()) {return super.hitEntity(stack, target, attacker);}

        if (target.getShouldBeDead()) {
            target.playSound(ModSoundEvents.GOLD_KILL.get(),1,1);
            addStrangeKills(stack);
            stack.setDisplayName(LoreStatHelper.displayStrangeName(getStrangeKills(stack), LoreStatHelper.StrangeType.TITLE)
                    .appendSibling(new TranslationTextComponent("item.band_of_gigantism.golden_frying_pan_name_cut")));
        } else {
            target.playSound(ModSoundEvents.PAN_HIT.get(),0.5f,1);
        }

        return super.hitEntity(stack, target, attacker);
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
