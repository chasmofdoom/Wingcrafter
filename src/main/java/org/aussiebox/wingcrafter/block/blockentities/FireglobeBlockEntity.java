package org.aussiebox.wingcrafter.block.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.block.ModBlockEntities;

public class FireglobeBlockEntity extends BlockEntity {
    public FireglobeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FIREGLOBE_BLOCK_ENTITY, pos, state);
    }
}
