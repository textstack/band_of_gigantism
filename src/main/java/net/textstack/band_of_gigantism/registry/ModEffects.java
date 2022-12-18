package net.textstack.band_of_gigantism.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.effect.CrabbyEffect;
import net.textstack.band_of_gigantism.effect.DescentEffect;
import net.textstack.band_of_gigantism.effect.GenericEffect;
import net.textstack.band_of_gigantism.effect.MiraEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BandOfGigantism.MODID);

    public static final RegistryObject<MobEffect> FORGETFULNESS = EFFECTS.register("forgetfulness", () -> (new GenericEffect(16777045)));
    public static final RegistryObject<MobEffect> RECOVERING = EFFECTS.register("recovering", () -> (new GenericEffect(5592405)));
    public static final RegistryObject<MobEffect> STRAINS_OF_ASCENT = EFFECTS.register("strains_of_ascent", () -> (new DescentEffect(5592575)));
    public static final RegistryObject<MobEffect> CRABBY = EFFECTS.register("crabby", () -> (new CrabbyEffect(16465706)));
    public static final RegistryObject<MobEffect> MIRA = EFFECTS.register("mira", () -> (new GenericEffect(10027237)));
    public static final RegistryObject<MobEffect> MIRA_SICKNESS = EFFECTS.register("mira_sickness", () -> (new MiraEffect(10027237)));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
