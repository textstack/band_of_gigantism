package net.textstack.band_of_gigantism;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.data.BogData;
import net.textstack.band_of_gigantism.event.EventHandlerMyBallsInYourMouth;
import net.textstack.band_of_gigantism.item.BandBasic;
import net.textstack.band_of_gigantism.item.FalseHand;
import net.textstack.band_of_gigantism.registry.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BandOfGigantism.MODID)
public class BandOfGigantism
{
    public static final String MODID = "band_of_gigantism";

    public BandOfGigantism(IEventBus eventBus) {
        BogItems.register(eventBus);
        BogEffects.register(eventBus);
        BogSoundEvents.register(eventBus);
        BogLootModifiers.register(eventBus);
        BogBlocks.BLOCK_ENTITIES.register(eventBus);
        BogBlocks.BLOCKS.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BOGConfig.SPEC);

        eventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(EventHandlerMyBallsInYourMouth.class);
        NeoForge.EVENT_BUS.register(BogData.class);
    }

    @SubscribeEvent
    private void clientSetup(final FMLClientSetupEvent event) {
        FalseHand.registerVariants();
        BandBasic.registerVariants();
    }

    @SubscribeEvent
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(BogItems.BAND_GENERIC.get());
            event.accept(BogItems.LESSER_BAND_GENERIC.get());
            event.accept(BogItems.BAND_APATHY.get());
            event.accept(BogItems.BAND_BASIC.get());
            event.accept(BogItems.BAND_CRUSTACEOUS.get());
            event.accept(BogItems.BAND_PASSION.get());
            event.accept(BogItems.SHRINK_BAND_GENERIC.get());
            event.accept(BogItems.GLOBETROTTERS_BAND.get());
            event.accept(BogItems.MARK_DESCENDED.get());
            event.accept(BogItems.MARK_OBLITERATED.get());
            event.accept(BogItems.MARK_UNKNOWN.get());
            event.accept(BogItems.MARK_FORGOTTEN.get());
            event.accept(BogItems.MARK_JUDGED.get());
            event.accept(BogItems.MARK_PURIFIED.get());
            event.accept(BogItems.MARK_FADED.get());
            event.accept(BogItems.MARK_TRUE.get());
            event.accept(BogItems.MASK_DIMINISHMENT.get());
            event.accept(BogItems.FALSE_HAND.get());
            event.accept(BogBlocks.MIRAPOPPY.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(BogItems.FRYING_PAN.get());
            event.accept(BogItems.GOLDEN_FRYING_PAN.get());
        }

        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(BogItems.MIRAGIN.get());
        }
    }
}
