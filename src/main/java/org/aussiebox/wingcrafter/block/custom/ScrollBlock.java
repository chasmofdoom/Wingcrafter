package org.aussiebox.wingcrafter.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.attach.ModCustomAttachedData;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class ScrollBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final MapCodec<ScrollBlock> CODEC = createCodec(ScrollBlock::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

    public static final BooleanProperty ROLLED = BooleanProperty.of("rolled");
    public static final BooleanProperty WRITTEN = BooleanProperty.of("written");
    public static final BooleanProperty TITLED = BooleanProperty.of("titled");
    public static final BooleanProperty SEALED = BooleanProperty.of("sealed");

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
        setDefaultState(getDefaultState().with(TITLED, false));
        setDefaultState(getDefaultState().with(SEALED, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            if (player.isHolding(ModItems.SEAL)) {
                return ActionResult.PASS;
            }
            if (!(world.getBlockEntity(pos) instanceof ScrollBlockEntity scrollBlockEntity)) {
                return super.onUse(state, world, pos, player, hit);
            }
            if (!player.isSneaking()) {
                player.openHandledScreen(scrollBlockEntity);
            } else {
                boolean rolled = state.get(ROLLED);
                world.setBlockState(pos, state.with(ROLLED, !rolled));
                world.playSound(player, pos, SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                ModCustomAttachedData data = player.getAttachedOrSet(ModAttachmentTypes.SOUL_ATTACH, ModCustomAttachedData.DEFAULT);
                player.setAttached(ModAttachmentTypes.SOUL_ATTACH, data.setSoul(1000));
            }
            ModCustomAttachedData data = player.getAttachedOrSet(ModAttachmentTypes.SOUL_ATTACH, ModCustomAttachedData.DEFAULT);
            player.setAttached(ModAttachmentTypes.SOUL_ATTACH, data.removeSoul(data, 10));
            Wingcrafter.LOGGER.info(String.valueOf(player.getAttachedOrSet(ModAttachmentTypes.SOUL_ATTACH,  ModCustomAttachedData.DEFAULT)));
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
            if (!world.isClient()) {
                ItemStack itemStack = new ItemStack(this);
                itemStack.applyComponentsFrom(blockEntity.createComponentMap());
                itemStack.set(DataComponentTypes.BLOCK_STATE, new BlockStateComponent(Map.of())
                        .with(WRITTEN, state.get(WRITTEN))
                        .with(ROLLED, state.get(ROLLED))
                        .with(TITLED, state.get(TITLED))
                );
                itemStack.set(ModDataComponentTypes.SCROLL_TEXT, scrollBlockEntity.getText());
                itemStack.set(ModDataComponentTypes.SCROLL_TITLE, scrollBlockEntity.getTitle());

                ItemEntity itemEntity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);

                if (state.get(SEALED)) {
                    ItemEntity sealEntity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(ModItems.SEAL));
                    sealEntity.setToDefaultPickupDelay();
                    world.spawnEntity(sealEntity);
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
            if (!world.isClient()) {
                String text = itemStack.get(ModDataComponentTypes.SCROLL_TEXT);
                String title = itemStack.get(ModDataComponentTypes.SCROLL_TITLE);
                if (!Objects.equals(text, "")) {
                    scrollBlockEntity.setText(text);
                }
                if (!Objects.equals(title, "")) {
                    scrollBlockEntity.setTitle(title);
                }
            }
        }
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(ModItems.SEAL)) {
            boolean sealed = state.get(SEALED);
            if (!sealed) {
                world.setBlockState(pos, state.with(SEALED, true));
                world.playSound(player, pos, SoundEvents.BLOCK_HONEY_BLOCK_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (player.getGameMode() != GameMode.CREATIVE) {
                    stack.decrement(1);
                    player.getInventory().markDirty();
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
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
        builder.add(TITLED);
        builder.add(SEALED);
    }
}
