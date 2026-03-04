package org.aussiebox.wingcrafter.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.cca.FreezeComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMovementMixin extends LivingEntity {
    protected PlayerMovementMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(
            method = "move",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private Vec3d wingcrafter$canMove(Vec3d movement) {
        if (FreezeComponent.KEY.get(this).getFreezeTicks() > 0) {
            return new Vec3d(0, Math.min(0, movement.getY()), 0);
        }
        return movement;
    }
}
