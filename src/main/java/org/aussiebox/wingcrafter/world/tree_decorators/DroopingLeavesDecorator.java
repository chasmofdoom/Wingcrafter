package org.aussiebox.wingcrafter.world.tree_decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.aussiebox.wingcrafter.Wingcrafter;

import java.util.HashSet;
import java.util.Set;

public class DroopingLeavesDecorator extends TreeDecorator {
    public static final MapCodec<DroopingLeavesDecorator> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((treeDecorator) -> treeDecorator.probability),
                    Codec.intRange(0, 16).fieldOf("exclusion_radius_xz").forGetter((treeDecorator) -> treeDecorator.exclusionRadiusXZ),
                    Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter((treeDecorator) -> treeDecorator.exclusionRadiusY),
                    BlockStateProvider.TYPE_CODEC.fieldOf("drooping_block_provider").forGetter((treeDecorator) -> treeDecorator.blockProvider),
                    Codec.intRange(0, 16).fieldOf("min_droop_length").forGetter((treeDecorator) -> treeDecorator.minDroopLength),
                    Codec.intRange(0, 16).fieldOf("max_droop_length").forGetter((treeDecorator) -> treeDecorator.maxDroopLength),
                    BlockStateProvider.TYPE_CODEC.fieldOf("end_block_provider").forGetter((treeDecorator) -> treeDecorator.endBlockProvider),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("end_block_probability").forGetter((treeDecorator) -> treeDecorator.endBlockProbability),
                    Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter((treeDecorator) -> treeDecorator.requiredEmptyBlocks))
            .apply(instance, DroopingLeavesDecorator::new));
    protected final float probability;
    protected final int exclusionRadiusXZ;
    protected final int exclusionRadiusY;
    protected final BlockStateProvider blockProvider;
    protected final int minDroopLength;
    protected final int maxDroopLength;
    protected final BlockStateProvider endBlockProvider;
    protected final float endBlockProbability;
    protected final int requiredEmptyBlocks;

    public DroopingLeavesDecorator(float probability, int exclusionRadiusXZ, int exclusionRadiusY, BlockStateProvider blockProvider, int minDroopLength, int maxDroopLength, BlockStateProvider endBlockProvider, float endBlockProbability, int requiredEmptyBlocks) {
        this.probability = probability;
        this.exclusionRadiusXZ = exclusionRadiusXZ;
        this.exclusionRadiusY = exclusionRadiusY;
        this.blockProvider = blockProvider;
        this.minDroopLength = minDroopLength;
        this.maxDroopLength = maxDroopLength;
        this.endBlockProvider = endBlockProvider;
        this.endBlockProbability = endBlockProbability;
        this.requiredEmptyBlocks = requiredEmptyBlocks;
    }

    public void generate(TreeDecorator.Generator generator) {
        Set<BlockPos> set = new HashSet<>();
        Random random = generator.getRandom();

        for(BlockPos blockPos : Util.copyShuffled(generator.getLeavesPositions(), random)) {
            Direction direction = Direction.DOWN;
            BlockPos blockPos2 = blockPos.offset(direction);
            if (!set.contains(blockPos2) && random.nextFloat() < this.probability && this.meetsRequiredEmptyBlocks(generator, blockPos, direction)) {
                BlockPos blockPos3 = blockPos2.add(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ);
                BlockPos blockPos4 = blockPos2.add(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ);

                for(BlockPos blockPos5 : BlockPos.iterate(blockPos3, blockPos4)) {
                    set.add(blockPos5.toImmutable());
                }

                int droopLength = random.nextBetween(this.minDroopLength, this.maxDroopLength);
                BlockPos placePos = blockPos2;

                for (int i = 0; i < droopLength; i++) {
                    generator.replace(placePos, this.blockProvider.get(random, placePos));
                    placePos = placePos.down();
                }

                if (random.nextFloat() < this.endBlockProbability) {
                    generator.replace(placePos, this.endBlockProvider.get(random, placePos));
                }
            }
        }

    }

    private boolean meetsRequiredEmptyBlocks(TreeDecorator.Generator generator, BlockPos pos, Direction direction) {
        for(int i = 1; i <= this.requiredEmptyBlocks; ++i) {
            BlockPos blockPos = pos.offset(direction, i);
            if (!generator.isAir(blockPos)) {
                return false;
            }
        }

        return true;
    }

    protected TreeDecoratorType<?> getType() {
        return Wingcrafter.DROOPING_LEAVES_TREE_DECORATOR;
    }
}
