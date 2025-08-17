package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.capability.ThirstProvider;
import com.awa.hardthencreepersmod.network.NetworkHandler;
import com.awa.hardthencreepersmod.network.SyncThirstPacket;
import com.awa.hardthencreepersmod.thirst.ThirstImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class ThirstEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide()) {
            event.player.getCapability(ThirstProvider.THIRST_CAP).ifPresent(thirst -> {
                // 强制类型转换前检查
                if(thirst instanceof ThirstImpl) {
                    ((ThirstImpl)thirst).tick(event.player);
                    // 更频繁的同步（每2 tick）
                    if(event.player.tickCount % 2 == 0) {
                        NetworkHandler.sendToPlayer(
                                new SyncThirstPacket(thirst.getThirstLevel()),
                                (ServerPlayer)event.player
                        );
                    }
                }
            });
        }
    }
}