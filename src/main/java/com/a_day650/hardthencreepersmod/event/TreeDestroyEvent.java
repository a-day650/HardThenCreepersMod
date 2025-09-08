package com.a_day650.hardthencreepersmod.event;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;

import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class TreeDestroyEvent {
    @SubscribeEvent
    public static void onBlockDrops(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.is(BlockTags.LOGS) && event.getPlayer().getMainHandItem().is(ItemTags.TOOLS)) {
            if (isStrippedLog(state)) return;
            event.setCanceled(true);

            if (event.getLevel() instanceof Level level) {
                BlockState strippedState = getStrippedLogState(state);
                if (strippedState != null) {
                    // 保持原木方向
                    if (state.hasProperty(RotatedPillarBlock.AXIS)) {
                        strippedState = strippedState.setValue(RotatedPillarBlock.AXIS,
                                state.getValue(RotatedPillarBlock.AXIS));
                    }
                    level.setBlock(event.getPos(), strippedState, Block.UPDATE_ALL);
                }

                // 掉落树皮
                level.addFreshEntity(new ItemEntity(
                        level,
                        event.getPos().getX() + 0.5,
                        event.getPos().getY() + 0.5,
                        event.getPos().getZ() + 0.5,
                        new ItemStack(ModItems.BARK.get(), 1)
                ));
            }
        }
    }
    // 辅助方法：获取对应的去皮方块
    public static BlockState getStrippedLogState(BlockState originalState) {
        if (originalState.is(Blocks.OAK_LOG)) return Blocks.STRIPPED_OAK_LOG.defaultBlockState();
        if (originalState.is(Blocks.SPRUCE_LOG)) return Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState();
        if (originalState.is(Blocks.BIRCH_LOG)) return Blocks.STRIPPED_BIRCH_LOG.defaultBlockState();
        if (originalState.is(Blocks.JUNGLE_LOG)) return Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState();
        if (originalState.is(Blocks.ACACIA_LOG)) return Blocks.STRIPPED_ACACIA_LOG.defaultBlockState();
        if (originalState.is(Blocks.DARK_OAK_LOG)) return Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState();
        if (originalState.is(Blocks.MANGROVE_LOG)) return Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState();
        if (originalState.is(Blocks.CHERRY_LOG)) return Blocks.STRIPPED_CHERRY_LOG.defaultBlockState();
        if (originalState.is(Blocks.CRIMSON_STEM)) return Blocks.STRIPPED_CRIMSON_STEM.defaultBlockState();
        if (originalState.is(Blocks.WARPED_STEM)) return Blocks.STRIPPED_WARPED_STEM.defaultBlockState();
        return null;
    }
    public static boolean isStrippedLog(BlockState state) {
        return state.is(Blocks.STRIPPED_OAK_LOG) ||
                state.is(Blocks.STRIPPED_SPRUCE_LOG) ||
                state.is(Blocks.STRIPPED_BIRCH_LOG) ||
                state.is(Blocks.STRIPPED_JUNGLE_LOG) ||
                state.is(Blocks.STRIPPED_ACACIA_LOG) ||
                state.is(Blocks.STRIPPED_DARK_OAK_LOG) ||
                state.is(Blocks.STRIPPED_MANGROVE_LOG) ||
                state.is(Blocks.STRIPPED_CHERRY_LOG) ||
                state.is(Blocks.STRIPPED_CRIMSON_STEM) ||
                state.is(Blocks.STRIPPED_WARPED_STEM);
    }
}
