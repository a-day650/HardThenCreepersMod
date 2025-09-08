package com.a_day650.hardthencreepersmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 基础配置项
    public static final ForgeConfigSpec.BooleanValue ENABLE_FEATURE;
    public static final ForgeConfigSpec.IntValue DIFFICULTY_LEVEL;

    public static final ForgeConfigSpec SPEC;

    // 运行时值
    public static boolean enableFeature;
    public static int difficultyLevel;

    static {
        // 基础配置
        ENABLE_FEATURE = BUILDER
                .comment("是否启用核心功能")
                .define("enableFeature", true);

        DIFFICULTY_LEVEL = BUILDER
                .comment("难度等级 (1-5)")
                .defineInRange("difficulty", 3, 1, 5);

        BUILDER.push("Debug Settings");
        BUILDER.pop();

        // 只在最后构建一次 SPEC
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(net.minecraftforge.fml.event.config.ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            // 配置加载时更新变量
            enableFeature = ENABLE_FEATURE.get();
            difficultyLevel = DIFFICULTY_LEVEL.get();
        }
    }
}