package net.textstack.band_of_gigantism.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.textstack.band_of_gigantism.entity.MirapoppyEntity;
import net.textstack.band_of_gigantism.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Mirapoppy extends FlowerBlock implements EntityBlock {
    public Mirapoppy(MobEffect effect, int duration, Properties properties) {
        super(effect, duration, properties);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Random random) {
        super.animateTick(state, level, pos, random);

        VoxelShape voxelshape = this.getShape(state, level, pos, CollisionContext.empty());
        Vec3 vec3 = voxelshape.bounds().getCenter();
        double d0 = (double) pos.getX() + vec3.x;
        double d1 = (double) pos.getZ() + vec3.z;

        for (int i = 0; i < 3; ++i) {
            if (random.nextBoolean()) {
                level.addParticle(ParticleTypes.ENCHANT, d0 + (random.nextDouble() - 0.5D) / 3.0D,
                        (double) pos.getY() + random.nextDouble() * 0.5D + 0.25, d1 + (random.nextDouble() - 0.5D) / 3.0D,
                        (random.nextDouble() - 0.5D) * 0.35D, 0.0D, (random.nextDouble() - 0.5D) * 0.35D);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlocks.MIRAPOPPY_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (level1, pos1, state1, blockEntity) -> ((MirapoppyEntity) blockEntity).tick(level1, pos1);
    }
}
