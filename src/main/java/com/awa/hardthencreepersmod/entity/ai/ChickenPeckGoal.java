package com.awa.hardthencreepersmod.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Chicken;

import java.util.EnumSet;

public class ChickenPeckGoal extends Goal {
    private final Chicken chicken;
    private int peckCooldown = 0;
    private boolean hasPecked = false;

    public ChickenPeckGoal(Chicken chicken) {
        this.chicken = chicken;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return chicken.getLastHurtByMob() != null && peckCooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        // 冷却期间继续运行，直到冷却结束
        return peckCooldown > 0 || !hasPecked;
    }

    @Override
    public void start() {
        // 停止移动，专注于啄击
        chicken.getNavigation().stop();
        hasPecked = false;
    }

    @Override
    public void tick() {
        if (peckCooldown > 0) {
            peckCooldown--;
            return;
        }

        LivingEntity attacker = chicken.getLastHurtByMob();
        if (attacker == null || !attacker.isAlive()) {
            return;
        }

        if (!hasPecked) {
            // 面向攻击者
            chicken.getLookControl().setLookAt(attacker);

            // 执行啄击
            performPeck(attacker);
        }
    }

    private void performPeck(LivingEntity attacker) {
        if (chicken.distanceToSqr(attacker) < 4.0D) { // 2格范围内
            // 啄击！
            attacker.hurt(chicken.damageSources().mobAttack(chicken), 1.0F);
            chicken.playSound(net.minecraft.sounds.SoundEvents.CHICKEN_HURT, 1.0F, 1.5F);
        }

        // 无论是否啄中，都设置冷却
        peckCooldown = 20; // 1秒冷却
        hasPecked = true;
    }

    @Override
    public void stop() {
        hasPecked = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}