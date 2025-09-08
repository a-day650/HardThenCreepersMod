package com.a_day650.hardthencreepersmod.entity.ai;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HorseKickGoal extends Goal {
    private final Horse horse;
    private int kickCooldown = 0;
    private boolean hasKicked = false;

    public HorseKickGoal(Horse horse) {
        this.horse = horse;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // 只在被攻击且冷却结束时触发
        LivingEntity attacker = horse.getLastHurtByMob();
        return attacker != null && kickCooldown <= 0 && horse.distanceToSqr(attacker) < 16.0D;
    }

    @Override
    public boolean canContinueToUse() {
        // 允许在冷却期间继续运行，这样冷却计时器可以正常递减
        return kickCooldown > 0 || (horse.getLastHurtByMob() != null && !hasKicked);
    }

    @Override
    public void start() {
        // 立即停止移动
        horse.getNavigation().stop();
        hasKicked = false;
    }

    @Override
    public void tick() {
        // 先处理冷却计时
        if (kickCooldown > 0) {
            kickCooldown--;
            return;
        }

        LivingEntity attacker = horse.getLastHurtByMob();
        if (attacker == null || !attacker.isAlive()) {
            return;
        }

        if (!hasKicked) {
            // 直接计算背对攻击者的方向
            Vec3 toAttacker = attacker.position().subtract(horse.position());
            float targetYaw = (float) Math.toDegrees(Math.atan2(toAttacker.z, toAttacker.x)) + 180.0F;

            // 设置朝向（背对攻击者）
            horse.setYRot(targetYaw);
            horse.yHeadRot = targetYaw;
            horse.yBodyRot = targetYaw;

            // 执行踢击
            performKick(attacker);
        }
    }

    private void performKick(LivingEntity attacker) {
        // 检查距离
        if (horse.distanceToSqr(attacker) < 12.0D) { // 4格范围内
            // 踢击伤害
            attacker.hurt(horse.damageSources().mobAttack(horse), 10.0F);

            // 击退效果（向后踢）
            Vec3 kickDirection = attacker.position().subtract(horse.position()).normalize();
            double knockbackStrength = 0.8D;
            attacker.setDeltaMovement(
                    attacker.getDeltaMovement().add(
                            kickDirection.x * knockbackStrength,
                            0.6D,
                            kickDirection.z * knockbackStrength
                    )
            );

            horse.playSound(SoundEvents.HORSE_STEP_WOOD, 1.0F, 0.8F);
            horse.playSound(SoundEvents.HORSE_STEP_WOOD, 1.0F, 1.2F);
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