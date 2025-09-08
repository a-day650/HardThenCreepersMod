package com.a_day650.hardthencreepersmod.event;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.item.ModItems;
import com.a_day650.hardthencreepersmod.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HardThenCreepersMod.MODID)
public class Event {
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
}