package net.textstack.band_of_gigantism.registry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.item.*;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BandOfGigantism.MODID);

    public static final RegistryObject<Item> BAND_GENERIC = ITEMS.register("band_generic",
            () -> new BandGeneric(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0),
                    0, true, SoundEvents.ARMOR_EQUIP_GOLD));

    public static final RegistryObject<Item> LESSER_BAND_GENERIC = ITEMS.register("lesser_band_generic",
            () -> new BandGeneric(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.UNCOMMON).stacksTo(1).defaultDurability(0),
                    1, false, SoundEvents.ARMOR_EQUIP_IRON));

    public static final RegistryObject<Item> SHRINK_BAND_GENERIC = ITEMS.register("shrink_band_generic",
            () -> new BandGeneric(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.UNCOMMON).stacksTo(1).defaultDurability(0).setNoRepair(),
                    2, false, SoundEvents.ARMOR_EQUIP_IRON));

    public static final RegistryObject<Item> BAND_BASIC = ITEMS.register("band_basic",
            () -> new BandBasic(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.UNCOMMON).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> GLOBETROTTERS_BAND = ITEMS.register("band_globetrotters",
            () -> new BandGlobetrotters(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.EPIC).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> BAND_CRUSTACEOUS = ITEMS.register("band_crustaceous",
            () -> new BandCrustaceous(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.EPIC).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> BAND_PASSION = ITEMS.register("band_passion",
            () -> new BandPassion(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> BAND_APATHY = ITEMS.register("band_apathy",
            () -> new BandApathy(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_OBLITERATED = ITEMS.register("mark_obliterated",
            () -> new MarkObliterated(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_FADED = ITEMS.register("mark_faded",
            () -> new MarkFaded(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_FORGOTTEN = ITEMS.register("mark_forgotten",
            () -> new MarkForgotten(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_PURIFIED = ITEMS.register("mark_purified",
            () -> new MarkPurified(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_DESCENDED = ITEMS.register("mark_descended",
            () -> new MarkDescended(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_UNKNOWN = ITEMS.register("mark_unknown",
            () -> new MarkUnknown(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_JUDGED = ITEMS.register("mark_judged",
            () -> new MarkJudged(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MARK_TRUE = ITEMS.register("mark_true",
            () -> new MarkTrue(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> MASK_DIMINISHMENT = ITEMS.register("mask_diminishment",
            () -> new MaskDiminishment(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.EPIC).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> FALSE_HAND = ITEMS.register("false_hand",
            () -> new FalseHand(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.RARE).stacksTo(1).defaultDurability(0)));

    public static final RegistryObject<Item> FRYING_PAN = ITEMS.register("frying_pan",
            () -> new FryingPan(Tiers.IRON, 10, -3.2f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).rarity(Rarity.COMMON).stacksTo(1).defaultDurability(500)));

    public static final RegistryObject<Item> GOLDEN_FRYING_PAN = ITEMS.register("golden_frying_pan",
            () -> new GoldenFryingPan(Tiers.GOLD, 12, -3.2f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).rarity(Rarity.EPIC).stacksTo(1).defaultDurability(4095).fireResistant()));

    public static final RegistryObject<Item> MIRAGIN = ITEMS.register("mira_gin",
            () -> new MiraGin(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).rarity(Rarity.RARE).stacksTo(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
