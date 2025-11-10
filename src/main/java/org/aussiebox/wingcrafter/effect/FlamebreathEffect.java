package org.aussiebox.wingcrafter.effect;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FlamebreathEffect extends StatusEffect {
    public FlamebreathEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        entity.getEntityWorld().playSound(null, entity.getBlockPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
        super.onApplied(entity, amplifier);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        Vec3d startVec = entity.getEyePos();
        Vec3d lookVec = entity.getRotationVec(1.0F);
        double maxDistance = 20.0;
        Vec3d endVec = startVec.add(lookVec.multiply(maxDistance));

        RaycastContext context = new RaycastContext(
                startVec,
                endVec,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.ANY,
                entity
        );

        HitResult hitResult = world.raycast(context);

        List<Entity> entities = world.getOtherEntities(entity, new Box(hitResult.getPos().x-1.5, hitResult.getPos().y-1.5, hitResult.getPos().z-1.5, hitResult.getPos().x+1.5, hitResult.getPos().y+1.5, hitResult.getPos().z+1.5));
        DamageSource damageSource = new DamageSource(
                world.getRegistryManager()
                        .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(DamageTypes.ON_FIRE.getValue()).get(),
                entity
        );
        for (Entity attacked : entities) {
            attacked.damage(world, damageSource, 1);
            attacked.setOnFireForTicks(attacked.getFireTicks()+10);
        }

        double startX = entity.getEyePos().x;
        double startY = entity.getEyePos().y;
        double startZ = entity.getEyePos().z;
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

            world.spawnParticles(new DustParticleEffect(0xFFFC6603, 0.5F), x, y, z, 1,  0,  0,  0,  0);
            if (ThreadLocalRandom.current().nextInt(0, 10) == 0) {
                world.spawnParticles(ParticleTypes.FLAME, x, y, z, 1,  0.1,  0.1,  0.1,  0);
            }
            if (ThreadLocalRandom.current().nextInt(0, 25) == 0) {
                world.setBlockState(BlockPos.ofFloored(hitResult.getPos()), AbstractFireBlock.getState(world, BlockPos.ofFloored(hitResult.getPos())));
            }
        }
        return super.applyUpdateEffect(world, entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}

