package net.textstack.band_of_gigantism.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.event.GenericLootModifier;
import net.textstack.band_of_gigantism.event.GenericLootModifierMultiple;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootModifiers {

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {

        event.getRegistry().registerAll(
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "end_city_treasure")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "shipwreck_treasure")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "woodland_mansion")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "village_toolsmith")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "bastion_treasure")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "abandoned_mineshaft")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "simple_dungeon")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "stronghold_crossing")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "stronghold_corridor")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "buried_treasure")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "desert_pyramid")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "ruined_portal")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "jungle_temple")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "igloo_chest")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "bastion_other")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "nether_bridge")),
                new GenericLootModifierMultiple.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "underwater_ruin_big")),
                new GenericLootModifier.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "pillager_outpost")),
                new GenericLootModifier.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "spawn_bonus_chest")),
                new GenericLootModifier.Serializer().setRegistryName(
                        new ResourceLocation(BandOfGigantism.MODID, "piglin_brute"))
        );
    }
}
