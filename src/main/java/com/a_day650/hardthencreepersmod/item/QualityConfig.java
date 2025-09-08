// QualityConfig.java
package com.a_day650.hardthencreepersmod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.IntFunction;

public class QualityConfig {
    private final QualityTier[] tiers;
    private final IntFunction<Integer> qualityCalculator;

    public QualityConfig(QualityTier[] tiers, IntFunction<Integer> qualityCalculator) {
        this.tiers = tiers;
        this.qualityCalculator = qualityCalculator;
    }

    // 修复：使用具体的品质等级数组，而不是引用实例字段
    public static final QualityConfig DEFAULT = new QualityConfig(
            new QualityTier[] {
                    new QualityTier("tooltip.quality.poor", net.minecraft.ChatFormatting.DARK_RED, 0, 15),
                    new QualityTier("tooltip.quality.rough", net.minecraft.ChatFormatting.RED, 2, 10),
                    new QualityTier("tooltip.quality.average", net.minecraft.ChatFormatting.YELLOW, 4, 5)
            },
            playerLevel -> {
                // 修复：使用具体的数组而不是实例字段
                QualityTier[] defaultTiers = new QualityTier[] {
                        new QualityTier("tooltip.quality.poor", net.minecraft.ChatFormatting.DARK_RED, 0, 15),
                        new QualityTier("tooltip.quality.rough", net.minecraft.ChatFormatting.RED, 2, 10),
                        new QualityTier("tooltip.quality.average", net.minecraft.ChatFormatting.YELLOW, 4, 5)
                };

                for (int i = defaultTiers.length - 1; i >= 0; i--) {
                    if (playerLevel >= defaultTiers[i].minPlayerLevel()) {
                        return i;
                    }
                }
                return 0;
            }
    );

    // 或者更简单的默认计算逻辑
    public static final QualityConfig SIMPLE_DEFAULT = new QualityConfig(
            new QualityTier[] {
                    new QualityTier("tooltip.quality.poor", net.minecraft.ChatFormatting.DARK_RED, 0, 15),
                    new QualityTier("tooltip.quality.rough", net.minecraft.ChatFormatting.RED, 2, 10),
                    new QualityTier("tooltip.quality.average", net.minecraft.ChatFormatting.YELLOW, 4, 5)
            },
            playerLevel -> Math.min(playerLevel / 2, 2) // 简单计算：每2级提升品质，最大2级
    );

    public int calculateQuality(int playerLevel) {
        return qualityCalculator.apply(playerLevel);
    }

    public QualityTier getTier(int qualityLevel) {
        int index = Math.min(Math.max(qualityLevel, 0), tiers.length - 1);
        return tiers[index];
    }

    public int getDurabilityPenalty(int qualityLevel) {
        return getTier(qualityLevel).durabilityPenalty();
    }

    public void addQualityTooltip(int qualityLevel, List<Component> tooltip) {
        QualityTier tier = getTier(qualityLevel);

        MutableComponent qualityText = Component.translatable(tier.langKey())
                .withStyle(tier.color());

        tooltip.add(Component.translatable("tooltip.quality")
                .append(": ").append(qualityText));

        tooltip.add(Component.translatable("tooltip.durability_penalty", tier.durabilityPenalty())
                .withStyle(net.minecraft.ChatFormatting.RED));

        if (qualityLevel < tiers.length - 1) {
            tooltip.add(Component.translatable("tooltip.unrepairable")
                    .withStyle(net.minecraft.ChatFormatting.DARK_RED));
        }
    }

    public int getMaxQuality() {
        return tiers.length - 1;
    }
}