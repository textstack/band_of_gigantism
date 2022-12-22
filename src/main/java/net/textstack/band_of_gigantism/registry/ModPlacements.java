package net.textstack.band_of_gigantism.registry;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.textstack.band_of_gigantism.registry.ModFeatures.MIRA_FEATURE;

public class ModPlacements extends VegetationPlacements {
    public static final Holder<PlacedFeature> MIRA_PLACE = PlacementUtils.register("mira_placed", MIRA_FEATURE, PlacementUtils.isEmpty());
}
