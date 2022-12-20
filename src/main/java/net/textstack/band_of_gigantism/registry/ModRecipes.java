package net.textstack.band_of_gigantism.registry;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.recipes.StrangeRecipeProvider;

@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {
    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(true, new StrangeRecipeProvider(gen));
    }
}
