package com.a_day650.hardthencreepersmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;
import java.util.function.Function;

public class StageBreakingBlock extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    private final Function<Integer, List<ItemStack>> dropProvider;
    private final Function<Integer, Float> hardnessProvider;
    private final Function<Integer, ToolRequirement> toolRequirementProvider;
    private final int maxStage;
    private static final java.util.Map<BlockPos, Player> lastBreakPlayers = new java.util.WeakHashMap<>();

    // 工具要求类
    public static class ToolRequirement {
        private final net.minecraftforge.common.ToolAction toolAction;
        private final int minLevel;
        private final int maxLevel;
        private final float speedMultiplier;
        private final Function<Integer, List<ItemStack>> instantBreakDrops;

        public ToolRequirement(net.minecraftforge.common.ToolAction toolAction, int minLevel, int maxLevel, float speedMultiplier) {
            this(toolAction, minLevel, maxLevel, speedMultiplier, null);
        }

        public ToolRequirement(net.minecraftforge.common.ToolAction toolAction, int minLevel, int maxLevel, float speedMultiplier,
                               Function<Integer, List<ItemStack>> instantBreakDrops) {
            this.toolAction = toolAction;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.speedMultiplier = speedMultiplier;
            this.instantBreakDrops = instantBreakDrops;
        }

        public List<ItemStack> getInstantBreakDrops(int currentStage) {
            if (instantBreakDrops != null) {
                return instantBreakDrops.apply(currentStage);
            }
            return null; // 返回null表示使用默认行为
        }

        public boolean matches(ItemStack tool, Player player) {
            if (toolAction != null && !tool.canPerformAction(toolAction)) {
                return false;
            }
            if (tool.getItem() instanceof TieredItem tieredItem) {
                int toolLevel = tieredItem.getTier().getLevel();
                return toolLevel >= minLevel && toolLevel <= maxLevel;
            }
            return minLevel == 0; // 如果没有等级要求，任何工具都可以
        }

        public boolean shouldBreakInstantly(ItemStack tool, Player player) {
            // 如果工具等级超过最大等级，直接破坏
            if (tool.getItem() instanceof TieredItem tieredItem) {
                return tieredItem.getTier().getLevel() > maxLevel;
            }
            return false;
        }

        // 在 ToolRequirement 类中添加这个方法
        public boolean isToolSufficient(ItemStack tool, Player player) {
            if (tool.getItem() instanceof TieredItem tieredItem) {
                int toolLevel = tieredItem.getTier().getLevel();
                return toolLevel >= minLevel; // 只要工具等级 >= 最低要求就足够
            }
            return minLevel == 0;
        }

        public boolean isToolTooGood(ItemStack tool, Player player) {
            if (tool.getItem() instanceof TieredItem tieredItem) {
                int toolLevel = tieredItem.getTier().getLevel();
                return toolLevel > maxLevel; // 工具等级超过最大要求
            }
            return false;
        }

        public float getSpeedMultiplier() {
            return speedMultiplier;
        }
    }

    public StageBreakingBlock(Properties properties, int maxStage,
                              Function<Integer, List<ItemStack>> dropProvider,
                              Function<Integer, Float> hardnessProvider,
                              Function<Integer, ToolRequirement> toolRequirementProvider) {
        super(properties);
        this.maxStage = maxStage;
        this.dropProvider = dropProvider;
        this.hardnessProvider = hardnessProvider;
        this.toolRequirementProvider = toolRequirementProvider;
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    public StageBreakingBlock(Properties properties, int maxStage,
                              Function<Integer, List<ItemStack>> dropProvider) {
        this(properties, maxStage, dropProvider, (stage) -> 3.0f, (stage) -> new ToolRequirement(null, 0, 10, 1.0f));
    }

    public StageBreakingBlock(Properties properties, int maxStage,
                              Function<Integer, List<ItemStack>> dropProvider,
                              Function<Integer, Float> hardnessProvider) {
        this(properties, maxStage, dropProvider, hardnessProvider, (stage) -> new ToolRequirement(null, 0, 10, 1.0f));
    }

    public IntegerProperty getStageProperty() {
        return STAGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    public int getCurrentStage(BlockState state) {
        return state.getValue(STAGE);
    }

    public boolean isFinalStage(BlockState state) {
        return getCurrentStage(state) >= maxStage;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        int stage = getCurrentStage(state);
        ToolRequirement requirement = toolRequirementProvider.apply(stage);
        ItemStack tool = player.getMainHandItem();
        return requirement.matches(tool, player);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // 存储最后破坏这个方块的玩家
        lastBreakPlayers.put(pos, player);
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            ServerLevel serverLevel = (ServerLevel) level;
            int currentStage = getCurrentStage(state);

            // 使用存储的玩家信息，而不是查找最近的玩家
            Player player = lastBreakPlayers.get(pos);
            boolean canHarvest = false;
            boolean shouldBreakInstantly = false;
            ToolRequirement requirements = toolRequirementProvider.apply(currentStage);

            if (player != null) {
                ItemStack tool = player.getMainHandItem();
                ToolRequirement requirement = toolRequirementProvider.apply(currentStage);
                canHarvest = requirement.matches(tool, player);
                shouldBreakInstantly = requirement.shouldBreakInstantly(tool, player);

                // 清理存储的玩家信息
                lastBreakPlayers.remove(pos);
            }

            // 如果工具等级超过最大等级，直接破坏整个方块
            if (shouldBreakInstantly) {
                List<ItemStack> instantDrops = requirements.getInstantBreakDrops(currentStage);

                if (instantDrops != null) {
                    // 使用自定义掉落物
                    for (ItemStack item : instantDrops) {
                        popResource(serverLevel, pos, item.copy());
                    }
                } else {
                    // 默认行为：掉落所有阶段的物品
                    for (int i = currentStage; i <= maxStage; i++) {
                        List<ItemStack> drops = dropProvider.apply(i);
                        if (drops != null && !drops.isEmpty()) {
                            for (ItemStack item : drops) {
                                popResource(serverLevel, pos, item.copy());
                            }
                        }
                    }
                }
                super.onRemove(state, level, pos, newState, isMoving);
                return;
            }

            // 只有使用正确工具才能掉落物品
            if (canHarvest) {
                List<ItemStack> drops = dropProvider.apply(currentStage);
                if (drops != null && !drops.isEmpty()) {
                    for (ItemStack item : drops) {
                        popResource(serverLevel, pos, item.copy());
                    }
                }
            }

            // 如果还有下一阶段，转换为下一阶段
            if (currentStage < maxStage) {
                BlockState nextState = state.setValue(STAGE, currentStage + 1);
                level.setBlock(pos, nextState, 3);
                return;
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of();
    }
}