package com.awa.hardthencreepersmod.client;

import com.awa.hardthencreepersmod.capability.ThirstProvider;
import com.awa.hardthencreepersmod.effect.ModEffects;
import com.awa.hardthencreepersmod.event.ThirstShakeEvent;
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

    @SubscribeEvent
    public static void onThirstShake(ThirstShakeEvent event) {
        remainingShakeTicks = event.getDurationTicks();
        currentShakeIntensity = event.getIntensity();
    }

    public static final IGuiOverlay HUD_THIRST = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.options.hideGui || mc.gameMode == null || mc.player.isCreative()) {
            return; // 如果玩家在水中，则不显示口渴HUD
        }

        // 获取玩家口渴值
        player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
            int thirstLevel = thirst.getThirstLevel();

            // 计算绘制位置（保持原位置，食物栏下方）
            int x = screenWidth / 2 + 90;
            int y = screenHeight - 59;

            boolean shouldShake = remainingShakeTicks > 0 || thirstLevel <= 6;
            if (remainingShakeTicks > 0) remainingShakeTicks--;

            float intensity = currentShakeIntensity;

            // 绘制10个水滴图标
            for (int i = 0; i < 10; i++) {
                int iconX = x - i * 8 - 9;
                int iconY = player.getAirSupply()!=player.getMaxAirSupply() ? y : y+10;

                if (shouldShake) {
                    float offsetY = (RANDOM.nextFloat() * 2 - 1) * 2 * intensity;
                    iconY += (int) offsetY;
                }
                else {
                    iconY = player.getAirSupply()!=player.getMaxAirSupply() ? y : y+10;
                }

                int threshold = i * 2;

                if (thirstLevel > threshold + 1) {
                    drawIcon(guiGraphics, iconX, iconY, 0, player);
                } else if (thirstLevel == threshold + 1) {
                    drawIcon(guiGraphics, iconX, iconY, 9, player);
                } else {
                    drawIcon(guiGraphics, iconX, iconY, 18, player);
                }
            }
        });
    };

    private static void drawIcon(GuiGraphics guiGraphics, int x, int y, int vOffset,Player player) {
        guiGraphics.blit(
                player.hasEffect(ModEffects.THIRST.get())?THIRST_ICONS_IS_THIRST:THIRST_ICONS,
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