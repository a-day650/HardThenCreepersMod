package com.a_day650.hardthencreepersmod.block;

import com.a_day650.hardthencreepersmod.init.ModBlockEntities;
import com.a_day650.hardthencreepersmod.blockentity.WetRawBrickBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WetRawBrickBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty GROWTH_STAGE = IntegerProperty.create("growth_stage", 0, 3);

    // 标准砖块碰撞箱
    private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 5.0D, 12.0D, 2.0D, 10.0D);

    public WetRawBrickBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(GROWTH_STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, GROWTH_STAGE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return rotateShape(direction);
    }

    private VoxelShape rotateShape(Direction direction) {
        switch (direction) {
            case NORTH:
                return SHAPE; // 默认方向
            case SOUTH:
                return Block.box(4.0D, 0.0D, 6.0D, 13.0D, 2.0D, 11.0D);
            case EAST:
                return Block.box(6.0D, 0.0D, 3.0D, 11.0D, 2.0D, 12.0D);
            case WEST:
                return Block.box(5.0D, 0.0D, 4.0D, 10.0D, 2.0D, 13.0D);
            default:
                return SHAPE;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return rotateShape(direction);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WetRawBrickBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.WET_RAW_BRICK.get(), WetRawBrickBlockEntity::tick);
    }
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && !player.isCreative()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WetRawBrickBlockEntity) {
                int growthStage = state.getValue(GROWTH_STAGE);
                if (growthStage == 3) {
                    // 阶段3被破坏时掉落生砖
                    ((WetRawBrickBlockEntity) blockEntity).dropRawBrickItem();
                } else {
                    // 阶段0-2掉落粘土球
                    WetRawBrickBlockEntity.dropClayBalls(level, pos);
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);

        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            // 检测实体是否真的踩在方块上
            AABB entityBounds = entity.getBoundingBox();
            AABB blockBounds = new AABB(pos).move(0, 0.8, 0); // 方块顶部区域

            if (entityBounds.intersects(blockBounds)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof WetRawBrickBlockEntity wetBrick) {
                    // 添加冷却时间避免连续触发
                    if (level.getGameTime() % 10 == 0) { // 每10tick检测一次
                        wetBrick.onSteppedOn();
                    }
                }
            }
        }
    }
}