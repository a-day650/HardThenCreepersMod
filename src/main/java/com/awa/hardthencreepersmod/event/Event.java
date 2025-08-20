package com.awa.hardthencreepersmod.event;

import com.awa.hardthencreepersmod.HardThenCreepersMod;
import com.awa.hardthencreepersmod.blockentity.WetRawBrickBlockEntity;
import com.awa.hardthencreepersmod.client.screen.CrashScreen;

import com.awa.hardthencreepersmod.item.ModItems;
import com.awa.hardthencreepersmod.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class Event {
    private static final Component[] WARNINGS = {
            Component.translatable("message.hardthencreepersmod.fuck_w1"),
            Component.translatable("message.hardthencreepersmod.fuck_w2"),
            Component.translatable("message.hardthencreepersmod.fuck_w3")
    };
    private final static Map<UUID, Integer> playerWarningCount = new HashMap<>();
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        // 降低非斧头破坏原木的速度
        if (event.getState().is(BlockTags.LOGS)) {
            if (event.getEntity().getMainHandItem().is(ItemTags.AXES)) {
                event.setNewSpeed(event.getOriginalSpeed() * 0.4f); // 斧头仍然较慢
            }
            else if (event.getEntity().getMainHandItem().is(ModItems.FLINT_HATCHET.get())) {
                event.setNewSpeed(event.getOriginalSpeed() * 0.3f); // 允许破坏，但速度较慢
            }
            else {
                // 空手/其他物品时造成伤害并取消挖掘进度
                if (!event.getEntity().getMainHandItem().is(ItemTags.TOOLS)) {
                    event.getEntity().hurt(event.getEntity().damageSources().generic(), 1.0f); // 造成1点伤害
                    event.setCanceled(true); // 取消挖掘进度
                } else {
                    event.setNewSpeed(event.getOriginalSpeed() * 0.2f); // 徒手或其他工具更慢
                }
            }
        }
        // 降低非铲子破坏草方块的速度（可选）
        if (event.getState().is(Blocks.GRASS_BLOCK) || event.getState().is(Blocks.DIRT)) {
            if (event.getEntity().getMainHandItem().is(ItemTags.SHOVELS)) {
                event.setNewSpeed(event.getOriginalSpeed() * 0.3f);
            } else {
                event.setNewSpeed(event.getOriginalSpeed() * 0.12f);
            }
        }
        if (event.getState().is(ModTags.Blocks.MINEABLE_SLOW) || event.getState().is(Tags.Blocks.ORES)) {
            if (event.getEntity().getMainHandItem().is(ItemTags.PICKAXES)) {
                event.setNewSpeed(event.getOriginalSpeed() * 0.3f);
            } else {
                event.setNewSpeed(event.getOriginalSpeed() * 0.12f);
            }
        }
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        String message = event.getRawText(); // 获取原始消息（不含玩家名前缀）
        Player player = event.getPlayer();    // 获取发送消息的玩家
        UUID playerId = player.getUUID();

        // 示例：检测消息是否为指令
        if (message.startsWith("awa i fuck you!!")) {
            int count = playerWarningCount.getOrDefault(playerId, 0);
            if (count < 3) {
                player.displayClientMessage(WARNINGS[count], true);
                playerWarningCount.put(playerId, count + 1);
            }
            else {
                playerWarningCount.clear();
                crashClient(player);
            }
            event.setCanceled(true); // 可选：阻止消息广播到公共聊天
        }
    }

    public static void crashClient(Player player) {
        player.sendSystemMessage(Component.literal("§4[SEVERE] StackOverflowError in ClientThread").withStyle(ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal("§cat com.awa.hardthencreepersmod.event.Event.crashClient(Event.java:99)"));

        // 2. 屏幕抖动 + 黑屏（持续3秒）
        if (player.level().isClientSide) {
            Minecraft.getInstance().setScreen(new CrashScreen()); // 自定义黑屏界面
        }

        // 3. 播放崩溃音效
        player.playSound(SoundEvents.GLASS_BREAK, 1.0f, 0.5f);
    }

    @SubscribeEvent
    public static void onAnvilRepair(AnvilUpdateEvent event) {
        ItemStack tool = event.getLeft();
        if (tool.getTag() != null && tool.getTag().getBoolean("PoorCrafting")) {
            event.setCanceled(true);
            if (event.getPlayer() != null) {
                event.getPlayer().displayClientMessage(
                        Component.literal("§c工艺缺陷的工具无法修复！"),
                        true
                );
            }
        }
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockEvent.BreakEvent event) {
        if (event.getState().is(Blocks.GRAVEL)) {
            if (event.getLevel() instanceof Level level && !level.isClientSide) {
                // 只在非AIR时生成实体
                ItemStack stack = Math.random() > 0.9 ?
                        new ItemStack(ModItems.PEBBLE.get(), 1 + event.getPlayer().getRandom().nextInt(0, 2)) :
                        ItemStack.EMPTY;  // 使用EMPTY而非AIR

                if (!stack.isEmpty()) {  // 关键检查
                    level.addFreshEntity(new ItemEntity(
                            level,
                            event.getPos().getX() + 0.5,
                            event.getPos().getY() + 0.5,
                            event.getPos().getZ() + 0.5,
                            stack
                    ));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        BlockState clickedBlock = event.getLevel().getBlockState(event.getPos());
        if (heldItem.is(ItemTags.AXES) && clickedBlock.is(BlockTags.LOGS)) {
            event.setCanceled(true);
        }
    }

    public static void applyDurabilityPenalty(Player player, ItemStack tool, int quality) {
        int finalCost = 15 - quality;
        // 应用损耗
        tool.hurtAndBreak(finalCost, player, p -> {
            p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
        });
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        if (!result.is(ModTags.Items.POOR_CRAFTING)) return;

        Player player = event.getEntity();
        int qualityLevel = Math.min(10, Math.max(0, player.experienceLevel / 2));

        CompoundTag tag = result.getOrCreateTag();
        tag.putInt("CraftLevel", qualityLevel);

        // 仅存储语言键
        tag.putString("QualityKey", getQualityLangKey(qualityLevel));
        tag.putString("QualityColor", getQualityColor(qualityLevel));
    }

    private static String getQualityLangKey(int level) {
        return switch (Math.min(level, 2)) { // 限制为3个质量等级
            case 0 -> "tooltip.quality.poor";
            case 1 -> "tooltip.quality.rough";
            default -> "tooltip.quality.average";
        };
    }

    private static String getQualityColor(int level) {
        return switch (Math.min(level, 2)) {
            case 0 -> "DARK_RED";
            case 1 -> "RED";
            default -> "YELLOW";
        };
    }


    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        CompoundTag tag = event.getItemStack().getTag();
        if (tag == null || !tag.contains("QualityKey")) return;

        // 动态获取翻译文本
        Component qualityText = Component.translatable(tag.getString("QualityKey"))
                .withStyle(ChatFormatting.valueOf(tag.getString("QualityColor")));

        event.getToolTip().add(Component.translatable("tooltip.quality")
                .append(": ").append(qualityText));

        // 添加耐久惩罚提示
        int penalty = 15 - tag.getInt("CraftLevel");
        event.getToolTip().add(Component.translatable("tooltip.durability_penalty", penalty)
                .withStyle(ChatFormatting.RED));

        if (tag.getInt("CraftLevel") < 2) {
            event.getToolTip().add(Component.translatable("tooltip.unrepairable")
                    .withStyle(ChatFormatting.DARK_RED));
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