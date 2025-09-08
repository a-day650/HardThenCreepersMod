package com.a_day650.hardthencreepersmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class GuiltEffect extends MobEffect {
    public GuiltEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B4513); // 棕色表示罪恶感
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !entity.level().isClientSide()) {
            // 服务器端只需要管理效果逻辑
            // 视觉效果在客户端处理
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // 每tick都执行
    }
}