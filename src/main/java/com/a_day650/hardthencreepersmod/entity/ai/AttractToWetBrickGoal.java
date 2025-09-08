package com.a_day650.hardthencreepersmod.entity.ai;

import com.a_day650.hardthencreepersmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class AttractToWetBrickGoal extends Goal {
    private final PathfinderMob mob;
    private final Level level;
    private BlockPos targetPos;
    private final double speedModifier;
    private int tryTicks;

    public AttractToWetBrickGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.level = mob.level();
        this.speedModifier = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE)); // 这是一个移动目标
    }

    @Override
    public boolean canUse() {
        // 10%的几率尝试寻找湿泥砖
        if (this.mob.getRandom().nextFloat() > 0.1F) {
            return false;
        }

        // 在周围16格范围内寻找湿泥砖
        this.targetPos = findNearbyWetBrick();
        return this.targetPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        // 继续使用条件：目标位置存在且尝试时间不超过100tick
        return this.targetPos != null &&
                this.tryTicks < 100 &&
                this.level.getBlockState(this.targetPos).is(ModBlocks.WET_RAW_BRICK.get());
    }

    @Override
    public void start() {
        this.tryTicks = 0;
        // 开始向目标移动
        this.mob.getNavigation().moveTo(
                this.targetPos.getX(),
                this.targetPos.getY() + 0.5,
                this.targetPos.getZ(),
                this.speedModifier
        );
    }

    @Override
    public void stop() {
        this.targetPos = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.tryTicks++;
    }

    private BlockPos findNearbyWetBrick() {
        BlockPos mobPos = this.mob.blockPosition();
        int range = 16; // 搜索范围

        for (BlockPos pos : BlockPos.withinManhattan(mobPos, range, 4, range)) {
            BlockState state = this.level.getBlockState(pos);
            if (state.is(ModBlocks.WET_RAW_BRICK.get())) {
                return pos;
            }
        }
        return null;
    }
}