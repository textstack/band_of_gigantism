package net.textstack.band_of_gigantism.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class CurioHelper {
    /**
     * Determines if the player has a specific curio equipped
     *
     * @param entity the entity to query
     * @param curio  the item in question
     * @return true if the curio is equipped, false otherwise
     */
    public static boolean hasCurio(final LivingEntity entity, final Item curio) {
        if (entity == null) {
            throw new RuntimeException("[BOG] Tried checking the curio for a null entity!");
        }

        LazyOptional<ICuriosItemHandler> maybeCuriosInventory = CuriosApi.getCuriosInventory(entity);

        if (!maybeCuriosInventory.isPresent() || maybeCuriosInventory.resolve().isEmpty()) return false;

        return maybeCuriosInventory.resolve().get().findFirstCurio(curio).isPresent();
    }

    /**
     * Determines if the player has a specific curio equipped, and returns the ItemStack of that curio
     *
     * @param entity the entity to query
     * @param curio  the item in question
     * @return the ItemStack if the item is equipped, null otherwise
     */
    public static ItemStack hasCurioGet(final LivingEntity entity, final Item curio) {
        if (entity == null) {
            throw new RuntimeException("[BOG] Tried checking the curio for a null entity!");
        }

        LazyOptional<ICuriosItemHandler> maybeCuriosInventory = CuriosApi.getCuriosInventory(entity);

        if (!maybeCuriosInventory.isPresent() || maybeCuriosInventory.resolve().isEmpty()) return null;

        final Optional<SlotResult> data = maybeCuriosInventory.resolve().get().findFirstCurio(curio);
        return data.map(SlotResult::stack).orElse(null);
    }
}
