package org.aussiebox.wingcrafter.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class MoonGlobeBlock extends Block {

    private static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(4, 4, 4, 12, 12, 12);
    public static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(4, 4, 4, 12, 12, 12);

    public MoonGlobeBlock(Settings settings) {
        super(settings);
    }

    public static int getLuminance(BlockState state) {
        return 8;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isIn(BlockTags.LEAVES);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }
}
