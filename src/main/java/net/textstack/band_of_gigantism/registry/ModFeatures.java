package net.textstack.band_of_gigantism.registry;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModFeatures extends VegetationFeatures {
    //wonder if this works
    public static final Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> MIRA_FEATURE =
            FeatureUtils.register("mira_feature", Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.MIRAPOPPY.get().defaultBlockState())));
}

