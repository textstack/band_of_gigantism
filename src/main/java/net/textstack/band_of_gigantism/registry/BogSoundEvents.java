package net.textstack.band_of_gigantism.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.textstack.band_of_gigantism.BandOfGigantism;

import java.util.function.Supplier;

public class BogSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BandOfGigantism.MODID);


    public static final Supplier<SoundEvent> PAN_HIT = registerSoundEvent("pan_hit");

    public static final Supplier<SoundEvent> GOLD_KILL = registerSoundEvent("gold_kill");

    public static final Supplier<SoundEvent> CARD_FLIP = registerSoundEvent("card_flip");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(BandOfGigantism.MODID, name), 16));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
