package net.textstack.band_of_gigantism.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class CurioHelper {
    /**
     * Determines if the player has a specific curio equipped
     *
     * @param entity the entity to query
     * @param curio the item in question
     * @return true if the curio is equipped, false otherwise
     */
    public static boolean hasCurio(final LivingEntity entity, final Item curio) {
        if (entity == null) {throw new RuntimeException("[BOG] Tried checking the curio for a null entity!");}
        final Optional<SlotResult> data = CuriosApi.getCuriosHelper().findFirstCurio(entity, curio);
        //final Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
        return data.isPresent();
    }

    /**
     * Determines if the player has a specific curio equipped, and returns the ItemStack of that curio
     *
     * @param entity the entity to query
     * @param curio the item in question
     * @return the ItemStack if the item is equipped, null otherwise
     */
    public static ItemStack hasCurioGet(final LivingEntity entity, final Item curio) {
        if (entity == null) {throw new RuntimeException("[BOG] Tried checking the curio for a null entity!");}
        final Optional<SlotResult> data = CuriosApi.getCuriosHelper().findFirstCurio(entity, curio);
        //final Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
        ItemStack stack = null;
        if (data.isPresent()) {
            stack = data.get().stack();
        }
        return stack;
    }
}
