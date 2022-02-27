package net.textstack.band_of_gigantism.registry;

import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.effect.DescentEffect;
import net.textstack.band_of_gigantism.effect.GenericEffect;

public class ModEffects {
    public static final DeferredRegister<Effect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.POTIONS, BandOfGigantism.MODID);

    public static final RegistryObject<Effect> FORGETFULNESS = EFFECTS.register("forgetfulness", () -> (new GenericEffect(16777045)));
    public static final RegistryObject<Effect> RECOVERING = EFFECTS.register("recovering", () -> (new GenericEffect(5592405)));
    public static final RegistryObject<Effect> STRAINS_OF_ASCENT = EFFECTS.register("strains_of_ascent", () -> (new DescentEffect(5592575)));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
