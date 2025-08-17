package com.awa.hardthencreepersmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 基础配置项（示例）
    public static final ForgeConfigSpec.BooleanValue ENABLE_FEATURE = BUILDER
            .comment("是否启用核心功能")
            .define("enableFeature", true);

    public static final ForgeConfigSpec.IntValue DIFFICULTY_LEVEL = BUILDER
            .comment("难度等级 (1-5)")
            .defineInRange("difficulty", 3, 1, 5);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // 运行时值
    public static boolean enableFeature;
    public static int difficultyLevel;

    @SubscribeEvent
    public static void onLoad(net.minecraftforge.fml.event.config.ModConfigEvent event) {
        // 配置加载时更新变量
        enableFeature = ENABLE_FEATURE.get();
        difficultyLevel = DIFFICULTY_LEVEL.get();
    }
}