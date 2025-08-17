package com.awa.hardthencreepersmod.network;

import com.awa.hardthencreepersmod.capability.ThirstProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncThirstPacket {
    private final int thirst;

    // 主构造函数（用于创建数据包）
    public SyncThirstPacket(int thirst) {
        this.thirst = thirst;
    }

    // 解码构造函数（用于从字节缓冲读取）
    public SyncThirstPacket(FriendlyByteBuf buf) {
        this.thirst = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(thirst);
    }

    public static void handle(SyncThirstPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) { // 客户端处理
                Minecraft.getInstance().player.getCapability(ThirstProvider.THIRST_CAP)
                        .ifPresent(cap -> cap.setThirstLevel(msg.thirst));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public int getThirstLevel() {
        return thirst;
    }
}