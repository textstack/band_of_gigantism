package net.textstack.band_of_gigantism.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.registry.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.minecraft.world.item.Items.IRON_SWORD;

public class StrangeRecipeProvider extends RecipeProvider {
    public StrangeRecipeProvider(DataGenerator data) {
        super(data);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> recipe) {
        UpgradeRecipeBuilder.smithing(Ingredient.of(IRON_SWORD), Ingredient.of(ModItems.MIRAGIN.get()), ModItems.FRYING_PAN.get())
                .save(recipe, new ResourceLocation(BandOfGigantism.MODID,"smithing_test"));
    }
}
