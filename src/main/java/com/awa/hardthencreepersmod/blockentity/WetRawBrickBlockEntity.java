package com.awa.hardthencreepersmod.blockentity;

import com.awa.hardthencreepersmod.block.WetRawBrickBlock;
import com.awa.hardthencreepersmod.init.ModBlockEntities;
import com.awa.hardthencreepersmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WetRawBrickBlockEntity extends BlockEntity {
    private long placedTick = -1;
    private static final int TICKS_PER_STAGE = 24000/4; // 20分钟

    public WetRawBrickBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WET_RAW_BRICK.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("PlacedTick", placedTick);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        placedTick = tag.getLong("PlacedTick");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WetRawBrickBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        if (isBlockUnsupported(level, pos)) {
            spawnFallingParticles(level, pos);
            dropClayBalls(level, pos);
            level.destroyBlock(pos, false); // 破坏方块但不掉落物品
            return;
        }

        if (blockEntity.placedTick == -1) {
            blockEntity.placedTick = level.getGameTime();
            blockEntity.setChanged();
            return;
        }

        long elapsedTicks = level.getGameTime() - blockEntity.placedTick;
        int targetStage = (int) (elapsedTicks / TICKS_PER_STAGE);
        targetStage = Math.min(targetStage, 3); // 0-3 四个阶段

        int currentStage = state.getValue(WetRawBrickBlock.GROWTH_STAGE);
        if (targetStage != currentStage) {
            BlockState newState = state.setValue(WetRawBrickBlock.GROWTH_STAGE, targetStage);
            level.setBlockAndUpdate(pos, newState);
            blockEntity.setChanged();
        }
        if(targetStage==3){
            if (level.getGameTime()%10==0){
                spawnAdvancedBlackSmoke(level, pos);
            }
        }
    }
    public static void spawnAdvancedBlackSmoke(Level level, BlockPos pos) {
        double centerX = pos.getX() + 0.5D;
        double centerY = pos.getY() + 0.9D;
        double centerZ = pos.getZ() + 0.5D;

        // 产生多个粒子形成烟雾效果
        for (int i = 0; i < 3; i++) {
            double offsetX = (level.random.nextDouble() - 0.5D) * 0.6D;
            double offsetZ = (level.random.nextDouble() - 0.5D) * 0.6D;

            level.addParticle(ParticleTypes.SMOKE,
                    centerX + offsetX,
                    centerY,
                    centerZ + offsetZ,
                    (level.random.nextDouble() - 0.5D) * 0.02D,  // 随机X速度
                    0.05D + level.random.nextDouble() * 0.03D,    // 上升速度
                    (level.random.nextDouble() - 0.5D) * 0.02D); // 随机Z速度
        }
    }
    private static boolean isBlockUnsupported(Level level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        // 如果下方是空气、水、岩浆等非固体方块，则认为悬空
        return !belowState.isSolidRender(level, belowPos) &&
                !belowState.is(Blocks.SOUL_SAND) && // 排除一些特殊方块
                !belowState.is(Blocks.SOUL_SOIL) &&
                belowState.getFluidState().isEmpty(); // 排除流体
    }

    // 掉落粘土球
    public static void dropClayBalls(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            // 掉落1-2个粘土球
            int clayCount = 1 + level.random.nextInt(2);
            ItemStack clayStack = new ItemStack(Items.CLAY_BALL, clayCount);

            // 在方块位置生成物品实体
            ItemEntity itemEntity = new ItemEntity(level,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    clayStack);

            // 给物品一点随机速度
            itemEntity.setDeltaMovement(
                    (level.random.nextDouble() - 0.5D) * 0.1D,
                    0.2D,
                    (level.random.nextDouble() - 0.5D) * 0.1D
            );

            level.addFreshEntity(itemEntity);
        }
    }

    // 掉落时的粒子效果
    private static void spawnFallingParticles(Level level, BlockPos pos) {
        if (level.isClientSide) {
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.CLOUD,
                        pos.getX() + level.random.nextDouble(),
                        pos.getY() + level.random.nextDouble() * 0.5D,
                        pos.getZ() + level.random.nextDouble(),
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void onSteppedOn() {
        if (level != null && !level.isClientSide) {
            // 有100%几率被踩坏
            if (level.random.nextFloat() < 1.0F) {
                tryBreakBlock();
            }
        }
    }

    private void tryBreakBlock() {
        if (level == null || level.isClientSide) return;
        dropClayBalls(level, worldPosition);
        // 破坏方块
        level.destroyBlock(worldPosition, false);
        level.playLocalSound(
                worldPosition.getX() + 0.5D,  // X坐标
                worldPosition.getY() + 0.5D,  // Y坐标
                worldPosition.getZ() + 0.5D,  // Z坐标
                SoundEvents.SLIME_BLOCK_BREAK, // 声音事件
                SoundSource.BLOCKS,           // 音源类型
                0.8F,                         // 音量 (0.0-1.0)
                1.0F,                         // 音调 (0.5-2.0)
                false                         // 是否距离延迟
        );
        spawnBreakParticles(level, worldPosition);
    }

    // 掉落生砖物品
    public void dropRawBrickItem() {
        // 假设你有一个生砖物品，需要先注册
        ItemStack rawBrickStack = new ItemStack(ModItems.RAW_BRICK.get(), 1);

        ItemEntity itemEntity = new ItemEntity(level,
                worldPosition.getX() + 0.5D,
                worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D,
                rawBrickStack);

        level.addFreshEntity(itemEntity);
    }

    // 破坏粒子效果
    private static void spawnBreakParticles(Level level, BlockPos pos) {
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.CLOUD,
                        pos.getX() + level.random.nextDouble(),
                        pos.getY() + level.random.nextDouble(),
                        pos.getZ() + level.random.nextDouble(),
                        (level.random.nextDouble() - 0.5D) * 0.2D,
                        0.1D + level.random.nextDouble() * 0.1D,
                        (level.random.nextDouble() - 0.5D) * 0.2D);
            }
        }
    }
}