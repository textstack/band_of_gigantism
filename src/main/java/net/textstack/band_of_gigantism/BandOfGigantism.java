package net.textstack.band_of_gigantism;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.data.BogData;
import net.textstack.band_of_gigantism.event.EventHandlerMyBallsInYourMouth;
import net.textstack.band_of_gigantism.item.BandBasic;
import net.textstack.band_of_gigantism.item.FalseHand;
import net.textstack.band_of_gigantism.registry.*;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BandOfGigantism.MODID)
public class BandOfGigantism
{

    public static EventHandlerMyBallsInYourMouth bogHandler;

    public static final String MODID = "band_of_gigantism";

    public BandOfGigantism() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // main handler for the mod
        bogHandler = new EventHandlerMyBallsInYourMouth();

        BogItems.register(eventBus);
        BogEffects.register(eventBus);
        BogSoundEvents.register(eventBus);
        BogLootModifiers.register(eventBus);
        BogBlocks.BLOCK_ENTITIES.register(eventBus);
        BogBlocks.BLOCKS.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BOGConfig.SPEC);

        eventBus.addListener(this::clientSetup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(bogHandler);
        NeoForge.EVENT_BUS.register(BogData.class);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        FalseHand.registerVariants();
        BandBasic.registerVariants();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.BRACELET.getMessageBuilder().build());

        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.CURIO.getMessageBuilder().build());
    }
}
