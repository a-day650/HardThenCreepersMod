package com.a_day650.hardthencreepersmod.event;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import com.a_day650.hardthencreepersmod.block.WetRawBrickBlock;
import com.a_day650.hardthencreepersmod.blockentity.WetRawBrickBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class BrickCollectionEvent {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // 检测右键湿砖块
        if (state.is(ModBlocks.WET_RAW_BRICK.get())) {
            // 取消原有事件（防止放置方块等）
            event.setCanceled(true);

            // 获取生长阶段
            int growthStage = state.getValue(WetRawBrickBlock.GROWTH_STAGE);

            WetRawBrickBlockEntity.brickCollection(growthStage,pos,level);
        }
    }
}
