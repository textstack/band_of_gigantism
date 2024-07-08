package net.textstack.band_of_gigantism.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.textstack.band_of_gigantism.BandOfGigantism;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class BogRegistrySets extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, BogDamageTypes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, BogFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, BogPlacements::bootstrap);

    public BogRegistrySets(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Collections.singleton(BandOfGigantism.MODID));
    }
}
