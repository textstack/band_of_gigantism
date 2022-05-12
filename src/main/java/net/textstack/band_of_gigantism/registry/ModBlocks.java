package net.textstack.band_of_gigantism.registry;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.block.Mirapoppy;
import net.textstack.band_of_gigantism.entity.MirapoppyEntity;
import net.textstack.band_of_gigantism.item.MirapoppyItem;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BandOfGigantism.MODID);
    public static final DeferredRegister<Item> ITEMS = ModItems.ITEMS;

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BandOfGigantism.MODID);

    public static final RegistryObject<Block> MIRAPOPPY = register("mirapoppy",
            () -> new Mirapoppy(MobEffects.CONFUSION, 30, BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_PURPLE).sound(SoundType.GRASS).noCollission()),
            object -> () -> new MirapoppyItem(object.get(), new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.UNCOMMON).stacksTo(1).defaultDurability(0)));

    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<BlockEntityType<MirapoppyEntity>> MIRAPOPPY_ENTITY = BLOCK_ENTITIES.register("mirapoppy",
            () -> BlockEntityType.Builder.of(MirapoppyEntity::new, MIRAPOPPY.get()).build(null));

    private static <T extends Block> RegistryObject<T> registerBlock(final String name, final Supplier<? extends T> block) {
        return BLOCKS.register(name, block);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<? extends T> block,
                                                                Function<RegistryObject<T>, Supplier<? extends Item>> item) {
        RegistryObject<T> obj = registerBlock(name, block);
        ITEMS.register(name, item.apply(obj));
        return obj;
    }
}
