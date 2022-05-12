package net.textstack.band_of_gigantism.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.textstack.band_of_gigantism.entity.MirapoppyEntity;
import net.textstack.band_of_gigantism.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mirapoppy extends FlowerBlock implements EntityBlock {
    public Mirapoppy(MobEffect effect, int duration, Properties properties) {
        super(effect, duration, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlocks.MIRAPOPPY_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (level1, pos1, state1, blockEntity) -> ((MirapoppyEntity)blockEntity).tick(level1, pos1);
    }
}
