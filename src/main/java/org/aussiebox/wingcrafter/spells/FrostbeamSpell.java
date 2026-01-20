package org.aussiebox.wingcrafter.spells;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.cca.FreezeComponent;
import org.aussiebox.wingcrafter.cca.SoulComponent;
import org.aussiebox.wingcrafter.spells.util.Spell;

import java.util.List;

public class FrostbeamSpell extends Spell {
    public FrostbeamSpell() {
        super(
                "frostbeam",
                Wingcrafter.id("textures/gui/sprites/soul_scroll/spells/frostbeam.png"),
                3,
                0,
                100
        );
    }

    @Override
    public void cast(ServerPlayerEntity source) {
        super.cast(source);
        afflictSoulPenalty(source);

        ServerWorld world = source.getEntityWorld();
        SoulComponent data = SoulComponent.KEY.get(source);

        Vec3d startVec = source.getEyePos();
        Vec3d lookVec = source.getRotationVec(1.0F);
        double maxDistance = 20.0;
        Vec3d endVec = startVec.add(lookVec.multiply(maxDistance));

        RaycastContext context = new RaycastContext(
                startVec,
                endVec,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.ANY,
                source
        );

        HitResult hitResult = world.raycast(context);

        BlockPos centerPos = BlockPos.ofFloored(hitResult.getPos().subtract(0, 1, 0));
        int searchRadius = 1;

        for (int x = centerPos.getX() - searchRadius; x <= centerPos.getX() + searchRadius; x++) {
            for (int y = centerPos.getY() - searchRadius; y <= centerPos.getY() + searchRadius; y++) {
                for (int z = centerPos.getZ() - searchRadius; z <= centerPos.getZ() + searchRadius; z++) {
                    if (world.getBlockState(BlockPos.ofFloored(x, y, z)).isOpaque() || world.getBlockState(BlockPos.ofFloored(x, y, z)).isOf(Blocks.WATER)) {
                        world.setBlockState(BlockPos.ofFloored(x, y, z), Blocks.ICE.getDefaultState());
                    }
                    if (world.getBlockState(BlockPos.ofFloored(x, y, z)).isOf(Blocks.FIRE)) {
                        world.setBlockState(BlockPos.ofFloored(x, y, z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        List<Entity> entities = world.getOtherEntities(source, new Box(hitResult.getPos().x-1.5, hitResult.getPos().y-1.5, hitResult.getPos().z-1.5, hitResult.getPos().x+1.5, hitResult.getPos().y+1.5, hitResult.getPos().z+1.5));
        DamageSource damageSource = new DamageSource(
                world.getRegistryManager()
                        .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(DamageTypes.FREEZE.getValue()).get(),
                source
        );
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                entity.damage(world, damageSource, 5);
                data.changeSoul(-5);
                if (livingEntity.isDead()) {
                    data.changeSoul(-10);
                }
            }
        }

        double startX = source.getEyePos().x;
        double startY = source.getEyePos().y;
        double startZ = source.getEyePos().z;
        double endX = hitResult.getPos().x;
        double endY = hitResult.getPos().y;
        double endZ = hitResult.getPos().z;

        double length = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2) + Math.pow(endZ - startZ, 2));
        int particleCount = (int) length * 5;
        double stepX = (endX - startX) / particleCount;
        double stepY = (endY - startY) / particleCount;
        double stepZ = (endZ - startZ) / particleCount;

        for (int i = 0; i < particleCount; i++) {
            double x = startX + stepX * i;
            double y = startY + stepY * i;
            double z = startZ + stepZ * i;

            world.spawnParticles(new DustParticleEffect(0xFF8FFDFF, 3.5F), x, y, z, 1,  0.2,  0.2,  0.2,  0);
            world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK_CRUMBLE, Blocks.ICE.getDefaultState()), x, y, z, 1,  1,  1,  1,  0);
        }
        world.playSound(null, BlockPos.ofFloored(source.getEyePos()), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.5F, 1.2F);
        source.setFrozenTicks(100);
        FreezeComponent.KEY.get(source).setFreezeTicks(50);
    }
}
