package com.a_day650.hardthencreepersmod.client;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiltEffectHandler {

    private static final Map<UUID, GuiltEffectData> playerEffects = new HashMap<>();
    private static final float DARKNESS_STRENGTH = 0.7f; // 黑暗效果强度（0-1，1为完全黑暗）

    private static class GuiltEffectData {
        float currentDarkness;
        int effectDuration;
        long effectStartTime;
        long recoveryStartTime = -1;
        boolean isInRecoveryPhase = false;

        GuiltEffectData(int duration) {
            this.currentDarkness = 0f;
            this.effectDuration = duration;
            this.effectStartTime = System.currentTimeMillis();
        }

        // 计算效果进度（基于毫秒）
        float getEffectProgress(long currentTime) {
            if (effectDuration == -1) return 1.0f; // 无限效果立即最大

            long elapsed = currentTime - effectStartTime;
            long totalTime = effectDuration * 50L;
            float progress = (float) elapsed / totalTime;
            return Math.min(progress, 1.0f);
        }

        // 检查是否应该开始恢复阶段（效果过半）
        boolean shouldStartRecovery(long currentTime) {
            if (effectDuration == -1) return false; // 无限效果不自动恢复

            float progress = getEffectProgress(currentTime);
            return progress >= 0.5f && !isInRecoveryPhase;
        }

        // 开始恢复阶段
        void startRecoveryPhase() {
            if (!isInRecoveryPhase) {
                isInRecoveryPhase = true;
                recoveryStartTime = System.currentTimeMillis();
            }
        }

        // 计算恢复进度
        float getRecoveryProgress(long currentTime) {
            if (!isInRecoveryPhase) return 0f;
            long elapsed = currentTime - recoveryStartTime;
            // 恢复时间应该是效果总时间的一半
            long recoveryTime = (effectDuration * 50L);
            return Math.min((float) elapsed / recoveryTime, 1.0f);
        }

        boolean isFullyRecovered(long currentTime) {
            return isInRecoveryPhase && getRecoveryProgress(currentTime) >= 0.99f;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.level != null) {
                UUID playerId = mc.player.getUUID();
                MobEffectInstance effect = mc.player.getEffect(ModEffects.GUILT.get());
                long currentTime = System.currentTimeMillis();

                if (effect != null) {
                    GuiltEffectData data = playerEffects.get(playerId);
                    if (data == null) {
                        data = new GuiltEffectData(effect.getDuration());
                        playerEffects.put(playerId, data);
                    }

                    // 更新持续时间
                    data.effectDuration = effect.getDuration();

                    // 检查是否应该开始恢复阶段
                    if (data.shouldStartRecovery(currentTime)) {
                        data.startRecoveryPhase();
                    }

                    if (data.isInRecoveryPhase) {
                        // 恢复阶段
                        float recoveryProgress = data.getRecoveryProgress(currentTime);
                        float easedRecovery = easeOutCubic(recoveryProgress);

                        // 恢复亮度
                        data.currentDarkness = DARKNESS_STRENGTH * (1f - easedRecovery);
                    } else {
                        // 效果增强阶段
                        float effectProgress = data.getEffectProgress(currentTime);
                        // 限制进度不超过0.5（一半）
                        float limitedProgress = Math.min(effectProgress, 0.5f) * 2f; // 映射到0-1范围
                        float easedProgress = easeInOutCubic(limitedProgress);

                        // 应用黑暗效果
                        data.currentDarkness = DARKNESS_STRENGTH * easedProgress;
                    }

                } else if (playerEffects.containsKey(playerId)) {
                    // 效果已结束，完全恢复
                    GuiltEffectData data = playerEffects.get(playerId);

                    if (!data.isInRecoveryPhase) {
                        data.startRecoveryPhase();
                    }

                    float recoveryProgress = data.getRecoveryProgress(currentTime);
                    float easedRecovery = easeOutCubic(recoveryProgress);

                    // 完全恢复亮度
                    data.currentDarkness = DARKNESS_STRENGTH * (1f - easedRecovery);

                    if (data.isFullyRecovered(currentTime)) {
                        playerEffects.remove(playerId);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            UUID playerId = mc.player.getUUID();
            GuiltEffectData data = playerEffects.get(playerId);

            if (data != null && data.currentDarkness > 0) {
                GuiGraphics guiGraphics = event.getGuiGraphics();
                int width = event.getWindow().getGuiScaledWidth();
                int height = event.getWindow().getGuiScaledHeight();
                int alpha = (int)(data.currentDarkness * 180); // 透明度

                // 在屏幕上绘制黑色半透明矩形
                guiGraphics.fill(0, 0, width, height, (alpha << 24) | 0x000000);
            }
        }
    }

    private static float easeInOutCubic(float t) {
        return t < 0.5f ? 4f * t * t * t : 1f - (float)Math.pow(-2f * t + 2f, 3) / 2f;
    }

    private static float easeOutCubic(float t) {
        return (float) (1 - Math.pow(1 - t, 3));
    }
}