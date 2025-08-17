// NetworkHandler.java
package com.awa.hardthencreepersmod.network;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HardThenCreepersMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        // 注册口渴值同步包
        INSTANCE.registerMessage(packetId++,
                SyncThirstPacket.class,
                SyncThirstPacket::encode,
                SyncThirstPacket::new,
                SyncThirstPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    // 发送给特定玩家
    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    // 发送给所有追踪实体的玩家
    public static void sendToTrackingPlayers(Object msg, ServerPlayer excluded) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> excluded), msg);
    }
}