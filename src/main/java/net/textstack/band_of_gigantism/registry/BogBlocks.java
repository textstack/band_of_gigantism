package net.textstack.band_of_gigantism.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.block.Mirapoppy;
import net.textstack.band_of_gigantism.entity.MirapoppyEntity;
import net.textstack.band_of_gigantism.item.MirapoppyItem;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BogBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, BandOfGigantism.MODID);
    public static final DeferredRegister<Item> ITEMS = BogItems.ITEMS;

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BandOfGigantism.MODID);

    public static final Supplier<Block> MIRAPOPPY = register("mirapoppy",
            () -> new Mirapoppy(() -> MobEffects.CONFUSION, BlockBehaviour.Properties.of().instabreak().sound(SoundType.GRASS).noCollission().offsetType(BlockBehaviour.OffsetType.XZ)),
            object -> () -> new MirapoppyItem(object.get(), new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).defaultDurability(0)));

    @SuppressWarnings("ConstantConditions")
    public static final Supplier<BlockEntityType<MirapoppyEntity>> MIRAPOPPY_ENTITY = BLOCK_ENTITIES.register("mirapoppy",
            () -> BlockEntityType.Builder.of(MirapoppyEntity::new, MIRAPOPPY.get()).build(null));

    private static <T extends Block> Supplier<T> registerBlock(final String name, final Supplier<? extends T> block) {
        return BLOCKS.register(name, block);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> Supplier<T> register(final String name, final Supplier<? extends T> block,
                                                                Function<Supplier<T>, Supplier<? extends Item>> item) {
        Supplier<T> obj = registerBlock(name, block);
        ITEMS.register(name, item.apply(obj));
        return obj;
    }
}
