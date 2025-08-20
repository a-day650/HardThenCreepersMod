package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class PlayerStatEventHandler {

    private static final UUID HEALTH_ATTACK_MODIFIER_ID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final UUID HEALTH_SPEED_MODIFIER_ID = UUID.fromString("b2c3d4e5-f678-9012-bcde-f23456789012");

    // 存储上次的血量百分比，避免每tick都更新
    private static final java.util.WeakHashMap<Player, Float> lastHealthPercentages = new java.util.WeakHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide()) {
            Player player = event.player;

            // 获取玩家当前血量和最大血量
            float currentHealth = player.getHealth();
            float maxHealth = player.getMaxHealth();

            // 避免除以零错误
            if (maxHealth <= 0) return;

            float healthPercentage = currentHealth / maxHealth;

            // 检查血量百分比是否有变化，避免不必要的更新
            Float lastPercentage = lastHealthPercentages.get(player);
            if (lastPercentage != null && Math.abs(lastPercentage - healthPercentage) < 0.01f) {
                return; // 血量百分比变化很小，跳过更新
            }

            lastHealthPercentages.put(player, healthPercentage);

            // 根据血量百分比设置属性
            applyHealthBasedAttributes(player, healthPercentage);
        }
    }

    private static void applyHealthBasedAttributes(Player player, float healthPercentage) {
        AttributeInstance attackAttribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (attackAttribute == null || speedAttribute == null) return;

        double attackBonus = 0;
        double speedBonus = 0;

        // 根据血量设置不同的惩罚
        if (healthPercentage < 0.4f) {
            attackBonus = -1.0;  // 血量低于40%，攻击力-1
            speedBonus = -0.1;   // 血量低于40%，速度-10%
        } else if (healthPercentage < 0.8f) {
            attackBonus = -0.5;  // 血量低于80%，攻击力-0.5
            speedBonus = -0.05;  // 血量低于80%，速度-5%
        }

        // 处理攻击力修饰器
        handleAttributeModifier(attackAttribute, HEALTH_ATTACK_MODIFIER_ID,
                "health_attack_bonus", attackBonus,
                AttributeModifier.Operation.ADDITION);

        // 处理速度修饰器
        handleAttributeModifier(speedAttribute, HEALTH_SPEED_MODIFIER_ID,
                "health_speed_bonus", speedBonus,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    // 安全的修饰器处理方法
    private static void handleAttributeModifier(AttributeInstance attribute, UUID modifierId,
                                                String name, double amount,
                                                AttributeModifier.Operation operation) {
        if (attribute == null) return;

        // 先移除旧的修饰器
        attribute.removeModifier(modifierId);

        // 只有在有实际效果时才添加新修饰器
        if (amount != 0) {
            AttributeModifier modifier = new AttributeModifier(
                    modifierId,
                    name,
                    amount,
                    operation
            );

            // 安全地添加修饰器
            try {
                attribute.addTransientModifier(modifier);
            } catch (IllegalArgumentException e) {
                // 如果添加失败，记录错误但不崩溃
                HardThenCreepersMod.LOGGER.warn("Failed to add attribute modifier: {}", e.getMessage());
            }
        }
    }

    // 添加玩家退出时的清理
    @SubscribeEvent
    public static void onPlayerLogout(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            lastHealthPercentages.remove(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerDimensionChange(net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            lastHealthPercentages.remove(event.getEntity());
        }
    }
}