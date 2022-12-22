package net.textstack.band_of_gigantism.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MarkTrue extends Item {

    final BOGConfig c = BOGConfig.INSTANCE;

    public MarkTrue(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        MutableComponent slotsTooltip = Component.translatable("curios.slot")
                .append(": ").withStyle(ChatFormatting.GOLD);
        MutableComponent type = Component.translatable("curios.identifier.curio").withStyle(ChatFormatting.YELLOW);
        slotsTooltip.append(type);

        tooltip.add(slotsTooltip);
        tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_true_description_flavor"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.void"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_true_description_0"));
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.mark_true_description_1"));
        } else {
            tooltip.add(Component.translatable("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        //replaces the item with one of seven marks
        if (entityIn instanceof Player player) {

            if (player.isCreative() || !player.isAlive() || player.isSpectator()) return;
            NonNullList<ItemStack> list = player.getInventory().items;

            ItemStack stackCheck = stack;
            int pick = (int) (Math.random() * 7);
            switch (pick) {
                case 0 -> stackCheck = new ItemStack(ModItems.MARK_FADED.get());
                case 1 -> stackCheck = new ItemStack(ModItems.MARK_FORGOTTEN.get());
                case 2 -> stackCheck = new ItemStack(ModItems.MARK_PURIFIED.get());
                case 3 -> stackCheck = new ItemStack(ModItems.MARK_DESCENDED.get());
                case 4 -> stackCheck = new ItemStack(ModItems.MARK_UNKNOWN.get());
                case 5 -> stackCheck = new ItemStack(ModItems.MARK_JUDGED.get());
                case 6 -> stackCheck = new ItemStack(ModItems.MARK_OBLITERATED.get());
            }
            list.set(itemSlot, stackCheck);
        }
    }
}
