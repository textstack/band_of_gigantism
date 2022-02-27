package net.textstack.band_of_gigantism.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

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
        final Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
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
        final Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
        ItemStack stack = null;
        if (data.isPresent()) {
            stack = data.get().getRight();
        }
        return stack;
    }
}
