package net.textstack.band_of_gigantism.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.textstack.band_of_gigantism.BandOfGigantism;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BandOfGigantism.MODID);


    public static final RegistryObject<SoundEvent> PAN_HIT = registerSoundEvent("pan_hit");

    public static final RegistryObject<SoundEvent> GOLD_KILL = registerSoundEvent("gold_kill");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(BandOfGigantism.MODID,name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
