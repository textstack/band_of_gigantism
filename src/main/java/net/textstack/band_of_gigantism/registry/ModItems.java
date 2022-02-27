package net.textstack.band_of_gigantism.registry;

import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.item.*;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BandOfGigantism.MODID);

    public static final RegistryObject<Item> BAND_GENERIC = ITEMS.register("band_generic",
            () -> new BandGeneric(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0),
                    0,true, SoundEvents.ITEM_ARMOR_EQUIP_GOLD));

    public static final RegistryObject<Item> LESSER_BAND_GENERIC = ITEMS.register("lesser_band_generic",
            () -> new BandGeneric(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.UNCOMMON).maxStackSize(1).defaultMaxDamage(0),
                    1,false, SoundEvents.ITEM_ARMOR_EQUIP_IRON));

    public static final RegistryObject<Item> SHRINK_BAND_GENERIC = ITEMS.register("shrink_band_generic",
            () -> new BandGeneric(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.UNCOMMON).maxStackSize(1).defaultMaxDamage(0).setNoRepair(),
                    2,false, SoundEvents.ITEM_ARMOR_EQUIP_IRON));

    public static final RegistryObject<Item> GLOBETROTTERS_BAND = ITEMS.register("band_globetrotters",
            () -> new BandGlobetrotters(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.EPIC).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_OBLITERATED = ITEMS.register("mark_obliterated",
            () -> new MarkObliterated(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_FADED = ITEMS.register("mark_faded",
            () -> new MarkFaded(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_FORGOTTEN = ITEMS.register("mark_forgotten",
            () -> new MarkForgotten(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_PURIFIED = ITEMS.register("mark_purified",
            () -> new MarkPurified(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_DESCENDED = ITEMS.register("mark_descended",
            () -> new MarkDescended(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_UNKNOWN = ITEMS.register("mark_unknown",
            () -> new MarkUnknown(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_JUDGED = ITEMS.register("mark_judged",
            () -> new MarkJudged(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MARK_TRUE = ITEMS.register("mark_true",
            () -> new MarkTrue(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.RARE).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> MASK_DIMINISHMENT = ITEMS.register("mask_diminishment",
            () -> new MaskDiminishment(new Item.Properties().group(ItemGroup.TOOLS).rarity(Rarity.EPIC).maxStackSize(1).defaultMaxDamage(0)));

    public static final RegistryObject<Item> FRYING_PAN = ITEMS.register("frying_pan",
            () -> new FryingPan(ItemTier.IRON, 10, -3.2f, new Item.Properties().group(ItemGroup.COMBAT).rarity(Rarity.COMMON).maxStackSize(1).defaultMaxDamage(500)));

    public static final RegistryObject<Item> GOLDEN_FRYING_PAN = ITEMS.register("golden_frying_pan",
            () -> new GoldenFryingPan(ItemTier.GOLD, 12, -3.2f, new Item.Properties().group(ItemGroup.COMBAT).rarity(Rarity.EPIC).maxStackSize(1).defaultMaxDamage(4095).isImmuneToFire()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
