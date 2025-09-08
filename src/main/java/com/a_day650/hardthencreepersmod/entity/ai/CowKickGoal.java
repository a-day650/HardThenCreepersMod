package com.a_day650.hardthencreepersmod.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class CowKickGoal extends Goal {
    private final Cow cow;
    private int kickCooldown = 0;
    private boolean hasKicked = false;

    public CowKickGoal(Cow cow) {
        this.cow = cow;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // 只在被攻击且冷却结束时触发
        LivingEntity attacker = cow.getLastHurtByMob();
        return attacker != null && kickCooldown <= 0 && cow.distanceToSqr(attacker) < 16.0D;
    }

    @Override
    public boolean canContinueToUse() {
        // 冷却期间继续运行，直到冷却结束
        return kickCooldown > 0 || !hasKicked;
    }

    @Override
    public void start() {
        // 立即停止移动
        cow.getNavigation().stop();
        hasKicked = false;
    }

    @Override
    public void tick() {
        if (kickCooldown > 0) {
            kickCooldown--;
            return;
        }

        LivingEntity attacker = cow.getLastHurtByMob();
        if (attacker == null || !attacker.isAlive()) {
            return;
        }

        if (!hasKicked) {
            // 直接计算背对攻击者的方向
            Vec3 toAttacker = attacker.position().subtract(cow.position());
            float targetYaw = (float) Math.toDegrees(Math.atan2(toAttacker.z, toAttacker.x)) + 180.0F;

            // 设置牛的朝向（背对攻击者）
            cow.setYRot(targetYaw);
            cow.yHeadRot = targetYaw;
            cow.yBodyRot = targetYaw;

            // 执行踢击
            performKick(attacker);
        }
    }

    private void performKick(LivingEntity attacker) {
        // 检查距离
        if (cow.distanceToSqr(attacker) < 12.0D) { // 4格范围内
            // 踢击伤害
            attacker.hurt(cow.damageSources().mobAttack(cow), 8.0F);

            // 击退效果（向后踢）
            Vec3 kickDirection = attacker.position().subtract(cow.position()).normalize();
            double knockbackStrength = 0.7D;
            attacker.setDeltaMovement(
                    attacker.getDeltaMovement().add(
                            kickDirection.x * knockbackStrength,
                            0.5D,
                            kickDirection.z * knockbackStrength
                    )
            );

            cow.playSound(net.minecraft.sounds.SoundEvents.COW_HURT, 1.0F, 0.8F);
            cow.playSound(net.minecraft.sounds.SoundEvents.HORSE_STEP_WOOD, 1.0F, 1.2F);
        }
        // 无论是否踢中，都设置冷却
        kickCooldown = 40; // 2秒冷却
        hasKicked = true;
    }

    @Override
    public void stop() {
        hasKicked = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}