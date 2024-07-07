package net.textstack.band_of_gigantism.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.textstack.band_of_gigantism.BandOfGigantism;

public class ModDamageSources {
    public static final ResourceKey<DamageType> BOG_OBLITERATED = createKey("mark_obliterated");
    public static final ResourceKey<DamageType> BOG_FADED = createKey("mark_faded");
    public static final ResourceKey<DamageType> BOG_FORGOTTEN = createKey("mark_forgotten");
    public static final ResourceKey<DamageType> BOG_PURIFIED = createKey("mark_purified");
    public static final ResourceKey<DamageType> BOG_UNKNOWN = createKey("mark_unknown");
    public static final ResourceKey<DamageType> BOG_DESCENDED = createKey("mark_descended");
    public static final ResourceKey<DamageType> BOG_JUDGED = createKey("mark_judged");
    public static final ResourceKey<DamageType> BOG_MIRA = createKey("mira");

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(BOG_OBLITERATED, new DamageType("mark_obliterated", 0.0f));
        context.register(BOG_FADED, new DamageType("mark_faded", 0.0f));
        context.register(BOG_FORGOTTEN, new DamageType("mark_forgotten", 0.0f));
        context.register(BOG_PURIFIED, new DamageType("mark_purified", 0.0f));
        context.register(BOG_UNKNOWN, new DamageType("mark_unknown", 0.0f));
        context.register(BOG_DESCENDED, new DamageType("mark_descended", 0.0f));
        context.register(BOG_JUDGED, new DamageType("mark_judged", 0.0f));
        context.register(BOG_MIRA, new DamageType("mira", 0.0f));
    }

    private static ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BandOfGigantism.MODID, name));
    }

    public static DamageSource damageSource(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
