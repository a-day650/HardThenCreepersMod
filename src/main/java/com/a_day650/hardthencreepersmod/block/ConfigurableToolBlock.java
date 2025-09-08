package com.a_day650.hardthencreepersmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.common.ToolAction;

import java.util.List;

public abstract class ConfigurableToolBlock extends Block {
    private final ToolRequirement toolRequirement;
    private final Block partialBreakBlock;

    public record ToolRequirement(ToolAction toolAction, int minLevel, int minDestroyLevel, int maxDorpLevel,
                                  float speedMultiplier) {
    }

    public ConfigurableToolBlock(Properties properties, ToolRequirement requirement, Block partialBlock) {
        super(properties);
        this.toolRequirement = requirement;
        this.partialBreakBlock = partialBlock;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.isClientSide || state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }

        // 获取最近的玩家和工具
        Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false);
        if (player == null) {
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }

        ItemStack tool = player.getMainHandItem();
        boolean canBreakFully = canBreakFully(tool, player);

        if (!canBreakFully || !(getToolLevel(tool) >= toolRequirement.minDestroyLevel)) {
            // 工具等级不足，放置部分破坏方块并掉落部分物品
            level.setBlock(pos, partialBreakBlock.defaultBlockState(), 3);
            if (!(getToolLevel(tool) >= toolRequirement.maxDorpLevel)){getTooLittleDrops().forEach(drop -> popResource((ServerLevel) level, pos, drop));}else {getPartialDrops().forEach(drop -> popResource((ServerLevel) level, pos, drop));}
        } else {
            // 工具等级足够，正常掉落
            getFullDrops().forEach(drop -> popResource((ServerLevel) level, pos, drop));
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of();
    }

    private boolean canBreakFully(ItemStack tool, Player player) {
        return isSuitableTool(tool, player)&&getToolLevel(tool) >= toolRequirement.minLevel;
    }

    private boolean isSuitableTool(ItemStack tool, Player player) {
        if (toolRequirement.toolAction != null && !tool.canPerformAction(toolRequirement.toolAction)) {
            return false;
        }
        return true;
    }

    private int getToolLevel(ItemStack tool) {
        if (tool.getItem() instanceof TieredItem tieredItem) {
            return tieredItem.getTier().getLevel();
        }
        return 0;
    }

    protected abstract List<ItemStack> getPartialDrops();
    protected abstract List<ItemStack> getFullDrops();
    protected abstract List<ItemStack> getTooLittleDrops();
}