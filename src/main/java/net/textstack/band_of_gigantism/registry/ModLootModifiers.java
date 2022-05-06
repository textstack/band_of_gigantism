package net.textstack.band_of_gigantism.registry;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.event.GenericLootModifier;
import net.textstack.band_of_gigantism.event.GenericLootModifierMultiple;

@SuppressWarnings("unused")
public class ModLootModifiers {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS.get(), BandOfGigantism.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> END_CITY_TREASURE = LOOT_MODIFIERS.register(
            "end_city_treasure", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> SHIPWRECK_TREASURE = LOOT_MODIFIERS.register(
            "shipwreck_treasure", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> WOODLAND_MANSION = LOOT_MODIFIERS.register(
            "woodland_mansion", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> VILLAGE_TOOLSMITH = LOOT_MODIFIERS.register(
            "village_toolsmith", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> BASTION_TREASURE = LOOT_MODIFIERS.register(
            "bastion_treasure", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> ABANDONED_MINESHAFT = LOOT_MODIFIERS.register(
            "abandoned_mineshaft", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> SIMPLE_DUNGEON = LOOT_MODIFIERS.register(
            "simple_dungeon", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> STRONGHOLD_CROSSING = LOOT_MODIFIERS.register(
            "stronghold_crossing", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> STRONGHOLD_CORRIDOR = LOOT_MODIFIERS.register(
            "stronghold_corridor", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> BURIED_TREASURE = LOOT_MODIFIERS.register(
            "buried_treasure", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> DESERT_PYRAMID = LOOT_MODIFIERS.register(
            "desert_pyramid", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> RUINED_PORTAL = LOOT_MODIFIERS.register(
            "ruined_portal", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> JUNGLE_TEMPLE = LOOT_MODIFIERS.register(
            "jungle_temple", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> IGLOO_CHEST = LOOT_MODIFIERS.register(
            "igloo_chest", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> BASTION_OTHER = LOOT_MODIFIERS.register(
            "bastion_other", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> NETHER_BRIDGE = LOOT_MODIFIERS.register(
            "nether_bridge", GenericLootModifierMultiple.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> UNDERWATER_RUIN_BIG = LOOT_MODIFIERS.register(
            "underwater_ruin_big", GenericLootModifierMultiple.Serializer::new);


    public static final RegistryObject<GlobalLootModifierSerializer<?>> PILLAGER_OUTPOST = LOOT_MODIFIERS.register(
            "pillager_outpost", GenericLootModifier.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> SPAWN_BONUS_CHEST = LOOT_MODIFIERS.register(
            "spawn_bonus_chest", GenericLootModifier.Serializer::new);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> PIGLIN_BRUTE = LOOT_MODIFIERS.register(
            "piglin_brute", GenericLootModifier.Serializer::new);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS.register(eventBus);
    }
}
