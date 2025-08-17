package com.awa.hardthencreepersmod.thirst;

import com.awa.hardthencreepersmod.event.ThirstShakeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

public class ThirstImpl implements IThirst {
    private int thirst = 20;
    private static final int MAX_THIRST = 20;
    private static final Component LOW_THIRST_WARNING = Component.translatable("message.hardthencreepersmod.low_thirst");
    private static int tickChange = 0;

    @Override
    public int getThirstLevel() {
        return thirst;
    }

    @Override
    public void setThirstLevel(int level) {
        this.thirst = Math.min(Math.max(level, 0), MAX_THIRST); // 确保在0-MAX_THIRST范围内
    }

    @Override
    public void addThirst(int amount) {
        this.thirst = Math.min(Math.max(thirst + amount, 0), MAX_THIRST);
    }

    // 每Tick更新逻辑（仅口渴值）
    public void tick(Player player) {
        if (player.isCreative()) return;
        // 简单的口渴值减少逻辑（示例）
        if(player.tickCount % (23 * 20) == 0) {
            MinecraftForge.EVENT_BUS.post(new ThirstShakeEvent(3, 1.0f));
            addThirst(-1);
        }
        if (thirst <= 0) {
            MinecraftForge.EVENT_BUS.post(new ThirstShakeEvent(10, 2.5f));
            if (player.tickCount % (2 * 20) == 0) {
                DamageSource damageSource = new DamageSource(
                        player.level().registryAccess()
                                .registryOrThrow(Registries.DAMAGE_TYPE)
                                .getHolderOrThrow(ResourceKey.create(
                                        Registries.DAMAGE_TYPE,
                                        new ResourceLocation("hardthencreepersmod", "thirst")
                                ))
                );
                player.hurt(damageSource, 1.0f);
            }
        }
        if (thirst <= 7) {
            MinecraftForge.EVENT_BUS.post(new ThirstShakeEvent(10, 0.8f));
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,  // 缓慢效果
                    20,  // 持续时间
                    1,    // 等级（0=基础，1=减速20%，2=减速40%...）
                    true, // 是否隐藏粒子
                    true   // 是否显示图标
            ));
            if (thirst <= 3) {
                MinecraftForge.EVENT_BUS.post(new ThirstShakeEvent(10, 1.6f));
                player.addEffect(new MobEffectInstance(
                        MobEffects.CONFUSION, // 反胃效果
                        200,    // 持续时间（10秒）
                        0,      // 强度（0=基础效果）
                        true,  // 是否显示粒子
                        true    // 是否显示图标
                ));
                if (player.tickCount % 5 * 20 == 0) {
                    player.displayClientMessage(LOW_THIRST_WARNING, true);
                }
            }
        }
        if (player.isSprinting() && player.getSpeed() > 0.1f) {
            MinecraftForge.EVENT_BUS.post(new ThirstShakeEvent(10, 0.7f));
            tickChange ++;
            if (tickChange >= 5*20) {addThirst(-1);tickChange = 0;}
        }
    }
}