package net.textstack.band_of_gigantism.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.event.GenericLootModifier;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BandOfGigantism.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZER.register("add_item", GenericLootModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZER.register(bus);
    }
}

/*@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
}*/
