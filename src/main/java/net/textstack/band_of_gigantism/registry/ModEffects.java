package net.textstack.band_of_gigantism.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.effect.CrabbyEffect;
import net.textstack.band_of_gigantism.effect.DescentEffect;
import net.textstack.band_of_gigantism.effect.GenericEffect;
import net.textstack.band_of_gigantism.effect.MiraEffect;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, BandOfGigantism.MODID);

    public static final Supplier<MobEffect> FORGETFULNESS = EFFECTS.register("forgetfulness", () -> (new GenericEffect(16777045)));
    public static final Supplier<MobEffect> RECOVERING = EFFECTS.register("recovering", () -> (new GenericEffect(5592405)));
    public static final Supplier<MobEffect> STRAINS_OF_ASCENT = EFFECTS.register("strains_of_ascent", () -> (new DescentEffect(5592575)));
    public static final Supplier<MobEffect> CRABBY = EFFECTS.register("crabby", () -> (new CrabbyEffect(16465706)));
    public static final Supplier<MobEffect> MIRA = EFFECTS.register("mira", () -> (new GenericEffect(10027237)));
    public static final Supplier<MobEffect> MIRA_SICKNESS = EFFECTS.register("mira_sickness", () -> (new MiraEffect(10027237)));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
