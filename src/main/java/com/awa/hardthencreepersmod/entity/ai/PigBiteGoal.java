package com.awa.hardthencreepersmod.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class PigBiteGoal extends Goal {
    private final Pig pig;
    private final Random random = new Random();
    private int biteCooldown = 0;
    private int randomBiteTimer = 0;
    private boolean hasBitten = false;

    public PigBiteGoal(Pig pig) {
        this.pig = pig;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        resetRandomTimer();
    }

    private void resetRandomTimer() {
        randomBiteTimer = 2000 + random.nextInt(2000);
    }

    @Override
    public boolean canUse() {
        // 被攻击时或者随机时间到且附近有玩家
        return (pig.getLastHurtByMob() != null && biteCooldown <= 0) ||
                (randomBiteTimer <= 0 && biteCooldown <= 0 &&
                        !pig.level().getEntitiesOfClass(LivingEntity.class,
                                new AABB(pig.blockPosition()).inflate(2.0D)).isEmpty());
    }

    @Override
    public boolean canContinueToUse() {
        // 冷却期间继续运行，直到冷却结束
        return biteCooldown > 0 || !hasBitten;
    }

    @Override
    public void start() {
        // 停止移动，专注于啃咬
        pig.getNavigation().stop();
        hasBitten = false;
    }

    @Override
    public void tick() {
        if (randomBiteTimer > 0) {
            randomBiteTimer--;
        }

        if (biteCooldown > 0) {
            biteCooldown--;
            return;
        }

        if (!hasBitten) {
            // 查找附近的玩家或攻击者
            LivingEntity target = pig.getLastHurtByMob();
            if (target == null) {
                // 随机啃咬附近的生物
                List<LivingEntity> entities = pig.level().getEntitiesOfClass(LivingEntity.class,
                        new AABB(pig.blockPosition()).inflate(2.0D),
                        entity -> entity != pig); // 排除自己
                if (!entities.isEmpty()) {
                    target = entities.get(random.nextInt(entities.size()));
                }
            }

            if (target != null && pig.distanceToSqr(target) < 4.0D) {
                // 执行啃咬
                performBite(target);
            } else {
                // 没有找到目标，结束行为
                hasBitten = true;
            }
        }
    }

    private void performBite(LivingEntity target) {
        // 啃咬！
        target.hurt(pig.damageSources().mobAttack(pig), 1.5F);
        pig.playSound(net.minecraft.sounds.SoundEvents.PIG_AMBIENT, 1.0F, 0.8F);

        // 设置冷却
        biteCooldown = 30; // 1.5秒冷却
        hasBitten = true;
        resetRandomTimer();
    }

    @Override
    public void stop() {
        hasBitten = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}