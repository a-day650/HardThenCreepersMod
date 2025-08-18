package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.item.WaterBagItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WaterPurificationHandler {
    @SubscribeEvent
    public static void onItemInFire(EntityEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();

            if (stack.getItem() instanceof WaterBagItem &&
                    WaterBagItem.getWaterAmount(stack) > 0) {

                // 使用精确检测
                if (isInFire(itemEntity)) {
                    // 使用物品实体的精确坐标播放音效
                    WaterBagItem.purifyWater(stack, itemEntity.level(),
                            BlockPos.containing(itemEntity.position()));

                    // 强制更新物品实体
                    itemEntity.setItem(stack);

                    // 视觉反馈
                    spawnParticles(itemEntity);
                }
            }
        }
    }

    private static void spawnParticles(ItemEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE,
                    entity.getX(), entity.getY() + 0.5, entity.getZ(),
                    5, 0.2, 0.1, 0.2, 0.01);
        }
    }

    private static boolean isInFire(ItemEntity entity) {
        Level level = entity.level();
        // 获取精确坐标（包含小数部分）
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        // 检测精确坐标所在的方块状态
        BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));

        // 检查当前方块和下方方块（针对悬挂火焰）
        return level.getBlockState(pos).is(Blocks.FIRE) ||
                level.getBlockState(pos.below()).is(Blocks.FIRE) ||
                level.getBlockState(pos).is(Blocks.LAVA) ||
                level.getBlockState(pos.below()).is(Blocks.CAMPFIRE);
    }
}