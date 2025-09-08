package com.a_day650.hardthencreepersmod.effect;

import com.a_day650.hardthencreepersmod.capability.ThirstProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ThirstEffect extends MobEffect {
    public ThirstEffect() {
        super(MobEffectCategory.HARMFUL, 0x00ED73);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return;
        if (player.isCreative()) return;

        int tickInterval = Math.max(70, 100 / (amplifier + 1));

        if (player.tickCount % tickInterval == 0) {
            player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
                // 每次固定减少1点，或根据等级调整
                int decreaseAmount = Math.max(1, (amplifier + 1) / 2); // 每2级多减1点
                if (-decreaseAmount >= 0){decreaseAmount=1;}
                thirst.addThirst(-decreaseAmount);
            });
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // 每tick都检查（实际执行频率由内部逻辑控制）
    }
}