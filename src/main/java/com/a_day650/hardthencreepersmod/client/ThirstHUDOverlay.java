package com.a_day650.hardthencreepersmod.client;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.capability.ThirstProvider;
import com.a_day650.hardthencreepersmod.effect.ModEffects;
import com.a_day650.hardthencreepersmod.event.ThirstShakeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ThirstHUDOverlay {
    private static final ResourceLocation THIRST_ICONS =
            new ResourceLocation("hardthencreepersmod", "textures/gui/thirst_icons.png");
    private static final ResourceLocation THIRST_ICONS_IS_THIRST =
            new ResourceLocation("hardthencreepersmod", "textures/gui/thirst_icons_is_thirst.png");
    private static final int ICON_DISPLAY_WIDTH = 9;
    private static final int ICON_DISPLAY_HEIGHT = 9;
    private static final int TEXTURE_TILE_SIZE = 9;
    private static final int TEXTURE_FULL_HEIGHT = 27;
    private static int remainingShakeTicks = 0;
    private static float currentShakeIntensity = 1.0f;
    private static final Random RANDOM = new Random();

    // 添加最后已知口渴值
    private static int lastKnownThirstLevel = 20;
    private static Player lastPlayer = null;

    @SubscribeEvent
    public static void onThirstShake(ThirstShakeEvent event) {
        remainingShakeTicks = event.getDurationTicks();
        currentShakeIntensity = event.getIntensity();
    }

    public static final IGuiOverlay HUD_THIRST = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null||mc.options.hideGui||mc.gameMode == null||player.isSpectator()||player.isCreative()) {
            return;
        }

        // 检查是否是新的玩家实例
        if (lastPlayer != player) {
            lastPlayer = player;
            lastKnownThirstLevel = 20; // 重置为默认值
        }

        // 使用final数组作为容器
        final int[] thirstLevelContainer = new int[]{lastKnownThirstLevel};

        // 尝试获取当前口渴值
        try {
            player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
                int currentLevel = thirst.getThirstLevel();
                lastKnownThirstLevel = currentLevel; // 更新最后已知值
                thirstLevelContainer[0] = currentLevel; // 更新容器中的值
            });
        } catch (Exception e) {
            HardThenCreepersMod.LOGGER.debug("Failed to get thirst capability, using last known value: {}", lastKnownThirstLevel);
            // 使用最后已知值继续渲染
        }

        // 计算绘制位置（保持原位置，食物栏下方）
        int x = screenWidth / 2 + 90;
        int y = screenHeight - 59;

        boolean shouldShake = remainingShakeTicks > 0 || thirstLevelContainer[0] <= 6;
        if (remainingShakeTicks > 0) remainingShakeTicks--;

        float intensity = currentShakeIntensity;

        // 绘制10个水滴图标
        for (int i = 0; i < 10; i++) {
            int iconX = x - i * 8 - 9;
            int iconY = player.getAirSupply() != player.getMaxAirSupply() ? y : y + 10;

            if (shouldShake) {
                float offsetY = (RANDOM.nextFloat() * 2 - 1) * 2 * intensity;
                iconY += (int) offsetY;
            } else {
                iconY = player.getAirSupply() != player.getMaxAirSupply() ? y : y + 10;
            }

            int threshold = i * 2;

            if (thirstLevelContainer[0] > threshold + 1) {
                drawIcon(guiGraphics, iconX, iconY, 0, player);
            } else if (thirstLevelContainer[0] == threshold + 1) {
                drawIcon(guiGraphics, iconX, iconY, 9, player);
            } else {
                drawIcon(guiGraphics, iconX, iconY, 18, player);
            }
        }
    };

    private static void drawIcon(GuiGraphics guiGraphics, int x, int y, int vOffset, Player player) {
        guiGraphics.blit(
                player.hasEffect(ModEffects.THIRST.get()) ? THIRST_ICONS_IS_THIRST : THIRST_ICONS,
                x, y,
                ICON_DISPLAY_WIDTH,
                ICON_DISPLAY_HEIGHT,
                0,
                vOffset,
                TEXTURE_TILE_SIZE,
                TEXTURE_TILE_SIZE,
                TEXTURE_TILE_SIZE,
                TEXTURE_FULL_HEIGHT
        );
    }
}