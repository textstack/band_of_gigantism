package net.textstack.band_of_gigantism.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.textstack.band_of_gigantism.config.BOGConfig;
import net.textstack.band_of_gigantism.registry.ModBlocks;
import net.textstack.band_of_gigantism.registry.ModEffects;

import java.util.List;

public class MirapoppyEntity extends BlockEntity {

    BOGConfig c = BOGConfig.INSTANCE;

    public MirapoppyEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.MIRAPOPPY_ENTITY.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos) {
        if (level.getGameTime() % 20 == 0) {
            AABB aabb = (new AABB(pos)).inflate(c.mirapoppy_radius.get());
            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity living : list) {
                if (!(living instanceof Player))
                    living.addEffect(new MobEffectInstance(ModEffects.MIRA.get(), 100));
            }
        }
    }
}
