package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.textstack.band_of_gigantism.config.BOGConfig;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class MirapoppyItem extends BlockItem implements ICurioItem {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MirapoppyItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mirapoppy_description_flavor"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mirapoppy_description_0"));
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.mirapoppy_description_1"));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }
}
