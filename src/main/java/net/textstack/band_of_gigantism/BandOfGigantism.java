package net.textstack.band_of_gigantism;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.event.EventHandlerMyBallsInYourMouth;
import net.textstack.band_of_gigantism.item.BandBasic;
import net.textstack.band_of_gigantism.item.FalseHand;
import net.textstack.band_of_gigantism.registry.ModBlocks;
import net.textstack.band_of_gigantism.registry.ModEffects;
import net.textstack.band_of_gigantism.registry.ModItems;
import net.textstack.band_of_gigantism.registry.ModSoundEvents;
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
        //bogConfig = new BOGConfig();

        ModItems.register(eventBus);
        ModEffects.register(eventBus);
        ModSoundEvents.register(eventBus);
        ModBlocks.BLOCK_ENTITIES.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BOGConfig.SPEC);

        eventBus.addListener(this::clientSetup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(bogHandler);
        //MinecraftForge.EVENT_BUS.register(BOGConfig.class);
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
