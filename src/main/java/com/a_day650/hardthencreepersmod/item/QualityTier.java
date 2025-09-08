// QualityTier.java
package com.a_day650.hardthencreepersmod.item;

import net.minecraft.ChatFormatting;

public record QualityTier(
        String langKey,
        ChatFormatting color,
        int minPlayerLevel,
        int durabilityPenalty
) {
    // 默认品质等级配置
    public static final QualityTier POOR = new QualityTier(
            "tooltip.quality.poor",
            ChatFormatting.DARK_RED,
            0,
            15
    );

    public static final QualityTier ROUGH = new QualityTier(
            "tooltip.quality.rough",
            ChatFormatting.RED,
            2,
            10
    );

    public static final QualityTier AVERAGE = new QualityTier(
            "tooltip.quality.average",
            ChatFormatting.YELLOW,
            4,
            5
    );

    public static final QualityTier[] DEFAULT_TIERS = {POOR, ROUGH, AVERAGE};
}