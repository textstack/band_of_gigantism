package net.textstack.band_of_gigantism.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MarkTrue extends Item {

    BOGConfig c = BOGConfig.INSTANCE;

    /*private static final boolean description_enable = c.description_enable.get();*/

    public MarkTrue(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!c.description_enable.get()) return;

        tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_true_description_flavor"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.void"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_true_description_0"));
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.mark_true_description_1"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.band_of_gigantism.shift"));
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        //replaces the item with one of seven marks
        if (entityIn instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) entityIn;
            if (player.isCreative()||!player.isAlive()||player.isSpectator()) return;
            NonNullList<ItemStack> list = player.inventory.mainInventory;

            ItemStack stackCheck = stack;
            int pick = (int) (Math.random() * 7);
            switch (pick) {
                case 0:
                    stackCheck = new ItemStack(ModItems.MARK_FADED.get());
                    break;
                case 1:
                    stackCheck = new ItemStack(ModItems.MARK_FORGOTTEN.get());
                    break;
                case 2:
                    stackCheck = new ItemStack(ModItems.MARK_PURIFIED.get());
                    break;
                case 3:
                    stackCheck = new ItemStack(ModItems.MARK_DESCENDED.get());
                    break;
                case 4:
                    stackCheck = new ItemStack(ModItems.MARK_UNKNOWN.get());
                    break;
                case 5:
                    stackCheck = new ItemStack(ModItems.MARK_JUDGED.get());
                    break;
                case 6:
                    stackCheck = new ItemStack(ModItems.MARK_OBLITERATED.get());
                    break;
            }
            list.set(itemSlot, stackCheck);
        }
    }
}
