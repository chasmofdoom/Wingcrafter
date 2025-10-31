package org.aussiebox.wingcrafter.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ScrollBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final MapCodec<ScrollBlock> CODEC = createCodec(ScrollBlock::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

    public static final BooleanProperty ROLLED = BooleanProperty.of("rolled");
    public static final BooleanProperty WRITTEN = BooleanProperty.of("written");

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ScrollBlockEntity(pos, state);
    }

    public ScrollBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ROLLED, false));
        setDefaultState(getDefaultState().with(WRITTEN, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            if (!(world.getBlockEntity(pos) instanceof ScrollBlockEntity scrollBlockEntity)) {
                return super.onUse(state, world, pos, player, hit);
            }
            if (!player.isSneaking()) {
                player.openHandledScreen(scrollBlockEntity);
            } else {
                boolean activated = state.get(ROLLED);
                world.setBlockState(pos, state.with(ROLLED, !activated));
                world.playSound(player, pos, SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ROLLED);
        builder.add(WRITTEN);
    }
}
