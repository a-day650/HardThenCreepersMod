package com.awa.hardthencreepersmod.client;

import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "hardthencreepersmod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 客户端初始化代码（如有需要）
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(
                VanillaGuiOverlay.PLAYER_HEALTH.id(), // 使用Vanilla定义的常量
                "thirst",
                ThirstHUDOverlay.HUD_THIRST
        );
    }

    @SubscribeEvent
    public static void registerEventListeners(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ThirstHUDOverlay.class);
    }
}