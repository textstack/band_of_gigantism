package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.item.base.MarkItem;
import net.textstack.band_of_gigantism.registry.ModDamageSources;
import net.textstack.band_of_gigantism.util.LoreStatHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MarkForgotten extends MarkItem {

    public MarkForgotten(Properties properties) {
        super(properties, ModDamageSources.BOG_FORGOTTEN);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_forgotten_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(LoreStatHelper.displayStat(c.mark_forgotten_resistance.get().floatValue(), LoreStatHelper.Stat.RESISTANCE, true));
            tooltip.add(LoreStatHelper.displayStat(c.mark_forgotten_critical_damage.get().floatValue(), LoreStatHelper.Stat.CRITICAL_DAMAGE, true));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_forgotten_description_shift_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_forgotten_description_shift_1"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_generic_description"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }
}
