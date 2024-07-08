package net.textstack.band_of_gigantism.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class BogPlacements extends VegetationPlacements {
    public static final ResourceKey<PlacedFeature> MIRA_PLACE = PlacementUtils.createKey("mira_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> miraHolder = holdergetter.getOrThrow(BogFeatures.MIRA_FEATURE);
        PlacementUtils.register(context, MIRA_PLACE, miraHolder, new PlacementModifier[]{PlacementUtils.isEmpty()});
    }
}
